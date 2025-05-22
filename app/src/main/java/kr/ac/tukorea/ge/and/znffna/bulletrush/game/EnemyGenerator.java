package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class EnemyGenerator implements IGameObject {
    private static final String TAG = EnemyGenerator.class.getSimpleName();
    private final Random random = new Random();
    public static final float GEN_INTERVAL = 5.0f;
    private final MainScene scene;
    private float enemyTime = 0;
    private int wave;

    public EnemyGenerator(MainScene mainScene) {
        this.scene = mainScene;
    }

    @Override
    public void update() {
        enemyTime -= GameView.frameTime;
        if (enemyTime < 0) {
            generate();
            enemyTime = GEN_INTERVAL;
        }
    }

    private void generate() {
        wave++;

        StringBuilder enemies = new StringBuilder(); // for debug

        int level = 0;
        float x = random.nextFloat(), y = random.nextFloat();
        int resIndex = 0;
        scene.add(Enemy.get(x % Metrics.worldWidth, y % Metrics.worldHeight, resIndex, level));

        enemies.append(level); // for debug

        Log.v(TAG, "Generating: wave " + wave + " : " + enemies.toString());
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
