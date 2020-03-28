package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




       new Handler().postDelayed(new Runnable() {
            @Override
         public void run()
            {       Intent i=new Intent(MainActivity.this,
                        loginActivity.class);


                startActivity(i);


                finish();

            }
        }, 4*1000);

    }
}
