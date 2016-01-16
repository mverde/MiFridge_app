package com.martin.mifridge;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Martin on 1/15/2016.
 */
public class MiInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = "IIDListenerService";
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        String name = readFromFile();
        if (name.equals("")) {
            return;
        }

        Intent intent = new Intent(this, RegistrationIntentService.class);
        intent.putExtra("name", name);
        startService(intent);
    }

    private String readFromFile() {
        String ret = "";

        try {
            InputStream inputStream = openFileInput("name.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
                Log.i(TAG, "File contents: " + ret);
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
            ret = "";
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }

        return ret;
    }
}
