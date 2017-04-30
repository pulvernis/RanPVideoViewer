package com.ranpulvernis.ranpvideoviewer;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;

/**
 * Created by ranpulvernis on 27/04/2017.
 */

public class VideoViewFromList extends AppCompatActivity {

    private android.widget.VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private TextView txtCbVideoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the main layout of the activity
        setContentView(R.layout.presented_video);

        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(VideoViewFromList.this);
        }

        //initialize the VideoViewFromList
        myVideoView = (android.widget.VideoView) findViewById(R.id.video_view);
        txtCbVideoName = (TextView) findViewById(R.id.txtVideoName);

        // create a progress bar while the video file is loading
        progressDialog = new ProgressDialog(VideoViewFromList.this);
        // set a title for the progress bar
        progressDialog.setTitle("Video View Example");
        // set a message for the progress bar
        progressDialog.setMessage("Loading...");
        //set the progress bar not cancelable on users' touch
        progressDialog.setCancelable(false);
        // show the progress bar
        progressDialog.show();

        try {
            //set the media controller in the VideoViewFromList
            myVideoView.setMediaController(mediaControls);

            // get video name and code from the row clicked from video list in MainActivity Class
            String videoNameStr = getIntent().getStringExtra("video_name");
            txtCbVideoName.setText(videoNameStr);
            String videoCodeStr = getIntent().getStringExtra("video_code");
            //set the uri of the video to be played
            myVideoView.setVideoURI(Uri.parse("parse by given server URI include ->" + videoCodeStr));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        myVideoView.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                // close the progress bar and play the video
                progressDialog.dismiss();
                //if we have a position on savedInstanceState, the video playback should start from here
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    myVideoView.pause();
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //we use onRestoreInstanceState in order to play the video playback from the stored position
        position = savedInstanceState.getInt("Position");
        myVideoView.seekTo(position);
    }
}
