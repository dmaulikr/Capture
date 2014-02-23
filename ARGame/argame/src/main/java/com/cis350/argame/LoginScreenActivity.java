package com.cis350.argame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Alan on 2/22/14.
 */
public class LoginScreenActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
    }

    public void onLoginSubmitButtonClick(View v) {
        TextView usernameView = (TextView) findViewById(R.id.usernameField);
        TextView passwordView = (TextView) findViewById(R.id.passwordField);
        CharSequence username = usernameView.getText();
        CharSequence password = passwordView.getText();

        if (!credentialsAreValid(username, password)) {
            Toast.makeText(this, "Credentials were invalid. Please try again.",
                    Toast.LENGTH_SHORT).show();
        } else {
            String message = "Login Successful! Submitting " + username + "," + password;
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean credentialsAreValid(CharSequence username, CharSequence password) {
        // TODO : Verify with DB
        return username.length() > 0 && password.length() > 0;
    }
}
