package test.takemetohome.view;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by admin on 13/12/17.
 */

public class AppButton extends AppCompatButton
{
    public AppButton(Context context)
    {
        super(context);
        init();
    }

    public AppButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public AppButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {

    }
}
