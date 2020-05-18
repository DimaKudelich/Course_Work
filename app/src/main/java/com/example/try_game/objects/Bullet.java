package com.example.try_game.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.try_game.surface.GameSurface;

public class Bullet {
    private Bitmap bitmap;
    private GameSurface gameSurface;

    private int x;
    private int y;

    private static final float VELOCITY = 2.5f;

    private int moveX;
    private int moveY;

    private long lastDrawNanoTime = -1;
    private boolean isDestroy = false;

    public Bullet(GameSurface gameSurface,Bitmap bitmap,int x,int y,int xA,int yA) {
        this.gameSurface = gameSurface;
        this.bitmap = bitmap;

        this.setBullet(x, y, xA, yA);
    }

    public void update(){
        long now = System.nanoTime();

        //Если еще ни разу не рисовали, то придаем значение nom
        if (lastDrawNanoTime == -1) {
            lastDrawNanoTime = now;
        }

        int deltaTime = (int) ((now - lastDrawNanoTime) / 1000000);
        float distance = VELOCITY * deltaTime;

        int kx = moveX > 0 ? 1 : -1;
        int ky = moveY > 0 ? 1 : -1;

        double moveVectorLength = Math.sqrt(moveX * moveX + moveY * moveY);

        double angle = Math.asin(Math.abs(moveY) / moveVectorLength);

        this.x = x + (int) (kx * distance * Math.cos(angle));
        this.y = y + (int) (ky * distance * Math.sin(angle));

        this.isBulletStrikesWithWalls();
    }

    public void isBulletStrikesWithWalls() {
        if (x < 0 || x > gameSurface.getWidth() - bitmap.getWidth() || y < 0 || y > gameSurface.getHeight() - bitmap.getHeight()) {
            isDestroy = true;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap,x,y,null);
        this.lastDrawNanoTime = System.nanoTime();
    }

    public void setBullet(int x,int y,int xA,int yA){
        this.x = x;
        this.y = y;

        this.moveX = xA - x;
        this.moveY = yA -y;
    }

    public boolean getIsDestroy(){
        return isDestroy;
    }

    public boolean isStrikesWithEnemy(GameObject o){
        if (x + bitmap.getWidth() > o.x &&
                x < o.x + o.width &&
                y < o.y + o.height &&
                y + bitmap.getHeight() > o.y) {
            return true;
        }else{
            return false;
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setMoveX(int moveX) {
        this.moveX = moveX;
    }

    public void setMoveY(int moveY) {
        this.moveY = moveY;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMoveX() {
        return moveX;
    }

    public int getMoveY() {
        return moveY;
    }
}
