package com.dplovers.sanjeevkumar.mediav1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
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

    public static List<AlbumMetaData> albumMetaDataList;

    public AlbumListAdapter(String search_text) {
        albumMetaDataList = getDataForListView(search_text);
    }

    @Override
    public int getCount() {
        return albumMetaDataList.size();
    }

    //TODO :
    @Override
    public AlbumMetaData getItem(int id) {
        return albumMetaDataList.get(id);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int id, View rowView, ViewGroup parent) {
        ViewHolder holder;
        if(rowView == null) {
            LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(R.layout.album_list_item_layout, parent ,false);
            holder = new ViewHolder();
            holder.album_title = (TextView) rowView.findViewById(R.id.title);
            holder.album_artist = (TextView) rowView.findViewById(R.id.album_artist);
            holder.album_art = (ImageView) rowView.findViewById(R.id.album_art);
            rowView.setTag(holder);
        }
        else {
            holder = (ViewHolder) rowView.getTag();
        }
        AlbumMetaData albumMetaData = getItem(id);
        if(albumMetaData.file_path == "" || albumMetaData.file_path == null) return null;

        holder.album_title.setText(albumMetaData.title);
        holder.album_artist.setText(albumMetaData.album_artist);
        holder.album_art.setImageResource(R.drawable.no_album_art);
        holder.file_path = albumMetaData.file_path;
        holder.position = id;

//start
        final int finalPosition = id;
        try {
            new AsyncTask<ViewHolder, Void, Bitmap>() {

                ViewHolder v;

                @Override
                protected Bitmap doInBackground(AlbumListAdapter.ViewHolder ... params) {
                    v = params[0];
                    return getAlbumArtFromMediaMetaData(v.position);

                }

                @Override
                protected void onPostExecute(Bitmap bmp) {

                    if (v.position == finalPosition) {
                        if(bmp != null) {
                            v.album_art.setImageBitmap(bmp);
                        }
                    }
                }


                private Bitmap getAlbumArtFromMediaMetaData(int pos) {
                    Bitmap bmp = null;
                    String file_path = AlbumListAdapter.albumMetaDataList.get(pos).file_path;
                    GetMediaMetaData getMediaMetaData = new GetMediaMetaData();
                    byte[] album_art = GetMediaMetaData.mediaMetaData(file_path).album_art;
                    if(album_art != null) {
                        bmp = BitmapFactory.decodeByteArray(album_art, 0, album_art.length);
                    }
                    return bmp;
                }
            }.execute(holder);
        }
        catch (Exception ex) {
            Log.e("", "Error in setting album art");
        }

//end



        return rowView;
    }

    public List<AlbumMetaData> getDataForListView(String search_text) {
        GetAdapterDataList getAdapterDataList = new GetAdapterDataList();
        albumMetaDataList = getAdapterDataList.getData(search_text);
        return albumMetaDataList;
    }

    //static class for handling view for recycle ListView
    public static class ViewHolder {
        TextView album_title;
        TextView album_artist;
        ImageView album_art;
        String file_path;
        int position;
    }
}
