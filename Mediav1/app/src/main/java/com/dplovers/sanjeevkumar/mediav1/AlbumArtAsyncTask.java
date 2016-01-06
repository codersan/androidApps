package com.dplovers.sanjeevkumar.mediav1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * Created by sanjeevkumar on 12/21/15.
 */

public class AlbumArtAsyncTask extends AsyncTask<AlbumListAdapter.ViewHolder, Void, Bitmap> {

    private AlbumListAdapter.ViewHolder v;

    @Override
    protected Bitmap doInBackground(AlbumListAdapter.ViewHolder ... params) {
        v = params[0];
        return getAlbumArtFromMediaMetaData(v.file_path);

    }

    @Override
    protected void onPostExecute(Bitmap bmp) {
        super.onPostExecute(bmp);
        if (v.position == 1) {
            if(bmp != null) {
                v.album_art.setImageBitmap(bmp);
            } else {
                v.album_art.setImageResource(R.drawable.no_album_art);
            }
        }
    }


    private Bitmap getAlbumArtFromMediaMetaData(String id) {
        Bitmap bmp = null;
        int pos = Integer.parseInt(id);
        String file_path = AlbumListAdapter.albumMetaDataList.get(pos).file_path;
        GetMediaMetaData getMediaMetaData = new GetMediaMetaData();
        byte[] album_art = GetMediaMetaData.mediaMetaData(file_path).album_art;
        if(album_art != null) {
            bmp = BitmapFactory.decodeByteArray(album_art, 0, album_art.length);
        }
        return bmp;
    }
}
