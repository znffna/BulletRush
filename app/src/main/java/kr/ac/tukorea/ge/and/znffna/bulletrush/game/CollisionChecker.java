package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.CollisionHelper;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class CollisionChecker implements IGameObject {
    private static final String TAG = CollisionChecker.class.getSimpleName();
    private final MainScene scene;

    public CollisionChecker(MainScene mainScene) {
        this.scene = mainScene;
    }

    @Override
    public void update() {
        EnemyToBullet();
        PlayerToBullet();
        PlayerToEnemy();
        PlayerToItem();
    }

    private void PlayerToItem() {
        ArrayList<IGameObject> players = scene.objectsAt(MainScene.Layer.player);
        for (int p = players.size() - 1; p >= 0; p--) {
            Player player = (Player)players.get(p);
            ArrayList<IGameObject> items = scene.objectsAt(MainScene.Layer.item);
            for (int i = items.size() - 1; i >= 0; i--) {
                PowerUp powerItem = (PowerUp)items.get(i);
                if (CollisionHelper.collides(player, powerItem)) {
                    Log.d(TAG, "Collision !! : Player@" + System.identityHashCode(player) + " vs PowerUp@" + System.identityHashCode(powerItem));
                    scene.remove(powerItem);
                    player.applyPowerUp(powerItem);
                    break;
                }
            }
        }
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
                    boolean dead = player.decreaseLife(enemy.getPower());
                    if (dead) {
                        // TODO :: 게임 결과 화면 띄우기
                        Log.d(TAG, "Player Died");
                        GameView.view.popScene();
                    }
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
                    if(!bullet.onHit(player)) {
                        continue; // 이미 해당 총알에 피해를 받았을 경우 무시한다.
                    }
                    boolean dead = player.decreaseLife(bullet.getPower());
                    if (dead) {
                        // TODO :: 게임 결과 화면 띄우기
                        Log.d(TAG, "Player Died");
                        GameView.view.popScene();
                    }
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
                    if(!bullet.onHit(enemy)) {
                        continue; // 이미 해당 총알에 피해를 받았을 경우 무시한다.
                    }
                    boolean dead = enemy.decreaseLife(bullet.getPower());
                    if (dead) {
                        enemy.hasDied();
                        scene.add(PowerUp.get(enemy.getX(), enemy.getY(), (int)(enemy.px + enemy.py) % PowerUp.PowerType.COUNT));
                        scene.addExp(enemy.getExp());
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

