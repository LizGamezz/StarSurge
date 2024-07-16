package com.zybooks.starsurge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupEnemySpawner();
    }
        private EnemySpawner enemySpawner;

        private void setupEnemySpawner() {
            enemySpawner = new EnemySpawner(this, null);
            setContentView(enemySpawner);

            Bitmap[] enemyBitmaps = {
                    BitmapFactory.decodeResource(getResources(), R.drawable.enemy1),
                    BitmapFactory.decodeResource(getResources(), R.drawable.enemy2),
                    BitmapFactory.decodeResource(getResources(), R.drawable.enemy3)
            };

            enemySpawner.setEnemyBitmaps(enemyBitmaps);
        }
    }