package com.ranpulvernis.ranpvideoviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.ranpulvernis.ranpvideoviewer.data.VideoData;
import com.ranpulvernis.ranpvideoviewer.service.VideoService;
import com.ranpulvernis.ranpvideoviewer.service.VideoViewerV1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements VideoViewerV1{
    ArrayList<DataModel> dataModels;
    ListView listView;
    private CustomAdapter adapter;
    private VideoService service;
    private String[] videoNameStr;
    private String[] videoCodeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = new VideoService(this);
        listView=(ListView)findViewById(R.id.list);

        //fetch data:
        service.refreshMovies();
        //when click on item in the list, video row will start playing from VideoViewFromList activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(view.getContext(), VideoViewFromList.class);
                startActivity(myIntent);
                // passing video_code to load in videoview on VideoViewFromList Class
                myIntent.putExtra("video_code", videoCodeStr);
                myIntent.putExtra("video_name", videoNameStr);

            }
        });
    }
    // fetch data by AsyncTask -> In onPostExecute(JsonObject data) method we called
    // to serviceSuccess method by instance of VideoViewerV1 interface with JSONObject data
    // only if its not null.. otherwise we will use serviceFailure to send an error/exception
    // in this class we are implements VideoViewerV1 interface and we create instance of MovieSerice class
    // with argument 'this' that means instance of VideoViewerV1 interface
    @Override
    public void serviceSuccess(JSONObject data) {
        JSONArray VideoDataJSON = data.optJSONArray("movies_data");
        int JSONLength = VideoDataJSON.length();

        VideoData[] videoDatas = new VideoData[JSONLength];

        for (int i = 0; i < JSONLength; i++) {
            videoDatas[i] = new VideoData();
            try {
                videoDatas[i].populate(VideoDataJSON.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        videoNameStr = new String[JSONLength];
        videoCodeStr = new String[JSONLength];
        dataModels= new ArrayList<>();

        for (int i = 0; i < JSONLength; i++) {
            videoNameStr[i] = videoDatas[i].getVideoName();
            videoCodeStr[i] = videoDatas[i].getVideoCode();
            dataModels.add(new DataModel(videoNameStr[i], videoCodeStr[i]));

        }

        adapter= new CustomAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);

    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
