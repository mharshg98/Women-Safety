package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafety.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {
EditText phoneno;
TextView getotp;

FirebaseDatabase database;
DatabaseReference databaseReference;
Boolean x=false;
ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneno=(EditText)findViewById(R.id.signupphoneno);
        getotp=(TextView)findViewById(R.id.signupgetotp);
        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReferenceFromUrl("https://womensafety-20b61.firebaseio.com/user");
        progressDialog=new ProgressDialog(this);

        getotp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                   String x=phoneno.getText().toString();
                   if(x.length()==10) {
                       Intent i = new Intent(loginActivity.this, verifyActivity.class);
                       i.putExtra("phoneno", phoneno.getText().toString());
                       startActivity(i);
                   }
                   else{
                       System.out.println("hasgjdhgjas");
                   }


               }

       });






    }

}
