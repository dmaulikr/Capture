package com.cis350.argame;

import junit.framework.TestCase;

/**
 * Created by Sacha on 4/3/2014.
 */
public class ParseManagerTest extends TestCase {

    public static final String USER = "testUser";
    public static final String PASS = "testPass";
    public static final String EMAIL = "test@test.com";

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {

    }

    public void testIsLoggedIn() throws Exception {
        ParseManager.LoginResult success = ParseManager.logIn(USER, PASS);
        assertEquals(true, ParseManager.isLoggedIn());
    }

    public void testGetCurrentUser() throws Exception {
        assertEquals(ParseManager.getCurrentUser(), ParseManager.getCurrentUser());
    }

    public void testLogIn() throws Exception {
        ParseManager.LoginResult success = ParseManager.logIn(USER, PASS);
        assertEquals(success, ParseManager.LoginResult.SUCCESS);
        ParseManager.LoginResult failure = ParseManager.logIn(USER, "bullshit");
        assertEquals(failure, ParseManager.LoginResult.INVALID);
        ParseManager.LoginResult nulls = ParseManager.logIn(null, null);
        assertEquals(nulls, ParseManager.LoginResult.INVALID);
    }

    public void testSignUp() throws Exception {
        ParseManager.LoginResult success = ParseManager.signUp(USER + System.currentTimeMillis(), PASS, EMAIL);
        assertEquals(success, ParseManager.LoginResult.SUCCESS);
        ParseManager.LoginResult failure = ParseManager.signUp(USER, PASS, "bullshit@bullshit.com");
        assertEquals(failure, ParseManager.LoginResult.USER_TAKEN);
        ParseManager.LoginResult failure2 = ParseManager.signUp("test" + System.currentTimeMillis(), PASS, EMAIL);
        assertEquals(failure2, ParseManager.LoginResult.EMAIL_TAKEN);
        ParseManager.LoginResult nulls = ParseManager.signUp(null, null, null);
        assertEquals(nulls, ParseManager.LoginResult.INVALID);
    }

    public void testCapturePoint() throws Exception {

    }

    public void testCreatePoint() throws Exception {

    }

    public void testGetPointByID() throws Exception {

    }

    public void testGetBuildingsByOwner() throws Exception {

    }

    public void testGetBuildingsOwnersIds() throws Exception {

    }

    public void testMakeArrayOfOwners() throws Exception {

    }
}
