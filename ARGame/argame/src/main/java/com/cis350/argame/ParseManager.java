package com.cis350.argame;

import com.parse.*;
import com.parse.ParseException;

import android.util.*;

import java.lang.reflect.Array;
import java.text.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sacha on 2/27/14.
 * Edited by Sacha on 3/15/14.
 */
public class ParseManager {

    /**
     * This is the only exception thrown by the ParseManager class. It is only thrown when there is
     * an issue connecting to the Parse servers.
     */
    public static final class ConnectionFailedException extends Exception {

    }

    /**
     * This enum represents the results of a login or sign up operation.
     */
    public enum LoginResult { SUCCESS, USER_TAKEN, EMAIL_TAKEN, INVALID }

    /**
     * This method checks if there is a user currently logged in.
     * @return true if there is a user logged in, false otherwise.
     */
    public static boolean isLoggedIn() {
        return ParseUser.getCurrentUser() != null && ParseUser.getCurrentUser().isAuthenticated();
    }

    /**
     * This is an interface method for ParseUser.getCurrentUser().
     * @return the current ParseUser
     */
    public static ParseUser getCurrentUser() {
        return ParseUser.getCurrentUser();
    }
    /**
     * This method tires to log in a user to Parse and assumes the Strings for username and password have been
     * checked for invalid characters.
     * @param username the username to check
     * @param password the password to check
     * @return a LoginResult representing the status of the attempt
     */
    public static LoginResult logIn(String username, String password) throws ConnectionFailedException {
        try {
            ParseUser.logIn(username, password);
        } catch (ParseException e) {
            switch (e.getCode()) {
                case ParseException.ACCOUNT_ALREADY_LINKED:
                case ParseException.INVALID_EMAIL_ADDRESS:
                    return LoginResult.INVALID;
                default:
                    throw new ConnectionFailedException();
            }
        }
        return LoginResult.SUCCESS;
    }
    // TODO - Separate registration method
    /**
     * This method signs up a user on the Parse database, checking first to see if the specified information
     * already exists. This method assumes that the information has been checked for invalid characters.
     * @param username the username to check
     * @param password the password to check
     * @param email the email to check
     * @return a LoginResult representing the status of the attempt
     */
    public static LoginResult signUp(String username, String password, String email)
            throws ConnectionFailedException {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        try {
            newUser.signUp();
        } catch (ParseException e) {
            switch (e.getCode()) {
                case ParseException.EMAIL_TAKEN:
                    return LoginResult.EMAIL_TAKEN;
                case ParseException.INVALID_EMAIL_ADDRESS:
                    return LoginResult.INVALID;
                case ParseException.USERNAME_TAKEN:
                    return LoginResult.USER_TAKEN;
                default:
                    throw new ConnectionFailedException();
            }
        }
        return LoginResult.SUCCESS;
    }
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
    public static ParseObject getPointByID(String pointID) throws ParseException {
        ParseQuery forID = ParseQuery.getQuery("CapturePoint");
        forID.whereEqualTo("pointID", pointID);
        if (forID.find().size() > 0) {
            return (ParseObject) forID.find().get(0);
        } else return null;
    }
    public static ParseObject[] getBuildingsByOwner(ParseUser user) throws ParseException {
        ParseQuery forUser = ParseQuery.getQuery("CapturePoint");
        forUser.whereEqualTo("owner", user);
        return (ParseObject[]) forUser.find().toArray();
    }

    /*public static String[] getBuildingsOwnersIds(ArrayList<String> s) throws ParseException {
        int len = s.size();
        String[] out = new String[len];
        for (int i = 0; i < s.size(); i++) {
            ParseObject forID = getPointByID(s.get(i));
            out[i] = (String) forID.get("ownerID");
        }
        return out;
    }*/

    //////get the owner ids of the buildings (String[] )
    public static ParseObject[] getBuildingsOwnersIds(String[] buildings) throws ParseException {
        int size = 0;
        size = buildings.length;
        String[] result = new String[size];
        ParseQuery query = ParseQuery.getQuery("CapturePoint");
        query.whereContainedIn("pointID", Arrays.asList(buildings));
        /*Object[] objects_from_parse = (Object[]) query.find().toArray();
        ParseObject[] objects = (ParseObject[]) objects_from_parse;
        Log.w("myAppOwners", "current building owners "+objects.length+""+"CAAAAAAA");
        String[] objs = new String[objects.length];
        Log.w("myAppOwners", "current building owners "+objs.length+""+"BAAAAAAA");
        for (int i = 0; i < objs.length; i++) {
            result[i] = objects[i].get("ownerID").toString();
            Log.w("myAppOwners", "current building owner is "+result[i]+"");
        }
        Log.w("myAppOwners", "current building owners "+result+""+"HAAAAAAA");
        return result;*/
        if (query.find().size() > 0) {
            return (ParseObject[]) query.find().toArray(new ParseObject[size]);
        } else return null;
    }

    public static String[] makeArrayOfOwners(ParseObject[] objects) throws java.text.ParseException {
        if (objects == null) return new String[0];
        int size = objects.length;
        String[] result = new String[size];
        String[] objs = new String[size];
        //Log.w("myAppOwners", "current building owners "+objs.length+""+"BAAAAAAA");
        for (int i = 0; i < objs.length; i++) {
            if (objects[i] != null) {
                result[i] = (String) objects[i].get("ownerID").toString();
            } else {
                result[i] = null;
            }
            //Log.w("myAppOwners", "current building owner is "+result[i]+"");
        }
        //Log.w("myAppOwners", "current building owners "+result+""+"HAAAAAAA");
        return result;
    }
}
