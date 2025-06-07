package kr.ac.tukorea.ge.spgp2025.a2dg.framework.util;

import android.graphics.RectF;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class CollisionHelper {
    public static boolean collides(IBoxCollidable obj1, IBoxCollidable obj2) {
        RectF r1 = new RectF(obj1.getCollisionRect());
        RectF r2 = new RectF(obj2.getCollisionRect());

        wrapRect(r1, r2);
        return collides(r1, r2);
    }

    private static void wrapRect(RectF r1, RectF r2) {
        float centerX1 = r1.centerX();
        float centerY1 = r1.centerY();
        float centerX2 = r2.centerX();
        float centerY2 = r2.centerY();

        if(Math.abs(centerX1 - centerX2) > Metrics.worldWidth / 2){
            if(centerX1 < centerX2) r1.offset(Metrics.worldWidth, 0);
            else r2.offset(Metrics.worldWidth, 0);
        }
        if(Math.abs(centerY1 - centerY2) > Metrics.worldHeight / 2){
            if(centerY1 < centerY2) r1.offset(0, Metrics.worldHeight);
            else r2.offset(0, Metrics.worldHeight);
        }
    }

    public static boolean collides(RectF r1, RectF r2) {
        if (r1.left > r2.right) return false;
        if (r1.top > r2.bottom) return false;
        if (r1.right < r2.left) return false;
        if (r1.bottom < r2.top) return false;
        return true;
    }

    public static boolean collidesRadius(Sprite s1, Sprite s2) {
        double dx = s1.getX() - s2.getX();
        double dy = s1.getY() - s2.getY();
        double dist_sq = dx * dx + dy * dy;
        double sum_of_two_radii = s1.getRadius() + s2.getRadius();
        double sum_sq = sum_of_two_radii * sum_of_two_radii;
        return dist_sq < sum_sq;
    }
}
