package com.meyers.survivor.server.helpers;

public class Logging
{
    private static void log(String tag, String message) {
        System.out.println(String.format("[%s] %s", tag, message));
    }
    
    private static final String SERVER_TAG = "Server";
    public static void logServer(String message) {
        log(SERVER_TAG, message);
    }
    
    private static final String GAME_TAG = "Game State";
    public static void logGame(String message) { log(GAME_TAG, message); }
}
