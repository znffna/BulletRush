package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.JoyStick;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.Gauge;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MainScene extends Scene {


    private final Player player;
    private final JoyStick joyStick;

    public void addExp(float exp) {
        player.addExp(exp);
    }

    public enum Layer {
        none, bg, enemy, bullet, player, gun, ui, controller;
        public static final int COUNT = values().length;
    }

    public MainScene() {
        Metrics.setGameSize(900, 1500);
        initLayers(Layer.COUNT);

        joyStick = new JoyStick(R.mipmap.joystick_bg, R.mipmap.joystick_thumb, Metrics.width - 200, Metrics.height - 200, 100, 30, 80);
        player = new Player(joyStick);
        Enemy.setTarget(player);

        Gun gun = new Gun(player, 50, 5, 0);
        add(Layer.gun, gun);

        add(Layer.player, player);
        add(Layer.ui, joyStick);

        ScrollBackground bg = new ScrollBackground(R.mipmap.background, player);
        add(Layer.bg, bg);

        add(Layer.controller, new CollisionChecker(this));
        add(Layer.controller, new EnemyGenerator(this));
//        add(Layer.enemy, new Enemy(0, 100, 100));
    }

    // Game Loop Functions
    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return joyStick.onTouch(event);
    }
}
