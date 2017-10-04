package com.omrobbie.bakingapp.util;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLUtils {

    public static String getContentType(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        if (isRedirect(connection.getResponseCode())) {
            String newUrl = connection.getHeaderField("Location");
            return getContentType(newUrl);
        }
        return connection.getContentType();
    }

    private static boolean isRedirect(int statusCode) {
        if (statusCode != HttpURLConnection.HTTP_OK) {
            if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || statusCode == HttpURLConnection.HTTP_MOVED_PERM
                    || statusCode == HttpURLConnection.HTTP_SEE_OTHER) {
                return true;
            }
        }
        return false;
    }
}
