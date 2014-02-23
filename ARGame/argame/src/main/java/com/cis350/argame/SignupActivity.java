package com.cis350.argame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.*;

/**
 * Created by Alan on 2/22/14.
 */
public class SignupActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

    }

    public void onSubmitButtonClick(View v) {
        TextView usernameFieldView = (TextView) findViewById(R.id.nameField);
        TextView passwordInitialView = (TextView) findViewById(R.id.passwordInitialText);
        TextView passwordConfirmView = (TextView) findViewById(R.id.passwordConfirmText);
        TextView emailFieldView = (TextView) findViewById(R.id.emailText);

        // First check if username is taken.
        if (userNameTaken(usernameFieldView.getText())) {
            String message = "The name " + usernameFieldView.getText() + " is taken. Please " +
                    "choose another name and try again.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

        // Check if passwords match
        else if (!passwordsMatch(passwordInitialView.getText(), passwordConfirmView.getText())) {
            Toast.makeText(this, "Your passwords do not match. Please confirm and try again.",
                    Toast.LENGTH_LONG).show();
        }

        // Check if email is valid
        else if (!emailIsValid(emailFieldView.getText())) {
            Toast.makeText(this, "Your email is invalid. Please check it and try again.",
                    Toast.LENGTH_LONG).show();
        }

        // If all is well, submit strings in fields to DB.
        else {
            // TODO: Send to database
            // Temporary filler for current format
            finish();
        }
    }

    public void onBackButtonClick(View v) {
        finish();
    }

    private boolean userNameTaken(CharSequence name) {
        // TODO: Unimplemented
        // Query database for matching name
        if (name == null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean passwordsMatch(CharSequence pw1, CharSequence pw2) {
        if (pw1 == null || pw2 == null) {
            return false;
        } else {
            return (pw1 == pw2);
        }
    }

    private boolean emailIsValid(CharSequence email) {
        // TODO: Make more thorough by querying for popular email domains? e.g. Gmail/Ymail/penn.edu
        // TODO: Or send a dummy email to the specified mail server and see if it bounces
        if (email == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }
}
