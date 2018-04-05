package com.example.sajujoseph.besafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by sajujoseph on 3/27/18.
 */

public class SetPinCode extends AppCompatActivity implements View.OnClickListener{
    EditText pincode;
    DataStore ds;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pincode);

        Button submit = (Button) findViewById(R.id.pincode_submit);
        pincode = (EditText) findViewById(R.id.pincode_text);

        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        String pin=pincode.getText().toString().trim();
        ds = new DataStore();

        HashMap<String, String> hMap = new HashMap<String, String>();
        if (ds.getDataFromStoredPref(this, "PINCODE")=="") {

            hMap.put("PINCODE", pin);
            ds.setHashMap(hMap);
            ds.storeSaredPref(this);
            setContentView(R.layout.activity_pincode);

            Toast.makeText(this, "Please Re-Enter your Pin", Toast.LENGTH_LONG).show();
        }
        else{
            if (ds.getDataFromStoredPref(this, "PINCODE").toString().trim() == pin){
                Toast.makeText(this, "PIN DOES NOT MATCH. Please Try again "+pin+" "
                        +ds.getDataFromStoredPref(this, "PINCODE"), Toast.LENGTH_LONG).show();
                hMap.put("PINCODE", "");
                ds.setHashMap(hMap);
                ds.storeSaredPref(this);
                setContentView(R.layout.activity_pincode);
            }
            else {
                Toast.makeText(this, "You are all SET!! You now have a powerful button to your AID.", Toast.LENGTH_LONG).show();
                startEmergency();
            }

        }


    }
    public void startEmergency(){

        Intent PersonalDataIntent = new Intent(this, Emergency.class);
            PersonalDataIntent.setClass(this, Emergency.class);
            startActivity(PersonalDataIntent);
        }
    }
