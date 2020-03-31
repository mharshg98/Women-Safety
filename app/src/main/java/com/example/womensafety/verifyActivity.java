package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class verifyActivity extends AppCompatActivity {
    TextView verifyotp,resendotp,timer;
    ProgressDialog sendingotp,verifyingotp;
    private String verificationId;
    private EditText editText;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;


    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        String nphoneno=getIntent().getStringExtra("phoneno");
        final String phoneno="+91"+nphoneno;
        System.out.print(phoneno);

        database=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        resendotp=findViewById(R.id.resendotpbtn);
        resendotp.setVisibility(View.INVISIBLE);
        sendingotp=new ProgressDialog(this);
        sendingotp.setCanceledOnTouchOutside(false);
        verifyingotp=new ProgressDialog(this);
        verifyingotp.setMessage("verifying OTP");
        verifyingotp.setCanceledOnTouchOutside(false);

        timer=findViewById(R.id.timer);
        verifyotp = (TextView)findViewById(R.id.verifyotpbutton);
        editText=(EditText)findViewById(R.id.otpverifytext);
        sendingotp.setMessage("Sending OTP");
        sendingotp.show();
        sendVerificationCode(phoneno);





        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hello");
                sendVerificationCode(phoneno);
            }
        });

        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = editText.getText().toString().trim();

                if(code.isEmpty() || code.length()<6){
                    editText.setError("Enter Code...");
                    editText.requestFocus();
                    return;

                }
                verifyCode(code);


            }
        });
    }

    private void verifyCode(String code){
        verifyingotp.show();
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    sendingotp.cancel();
                    verifyingotp.cancel();
                 check();


                }
                else{
                    Toast.makeText(verifyActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    sendingotp.cancel();
                    verifyingotp.cancel();
                }
            }
        });
    }

    private void check() {
        final ProgressDialog progressDialog =new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("checking");
        progressDialog.show();
        String phonenumber=mAuth.getCurrentUser().getPhoneNumber();
        DatabaseReference databaseUserRegister=database.getReferenceFromUrl("https://womensafety-20b61.firebaseio.com/user");
        DatabaseReference child=databaseUserRegister.child(phonenumber.substring(1));

        child.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Profile Updates")) {
                    if (dataSnapshot.child("Profile Updates").getValue(String.class).equals("YES")) {
                        progressDialog.cancel();
                        Intent i = new Intent(verifyActivity.this, dashboardActivity.class);
                        i.putExtra("g1phone1", dataSnapshot.child("Guardian1").child("Phone").getValue(String.class).toString());
                        i.putExtra("g1phone2", dataSnapshot.child("Guardian2").child("Phone").getValue(String.class).toString());
                        i.putExtra("g1phone3", dataSnapshot.child("Guardian3").child("Phone").getValue(String.class).toString());

                        i.putExtra("g1email1", dataSnapshot.child("Guardian1").child("Email").getValue(String.class).toString());
                        i.putExtra("g1email2", dataSnapshot.child("Guardian2").child("Email").getValue(String.class).toString());
                        i.putExtra("g1email3", dataSnapshot.child("Guardian3").child("Email").getValue(String.class).toString());

                        startActivity(i);
                        finish();
                    } else {

                        progressDialog.cancel();
                        Intent i = new Intent(verifyActivity.this, completeProfile.class);
                        startActivity(i);
                        finish();


                    }
                }
                else{
                    progressDialog.cancel();
                    Intent i = new Intent(verifyActivity.this, signupActivity.class);
                    startActivity(i);
                    finish();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });



    }

    private void sendVerificationCode(String phoneno) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneno,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s,PhoneAuthProvider.ForceResendingToken forceResendingToken){
            super.onCodeSent(s,forceResendingToken);
            verificationId=s;

            sendingotp.cancel();
            countDownTimer=new CountDownTimer(60000,1000) {
                @Override
                public void onTick(long l) {
                    timer.setVisibility(View.VISIBLE);
                    timer.setText("OTP is valid till 00:"+l/1000+" sec");
                }

                @Override
                public void onFinish() {
                    resendotp.setVisibility(View.VISIBLE);
                    timer.setVisibility(View.INVISIBLE);
                }
            }.start();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null){
                sendingotp.cancel();

                editText.setText(code);
                verifyCode(code);

            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(verifyActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            sendingotp.cancel();
            verifyingotp.cancel();
            finish();
        }
    };
}
