package com.example.sajujoseph.besafe;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.icu.text.UnicodeSetSpanner;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.example.sajujoseph.besafe.R;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.sajujoseph.besafe.R.drawable.red_buzzer;

/**
 * Created by sajujoseph on 3/16/18.
 */


public class Emergency extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "BeSafe";
    GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private int recordCountStatus = 0;
    private boolean mAlreadyStartedService = false;
    String latitude, longitude;
    Button sos, cancel_alert, reportAct;
    TextView textmessage;
    String message;
    DataStore ds;
    LocationManager locationManager;

    double mLatitude,mLongitude;

    Boolean sosButtonCLicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_button);
        sos = (Button) findViewById(R.id.emergency_button_sos);
        cancel_alert = (Button) findViewById(R.id.emergency_cancel_alert);
        reportAct = (Button)findViewById(R.id.emergency_button_record_activity);
        textmessage = (TextView)findViewById(R.id.emergency_textview);
        cancel_alert.setVisibility(View.INVISIBLE);
        textmessage.setVisibility(View.INVISIBLE);

        ds = new DataStore();

        sos.setOnClickListener(this);
        buildGoogleApiClient();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "NO PERMISSION GRANTED", Toast.LENGTH_LONG).show();

            return;
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override


                    public void onReceive(Context context, Intent intent) {
                         latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                         longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);

                        if (latitude != null && longitude != null) {
                            message = "Your friend needs your help!! Location: http://maps.google.com/?q=";
                            message = message+ latitude +","+ longitude;
                            mLatitude = Double.valueOf(latitude);
                            mLongitude = Double.valueOf(longitude);
                            sendDataToCloud();
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)


        );
        if (mLastLocation == null)
            mLastLocation = getLastKnownLocation();


    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "No back", Toast.LENGTH_SHORT).show();
    }



    private Location getLastKnownLocation() {
        Log.i("RENCY", "INTO getLastKnownLocation");

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                        Constants.MY_PERMISSIONS_REQUEST_LOCATION);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)
                Log.i("RENCY", "NO PERMISSION");
            else
                Log.i("RENCY", "PERMISSION GRANTED");

            Location l = locationManager.getLastKnownLocation(provider);
            Log.d("RENCY", "last known location, provider: %s, location: %s" + provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                Log.d("RENCY: ", "found best last known location: %s" + l);
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            Log.i("RENCY", "Location is null");

            return null;

        }

        return bestLocation;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
//        mGoogleApiClient.connect();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * Step 1: Check Google Play services
     */
    private void startStep1() {

        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {

            //Passing null to indicate that it is executing for the first time.
            startStep2(null);

        } else {
            Toast.makeText(getApplicationContext(), "SERVICE NOT AVAILABLE", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Step 2: Check & Prompt Internet connection
     */
    private Boolean startStep2(DialogInterface dialog) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            promptInternetConnect();
            return false;
        }


        if (dialog != null) {
            dialog.dismiss();
        }

        //Yes there is active internet connection. Next check Location is granted by user or not.

        if (checkPermissions()) { //Yes permissions are granted by the user. Go to the next step.
            startStep3();
        } else {  //No user has not granted the permissions yet. Request now.
            requestPermissions();
        }

        return true;
    }

    /**
     * Step 3: Start the Location Monitor Service
     */
    private void startStep3() {

        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.

        if (!mAlreadyStartedService) {



            //Start location sharing service to app server.........
            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);

            mAlreadyStartedService = true;
            //Ends................................................
        }
    }


    private void promptInternetConnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Emergency.this);
        builder.setTitle("INTERNET NOT CONNECTED");
        builder.setMessage("Please Ensure you are connected to the Internet");

        String positiveText = "Refresh";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //Block the Application Execution until user grants the permissions
                        if (startStep2(dialog)) {

                            //Now make sure about location permission.
                            if (checkPermissions()) {

                                //Step 2: Start the Location Monitor Service
                                //Everything is there to start the service.
                                startStep3();
                            } else if (!checkPermissions()) {
                                requestPermissions();
                            }

                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;

    }
    /**
     * Start permissions requests.
     */
    private void requestPermissions() {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);


        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale || shouldProvideRationale2) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(Emergency.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    Constants.MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(Emergency.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlreadyStartedService) {
            stopService(new Intent(this, LocationMonitoringService.class));
            mAlreadyStartedService = false;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void getsendMessage() {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String firstName = ds.getDataFromStoredPref(this,"FIRST_NAME") ;
        String phoneNumber = ds.getDataFromStoredPref(this,"EMERGENCY_PHONE_NUMBER") ;
        message="Your friend "+firstName+" needs your help. Current location: Location: http://maps.google.com/?q="+mLatitude+","+mLongitude;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        sendDataToCloud();

    }

    public void getsendMessage(String phoneNumber, String message){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void sendDataToCloud() {
        RequestQueue queue = Volley.newRequestQueue(Emergency.this);
        StringRequest request = new StringRequest(Request.Method.POST,
                Constants.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("EMERGENCY",response);
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("EMERGENCY", String.valueOf(error));

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("firstName", ds.getDataFromStoredPref(Emergency.this, "FIRST_NAME"));
                map.put("lastName", ds.getDataFromStoredPref(Emergency.this, "LAST_NAME"));
                map.put("longitude", String.valueOf(mLatitude));
                map.put("latitude", String.valueOf(mLongitude));
                return map;
            }
        };
        queue.add(request);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (sosButtonCLicked)
            outState.putString("SOS_CLICKED", "true");
        else
            outState.putString("SOS_CLICKED", "false");

    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String sos_clicked =  savedInstanceState.getString("SOS_CLICKED");

        if (sos_clicked == "true")
            sosClicked();
    }

    public void startRecordCategory(){
        Intent recordCategory = new Intent(this, RecordCategory.class);
        recordCategory.setClass(this, RecordCategory.class);
        startActivity(recordCategory);
    }

    public void sosClicked(){
        sos.setBackground(getResources().getDrawable(red_buzzer));
        startStep1();
        cancel_alert.setVisibility(View.VISIBLE);
        cancel_alert.setOnClickListener(this);
        reportAct.setVisibility(View.GONE);
        textmessage.setVisibility(View.VISIBLE);

    }
    @Override
    public void onClick(View view) {

        sos = (Button)findViewById(R.id.emergency_button_sos);
        switch (view.getId()) {
            case R.id.emergency_button_sos:
                try {
                    sosButtonCLicked = true;
                    getsendMessage();
                    textmessage.setText("Message has been sent to "+ds.getDataFromStoredPref(this,"EMERGENCY_FIRST_NAME"));
                    sosClicked();

                } catch (Exception e) {


                    e.printStackTrace();
                }
                break;
            case R.id.emergency_cancel_alert:
                String phoneNo, message;
                message = ds.getDataFromStoredPref(this, "FIRST_NAME")+" has cancelled the alert.";
                getsendMessage(ds.getDataFromStoredPref(this, "EMERGENCY_PHONE_NUMBER"),
                        message);
                Intent checkPin = new Intent(this, CheckPin.class);
                checkPin.setClass(this, CheckPin.class);
                startActivity(checkPin);
                if (mAlreadyStartedService) {
                    stopService(new Intent(this, LocationMonitoringService.class));
                    mAlreadyStartedService = false;
                }

                break;

            case R.id.emergency_button_record_activity:
                startRecordCategory();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        String no_location_detected = "Location could not be determied";
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation == null)
            mLastLocation = getLastKnownLocation();

        if (mLastLocation != null) {
            mLatitude = mLastLocation.getLatitude();
            mLongitude = mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("RENCY", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

    }
}
