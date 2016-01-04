package com.example.sanjeevkumar.backgroundmedia;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanjeevkumar on 12/13/15.
 * return AlbumMetaData from file_path input
 * Call getMediaMetaData given file_path
 */
public class GetMediaMetaData {

    MediaMetadataRetriever mmr;

    public AlbumMetaData mediaMetaData(String file_path) {
        AlbumMetaData albumMetaData = new AlbumMetaData();
        try {
            if(mmr == null) {
                mmr = new MediaMetadataRetriever();
            }
            mmr.setDataSource(file_path);

            albumMetaData.file_path = file_path;
            albumMetaData.title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            albumMetaData.album_artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
            albumMetaData.artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            albumMetaData.author = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
            albumMetaData.genre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
            albumMetaData.duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            //albumMetaData.album_art = mmr.getEmbeddedPicture();
        }
        catch (Exception ex) {
            Log.e("Error: ", "Unable to get meta data");
        }
        if(albumMetaData.file_path == null || albumMetaData.file_path.isEmpty()) albumMetaData.file_path = "";
        if(albumMetaData.album_artist == null || albumMetaData.album_artist.isEmpty()) albumMetaData.album_artist = "Unknown Album Artist";
        if(albumMetaData.title == null || albumMetaData.title.isEmpty()) albumMetaData.title = "Unknown Title";
        if(albumMetaData.artist == null || albumMetaData.artist.isEmpty()) albumMetaData.artist = "Unknown Artist";
        if(albumMetaData.author == null || albumMetaData.author.isEmpty()) albumMetaData.author = "Unknown Author";
        if(albumMetaData.genre == null || albumMetaData.genre.isEmpty()) albumMetaData.genre = "Unknown Genre";
        if(albumMetaData.duration == null || albumMetaData.duration.isEmpty()) albumMetaData.duration = "-:-";
        mmr.release();
        return albumMetaData;
    }
}
