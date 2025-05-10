package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class ScrollBackground extends Sprite {
    private Player player;

    private float curr_x;
    private float curr_y;

    public ScrollBackground(int bitmapResId, Player player) {
        super(bitmapResId);
        this.width = bitmap.getWidth() * Metrics.height / bitmap.getHeight();
        this.height = bitmap.getHeight() * Metrics.width / bitmap.getWidth();
        setPosition(Metrics.width / 2, Metrics.height / 2, width, height);
        this.player = player;
    }

    @Override
    public void update() {
        if(player == null) return;
        this.y = player.getY();
        this.x = player.getX();
//        this.y += speed * GameView.frameTime; // y 값을 스크롤된 양으로 사용한다
    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw(canvas);
        curr_x = x % width;
        if(curr_x > 0) curr_x -= width;
        while (curr_x < Metrics.width) {
            curr_y = y % height;
            if (curr_y > 0) curr_y -= height;
            while (curr_y < Metrics.height) {
                dstRect.set(curr_x, curr_y, curr_x + width, curr_y + height);
                canvas.drawBitmap(bitmap, null, dstRect, null);
                curr_y += height;
            }
            curr_x += width;
        }
    }
}
