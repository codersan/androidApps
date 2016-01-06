package com.dplovers.sanjeevkumar.mediav1;

/**
 * Created by sanjeevkumar on 12/17/15.
 */
public class ObjectStringConversion {

    String seperator = "-----------";

    public AlbumMetaData stringToAlbumMetaData(String data) {
        AlbumMetaData albumMetaData = new AlbumMetaData();
        String data_list [] = data.split(seperator);

        try {
            albumMetaData.file_path = data_list[0];
            albumMetaData.title = data_list[1];
            albumMetaData.album_artist = data_list[2];
            albumMetaData.author = data_list[3];
            albumMetaData.artist = data_list[4];
            albumMetaData.genre = data_list[5];
            albumMetaData.duration = data_list[6];
            albumMetaData.composer = data_list[7];
            albumMetaData.writer = data_list[8];
            //albumMetaData.album_art = data_list[9];
        }
        catch (Exception ex) {

        }
        return albumMetaData;
    }

    public String albumMetaDataToString(AlbumMetaData albumMetaData) {
//        Log.i("title : ", albumMetaData.title);
//        Log.i("Album Artist : ", albumMetaData.album_artist);
//        Log.i("Artist : ", albumMetaData.artist);
//        Log.i("Author  : ", albumMetaData.author);
//        Log.i("Genre : ", albumMetaData.genre);
//        Log.i("Artist : ", albumMetaData.artist);
//        Log.i("Composer : ", albumMetaData.composer);
//        Log.i("Writer : ", albumMetaData.writer);

        StringBuilder sb = new StringBuilder();
        sb.append(albumMetaData.file_path+seperator);
        sb.append(albumMetaData.title+seperator);
        sb.append(albumMetaData.album_artist+seperator);
        sb.append(albumMetaData.author+seperator);
        sb.append(albumMetaData.author + seperator);
        sb.append(albumMetaData.genre+seperator);
        sb.append(albumMetaData.duration+seperator);
        sb.append(albumMetaData.composer+seperator);
        sb.append(albumMetaData.writer+seperator);
        //sb.append(albumMetaData.album_art);
        String val = sb.toString();
        return val;
    }
}
