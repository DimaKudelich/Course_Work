package com.example.try_game.objects;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.try_game.R;

public class MusicService extends Service {
    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        mediaPlayer = MediaPlayer.create(this, R.raw.gameplay);
        mediaPlayer.setLooping(true);
    }

    @Override
    public void onStart(Intent intent,int startId){
        mediaPlayer.start();
    }

    @Override
    public void onDestroy(){
        mediaPlayer.stop();
    }
}
