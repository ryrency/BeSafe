package com.example.sajujoseph.besafe;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by sajujoseph on 3/23/18.
 */

public class DataStore {

    private HashMap hashMap;

    public void setHashMap(HashMap hashMap) {
        this.hashMap = hashMap;
    }

    public HashMap getHashMap() {
        return hashMap;
    }

    public void storeSaredPref(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        Iterator it = hashMap.entrySet().iterator();

        while (it.hasNext()) {
            java.util.Map.Entry pair = (java.util.Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            editor.putString(pair.getKey().toString() ,pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }
        editor.commit();
    }

    public String getDataFromStoredPref(Context context, String key){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return(sharedPref.getString(key, "DEFAULT"));

    }
}
