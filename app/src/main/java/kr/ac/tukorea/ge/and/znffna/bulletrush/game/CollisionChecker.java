package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.CollisionHelper;

public class CollisionChecker implements IGameObject {
    private static final String TAG = CollisionChecker.class.getSimpleName();
    private final MainScene scene;

    public CollisionChecker(MainScene mainScene) {
        this.scene = mainScene;
    }

    @Override
    public void update() {
        EnemyToBullet();
//        PlayerToBullet();
//        PlayerToEnemy();
    }

    private void PlayerToEnemy() {
        ArrayList<IGameObject> players = scene.objectsAt(MainScene.Layer.player);
        for (int p = players.size() - 1; p >= 0; p--) {
            Player player = (Player)players.get(p);
            ArrayList<IGameObject> enemies = scene.objectsAt(MainScene.Layer.enemy);
            for (int e = enemies.size() - 1; e >= 0; e--) {
                Enemy enemy = (Enemy)enemies.get(e);
                if (CollisionHelper.collides(player, enemy)) {
                    Log.d(TAG, "Collision !! : Player@" + System.identityHashCode(player) + " vs Enemy@" + System.identityHashCode(enemy));
//                    boolean dead = enemy.decreaseLife(bullet.getPower());
//                    if (dead) {
//                        scene.remove(MainScene.Layer.enemy, enemy);
//                        scene.addScore(enemy.getScore());
//                        // removed = true;
//                    }
                    break;
                }
            }
        }
    }

    private void PlayerToBullet() {
        ArrayList<IGameObject> players = scene.objectsAt(MainScene.Layer.player);
        for (int p = players.size() - 1; p >= 0; p--) {
            Player player = (Player)players.get(p);
            ArrayList<IGameObject> bullets = scene.objectsAt(MainScene.Layer.bullet);
            for (int b = bullets.size() - 1; b >= 0; b--) {
                Bullet bullet = (Bullet)bullets.get(b);
                MainScene.Layer layer = bullet.getTargetLayer();
                if(player.getLayer() != layer) continue;
                if (CollisionHelper.collides(player, bullet)) {
                    Log.d(TAG, "Collision !! : Bullet@" + System.identityHashCode(bullet) + " vs Player@" + System.identityHashCode(player));
                    scene.remove(bullet);
//                    boolean dead = enemy.decreaseLife(bullet.getPower());
//                    if (dead) {
//                        scene.remove(MainScene.Layer.enemy, enemy);
//                        scene.addScore(enemy.getScore());
//                        // removed = true;
//                    }
                    break;
                }
            }
        }
    }

    private void EnemyToBullet() {
        ArrayList<IGameObject> enemies = scene.objectsAt(MainScene.Layer.enemy);
        for (int e = enemies.size() - 1; e >= 0; e--) {
            Enemy enemy = (Enemy)enemies.get(e);
            ArrayList<IGameObject> bullets = scene.objectsAt(MainScene.Layer.bullet);
            for (int b = bullets.size() - 1; b >= 0; b--) {
                Bullet bullet = (Bullet)bullets.get(b);
                MainScene.Layer layer = bullet.getTargetLayer();
                if(enemy.getLayer() != layer) continue;
                if (CollisionHelper.collides(enemy, bullet)) {
                    Log.d(TAG, "Collision !! : Bullet@" + System.identityHashCode(bullet) + " vs Enemy@" + System.identityHashCode(enemy));
                    scene.remove(bullet);
                    boolean dead = enemy.decreaseLife(bullet.getPower());
                    if (dead) {
                        scene.remove(MainScene.Layer.enemy, enemy);
//                        scene.addScore(enemy.getScore());
                        // removed = true;
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {}
}

