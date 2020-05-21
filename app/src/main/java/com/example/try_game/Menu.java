package com.example.try_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.try_game.objects.MusicService;

import java.util.Objects;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Objects.requireNonNull(getSupportActionBar()).hide();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_menu);

        Button toStart = findViewById(R.id.button1);
        toStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Menu.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e){ }
            }
        });

        Button end = findViewById(R.id.button2);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.exit(0);
                    finish();
                }catch (Exception e){ }
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.button1:
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button2:
                finish();
                break;
            default:
                break;
        }
    }
}
