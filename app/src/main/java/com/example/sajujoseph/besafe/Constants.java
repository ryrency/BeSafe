package com.example.sajujoseph.besafe;

import com.google.android.gms.location.DetectedActivity;

final class Constants {

    private Constants() {}

    static final String url = "http://192.168.0.3/insertUsertrack.php";

    private static final String PACKAGE_NAME =
            "com.google.android.gms.location.activityrecognition";

    static final String KEY_ACTIVITY_UPDATES_REQUESTED = PACKAGE_NAME +
            ".ACTIVITY_UPDATES_REQUESTED";

    static final int  LOCATION_INTERVAL=5000;
    static final int MY_PERMISSIONS_REQUEST_LOCATION = 110;
    static final int FASTEST_LOCATION_INTERVAL=1000;
    static final String KEY_DETECTED_ACTIVITIES = PACKAGE_NAME + ".DETECTED_ACTIVITIES";

    static final int RESULT_LOAD_IMAGE = 1;

    /**
     * The desired time between activity detections. Larger values result in fewer activity
     * detections while improving battery life. A value of 0 results in activity detections at the
     * fastest possible rate.
     */
    static final long DETECTION_INTERVAL_IN_MILLISECONDS = 30 * 1000; // 30 seconds
    /**
     * List of DetectedActivity types that we monitor in this sample.
     */
    static final int[] MONITORED_ACTIVITIES = {
            DetectedActivity.STILL,
            DetectedActivity.ON_FOOT,
            DetectedActivity.WALKING,
            DetectedActivity.RUNNING,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.TILTING,
            DetectedActivity.UNKNOWN
    };
}