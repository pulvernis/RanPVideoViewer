package com.ranpulvernis.ranpvideoviewer.service;

import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ranpulvernis on 12/04/2017.
 */

public class VideoService {
    private VideoViewerV1 callback;
    private Exception error;

    public VideoService(VideoViewerV1 callback) {
        this.callback = callback;
    }

    public void refreshMovies(){
        new AsyncTask<Void, Void, JSONObject>() {

            @Override
            protected JSONObject doInBackground(Void... voids) {

                try {
                    URL url = new URL("endpoint");

                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    reader.close();

                    JSONObject data = new JSONObject(result.toString());

                    return data;

                } catch (Exception e) {
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(JSONObject data) {

                if (data == null && error != null) {
                    callback.serviceFailure(error);
                } else {
                    callback.serviceSuccess(data);
                }

            }

        }.execute();
    }
}
