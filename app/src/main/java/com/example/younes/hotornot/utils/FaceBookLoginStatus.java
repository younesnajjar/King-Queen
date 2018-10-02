package com.example.younes.hotornot.utils;

import android.content.Context;
import com.facebook.AccessToken;

/**
 * Created by younes on 10/2/2018.
 */

public class FaceBookLoginStatus {
    private FaceBookLoginStatus(){}

    public static Boolean isFbConnected(Context context){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        return isLoggedIn && SaveSharedPreference.getLoggedStatus(context);
    }
}
