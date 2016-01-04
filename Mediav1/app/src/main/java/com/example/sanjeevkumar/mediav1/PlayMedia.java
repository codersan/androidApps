package com.example.sanjeevkumar.mediav1;

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

        AlbumMetaData albumMetaData = getMediaInfo(position);
        Log.i("File Path: ", albumMetaData.file_path);
        if(fromBottomStrip == false) {
            startPlaying(albumMetaData);
        }
        setScreen(albumMetaData.file_path);
    }

    private AlbumMetaData getMediaInfo(int position) {
        return AlbumListAdapter.albumMetaDataList.get(position);
    }

    private void startPlaying(AlbumMetaData albumMetaData) {
        Log.i("start playing", "true");
        MediaPlayerActivity mediaPlayerActivity = new MediaPlayerActivity(activity);
        mediaPlayerActivity.play(albumMetaData.file_path);
        SongListenTracker.updateCount(albumMetaData.file_path);
    }

    private void setScreen(String file_path) {
        ImageView player_activity_play_pause_current = (ImageView) PlayerScreenActivity.activity.findViewById(R.id.player_activity_play_pause_current);

        player_activity_play_pause_current.setClickable(true);
        PlayScreenUIEvents playScreenUIEvents = new PlayScreenUIEvents(activity);
        Log.i("set playScreenUI", "called");
        playScreenUIEvents.init(file_path);
    }
}
