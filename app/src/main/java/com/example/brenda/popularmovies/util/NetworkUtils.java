package com.example.brenda.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by brenda on 8/28/17.
 */

public class NetworkUtils {
    static final String POSTER_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    public static boolean isPhoneConnectedToInternet(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getPosterImageBaseURL(){
        return POSTER_IMAGE_BASE_URL;
    }
}
