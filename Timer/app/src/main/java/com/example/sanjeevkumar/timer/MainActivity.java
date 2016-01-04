package com.example.sanjeevkumar.timer;

import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
public class MainActivity extends AppCompatActivity {

    private EditText timerEditText;
    private TextView showTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new CustomView(this));
        setContentView(R.layout.activity_main);
        showTime = (TextView)findViewById(R.id.showTime);
        //ImageView circleTextView = (ImageView)findViewById(R.id.circleImageView);
       // circleTextView.draw(new CustomView(this));
    }

    public void startTimer(View view){
        timerEditText = (EditText) findViewById(R.id.timerEditText);
        String value = timerEditText.getText().toString();
        Integer t = Integer.parseInt(value);
        CountDownTimerClass timer = new CountDownTimerClass(t*1000, 1000);
        timer.start();
    }

    public void playSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.pearl);
        mediaPlayer.start();
    }
    public class CountDownTimerClass extends CountDownTimer {

        public CountDownTimerClass(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            showTime.setText("Done");
            playSound();
        }
        public void onTick(long millisUntilFinished) {
            showTime.setText(String.valueOf(millisUntilFinished / 1000));
        }
    }
}