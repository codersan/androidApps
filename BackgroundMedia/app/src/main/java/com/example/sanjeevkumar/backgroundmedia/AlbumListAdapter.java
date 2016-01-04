package com.example.sanjeevkumar.backgroundmedia;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by sanjeevkumar on 12/12/15.
 * Called from MainActivity Class
 * Creates Album List View by getting data from device storage
 */

public class AlbumListAdapter extends BaseAdapter{

    public static List<String> media_file_path_list;

    public AlbumListAdapter() {
        media_file_path_list = getDataForListView();
    }

    @Override
    public int getCount() {
        return media_file_path_list.size();
    }

    @Override
    public AlbumMetaData getItem(int id) {
        GetMediaMetaData getMediaMetaData = new GetMediaMetaData();
        return getMediaMetaData.mediaMetaData(media_file_path_list.get(id));
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int id, View rowView, ViewGroup parent) {
        if(rowView == null) {
            LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(R.layout.album_list_item_layout, parent ,false);
        }
        TextView album_title = (TextView) rowView.findViewById(R.id.title);
        TextView album_artist = (TextView) rowView.findViewById(R.id.album_artist);
        ImageView album_art = (ImageView) rowView.findViewById(R.id.album_art);

        AlbumMetaData albumMetaData = getItem(id);
        if(albumMetaData.file_path == "" || albumMetaData.file_path == null) return null;

        album_title.setText(albumMetaData.title);
        album_artist.setText(albumMetaData.album_artist);
        //TODO :
        //if( albumMetaData.album_art !=null ){
            if(false){
          //  album_art.setImageBitmap( BitmapFactory.decodeByteArray(albumMetaData.album_art, 0, albumMetaData.album_art.length));
        }
        else {
            album_art.setImageResource(R.drawable.no_album_art);
        }

        return rowView;
    }

    public List<String> getDataForListView() {
        GetMediaFilePathList getMediaFilePathList = new GetMediaFilePathList();
        media_file_path_list = getMediaFilePathList.getFilePaths();
//        ExtractMediaFilesPathFromDeviceStorage getAllAlbumsFromDevice = new ExtractMediaFilesPathFromDeviceStorage();
//        albumMetaDataList = getAllAlbumsFromDevice.addMediaMetaDataList();
        return media_file_path_list;
    }
}
