package test.takemetohome.root;

/**
 * Created by admin on 14/12/17.
 */

public class Constants
{
    private static Constants instance;

    public static Constants getInstance()
    {
        if (instance == null)
        {
            instance = new Constants();
        }
        return instance;
    }

    private Constants()
    {
    }

    public final String IS_SHORT_CUT_CREATED = "IS_SHORT_CUT_CREATED";
    public final String DESTINATION_LATITUDE = "DESTINATION_LATITUDE";
    public final String DESTINATION_LONGITUDE = "DESTINATION_LONGITUDE";
}
