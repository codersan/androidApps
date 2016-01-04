package com.example.sanjeevkumar.mediav1;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static Context context;

    public final static String EXTRA_MESSAGE_POS = "com.example.sanjeevkumar.mediav1.MESSAGE_POS";
    public final static String EXTRA_MESSAGE_SRC = "com.example.sanjeevkumar.mediav1.MESSAGE_SRC";
    public static MediaPlayer mediaPlayer;
    public static Activity activity;
    public ListView album_list;
    public static MediaMetadataRetriever mmr;
    public static Integer current_song_id = -1;
    public static boolean is_paused = true;
    public static boolean play_looping = false;
    public static boolean play_next = true;
    public static Context getContext() {
        return context;
    }
    public static void setContext(Context mContext) {
        context = mContext;
    }

    public MainActivity() {
        mediaPlayer = new MediaPlayer();
        //MainActivity.mediaPlayer.setWakeMode(MainActivity.getContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mmr = new MediaMetadataRetriever();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.setContext(this);
        album_list = (ListView) findViewById(R.id.album_list);
        String search_text = ""; // initially populate with whole data
        populateSongsList(search_text);
        addClickListenerOnAlbumListItem();
        addButtonBar();
        this.activity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String search_text) {
                populateSongsList(search_text);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.sync:
                GetAdapterDataList.sync = true;
                Toast.makeText(MainActivity.this, "Songs list is updating. Please wait...", Toast.LENGTH_LONG).show();
                populateSongsList("");
                Toast.makeText(MainActivity.this, "Updated songs list", Toast.LENGTH_LONG).show();
                GetAdapterDataList.sync = false;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            Log.i("Success! ", "Released");
            MainActivity.mediaPlayer.reset();
            MainActivity.mediaPlayer.release();
            MainActivity.mediaPlayer = null;
        }
        if(MainActivity.mmr!= null) {
            Log.i("Success! ", "Released");
            MainActivity.mmr.release();
            MainActivity.mmr = null;
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

    public void populateSongsList(String search_text) {
        AlbumListAdapter albumListAdapter = new AlbumListAdapter(search_text);
        album_list.setAdapter(albumListAdapter);
    }

    public void addClickListenerOnAlbumListItem() {

        album_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                current_song_id = position;
                boolean alreadyPlaying = false;
                GoToPlayerScreenActivityHelper goToPlayerScreenActivityHelper = new GoToPlayerScreenActivityHelper();
                goToPlayerScreenActivityHelper.setPlayerScreenActivityIntent(position, alreadyPlaying);
            }
        });
    }

    private void addButtonBar() {
            new BottomBar(this);
    }

}
