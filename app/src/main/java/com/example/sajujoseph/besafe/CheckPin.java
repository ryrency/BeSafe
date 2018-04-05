package com.example.sajujoseph.besafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by sajujoseph on 3/27/18.
 */

public class CheckPin extends AppCompatActivity implements View.OnClickListener {
    EditText password;
    DataStore ds;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pincode);

        Button submit = (Button) findViewById(R.id.pincode_submit);
        password = (EditText) findViewById(R.id.pincode_text);

        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
         ds = new DataStore();
        String passwordRawText = password.getText().toString().trim();


        if (passwordRawText.equals(ds.getDataFromStoredPref(this,"PINCODE").toString().trim())){
            sendMessage();
            Intent EmergencyIntent = new Intent(this, Emergency.class);
            EmergencyIntent.setClass(this, Emergency.class);
            startActivity(EmergencyIntent);
        }
        else
            Toast.makeText(this, "Incorrect PINCODE"+ds.getDataFromStoredPref(this,"PINCODE"), Toast.LENGTH_LONG).show();

        }
    public void sendMessage() {

        String phoneNumber = ds.getDataFromStoredPref(this,"PHONE_NUMBER");

        String message = "Your Friend has cancelled the Alert!";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(getApplicationContext(), ds.getDataFromStoredPref(this,"EMERGENCY_FIRST_NAME")+
                        " has been informed that you are safe!!",
                Toast.LENGTH_LONG).show();


    }
}
