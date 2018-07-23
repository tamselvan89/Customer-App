package com.mivi.customerapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mivi.customerapp.R;
import com.mivi.customerapp.generic.CommonUtils;
import com.mivi.customerapp.generic.PreferencesManager;

public class WelcomeActivity extends AppCompatActivity {

    int SPLASH_TIMOUT = 2000;
    Handler mHandler;
    Runnable runnable;
    TextView txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    private void init() {
        txtWelcome = findViewById(R.id.textWelcome);
        PreferencesManager preferencesManager = CommonUtils.getPerferencesInstance(this);
        String name = getString(R.string.txt_welcome_to_mivi, preferencesManager.getValue(PreferencesManager.PREF_KEY_USER_NAME, ""));
        txtWelcome.setText(name.toUpperCase());

        mHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        };
        mHandler.postDelayed(runnable, SPLASH_TIMOUT);
    }
}
