package com.example.try_game.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.try_game.surface.GameSurface;

public class ControlBar {
    //Координаты центра окружности и её радиус
    private int xC;
    private int yC;
    private int radiusC;

    //Нажат ли Control Bar
    private boolean isTouched = false;

    //Если нажат, то координаты точки нажатия
    private int xP;
    private int yP;
    private int radiusP = 60;

    //Обработчик игровой поверхности
    private GameSurface gameSurface;

    public ControlBar(GameSurface surface, int xC, int yC, int radiusC) {
        this.gameSurface = surface;
        this.xC = xC;
        this.yC = yC;
        this.radiusC = radiusC;
    }

    public void draw(Canvas canvas) {
        //Объект для рисования
        Paint p = new Paint();

        //Настройка объекта для рисования
        p.setColor(Color.GRAY);
        p.setStrokeWidth(5);

        //Если поле активно, то отрисовывать точку нажатия
        if (isTouched) {
            p.setAlpha(100);
            p.setStyle(Paint.Style.STROKE);

            canvas.drawCircle(xC, yC, radiusC, p);//300 800 200
            canvas.drawCircle(xC,yC,radiusP-20,p);

            //Заполнять окружность цветом
            p.setStyle(Paint.Style.FILL);
            p.setAlpha(200);

            //Отрисовка точки нажатия
            canvas.drawCircle(xP, yP, radiusP, p);
        }else{
            p.setAlpha(40);
            p.setStyle(Paint.Style.STROKE);

            canvas.drawCircle(xC, yC, radiusC, p);//300 800 200
            canvas.drawCircle(xC,yC,radiusP-20,p);
        }
    }

    public boolean isBarTouched(int x, int y) {
        //Если точка нажатия лежит в окружности радиуса radius + несколько пикселей
        if ((y - yC) * (y - yC) + (x - xC) * (x - xC) < radiusC * radiusC + 50) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCenterTouched(int x, int y){
        if ((y - yC) * (y - yC) + (x - xC) * (x - xC) < (radiusP-20)*(radiusP-20)) {
            return true;
        } else {
            return false;
        }
    }

    public int getXC() {
        return xC;
    }

    public int getYC() {
        return yC;
    }

    public int getXP(){
        return xP;
    }

    public int getYP(){
        return yP;
    }

    public boolean getIsTouched(){
        return isTouched;
    }

    public void setIsTouched(boolean isTouched) {
        this.isTouched = isTouched;
    }

    public void setCoordinatesOfPoint(int xP, int yP) {
        this.xP = xP;
        this.yP = yP;
    }

    public void setCoordinatesOfPointNotInRadius(int x,int y) {
        int dx = x - this.xC;
        int dy = y - this.yC;

        int kx = dx > 0 ? 1 : -1;
        int ky = dy > 0 ? 1 : -1;

        double dxy = Math.sqrt(dx * dx + dy * dy);
        double angle = Math.asin(Math.abs(dy)/dxy);

        int dxP = (int)(kx*Math.cos(angle)* radiusC);
        int dyP = (int)(ky*Math.sin(angle)* radiusC);

        this.setCoordinatesOfPoint(xC+dxP,yC+dyP);
    }
}
