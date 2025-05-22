package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.PointF;
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
        this.x = (x % Metrics.worldWidth + Metrics.worldWidth) % Metrics.worldWidth;
        this.y = (y % Metrics.worldHeight + Metrics.worldHeight) % Metrics.worldHeight;
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
        // camera 좌표계로 이동
        px = x - camera.x;
        py = y - camera.y;

        // wrap-round
        if (px >  Metrics.worldWidth / 2) px -= Metrics.worldWidth;
        if (px < -Metrics.worldWidth / 2) px += Metrics.worldWidth;
        if (py >  Metrics.worldHeight / 2) py -= Metrics.worldHeight;
        if (py < -Metrics.worldHeight / 2) py += Metrics.worldHeight;

        // 실제 화면공간으로 이동시킨다.
        px = px + Metrics.width / 2;
        py = py + Metrics.height / 2;
    }

    public RectF getCollisionRect() {
        return dstRect;
    }
}
