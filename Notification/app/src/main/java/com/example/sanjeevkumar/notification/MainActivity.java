package com.example.sanjeevkumar.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText content;
    private Button notification_button;
    private Button play_music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notification_button = (Button) findViewById(R.id.notification_button);
        play_music = (Button) findViewById(R.id.play_music);
        content = (EditText) findViewById(R.id.content);

        setClickListener();
        setClickListenerOnPlay();
        createIntent();
    }


    private void createIntent() {
        Intent intent = new Intent( getApplicationContext(), MediaPlayerService.class );
        intent.setAction( MediaPlayerService.ACTION_PLAY );
        startService( intent );
    }
    private void setClickListener() {
        notification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content_str = content.getText().toString();
                //buildNotification(content_str);
            }
        });
    }

//    private void buildNotification(String content) {
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setVisibility(Notification.VISIBILITY_PUBLIC)
//                        .addAction(android.R.drawable.ic_media_previous, "Previous", prevPendingIntent) // #0
//                        .addAction(android.R.drawable.ic_media_pause, "Pause", prevPendingIntent) // #1
//                        .addAction(android.R.drawable.ic_media_next, "Next", prevPendingIntent) // #2
//                         // Apply the media style template
//                        .setStyle(new Notification.MediaStyle()
//                        .setShowActionsInCompactView(1 /* #1: pause button */)
//                        .setMediaSession(mMediaSession.getSessionToken())
//                        .setLargeIcon(albumArtBitmap)
//                        .setContentTitle(content)
//                        .setContentText("Hello World!");
//
//        //open corresponding activity from intent
//        Intent resultIntent = new Intent(this, ResultActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        // Adds the back stack
//        stackBuilder.addParentStack(ResultActivity.class);
//        // Adds the Intent to the top of the stack
//        stackBuilder.addNextIntent(resultIntent);
//
//        PendingIntent resultPendingIntent =
//                PendingIntent.getActivity(
//                        this,
//                        0,
//                        resultIntent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        // mId allows you to update the notification later on.
//        mNotificationManager.notify(0, mBuilder.build());
//    }
//
    private void setClickListenerOnPlay() {
        play_music.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.awari);
                mediaPlayer.start();
            }
        });
    }
}
