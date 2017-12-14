package test.takemetohome.root;

import java.util.Locale;

/**
 * Created by admin on 14/12/17.
 */

public class AppSettings
{
    private static AppSettings instance;

    private AppSettings()
    {
    }

    public static AppSettings getInstance()
    {
        if (instance == null)
        {
            instance = new AppSettings();
        }
        return instance;
    }

    public Locale getLocale()
    {
        return Locale.ENGLISH;
    }

}
