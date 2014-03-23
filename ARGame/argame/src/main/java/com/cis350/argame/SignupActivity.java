package com.cis350.argame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
import com.parse.ParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
=======
import java.util.regex.*;
>>>>>>> master

/**
 * Created by Alan on 2/22/14.
 * Edited by Sacha on 3/15/14.
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
            Toast.makeText(this, "Specified username contains invalid characters",
                    Toast.LENGTH_LONG).show();
        }

        /* Check if username is taken. - handled by parse
        else if (usernameTaken(username)) {
            String message = "The name " + username + " is taken. Please " +
                    "choose another name and try again.";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } */

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
            CharSequence submitMessage = "Successfully submitted! " + username + "," +
                    passwordInitial + "," + email;
            try {
                ParseManager.signUp(username.toString(), passwordConfirm.toString(), email.toString());
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

    /* This is handled by Parse.
    private boolean usernameTaken(CharSequence name) {
        // TODO: Unimplemented
        // Query database for matching name
        if (name == null) {
            return false;
        } else {
            return false;
        }
    } */

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
