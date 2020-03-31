package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.UUID;

public class completeProfile extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    TextView name;
    ImageView imageView;
    FirebaseDatabase database;
    DatabaseReference refdatabase;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageReference;
    CircularImageView profileImage;
    String phonenumber;

    EditText nameg1, relationg1, phoneg1, emailg1;
    EditText nameg2, relationg2, phoneg2, emailg2;
    EditText nameg3, relationg3, phoneg3, emailg3;
    TextView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance();

        nameg1 = findViewById(R.id.guardian1name);
        nameg2 = findViewById(R.id.guardian2name);
        nameg3 = findViewById(R.id.guardian3name);

        relationg1 = findViewById(R.id.guardian1Relation);
        relationg2 = findViewById(R.id.guardian2Relation);
        relationg3 = findViewById(R.id.guardian3Relation);

        phoneg1 = findViewById(R.id.guardian1phone);
        phoneg2 = findViewById(R.id.guardian2phone);
        phoneg3 = findViewById(R.id.guardian3phone);

        emailg1 = findViewById(R.id.guardia1email);
        emailg2 = findViewById(R.id.guardia2email);
        emailg3 = findViewById(R.id.guardia3email);

        submit = findViewById(R.id.submit);


        auth = FirebaseAuth.getInstance();
        phonenumber = auth.getCurrentUser().getPhoneNumber();
        System.out.println(phonenumber);
        profileImage = (CircularImageView) findViewById(R.id.profilepicture);
        profileImage.setBorderWidth(10);
// Add Shadow with default param
        profileImage.addShadow();
// or with custom param
        profileImage.setShadowRadius(20);
        profileImage.setShadowColor(Color.RED);

        imageView = (ImageView) findViewById(R.id.changeprofilepicture);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadinfo();
            }
        });

    }

    private void uploadinfo() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Uploading Information");
        progressDialog.show();


        if (!TextUtils.isEmpty(nameg1.getText()) && !TextUtils.isEmpty(nameg2.getText()) && !TextUtils.isEmpty(nameg3.getText()) && !TextUtils.isEmpty(relationg1.getText()) && !TextUtils.isEmpty(relationg2.getText()) && !TextUtils.isEmpty(relationg3.getText()) && !TextUtils.isEmpty(phoneg1.getText()) && !TextUtils.isEmpty(phoneg2.getText()) && !TextUtils.isEmpty(phoneg3.getText()) && !TextUtils.isEmpty(emailg1.getText()) && !TextUtils.isEmpty(emailg3.getText()) && !TextUtils.isEmpty(emailg2.getText())) {

            DatabaseReference databaseUserRegister = database.getReferenceFromUrl("https://womensafety-20b61.firebaseio.com/user");
            DatabaseReference child = databaseUserRegister.child(phonenumber.substring(1));

            DatabaseReference child1 = child.child("Guardian1");
            DatabaseReference child2 = child1.child("Name");
            child2.setValue(nameg1.getText().toString());

            child2 = child1.child("Relation");
            child2.setValue(relationg1.getText().toString());

            child2 = child1.child("Phone");
            child2.setValue(phoneg1.getText().toString());

            child2 = child1.child("Email");
            child2.setValue(emailg1.getText().toString());

            DatabaseReference child3 = child.child("Guardian2");
            DatabaseReference child4 = child3.child("Name");
            child4.setValue(nameg2.getText().toString());

            child4 = child3.child("Relation");
            child4.setValue(relationg2.getText().toString());

            child4 = child3.child("Phone");
            child4.setValue(phoneg2.getText().toString());

            child4 = child3.child("Email");
            child4.setValue(emailg2.getText().toString());


            DatabaseReference child5 = child.child("Guardian3");
            DatabaseReference child6 = child5.child("Name");
            child6.setValue(nameg3.getText().toString());

            child6 = child5.child("Relation");
            child6.setValue(relationg3.getText().toString());

            child6 = child5.child("Phone");
            child6.setValue(phoneg3.getText().toString());

            child6 = child5.child("Email");
            child6.setValue(emailg3.getText().toString());

            child6 = child.child("Profile Updates");
            child6.setValue("YES");


            child.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("Profile Updates").getValue(String.class).equals("YES")) {
                        progressDialog.cancel();
                        Intent i = new Intent(completeProfile.this, dashboardActivity.class);
                        i.putExtra("g1phone1", dataSnapshot.child("Guardian1").child("Phone").getValue(String.class).toString());
                        i.putExtra("g1phone2", dataSnapshot.child("Guardian2").child("Phone").getValue(String.class).toString());
                        i.putExtra("g1phone3", dataSnapshot.child("Guardian3").child("Phone").getValue(String.class).toString());
                        i.putExtra("g1email1", dataSnapshot.child("Guardian1").child("Email").getValue(String.class).toString());
                        i.putExtra("g1email2", dataSnapshot.child("Guardian2").child("Email").getValue(String.class).toString());
                        i.putExtra("g1email3", dataSnapshot.child("Guardian3").child("Email").getValue(String.class).toString());
                        startActivity(i);
                        finish();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            Toast.makeText(this, "Please Enter All The Entries", Toast.LENGTH_LONG).show();
            progressDialog.cancel();
        }

    }



    private void chooseImage() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_REQUEST);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
System.out.println("hello");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            System.out.println(filePath);

        }
           uploadimage();
    }

    private void uploadimage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            final StorageReference ref = storageReference.child("userimages/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess( UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            adddownloadurl(ref);
                            Toast.makeText(getBaseContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            profileImage.setImageURI(filePath);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void adddownloadurl(StorageReference ref) {

        Task<Uri> urlTask = ref.getDownloadUrl();
        while (!urlTask.isSuccessful());
        final Uri downloadUrl = urlTask.getResult();
        System.out.println(downloadUrl.toString());

        DatabaseReference databaseUserRegister=database.getReferenceFromUrl("https://womensafety-20b61.firebaseio.com/user");
        DatabaseReference child=databaseUserRegister.child(phonenumber.substring(1));

        DatabaseReference child1=child.child("Profile Image URL");
        child1.setValue(downloadUrl.toString());


    }


}
