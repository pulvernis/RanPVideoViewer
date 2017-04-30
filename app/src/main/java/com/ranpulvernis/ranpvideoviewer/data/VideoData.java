package com.ranpulvernis.ranpvideoviewer.data;

import org.json.JSONObject;

/**
 * Created by ranpulvernis on 17/04/2017.
 */

public class VideoData {

    private String videoName;
    private String videoCode;

    public String getVideoName() {
        return videoName;
    }

    public String getVideoCode() {
        return videoCode;
    }

    public void populate(JSONObject data) {

        videoName = data.optString("video_name");
        videoCode = data.optString("video_code");

    }
}
