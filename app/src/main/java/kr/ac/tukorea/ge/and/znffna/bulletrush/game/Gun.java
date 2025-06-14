package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

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
    private float GUN_WIDTH = 112f;
    private float GUN_HEIGHT = 40f;


    private int type;
    private float power = 5f;

    private static final int[] resIds = {
        R.mipmap.assault_rifle, R.mipmap.assault_rifle_left
    };
    private MapObject Follow;
    private float GUN_OFFSET_Y = 0f;
    private float GUN_OFFSET_X = 0f;
    private static Paint paint;
    private float range = 500f;

    public void setFIRE_INTERVAL(float FIRE_INTERVAL) {
        this.FIRE_INTERVAL = FIRE_INTERVAL;
    }

    private float FIRE_INTERVAL = 0.25f;
    private float fireCoolTime = FIRE_INTERVAL;


    private MainScene.Layer targetLayer;
    private MapObject nearest;
    private float angle;


    public Gun(int mipmapId) {
        super(mipmapId);
    }

    public Gun(MapObject object, MainScene.Layer targetLayer, float offset_x, float offset_y, int type){
        super(R.mipmap.assault_rifle);
        init(object, targetLayer, offset_x, offset_y, type);
    }

    public static Gun get(MapObject object, MainScene.Layer targetLayer, float x, float y) {
        return get(object, targetLayer, x, y,0);
    }

    public static Gun get(MapObject object, MainScene.Layer targetLayer, float x, float y, int type) {
        return Scene.top().getRecyclable(Gun.class).init(object, targetLayer, x, y, type);
    }

    Gun init(MapObject object, MainScene.Layer targetLayer, float x, float y, int type) {
        setImageResourceId(resIds[type * 2]);

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
        this.type = type;
        if (type == 0) {
            power = 5;
        } else {
            power = 5;
        }
    }

    @Override
    public void update() {
        super.update();
        // Player 기준 상대 위치로 이동
        setPosition(Follow.getX() + GUN_OFFSET_X, Follow.getY() + GUN_OFFSET_Y, GUN_WIDTH, GUN_HEIGHT);

        // 총 격발 인터벌 갱신
        fireCoolTime -= GameView.frameTime;
        fire();
    }

    public void fire() {
        if (fireCoolTime <= 0) {
            // 자신과 가장 가까운 enemy 조준
            if (!findNearestTarget()) return;

            if (maxLength < MAX_RANGE) {
                if (null == nearest) return;
                float tx = nearest.getX();
                float ty = nearest.getY();
                fireBullet((float) Math.atan2(ty - this.y, tx - this.x));
                fireCoolTime = FIRE_INTERVAL;
            }
        }
    }

    private float maxLength;
    private final float MAX_RANGE = Math.min(Metrics.width, Metrics.height);
    private boolean findNearestTarget() {
        Scene scene = Scene.top();
        if(scene == null) return false;

        maxLength = range * range;
        MapObject bfNear = nearest;
        nearest = null;
        ArrayList<IGameObject> targets = Scene.top().objectsAt(targetLayer);
        for (IGameObject target : targets) {
            if (target instanceof MapObject) {
                float tx = ((Sprite) target).getX();
                float ty = ((Sprite) target).getY();
                float length = (tx - x) * (tx - x) + (ty - y) * (ty - y);
                if (length < maxLength){
                    nearest = ((MapObject)target);
                    maxLength = length;
                }
            }
        }

        maxLength = (float) Math.sqrt(maxLength);
        return true;
    }

    private float SPARK_DURATION = 0.1f;
    private float SPARK_OFFSET = 66f;
    private float SPARK_WIDTH = GUN_WIDTH / 2;
    private float SPARK_HEIGHT = SPARK_WIDTH * 3 / 5;

    private RectF sparkRect;
    private Bitmap sparkBitmap;

    private void fireBullet(float angle) {
        MainScene scene = (MainScene) Scene.top();
        if (scene == null) return;

        this.angle = (float) Math.toDegrees(angle);
        Bullet bullet = Bullet.get(x + GUN_WIDTH / 2 * (float)Math.cos(angle), y + GUN_HEIGHT / 2 * (float)Math.sin(angle), angle, power, targetLayer);
        scene.add(bullet);
    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw(canvas);
        canvas.save();
        canvas.translate(Metrics.width / 2 - MapObject.camera.x, Metrics.height / 2 - MapObject.camera.y);
        canvas.rotate(angle, x, y);
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);

        // 사격 사거리 출력
        canvas.drawCircle(x, y, range, paint);

        // 격발 이미지 출력
        if (FIRE_INTERVAL - fireCoolTime < SPARK_DURATION) {
            canvas.rotate(90f, x, y);
            RectUtil.setRect(sparkRect, x, y - SPARK_OFFSET , SPARK_WIDTH, SPARK_HEIGHT);
            canvas.drawBitmap(sparkBitmap, null, sparkRect, null);
        }
        canvas.restore();
    }

    public void setPower(float attackPower) {
        this.power = attackPower;
    }
}
