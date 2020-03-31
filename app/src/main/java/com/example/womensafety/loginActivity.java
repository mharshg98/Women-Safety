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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {
EditText phoneno;
TextView getotp;
FirebaseAuth auth;
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
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("checking");
        auth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=auth.getCurrentUser();

        if(firebaseUser!=null) {
        progressDialog.show();
        check();
        }


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
                       Toast.makeText(getApplicationContext(),"please enter the valid phone number",Toast.LENGTH_LONG).show();
                   }


               }

       });






    }

    private void check() {
       String phonenumber =auth.getCurrentUser().getPhoneNumber();

        DatabaseReference databaseUserRegister=database.getReferenceFromUrl("https://womensafety-20b61.firebaseio.com/user");
        DatabaseReference child=databaseUserRegister.child(phonenumber.substring(1));


        child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.toString());
                System.out.println(dataSnapshot.getChildrenCount());

                if(dataSnapshot.child("Profile Updates").getValue(String.class).equals("YES")){
                    progressDialog.cancel();
                    Intent i =new Intent(loginActivity.this,dashboardActivity.class);
                    i.putExtra("g1phone1",dataSnapshot.child("Guardian1").child("Phone").getValue(String.class).toString());
                    i.putExtra("g1phone2",dataSnapshot.child("Guardian2").child("Phone").getValue(String.class).toString());
                    i.putExtra("g1phone3",dataSnapshot.child("Guardian3").child("Phone").getValue(String.class).toString());

                    i.putExtra("g1email1",dataSnapshot.child("Guardian1").child("Email").getValue(String.class).toString());
                    i.putExtra("g1email2",dataSnapshot.child("Guardian2").child("Email").getValue(String.class).toString());
                    i.putExtra("g1email3",dataSnapshot.child("Guardian3").child("Email").getValue(String.class).toString());


                    startActivity(i);
                    finish();
                }
                else{

                    progressDialog.cancel();
                    Intent i =new Intent(loginActivity.this,completeProfile.class);
                    startActivity(i);
                    finish();


                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
