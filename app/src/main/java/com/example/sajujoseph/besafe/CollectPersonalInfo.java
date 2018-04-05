package com.example.sajujoseph.besafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by sajujoseph on 3/16/18.
 */

public class CollectPersonalInfo extends AppCompatActivity implements View.OnClickListener{

    private String firstName="";
    private String lastName="";
    private String phoneNumber="" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        Button name_next = (Button)findViewById(R.id.personal_info_button_next);
        name_next.setOnClickListener(this);
    }


    public void startEmergencyInfoScreen(){


        if (validate_input()) {
            Intent PersonalDataIntent = new Intent(this, CollectPersonalInfo.class);
            DataStore ds = new DataStore();

            HashMap<String,String> hMap = new HashMap<String, String>();
            hMap.put("FIRST_NAME",firstName);
            hMap.put("LAST_NAME",lastName);
            hMap.put("PHONE_NUMBER",phoneNumber);
            hMap.put("PINCODE","");

            ds.setHashMap(hMap);
            ds.storeSaredPref(this);

            PersonalDataIntent.setClass(this, CollectEmergencyInfo.class);
            startActivity(PersonalDataIntent);
        }
    }

    public boolean validate_input(){
        String toastMessage;
        boolean valid=false;

        if (firstName.equals("") && lastName.equals("") && phoneNumber.equals("")){
            toastMessage = "Please Enter all data";
            Toast.makeText(this,toastMessage, Toast.LENGTH_LONG).show();
        }

        else if (firstName.equals("")){
            toastMessage = "Please Enter your First Name";
            Toast.makeText(this,toastMessage, Toast.LENGTH_LONG).show();
        }
        else if (lastName.equals("")){
            toastMessage = "Please Enter your Last Name";
            Toast.makeText(this,toastMessage, Toast.LENGTH_LONG).show();
        }
        else if (phoneNumber.equals("")){
            toastMessage = "Please Enter your Phone Number";
            Toast.makeText(this,toastMessage, Toast.LENGTH_LONG).show();

        }
        else if (phoneNumber.length() !=10){
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
            case R.id.personal_info_button_next:
                try {
                    getPersonalDetails();
                    startEmergencyInfoScreen();
                } catch (Exception e) {


                    e.printStackTrace();
                }
                break;
        }
    }

    public void getPersonalDetails(){

        EditText fnm = (EditText) findViewById(R.id.personal_info_et_first_name);
        EditText lnm = (EditText) findViewById(R.id.personal_info_et_last_name);
        EditText pnm = (EditText) findViewById(R.id.personal_info_et_phone_no);

        Log.i("RENCY_FNM",fnm.getText().toString().trim());
        if (fnm.getText().toString().trim() == "")
            Log.i("RENCY", "Here I am");

        firstName = fnm.getText().toString().trim();
        lastName = lnm.getText().toString().trim();
        Toast.makeText(this,lastName,Toast.LENGTH_SHORT).show();
        phoneNumber = (pnm.getText().toString().trim());


    }

    public void onSaveInstanceState(Bundle outState){

        super.onSaveInstanceState(outState);

        outState.putString("FIRST_NAME", firstName.toString());
        outState.putString("LAST_NAME", lastName.toString());
        outState.putString("PHONE_NUMBER",phoneNumber.toString());
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        firstName = savedInstanceState.getString("FIRST_NAME");
        lastName = savedInstanceState.getString("LAST_NAME");
        phoneNumber = savedInstanceState.getString("PHONE_NUMBER");
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
