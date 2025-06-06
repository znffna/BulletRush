package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.RectF;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class Bullet extends MapObject implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer> {
    private static final float SPEED = 400f;
    private static final float BULLET_WIDTH = 30f;
    private static final float BULLET_HEIGHT = BULLET_WIDTH;
    private int power;
    private float maxRange = 3.0f;
    private MainScene.Layer targetLayer;

    public static Bullet get(float x, float y, float angle, int power, MainScene.Layer target) {
        return Scene.top().getRecyclable(Bullet.class).init(x, y, angle, power, target);
    }

    private Bullet init(float x, float y, float angle, int power, MainScene.Layer target) {
        setPosition(x, y, BULLET_WIDTH, BULLET_HEIGHT);
        this.dx = (float) (SPEED * Math.cos(angle));
        this.dy = (float) (SPEED * Math.sin(angle));
        this.power = power;
        this.maxRange = 1000.0f;
        this.targetLayer = target;
        return this;
    }

    @Override
    public void update() {
        super.update();

        maxRange -= SPEED * GameView.frameTime;
        if (maxRange < 0.0f){
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

    public MainScene.Layer getTargetLayer() {
        return targetLayer;
    }
}
