package com.abhi.controller.persistence;

import java.util.prefs.Preferences;

public class Preference {

    private static Preference preference;

    private final Preferences preferences;

    private Preference(){
        preferences = Preferences.userNodeForPackage(getClass());
    }

    public static Preference getInstance(){
        if(preference==null) preference = new Preference();
        return preference;
    }

    public void setInt(String key, int value){
        preferences.putInt(key, value);
    }

    public int getInt(String key){
        return preferences.getInt(key, 0);
    }
}
