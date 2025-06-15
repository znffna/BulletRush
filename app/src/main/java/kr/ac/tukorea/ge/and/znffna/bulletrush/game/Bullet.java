package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.RectF;

import java.util.HashSet;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class Bullet extends MapObject implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer> {
    private int penetrableTimes; // 관통 가능 횟수
    private HashSet<MapObject> hitObject = new HashSet<>();

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public static float SPEED = 1000f;
    private float speed = SPEED;

    private static final float BULLET_WIDTH = 30f;
    private static final float BULLET_HEIGHT = BULLET_WIDTH;
    private static final String TAG = CollisionChecker.class.getSimpleName();
    private static final float DEFAULT_POWER_OUTPUT = 0.5f;
    private float power;
    private float maxRange = 3.0f;
    private MainScene.Layer targetLayer;

    public static Bullet get(float x, float y, float angle, float power, MainScene.Layer target, float speed, int penetrableTimes) {
        return Scene.top().getRecyclable(Bullet.class).init(x, y, angle, power, target, speed, penetrableTimes);
    }

    private Bullet init(float x, float y, float angle, float power, MainScene.Layer target, float speed, int penetrableTimes) {
        setPosition(x, y, BULLET_WIDTH, BULLET_HEIGHT);
        this.speed = speed;
        this.dx = (float) (this.speed * Math.cos(angle));
        this.dy = (float) (this.speed * Math.sin(angle));
        this.power = power;
        this.maxRange = 1000.0f;
        this.targetLayer = target;
        this.penetrableTimes = penetrableTimes;
        hitObject.clear();
        return this;
    }

    @Override
    public void update() {
        super.update();

        maxRange -= speed * GameView.frameTime;
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

    public void onHit(MapObject object) {
        // 이미 부딫힌 오브젝트인지 확인
        if(hitObject.contains(object)) return;
        
        // 새로 부딫힌 오브젝트에 대한 충돌로직 처리
        hitObject.add(object);
        Scene.top().add(new HitPopup("" + (int)this.power, px, py, 40, 0f, -50f, 0.3f));
        penetrableTimes -= 1;
        if(penetrableTimes < 0){
            Scene.top().remove(this);
        }
    }
}
