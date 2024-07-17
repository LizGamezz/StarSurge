// EnemySpawner.java
package com.zybooks.starsurge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySpawner extends View {
    private List<Enemy> enemies;
    private Bitmap[] enemyBitmaps;
    private Paint paint;
    private Random random;
    private Handler handler;

    public EnemySpawner(Context context) {
        super(context);
        init();
    }

    public EnemySpawner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        enemies = new ArrayList<>();
        paint = new Paint();
        random = new Random();
        handler = new Handler();
        handler.postDelayed(spawnRunnable, 1000); // Initial delay of 1 second
    }

    public void setEnemyBitmaps(Bitmap[] bitmaps) {
        enemyBitmaps = bitmaps;
    }

    private Runnable spawnRunnable = new Runnable() {
        @Override
        public void run() {
            spawnEnemy();
            handler.postDelayed(this, 1000); // Spawn enemy every second
        }
    };

    private void spawnEnemy() {
        if (enemyBitmaps != null) {
            int x = random.nextInt(getWidth() - enemyBitmaps[0].getWidth());
            Bitmap bitmap = enemyBitmaps[random.nextInt(enemyBitmaps.length)];
            enemies.add(new Enemy(x, 0, bitmap));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Enemy enemy : enemies) {
            enemy.move();
            enemy.draw(canvas, paint);
        }

        invalidate(); // Redraw the view
    }

    private class Enemy {
        private int x, y;
        private Bitmap bitmap;
        private int speed = 5;

        public Enemy(int x, int y, Bitmap bitmap) {
            this.x = x;
            this.y = y;
            this.bitmap = bitmap;
        }

        public void move() {
            y += speed;
        }

        public void draw(Canvas canvas, Paint paint) {
            canvas.drawBitmap(bitmap, x, y, paint);
        }
    }
}