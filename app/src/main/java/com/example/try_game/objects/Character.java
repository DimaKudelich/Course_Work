package com.example.try_game.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.try_game.surface.GameSurface;

public class Character extends GameObject {
    //Скорость передвижения
    private static final float VELOCITY = 0.8f;

    //Векторы движения по оси x и y
    private int moveX = 0;
    private int moveY = 0;

    //Время в наносекундах
    private long lastDrawNanoTime = -1;
    private long lastAnimTime = -1;

    //Обработчик игровой поверхности
    private GameSurface gameSurface;

    //Движется ли герой
    private boolean isMove = false;

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

    public Character(GameSurface gameSurface, Bitmap bitmap, int x, int y) {
        super(bitmap,4,3, x, y);
        this.gameSurface = gameSurface;

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

    //Обновление состояния объекта
    public void update() {
        long now = System.nanoTime();

        if(isMove==true) {
            if ((now-lastAnimTime)>100000000){
                lastAnimTime = now;
                this.colUsing++;
            }
        }else{
            this.colUsing = 1;
        }

        if (colUsing >= this.colCount) {
            this.colUsing = 0;
        }

        //Если еще ни разу не рисовали, то придаем значение nom
        if (lastDrawNanoTime == -1) {
            lastDrawNanoTime = now;
            lastAnimTime = now;
        }

        if (isMove) {
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
        }

        this.isCharacterStrikesWithWalls();

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

    //Отрисовка изображения
    public void draw(Canvas canvas) {
        Bitmap bmp = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bmp, x, y, null);
        this.lastDrawNanoTime = System.nanoTime();
    }

    //Присваивание нового вектора движения
    public void setMoveVector(int moveX, int moveY) {
        this.moveX = moveX;
        this.moveY = moveY;
    }

    public void setMove(boolean isMove){
        this.isMove = isMove;
    }

    public void isCharacterStrikesWithWalls() {
        if (x < 0) {
            x = 0;
            moveX = 0;
        } else if (x > this.gameSurface.getWidth() - width) {
            x = this.gameSurface.getWidth() - width;
            moveX = 0;
        }
        if (y < 0) {
            y = 0;
            moveY = 0;
        } else if (y > this.gameSurface.getHeight() - height) {
            y = this.gameSurface.getHeight() - height;
            moveY = 0;
        }
    }

    public boolean isCharacterStrikesWithObject(GameObject o) {
        if (x + getWidth() > o.x+o.getWidth()/4 && x < o.x + 3*o.getWidth()/4 && y < o.y + 3*o.getHeight()/4 && y + getHeight() > o.y+o.getHeight()/4) {
            return true;
        }else {
            return false;
        }
    }

    public int getMoveX() {
        return moveX;
    }

    public int getMoveY() {
        return moveY;
    }
}
