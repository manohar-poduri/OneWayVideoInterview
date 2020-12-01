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

        final DatabaseReference databaseReference   = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("UserDetails").child("Questions");

        DatabaseReference databaseReference1 = databaseReference.child("UserDetails");
        databaseReference1.orderByChild("email").equalTo(auth.getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Log.d("findUid", String.valueOf(dataSnapshot.getKey()));

                    String uniqueId = dataSnapshot.getKey();
                    DataSnapshot addData = dataSnapshot.child("Questions");
//                    Log.d("uniqueId", uniqueId);
//                    Log.d("dataAdded", addData);
                    System.out.println(uniqueId);
                    System.out.println(addData);

                    moveFirebaseRecord(databaseReference.child("questions"),addData);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        moveFirebaseRecord(databaseReference.child("questions"),databaseReference2.child("Questions"));



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

                /*DatabaseReference databaseReference1 = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("questions").orderByChild(String.valueOf(qid));
                userDetails.setQuestionsRating(s);
                databaseReference1.child("two").setValue(userDetails);*/
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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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


  /*  public void moveFirebaseRecord(DatabaseReference fromPath, final DatabaseReference toPath)
    {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener()
                {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null)
                        {
                            System.out.println("Copy failed");
                        }
                        else
                        {
                            System.out.println("Success");
                        }
                    }


                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Copy failed");

            }

        });
    }*/

    public void moveFirebaseRecord(DatabaseReference fromPath, final DataSnapshot toPath)
    {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                toPath.child(String.valueOf(dataSnapshot.getValue())    );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Copy failed");

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
