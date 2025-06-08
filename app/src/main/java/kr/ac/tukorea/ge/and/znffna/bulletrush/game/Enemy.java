package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.Gauge;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Enemy extends MapObject implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer> {


    private int level;
    private Gun gun;

    public enum EnemyType {
        Normal, Rush, Gunner
    }
    private EnemyType type;

    private float ENEMY_WIDTH = 100f;
    private float ENEMY_HEIGHT = ENEMY_WIDTH;
    private float SPEED = 200f;
    private Gauge gauge;

    public float getExp() {
        return exp;
    }

    public enum State {
        idle, move
    }
    State state = State.idle;
    private AnimSprite[] enemyAnimSprite;

    private static final int[] idle_resIds = {
            R.mipmap.enemy_idle0
    };

    private static final int[] move_resIds = {
            R.mipmap.enemy_move0
    };

    private static MapObject target;

    public static Enemy get(float x, float y, int level, EnemyType type) {
        return Scene.top().getRecyclable(Enemy.class).init(x, y, level, type);
    }

    private Enemy init(float x, float y, int level, EnemyType type) {
        setPosition(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
        state = State.idle;
        this.level = level;
        setType(type);
        return this;
    }

    private void setType(EnemyType type) {
        this.type = type;
        AnimSprite enemy_idle = new AnimSprite(idle_resIds[type.ordinal()], 5);
        AnimSprite enemy_move = new AnimSprite(move_resIds[type.ordinal()], 5);
        enemyAnimSprite = new AnimSprite[]{enemy_idle, enemy_move};

        if (type == EnemyType.Normal){
            this.maxLife = this.life = 100 + level * 40;
            this.power = 10 + level * 20;
            this.exp = (float)Math.pow(1.5f, level) * 100;
        }
        else if (type == EnemyType.Rush){
            this.maxLife = this.life = 100 + level * 40;
            this.power = 40 + level * 40;
            this.exp = (float)Math.pow(3.0f, level) * 100;
        }
        else if (type == EnemyType.Gunner){
            this.maxLife = this.life = 100 + level * 40;
            this.power = 10 + level * 20;
            this.exp = (float)Math.pow(2.0f, level) * 100;
            this.gun = Gun.get(this, MainScene.Layer.player, 50, 5, 0);
            Scene.top().add(this.gun);
        }
    }

    private float life, maxLife;

    public float getPower() {
        return power;
    }

    private float power;
    private float exp;

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

        float barSize = width * 2 / 3;
        if (gauge == null){
            gauge = new Gauge(0.2f, R.color.enemy_health_fg, R.color.enemy_health_bg);
        }
        if(life < maxLife) gauge.draw(canvas, px - barSize / 2, py + barSize / 2 + 30, barSize, life / maxLife);
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
