package com.cis350.argame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

        CharSequence username = usernameFieldView.getText();
        CharSequence passwordInitial = passwordInitialView.getText();
        CharSequence passwordConfirm = passwordConfirmView.getText();
        CharSequence email = emailFieldView.getText();

        // First check if username contains invalid characters
        if (usernameContainsInvalidCharacters(usernameFieldView.getText())) {

        }

        // Check if username is taken.
        else if (usernameTaken(username)) {
            String message = "The name " + username + " is taken. Please " +
                    "choose another name and try again.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

        // Check if passwords match
        else if (!passwordsMatch(passwordInitial, passwordConfirm)) {
            Toast.makeText(this, "Your passwords do not match. Please confirm and try again.",
                    Toast.LENGTH_LONG).show();
        }

        // Check if email is valid
        else if (!emailIsValid(email)) {
            Toast.makeText(this, "Your email is invalid. Please check it and try again.",
                    Toast.LENGTH_LONG).show();
        }

        // If all is well, submit strings in fields to DB.
        else {
            // TODO: Send to database
            CharSequence submitMessage = "Successfully submitted! " + username + "," +
                    passwordInitial + "," + email;
            Toast.makeText(this, submitMessage, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean usernameContainsInvalidCharacters(CharSequence name) {
        // TODO: Unimplemented
        return false;
    }

    private boolean usernameTaken(CharSequence name) {
        // TODO: Unimplemented
        // Query database for matching name
        if (name == null) {
            return false;
        } else {
            return false;
        }
    }

    private boolean passwordsMatch(CharSequence pw1, CharSequence pw2) {
        if (pw1 == null || pw2 == null) {
            return false;
        } else {
            String pw1str = pw1.toString();
            String pw2str = pw2.toString();
            return (pw1str.contentEquals(pw2str));
        }
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
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
