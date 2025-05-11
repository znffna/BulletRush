package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Enemy extends WrapSprite implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer> {

    public enum State {
        idle, move
    }
    State state = State.idle;
    private AnimSprite[] enemyAnimSprite;
    private float ENEMY_WIDTH = 100f;
    private float ENEMY_HEIGHT = ENEMY_WIDTH;
    private float SPEED = 200f;

    private static final int[] idle_resIds = {
            R.mipmap.enemy_idle
    };

    private static final int[] move_resIds = {
            R.mipmap.enemy_move
    };

    private static Player target;

    public static Enemy get(float x, float y, int resIndex) {
        return Scene.top().getRecyclable(Enemy.class).init(x, y, resIndex);
    }

    private Enemy init(float x, float y, int resIndex) {
        setPosition(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
        state = State.idle;
        AnimSprite enemy_idle = new AnimSprite(idle_resIds[resIndex], 5);
        AnimSprite enemy_move = new AnimSprite(move_resIds[resIndex], 5);
        enemyAnimSprite = new AnimSprite[]{enemy_idle, enemy_move};
        return this;
    }

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

    }

    public float[] getPosition() {
        return new float[]{this.x, this.y};
    }

    public static void setTarget(Player target) {
        Enemy.target = target;
    }

    public void update() {
        float distance = SPEED * GameView.frameTime;

        calcuateDrawPosition();
        float deltaX = (float) (target.getX() - this.x);
        float deltaY = (float) (target.getY() - this.y);
        if (deltaX >  Metrics.width  / 2) deltaX -= Metrics.width;
        if (deltaX < -Metrics.width  / 2) deltaX += Metrics.width;
        if (deltaY >  Metrics.height / 2) deltaY -= Metrics.height;
        if (deltaY < -Metrics.height / 2) deltaY += Metrics.height;

        float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        dx = distance * deltaX / length;
        dy = distance * deltaY / length;
        x += dx;
        y += dy;

        if(dx == 0 && dy == 0){
            state = State.idle;
        }
        else{
            state = State.move;
        }


        setPosition(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
        super.update();
    }

    @Override
    public void draw(Canvas canvas) {
        // super.draw(canvas);
        setDstRectPlayerSpace();
        enemyAnimSprite[state.ordinal()].setPosition(px, py, ENEMY_WIDTH, ENEMY_HEIGHT);
        enemyAnimSprite[state.ordinal()].draw(canvas);
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
