package com.example.try_game.surface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.try_game.R;
import com.example.try_game.objects.Bullet;
import com.example.try_game.objects.Character;
import com.example.try_game.objects.ControlBar;
import com.example.try_game.objects.Enemy;
import com.example.try_game.thread.GameThread;

import java.util.ArrayList;
import java.util.Random;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    //Игровой поток
    private GameThread gameThread;

    private int score = 0;

    private Bitmap fon;

    //Герой
    private Character hero;
    private Enemy enemy;

    private ArrayList<Enemy> enemies;

    //Пули
    private ArrayList<Bullet> bullets;
    //Пример пули
    private Bullet bullet;

    //Время последнего выстрела
    private long lastShot = -1;
    //Действительное время
    private long currentTime = -1;
    private long lastSpawnTime = -1;
    private long spawnPause = 5000;
    private long spawnBorder = 1000;

    //Управление
    private ControlBar controlBar;

    private SoundPool sounds;
    private int shot;
    private int kill;
    private int appear;

    public GameSurface(Context context) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    //Обновление состояния игровой поверхности
    public void update() {
        long time = System.nanoTime() / 1000000;

        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < enemies.size(); j++) {
                if (bullets.get(i).isStrikesWithEnemy(enemies.get(j))) {
                    bullets.remove(i);
                    enemies.remove(j);
                    sounds.play(kill,1.0f, 1.0f, 0, 0, 1.5f);
                    score+=1;

                    if (spawnPause > spawnBorder) {
                        spawnPause -= 100;
                    }
                }
            }
        }

        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).getIsDestroy()) {
                bullets.remove(i);
            }
        }

        for (Bullet bullet : bullets) {
            bullet.update();
        }

        if (time - lastSpawnTime > spawnPause) {
            Random rand = new Random();
            int randPlace = Math.abs(rand.nextInt()) % 3;

            int xN,yN;

            if (randPlace == 0) {
                xN = 0;
                yN=0;
            } else if (randPlace == 1) {
                xN = this.getWidth() - enemy.getWidth();
                yN = 0;
            } else {
                xN = this.getWidth() - enemy.getWidth();
                yN = this.getHeight() - enemy.getHeight();
            }

            enemies.add(new Enemy(this,this.enemy.getBitmap(),xN,yN,0,0));
            lastSpawnTime = time;
            sounds.play(appear,1.0f, 1.0f, 0, 0, 1.5f);
        }

        for (Enemy enemy : enemies) {
            if (hero.isCharacterStrikesWithObject(enemy)){
                break;
            }

            enemy.setMoveX(hero.getX() - enemy.getX());
            enemy.setMoveY(hero.getY() - enemy.getY());
            enemy.update();
        }

        this.hero.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (lastShot == -1) {
            lastShot = System.nanoTime() / 1000000;
            lastSpawnTime = System.nanoTime() / 1000000;
        }

        currentTime = System.nanoTime() / 1000000;
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            int ind = event.getActionIndex();
            if (currentTime - lastShot > 200) {
                bullets.add(new Bullet(this, bullet.getBitmap(), hero.getX()+hero.getWidth()/2, hero.getY()+hero.getHeight()/2,(int)event.getX(ind), (int)event.getY(ind)));
                sounds.play(shot,1.0f, 1.0f, 0, 0, 1.5f);
                lastShot = System.nanoTime() / 1000000;
            }
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (this.controlBar.isBarTouched(x, y)) {
                if (this.controlBar.isCenterTouched(x, y)) {
                    this.controlBar.setIsTouched(true);
                    this.controlBar.setCoordinatesOfPoint(x, y);
                    this.hero.setMove(false);
                } else {
                    int moveX = x - this.controlBar.getXC();
                    int moveY = y - this.controlBar.getYC();

                    this.hero.setMoveVector(moveX, moveY);
                    this.hero.setMove(true);

                    this.controlBar.setIsTouched(true);
                    this.controlBar.setCoordinatesOfPoint(x, y);
                }
            } else if (this.controlBar.getIsTouched() && !this.controlBar.isBarTouched(x, y)) {
                this.controlBar.setCoordinatesOfPointNotInRadius(x, y);

                int moveX = this.controlBar.getXP() - this.controlBar.getXC();
                int moveY = this.controlBar.getYP() - this.controlBar.getYC();

                this.hero.setMoveVector(moveX, moveY);
                this.hero.setMove(true);
            } else if (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN || event.getAction() != MotionEvent.ACTION_MOVE) {
                if (currentTime - lastShot > 200) {
                    bullets.add(new Bullet(this, bullet.getBitmap(), hero.getX()+hero.getWidth()/2, hero.getY()+hero.getHeight()/2, x, y));
                    sounds.play(shot,1.0f, 1.0f, 0, 0, 1.5f);
                    lastShot = System.nanoTime() / 1000000;
                }
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            this.hero.setMove(false);
            this.controlBar.setIsTouched(false);

            return true;
        }
        return false;
    }

    //Рисоввние поверхности
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setTextSize(50f);
        paint.setColor(Color.WHITE);

        canvas.drawBitmap(fon,0,0,null);

        for (Bullet bullet : bullets) {
            bullet.draw(canvas);
        }

        this.hero.draw(canvas);
        this.controlBar.draw(canvas);

        for (Enemy enemy : enemies) {
            enemy.draw(canvas);
        }

        canvas.drawText("Kills: "+ score,100,100,paint);
    }

    //Создание поверхности
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Получение картинки
        this.fon = BitmapFactory.decodeResource(this.getResources(),R.drawable.fon_igri);
        Bitmap heroBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.hero);
        Bitmap bullet = BitmapFactory.decodeResource(this.getResources(), R.drawable.bullet);
        Bitmap enemy = BitmapFactory.decodeResource(this.getResources(), R.drawable.enemy);

        //Инициализация героя
        this.hero = new Character(this, heroBitmap, getWidth()/2, getHeight()/2);
        //Инициализация панели управления
        this.controlBar = new ControlBar(this, 300, 800, 200);
        //Инициализация пули и массива пуль
        this.bullet = new Bullet(this, bullet, 0, 0, 0, 0);
        this.bullets = new ArrayList<>();

        this.enemies = new ArrayList<>();
        this.enemy = new Enemy(this, enemy, 800, 200, 300, 400);

        this.sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        this.shot = sounds.load(getContext(),R.raw.shot,1);
        this.kill = sounds.load(getContext(),R.raw.kill,1);
        this.appear = sounds.load(getContext(),R.raw.appear,1);

        //Создание потока
        this.gameThread = new GameThread(this, holder);
        //Запуск потока
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    //Закрытие поверхности
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while (retry) {
            try {
                //Попытка остановить работу поверхности
                this.gameThread.setRunning(false);

                this.gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            retry = true;
        }
    }

    public GameThread getGameThread(){
        return gameThread;
    }
}
