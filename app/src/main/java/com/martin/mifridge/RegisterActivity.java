package com.martin.mifridge;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * A login screen that offers login via name.
 */
public class RegisterActivity extends AppCompatActivity {
    private final String TAG = "RegisterActivity";

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private EditText mNameView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (readFromFile().equals("")) {
            createFile();
        } else if (this.getIntent().getStringExtra("Reregister") == null){
            Intent intent = new Intent(this, RegisteredActivity.class);
            intent.putExtra("name", readFromFile());
            startActivity(intent);
        }

        mNameView = (EditText) findViewById(R.id.name);
        mNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        Button mNameSignInButton = (Button) findViewById(R.id.name_sign_in_button);
        mNameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid name, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegistration() {
        // Reset errors.
        mNameView.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString();
        Log.i(TAG, name);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid name, if the user entered one.
        if (!isNameValid(name)) {
            mNameView.setError(getString(R.string.error_invalid_name));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (this.getIntent().getStringExtra("Reregister") != null) {
                overwriteToFile(name);
            } else {
                writeToFile(name);
            }

            // Start RegisteredActivity and send it the user's name.
            Intent intent = new Intent(this, RegisteredActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
            finish();
        }
    }

    private boolean isNameValid(String name) {
        return name.length() > 0;
    }

    private void createFile() {
        File file = new File(this.getFilesDir(), "name.txt");
        try {
            if (!file.exists()) {
                Log.i(TAG, "Creating name file");
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("name.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.i(TAG, "Wrote file with name " + data);
        }
        catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
    }

    private void overwriteToFile(String data) {
        try {
            File dir = new File(getFilesDir().getAbsolutePath());
            FileWriter fw = new FileWriter(dir + "/name.txt", false);
            fw.write(data);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

