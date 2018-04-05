package com.example.sajujoseph.besafe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    boolean sendMessage;
    DataStore ds = new DataStore();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (ds.getDataFromStoredPref(this,"TUTORIAL_DISPLAYED") == "DEFAULT") {
            startTutorial();
            HashMap<String, String> hMap = new HashMap<String, String>();
            hMap.put("TUTORIAL_DISPLAYED", "DONE");

            ds.setHashMap(hMap);
            ds.storeSaredPref(this);
        }
        else if (ds.getDataFromStoredPref(this,"FIRST_NAME") == "DEFAULT")
            StartCollectPersonalInfo();
        else if(ds.getDataFromStoredPref(this, "EMERGENCY_FIRST_NAME") == "DEFAULT"){
            String data = "Let's complete your profile!!";

            Toast.makeText(this,data, Toast.LENGTH_LONG).show();

            startCollectEmergencyInfo();
        }
        else{
            DataStore ds = new DataStore();
            startMainEmergencyPage();
        }
    }
    public void startTutorial(){
        Intent tutorialInfo = new Intent(this, DisplayTutorial.class);
        startActivity(tutorialInfo);
    }

    public void StartCollectPersonalInfo(){
        Intent collectNameInfo = new Intent(this, CollectPersonalInfo.class);
        startActivity(collectNameInfo);
    }

    public void startCollectEmergencyInfo(){
        Intent collectEmergencyInfo = new Intent(this, CollectEmergencyInfo.class);
        startActivity(collectEmergencyInfo);
    }

    public void startMainEmergencyPage(){

        Intent PersonalDataIntent = new Intent(this, Emergency.class);

//        Log.d("*** INBOUND INTENT: ", "" + ("FirstName is "+sharedPref.getString("FIRST_NAME","DEFAULT")));
        PersonalDataIntent.setClass(this, Emergency.class);
        startActivity(PersonalDataIntent);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
