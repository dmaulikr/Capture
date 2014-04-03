package com.cis350.argame;

import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alan on 4/3/14.
 */
public class LoginScreenActivityTest extends TestCase {
    private boolean usernameContainsInvalidCharacters(CharSequence name) {
        if (name == null) {
            return true;
        }
        Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\_^]");
        Matcher matcher = pattern.matcher(name.toString());
        return matcher.find();
    }



    public void testTilde() {
        CharSequence input = "a~";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testPound() {
        CharSequence input = "b#";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testAt() {
        CharSequence input = "c@";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testAsterisk() {
        CharSequence input = "d*";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testPlus() {
        CharSequence input = "e+";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testPercent() {
        CharSequence input = "f%";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testLeftCurly() {
        CharSequence input = "g{";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testRightCurly() {
        CharSequence input = "h}";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testLT() {
        CharSequence input = "i<";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testGT() {
        CharSequence input = "j>";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testLeftHard() {
        CharSequence input = "k[";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testRightHard() {
        CharSequence input = "l]";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testBar() {
        CharSequence input = "m|";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testQuote() {
        CharSequence input = "n\"";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testUnderscore() {
        CharSequence input = "o_";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testCarot() {
        CharSequence input = "p^";
        assertTrue(usernameContainsInvalidCharacters(input));
    }

    public void testValid1() {
        CharSequence input = "alan";
        assertFalse(usernameContainsInvalidCharacters(input));
    }
    public void testValid2() {
        CharSequence input = "asjdy1892mndasn";
        assertFalse(usernameContainsInvalidCharacters(input));
    }
}
