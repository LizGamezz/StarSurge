package com.zybooks.starsurge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.zybooks.starsurge.EnemySpawner;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        playerView = new PlayerView(this);
        setupEnemySpawner();
    }

    public class PlayerView extends View {
        private float playerX;
        private float playerY;
        private float speed = 5f;
        private int health = 100;
        private Paint paint;
        private List<Bullet> bullets = new ArrayList<>();
        private Bitmap playerBitmap; // New field for the player image

        public PlayerView(Context context) {
            super(context);
            init();
        }

        public PlayerView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);

            // Load the player image
            playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_image);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            // Set the initial player position to the center bottom of the screen
            playerX = w / 2f;
            playerY = h - playerBitmap.getHeight() / 2f;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.BLACK);

            // Draw the player
            canvas.drawBitmap(playerBitmap, playerX - playerBitmap.getWidth() / 2, playerY - playerBitmap.getHeight() / 2, null);

            // Draw the bullets
            for (Bullet bullet : bullets) {
                bullet.move();
                canvas.drawCircle(bullet.getX(), bullet.getY(), 10, paint);
            }

            // Draw the health
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            canvas.drawText("Health: " + health, 50, 50, paint);
            paint.setColor(Color.RED);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    playerX = event.getX();
                    shootBullet();
                    invalidate();
                    return true;
            }
            return super.onTouchEvent(event);
        }

        private void shootBullet() {
            Bullet bullet = new Bullet(playerX, playerY, 10f);
            bullets.add(bullet);
        }

        public void takeDamage(int damage) {
            health -= damage;
            if (health <= 0) {
                // Implement game over logic here (e.g., restart level, show game over screen, etc.)
                health = 0;
                invalidate();
            }
        }

        public void movePlayer(float dx) {
            playerX += dx;
            invalidate();
        }
    }

    public class Bullet {
        private float x;
        private float y;
        private float speed;

        public Bullet(float x, float y, float speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        public void move() {
            y -= speed;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
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
        // Scale down the bitmaps
        for (int i = 0; i < enemyBitmaps.length; i++) {
            int width = enemyBitmaps[i].getWidth() / 10; // Change these values as needed
            int height = enemyBitmaps[i].getHeight() / 10; // Change these values as needed
            enemyBitmaps[i] = Bitmap.createScaledBitmap(enemyBitmaps[i], width, height, true);
        }

        enemySpawner.setEnemyBitmaps(enemyBitmaps);
    }
}