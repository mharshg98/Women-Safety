package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signupActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText emailtextview,altphonenumber,age,city;
    TextView signup;
    ImageView back;
    String phoneno;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        database =FirebaseDatabase.getInstance();

        firebaseAuth=FirebaseAuth.getInstance();
        phoneno=firebaseAuth.getCurrentUser().getPhoneNumber();
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        firstName = (EditText) findViewById(R.id.signupfirstname);
        lastName = (EditText) findViewById(R.id.signuplastname);
        altphonenumber=(EditText)findViewById(R.id.alternatephoneno);
        age=(EditText)findViewById(R.id.Age) ;
        city=(EditText)findViewById(R.id.city);
        emailtextview = (EditText) findViewById(R.id.signupemail);
        signup = (TextView) findViewById(R.id.createaccount);
        back=(ImageView) findViewById(R.id.imageView_back);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(signupActivity.this,loginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void saveUser() {


        String fname = firstName.getText().toString().trim();
        String lname = lastName.getText().toString().trim();
        String e_mail = emailtextview.getText().toString().trim();
        String altphone=altphonenumber.getText().toString().trim();
        String Age=age.getText().toString().trim();
        String City=city.getText().toString().trim();
        if (!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname) && !TextUtils.isEmpty(e_mail) && !TextUtils.isEmpty(altphone) && !TextUtils.isEmpty(Age) && altphone.length() >= 6 && !TextUtils.isEmpty(City)) {


            doAuthentication1(fname,lname,e_mail,phoneno,altphone,Age,City);



        }
        else if (altphone.length() < 10)
            Toast.makeText(this, "Password should be of atleast 6 characters", Toast.LENGTH_LONG).show();
        else if (TextUtils.isEmpty(fname))
            Toast.makeText(this, "Enter the First Name", Toast.LENGTH_LONG).show();
        else if (TextUtils.isEmpty(lname))
            Toast.makeText(this, "Enter the Last Name", Toast.LENGTH_LONG).show();
        else if (TextUtils.isEmpty(e_mail))
            Toast.makeText(this, "Enter an Email ID", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Can not register the User", Toast.LENGTH_LONG).show();


    }

    private void doAuthentication1(String fname, String lname, String e_mail, String phoneno, String altphone, String age, String city) {
        progressBar.setVisibility(View.VISIBLE);
        System.out.println(fname);
        System.out.println(lname);
        System.out.println(e_mail);
        System.out.println(altphone);
        System.out.println(age);
        System.out.println(phoneno);
        System.out.println(city);
        DatabaseReference databaseUserRegister=database.getReferenceFromUrl("https://womensafety-20b61.firebaseio.com/user");
        DatabaseReference child=databaseUserRegister.child(phoneno.substring(1));


        DatabaseReference child1=child.child("First Name");
        child1.setValue(fname);

        child1=child.child("Last Name");
        child1.setValue(lname);

        child1=child.child("email");
        child1.setValue(e_mail);

        child1=child.child("Alternate Number");
        child1.setValue(altphone);

        child1=child.child("Age");
        child1.setValue(age);

        child1=child.child("Phone");
        child1.setValue(phoneno);

        child1=child.child("City");
        child1.setValue(city);

        child1=child.child("Profile Updates");
        child1.setValue("NO");




        progressBar.setVisibility(View.INVISIBLE);

        Intent i =new Intent(signupActivity.this,completeProfile.class);
        startActivity(i);
        finish();
    }



    @Override
    public void onBackPressed() {
        Intent i=new Intent(signupActivity.this,loginActivity.class);
        startActivity(i);
        finish();


    }


}
