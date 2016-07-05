package com.example.dotua.goldendictbridge;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dotua on 29-Jun-16.
 */
public class DirectTranslate_GetImageTask extends AsyncTask<String,Void, String> {
    private final String CUSTOM_SEARCH_ID = "004751125689537977935:7fpfquvojn0";
    private final String SERVER_KEY_ID = "AIzaSyCilOcsqmPP5Rkucxv8GpucQmjzffBhwL4";

    private String LOG_TAG = DirectTranslate_GetImageTask.class.getSimpleName();
    private String NO_INTERNET_CONNECTION = "No internet connection.";
    private String CANNOT_FIND_RESULT = "Cannot find result.";

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public DirectTranslate_GetImageTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... params) {
        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String rawJsonString = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String BASE_URL =
                    "https://www.googleapis.com/customsearch/v1?";
            final String SERVER_KEY = "key";
            final String CUSTOM_SEARCH = "cx";
            final String QUERY_TEXT = "q";
            final String SEACH_TYPE = "searchType";
            final String FILE_TYPE = "fileType";
            final String IMG_SIZE = "imgSize";
            final String NUMBER_OF_RESULTS = "num";
            final String RETURN_FORMAT = "alt";

            String searchType = "image";
            String fileType = "jpg";
            String imgSize = "medium";
            String numberOfResults = "1";
            String returnFormat = "json";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(SERVER_KEY, SERVER_KEY_ID)
                    .appendQueryParameter(CUSTOM_SEARCH, CUSTOM_SEARCH_ID)
                    .appendQueryParameter(QUERY_TEXT, params[0])
                    .appendQueryParameter(SEACH_TYPE, searchType)
                    .appendQueryParameter(FILE_TYPE, fileType)
                    .appendQueryParameter(IMG_SIZE, imgSize)
                    .appendQueryParameter(NUMBER_OF_RESULTS, numberOfResults)
                    .appendQueryParameter(RETURN_FORMAT, returnFormat)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return CANNOT_FIND_RESULT;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return CANNOT_FIND_RESULT;
            }
            rawJsonString = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return NO_INTERNET_CONNECTION;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getImageUrlFromJson(rawJsonString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return CANNOT_FIND_RESULT;
    }

    private String getImageUrlFromJson(String returnJsonStr)
            throws JSONException {

        final String OWM_ITEMS = "items";
        final String OWM_LNK = "link";

        JSONObject resultJson = new JSONObject(returnJsonStr);
        JSONArray resultArray = resultJson.getJSONArray(OWM_ITEMS);
        JSONObject firstResult = resultArray.getJSONObject(0);

        String imageUrl = firstResult.getString(OWM_LNK);

        return imageUrl;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.processFinish(s);
    }
}
