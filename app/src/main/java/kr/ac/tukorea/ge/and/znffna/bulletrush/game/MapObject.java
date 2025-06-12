package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

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
        warpedPosition();
    }

    protected void warpedPosition() {
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

    protected PointF getWrappedDelta(@NonNull PointF from, @NonNull PointF to){
        return getWrappedDelta(from.x, from.y, to.x, to.y);
    }

    protected PointF getWrappedDelta(float from_x, float from_y, @NonNull PointF to){
        return getWrappedDelta(from_x, from_y, to.x, to.y);
    }

    protected PointF getWrappedDelta(@NonNull PointF from, float to_x, float to_y){
        return getWrappedDelta(from.x, from.y, to_x, to_y);
    }


    protected PointF getWrappedDelta(float from_x, float from_y, float to_x, float to_y) {
        float deltaX = (float) (to_x - from_x);
        float deltaY = (float) (to_y - from_y);
        
        if (deltaX >  Metrics.worldWidth / 2) deltaX -= Metrics.worldWidth;
        if (deltaX < -Metrics.worldWidth / 2) deltaX += Metrics.worldWidth;
        if (deltaY >  Metrics.worldHeight / 2) deltaY -= Metrics.worldHeight;
        if (deltaY < -Metrics.worldHeight / 2) deltaY += Metrics.worldHeight;
        
        return new PointF(deltaX, deltaY);
    }

    protected void calcuateDrawPosition() {
        // camera 좌표계로 이동
        PointF deltaPosition = getWrappedDelta(camera, x, y);

        // 실제 화면공간으로 이동시킨다.
        px = deltaPosition.x + Metrics.width / 2;
        py = deltaPosition.y + Metrics.height / 2;
    }

    public RectF getCollisionRect() {
        return dstRect;
    }

    public MainScene.Layer getLayer() {
        return MainScene.Layer.none;
    }
}
