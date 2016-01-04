package com.example.sanjeevkumar.backgroundmedia;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by sanjeevkumar on 12/13/15.
 * Return files path either from local storage or from sd card.
 */
public class GetMediaFilePathList {

    SharedPreferences sharedPreferences = MainActivity.getContext().getSharedPreferences(MainActivity.getContext().getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE);
    public static List<String> media_file_path_list = new ArrayList<>();

    public List<String> getFilePaths() {
        //update shared storage, if not already present
        //TODO : Remove this
        Log.i("Info: ", "get File paths ");
        boolean auto_update = true;
        if(updateRequired() == true || auto_update == true) {
            Log.i("Update required", "true");
            ExtractMediaFilesPathFromDeviceStorage extractMediaFilesPathFromDeviceStorage = new ExtractMediaFilesPathFromDeviceStorage();
            media_file_path_list = extractMediaFilesPathFromDeviceStorage.extractFilesFromDeviceStorage();
            setFilePathsInSharedPreference(media_file_path_list);
        }

        media_file_path_list = getFilePathsFromSharedPreference();
        return media_file_path_list;
    }

    private boolean updateRequired() {
        boolean update_required = false;
        if(sharedPreferences.getString("last_updated", null) == null) {
            update_required = true;
        }
        return update_required;
    }

    private List<String> getFilePathsFromSharedPreference() {

        List<String> file_paths = new ArrayList<>();
        Map<String, ?> allEntries = sharedPreferences.getAll();
        File file;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            file = new File(entry.getValue().toString());
            if(file.exists() == true  && entry.getKey() != "last_updated") {
                file_paths.add(entry.getValue().toString());
            }
        }
        return file_paths;
    }

    /*
        @param: List of file path
        set file paths in shared Preference in sorted order of title
     */
    private void setFilePathsInSharedPreference(List<String> file_paths) {

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Integer countFiles = file_paths.size();

        for(Integer file = 0; file < countFiles; file++) {
            mmr.setDataSource(file_paths.get(file));
            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            editor.putString(title, file_paths.get(file));
        }
        //set update time
        Calendar c = Calendar.getInstance();
        editor.putString("last_updated", c.toString());
        editor.commit();
        mmr.release();
    }
}
