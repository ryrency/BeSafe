package com.example.sajujoseph.besafe;

import android.graphics.drawable.Drawable;

/**
 * Created by sajujoseph on 3/27/18.
 */

public class TutorialImages {
    private final Drawable imagePath;
    private final String description; /*Image, Text or Location*/

    public TutorialImages(Drawable imagePath, String description) {
        this.imagePath = imagePath;
        this.description = description;
    }


    public Drawable getImagePath() {
        return imagePath;
    }
    public String getDescription() {
        return description;
    }


}

