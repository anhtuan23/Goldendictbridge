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

import static com.example.dotua.goldendictbridge.Main_Activity.changeDirectTranslateTextView;

/**
 * Created by dotua on 29-Jun-16.
 */
public class DirectTranslate_Task extends AsyncTask<String,String, String> {
    private final String API_KEY = "trnsl.1.1.20160628T114419Z.f94e02590b8527ee.cc30b2d8978d8c309a95ff05f53c05d2ceaaf214";
    private final String LANGUAGE_CHINESE_ENGLSIH = "zh-en";
    private String LOG_TAG = DirectTranslate_GetImageTask.class.getSimpleName();
    private String NO_INTERNET_CONNECTION = "No internet connection.";
    private String CANNOT_FIND_RESULT = "Cannot find result.";

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        changeDirectTranslateTextView("Translating "+ values[0]);
    }

    @Override
    protected String doInBackground(String... params) {
        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        publishProgress(params[0]);
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
                    "https://translate.yandex.net/api/v1.5/tr.json/translate?";
            final String QUERY_API_KEY = "key";
            final String QUERY_SOURCE_TEXT = "text";
            final String QUERY_ANGUAGE = "lang";

            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_API_KEY, API_KEY)
                    .appendQueryParameter(QUERY_SOURCE_TEXT, params[0])
                    .appendQueryParameter(QUERY_ANGUAGE, LANGUAGE_CHINESE_ENGLSIH)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(4000);
            urlConnection.setReadTimeout(5000);
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
            return getWeatherDataFromJson(rawJsonString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return CANNOT_FIND_RESULT;
    }

    private String getWeatherDataFromJson (String forecastJsonStr)
            throws JSONException {

//        final String OWM_CODE = "code";
//        final String OWM_LANG = "lang";
        final String OWM_TEXT = "text";

        JSONObject translatedTextJson = new JSONObject(forecastJsonStr);
        JSONArray translatedTextArray = translatedTextJson.getJSONArray(OWM_TEXT);

        String translatedText = translatedTextArray.getString(0);

        return translatedText;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        changeDirectTranslateTextView(s);
    }
}
