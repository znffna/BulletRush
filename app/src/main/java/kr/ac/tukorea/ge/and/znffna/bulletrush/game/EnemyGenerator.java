package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class EnemyGenerator implements IGameObject {
    private static final String TAG = EnemyGenerator.class.getSimpleName();
    private final Random random = new Random();
    public static final float GEN_INTERVAL = 10.0f;
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
        
        // 적이 없는 경우에 바로 생성
        if(Scene.top().objectsAt(MainScene.Layer.enemy).isEmpty()){
            generate();
            enemyTime = GEN_INTERVAL;
        }
    }

    private void generate() {
        wave++;

        StringBuilder enemies = new StringBuilder(); // for debug

        Player player = (Player) scene.objectsAt(MainScene.Layer.player).get(0);
        float px = player.getX();
        float py = player.getY();
        for(int i = 0; i < wave / 2 + 1; ++i){
            int level = random.nextInt(wave / 10 + 1);
            float x = random.nextFloat(), y = random.nextFloat();
            Enemy.EnemyType type = Enemy.EnemyType.getType(random.nextInt(Enemy.EnemyType.COUNT));
            scene.add(Enemy.get(px + Metrics.width/2 + x * (Metrics.worldWidth - Metrics.width), py + Metrics.height / 2 + y * (Metrics.worldHeight - Metrics.height), level, type));

            enemies.append(level); // for debug
        }

        Log.v(TAG, "Generating: wave " + wave + " : " + enemies);
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
