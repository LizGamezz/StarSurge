package com.zybooks.starsurge;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PlayerView playerView;
    private EnemySpawner enemySpawner;

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
        FrameLayout enemyContainer = findViewById(R.id.enemy_view_container);
        enemySpawner = new EnemySpawner(this);
        enemyContainer.addView(enemySpawner);
    }

    public class PlayerView extends View {
        private float playerX;
        private float playerY;
        private int health = 100;
        private Paint paint;
        private List<Bullet> bullets = new ArrayList<>();
        private Bitmap playerBitmap;
        private Bitmap bulletBitmap;
        private Handler bulletHandler;
        private Runnable bulletRunnable;

        public PlayerView(Context context) {
            super(context);
            init(context);
        }

        public PlayerView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            setBackgroundColor(Color.TRANSPARENT);

            // Load and scale the player bitmap
            Bitmap originalPlayerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership);
            playerBitmap = Bitmap.createScaledBitmap(originalPlayerBitmap, originalPlayerBitmap.getWidth() / 4, originalPlayerBitmap.getHeight() / 4, true);

            // Load and scale the bullet bitmap
            Bitmap originalBulletBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bulletimage);
            bulletBitmap = Bitmap.createScaledBitmap(originalBulletBitmap, originalBulletBitmap.getWidth() / 80, originalBulletBitmap.getHeight() / 80, true);

            paint = new Paint();

            bulletHandler = new Handler();
            bulletRunnable = new Runnable() {
                @Override
                public void run() {
                    moveBullets();
                    bulletHandler.postDelayed(this, 30); // Move bullets every 30 ms
                }
            };
            bulletHandler.post(bulletRunnable); // Start the bullet movement
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            playerX = w / 2f;
            playerY = h - playerBitmap.getHeight();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(playerBitmap, playerX - playerBitmap.getWidth() / 2, playerY - playerBitmap.getHeight() / 2, null);

            for (Bullet bullet : bullets) {
                canvas.drawBitmap(bulletBitmap, bullet.getX() - bulletBitmap.getWidth() / 2, bullet.getY() - bulletBitmap.getHeight() / 2, null);
            }

            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            canvas.drawText("Health: " + health, 50, 50, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    playerX = event.getX();
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    shootBullet();
                    invalidate();
                    break;
            }
            return true;
        }

        private void shootBullet() {
            Bullet bullet = new Bullet(playerX, playerY, 10f);
            bullets.add(bullet);
        }

        private void moveBullets() {
            Iterator<Bullet> iterator = bullets.iterator();
            while (iterator.hasNext()) {
                Bullet bullet = iterator.next();
                bullet.move();
                if (bullet.getY() < 0) { // Remove bullet if it goes off the screen
                    iterator.remove();
                }
            }
            invalidate(); // Redraw the view to show updated bullet positions
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

        public List<Bullet> getBullets() {
            return bullets;
        }

        public int getBulletWidth() {
            return bulletBitmap.getWidth();
        }

        public int getBulletHeight() {
            return bulletBitmap.getHeight();
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

    public class Enemy {
        private Bitmap image;
        private float x, y;
        private float speed;

        public Enemy(Bitmap bmp, float x, float y, float speed) {
            this.image = bmp;
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        public void move() {
            y += speed;
        }

        public void draw(Canvas canvas) {
            canvas.drawBitmap(image, x - image.getWidth() / 2, y - image.getHeight() / 2, null);
        }

        public boolean checkCollision(float bulletX, float bulletY, int bulletWidth, int bulletHeight) {
            float bulletLeft = bulletX - bulletWidth / 2;
            float bulletRight = bulletX + bulletWidth / 2;
            float bulletTop = bulletY - bulletHeight / 2;
            float bulletBottom = bulletY + bulletHeight / 2;

            float enemyLeft = x - image.getWidth() / 2;
            float enemyRight = x + image.getWidth() / 2;
            float enemyTop = y - image.getHeight() / 2;
            float enemyBottom = y + image.getHeight() / 2;

            return bulletRight > enemyLeft && bulletLeft < enemyRight &&
                    bulletBottom > enemyTop && bulletTop < enemyBottom;
        }
    }

    public class EnemySpawner extends View {
        private List<Enemy> enemies = new ArrayList<>();
        private Bitmap[] enemyBitmaps;
        private int screenWidth;
        private int screenHeight;
        private Handler handler;
        private Runnable enemySpawnerRunnable;

        public EnemySpawner(Context context) {
            super(context);
            init(context);
        }

        public EnemySpawner(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        private void init(Context context) {
            setBackgroundColor(Color.TRANSPARENT);

            Bitmap originalEnemy1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1);
            Bitmap originalEnemy2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
            Bitmap originalEnemy3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy3);

            enemyBitmaps = new Bitmap[] {
                    Bitmap.createScaledBitmap(originalEnemy1, originalEnemy1.getWidth() / 10, originalEnemy1.getHeight() / 10, true),
                    Bitmap.createScaledBitmap(originalEnemy2, originalEnemy2.getWidth() / 10, originalEnemy2.getHeight() / 10, true),
                    Bitmap.createScaledBitmap(originalEnemy3, originalEnemy3.getWidth() / 10, originalEnemy3.getHeight() / 10, true)
            };

            handler = new Handler();
            enemySpawnerRunnable = new Runnable() {
                @Override
                public void run() {
                    spawnEnemy();
                    handler.postDelayed(this, 2000); // Spawn a new enemy every 2 seconds
                }
            };
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            screenWidth = w;
            screenHeight = h;
            handler.post(enemySpawnerRunnable); // Start the enemy spawning
        }

        private void spawnEnemy() {
            float x = (float) (Math.random() * screenWidth);
            float y = 0; // Spawn at the top of the screen
            float speed = 5 + (float) (Math.random() * 5);
            Bitmap enemyBitmap = enemyBitmaps[(int) (Math.random() * enemyBitmaps.length)];
            Enemy enemy = new Enemy(enemyBitmap, x, y, speed);
            enemies.add(enemy);
            invalidate(); // Trigger a redraw to show the new enemy
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Iterator<Enemy> enemyIterator = enemies.iterator();
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                enemy.move();
                enemy.draw(canvas);

                Iterator<Bullet> bulletIterator = playerView.getBullets().iterator();
                while (bulletIterator.hasNext()) {
                    Bullet bullet = bulletIterator.next();
                    if (enemy.checkCollision(bullet.getX(), bullet.getY(), playerView.getBulletWidth(), playerView.getBulletHeight())) {
                        bulletIterator.remove();
                        enemyIterator.remove();
                        break;
                    }
                }
            }
            invalidate();  // Continuously redraw to move enemies and check for collisions
        }
    }
}
