package by.bsuir.relax;

import android.app.Application;
import android.content.Context;

import by.bsuir.relax.dao.DAOFactory;
import by.bsuir.relax.net.AstrologyAPI;

public class RelaxApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();

        RelaxApplication.context = getApplicationContext();

    }

    public static Context getAppContext() {
        return RelaxApplication.context;
    }
}
