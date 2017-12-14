package test.takemetohome.root;

import android.app.Application;

/**
 * Created by admin on 14/12/17.
 */

public class MyApp extends Application
{
    private static MyApp instance;

    public static MyApp getInstance()
    {
        if (instance == null)
        {
            instance = new MyApp();
        }
        return instance;
    }

    private MyApp()
    {
    }

}
