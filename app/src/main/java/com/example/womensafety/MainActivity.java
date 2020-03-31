package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions= new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,

            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      if(checkAndRequestPermissions()) {
          Intent i=new Intent(MainActivity.this,loginActivity.class);
          startActivity(i);
          finish();


      }



    }

    private boolean checkAndRequestPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if (grantResults.length > 0) {

                    for (int i = 0; i < permissions.length; i++) {


                        if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                            System.out.println("hello denied");
                            System.out.println(permissions[i]);
                            finish();

                        }
                        else {
                            System.out.println(permissions[i]+"access");
                        }



                    }

                    if(checkAndRequestPermissions()) {
                        Intent i=new Intent(MainActivity.this,loginActivity.class);
                        startActivity(i);
                        finish();


                    }

                }
                return;
            }
        }

  }
}
