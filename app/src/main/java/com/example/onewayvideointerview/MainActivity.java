package com.example.onewayvideointerview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 102;
    public static final int VIDEO_PICK_CAMERA_CODE = 101;
    public String[] cameraPermission;

    private Uri videoUri = null;
    private VideoView videoView;

    private TextView questions,countTime;
    private Button nextQuestion;

    private int questionCount = 1;
    private int Totalquestions = 1;
    private int qid = 1;
    private HashMap<Integer,Integer> listQid = new HashMap<Integer,Integer>();
    private CountDownTimer ct;

    private FirebaseAuth auth;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.video_view);
        nextQuestion =  findViewById(R.id.nextQuestion);
        questions = findViewById(R.id.questions);
        countTime = findViewById(R.id.countTime);

        auth = FirebaseAuth.getInstance();

        cameraPermission = new String[]{Manifest.permission.CAMERA};

        ct = new CountDownTimer(60000, 1000) {
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

                videoView.stopPlayback();

                videoView.suspend();

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
                .child(String.valueOf(qid)).child("one");

        ct.cancel();
        ct.start();
        qid+=1;

        /*Random randomGenerator = new Random();
        randomGenerator.nextInt(qid);
*/
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
                    Toast.makeText(MainActivity.this, "Questions have completed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ItemDetail", "onCancelled", databaseError.toException());
            }
        });


    }

    public void nextPage(View view) {
        Intent intent = new Intent(MainActivity.this, RatingEachQuestionPage.class);
        startActivity(intent);
    }

    public void captureVideo(View view) {

        if (!checkCameraPermission()){
            // if not allowed camera please accept camera
            requestCameraPermission();
        } else {
            //if camera is accessed
            videoPickCamera();
        }
    }

    public void SaveVideo(View view) {

        uploadVideoFirebase();
    }

    private void uploadVideoFirebase() {

        final String  timeStamp = "" + System.currentTimeMillis();
        String filePathName = "Videos/" + "video_" + timeStamp;

        final String[] hh = new String[1];

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathName);

        Task<Uri> urlTask = storageReference.putFile(videoUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String downloadURL = downloadUri.toString();
                    hh[0] = downloadURL;
                    Log.d("url: ", downloadURL);

                } else {
                    Toast.makeText(MainActivity.this, "out", Toast.LENGTH_SHORT).show();
                }
            }
        }).continueWithTask(new Continuation<Uri, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<Uri> task) throws Exception {

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("videoUrl", hh[0]);

                DatabaseReference databaseReference =   FirebaseDatabase.getInstance().getReference("UserDetails");
                databaseReference.push().setValue(hashMap);
                return null;
            }
        });

    }

    private  void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
//        boolean result2 = ContextCompat.checkSelfPermission(this,Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED;

        return result1;
    }

    private void videoPickCamera(){

        //camera will access
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,VIDEO_PICK_CAMERA_CODE);
    }

    private void setVideoToVideoView(){
        MediaController mediaController =  new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK){

            if (requestCode == VIDEO_PICK_CAMERA_CODE){
                videoUri = data.getData();
                setVideoToVideoView();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 ){

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        videoPickCamera();
                    }else {
                        Toast.makeText(this, "Camera & Storage permissions are Required", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
            /*Toast.makeText(getApplicationContext(), "back press",
                    Toast.LENGTH_LONG).show();*/

            return false;
        // Disable back button..............
        return false;
    }
}
