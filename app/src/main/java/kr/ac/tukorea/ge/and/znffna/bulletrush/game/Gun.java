package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import static kr.ac.tukorea.ge.and.znffna.bulletrush.game.Gun.GunType.getType;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.and.znffna.bulletrush.util.WarpUtil;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.RectUtil;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Gun extends Sprite implements IRecyclable, ILayerProvider<MainScene.Layer> {

    private static final String TAG = Gun.class.getSimpleName();
    private final float GUN_WIDTH = 112f;
    private final float GUN_HEIGHT = 40f;
    private int penetrableTimes; // 관통 가능 횟수

    public enum GunType {
        AR, SG, SR;

        public static final int COUNT = values().length;

        public static GunType getType(int index) {
            return values()[index % COUNT];
        }
    }

    private GunType type;
    private float power = 5f;

    public static final int[] resIds = {
        R.mipmap.ar_1, R.mipmap.sg_1, R.mipmap.sr_1
    };
    private MapObject Follow;
    private float GUN_OFFSET_Y = 0f;
    private float GUN_OFFSET_X = 0f;
    private static Paint paint;
    private float speed = Bullet.SPEED;
    private float radianAngle;

    public void setRange(float range) {
        this.range = range;
    }

    private float range = 500f;

    public void setFIRE_INTERVAL(float FIRE_INTERVAL) {
        this.FIRE_INTERVAL = FIRE_INTERVAL;
    }

    public float getFIRE_INTERVAL() {
        return FIRE_INTERVAL;
    }

    private float FIRE_INTERVAL = 0.25f;
    private float fireCoolTime = FIRE_INTERVAL;


    private MainScene.Layer targetLayer;
    private MapObject nearestTarget;
    private float angle;


    public Gun(int mipmapId) {
        super(mipmapId);
    }

    public Gun(MapObject object, MainScene.Layer targetLayer, float offset_x, float offset_y, int type){
        super(resIds[type]);
        init(object, targetLayer, offset_x, offset_y, type);
    }

    public static Gun get(MapObject object, MainScene.Layer targetLayer, float x, float y) {
        return get(object, targetLayer, x, y,0);
    }

    public static Gun get(MapObject object, MainScene.Layer targetLayer, float x, float y, int type) {
        return Scene.top().getRecyclable(Gun.class).init(object, targetLayer, x, y, type);
    }

    Gun init(MapObject object, MainScene.Layer targetLayer, float x, float y, int type) {

        if(paint == null) {
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3f);
        }

        if(sparkBitmap == null) sparkBitmap = BitmapPool.get(R.mipmap.gun_spark);
        if(sparkRect == null) sparkRect = new RectF();

        this.GUN_OFFSET_X = x;
        this.GUN_OFFSET_Y = y;
        this.Follow = object;
        this.targetLayer = targetLayer;
        setType(type);
        setPosition(Follow.getX() + GUN_OFFSET_X, Follow.getY() + GUN_OFFSET_Y, GUN_WIDTH, GUN_HEIGHT);
        return this;
    }

    public Gun() {
        super(R.mipmap.bullet);
    }

    public void setPosition(float x, float y){
        setPosition(x, y, GUN_WIDTH, GUN_HEIGHT);
    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.gun;
    }

    @Override
    public void onRecycle() {}

    public void setType(int type) {
        this.type = getType(type);
        setImageResourceId(resIds[type]);

        switch (this.type) {
            case AR:
                this.FIRE_INTERVAL = 0.25f;
                this.penetrableTimes = 0;
                break;
            case SG:
                this.FIRE_INTERVAL = 1.0f;
                this.penetrableTimes = 0;
                break;
            case SR:
                this.FIRE_INTERVAL = 1.0f;
                this.penetrableTimes = 2;
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        // Player 기준 상대 위치로 이동
        setPosition(Follow.getX() + GUN_OFFSET_X, Follow.getY() + GUN_OFFSET_Y, GUN_WIDTH, GUN_HEIGHT);

        // 총 격발 인터벌 갱신
        findNearestTarget();

        fireCoolTime -= GameView.frameTime;
        fire();
    }

    public void fire() {
        if (fireCoolTime <= 0) {
            // 자신과 가장 가까운 enemy 조준
            if (nearestTarget == null) return;

            if (maxLength < MAX_RANGE) {
                fireBullet();
                fireCoolTime = FIRE_INTERVAL;
            }
        }
    }

    private float maxLength;
    private final float MAX_RANGE = Math.min(Metrics.width, Metrics.height);
    private void findNearestTarget() {
        Scene scene = Scene.top();
        if(scene == null) return;

        maxLength = range;
        MapObject bfNear = nearestTarget;
        nearestTarget = null;
        ArrayList<IGameObject> targets = Scene.top().objectsAt(targetLayer);
        for (IGameObject target : targets) {
            if (target instanceof MapObject) {
                float tx = ((Sprite) target).getX();
                float ty = ((Sprite) target).getY();
                PointF len = WarpUtil.getWrappedDelta(x, y, tx, ty);
                float length = len.length();
                if (length < maxLength){
                    nearestTarget = ((MapObject)target);
                    maxLength = length;
                }
            }
        }

        if(nearestTarget != null){
            float tx = nearestTarget.getX();
            float ty = nearestTarget.getY();
            PointF wPos = WarpUtil.getWrappedDelta(x, y, tx, ty);
            this.radianAngle = (float) Math.atan2(wPos.y, wPos.x);
            this.angle = (float)Math.toDegrees(this.radianAngle);
        }
    }

    private final float SPARK_DURATION = 0.1f;
    private final float SPARK_OFFSET = 66f;
    private final float SPARK_WIDTH = GUN_WIDTH / 2;
    private final float SPARK_HEIGHT = SPARK_WIDTH * 3 / 5;

    private RectF sparkRect;
    private Bitmap sparkBitmap;

    private void fireBullet() {
        MainScene scene = (MainScene) Scene.top();
        if (scene == null) return;

        float angle = this.angle - 10f;
        switch (type){
            case AR:
            case SR:
                Bullet bullet = Bullet.get(x + GUN_WIDTH / 2 * (float)Math.cos(radianAngle), y + GUN_HEIGHT / 2 * (float)Math.sin(radianAngle), radianAngle, power, targetLayer, speed, penetrableTimes);
                scene.add(bullet);
                break;
            case SG:
                for(int i = 0; i < 3; ++i){
                    bullet = Bullet.get(x + GUN_WIDTH / 2 * (float)Math.cos(radianAngle), y + GUN_HEIGHT / 2 * (float)Math.sin(radianAngle), (float) Math.toRadians(angle), power, targetLayer, speed, penetrableTimes);
                    scene.add(bullet);
                    angle += 10f;
                }
                break;

        }

    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw(canvas);
        canvas.save();
        canvas.translate(Metrics.width / 2 - MapObject.camera.x, Metrics.height / 2 - MapObject.camera.y);
        canvas.rotate(angle, x, y);
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);

        // 사격 사거리 출력
//        canvas.drawCircle(x, y, range, paint);

        // 격발 이미지 출력
        if (FIRE_INTERVAL - fireCoolTime < SPARK_DURATION) {
            canvas.rotate(90f, x, y);
            RectUtil.setRect(sparkRect, x, y - SPARK_OFFSET , SPARK_WIDTH, SPARK_HEIGHT);
            canvas.drawBitmap(sparkBitmap, null, sparkRect, null);
        }
        canvas.restore();
    }

    public void setPower(float attackPower) {
        switch (type){
            case AR:
            case SR:
                this.power = attackPower;
                break;
            case SG:
                this.power = attackPower * 0.5f;
                break;
        }
    }

    public void resetCoolTime() {
        fireCoolTime = FIRE_INTERVAL;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
