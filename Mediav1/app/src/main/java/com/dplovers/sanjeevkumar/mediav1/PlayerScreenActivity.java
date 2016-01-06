package com.dplovers.sanjeevkumar.mediav1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class PlayerScreenActivity extends AppCompatActivity {

    public static Context context;
    public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set up back button navigation in toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context = this;
        activity = this;

        Intent intent = getIntent();
        int position = intent.getIntExtra(MainActivity.EXTRA_MESSAGE_POS, 0);
        boolean fromBottomStrip = intent.getBooleanExtra(MainActivity.EXTRA_MESSAGE_SRC, false);
        GoToPlayerScreenActivityHelper.setUpPlayer(position, fromBottomStrip, this);
    }

    public static Context getContext() {
        return context;
    }
}
