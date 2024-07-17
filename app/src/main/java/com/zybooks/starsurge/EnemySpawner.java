package com.zybooks.starsurge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class EnemySpawner extends View {
    private List<Enemy> enemies;
    private Bitmap[] enemyBitmaps; // Array of enemy bitmaps
    private float minSpawnInterval = 3000; // Minimum time interval between spawns (ms)
    private float maxSpawnInterval = 6000; // Maximum time interval between spawns (ms)
    private float spawnAreaMinX = 0; // Minimum x-coordinate for spawn area
    private float spawnAreaMaxX; // Maximum x-coordinate for spawn area
    private float spawnY = 0; // Fixed y-coordinate for spawning
    private float enemySpeed = 5f; // Speed at which the enemy moves down
    private float despawnY; // Y-coordinate at which enemies despawn

    private Handler handler;
    private Runnable spawnRunnable;
    private Random random;

    public EnemySpawner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        enemies = new ArrayList<>();
        random = new Random();
        handler = new Handler();

        spawnAreaMaxX = getResources().getDisplayMetrics().widthPixels;
        spawnY = 0;
        despawnY = getResources().getDisplayMetrics().heightPixels;

        spawnRunnable = new Runnable() {
            @Override
            public void run() {
                spawnEnemy();
                setRandomSpawnInterval();
                handler.postDelayed(this, (long) getRandomSpawnInterval());
            }
        };

        handler.post(spawnRunnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Enemy enemy : enemies) {
            enemy.update();
            enemy.draw(canvas);

            if (enemy.isOffScreen()) {
                enemies.remove(enemy);
            }
        }

        invalidate();
    }

    private void spawnEnemy() {
        int randomIndex = random.nextInt(enemyBitmaps.length);
        Bitmap randomEnemyBitmap = enemyBitmaps[randomIndex];

        // Get the screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        float screenWidth = displayMetrics.widthPixels;

        // Calculate a random x-coordinate for spawning
        float spawnX = random.nextFloat() * screenWidth;

        enemies.add(new Enemy(randomEnemyBitmap, spawnX, spawnY, enemySpeed, despawnY));
    }

    private void setRandomSpawnInterval() {
        handler.postDelayed(spawnRunnable, (long) getRandomSpawnInterval());
    }

    private float getRandomSpawnInterval() {
        return minSpawnInterval + random.nextFloat() * (maxSpawnInterval - minSpawnInterval);
    }

    public void setEnemyBitmaps(Bitmap[] enemyBitmaps) {
        this.enemyBitmaps = enemyBitmaps;
    }
}