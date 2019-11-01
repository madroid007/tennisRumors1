package com.example.mad.tennisnews;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private Context mContext ;

    public QueryUtils(Context context) {
        mContext = context ;
    }



    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the GUARDIAN Api and return an {@link TennisNews} object to represent a single news.
     */
    public  List<TennisNews> fetchTennisNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.error_closing_input), e);
        }

        // Extract relevant fields from the JSON response and create an {@link TennisNews} object
        List<TennisNews> tennisNews = extractnews(jsonResponse);


        // Return the {@link TennisNews}
        return tennisNews ;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private  URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, mContext.getString(R.string.error_with_creating_url), exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private  String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if(url==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(mContext.getString(R.string.get_methode));
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode()==200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, mContext.getString(R.string.error_response_codee) + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.problem_retrieving_json), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private  String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName(mContext.getString(R.string.utf_8)));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Return a list of {@link TennisNews} objects that has been built up from
     * parsing a JSON response.
     */
    public  ArrayList<TennisNews> extractnews(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        ArrayList<TennisNews> tennisNews = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the SAMPLE_JSON_RESPONSE string
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            JSONObject response = baseJsonResponse.getJSONObject(mContext.getString(R.string.response));


            // Extract the JSONArray associated with the key called "results",
            // which represents a list of results (or news).
            JSONArray tennisNewsArray = response.getJSONArray(mContext.getString(R.string.results));



            // For each news in the newsArray, create an {@link TennisNews} object
            for (int i = 0; i < tennisNewsArray.length(); i++) {

                // Get a single news at position i within the list of news
                JSONObject currentTennisNews = tennisNewsArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String title = currentTennisNews.getString(mContext.getString(R.string.title));

                // Extract the value for the key called "sectionName"
                String sectionName = currentTennisNews.getString(mContext.getString(R.string.section));

                // Extract the value for the key called "webPublicationDate"
                String time = currentTennisNews.getString(mContext.getString(R.string.date));

                // Extract the value for the key called "webUrl"
                String url = currentTennisNews.getString(mContext.getString(R.string.web_url));

                // Create a new {@link TennisNews} object with the magnitude, location, time,
                // and url from the JSON response.
                TennisNews tennisNew = new TennisNews(title, sectionName, time, url);



                // Add the new {@link TennisNews} to the list of tennisNews.
                tennisNews.add(tennisNew);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, mContext.getString(R.string.problem_parsing_json_response), e);
        }

        // Return the list of news
        return tennisNews;
    }

}
