package com.meyers.survivor.helpers;

import com.badlogic.gdx.Gdx;

public class Logging
{
    private static String NETWORK_TAG = "NETWORK";
    public static void logNetwork(String message) {
        Gdx.app.log(NETWORK_TAG, message);
    }
    
    private static String CLIENT_TAG = "CLIENT";
    public static void logClient(String message) {
        Gdx.app.log(CLIENT_TAG, message);
    }
}
