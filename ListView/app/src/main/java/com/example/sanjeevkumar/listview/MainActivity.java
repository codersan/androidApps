package com.example.sanjeevkumar.listview;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String[] listItems = new String[] { "1","2","3", "4","5","6","7","8","9","10","11","12","13","14","15","1","2","3", "4","5","6","7","8","9","10","11","12","13","14","15"};
//        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, R.layout.list_view_content,listItems);
//        listView = (ListView)findViewById(R.id.list);
//        listView.setAdapter(listAdapter);

//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                // ListView Clicked item index
//                int itemPosition = position;
//
//                // ListView Clicked item value
//                String itemValue = (String) listView.getItemAtPosition(position);
//
//                // Show Alert
//                Toast.makeText(MainActivity.this,"Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();
//            }
//        });

//        CustomListView customListView = new CustomListView();
//        ListView listView = (ListView) findViewById(R.id.album_list);
//        listView.setAdapter(customListView);

    }

    public void playThis(View v) throws IOException {
        MediaPlayer mp = new MediaPlayer();
        mp.setDataSource("/storage/sdcard1/dedanadan03(www.songs.pk).mp3");
        mp.prepare();
        mp.start();
    }


}
