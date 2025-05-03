package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.JoyStick;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Player extends Sprite {
    private static final float PLAYER_WIDTH = 200f;
    private static final float PLAYER_HEIGHT = PLAYER_WIDTH;
    private final AnimSprite player_idle;
    private final AnimSprite player_move;

    private static final float SPEED = 300f;
    private final int state;
    private final JoyStick joyStick;

    private float targetX;
    private float targetY;

    public Player(JoyStick joyStick) {
        super(0);
        player_idle = new AnimSprite(R.mipmap.player_idle, 4);
        player_move = new AnimSprite(R.mipmap.player_move, 4);
        this.joyStick = joyStick;
        state = 0;

        setPosition(Metrics.width / 2, Metrics.height - 200, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public void update() {
        if (joyStick.power <= 0) {
            dx = 0;
            dy = 0;
            return;
        }
        float distance = SPEED * joyStick.power * GameView.frameTime;
        dx = (float) (distance * Math.cos(joyStick.angle_radian));
        dy = (float) (distance * Math.sin(joyStick.angle_radian));
        x += dx;
        y += dy;
        setPosition(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
        super.update();
    }

    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }

    public void setTargetY(float targetY) {
        this.targetY = targetY;
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
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
