package com.example.try_game;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.try_game.objects.MusicService;

import java.util.Objects;

public class Menu extends AppCompatActivity {
    SharedPreferences sp;
    final String RECORD = "record";
    TextView tv;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

        tv = findViewById(R.id.textView3);
        load();

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
    public void onBackPressed(){
        System.exit(0);
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }


    void load(){
        sp = getSharedPreferences(RECORD,MODE_PRIVATE);
        String rec =  Integer.toString(sp.getInt(RECORD,1));
        tv.setText(rec);
    }
}
