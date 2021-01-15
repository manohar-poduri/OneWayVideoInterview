package com.example.onewayvideointerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChooseAndPaymentActivity extends AppCompatActivity {

    private Button payAmount;
    private TextView amount, experience;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_and_payment);

        payAmount = findViewById(R.id.pay);
        experience = findViewById(R.id.experience);
        amount = findViewById(R.id.amount);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        DatabaseReference databaseReference = firebaseDatabase.getReference("UserDetails");
        databaseReference.orderByChild("email").equalTo(auth.getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                    String experience1 = userDetails.getExperience();

                    experience.setText(experience1);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ChooseAndPaymentActivity.this, error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });



        DatabaseReference databaseReference1 = firebaseDatabase.getReference("UserDetails");
        databaseReference1.orderByChild("email").equalTo(auth.getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                    String payment = userDetails.getPayment();

                    amount.setText(payment);
                    amount.setMovementMethod(LinkMovementMethod.getInstance());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ChooseAndPaymentActivity.this, error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


        payAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                startActivity(new Intent(ChooseAndPaymentActivity.this,AttendA.class));


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout){

            FirebaseAuth.getInstance().signOut();
            Toast.makeText(ChooseAndPaymentActivity.this,"You have logged out successful!!",
                    Toast.LENGTH_LONG).show();
            startActivity(new Intent(ChooseAndPaymentActivity.this,SignUpLoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
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
