package pl.test.capi.myapplication;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by hiddenpik on 08.03.2018.
 */


public class NetworkUtils {

    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie";

    private static final String SORT_BY = "sort_by";

    private static final String API = "api_key";

    private static final String YOUR_API_KEY = "";

    public static URL buildURL(String sortedOrder) {

        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(sortedOrder)
                .appendQueryParameter(API, YOUR_API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v("NetworkUtils.class", "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setConnectTimeout(7000);
            urlConnection.setReadTimeout(12000);
            InputStream inputStream = urlConnection.getInputStream();
            Log.i("Network Utils", "input stream taken");
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();

        }

    }
}
