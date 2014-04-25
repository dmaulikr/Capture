package com.cis350.argame;

import com.cis350.argame.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class LaunchScreenActivity extends Activity {
    private boolean loggedIn = false; // Is the user logged in?

    private Button loginButton;

    // Result code constants
    private int LOGIN_COMPLETED = 50;
    private int REGISTRATION_COMPLETED = 51;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launchscreen_layout);
        loginButton = (Button) findViewById(R.id.loginButton);
        parseInit();
    }

    public void parseInit() {
        Parse.initialize(this, "tfq8Gi16KZq2L98xOp5cmlgKjM4rBXaiIlo2gBZx", "88zROkFEksIDYF2XPhqWqOumxocCy7hMVmkystCz");
    }

    public void onLoginButtonClick(View v) {
        if (!loggedIn) {
            // Start login process
            Intent i = new Intent(this, LoginScreenActivity.class);
            int requestCode = LOGIN_COMPLETED;
            startActivityForResult(i, requestCode);
        } else {
            // Log out
            loginButton.setText("Log In");
            loggedIn = false;
        }
    }

    public void onRegisterButtonClick(View v) {
        // Log out user first if applicable
        if (loggedIn) {
            loginButton.setText("Log In");
        }
        Intent i = new Intent(this, SignupActivity.class);
        int requestCode = REGISTRATION_COMPLETED;
        startActivityForResult(i, requestCode);
    }

    public void onLaunchButtonClick(View v) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    public void onQuitButtonClick(View v) {
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                            Intent data) {
        if (requestCode == LOGIN_COMPLETED ||
                requestCode == REGISTRATION_COMPLETED) {
            if (resultCode == RESULT_OK) {
                // Default value of 'loggedin' is [false] if it was not set
                // when the LoginScreenActivity instance terminated
                this.loggedIn = data.getBooleanExtra(
                        "com.cis350.argame.loggedin", false);
                if (loggedIn) {
                    loginButton.setText("Log Out");
                }
            }
        }
    }
}
