package com.example.try_game.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameObject {
    //Изображение объекта
    protected Bitmap bitmap;
    //Координаты объекта
    protected int x;
    protected int y;

    //Номер стркои и столбца спрайти
    protected final int rowCount;
    protected final int colCount;

    //Высота и ширина набора спрайтов
    protected final int WIDTH;
    protected final int HEIGHT;

    //Высота и ширина каждого отдельного спрайта
    protected final int width;
    protected final int height;

    public GameObject(Bitmap bitmap, int rowCount, int colCount, int x, int y) {
        this.rowCount = rowCount;
        this.colCount = colCount;

        this.bitmap = bitmap;
        this.x = x;
        this.y = y;

        this.WIDTH = bitmap.getWidth();
        this.HEIGHT = bitmap.getHeight();

        this.width = this.WIDTH / colCount;
        this.height = this.HEIGHT / rowCount;
    }

    public Bitmap createSubImage(int row, int col) {
        Bitmap subImage = Bitmap.createBitmap(bitmap, col * width, row * height, width, height);
        return subImage;
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

    public Bitmap getBitmap() {

        return bitmap;
    }

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }
}
