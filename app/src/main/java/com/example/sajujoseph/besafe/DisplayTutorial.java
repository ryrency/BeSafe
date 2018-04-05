package com.example.sajujoseph.besafe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;

import java.util.ArrayList;

import static com.example.sajujoseph.besafe.R.drawable.be_safe;
import static com.example.sajujoseph.besafe.R.drawable.emergency_button;
import static com.example.sajujoseph.besafe.R.drawable.emergency_button_pressed;
import static com.example.sajujoseph.besafe.R.drawable.emergency_contact_info;
import static com.example.sajujoseph.besafe.R.drawable.enter_pin_to_cancel;
import static com.example.sajujoseph.besafe.R.drawable.red_buzzer;
import static com.example.sajujoseph.besafe.R.drawable.report_incident_1;
import static com.example.sajujoseph.besafe.R.drawable.report_incident_3;
import static com.example.sajujoseph.besafe.R.drawable.user_information;


/**
 * Created by sajujoseph on 3/27/18.
 */

public class DisplayTutorial extends AppCompatActivity {
    String category;
    private RecyclerView recyclerView;
    TutorialAdapter tutorialAdapter;
    Uri selectedImage;
    final ArrayList<Object> question_list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_main_layout);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            recyclerView = (RecyclerView) findViewById(R.id.recycle_main_view);

            final LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);

            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);

            TutorialImages[] images = setImages();
            tutorialAdapter = new TutorialAdapter(DisplayTutorial.this, images);
            recyclerView.setAdapter(tutorialAdapter);
        }
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public TutorialImages[] setImages() {
        TutorialImages[] images = {
                new TutorialImages(getResources().getDrawable(be_safe),
                        "You can now get help at the press of a button!!"),
                new TutorialImages(getResources().getDrawable(user_information),
                        "Enter your information so that we could reach you when necessary"),
                new TutorialImages(getResources().getDrawable(emergency_contact_info),
                        "Enter your emergency contact information so that we could reach them when you need us to!"),
                new TutorialImages(getResources().getDrawable(enter_pin_to_cancel),
                        "Once you set your PIN,  you are ready to use the App"),
                new TutorialImages(getResources().getDrawable(emergency_button),
                        "Two features available - The SOS Button and "+
                                " The Record Activity"),
                new TutorialImages(getResources().getDrawable(emergency_button_pressed),
                        "As soon as you press the SOS button, your location details would be sent to your Emergency " +
                                "contact and saved on to Cloud. To cancel the alert all you need to do is enter your PIN and your contact would be informed that you are safe"),
                new TutorialImages(getResources().getDrawable(report_incident_1),
                        "To record any activity, you can select the category of the incident you want to record."),
                new TutorialImages(getResources().getDrawable(report_incident_3),
                        "Provide details and Submit!! Your done."),
        };
        return images;
    }
}



