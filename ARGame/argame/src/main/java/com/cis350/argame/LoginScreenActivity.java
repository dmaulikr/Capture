package com.cis350.argame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alan on 2/22/14.
 * Edited by Sacha on 3/15/14.
 */
public class LoginScreenActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
    }

    public void onLoginSubmitButtonClick(View v) {
        TextView usernameView = (TextView)findViewById(R.id.usernameField);
        TextView passwordView = (TextView)findViewById(R.id.passwordField);

        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        if (usernameContainsInvalidCharacters(username)) {
            Toast.makeText(this, "The specified username contains invalid characters. Please try again.",
                    Toast.LENGTH_SHORT).show();
        } else {
            CharSequence submitMessage = "Logged in.";
            try {
                ParseManager.logIn(username.toString(), password.toString());
            } catch (ParseManager.ConnectionFailedException e) {
                submitMessage = "Couldn't connect to Parse. Please check your internet connection.";
            }
            Toast.makeText(this, submitMessage, Toast.LENGTH_LONG).show();
            finish();
        }
    }
    private boolean usernameContainsInvalidCharacters(CharSequence name) {
        Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\_^]");
        Matcher matcher = pattern.matcher(name.toString());
        return matcher.find();
    }
}
