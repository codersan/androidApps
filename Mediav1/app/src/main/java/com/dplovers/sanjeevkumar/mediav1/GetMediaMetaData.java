package com.dplovers.sanjeevkumar.mediav1;

import android.media.MediaMetadataRetriever;
import android.util.Log;

/**
 * Created by sanjeevkumar on 12/13/15.
 * return AlbumMetaData from file_path input
 * Call getMediaMetaData given file_path
 */
public class GetMediaMetaData {

    public static AlbumMetaData mediaMetaData(String file_path) {
        AlbumMetaData albumMetaData = new AlbumMetaData();
        try {
            if(MainActivity.mmr == null) {
                MainActivity.mmr = new MediaMetadataRetriever();
            }
            MainActivity.mmr.setDataSource(file_path);

            albumMetaData.file_path = file_path;
            albumMetaData.title = MainActivity.mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            albumMetaData.album_artist = MainActivity.mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
            albumMetaData.artist = MainActivity.mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            albumMetaData.author = MainActivity.mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
            albumMetaData.genre = MainActivity.mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
            albumMetaData.duration = MainActivity.mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            albumMetaData.composer = MainActivity.mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER);
            albumMetaData.writer = MainActivity.mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER);
            albumMetaData.album_art = MainActivity.mmr.getEmbeddedPicture();
        }
        catch (Exception ex) {
            Log.e("Error: ", "Unable to get meta data");
        }
        if(albumMetaData.file_path == null || albumMetaData.file_path.isEmpty()) albumMetaData.file_path = "";
        if(albumMetaData.album_artist == null || albumMetaData.album_artist.isEmpty()) albumMetaData.album_artist = "Album Artist NA";
        if(albumMetaData.title == null || albumMetaData.title.isEmpty()) albumMetaData.title = CommonFunctions.getFileName(albumMetaData.file_path);
        if(albumMetaData.artist == null || albumMetaData.artist.isEmpty()) albumMetaData.artist = "Artist MA";
        if(albumMetaData.author == null || albumMetaData.author.isEmpty()) albumMetaData.author = "Author NA";
        if(albumMetaData.genre == null || albumMetaData.genre.isEmpty()) albumMetaData.genre = "Genre NA";
        if(albumMetaData.duration == null || albumMetaData.duration.isEmpty()) albumMetaData.duration = "-:-";
        if(albumMetaData.composer == null || albumMetaData.composer.isEmpty()) albumMetaData.composer = "Composer NA";
        if(albumMetaData.writer == null || albumMetaData.writer.isEmpty()) albumMetaData.writer = "Writer NA";

        return albumMetaData;
    }
}
