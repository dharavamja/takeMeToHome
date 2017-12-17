package test.takemetohome.activity;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by tejas on 17-12-2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import test.takemetohome.R;

public class SplashActivity extends AppCompatActivity {

    public static final int SPLASH_SCREEN_SHOWTIME_MILLISECONDS = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_splash_screen);

        /* Show Splash Screen after Certain Delay */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMainScreen();
            }
        }, SPLASH_SCREEN_SHOWTIME_MILLISECONDS);
    }

    private void showMainScreen() {
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
