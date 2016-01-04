package com.example.sanjeevkumar.backgroundmedia;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sanjeevkumar on 12/12/15.
 * Will play song
 */
public class PlayMedia {

    //public MediaPlayer mediaPlayer;

    public void onMediaItemClick(int position) {
        AlbumMetaData albumMetaData = getMediaInfo(position);
        startPlaying(albumMetaData.file_path);
    }

    private AlbumMetaData getMediaInfo(int position) {
        GetMediaMetaData getMediaMetaData = new GetMediaMetaData();
        return getMediaMetaData.mediaMetaData(GetMediaFilePathList.media_file_path_list.get(position));
    }

    private void startPlaying(String file_path) {
//        if(MainActivity.mediaPlayer ==null) {
//            MainActivity.mediaPlayer = new MediaPlayer();
//        }
//        try {
//            Log.d("DEBUG!", MainActivity.mediaPlayer.isPlaying() + "");
//            if(MainActivity.mediaPlayer.isPlaying() == true) {
//                Log.d("DEBUG!", "isplaying");
//                MainActivity.mediaPlayer.reset();
////                MainActivity.mediaPlayer.release();
////                mediaPlayer = null;
////                mediaPlayer = new MediaPlayer();
//            }
//            MainActivity.mediaPlayer.setDataSource(file_path);
//            MainActivity.mediaPlayer.prepare();
//            MainActivity.mediaPlayer.start();
//
//            MainActivity.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    Log.d("DEBUG! :","completed");
//                    mp.reset();
//                    mp.release();
//                    MainActivity.mediaPlayer = null;
//                }
//            });
//        }
//        catch (Exception ex) {
//
//        }
        MediaPlayerActivity mediaPlayerActivity = new MediaPlayerActivity();
        mediaPlayerActivity.play(file_path);
    }
}
