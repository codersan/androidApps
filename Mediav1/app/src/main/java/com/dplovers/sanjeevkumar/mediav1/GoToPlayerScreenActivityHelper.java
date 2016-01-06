package com.dplovers.sanjeevkumar.mediav1;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sanjeevkumar on 12/20/15.
 */
public class GoToPlayerScreenActivityHelper {


    public void setPlayerScreenActivityIntent(int position, boolean alreadyPlaying) {

        MainActivity.current_song_id = position;
        Log.i("Position in setPlaye", position + "");
        Intent player_screen_intent = new Intent(MainActivity.getContext(), PlayerScreenActivity.class);
        player_screen_intent.putExtra(MainActivity.EXTRA_MESSAGE_POS, position);
        player_screen_intent.putExtra(MainActivity.EXTRA_MESSAGE_SRC, alreadyPlaying); //from bottom strip to player screen
        MainActivity.getContext().startActivity(player_screen_intent);
    }

    public static void setUpPlayer(int position, boolean fromBottomStrip, Activity _activity) {
        PlayMedia playMedia = new PlayMedia(_activity);
        playMedia.onMediaItemClick(position, fromBottomStrip);
    }
}
