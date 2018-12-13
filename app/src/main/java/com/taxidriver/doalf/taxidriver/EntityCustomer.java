package com.taxidriver.doalf.taxidriver;

import android.graphics.Bitmap;
import android.media.Image;

public class EntityCustomer {
    public String name;
    public Bitmap image;

    public EntityCustomer(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }
}
