package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.JoyStick;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.Gauge;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.RectUtil;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Player extends MapObject implements IBoxCollidable, ILayerProvider<MainScene.Layer> {
    private static final String TAG = Player.class.getSimpleName();
    private static final float PLAYER_WIDTH = 100f;
    private static final float PLAYER_HEIGHT = PLAYER_WIDTH;
    private Gauge health_gauge;
    private float life = 100;
    private float maxLife = 100;

    private int level = 1;
    private Gauge exp_gauge;
    private float exp = 0;
    private float maxExp = 100;
    private ArrayList<Gun> guns = new ArrayList<>();

    public void addExp(float exp) {
        this.exp += exp;
        if(this.exp >= maxExp){
            addLevel();
        }
    }

    private void addLevel() {
        setLevel(this.level + 1);

        // exp 재설정
        this.exp -= this.maxExp;
        if( this.exp < 0.0f) this.exp = 0f;
        this.maxExp *= 2;
    }

    private void setLevel(int level) {
        this.level = level;
    }

    public void addGun(Gun gun) {
        this.guns.add(gun);
        gun.setPower(this.attackPower);
    }


    public enum State {
        idle, move
    }
    State state = State.idle;
    private final AnimSprite[] playerAnimSprite;


    private float SPEED;
    private final JoyStick joyStick;

    private float FIRE_INTERVAL = 0.25f;
    // 총알의 damage 처리용
    private float attackPower = 0;

    public Player(JoyStick joyStick) {
        super(0);
        SPEED = 300f;
        AnimSprite player_idle = new AnimSprite(R.mipmap.player_idle, 4);
        AnimSprite player_move = new AnimSprite(R.mipmap.player_move, 4);
        playerAnimSprite = new AnimSprite[] {player_idle, player_move};
        this.joyStick = joyStick;
        this.state = State.idle;
        setPosition(Metrics.width / 2, Metrics.height / 2, PLAYER_WIDTH, PLAYER_HEIGHT);

        maxLife = life = 100f;
        attackPower = 50f;
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
            dx = 0;
            dy = 0;
            setPosition(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
        setCamera(x,y);
        super.update();

    }

    @Override
    public void draw(Canvas canvas) {
        // super.draw(canvas);
        // 맵 중앙 고정 출력
        RectUtil.setRect(dstRect, Metrics.width / 2, Metrics.height / 2, width, height);
        playerAnimSprite[state.ordinal()].setPosition(Metrics.width / 2, Metrics.height / 2, PLAYER_WIDTH, PLAYER_HEIGHT);
        playerAnimSprite[state.ordinal()].draw(canvas);
        
        // life 출력
        float barSize = width * 2 / 3;
        if (health_gauge == null){
            health_gauge = new Gauge(0.2f, R.color.player_health_fg, R.color.player_health_bg);
        }
        health_gauge.draw(canvas, Metrics.width / 2 - barSize / 2, Metrics.height / 2 + barSize / 2 + 30, barSize, life / maxLife);

        // exp 출력
        float exp_aspect = 0.2f;
        float expSize = Metrics.width * 1 / 4;
        if (exp_gauge == null){
            exp_gauge = new Gauge(exp_aspect, R.color.player_exp_fg, R.color.player_exp_bg, Paint.Cap.SQUARE);
        }
        exp_gauge.draw(canvas, 0f, expSize * exp_aspect / 2, expSize, exp / maxExp);
        
        // Status 문자 출력
        drawStatus(canvas);
    }

    private void drawStatus(Canvas canvas) {
        TextHelper.drawFontString(canvas, "EXP:" + (int)(this.exp), 0, 0, 32);
        TextHelper.drawFontString(canvas, "HP:" + (int)(this.life / this.maxLife * 100)+"%", 0, 40, 32);
        TextHelper.drawFontString(canvas, "ATK:" + (int)this.attackPower, 0, 80, 32);
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
