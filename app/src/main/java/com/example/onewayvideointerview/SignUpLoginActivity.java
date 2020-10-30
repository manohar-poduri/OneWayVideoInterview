package com.example.onewayvideointerview;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpLoginActivity extends AppCompatActivity {

    private TextInputLayout email,password;
    private Button login;
    private TextView registerHere,forgotPassword;
    private FirebaseAuth auth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login);
        Window window = SignUpLoginActivity.this.getWindow();

        window.setStatusBarColor(ContextCompat.getColor(SignUpLoginActivity.this, R.color.skyblue));

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        registerHere = findViewById(R.id.registerHere);
        forgotPassword = findViewById(R.id.forgotPassword);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                auth.signInWithEmailAndPassword(email.getEditText().getText().toString(),
                        password.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignUpLoginActivity.this, "Login Successful!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpLoginActivity.this,ChooseAndPaymentActivity.class));
                        } else {
                            Toast.makeText(SignUpLoginActivity.this, "Please Give the correct credentials!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpLoginActivity.this,RegisterActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(SignUpLoginActivity.this,ForgotPassword.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(SignUpLoginActivity.this,ChooseAndPaymentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);        }
    }
}
