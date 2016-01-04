package com.example.sanjeevkumar.mediav1;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sanjeevkumar on 12/21/15.
 */

public class SongListenTracker {

    private static SharedPreferences sharedPreferencesListenCount = MainActivity.getContext().getSharedPreferences(MainActivity.getContext().getString(R.string.shared_preference_file_key_listen_count), Context.MODE_PRIVATE);

    public static void updateCount(String file_path) {
        SharedPreferences.Editor editor = sharedPreferencesListenCount.edit();
        int prev_listened_count = sharedPreferencesListenCount.getInt(file_path, 0);
        int updated_listen_count = prev_listened_count + 1;
        editor.putInt(file_path,updated_listen_count);
        editor.commit();
    }

    public static int getCount(String file_path) {
        int listen_count = sharedPreferencesListenCount.getInt(file_path, 0);
        return listen_count;
    }
}
