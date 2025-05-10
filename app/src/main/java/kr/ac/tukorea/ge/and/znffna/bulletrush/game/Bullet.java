package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.RectF;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Bullet extends Sprite implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer> {
    private static final float SPEED = 1000f;
    private static final float BULLET_WIDTH = 30f;
    private static final float BULLET_HEIGHT = BULLET_WIDTH;
    private int power;
    private float lifeTime = 3.0f;

    public static Bullet get(float x, float y, float angle, int power) {
        return Scene.top().getRecyclable(Bullet.class).init(x, y, angle, power);
    }

    private Bullet init(float x, float y, float angle, int power) {
        setPosition(x, y, BULLET_WIDTH, BULLET_HEIGHT);
        this.dx = (float) (SPEED * Math.cos(angle));
        this.dy = (float) (SPEED * Math.sin(angle));
        this.power = power;
        this.lifeTime = 3.0f;
        return this;
    }

    @Override
    public void update() {
        super.update();

        lifeTime -= GameView.frameTime;
        if (lifeTime < 0.0f){
            Scene.top().remove(this);
        }
    }

    public Bullet() {
        super(R.mipmap.bullet);
    }

    public int getPower() {
        return power;
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect;
    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.bullet;
    }

    @Override
    public void onRecycle() {

    }
}
