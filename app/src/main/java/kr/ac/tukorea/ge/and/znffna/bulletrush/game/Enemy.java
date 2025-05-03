package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class Enemy extends Sprite implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer> {

    private AnimSprite enemy_idle;
    private AnimSprite enemy_move;
    private float ENEMY_WIDTH = 200f;
    private float ENEMY_HEIGHT = 200f;
    private float SPEED = 200f;

    private static Player target;

    public Enemy() {
        this(0, 0);
    }
    public Enemy(int mipmapId) {
        this(mipmapId, 0, 0);
    }

    public Enemy(float x, float y) {
        this(0, x, y);
    }

    public Enemy(int mipmapId, float x, float y) {
        super(mipmapId);
        this.x = x;
        this.y = y;
        enemy_idle = new AnimSprite(R.mipmap.enemy_idle, 5);
        enemy_move = new AnimSprite(R.mipmap.enemy_move, 5);
    }

    public float[] getPosition() {
        return new float[]{this.x, this.y};
    }

    public static void setTarget(Player target) {
        Enemy.target = target;
    }

    public void update() {
        float distance = SPEED * GameView.frameTime;
        float deltaX = (float) (target.getX() - x);
        float deltaY = (float) (target.getY() - y);
        float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        dx = distance * deltaX / length;
        dy = distance * deltaY / length;
        x += dx;
        y += dy;
        setPosition(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);

        super.update();
    }

    @Override
    public void draw(Canvas canvas) {
        // super.draw(canvas);
        if (dx == 0 && dy == 0) {
            enemy_idle.setPosition(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
            enemy_idle.draw(canvas);
        }
        else {
            enemy_move.setPosition(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
            enemy_move.draw(canvas);
        }
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect;
    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.enemy;
    }

    @Override
    public void onRecycle() {

    }
}
