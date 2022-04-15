package by.bsuir.relax.ui.mental;

import android.graphics.drawable.Drawable;

public class InfoModel {
    private String text;
    private int drawable;


    public InfoModel(String text, int drawable) {
        this.text = text;
        this.drawable = drawable;
    }

    public String getText() {
        return text;
    }

    public int getDrawable() {
        return drawable;
    }
}
