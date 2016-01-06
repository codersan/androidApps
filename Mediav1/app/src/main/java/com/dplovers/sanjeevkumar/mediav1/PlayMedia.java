package com.dplovers.sanjeevkumar.mediav1;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by sanjeevkumar on 12/12/15.
 * Will play song
 */
public class PlayMedia {

    //public MediaPlayer mediaPlayer;
    private Activity activity;
    public PlayMedia(Activity _activity) {
        this.activity = _activity;
    }
    public void onMediaItemClick(int position, boolean fromBottomStrip) {

        String file_path = MainActivity.current_song_file_path;
        if(fromBottomStrip == false) {
            startPlaying(file_path);
        }
        Log.i("Current Song ", file_path);
        setScreen(file_path);
    }

    private AlbumMetaData getMediaInfo(int position) {
        return AlbumListAdapter.albumMetaDataList.get(position);
    }

    private void startPlaying(String file_path) {
        Log.i("start playing", "true");
        MediaPlayerActivity mediaPlayerActivity = new MediaPlayerActivity(activity);
        mediaPlayerActivity.play(file_path);
        SongListenTracker.updateCount(file_path);
    }

    private void setScreen(String file_path) {
        ImageView player_activity_play_pause_current = (ImageView) PlayerScreenActivity.activity.findViewById(R.id.player_activity_play_pause_current);

        player_activity_play_pause_current.setClickable(true);
        PlayScreenUIEvents playScreenUIEvents = new PlayScreenUIEvents(activity);
        Log.i("set playScreenUI", "called");
        Log.i("set playScreenUI", file_path);
        playScreenUIEvents.init(file_path);
    }
}
