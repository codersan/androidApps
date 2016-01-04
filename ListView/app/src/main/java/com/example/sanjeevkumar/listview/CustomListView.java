package com.example.sanjeevkumar.listview;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanjeevkumar on 12/12/15.
 */
public class CustomListView extends BaseAdapter {

    List<AlbumMetaData> songMetaDataList;

    public CustomListView() {
        songMetaDataList = getDataForListView();
    }

    @Override
    public int getCount() {
        return songMetaDataList.size();
    }

    @Override
    public Object getItem(int id) {
        return songMetaDataList.get(id);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int id, View rowView, ViewGroup parent) {
        if(rowView == null) {
            LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(R.layout.custom_list_item, parent ,false);
        }
        TextView album_title = (TextView) rowView.findViewById(R.id.album_title);
        TextView album_artist = (TextView) rowView.findViewById(R.id.album_artist);

        AlbumMetaData songMetaData = songMetaDataList.get(id);
        album_title.setText(songMetaData.album_title);
        album_artist.setText(songMetaData.album_artist);
        return rowView;
    }

    public List<AlbumMetaData> getDataForListView() {

        songMetaDataList = new ArrayList<AlbumMetaData>();

        for(Integer i=0;i<10;i++) {
            AlbumMetaData songMetaData = new AlbumMetaData();
            songMetaData.album_artist = "Album Artist";
            songMetaData.album_title = "Title";
            songMetaData.artist = "Artist";
            songMetaData.author = "Author";
            songMetaData.duration = 4;
            songMetaData.genre = "Genre";
            songMetaDataList.add(songMetaData);
        }
        return songMetaDataList;
    }

}
