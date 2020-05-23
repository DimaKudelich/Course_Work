package com.example.try_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.try_game.objects.MusicService;
import com.example.try_game.surface.GameSurface;

public class MainActivity extends AppCompatActivity {
    private GameSurface surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        startService(new Intent(this, MusicService.class));

        surface = new GameSurface(this);

        setContentView(surface);
    }

    @Override
    public void onBackPressed(){
        stopService(new Intent(this,MusicService.class));

        Intent intent = new Intent(MainActivity.this,Menu.class);
        startActivity(intent);

        finish();
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        stopService(new Intent(this,MusicService.class));

        super.onDestroy();
    }

    @Override
    protected void onStop(){
        stopService(new Intent(this,MusicService.class));

        super.onStop();
    }
}
