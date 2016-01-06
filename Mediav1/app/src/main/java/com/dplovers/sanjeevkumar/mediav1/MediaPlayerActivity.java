package com.dplovers.sanjeevkumar.mediav1;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by sanjeevkumar on 12/13/15.
 */

public class MediaPlayerActivity {

    private static Activity activity;
    public  MediaPlayerActivity(Activity _activity) {
        activity = _activity;
    }

    //public MediaPlayer mediaPlayer;
    public void play(String src) {
        if(MainActivity.mediaPlayer ==null) {
            MainActivity.mediaPlayer = new MediaPlayer();
            MainActivity.mediaPlayer.setWakeMode(MainActivity.activity, PowerManager.PARTIAL_WAKE_LOCK);
        }
        try {

            //if(MainActivity.mediaPlayer.isPlaying() == true || MainActivity.is_paused == false) {
                Log.d("DEBUG!!!", "isplaying");
                MainActivity.mediaPlayer.reset();
                MainActivity.mediaPlayer.release();
                MainActivity.mediaPlayer = new MediaPlayer();
            //}

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
                    Log.i("Completeed ", "hey hey");
                    if (MainActivity.play_looping) {
                        SongListenTracker.updateCount(MainActivity.current_song_file_path);
                        Log.i("Completeed ", " loop");
                        mp.setLooping(true);
                    } else if (MainActivity.play_next) {
                        Log.i("Completeed ", "next");
                        playNext();
                    } else {
                        Log.i("Completeed ", "completed");
                        mp.reset();
                        mp.release();
                        MainActivity.mediaPlayer = null;
                    }

                }
            });
            MainActivity.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener(){
                public boolean onError(MediaPlayer mr, int what, int extra ){
                    Log.d("Error in mediaplayer", "what : " + what + " extra: "+ extra);
                    MainActivity.mediaPlayer.reset();
                    return true;
                }
            });
        }
        catch (Exception ex) {

        }
        new BottomBar(MainActivity.activity);
        showMediaPauseIcon();
        MainActivity.is_paused = false;
    }

    public static void pause() {
        MainActivity.is_paused = true;
        MainActivity.mediaPlayer.pause();
        showMediaPlayIcon();
    }


    public static void resume() {
        MainActivity.is_paused = false;
        MainActivity.mediaPlayer.start();
        showMediaPauseIcon();
    }

    public static void playPrev() {
        MainActivity.is_paused = false;
        boolean alreadyPlaying = false;
        int position = MainActivity.current_song_id;
        //if play list is activated then go to prev song else repeat
        if(MainActivity.play_list) {
            position--;
            position = (position + AlbumListAdapter.albumMetaDataList.size()) % AlbumListAdapter.albumMetaDataList.size();
            MainActivity.current_song_id = position;
            MainActivity.current_song_file_path = AlbumListAdapter.albumMetaDataList.get(position).file_path;
        }
        GoToPlayerScreenActivityHelper.setUpPlayer(position, alreadyPlaying, activity);
        showMediaPauseIcon();
    }

    public static void playNext() {
        MainActivity.is_paused = false;
        boolean alreadyPlaying = false;
        int position = MainActivity.current_song_id;
        //if play list is activated then go to next song else repeat
        if(MainActivity.play_list) {
            position++;
            position = position%AlbumListAdapter.albumMetaDataList.size();
            MainActivity.current_song_id = position;
            MainActivity.current_song_file_path = AlbumListAdapter.albumMetaDataList.get(position).file_path;
        }
        GoToPlayerScreenActivityHelper.setUpPlayer(position, alreadyPlaying, activity);
        showMediaPauseIcon();
    }

    public static void showMediaPauseIcon() {
        try {
            ImageView player_activity_play_pause_current = (ImageView) PlayerScreenActivity.activity.findViewById(R.id.player_activity_play_pause_current);
            player_activity_play_pause_current.setImageResource(android.R.drawable.ic_media_pause);
        }
        catch (Exception ex) {

        }
        try {
            ImageView bottom_bar_play_pause_current = (ImageView) MainActivity.activity.findViewById(R.id.bottom_bar_play_pause_current);
            bottom_bar_play_pause_current.setImageResource(android.R.drawable.ic_media_pause);
        }
        catch (Exception ex) {

        }
    }
    public static void showMediaPlayIcon() {
        try {
            ImageView player_activity_play_pause_current = (ImageView) PlayerScreenActivity.activity.findViewById(R.id.player_activity_play_pause_current);
            player_activity_play_pause_current.setImageResource(android.R.drawable.ic_media_play);

        }
        catch (Exception ex) {

        }
        try {
            ImageView bottom_bar_play_pause_current = (ImageView) MainActivity.activity.findViewById(R.id.bottom_bar_play_pause_current);
            bottom_bar_play_pause_current.setImageResource(android.R.drawable.ic_media_play);
        }
        catch (Exception ex) {

        }
    }
}
