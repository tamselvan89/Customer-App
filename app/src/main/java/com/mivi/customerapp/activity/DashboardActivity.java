package com.mivi.customerapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mivi.customerapp.R;
import com.mivi.customerapp.generic.CommonUtils;
import com.mivi.customerapp.generic.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity {

    Context mContext;
    TextView txtName;
    TextView txtSubscription;
    TextView txtProduct;
    String dataBal, expDate;
    String proName, proPrice, proText, proVoic, proInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
        process();
    }

    private void init() {
        mContext = DashboardActivity.this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtName = findViewById(R.id.txtUserName);
        txtSubscription = findViewById(R.id.txtSubscription);
        txtProduct = findViewById(R.id.txtProducts);

        PreferencesManager preferencesManager = CommonUtils.getPerferencesInstance(this);
        String name = "Hi " + preferencesManager.getValue(PreferencesManager.PREF_KEY_USER_NAME, "");
        txtName.setText(name.toUpperCase());
    }

    private void process() {
        String filePath = CommonUtils.WriteJsonToLocal(mContext);
        String jsonValue = CommonUtils.readJsonFromPath(filePath);

        try {
            JSONObject jsonObject = new JSONObject(jsonValue);
            if (jsonObject.has("included")) {
                JSONArray jsonArray = jsonObject.getJSONArray("included");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (object.has("type") && object.optString("type").equalsIgnoreCase("subscriptions")) {
                        JSONObject obj = object.optJSONObject("attributes");
                        dataBal = obj.optString("included-data-balance");
                        expDate = obj.optString("expiry-date");
                    } else if (object.has("type") && object.optString("type").equalsIgnoreCase("products")) {
                        JSONObject obj = object.optJSONObject("attributes");
                        proName = obj.optString("name");
                        proPrice = obj.optString("price");
                        proText = obj.optBoolean("unlimited-text") ? "UNLIMITED" : "LIMITED";
                        proVoic = obj.optBoolean("unlimited-talk") ? "UNLIMITED" : "LIMITED";
                        proInt = obj.optString("included-international-talk");
                        if (proInt.equalsIgnoreCase("null") || proInt.isEmpty()) {
                            proInt = "Nil";
                        }
                    }
                }
            }

            txtSubscription.setText(getString(R.string.txt_subscription, dataBal, expDate));
            txtProduct.setText(getString(R.string.txt_product, proName, proPrice, proText, proVoic, proInt));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
