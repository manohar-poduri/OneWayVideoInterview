package com.example.onewayvideointerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static int VIDEO_REQUEST = 101;
    private Uri videoUri = null;
    private VideoView videoView;

    private TextView questions,countTime;
    private Button nextQuestion;
    private int questionCount = 1;
    private int Totalquestions = 1;
    private int qid = 1;
    private HashMap<Integer,Integer> listQid = new HashMap<Integer,Integer>();
    private CountDownTimer ct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.video_view);
        nextQuestion =  findViewById(R.id.nextQuestion);
        questions = findViewById(R.id.questions);
        countTime = findViewById(R.id.countTime);



        ct = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
//                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                countTime.setText(f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                countTime.setText("00:00");
                displayQuestion();
            }
        };


        final Query questionToDisplaySize = FirebaseDatabase.getInstance()
                .getReference()
                .child("questions")
                .child("size");

        questionToDisplaySize.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", String.valueOf(dataSnapshot));
                //QuizQuestion quix = question.getValue(QuizQuestion.class);
                Integer mp = dataSnapshot.getValue(Integer.class);
                Totalquestions = mp;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ItemDetail", "onCancelled", databaseError.toException());
            }
        });


        displayQuestion();

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayQuestion();

            }
        });


    }


    private int ifexistID(int a){
        if(listQid.get(a) == null && a!=0){
            listQid.put(a,a);
            return a;
        }
        return -1;
    }

    private void displayQuestion() {



        final Query questionToDisplay = FirebaseDatabase.getInstance()
                .getReference()
                .child("questions")
                .child(String.valueOf(qid));

        ct.cancel();
        ct.start();
        qid+=1;

        // Generate
        // a random
        // integer from 1 to 30


        questionToDisplay.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("One", String.valueOf(dataSnapshot));

                if (questionCount <= Totalquestions) {
                    //QuizQuestion quix = question.getValue(QuizQuestion.class);
                    String mp = dataSnapshot.getValue(String.class);
                    questions.setText(mp);
                    questionCount += 1;

                } else {
                    finish(); // Close Activity
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ItemDetail", "onCancelled", databaseError.toException());
            }
        });


    }



    public void startVideo(View view) {

        videoView.setVideoURI(videoUri);
        videoView.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_REQUEST && resultCode == RESULT_OK) {

            videoUri = data.getData();
        }
    }

    public void captureVideo(View view) {

        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (videoIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(videoIntent,VIDEO_REQUEST);
        }
    }
}
