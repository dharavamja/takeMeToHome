package test.takemetohome.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 14/12/17.
 */

public class SharedPrefUtil
{
    private static SharedPrefUtil instance;
    private static final String SHARED_PREF_MAIN_KEY = "com.test.takeMeToHome.PREFERENCE_FILE_KEY";
    SharedPreferences.Editor prefEditor;
    private static SharedPreferences sharedPreferences;


    private SharedPrefUtil()
    {
    }

    public static SharedPrefUtil getInstance()
    {
        if (instance == null)
        {
            instance = new SharedPrefUtil();
        }
        return instance;
    }

    public static void initialize(Context context)
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_MAIN_KEY, Context.MODE_PRIVATE);
    }

    public synchronized void setStringToSharedPref(String key, String value)
    {
        prefEditor = sharedPreferences.edit();
        prefEditor.putString(key, value);
        prefEditor.apply();
    }

    public synchronized void setBooleanForKey(String key, boolean value)
    {
        prefEditor = sharedPreferences.edit();
        prefEditor.putBoolean(key, value);
        prefEditor.apply();
    }

    public synchronized boolean getBooleanForKey(String key)
    {
        return sharedPreferences.getBoolean(key, false);
    }

    public synchronized String getStringForKey(String key)
    {
        return sharedPreferences.getString(key, null);
    }


}
