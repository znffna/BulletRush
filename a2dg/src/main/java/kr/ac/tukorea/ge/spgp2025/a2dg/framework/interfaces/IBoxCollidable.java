package kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces;

import android.graphics.RectF;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;

public interface IBoxCollidable {
    public RectF getCollisionRect();
    public RectF getCollisionRect(Sprite other);
}
