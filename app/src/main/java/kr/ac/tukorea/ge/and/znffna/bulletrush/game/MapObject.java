package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MapObject extends AnimSprite implements IBoxCollidable, ILayerProvider<MainScene.Layer> {

    public static void setCamera(float x, float y) {
        MapObject.camera.x = x;
        MapObject.camera.y = y;
    }

    static PointF camera = new PointF();

    public MapObject(int mipmapId) {
        super(mipmapId, 1);
    }

    @Override
    public void update() {
        super.update();
        this.x = (x % Metrics.worldWidth + Metrics.worldWidth) % Metrics.worldWidth;
        this.y = (y % Metrics.worldHeight + Metrics.worldHeight) % Metrics.worldHeight;
        dstRect.offsetTo(x - width / 2, y - height / 2);
    }

    float px;
    float py;

    void setDstRectCameraSpace(){
        calcuateDrawPosition();
        dstRect.offsetTo(px - width / 2, py - height / 2);
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

    public MainScene.Layer getLayer() {
        return MainScene.Layer.none;
    }
}
