package kr.ac.tukorea.ge.and.znffna.bulletrush.util;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class WarpUtil {
    public static PointF getWrappedDelta(@NonNull PointF from, @NonNull PointF to){
        return getWrappedDelta(from.x, from.y, to.x, to.y);
    }

    public static PointF getWrappedDelta(float from_x, float from_y, @NonNull PointF to){
        return getWrappedDelta(from_x, from_y, to.x, to.y);
    }

    public static PointF getWrappedDelta(@NonNull PointF from, float to_x, float to_y){
        return getWrappedDelta(from.x, from.y, to_x, to_y);
    }


    public static PointF getWrappedDelta(float from_x, float from_y, float to_x, float to_y) {
        float deltaX = to_x - from_x;
        float deltaY = to_y - from_y;

        if (deltaX >  Metrics.worldWidth / 2) deltaX -= Metrics.worldWidth;
        if (deltaX < -Metrics.worldWidth / 2) deltaX += Metrics.worldWidth;
        if (deltaY >  Metrics.worldHeight / 2) deltaY -= Metrics.worldHeight;
        if (deltaY < -Metrics.worldHeight / 2) deltaY += Metrics.worldHeight;

        return new PointF(deltaX, deltaY);
    }
}
