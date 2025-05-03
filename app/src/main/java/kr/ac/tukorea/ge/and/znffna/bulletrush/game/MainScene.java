package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.view.MotionEvent;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.JoyStick;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class MainScene extends Scene {

    private final Player player;
    private final JoyStick joyStick;

    private float minLength = Float.MAX_VALUE;
    private Enemy target = null;

    public enum Layer {
        bg, enemy, bullet, player, gun, ui, controller;
        public static final int COUNT = values().length;
    }

    public MainScene() {
        Metrics.setGameSize(900, 1500);
        initLayers(Layer.COUNT);

        joyStick = new JoyStick(R.mipmap.joystick_bg, R.mipmap.joystick_thumb, 100, 1500, 100, 30, 80);
        player = new Player(joyStick);
        Enemy.setTarget(player);

        add(Layer.player, player);
        add(Layer.ui, joyStick);

        add(Layer.controller, new EnemyGenerator(this));
//        add(Layer.enemy, new Enemy(0, 100, 100));
    }

    // Game Loop Functions
    @Override
    public void update() {
        super.update();
        setPlayerTarget();
    }

    private void setPlayerTarget() {
        // player target Update
        minLength = Float.MAX_VALUE;
        target = null;

        ArrayList<IGameObject> enemyArrayList = objectsAt(Layer.enemy);
        int count = enemyArrayList.size();
        for(int i = 0; i < count; ++i){
            Enemy enemy = (Enemy)enemyArrayList.get(i);
            float[] enemyPosition = enemy.getPosition();
            float playerX = player.getX();
            float playerY = player.getY();

            float length = (enemyPosition[0] - playerX) * (enemyPosition[0] - playerX) + (enemyPosition[1] - playerY) * (enemyPosition[1] - playerY);

            if (length < minLength){
                minLength = length;
                target = enemy;
            }
        }
        player.setTarget(target);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return joyStick.onTouch(event);
    }
}
