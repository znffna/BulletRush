package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Bullet extends Sprite {

    private static final float SPEED = 50f;

    private static final float BULLET_WIDTH = 30f;
    private static final float BULLET_HEIGHT = BULLET_WIDTH;

    public Bullet(float angle_radian) {
        super(R.mipmap.bullet);
        this.dx = (float) (SPEED * Math.cos(angle_radian));
        this.dy = (float) (SPEED * Math.sin(angle_radian));

        setPosition(100, 100, BULLET_WIDTH, BULLET_HEIGHT);
    }

}
