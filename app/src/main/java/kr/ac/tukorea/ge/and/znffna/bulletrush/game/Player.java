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
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.RectUtil;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Player extends WrapSprite implements IBoxCollidable, ILayerProvider<MainScene.Layer> {
    private static final String TAG = Player.class.getSimpleName();
    private static final float PLAYER_WIDTH = 100f;
    private static final float PLAYER_HEIGHT = PLAYER_WIDTH;

    public enum State {
        idle, move
    }
    State state = State.idle;
    private final AnimSprite[] playerAnimSprite;


    private float SPEED;
    private final JoyStick joyStick;

    private float FIRE_INTERVAL = 0.25f;
    private int power = 0;

    public static Player player;

    public Player(JoyStick joyStick) {
        super(0);
        player = this;
        SPEED = 300f;
        AnimSprite player_idle = new AnimSprite(R.mipmap.player_idle, 4);
        AnimSprite player_move = new AnimSprite(R.mipmap.player_move, 4);
        playerAnimSprite = new AnimSprite[] {player_idle, player_move};
        this.joyStick = joyStick;
        this.state = State.idle;
        setPosition(Metrics.width / 2, Metrics.height / 2, PLAYER_WIDTH, PLAYER_HEIGHT);

    }

    public void update() {
        if (joyStick.power <= 0) {
            state = State.idle;
            dx = 0;
            dy = 0;
        }
        else {
            state = State.move;
            float distance = SPEED * joyStick.power * GameView.frameTime;
            dx = (float) (distance * Math.cos(joyStick.angle_radian));
            dy = (float) (distance * Math.sin(joyStick.angle_radian));
            x += dx;
            y += dy;
            setPosition(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
            super.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // super.draw(canvas);
        // 맵 중앙 고정 출력
        RectUtil.setRect(dstRect, Metrics.width / 2, Metrics.height / 2, width, height);
        playerAnimSprite[state.ordinal()].setPosition(Metrics.width / 2, Metrics.height / 2, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerAnimSprite[state.ordinal()].draw(canvas);
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect;
    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.player;
    }

}
