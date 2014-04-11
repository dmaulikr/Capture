package com.cis350.argame;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Patterns;

import junit.framework.TestCase;

/**
 * Created by Alan on 4/3/14.
 */
public class SignupActivityTest extends TestCase {
    // Username contains invalid characters tested in Login

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
        if (email == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public void testMatchNull() {
        CharSequence pw1 = "a";
        CharSequence pw2 = "a";
        assertFalse(passwordsMatch(pw1, null));
        assertFalse(passwordsMatch(null, pw1));
        assertFalse(passwordsMatch(null, null));
    }

    public void testMatchValid() {
        CharSequence pw1 = "asdfjkl";
        CharSequence pw2 = "asdfjkl";
        assertTrue(passwordsMatch(pw1, pw2));
    }

    public void testMatchInvalid() {
        CharSequence pw1 = "aasdfjkl";
        CharSequence pw2 = "asdfjkl";
        assertFalse(passwordsMatch(pw1, pw2));
    }

    public void testEmailNull() {
        assertFalse(emailIsValid(null));
    }

    public void testEmailValid() {
        CharSequence email = "asdf@gmail.com";
        assertTrue(emailIsValid(email));
    }

    public void testEmailInvalid() {
        CharSequence email = "lolwut";
        assertFalse(emailIsValid(email));
    }
}
