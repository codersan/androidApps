package com.dplovers.sanjeevkumar.mediav1;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by sanjeevkumar on 12/13/15.
 * Return DataList either from local storage or from sd card.
 */

public class GetAdapterDataList {

    SharedPreferences sharedPreferencesMediaMetaData = MainActivity.getContext().getSharedPreferences(MainActivity.getContext().getString(R.string.shared_preference_file_key_media_metadata), Context.MODE_PRIVATE);
    public static List<AlbumMetaData> album_metadata_list = new ArrayList<>();
    public static List<String> media_file_path_list = new ArrayList<>();
    public static boolean sync = false;

    public List<AlbumMetaData> getData(String search_text) {
        //update shared storage, if not already present
        if(updateRequired() == true || sync == true) {
            ExtractMediaFilesPathFromDeviceStorage extractMediaFilesPathFromDeviceStorage = new ExtractMediaFilesPathFromDeviceStorage();
            media_file_path_list = extractMediaFilesPathFromDeviceStorage.extractFilesFromDeviceStorage();
            setAlbumDataInSharedPreference(media_file_path_list);
        }

        album_metadata_list = getAlbumDataFromSharedPreference(search_text);
        return album_metadata_list;
    }

    private boolean updateRequired() {
        boolean update_required = false;
        if(sharedPreferencesMediaMetaData.getString("last_updated", null) == null) {
            update_required = true;
        }
        return update_required;
    }

    private List<AlbumMetaData> getAlbumDataFromSharedPreference(String search_text) {

        List<AlbumMetaData> album_metadata_list = new ArrayList<>();
        List<String> file_paths = new ArrayList<>();

        Map<String, ?> allEntries = sharedPreferencesMediaMetaData.getAll();
        File file;
        ObjectStringConversion objectStringConversion = new ObjectStringConversion();
        AlbumMetaData albumMetaData;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            file = new File(entry.getKey());
            String val = entry.getValue().toString();
            if(entry.getKey() != "last_updated") {
                albumMetaData = objectStringConversion.stringToAlbumMetaData(val);
                if (file.exists() == true) {
                    boolean isPresent = searchInAlbumMetaData(albumMetaData, search_text);
                    if (isPresent == true) {
                        file_paths.add(entry.getKey().toString());
                        album_metadata_list.add(albumMetaData);
                    }
                }
            }
        }
        return album_metadata_list;
    }

    /*
        @param: List of file path
        set album meta data in shared Preference in sorted order of title
     */
    private void setAlbumDataInSharedPreference(List<String> file_paths) {

        sharedPreferencesMediaMetaData.edit().clear().commit();
        SharedPreferences.Editor editor = sharedPreferencesMediaMetaData.edit();

        Integer countFiles = file_paths.size();
        AlbumMetaData albumMetaData;
        GetMediaMetaData getMediaMetaData = new GetMediaMetaData();
        ObjectStringConversion objectStringConversion = new ObjectStringConversion();
        for(Integer file = 0; file < countFiles; file++) {
            albumMetaData = GetMediaMetaData.mediaMetaData(file_paths.get(file));
            String val = objectStringConversion.albumMetaDataToString(albumMetaData);
            editor.putString(file_paths.get(file), val);
        }
        //set update time
        Calendar c = Calendar.getInstance();
        editor.putString("last_updated", c.toString());
        editor.commit();
    }

    private boolean searchInAlbumMetaData(AlbumMetaData albumMetaData, String search_text) {
        boolean isPresent = false;
//        Log.i("title : ", albumMetaData.title);
//        Log.i("Album Artist : ", albumMetaData.album_artist);
//        Log.i("Artist : ", albumMetaData.artist);
//        Log.i("Author  : ", albumMetaData.author);
//        Log.i("Genre : ", albumMetaData.genre);
//        Log.i("Artist : ", albumMetaData.artist);
//        Log.i("Writer : ", albumMetaData.writer);
//        Log.i("Composer : ", albumMetaData.composer);
//        Log.i("Search text", search_text);

        search_text = search_text.trim().toLowerCase();
        if( albumMetaData.title.toLowerCase().contains(search_text)
            || albumMetaData.album_artist.toLowerCase().contains(search_text)
            || albumMetaData.author.toLowerCase().contains(search_text)
            || albumMetaData.artist.toLowerCase().contains(search_text)
            || albumMetaData.genre.toLowerCase().contains(search_text)
            || albumMetaData.duration.toLowerCase().contains(search_text)
            || albumMetaData.composer.toLowerCase().contains(search_text)
            || albumMetaData.writer.toLowerCase().contains(search_text)
        ) {
            isPresent = true;
        }
        return isPresent;
    }
}
