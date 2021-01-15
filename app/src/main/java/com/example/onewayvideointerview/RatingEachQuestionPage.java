package com.example.onewayvideointerview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RatingEachQuestionPage extends AppCompatActivity {

    private TextView questions1;
    private Button nextQuestion1;
    private RatingBar ratingBar;
    private UserDetails userDetails;

    private int Totalquestions = 1;
    private int qid = 1;
    private int questionCount = 1;

    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_each_question_page);

        questions1 = findViewById(R.id.questionForRating);
        nextQuestion1 = findViewById(R.id.nextQuestionForRating);
        ratingBar = findViewById(R.id.ratingBarEachQuestion);


        auth = FirebaseAuth.getInstance();
        userDetails = new UserDetails();

        FirebaseDatabase.getInstance().getReference().child("questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    HashMap user = (HashMap) snapshot.getValue();

                    DatabaseReference databaseReference = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("UserDetails").child("-MORKXUEJuAIbjUEOfnU");
                    databaseReference.child("Questions").setValue(user);
                    Log.d("userDetails", String.valueOf(user));
                    FirebaseDatabase.getInstance().getReference().child("questions").setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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

        nextQuestion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayQuestion();

                String s = String.valueOf(ratingBar.getRating());
                Toast.makeText(RatingEachQuestionPage.this, s + " Star", Toast.LENGTH_SHORT).show();
                ratingBar.setRating(0.0f);

            }

        });

    }

    private void displayQuestion() {

        final Query questionToDisplay = FirebaseDatabase.getInstance()
                .getReference()
                .child("questions")
                .child(String.valueOf(qid)).child("one");

//        ct.cancel();
//        ct.start();
        qid+=1;

        // Generate
        // a random
        // integer from 1 to 30


        questionToDisplay.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                Log.d("One", String.valueOf(dataSnapshot));

                if (questionCount <= Totalquestions) {
                    //QuizQuestion quix = question.getValue(QuizQuestion.class);
                    String mp = dataSnapshot.getValue(String.class);
                    questions1.setText(mp);
                    questionCount += 1;

                } else {
                    Toast.makeText(RatingEachQuestionPage.this, "Questions have completed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ItemDetail", "onCancelled", databaseError.toException());
            }
        });


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
