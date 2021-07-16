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

    public void setValue(String key, String value){
        preferences.put(key, value);
    }

    public int getInt(String key, int defaultValue){
        return preferences.getInt(key, defaultValue);
    }
}
