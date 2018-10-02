package com.example.younes.hotornot.utils;

import android.content.Context;
import android.util.Log;

import com.facebook.AccessToken;

/**
 * Created by younes on 10/2/2018.
 */

public class FaceBookLoginStatus {
    private static AccessToken accessToken;
    private static boolean isLoggedIn;
    public static Boolean isFbConnected(Context context){
        accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();

        return isLoggedIn && SaveSharedPreference.getLoggedStatus(context);
    }
}
