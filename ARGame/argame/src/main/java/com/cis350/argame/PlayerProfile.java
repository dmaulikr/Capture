package com.cis350.argame;

import com.parse.ParseUser;

/**
 * Created by Anton on 4/10/14.
 */
public class PlayerProfile {
    private String ID;
    private int ARMY;
    private int GOLD;
    private String NAME;

    public PlayerProfile() {
        // get id, army, gold from parse
        ParseUser currUser = ParseManager.getCurrentUser();
        ID = currUser.getObjectId();
        NAME = (String) currUser.get("username");
        ARMY = (Integer) currUser.get("army");
        GOLD = (Integer) currUser.get("gold");
    }

    public String getId() {
        return ID;
    }

    public int getArmy() {
        return ARMY;
    }

    public int getGold() {
        return GOLD;
    }

    public String getName() {
        return NAME;
    }
}
