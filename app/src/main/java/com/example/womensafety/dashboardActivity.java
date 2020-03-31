package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import static java.security.AccessController.getContext;

public class dashboardActivity extends AppCompatActivity implements LocationListener {
    BottomNavigationView bottomNavigationView;
    String gph1, gph2, gph3;
    String gemail1,gemail2,gemail3;
    SubscriptionManager subscriptionManager;
    SubscriptionInfo subscriptionInfo1, subscriptionInfo2;
    protected LocationManager locationManager;
    Boolean x = true;
    Boolean switchl=false;
    double lat, lon;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    String userphoneno;
    ImageButton emergency;

    private final String LIST_WHICH_GUARDIAN_TO_CALL ="pref_Select_guardian_to_call";
    private final String SWITCH_SEND_LOCATION ="pref_send_location_with_sms";
    private final String LIST_SELECT_SMS_SIM ="pref_send_sms_sim";

    private final String GUARDIAN1_MOBILE ="pref_guadian1_mobile";
    private final String GUARDIAN1_EMAIL ="pref_guadian1_Email";

    private final String GUARDIAN2_MOBILE ="pref_guadian2_mobile";
    private final String GUARDIAN2_EMAIL ="pref_guadian2_Email";

    private final String GUARDIAN3_MOBILE ="pref_guadian3_mobile";
    private final String GUARDIAN3_EMAIL ="pref_guadian3_Email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        PreferenceManager.setDefaultValues(this,R.xml.preferences,false);
        firebaseAuth=FirebaseAuth.getInstance();
        userphoneno=firebaseAuth.getCurrentUser().getPhoneNumber();
        gph1 = getIntent().getExtras().getString("g1phone1");
        gph2 = getIntent().getExtras().getString("g1phone2");
        gph3 = getIntent().getExtras().getString("g1phone3");

        gemail1 = getIntent().getExtras().getString("g1email1");
        gemail2 = getIntent().getExtras().getString("g1email2");
        gemail3 = getIntent().getExtras().getString("g1email3");

