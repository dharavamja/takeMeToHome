package test.takemetohome.root;

import android.app.Application;

import test.takemetohome.utils.SharedPrefUtil;

/**
 * Created by admin on 14/12/17.
 */

public class MyApp extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        SharedPrefUtil.initialize(this);
    }
}
