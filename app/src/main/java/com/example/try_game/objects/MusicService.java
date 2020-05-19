package com.example.try_game.objects;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.try_game.R;

public class MusicService extends Service {
    private static final String TAG = "Service";
    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        Toast.makeText(this,"Service created",Toast.LENGTH_LONG).show();

        mediaPlayer = MediaPlayer.create(this, R.raw.gameplay);
        mediaPlayer.setLooping(true);
    }

    @Override
    public void onStart(Intent intent,int startId){
        Toast.makeText(this,"Service started",Toast.LENGTH_LONG).show();
        mediaPlayer.start();
    }

    @Override
    public void onDestroy(){
        Toast.makeText(this,"Service stoped",Toast.LENGTH_LONG).show();
        mediaPlayer.stop();
    }
}
