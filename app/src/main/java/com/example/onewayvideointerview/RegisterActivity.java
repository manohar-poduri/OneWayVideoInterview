package com.example.onewayvideointerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout username,email1, password1, phone;
    private Button register;
    private FirebaseAuth auth;
    private UserDetails userDetails;
    private DatabaseReference databaseReference;
    private Spinner spinner;
    private List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        userDetails = new UserDetails();
        names = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        username = findViewById(R.id.username);
        email1 = findViewById(R.id.email1);
        password1 = findViewById(R.id.password1);
        phone = findViewById(R.id.phone);
        register = findViewById(R.id.register);
        spinner = findViewById(R.id.chooseSpinner);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.getEditText().getText().toString().equals("") || email1.getEditText().getText().toString().equals("") || password1.getEditText().getText().toString().equals("") || phone.getEditText().getText().toString().equals("")){
                    username.setError("Username..");
                    username.requestFocus();
                    email1.setError("Email..");
                    email1.requestFocus();
                    password1.setError("Password..");
                    password1.requestFocus();
                    phone.setError("Phone Number..");
                    phone.requestFocus();
                } else {

                    userDetails.setUsername(username.getEditText().getText().toString());
                    userDetails.setEmail(email1.getEditText().getText().toString());
                    userDetails.setPhonenumber(phone.getEditText().getText().toString());
                    userDetails.setSubject(spinner.getSelectedItem().toString());

                    databaseReference.child("UserDetails").push().setValue(userDetails);

                    auth.createUserWithEmailAndPassword(email1.getEditText().getText().toString(), password1.getEditText().getText().toString()).addOnCompleteListener(RegisterActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("one", auth.getUid());
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, SignUpLoginActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(RegisterActivity.this, "register Successful!!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "registration failed!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        databaseReference.child("Field").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String spinnerField = dataSnapshot.child("name").getValue(String.class);
                    Log.d("TAG", spinnerField);
                    names.add(spinnerField);
                }

                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<String>(RegisterActivity.this,
                                android.R.layout.simple_spinner_item,names);

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinner.setAdapter(arrayAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
