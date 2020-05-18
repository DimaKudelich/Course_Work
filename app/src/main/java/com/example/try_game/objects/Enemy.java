package com.example.try_game.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.try_game.surface.GameSurface;

public class Enemy extends GameObject {
    private static final float VELOCITY = 0.5f;

    private int moveX = 0;
    private int moveY = 0;

    //Время в наносекундах
    private long lastDrawNanoTime = -1;
    private long lastAnimTime = -1;

    //Обработчик игровой поверхности
    private GameSurface gameSurface;

    private static final int ROW_TOP_TO_BOTTOM = 0;
    private static final int ROW_RIGHT_TO_LEFT = 1;
    private static final int ROW_LEFT_TO_RIGHT = 2;
    private static final int ROW_BOTTOM_TO_TOP = 3;

    private int rowUsing = ROW_LEFT_TO_RIGHT;
    private int colUsing;

    private Bitmap[] leftToRights;
    private Bitmap[] rightToLefts;
    private Bitmap[] topToBottoms;
    private Bitmap[] bottomToTops;

    public Enemy(GameSurface gameSurface,Bitmap bitmap,int x, int y,int moveX,int moveY) {
        super(bitmap,4,3, x, y);
        this.gameSurface =  gameSurface;

        this.topToBottoms = new Bitmap[colCount]; // 3
        this.rightToLefts = new Bitmap[colCount]; // 3
        this.leftToRights = new Bitmap[colCount]; // 3
        this.bottomToTops = new Bitmap[colCount]; // 3

        for(int col = 0; col< this.colCount; col++ ) {
            this.topToBottoms[col] = this.createSubImage(ROW_TOP_TO_BOTTOM, col);
            this.rightToLefts[col]  = this.createSubImage(ROW_RIGHT_TO_LEFT, col);
            this.leftToRights[col] = this.createSubImage(ROW_LEFT_TO_RIGHT, col);
            this.bottomToTops[col]  = this.createSubImage(ROW_BOTTOM_TO_TOP, col);
        }

        this.moveX = moveX - x;
        this.moveY = moveY - y;
    }

    public Bitmap[] getMoveBitmaps()  {
        switch (rowUsing)  {
            case ROW_BOTTOM_TO_TOP:
                return  this.bottomToTops;
            case ROW_LEFT_TO_RIGHT:
                return this.leftToRights;
            case ROW_RIGHT_TO_LEFT:
                return this.rightToLefts;
            case ROW_TOP_TO_BOTTOM:
                return this.topToBottoms;
            default:
                return null;
        }
    }

    public Bitmap getCurrentMoveBitmap()  {
        Bitmap[] bitmaps = this.getMoveBitmaps();
        return bitmaps[this.colUsing];
    }

    public void draw(Canvas canvas) {
        Bitmap bmp = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bmp, x, y, null);
        this.lastDrawNanoTime = System.nanoTime();
    }

    public void isEnemyStrikesWithWalls() {
        if (x < 0) {
            moveX *=-1;
        } else if (x > this.gameSurface.getWidth() - width) {
            moveX *=-1;
        }
        if (y < 0) {
            moveY *=-1;
        } else if (y > this.gameSurface.getHeight() - height) {
            moveY *=-1;
        }
    }

    public void update(){
        long now = System.nanoTime();

        if ((now-lastAnimTime)>100000000){
            lastAnimTime = now;
            this.colUsing++;
        }

        if (colUsing >= this.colCount) {
            this.colUsing = 0;
        }

        //Действительное время в наносекундах

        //Если еще ни разу не рисовали, то придаем значение nom
        if (lastDrawNanoTime == -1) {
            lastDrawNanoTime = now;
        }

        //Перевод из наносекунд в милисекунды
        int deltaTime = (int) ((now - lastDrawNanoTime) / 1000000);

        //Пройденное расстояние
        float distance = VELOCITY * deltaTime;

        int kx = moveX > 0 ? 1 : -1;
        int ky = moveY > 0 ? 1 : -1;

        //Длина вектора направления движения
        double moveVectorLength = Math.sqrt(moveX * moveX + moveY * moveY);

        //Угол между вектором направления движения и нижней полосой экрана
        double angle = Math.asin(Math.abs(moveY) / moveVectorLength);

        //Вычисление новых координат объекта
        this.x = x + (int) (kx * distance * Math.cos(angle));
        this.y = y + (int) (ky * distance * Math.sin(angle));

        this.isEnemyStrikesWithWalls();

        if (moveX > 0) {
            if (moveY > 0 && Math.abs(moveX) < Math.abs(moveY)) {
                this.rowUsing = ROW_TOP_TO_BOTTOM;
            } else if (moveY < 0 && Math.abs(moveX) < Math.abs(moveY)) {
                this.rowUsing = ROW_BOTTOM_TO_TOP;
            } else {
                this.rowUsing = ROW_LEFT_TO_RIGHT;
            }
        } else {
            if (moveY > 0 && Math.abs(moveX) < Math.abs(moveY)) {
                this.rowUsing = ROW_TOP_TO_BOTTOM;
            } else if (moveY < 0 && Math.abs(moveX) < Math.abs(moveY)) {
                this.rowUsing = ROW_BOTTOM_TO_TOP;
            } else {
                this.rowUsing = ROW_RIGHT_TO_LEFT;
            }
        }
    }

    public void setCoords(int x,int y){
        this.x = x;
        this.y = y;
    }

    public void setMoveX(int moveX) {
        this.moveX = moveX;
    }

    public void setMoveY(int moveY) {
        this.moveY = moveY;
    }
}
