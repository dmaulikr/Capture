package com.cis350.argame;

import com.parse.ParseUser;

/**
 * Created by Anton on 4/10/14.
 */
public class PlayerProfile {

    public static String ID;
    public static int ARMY;
    public static int GOLD;
    public static String NAME;

    public static ParseUser createPlayerProfile() {
        // get id, army, gold from parse
        ParseUser currUser = ParseManager.getCurrentUser();
        ID = currUser.getObjectId();
        NAME = (String) currUser.get("username");
        ARMY = (Integer) currUser.get("army");
        GOLD = (Integer) currUser.get("gold");
        return currUser;
    }

    public static String getId() {
        return ID;
    }

    public static void setArmy(int ARMY) {
        PlayerProfile.ARMY = ARMY;
    }

    public static int getArmy() {
        return ARMY;
    }

    public static int getGold() {
        return GOLD;
    }

    public static void setGold(int GOLD) {
        PlayerProfile.GOLD = GOLD;
    }

    public static String getName() {
        return NAME;
    }
}
