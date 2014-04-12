package com.cis350.argame;

/**
 * Created by Anton on 4/10/14.
 */
public class PlayerProfile {
    private String ID;
    private int ARMY;
    private int GOLD;

    public PlayerProfile() {
        // get id, army, gold from parse
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
}
