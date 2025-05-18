package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.RectUtil;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MapObject extends Sprite implements IBoxCollidable {
    public static void setCamera(float x, float y) {
        MapObject.camera.x = x;
        MapObject.camera.y = y;
    }

    static PointF camera = new PointF();

    public MapObject(int mipmapId) {
        super(mipmapId);
    }

    @Override
    public void update() {
        super.update();
        this.x = (x % Metrics.width + Metrics.width) % Metrics.width;
        this.y = (y % Metrics.height + Metrics.height) % Metrics.height;
        RectUtil.setRect(dstRect, x, y);
//        dstRect.offsetTo(x,y);
    }

    float px;
    float py;

    void setDstRectCameraSpace(){
        calcuateDrawPosition();
        RectUtil.setRect(dstRect, px, py);
//        dstRect.offsetTo(px,py);

//        RectUtil.setRect(dstRect, px, py, width, height);
//        RectUtil.setRect(dstRect, x + Metrics.width / 2 - Player.player.getX(), y + Metrics.height / 2 - Player.player.getY(), width, height);
    }

    @Override
    public void draw(Canvas canvas) {
        setDstRectCameraSpace();
        super.draw(canvas);
    }

    protected void calcuateDrawPosition() {
        px = x - camera.x;
        py = y - camera.y;

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
}
