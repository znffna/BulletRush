package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Enemy extends MapObject implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer> {


    private float ENEMY_WIDTH = 100f;
    private float ENEMY_HEIGHT = ENEMY_WIDTH;
    private float SPEED = 200f;


    public enum State {
        idle, move
    }
    State state = State.idle;
    private AnimSprite[] enemyAnimSprite;

    private static final int[] idle_resIds = {
            R.mipmap.enemy_idle
    };

    private static final int[] move_resIds = {
            R.mipmap.enemy_move
    };

    private static MapObject target;

    public static Enemy get(float x, float y, int resIndex, int level) {
        return Scene.top().getRecyclable(Enemy.class).init(x, y, resIndex, level);
    }

    private Enemy init(float x, float y, int resIndex, int level) {
        setPosition(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
        state = State.idle;
        AnimSprite enemy_idle = new AnimSprite(idle_resIds[resIndex], 5);
        AnimSprite enemy_move = new AnimSprite(move_resIds[resIndex], 5);
        enemyAnimSprite = new AnimSprite[]{enemy_idle, enemy_move};
        setLevel(level);
        return this;
    }

    private int life, maxLife;
    private int power;

    private void setLevel(int level) {
        this.maxLife = this.life = 100 + level * 40;
        this.power = 10 + level * 20;
    }

    public boolean decreaseLife(int power) {
        life -= power;
        return life <= 0;
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

    @Override
    public void update() {
//        calcuateDrawPosition();
        float deltaX = (float) (target.getX() - this.x);
        float deltaY = (float) (target.getY() - this.y);
        if (deltaX >  Metrics.worldWidth / 2) deltaX -= Metrics.worldWidth;
        if (deltaX < -Metrics.worldWidth / 2) deltaX += Metrics.worldWidth;
        if (deltaY >  Metrics.worldHeight / 2) deltaY -= Metrics.worldHeight;
        if (deltaY < -Metrics.worldHeight / 2) deltaY += Metrics.worldHeight;

        float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        dx = SPEED * deltaX / length;
        dy = SPEED * deltaY / length;
//        x += dx;
//        y += dy;

        if(dx == 0 && dy == 0){
            state = State.idle;
        }
        else{
            state = State.move;
        }
//        setPosition(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
        super.update();
    }

    @Override
    public void draw(Canvas canvas) {
        // super.draw(canvas);
        setDstRectCameraSpace();
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
