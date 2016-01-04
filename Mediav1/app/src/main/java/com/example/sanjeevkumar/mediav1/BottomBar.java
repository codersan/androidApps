package com.example.sanjeevkumar.mediav1;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by sanjeevkumar on 12/21/15.
 */
public class BottomBar {

    private RelativeLayout BottomStripLayout;
    private ImageView  bottom_bar_play_pause_current;
    private boolean alreadyPlaying = true;
    public static Activity activity;

    public BottomBar(Activity _activity) {
        this.activity = _activity;
        BottomStripLayout = (RelativeLayout) this.activity.findViewById(R.id.BottomStripLayout);
        bottom_bar_play_pause_current = (ImageView) this.activity.findViewById(R.id.bottom_bar_play_pause_current);
        setupBottomBar();
    }

    public void setupBottomBar() {
        initForAppLoad();
        setupBottomBarContent();
        registerClickEventForOpeningNewIntent();
    }

    private void initForAppLoad() {

        if(MainActivity.current_song_id == -1) {
            MainActivity.current_song_id = 0;
            alreadyPlaying = false;
            try {
                MainActivity.mediaPlayer.setDataSource(AlbumListAdapter.albumMetaDataList.get(MainActivity.current_song_id).file_path);
                MainActivity.mediaPlayer.prepareAsync();
                MainActivity.mediaPlayer.start();
                MainActivity.mediaPlayer.pause();
            }
            catch (Exception ex) {

            }
        }
    }
    private void registerClickEventForOpeningNewIntent() {

        BottomStripLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = MainActivity.current_song_id;
                GoToPlayerScreenActivityHelper goToPlayerScreenActivityHelper = new GoToPlayerScreenActivityHelper();
                goToPlayerScreenActivityHelper.setPlayerScreenActivityIntent(position, true);
            }
        });

        bottom_bar_play_pause_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MainActivity.mediaPlayer.isPlaying()) {
                    Log.i("MediaPlayer Info!!", "play paused");
                    MediaPlayerActivity.pause();
                }
                else {
                    Log.i("MediaPlayer Info!!", "play resume");
                    MediaPlayerActivity.resume();
                }
            }
        });

    }

    private void setupBottomBarContent() {
        ImageView bottom_bar_album_art = (ImageView) this.activity.findViewById(R.id.bottom_bar_album_art);
        TextView bottom_bar_title = (TextView) this.activity.findViewById(R.id.bottom_bar_title);
        TextView bottom_bar_album_artist = (TextView) this.activity.findViewById(R.id.bottom_bar_album_artist);
        ImageView bottom_bar_play_pause_current = (ImageView) this.activity.findViewById(R.id.bottom_bar_play_pause_current);

        String file_path = AlbumListAdapter.albumMetaDataList.get(MainActivity.current_song_id).file_path;
        AlbumMetaData albumMetaData = GetMediaMetaData.mediaMetaData(file_path);

        if( albumMetaData.album_art != null) {
            bottom_bar_album_art.setImageBitmap(BitmapFactory.decodeByteArray(albumMetaData.album_art, 0, albumMetaData.album_art.length));
        }
        else {
            bottom_bar_album_art.setImageResource(R.drawable.no_album_art);
        }

        bottom_bar_title.setText(albumMetaData.title.substring(0, Math.min(albumMetaData.title.length(),25)));
        bottom_bar_album_artist.setText(albumMetaData.album_artist.substring(0, Math.min(albumMetaData.album_artist.length(),25)));

        if(MainActivity.is_paused) {
            bottom_bar_play_pause_current.setImageResource(android.R.drawable.ic_media_play);
        }
        else {
            bottom_bar_play_pause_current.setImageResource(android.R.drawable.ic_media_pause);
        }

    }
}
