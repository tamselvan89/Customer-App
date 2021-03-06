package com.mivi.customerapp.generic;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    public static final String PREF_KEY_INITIAL_LOAD = "initial_load";
    public static final String PREF_KEY_LOGGED_IN = "logged_in";
    public static final String PREF_KEY_USER_NAME = "user_name";
    private static final String PREF_NAME = "MIVI";
    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void setValue(String key, String value) {
        mPref.edit()
                .putString(key, value)
                .apply();
    }

    public String getValue(String key, String value) {
        return mPref.getString(key, value);
    }

    public void setIntValue(String key, int value) {
        mPref.edit()
                .putInt(key, value)
                .apply();
    }

    public int getIntValue(String key, int value) {
        return mPref.getInt(key, value);
    }

    public void setBooleanValue(String key, boolean value) {
        mPref.edit()
                .putBoolean(key, value)
                .apply();
    }

    public boolean getBooleanValue(String key, boolean value) {
        return mPref.getBoolean(key, value);
    }

    public boolean checkValue(String key) {
        return mPref.contains(key);
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .apply();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }
}