package com.cis350.argame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

/**
 * Created by Alan on 2/22/14.
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

        try {
            ParseManager.logIn(username, password, "");
        } catch (ParseException pe) {
            Toast.makeText(this, "Username or password was invalid. Please " +
                    "try again.", Toast.LENGTH_LONG).show();
        }
    }

}
