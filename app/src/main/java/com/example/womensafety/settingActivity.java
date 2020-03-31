package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class settingActivity extends AppCompatActivity {
    String gph1, gph2, gph3;
    String gemail1,gemail2,gemail3;
    public String getGph1() {
        return gph1;
    }

    public String getGph2() {
        return gph2;
    }

    public String getGph3() {
        return gph3;
    }

    public String getGemail1() {
        return gemail1;
    }

    public String getGemail2() {
        return gemail2;
    }

    public String getGemail3() {
        return gemail3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        gph1 = getIntent().getExtras().getString("g1phone1");
        gph2 = getIntent().getExtras().getString("g1phone2");
        gph3 = getIntent().getExtras().getString("g1phone3");

        gemail1 = getIntent().getExtras().getString("g1email1");
        gemail2 = getIntent().getExtras().getString("g1email2");
        gemail3 = getIntent().getExtras().getString("g1email3");
        if(findViewById(R.id.fragment_container)!=null){
            if(savedInstanceState!=null)
                return;

            readsettings(gph1,gph2,gph3);

            getFragmentManager().beginTransaction().add(R.id.fragment_container, new settingFragment()).commit();

        }



    }

    private void readsettings(String gph1, String gph2, String gph3) {

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);


    }
}
