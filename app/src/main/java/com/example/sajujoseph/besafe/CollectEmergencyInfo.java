package com.example.sajujoseph.besafe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.SEND_SMS;

/**
 * Created by sajujoseph on 3/16/18.
 */

public class CollectEmergencyInfo extends AppCompatActivity implements View.OnClickListener{

    private String EmerContactFirstName, EmerContactLastName, EmerContactPhoneNumber;

    Handler handler;
    Runnable runnable;

    boolean sendMessage = false, getLocation = false;
    final int PERMISSION_ALL = 110;
    final int MY_PERMISSIONS_REQUEST_SEND_MESSAGE = 111;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE = 112;
    final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE = 112;

    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("RENCY","Setting new activity");
        setContentView(R.layout.activity_emergency_info);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
//                startCollectEmergencyInfo();
//                finish();
            }
        };
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Button name_next = (Button)findViewById(R.id.emergency_info_button_next);
        name_next.setOnClickListener(CollectEmergencyInfo.this);

        String[] PERMISSIONS = {
                SEND_SMS,
                ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION,
                READ_EXTERNAL_STORAGE,
        };

        if(!UtilPermissions.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else
            handler.postDelayed(runnable, 3000);
    }


    public void startCollectEmergencyInfo(){
        Intent collectEmergencyInfo = new Intent(this, CollectEmergencyInfo.class);
        startActivity(collectEmergencyInfo);
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        int index = 0;
        Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();
        for (String permission : permissions){
            PermissionsMap.put(permission, grantResults[index]);
            index++;
        }

        if((PermissionsMap.get(ACCESS_FINE_LOCATION) != 0)
                || PermissionsMap.get(SEND_SMS) != 0){
            Toast.makeText(this, "Location and SMS permissions are a must", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            handler.postDelayed(runnable, 1500);
        }
    }

    public void startProfileInfoScreen(){

        Intent PersonalDataIntent = new Intent(this, CollectPersonalInfo.class);

//        Log.d("*** INBOUND INTENT: ", "" + ("FirstName is "+sharedPref.getString("FIRST_NAME","DEFAULT")));
        PersonalDataIntent.setClass(this, CollectEmergencyInfo.class);
        startActivity(PersonalDataIntent);
    }

    public void startSetPin(){

        Intent setpin = new Intent(this, SetPinCode.class);


        if (validate_input()) {
            DataStore ds = new DataStore();

            HashMap<String, String> hMap = new HashMap<String, String>();
            hMap.put("EMERGENCY_FIRST_NAME", EmerContactFirstName);
            hMap.put("EMERGENCY_LAST_NAME", EmerContactLastName);
            hMap.put("EMERGENCY_PHONE_NUMBER", EmerContactPhoneNumber);

            ds.setHashMap(hMap);
            ds.storeSaredPref(this);

            setpin.setClass(this, SetPinCode.class);
            startActivity(setpin);
        }
    }


    public void getSendMessagePermission(){


        /* Check if user has given Permission to send message */
        if (ContextCompat.checkSelfPermission(this, SEND_SMS)
                == PackageManager.PERMISSION_GRANTED){
            sendMessage   = true;
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_MESSAGE);
            if (ContextCompat.checkSelfPermission(this, SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                sendMessage = true;
            }
        }
    }

    public void getLocationAccessPermission(){
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            getLocation = true;
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE);
            if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                getLocation = true;
            }
        }

    }

    public boolean validate_input(){
        String toastMessage;
        boolean valid=false;

        if (EmerContactFirstName.equals("") && EmerContactLastName.equals("") && EmerContactPhoneNumber.equals("")){
            toastMessage = "Please Enter all data";
            Toast.makeText(this,toastMessage, Toast.LENGTH_LONG).show();
        }

        else if (EmerContactFirstName.equals("")){
            toastMessage = "Please Enter your First Name";
            Toast.makeText(this,toastMessage, Toast.LENGTH_LONG).show();
        }
        else if (EmerContactLastName.equals("")){
            toastMessage = "Please Enter your Last Name";
            Toast.makeText(this,toastMessage, Toast.LENGTH_LONG).show();
        }
        else if (EmerContactPhoneNumber.equals("")){
            toastMessage = "Please Enter your Phone Number";
            Toast.makeText(this,toastMessage, Toast.LENGTH_LONG).show();

        }
        else if (EmerContactPhoneNumber.length() !=10){
            toastMessage = "Please Enter valid Phone Number";
            Toast.makeText(this,toastMessage, Toast.LENGTH_LONG).show();
        }
        else
            valid=true;
        return valid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emergency_info_button_next:
                try {
                    getEmergencyDetails();
                    startSetPin();


                } catch (Exception e) {

                    e.printStackTrace();
                }
                break;
        }
    }



    public void getEmergencyDetails(){

        EditText fnm = (EditText) findViewById(R.id.emergency_info_et_first_name);
        EditText lnm = (EditText) findViewById(R.id.emergency_info_et_last_name);
        EditText pnm = (EditText) findViewById(R.id.emergency_info_et_phone_no);

        EmerContactFirstName = fnm.getText().toString().trim();
        EmerContactLastName = fnm.getText().toString().trim();
        EmerContactPhoneNumber = (pnm.getText().toString().trim());


    }

    public void onSaveInstanceState(Bundle outState){

        super.onSaveInstanceState(outState);

        outState.putString("EMERGENCY_FIRST_NAME", EmerContactFirstName.toString());
        outState.putString("EMERGENCY_LAST_NAME", EmerContactLastName.toString());
        outState.putString("EMERGENCY_PHONE_NUMBER", EmerContactPhoneNumber.toString());
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        EmerContactFirstName = savedInstanceState.getString("EMERGENCY_FIRST_NAME");
        EmerContactLastName = savedInstanceState.getString("EMERGENCY_LAST_NAME");
        EmerContactPhoneNumber = savedInstanceState.getString("EMERGENCY_PHONE_NUMBER");
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
