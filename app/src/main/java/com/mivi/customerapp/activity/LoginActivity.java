package com.mivi.customerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mivi.customerapp.R;
import com.mivi.customerapp.generic.CommonUtils;
import com.mivi.customerapp.generic.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Context mContext;
    EditText editTextMsnCode;
    Button btnLogin;
    String jsonValue;
    PreferencesManager mPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        process();
    }

    private void init() {
        mContext = LoginActivity.this;
        mPreferencesManager = CommonUtils.getPerferencesInstance(mContext);
        String filePath = CommonUtils.WriteJsonToLocal(mContext);
        jsonValue = CommonUtils.readJsonFromPath(filePath);

        editTextMsnCode = findViewById(R.id.editTextMSN);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void process() {
        if (mPreferencesManager.getBooleanValue(PreferencesManager.PREF_KEY_LOGGED_IN, false)) {
            Intent intent = new Intent(this, DashboardActivity.class);
            finish();
            startActivity(intent);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = editTextMsnCode.getText().toString().trim();
                if (!value.isEmpty()) {
                    mPreferencesManager.setBooleanValue(PreferencesManager.PREF_KEY_LOGGED_IN, true);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonValue);
                        if (jsonObject.has("included")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("included");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                if (object.has("type") && object.optString("type").equalsIgnoreCase("services")) {
                                    JSONObject obj = object.optJSONObject("attributes");
                                    if (obj.optString("msn").equalsIgnoreCase(value)) {
                                        object = jsonObject.optJSONObject("data");
                                        object = object.optJSONObject("attributes");
                                        String name = object.optString("title") + " " + object.optString("first-name")+ " "  + object.optString("last-name");
                                        mPreferencesManager.setValue(PreferencesManager.PREF_KEY_USER_NAME, name);
                                        Intent intent = new Intent(mContext, WelcomeActivity.class);
                                        finish();
                                        startActivity(intent);
                                    } else {
                                        CommonUtils.ShowLongToast(mContext, getString(R.string.txt_invalid_code));
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    CommonUtils.ShowLongToast(mContext, getString(R.string.txt_code_required));
                }
            }
        });
    }
}
