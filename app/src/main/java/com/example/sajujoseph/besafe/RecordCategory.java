package com.example.sajujoseph.besafe;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RecordCategory extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    Spinner spinner;
    Boolean lifeThreatning = false;
    Uri selectedImage;
    ImageView image;
    Button submit;
    TextView questionText ;
    EditText answerEdit ;
    Button uploadImage;
    ConstraintLayout constraintLayout;
    TextView uploadText;
    CheckBox checkBox;
    TextView locationText ;
    EditText locationEdit ;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_category);

        setUI();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();

            startActivity(intent);

        }
    }

    public void setUI() {
        constraintLayout = (ConstraintLayout) findViewById(R.id.parent_layout);
        answerEdit = (EditText) findViewById(R.id.category_et);
        uploadImage = (Button) findViewById(R.id.category_UploadImage_button);
        questionText = (TextView) findViewById(R.id.category_tv);
        image = (ImageView) findViewById(R.id.category_imageView);
        submit = (Button) findViewById(R.id.category_submit);
        uploadText = (TextView) findViewById(R.id.upload_text);
        checkBox = (CheckBox) findViewById(R.id.category_check);
        locationText = (TextView) findViewById(R.id.category_tv_location);
        locationEdit = (EditText) findViewById(R.id.category_et_location);


        answerEdit.setEnabled(false);
        answerEdit.setVisibility(View.GONE);
        uploadImage.setVisibility(View.INVISIBLE);
        questionText.setVisibility(View.INVISIBLE);
        image.setVisibility(View.INVISIBLE);
        submit.setVisibility(View.INVISIBLE);
        uploadText.setVisibility(View.INVISIBLE);
        checkBox.setVisibility(View.INVISIBLE);
        locationEdit.setVisibility(View.INVISIBLE);
        locationText.setVisibility(View.INVISIBLE);
        setSpinner();
        keypadOff();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.category_check:
                if (checked) {
                    lifeThreatning = true;
                    Toast.makeText(this, "If you need immediate help, please press SOS / call 911", Toast.LENGTH_LONG).show();

                }
        }
    }

    public void setSpinner() {
        spinner = (Spinner) findViewById(R.id.category_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

         category = String.valueOf(parent.getItemAtPosition(pos));
        selectCategory(category);

    }

    public void selectCategory(String category) {

        String answer;
        switch (category) {
            case "Medical":
                questionText.setText("Describe your Medical Incident");

                setEditVisibility();
                break;
            case "Fire":
                questionText.setText("Describe your Fire Incident. Also, please confirm the number of people affected. ");
                setEditVisibility();
                break;
            case "Robbery":
                questionText.setText("Describe your Robbery Incident");
                setEditVisibility();

                break;
            case "Accident":
                questionText.setText("Describe your Accident Incident. Also, please confirm the number of people affected.");
                setEditVisibility();

                break;
            case "Security":
                questionText.setText("Describe your Security Incident");
                setEditVisibility();

                break;

            default:
                break;

        }
        if (category != "Select a Category") {
            questionText.setVisibility(View.VISIBLE);
            answer = answerEdit.getText().toString().trim();

        }
    }

    public void setEditVisibility() {
        checkBox.setVisibility(View.VISIBLE);
        answerEdit.setVisibility(View.VISIBLE);
        answerEdit.setEnabled(true);
        uploadImage.setVisibility(View.VISIBLE);
        uploadText.setVisibility(View.VISIBLE);
        image.setVisibility(View.VISIBLE);
        uploadImage.setOnClickListener(this);
        locationText.setText("Incident Location (Address):");
        locationText.setVisibility(View.VISIBLE);
        locationEdit.setVisibility(View.VISIBLE);

    }

    private void keypadOff() {
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });

    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    keypadOff();
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.category_UploadImage_button:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, Constants.RESULT_LOAD_IMAGE);

                break;

            case R.id.category_submit:
                storeBitmap();

                Toast.makeText(this, "Incident Reported", Toast.LENGTH_SHORT).show();

                Intent EmergencyIntent = new Intent(this, Emergency.class);
                EmergencyIntent.setClass(this, Emergency.class);
                startActivity(EmergencyIntent);
                break;

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {

            case RESULT_OK:

                // ... Check for some data from the intent
                if (requestCode == Constants.RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
                    if (data != null) {

                        selectedImage = data.getData();
                        image.setImageURI(selectedImage);
                        submit.setVisibility(View.VISIBLE);
                        submit.setOnClickListener(this);
                        image.setBackgroundColor(Color.parseColor("#ffffff"));


                    }
                }

                break;

            case RESULT_CANCELED:
                selectedImage = null;
                // ... Handle this situation
                break;
        }
    }

    public void storeBitmap(){
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //calling the method uploadBitmap to upload image
        uploadBitmap(bitmap);
    }

    private void uploadBitmap(final Bitmap bitmap) {

        //getting the tag from the edittext
//        final String tags = editTextTags.getText().toString().trim();

        //our custom volley request


        Log.i("RENCY","Here in this method uploadBitmap():  ####");

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.UPLOAD_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {

                            //JSONObject arrayJson = new JSONObject(response.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
            * If you want to add more parameters with the image
            * you can do it here
            * here we have only one parameter with the image
            * which is tags
            * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Category",category);
                params.put("Description", answerEdit.getText().toString().trim());
                params.put("Address",locationEdit.getText().toString().trim());
                return params;
            }

            /*
            * Here we are passing image by renaming it with a unique name
            * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
