package test.takemetohome.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by admin on 13/12/17.
 */

public class AppTextView extends AppCompatTextView
{
    public AppTextView(Context context)
    {
        super(context);
        init();
    }

    public AppTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public AppTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {

    }
}
