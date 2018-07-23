package com.mivi.customerapp.generic;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommonUtils {

    public static final String fileName = "collection.json";
    public static PreferencesManager mPreferencesManager;
    public static Toast popUpToast;

    /* Returns the PreferencesManager Instance*/
    public static PreferencesManager getPerferencesInstance(Context context) {
        PreferencesManager.initializeInstance(context);
        return PreferencesManager.getInstance();
    }

    public static String WriteJsonToLocal(Context ctx) {
        try {
            File jsonFilePath = new File(ctx.getFilesDir().getPath(), fileName);
            mPreferencesManager = getPerferencesInstance(ctx);
            if (!mPreferencesManager.getBooleanValue(PreferencesManager.PREF_KEY_INITIAL_LOAD, false)) {
                AssetManager assetManager = ctx.getAssets();
                InputStream inputStream = assetManager.open(fileName);
                OutputStream fileOutputStream = new FileOutputStream(jsonFilePath);
                copyFile(inputStream, fileOutputStream);
                mPreferencesManager.setBooleanValue(PreferencesManager.PREF_KEY_INITIAL_LOAD, true);
            }
            return jsonFilePath.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void copyFile(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            // write the output file
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Read the Json file from given path*/
    public static String readJsonFromPath(String path) {
        String jsonStirng = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonStirng = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonStirng;
    }

    public static void ShowLongToast(Context ctx, String msg) {
        if (popUpToast != null)
            popUpToast.cancel();

        popUpToast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        popUpToast.show();
    }
}
