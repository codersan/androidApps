package com.example.sanjeevkumar.backgroundmedia;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    ListView album_list;
    private boolean album_list_clickable = true;
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public static Context getContext() {
        return context;
    }
    public static void setContext(Context mContext) {
        context = mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        MainActivity.setContext(this);
        album_list = (ListView) findViewById(R.id.album_list);

        addAlbums();
        addClickListenerOnAlbumListItem();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //fragment class for different states of App
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(MainActivity.mediaPlayer!= null) {
            Log.i("ERROR! ", "Released");
            MainActivity.mediaPlayer.reset();
            MainActivity.mediaPlayer.release();
            MainActivity.mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    /*
        Add albums to the ListView from device storage
     */

    public void addAlbums() {
        AlbumListAdapter albumListAdapter = new AlbumListAdapter();
        album_list.setAdapter(albumListAdapter);
    }

    public void addClickListenerOnAlbumListItem() {

        album_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Log.i("Info: ", album_list_clickable+"");
                PlayMedia playMedia = new PlayMedia();
                playMedia.onMediaItemClick(position);
            }
        });
    }
}
