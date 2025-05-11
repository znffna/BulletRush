package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.RectUtil;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class WrapSprite extends Sprite implements IBoxCollidable {
    public WrapSprite(int mipmapId) {
        super(mipmapId);
    }

    @Override
    public void update() {
        super.update();
        this.x = (x % Metrics.width + Metrics.width) % Metrics.width;
        this.y = (y % Metrics.height + Metrics.height) % Metrics.height;
        setDstRectPlayerSpace();
    }

    float px;
    float py;

    void setDstRectPlayerSpace(){
        calcuateDrawPosition();
        RectUtil.setRect(dstRect, px, py, width, height);
//        RectUtil.setRect(dstRect, x + Metrics.width / 2 - Player.player.getX(), y + Metrics.height / 2 - Player.player.getY(), width, height);
    }

    protected void calcuateDrawPosition() {
        px = x - Player.player.getX();
        py = y - Player.player.getY();

        if (px >  Metrics.width  / 2) px -= Metrics.width;
        if (px < -Metrics.width  / 2) px += Metrics.width;
        if (py >  Metrics.height / 2) py -= Metrics.height;
        if (py < -Metrics.height / 2) py += Metrics.height;

        px = px + Metrics.width / 2;
        py = py + Metrics.height / 2;
    }

    public RectF getCollisionRect() {
        return dstRect;

    }

    @Override
    public RectF getCollisionRect(Sprite other) {
        float dx = other.getX() - this.x;

        if (Math.abs(dx) > Metrics.width / 2f)
            dx -= Math.signum(dx) * Metrics.width;

        float dy = other.getY() - this.y;
        if (Math.abs(dy) > Metrics.height / 2f)
            dy -= Math.signum(dy) * Metrics.height;

        float wrappedEnemyX = x + dx;
        float wrappedEnemyY = y + dy;

        RectUtil.setRect(dstRect, wrappedEnemyX, wrappedEnemyY, width, height);
        return dstRect;
    }
}
