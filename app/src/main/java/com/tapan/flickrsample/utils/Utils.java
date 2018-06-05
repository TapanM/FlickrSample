package com.tapan.flickrsample.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    // check if internet is available or not
    public static boolean isNetworkAvailable(Context context) {

        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = null;
            if (connectivityManager != null) {
                activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            }
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    public static int getInfoPosition(int position) {

        if(position%2==0) {
            return position+2;
        } else {
            return position+1;
        }
    }
}
