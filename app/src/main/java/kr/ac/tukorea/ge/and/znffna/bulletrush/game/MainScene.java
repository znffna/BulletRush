package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.view.MotionEvent;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.and.znffna.bulletrush.util.TextHelper;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.JoyStick;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.Sound;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MainScene extends Scene {


    private final Player player;
    private final JoyStick joyStick;

    private float playTime = 0f;

    public void addExp(float exp) {
        player.addExp(exp);
    }

    public enum Layer {
        none, bg, enemy, bullet, player, gun, ui, item, touch, controller;
        public static final int COUNT = values().length;
    }

    public MainScene(int weaponId) {
        Metrics.setGameSize(900, 1600);
        initLayers(Layer.COUNT);

        joyStick = new JoyStick(R.mipmap.joystick_bg, R.mipmap.joystick_thumb, Metrics.width - 200, Metrics.height - 200, 100, 30, 80);
        player = new Player(joyStick);
        Enemy.setTarget(player);

        Gun gun = new Gun(player, Layer.enemy,  50, 5, weaponId);
        player.addGun(gun);
        add(Layer.gun, gun);

        add(Layer.player, player);
        add(Layer.ui, joyStick);

        ScrollBackground bg = new ScrollBackground(R.mipmap.background, player);
        add(Layer.bg, bg);

        add(Layer.touch, new Button(R.mipmap.btn_pause, Metrics.width - 100, 100f, 100f, 100f, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {
                if(pressed) {
                    new PauseScene().push();
                    return true;
                }
                return false;
            }
        }));

        add(Layer.controller, new CollisionChecker(this));
        add(Layer.controller, new EnemyGenerator(this));
//        add(Layer.enemy, new Enemy(0, 100, 100));
    }


    // Game Loop Functions
    @Override
    public void update() {
        playTime += GameView.frameTime;
        super.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // draw PlayTime
        int fontSize = 40;
        TextHelper.drawFontString(canvas, String.format("%02d:%02d", (int)playTime / 60, (int)playTime % 60), (int) (Metrics.width / 2) - (int)(fontSize * 2.5), 0, fontSize);
    }


    @Override
    public boolean onBackPressed() {
        new PauseScene().push();
        return true;
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.touch.ordinal();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        joyStick.onTouchEvent(event);
        return true;
    }

    @Override
    public void onEnter() {
        Sound.playMusic(R.raw.game_music);
    }
    @Override
    public void onPause() {
        Sound.pauseMusic();
    }

    @Override
    public void onResume() {
        Sound.resumeMusic();
    }
    @Override
    public void onExit() {
        Sound.stopMusic();
    }
}
