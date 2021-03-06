package com.example.try_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

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

        surface = new GameSurface(this,getSharedPreferences("record",MODE_PRIVATE));

        startService(new Intent(this, MusicService.class));
        setContentView(surface);
    }

    @Override
    public void onBackPressed(){
        stopService(new Intent(this,MusicService.class));
        surface.surfaceDestroyed(surface.getGameThread().getSurfaceHolder());

        Intent intent = new Intent(MainActivity.this,Menu.class);

        startActivity(intent);

        finish();
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
