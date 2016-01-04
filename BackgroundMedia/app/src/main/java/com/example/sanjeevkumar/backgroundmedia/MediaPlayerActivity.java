package com.example.sanjeevkumar.backgroundmedia;

import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by sanjeevkumar on 12/13/15.
 */
public class MediaPlayerActivity {

    //public MediaPlayer mediaPlayer;
    public void play(String src) {
        if(MainActivity.mediaPlayer ==null) {
            MainActivity.mediaPlayer = new MediaPlayer();
        }
        try {
            Log.d("DEBUG!!!", MainActivity.mediaPlayer.isPlaying() + "");
            if(MainActivity.mediaPlayer.isPlaying() == true) {
                Log.d("DEBUG!!!", "isplaying");
                MainActivity.mediaPlayer.reset();
                MainActivity.mediaPlayer.release();
                MainActivity.mediaPlayer = new MediaPlayer();
            }

            MainActivity.mediaPlayer.setDataSource(src);
            MainActivity.mediaPlayer.prepareAsync();

            MainActivity.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.d("DEBUG! :", "started");
                    mp.start();
                }
            });

            MainActivity.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.d("DEBUG! :","completed");
                    mp.reset();
                    mp.release();
                    MainActivity.mediaPlayer = null;
                }
            });
        }
        catch (Exception ex) {

        }
    }
}
