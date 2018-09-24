package com.uber.flickrsearch.Network;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPApiCallService {

    private HTTPApiCallInterface mCallback;

    public HTTPApiCallService(HTTPApiCallInterface mCallback) {
        this.mCallback = mCallback;
    }

    public void getHTTPApiData(String url) {
        new HTTPApiCallAsync().execute(url);
    }

    class HTTPApiCallAsync extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String serverResponse = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    serverResponse = convertStreamToString(urlConnection.getInputStream());
                    //Log.d("Server Response: ", serverResponse);
                }
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(String serverResponse) {
            super.onPostExecute(serverResponse);
            //Log.d("Response", "" + serverResponse);

            try {
                if (mCallback != null && serverResponse != null)
                    mCallback.notifySuccess(new JSONObject(serverResponse));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Converting InputStream to String
    private static String convertStreamToString(InputStream inputStream) {

        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
