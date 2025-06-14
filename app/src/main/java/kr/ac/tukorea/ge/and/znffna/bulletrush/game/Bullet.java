package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

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
    private static final String TAG = CollisionChecker.class.getSimpleName();
    private static final float DEFAULT_POWER_OUTPUT = 0.5f;
    private float power;
    private float maxRange = 3.0f;
    private MainScene.Layer targetLayer;
    private boolean isHit = false;
    private float outputPowerTime = DEFAULT_POWER_OUTPUT;

    public static Bullet get(float x, float y, float angle, float power, MainScene.Layer target) {
        return Scene.top().getRecyclable(Bullet.class).init(x, y, angle, power, target);
    }

    private Bullet init(float x, float y, float angle, float power, MainScene.Layer target) {
        setPosition(x, y, BULLET_WIDTH, BULLET_HEIGHT);
        this.dx = (float) (SPEED * Math.cos(angle));
        this.dy = (float) (SPEED * Math.sin(angle));
        this.power = power;
        this.maxRange = 1000.0f;
        this.targetLayer = target;
        this.isHit = false;
        this.outputPowerTime = DEFAULT_POWER_OUTPUT;
        return this;
    }

    @Override
    public void update() {
        super.update();
        if(isHit) {
            outputPowerTime -= GameView.frameTime;
            if(outputPowerTime < 0.0f){
                Scene.top().remove(this);
                return;
            }
        };

        maxRange -= SPEED * GameView.frameTime;
        if (maxRange < 0.0f){
            Scene.top().remove(this);
        }
    }

    public Bullet() {
        super(R.mipmap.bullet);
    }


    public float getPower() {
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

    @Override
    public void draw(Canvas canvas) {
        if(isHit){
            setDstRectCameraSpace();
            TextHelper.drawFontString(canvas, "" + (int)this.power, (int)px, (int)py, 50);
        }
        else super.draw(canvas);
    }

    public void onHitted() {
        Log.v(TAG,"onHitted Bullet");
        isHit = true;

        // 출력될 Text를 위로 이동시킨다.
        dx = 0f;
        dy = -50f;
    }
}
