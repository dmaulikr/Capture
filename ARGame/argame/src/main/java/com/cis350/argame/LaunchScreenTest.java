/**
package com.cis350.argame;


import com.parse.Parse;
import android.test.*;


public class LaunchScreenTest extends ActivityInstrumentationTestCase2<LaunchScreenActivity> {

    public static final String USER = "testUser";
    public static final String PASS = "testPass";
    public static final String EMAIL = "test@test.com";
    public LaunchScreenActivity mActivity;
    public LaunchScreenTest() {
        super("com.cis350.argame", LaunchScreenActivity.class);
    }
    protected void setUp()  {
        try {
            super.setUp();
        } catch (Exception e) {e.printStackTrace();}
        mActivity = getActivity();
        mActivity.parseInit();
        System.out.println("Done setup");

    }

    public void tearDown()  {

    }

    public void testIsLoggedIn()  {
        try {
            ParseManager.LoginResult success = ParseManager.logIn(USER, PASS);
            System.out.println("done login");

        } catch (ParseManager.ConnectionFailedException e) {e.printStackTrace();}
        assertEquals(true, ParseManager.isLoggedIn());
    }

    public void testGetCurrentUser()  {
        Parse.initialize(getActivity(), "tfq8Gi16KZq2L98xOp5cmlgKjM4rBXaiIlo2gBZx", "88zROkFEksIDYF2XPhqWqOumxocCy7hMVmkystCz");
        assertEquals(ParseManager.getCurrentUser(), ParseManager.getCurrentUser());
        System.out.println("done get");
    }

    public void testLogIn()  {
        try {
            ParseManager.LoginResult success = ParseManager.logIn(USER, PASS);
            assertEquals(success, ParseManager.LoginResult.SUCCESS);
            ParseManager.LoginResult failure = ParseManager.logIn(USER, "bullshit");
            assertEquals(failure, ParseManager.LoginResult.INVALID);
            ParseManager.LoginResult nulls = ParseManager.logIn(null, null);
            assertEquals(nulls, ParseManager.LoginResult.INVALID);
            System.out.println("done login test");

        } catch (ParseManager.ConnectionFailedException e) {e.printStackTrace();}
    }

    public void testSignUp()  {
        try {
            ParseManager.LoginResult success = ParseManager.signUp(USER + System.currentTimeMillis(), PASS, EMAIL);
            assertEquals(success, ParseManager.LoginResult.SUCCESS);
            ParseManager.LoginResult failure = ParseManager.signUp(USER, PASS, "bullshit@bullshit.com");
            assertEquals(failure, ParseManager.LoginResult.USER_TAKEN);
            ParseManager.LoginResult failure2 = ParseManager.signUp("test" + System.currentTimeMillis(), PASS, EMAIL);
            assertEquals(failure2, ParseManager.LoginResult.EMAIL_TAKEN);
            ParseManager.LoginResult nulls = ParseManager.signUp(null, null, null);
            assertEquals(nulls, ParseManager.LoginResult.INVALID);
        } catch (ParseManager.ConnectionFailedException e) {e.printStackTrace();}
    }
    /*
    public void testCapturePoint()  {

    }

    public void testCreatePoint()  {

    }

    public void testGetPointByID()  {

    }

    public void testGetBuildingsByOwner()  {
        System.out.println("Done");
    }

    public void testGetBuildingsOwnersIds()  {

    }

    public void testMakeArrayOfOwners()  {

    }
}
**/