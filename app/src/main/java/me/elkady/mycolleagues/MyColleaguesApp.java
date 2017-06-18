package me.elkady.mycolleagues;

import android.app.Application;
import android.content.Context;

/**
 * Created by mak on 6/18/17.
 */

public class MyColleaguesApp extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
