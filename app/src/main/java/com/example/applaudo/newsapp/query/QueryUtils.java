package com.example.applaudo.newsapp.query;

import android.text.TextUtils;
import android.util.Log;

import com.example.applaudo.newsapp.models.News;

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


public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    //region JSON Parsing
    public static ArrayList<News> extractDataFromJson (String newsJson) {
        //Checks if the string is empty
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        //The ArrayList to be returned
        ArrayList<News> newsList = new ArrayList<>();

        try {
            JSONObject rootJson = new JSONObject(newsJson);

            JSONObject responseObject = rootJson.getJSONObject("response");

            JSONArray resultsArray = responseObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                 String id,headline,bodyText,section,thumbnail,website;
                //TODO: All the fields are optional so it doesn't fall into the catch and lose the data if one field is missing.

                 //This is where I get the section name and the Id
                JSONObject newsJsonData = resultsArray.getJSONObject(i);

                section = newsJsonData.optString("sectionName");
                id = newsJsonData.optString("id");



                //This is where I get all the other fields
                JSONObject newsJsonDataExtra = newsJsonData.getJSONObject("fields");

                headline = newsJsonDataExtra.optString("headline");
                bodyText = newsJsonDataExtra.optString("bodyText");
                website = newsJsonDataExtra.optString("shortUrl");
                thumbnail = newsJsonDataExtra.optString("thumbnail");

                //Checks if the JSON didn't have the required data
                id = checkEmptyResponse(id);
                headline = checkEmptyResponse(headline);
                bodyText = checkEmptyResponse(bodyText);
                website = checkEmptyResponse(website);
                section = checkEmptyResponse(section);
                thumbnail = checkEmptyResponse(thumbnail);

                newsList.add(new News(headline,bodyText,section,thumbnail,website,id));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG,"Problem parsing the JSON",e);
        }

        return newsList;
    }

    private static String checkEmptyResponse(String string)
    {
        if (TextUtils.isEmpty(string)) {
            string = "Not available";
        }
        return string;
    }
    //endregion

    //region Network connection

    public static ArrayList<News> fetchNewsData (String requestUrl) {
        //Create an URL object
        URL url = createUrl(requestUrl);

        //Perform the HTTP request
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem making the HTTP request.", e);
        }

        //Extract the relevant fields from the JSON response and create an ArrayList
        ArrayList<News> newsList = extractDataFromJson(jsonResponse);

        //Return the ArrayList
        return  newsList;
    }

    //Helper method to create an URL object
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //Helper method to make an HttpRequest
    private static  String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is null, then just exit the method with an empty response
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection =null;
        InputStream inputStream=null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(150000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Checks if the response was successful
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG,"Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG,"Problem retrieving JSON response: ", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //endregion

}


