import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Enemy {
    private Bitmap bitmap;
    private float x;
    private float y;
    private float speed;
    private float despawnY;

    public Enemy(Bitmap bitmap, float x, float y, float speed, float despawnY) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.despawnY = despawnY;
    }

    public void update() {
        y += speed;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, new RectF(x, y, x + bitmap.getWidth(), y + bitmap.getHeight()), null);
    }

    public boolean isOffScreen() {
        return y >= despawnY;
    }
}