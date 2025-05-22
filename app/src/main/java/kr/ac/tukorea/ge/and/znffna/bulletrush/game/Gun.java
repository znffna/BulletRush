package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Gun extends Sprite implements IRecyclable, ILayerProvider<MainScene.Layer> {

    private static final String TAG = Gun.class.getSimpleName();
    private float GUN_WIDTH = 112f;
    private float GUN_HEIGHT = 40f;


    private int type;
    private int power = 5;

    private static final int[] resIds = {
        R.mipmap.assault_rifle, R.mipmap.assault_rifle_left
    };
    private float angle;
    private Player Follow;
    private float GUN_OFFSET_Y = 0f;
    private float GUN_OFFSET_X = 0f;

    private float FIRE_INTERVAL = 0.25f;
    private float fireCoolTime = FIRE_INTERVAL;
    private Enemy nearest;
    private float maxLength;
    private final float MAX_RANGE = Math.min(Metrics.width, Metrics.height);
    private MainScene.Layer targetLayer;

    public Gun(int mipmapId) {
        super(mipmapId);
    }

    public Gun(Player player, float offset_x, float offset_y, int type){
        super(R.mipmap.assault_rifle);
        init(player, offset_x, offset_y, type);
    }

    public static Gun get(Player gameobject, float x, float y) {
        return get(gameobject, x, y,0);
    }

    public static Gun get(Player object, float x, float y, int type) {
        return Scene.top().getRecyclable(Gun.class).init(object, x, y, type);
    }

    Gun init(Player object, float x, float y, int type) {
        setImageResourceId(resIds[type * 2]);
        GUN_OFFSET_X = x;
        GUN_OFFSET_Y = y;
        setFollow(object);
        setType(type);

        setPosition(Follow.getX() + GUN_OFFSET_X, Follow.getY() + GUN_OFFSET_Y, GUN_WIDTH, GUN_HEIGHT);
        this.type = type;
        return this;
    }

    private void setFollow(Player object) {
        Follow = object;
        targetLayer = Follow.getLayer() == MainScene.Layer.enemy? MainScene.Layer.player : MainScene.Layer.enemy;
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
    public void onRecycle() {

    }

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

        if (!findNearestTarget()) return;
        fireCoolTime -= GameView.frameTime;
        if (fireCoolTime <= 0) {
            // 자신과 가장 가까운 enemy 조준
            if (maxLength < MAX_RANGE) {
                if (null == nearest) return;
                float[] targetPosition = nearest.getPosition();
                fireBullet((float) Math.atan2(targetPosition[1] - this.y, targetPosition[0] - this.x));
                fireCoolTime = FIRE_INTERVAL;
            }
        }
    }

    private boolean findNearestTarget() {
        Scene scene = Scene.top();
        if(scene == null) return false;

        maxLength = Float.MAX_VALUE;
        Enemy bfNear = nearest;
        nearest = null;
        ArrayList<IGameObject> enemys = Scene.top().objectsAt(MainScene.Layer.enemy);
        for (IGameObject enemy : enemys) {
            if (enemy instanceof Sprite) {
                float[] position = ((Enemy) enemy).getPosition();
                float length = (position[0] - x) * (position[0] - x) + (position[1] - y) * (position[1] - y);
                if (length < maxLength){
                    nearest = (Enemy) enemy;
                    maxLength = length;
                }
            }
        }
//        if(nearest != bfNear) Log.d(TAG, "Nearest = " + nearest);
//      if(nearest != null) Log.d(TAG, "Nearest = " + nearest);

        maxLength = (float) Math.sqrt(maxLength);
        return true;
    }

    private void fireBullet(float angle) {
        MainScene scene = (MainScene) Scene.top();
        if (scene == null) return;

//        int score = scene.getScore();
//        int power = 10 + score / 1000;

        this.angle = (float) Math.toDegrees(angle);
        Log.d(TAG, "fireBullet | angle= " + this.angle);

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
        canvas.restore();
    }

}
