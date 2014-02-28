package com.cis350.argame;

import com.parse.*;

/**
 * Created by Sacha on 2/27/14.
 */
public class ParseManager {

    /**
     * This method checks if there is a user currently logged in.
     * @return true if there is a user logged in, false otherwise
     */
    public static boolean isLoggedIn() {
        return ParseUser.getCurrentUser() != null;
    }

    /**
     * This method tires to log in a user to Parse, and if the user does not exist, it will sign up a new
     * user with the given information.
     * @param username the username to check
     * @param password the password to check
     * @param email the email to check
     * @return true if the user existed prior, false otherwise
     */
    public static boolean logIn(String username, String password, String email) {
        ParseUser currentUser = null;
        try {
            currentUser = ParseUser.logIn(username, password);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (currentUser == null) {
            currentUser = new ParseUser();
            currentUser.setUsername(username);
            currentUser.setPassword(password);
            currentUser.setEmail(email);
            try {
                currentUser.signUp();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

}
