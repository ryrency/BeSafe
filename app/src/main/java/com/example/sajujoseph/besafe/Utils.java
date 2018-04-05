package com.example.sajujoseph.besafe;

/**
 * Created by sajujoseph on 3/27/18.
 */

import android.content.Context;
            import android.content.res.Resources;

            import com.google.android.gms.location.DetectedActivity;
//            import com.google.gson.Gson;
//            import com.google.gson.reflect.TypeToken;

            import java.lang.reflect.Type;
            import java.util.ArrayList;

public class Utils {

        private Utils() {}

        /**
         * Returns a human readable String corresponding to a detected activity type.
         */
        static String getActivityString(Context context, int detectedActivityType) {
            Resources resources = context.getResources();
            switch(detectedActivityType) {
                case DetectedActivity.IN_VEHICLE:
                    return "in_vehicle";
                case DetectedActivity.ON_BICYCLE:
                    return "on_bicycle";
                case DetectedActivity.ON_FOOT:
                    return "on_foot";
                case DetectedActivity.RUNNING:
                    return "running";
                case DetectedActivity.STILL:
                    return "still";
                case DetectedActivity.TILTING:
                    return "tilting";
                case DetectedActivity.UNKNOWN:
                    return "unknown";
                case DetectedActivity.WALKING:
                    return "walking";
                default:
                    return "unidentifiable_activity, detectedActivityType)";
            }
        }

//        static String detectedActivitiesToJson(ArrayList<DetectedActivity> detectedActivitiesList) {
//            Type type = new TypeToken<ArrayList<DetectedActivity>>() {}.getType();
//            return new Gson().toJson(detectedActivitiesList, type);
//        }

//        static ArrayList<DetectedActivity> detectedActivitiesFromJson(String jsonArray) {
//            Type listType = new TypeToken<ArrayList<DetectedActivity>>(){}.getType();
//            ArrayList<DetectedActivity> detectedActivities = new Gson().fromJson(jsonArray, listType);
//            if (detectedActivities == null) {
//                detectedActivities = new ArrayList<>();
//            }
//            return detectedActivities;
//        }

}
