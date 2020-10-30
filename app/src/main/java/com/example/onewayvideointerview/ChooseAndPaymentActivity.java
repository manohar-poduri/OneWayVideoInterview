package com.example.onewayvideointerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChooseAndPaymentActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private Button payAmount;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_and_payment);

        seekBar = findViewById(R.id.experienceSeekbar);
        payAmount = findViewById(R.id.pay);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressBar = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                progressBar = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        payAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ChooseAndPaymentActivity.this,AttendA.class));
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
}
