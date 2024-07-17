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
import androidx.appcompat.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout playerContainer = findViewById(R.id.player_view_container);
        playerView = new PlayerView(this);
        playerContainer.addView(playerView);

        setupEnemySpawner();
    }

    private void setupEnemySpawner() {
        EnemySpawner enemySpawner = new EnemySpawner(this);
        FrameLayout enemyContainer = findViewById(R.id.enemy_view_container);
        enemyContainer.addView(enemySpawner);

        Bitmap[] enemyBitmaps = {
                BitmapFactory.decodeResource(getResources(), R.drawable.enemy1),
                BitmapFactory.decodeResource(getResources(), R.drawable.enemy2),
                BitmapFactory.decodeResource(getResources(), R.drawable.enemy3)
        };

        for (int i = 0; i < enemyBitmaps.length; i++) {
            int width = enemyBitmaps[i].getWidth() / 10;
            int height = enemyBitmaps[i].getHeight() / 10;
            enemyBitmaps[i] = Bitmap.createScaledBitmap(enemyBitmaps[i], width, height, true);
        }

        enemySpawner.setEnemyBitmaps(enemyBitmaps);
    }

    public class PlayerView extends View {
        private float playerX;
        private float playerY;
        private float speed = 5f;
        private int health = 100;
        private Paint paint;
        private List<Bullet> bullets = new ArrayList<>();
        private Bitmap playerBitmap;

        public PlayerView(Context context) {
            super(context);
            init();
        }

        public PlayerView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            setBackgroundColor(Color.TRANSPARENT);
            playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_image);
            paint = new Paint();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            playerX = w / 2f;
            playerY = h - playerBitmap.getHeight() / 2f;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(playerBitmap, playerX - playerBitmap.getWidth() / 2, playerY - playerBitmap.getHeight() / 2, null);

            for (Bullet bullet : bullets) {
                bullet.move();
                canvas.drawCircle(bullet.getX(), bullet.getY(), 10, paint);
            }

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
}
