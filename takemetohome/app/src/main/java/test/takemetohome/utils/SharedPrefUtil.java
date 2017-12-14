package test.takemetohome.utils;

import android.content.Context;
import android.content.SharedPreferences;

import test.takemetohome.R;
import test.takemetohome.root.MyApp;

/**
 * Created by admin on 14/12/17.
 */

public class SharedPrefUtil
{
    private static SharedPrefUtil instance;
    SharedPreferences.Editor prefEditor;
    private SharedPreferences sharedPreferences;

    private SharedPrefUtil()
    {
        Context context = MyApp.getInstance().getApplicationContext();
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.main_app_shared_pref_key), Context.MODE_PRIVATE);
    }

    public static SharedPrefUtil getInstance()
    {
        if (instance == null)
        {
            instance = new SharedPrefUtil();
        }
        return instance;
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
