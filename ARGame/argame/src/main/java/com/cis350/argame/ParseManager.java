package com.cis350.argame;

import com.parse.*;

/**
 * Created by Sacha on 2/27/14.
 */
public class ParseManager {

    /**
     * This method checks if there is a user currently logged in.
     * @return true if there is a user logged in, false otherwise.
     */
    public static boolean isLoggedIn() {
        return ParseUser.getCurrentUser() != null;
    }

    /**
     * This is an interface method for ParseUser.getCurrentUser().
     * @return the current ParseUser
     */
    public static ParseUser getCurrentUser() {
        return ParseUser.getCurrentUser();
    }
    /**
     * This method tires to log in a user to Parse, and if the user does not exist, it will sign up a new
     * user with the given information. This method assumes the Strings for username and password have been
     * checked for invalid characters.
     * @param username the username to check
     * @param password the password to check
     * @param email the email to check
     * @throw a ParseException if the data is incorrect
     * @return true if the user existed prior, false otherwise
     */
    public static boolean logIn(String username, String password, String email)
            throws ParseException {
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
    // TODO - Separate registration method

    /**
     * capturePoint modifies the ParseObject for the CapturePoint specified such that the owner becomes the
     * the current user and the defending troops become the number specified.
     * @param point the CapturePoint to be captured
     * @param newDefense the new amount of defending
     */
    public static void capturePoint(ParseObject point, int newDefense) {
        point.put("defense", newDefense);
        point.put("currentOwner", ParseManager.getCurrentUser());
        point.saveInBackground();
    }
    // TODO - modify this method to subtract troops from the previous owner?
    public static void createPoint(ParseObject[] nodes, int pointID) {
        ParseObject newPoint = new ParseObject("CapturePoint");
        newPoint.put("defense", 0);
        newPoint.put("nodes", nodes);
        newPoint.put("pointID", pointID);
        newPoint.saveInBackground();
    }
    public static ParseObject getPointByID(int pointID) throws ParseException {
        ParseQuery forID = ParseQuery.getQuery("CapturePoint");
        forID.whereEqualTo("pointID", pointID);
        return (ParseObject) forID.find().get(0);
    }
}
