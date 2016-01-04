package com.example.sanjeevkumar.mediav1;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;

/**
 * Created by sanjeevkumar on 12/18/15.
 */

public class PlayScreenUIEvents {

    public Activity activity;
    private SeekBar seekbar;
    private AlbumMetaData albumMetaData;
    private Handler songTimeHandler = new Handler();
    private double current_time = 0;
    private double final_time = 0;
    private boolean total_duration_set = false;
    private ImageView album_art;
    private TextView title;
    private TextView final_time_text_view;
    private TextView current_time_text_view;
    private ImageView play_prev;
    private ImageView play_pause_current;
    private ImageView play_next;
    private TextView listen_count;
    private ImageView play_loop;


    PlayScreenUIEvents(Activity _activity) {
        this.activity = _activity;
        seekbar = (SeekBar) PlayerScreenActivity.activity.findViewById(R.id.seekBar);
        play_prev = (ImageView) PlayerScreenActivity.activity.findViewById(R.id.player_activity_play_prev);;
        play_pause_current = (ImageView) PlayerScreenActivity.activity.findViewById(R.id.player_activity_play_pause_current);
        play_next = (ImageView) PlayerScreenActivity.activity.findViewById(R.id.player_activity_play_next);
        play_loop = (ImageView) PlayerScreenActivity.activity.findViewById(R.id.player_activity_play_loop);
        album_art = (ImageView) PlayerScreenActivity.activity.findViewById(R.id.album_art);
        title = (TextView) PlayerScreenActivity.activity.findViewById(R.id.title);
        current_time_text_view = (TextView) PlayerScreenActivity.activity.findViewById(R.id.current_time_text_view);
        final_time_text_view = (TextView) PlayerScreenActivity.activity.findViewById(R.id.final_time_text_view);
        listen_count = (TextView) PlayerScreenActivity.activity.findViewById(R.id.listen_count);
        if(MainActivity.is_paused) {
            play_pause_current.setImageResource(android.R.drawable.ic_media_play);
        }
        else {
            play_pause_current.setImageResource(android.R.drawable.ic_media_pause);
        }
        registerPlayActions();
    }

    public void init(String file_path) {
        GetMediaMetaData getMediaMetaData = new GetMediaMetaData();
        albumMetaData = getMediaMetaData.mediaMetaData(file_path);
        setSeekbar();
        setContent(albumMetaData);
    }

    private void setContent(AlbumMetaData albumMetaData) {
        title.setText(albumMetaData.title.substring(0,Math.min(albumMetaData.title.length(),28)));
        listen_count.setText("Views:" + SongListenTracker.getCount(albumMetaData.file_path));
        if( albumMetaData.album_art != null) {
            album_art.setImageBitmap(BitmapFactory.decodeByteArray(albumMetaData.album_art, 0, albumMetaData.album_art.length));
        }
        else {
            album_art.setImageResource(R.drawable.no_album_art);
        }
    }

    private void setSeekbar() {
        seekbar.setClickable(true);
        if(total_duration_set == false) {
            final_time = Integer.parseInt(albumMetaData.duration);
            String final_time_string = formattedTime(final_time);
            final_time_text_view.setText(final_time_string);
            seekbar.setMax((int)final_time);
            total_duration_set = true;
        }
        current_time = MainActivity.mediaPlayer.getCurrentPosition();
        seekbar.setProgress((int)current_time);
        songTimeHandler.postDelayed(UpdateSongTime, 500);
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            try {
                current_time = MainActivity.mediaPlayer.getCurrentPosition();
                String current_time_string = formattedTime(current_time);
                current_time_text_view.setText(current_time_string);
                seekbar.setProgress((int)current_time);
                songTimeHandler.postDelayed(this, 500);
            }
            catch (Exception ex) {

            }
        }
    };

    private String formattedTime(double time) {
        String formatted_time = "";
        formatted_time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) time),
                TimeUnit.MILLISECONDS.toSeconds((long) time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) time)));
        return formatted_time;
    }

    private void registerPlayActions() {

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                MainActivity.mediaPlayer.seekTo(progress);
            }
        });

        play_prev.setClickable(true);
        play_pause_current.setClickable(true);
        play_next.setClickable(true);

        play_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MediaPlayer Info!!", "play prev");
                MediaPlayerActivity.playPrev();
            }
        });

        play_pause_current.setOnClickListener(new View.OnClickListener() {
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

        play_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MediaPlayer Info!!", "play next");
                MediaPlayerActivity.playNext();
            }
        });

        play_loop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity.play_looping = !MainActivity.play_looping;
                MainActivity.play_next = !MainActivity.play_next;
                if(MainActivity.play_looping) {
                    play_loop.setBackgroundColor(Color.parseColor("#32CD32"));
                }
                else {
                    play_loop.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                Log.i(" looping : ", MainActivity.play_looping+"");
                Log.i(" next : ", MainActivity.play_next+"");
            }
        });


    }
}
