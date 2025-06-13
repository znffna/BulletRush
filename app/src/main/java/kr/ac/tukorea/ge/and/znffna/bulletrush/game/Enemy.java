package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.Gauge;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class Enemy extends MapObject implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer> {


    private int level;
    private Gun gun;
    private float range;
    private Paint paint;
    private final int[] rangeColor = {Color.CYAN, Color.YELLOW, Color.RED};

    public void hasDied() {
        GameView.view.getTopScene().remove(MainScene.Layer.enemy, this);
        if(this.gun != null) GameView.view.getTopScene().remove(MainScene.Layer.gun, this.gun);
    }

    public enum EnemyType {
        Normal, Rush, Gunner;

        public static final int COUNT = values().length;

        public static EnemyType getType(int index) {
            return values()[index % COUNT];
        }

    }
    private EnemyType type;

    private float ENEMY_WIDTH = 100f;
    private float ENEMY_HEIGHT = ENEMY_WIDTH;
    private float speed = 200f;
    private Gauge gauge;

    public float getExp() {
        return exp;
    }

    public enum State {
        idle, move, rush, stun;

        public static final int COUNT = values().length;
    }

    State state = State.idle;
    public void setState(State state) {
        this.state = state;
        setImageResourceId(resource_resIds[this.type.ordinal() * State.COUNT + state.ordinal()], 4);
    }


    private static final int[] resource_resIds = {
            R.mipmap.enemy_idle0, R.mipmap.enemy_move0, R.mipmap.enemy_idle0, R.mipmap.enemy_idle0,
            R.mipmap.enemy_idle1, R.mipmap.enemy_move1, R.mipmap.enemy_idle1, R.mipmap.enemy_idle1,
            R.mipmap.enemy_idle2, R.mipmap.enemy_move2, R.mipmap.enemy_idle2, R.mipmap.enemy_idle2
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

        if(paint == null){
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1.5f);
        }

        paint.setColor(rangeColor[type.ordinal()]);

        return this;
    }

    private void setType(EnemyType type) {
        this.type = type;
        if (type == EnemyType.Normal){
            this.maxLife = this.life = 100 + level * 40;
            this.power = 10 + level * 20;
            this.exp = (float)Math.pow(1.5f, level) * 100;
            this.range = ENEMY_WIDTH * 1.5f;
            this.speed = 200f;
        }
        else if (type == EnemyType.Rush){
            this.maxLife = this.life = 100 + level * 40;
            this.power = 40 + level * 40;
            this.exp = (float)Math.pow(3.0f, level) * 100;
            this.range = ENEMY_WIDTH * 6.0f;
            this.speed = 200f;
        }
        else if (type == EnemyType.Gunner){
            this.maxLife = this.life = 100 + level * 40;
            this.power = 10 + level * 20;
            this.exp = (float)Math.pow(2.0f, level) * 100;
            this.gun = Gun.get(this, MainScene.Layer.player, 50, 5, 0);
            this.gun.setFIRE_INTERVAL(2.0f);
            Scene.top().add(this.gun);
            this.range = ENEMY_WIDTH * 6.0f;
            this.speed = 200f;
        }
    }

    private float life, maxLife;

    public float getPower() {
        return power;
    }

    private float power;
    private float exp;

    public boolean decreaseLife(float power) {
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
        this(resource_resIds[0], x, y);
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
        // 플레이어와의 상대적 위치 계산
        PointF vec = getWrappedDelta(x, y, target.getX(), target.getY());
        updateByType(vec);
    }

    private void updateByType(PointF vec) {
        // Type에 따른 State 변경 - State는 Bitmap 선택과 관련
        if(vec.length() < range){
            Attack(vec);
        }
        else{
            state = State.move;
            setState(State.move);
            moveTo(vec);
        }
        super.update();

        // 목표 위치를 넘지 않도록 위치 수정.
        float adjx = x;
        PointF newVec = getWrappedDelta(x, y, target.getX(), target.getY());
        if((vec.x < 0 && newVec.x > 0) || (vec.x > 0 && newVec.x < 0)){
            adjx = target.getX();
        }
        if(adjx != x){
            setPositionTo(adjx, y);
        }
        float adjy = y;
        if((vec.y < 0 && newVec.y > 0) || (vec.y > 0 && newVec.y < 0)){
            adjy = target.getY();
        }
        if(adjy != y){
            setPositionTo(x, adjy);
        }
    }

    private void setPositionTo(float x, float y) {
        this.x = x;
        this.y = y;
        dstRect.offsetTo(x - width / 2, y - height / 2);
    }

    private void moveTo(PointF shift) {
        dx = speed * shift.x / shift.length();
        dy = speed * shift.y / shift.length();
    }


    private void Attack(PointF vec) {
        if (type == EnemyType.Normal){
            dx = 0f;
            dy = 0f;
            state = State.idle;
        }
        else if (type == EnemyType.Rush){
            if(state == State.idle || state == State.move){
                state = State.rush;
                speed = 600;
            }
        }
        else if (type == EnemyType.Gunner){
            dx = 0f;
            dy = 0f;
            state = State.idle;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // super.draw(canvas);
        setDstRectCameraSpace();

        super.draw(canvas);

        if(GameView.drawsDebugStuffs) {
            // Range 출력
            canvas.drawCircle(px, py, range, paint);
        }

        // life 출력
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
