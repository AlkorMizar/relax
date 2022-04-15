package by.bsuir.relax.dao;

import android.graphics.Bitmap;

public class Photo {
    private Bitmap photo;
    private int id;
    private String date;

    public Photo(Bitmap photo, int id, String date) {
        this.photo = photo;
        this.id = id;
        this.date = date;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }
}