        toolbar=(Toolbar)findViewById(R.id.toolbardashboard);
        setSupportActionBar(toolbar);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        emergency = (ImageButton) findViewById(R.id.emergency);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            System.out.println("in");
            Toast.makeText(this,"getting Your Loction ,Please keep on the GPS",Toast.LENGTH_LONG).show();
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        } else {
            Log.e("DB", "PERMISSION GRANTED");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            subscriptionManager = getApplication().getSystemService(SubscriptionManager.class);
            subscriptionInfo1 = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(0);
            subscriptionInfo2 = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(1);


        }

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doallthecallandmessage();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new homeFragment()).commit();
    }

    private void doallthecallandmessage() {
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(subscriptionInfo1.getSubscriptionId());
        SmsManager smsManager1 = SmsManager.getSmsManagerForSubscriptionId(subscriptionInfo2.getSubscriptionId());


        if(!sharedPreferences.getBoolean(SWITCH_SEND_LOCATION,false)){
           System.out.println("inside not send location");
            String callselection=sharedPreferences.getString(LIST_WHICH_GUARDIAN_TO_CALL,"");
            String simselection=sharedPreferences.getString(LIST_SELECT_SMS_SIM,"");

                    if(simselection.equals("SIM 1")){
                        smsManager.sendTextMessage(gph1, null, "I am in trouble please help !!!!, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                        smsManager.sendTextMessage(gph2, null, "I am in trouble please help !!!!, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                        smsManager.sendTextMessage(gph3, null, "I am in trouble please help !!!!, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                    }
                    else{
                        if(simselection.equals("SIM 2")){
                            smsManager1.sendTextMessage(gph1, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                            smsManager1.sendTextMessage(gph2, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                            smsManager1.sendTextMessage(gph3, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                        }
                        else {
                            if(simselection.equals("Both")) {
                                smsManager.sendTextMessage(gph1, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                                smsManager1.sendTextMessage(gph1, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                                smsManager.sendTextMessage(gph2, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                                smsManager1.sendTextMessage(gph2, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                                smsManager.sendTextMessage(gph3, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                                smsManager1.sendTextMessage(gph3, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                            }
                            }
                    }


                    try{
                        Thread.sleep(5000);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if(callselection.equals("Guardian 1")){
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + gph1));
                            startActivity(callIntent);
                        }

                    }
                    else{
                        if(callselection.equals("Guardian 2")){
                            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + gph2));
                                startActivity(callIntent);
                            }

                        }
                        else {
                            if(callselection.equals("Guardian 3")){
                                if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:" + gph3));
                                    startActivity(callIntent);
                                }

                            }
                        }
                    }






        }

        else{

            System.out.println("inside send location");
            String callselection=sharedPreferences.getString(LIST_WHICH_GUARDIAN_TO_CALL,"");
            String simselection=sharedPreferences.getString(LIST_SELECT_SMS_SIM,"");


            if(x==true) {

                switchl=true;
                if(simselection.equals("SIM 1")){
                    smsManager.sendTextMessage(gph1, null, "I am in trouble please help !!!!, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                    smsManager.sendTextMessage(gph2, null, "I am in trouble please help !!!!, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                    smsManager.sendTextMessage(gph3, null, "I am in trouble please help !!!!, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                }
                else{
                    if(simselection.equals("SIM 2")){
                        smsManager1.sendTextMessage(gph1, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                        smsManager1.sendTextMessage(gph2, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                        smsManager1.sendTextMessage(gph3, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                    }
                    else {
                        if(simselection.equals("Both")) {

                            smsManager.sendTextMessage(gph1, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                            smsManager1.sendTextMessage(gph1, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                            smsManager.sendTextMessage(gph2, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                            smsManager1.sendTextMessage(gph2, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                            smsManager.sendTextMessage(gph3, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                            smsManager1.sendTextMessage(gph3, null, "I am in trouble please help, Link for my location is not avialable right now!!!1    wait for some time you will get my location in some time", null, null);
                        }
                        }
                }



                try {
                    Thread.sleep(5000);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (callselection.equals("Guardian 1")) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + gph1));
                        startActivity(callIntent);
                    }

                } else {
                    if (callselection.equals("Guardian 2")) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + gph2));
                            startActivity(callIntent);
                        }

                    } else {
                        if (callselection.equals("Guardian 3")) {
                            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + gph3));
                                startActivity(callIntent);
                            }

                        }
                    }
                }

            }
            else{

                if (simselection.equals("SIM 1")) {
                    smsManager.sendTextMessage(gph1, null, "I am in trouble please help    http://maps.google.com/maps?saddr="+lat+","+lon, null, null);
                    smsManager.sendTextMessage(gph2, null, "I am in trouble please help    http://maps.google.com/maps?saddr="+lat+","+lon, null, null);
                    smsManager.sendTextMessage(gph3, null, "I am in trouble please help    http://maps.google.com/maps?saddr="+lat+","+lon, null, null);
                } else {
                    if (simselection.equals("SIM 2")) {
                        smsManager1.sendTextMessage(gph1, null,"I am in trouble please help    http://maps.google.com/maps?saddr="+lat+","+lon, null, null);
                        smsManager1.sendTextMessage(gph2, null,"I am in trouble please help    http://maps.google.com/maps?saddr="+lat+","+lon, null, null);
                        smsManager1.sendTextMessage(gph3, null,"I am in trouble please help    http://maps.google.com/maps?saddr="+lat+","+lon, null, null);
                    } else {
                        if (simselection.equals("Both")) {
                            smsManager.sendTextMessage(gph1, null, "I am in trouble please help    http://maps.google.com/maps?saddr=" + lat + "," + lon, null, null);
                            smsManager1.sendTextMessage(gph1, null, "I am in trouble please help    http://maps.google.com/maps?saddr=" + lat + "," + lon, null, null);
                            smsManager.sendTextMessage(gph2, null, "I am in trouble please help    http://maps.google.com/maps?saddr=" + lat + "," + lon, null, null);
                            smsManager1.sendTextMessage(gph2, null, "I am in trouble please help    http://maps.google.com/maps?saddr=" + lat + "," + lon, null, null);
                            smsManager.sendTextMessage(gph3, null, "I am in trouble please help    http://maps.google.com/maps?saddr=" + lat + "," + lon, null, null);
                            smsManager1.sendTextMessage(gph3, null, "I am in trouble please help    http://maps.google.com/maps?saddr=" + lat + "," + lon, null, null);

                        }
                        }
                }


                try {
                    Thread.sleep(5000);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (callselection.equals("Guardian 1")) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + gph1));
                        startActivity(callIntent);
                    }

                } else {
                    if (callselection.equals("Guardian 2")) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + gph2));
                            startActivity(callIntent);
                        }

                    } else {
                        if (callselection.equals("Guardian 3")) {
                            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + gph3));
                                startActivity(callIntent);
                            }

                        }
                    }
                }


            }






        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    emergency.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new homeFragment()).commit();
                    return true;
                case R.id.navigation_helpline:
                    emergency.setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new helplineFragment()).commit();
                    return true;
                case R.id.navigation_profile:
                    emergency.setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new profileFragment()).commit();
                    return true;
            }

            return false;
        }
    };



    @Override
    public void onLocationChanged(Location location) {
        lat=location.getLatitude();
        lon=location.getLongitude();

        if(x==true){
            x=false;
            if(switchl==true){
                SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
                SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(subscriptionInfo1.getSubscriptionId());
                SmsManager smsManager1 = SmsManager.getSmsManagerForSubscriptionId(subscriptionInfo2.getSubscriptionId());


                String simselection=sharedPreferences.getString(LIST_SELECT_SMS_SIM,"");

                if (simselection.equals("SIM 1")) {
                    smsManager.sendTextMessage(gph1, null, "I am in trouble please help    http://maps.google.com/maps?saddr="+lat+","+lon, null, null);
                    smsManager.sendTextMessage(gph2, null, "I am in trouble please help    http://maps.google.com/maps?saddr="+lat+","+lon, null, null);
                    smsManager.sendTextMessage(gph3, null, "I am in trouble please help    http://maps.google.com/maps?saddr="+lat+","+lon, null, null);
                } else {
                    if (simselection.equals("SIM 2")) {
                        smsManager1.sendTextMessage(gph1, null,"I am in trouble please help    http://maps.google.com/maps?saddr="+lat+","+lon, null, null);
                        smsManager1.sendTextMessage(gph2, null,"I am in trouble please help    http://maps.google.com/maps?saddr="+lat+","+lon, null, null);
                        smsManager1.sendTextMessage(gph3, null,"I am in trouble please help    http://maps.google.com/maps?saddr="+lat+","+lon, null, null);
                    } else {
                        if (simselection.equals("Both")) {
                            smsManager.sendTextMessage(gph1, null, "I am in trouble please help    http://maps.google.com/maps?saddr=" + lat + "," + lon, null, null);
                            smsManager1.sendTextMessage(gph1, null, "I am in trouble please help    http://maps.google.com/maps?saddr=" + lat + "," + lon, null, null);
                            smsManager.sendTextMessage(gph2, null, "I am in trouble please help    http://maps.google.com/maps?saddr=" + lat + "," + lon, null, null);
                            smsManager1.sendTextMessage(gph2, null, "I am in trouble please help    http://maps.google.com/maps?saddr=" + lat + "," + lon, null, null);
                            smsManager.sendTextMessage(gph3, null, "I am in trouble please help    http://maps.google.com/maps?saddr=" + lat + "," + lon, null, null);
                            smsManager1.sendTextMessage(gph3, null, "I am in trouble please help    http://maps.google.com/maps?saddr=" + lat + "," + lon, null, null);

                        }
                    }
                }

            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.leftsidemenu,menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

            case R.id.navigation_setting:
                Intent i =new Intent(dashboardActivity.this,settingActivity.class);
                i.putExtra("g1phone1",gph1);
                i.putExtra("g1phone2",gph2);
                i.putExtra("g1phone3",gph3);

                i.putExtra("g1email1",gemail1);
                i.putExtra("g1email2",gemail2);
                i.putExtra("g1email3",gemail3);

                startActivity(i);

                return true;

            case R.id.navigation_aboutus:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
