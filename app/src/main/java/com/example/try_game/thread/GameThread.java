package com.example.try_game.thread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.example.try_game.surface.GameSurface;

public class GameThread extends Thread {
    //Является ли процесс запущенным
    private boolean running;

    //Обработчик игрововй поверхности
    private GameSurface gameSurface;

    //Объект для рисования
    private SurfaceHolder surfaceHolder;

    public GameThread(GameSurface gameSurface, SurfaceHolder surfaceHolder) {
        this.gameSurface = gameSurface;
        this.surfaceHolder = surfaceHolder;
    }

    //Присваивание информации о работе процесса
    public void setRunning(boolean running) {
        this.running = running;
    }

    //Работа приложения
    @Override
    public void run() {
        //Начало игрового цикла
        long startTime = System.nanoTime();

        //Пока процесс запущен
        while (running) {
            //Объект, инструмент для рисования
            Canvas canvas = null;
            try {
                //Получаем canvas из holder и блокируем его
                canvas = this.surfaceHolder.lockCanvas();

                //Если метод не используется другим потоком - он запускается
                synchronized (canvas) {
                    this.gameSurface.update();
                    this.gameSurface.draw(canvas);
                }
            } catch (Exception ex) {
                //Ничего
            } finally {
                if (canvas != null) {
                    //Разблокировка canvas
                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            //Конец игрового цикла
            long now = System.nanoTime();
            // Интервал для перерисовки игры (в миллисекундах)
            long waitTime = (now - startTime) / 1000000;

            if (waitTime < 10) {
                waitTime = 10;
            }

            try {
                // Сон
                this.sleep(waitTime);
            } catch (InterruptedException e) {
                //Ничего
            }
            //Начало игрового цикла
            startTime = System.nanoTime();
        }
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }
}
