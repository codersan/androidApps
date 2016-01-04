package com.example.sanjeevkumar.mediadataretriever;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public TextView mmrText;
    public Button play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mmrText = (TextView) findViewById(R.id.mmr);
        play = (Button) findViewById(R.id.play);
        setMediaPlayer();
    }

    public void setMediaPlayer() {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mediaPlayer = new MediaPlayer();
                String src = "/storage/sdcard0/airdroid/upload/David Guetta - Shot Me Down ft. Skylar Grey (Lyric Video).mp3";
                try {
                    mediaPlayer.setDataSource(src);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                } catch (Exception ex) {
                    Log.e("Error!", "Unable to set data source");
                }

                getMediaMetaData(src);
            }
        });
    }

    public void getMediaMetaData(String src) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(src);
        String info = "";
        String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
        info+=title+artist;
        mmrText.setText(info);
        File f = new File(src);
        String name = f.getName();
        int pos = name.lastIndexOf(".");
        if (pos > 0) {
            name = name.substring(0, pos);
        }
        mmrText.setText(name);
    }
}
