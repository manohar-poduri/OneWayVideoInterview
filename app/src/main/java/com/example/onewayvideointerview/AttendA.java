package com.example.onewayvideointerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AttendA extends AppCompatActivity {
    private TextView usernameView, subject;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private String userId;
    private CheckBox checkBox;
    private Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);

        usernameView = findViewById(R.id.UN);
        subject = findViewById(R.id.sub);
        checkBox = findViewById(R.id.checkboxInstruction);
        next = findViewById(R.id.next);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("UserDetails");
        databaseReference.orderByChild("email").equalTo(auth.getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                    Log.d("TAG", userDetails.getUsername());
                    Log.d("TAG1", userDetails.getSubject());
                    usernameView.setText(userDetails.getUsername());
                    subject.setText(userDetails.getSubject());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(AttendA.this, error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkBox.isChecked()){
                    Toast.makeText(AttendA.this, "You have read all the instructions", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AttendA.this,MainActivity.class));


                } else {
                    Toast.makeText(AttendA.this, "Please read all the instructions", Toast.LENGTH_SHORT).show();
                }

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
