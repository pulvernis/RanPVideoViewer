package com.ranpulvernis.ranpvideoviewer.service;

import org.json.JSONObject;

/**
 * Created by ranpulvernis on 12/04/2017.
 */

public interface VideoViewerV1 {
    void serviceSuccess(JSONObject data);
    void serviceFailure(Exception exception);
}
