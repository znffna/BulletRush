package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.JoyStick;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Player extends Sprite implements IBoxCollidable, ILayerProvider<MainScene.Layer> {
    private static final String TAG = Player.class.getSimpleName();
    private static final float PLAYER_WIDTH = 200f;
    private static final float PLAYER_HEIGHT = PLAYER_WIDTH;
    private final AnimSprite player_idle;
    private final AnimSprite player_move;

    private static final float SPEED = 300f;
    private final int state;
    private final JoyStick joyStick;

    private float FIRE_INTERVAL = 0.25f;
    private float fireCoolTime = FIRE_INTERVAL;
    private int power = 0;

    private Gun gun;
    private float GUN_OFFSET_X = 100f;
    private float GUN_OFFSET_Y = 10f;

    private Enemy target;

    float getX(){
        return this.x;
    }

    float getY(){
        return this.y;
    }

    public Player(JoyStick joyStick) {
        super(0);
        player_idle = new AnimSprite(R.mipmap.player_idle, 4);
        player_move = new AnimSprite(R.mipmap.player_move, 4);
        this.joyStick = joyStick;
        state = 0;
        setPosition(Metrics.width / 2, Metrics.height - 200, PLAYER_WIDTH, PLAYER_HEIGHT);

        gun = new Gun();
        gun.init(x + GUN_OFFSET_X,y + GUN_OFFSET_Y, 0);
    }

    public void update() {
        if (joyStick.power <= 0) {
            dx = 0;
            dy = 0;
        }
        else {
            float distance = SPEED * joyStick.power * GameView.frameTime;
            dx = (float) (distance * Math.cos(joyStick.angle_radian));
            dy = (float) (distance * Math.sin(joyStick.angle_radian));
            x += dx;
            y += dy;
            setPosition(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
            super.update();
        }

        fireCoolTime -= GameView.frameTime;
        if (null != target && fireCoolTime <= 0) {

            float[] targetPosition = target.getPosition();

            Log.d(TAG, "target = " + target + "position = (" + targetPosition[0] + "," + targetPosition[1] + ")");
            Log.d(TAG, "player_position = (" + x + "," + y + ")");

            fireBullet((float)Math.atan2(targetPosition[1] - this.y, targetPosition[0] - this.x));
            fireCoolTime = FIRE_INTERVAL;
        }

        gun.setPosition(x + GUN_OFFSET_X, y + GUN_OFFSET_Y);
        gun.update();
    }

    private void fireBullet(float angle) {
        MainScene scene = (MainScene) Scene.top();
        if (scene == null) return;

//        int score = scene.getScore();
//        int power = 10 + score / 1000;

        Bullet bullet = Bullet.get(x, y, angle, power);
        scene.add(bullet);
    }

    @Override
    public void draw(Canvas canvas) {
        // super.draw(canvas);
        if (dx == 0 && dy == 0) {
            player_idle.setPosition(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
            player_idle.draw(canvas);
        }
        else {
            player_move.setPosition(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
            player_move.draw(canvas);
        }

        gun.draw(canvas);
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect;
    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.player;
    }

    public void setTarget(Enemy target) {
        this.target = target;
    }
}
