package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import static java.lang.Math.abs;

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
import kr.ac.tukorea.ge.and.znffna.bulletrush.util.WarpUtil;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class Enemy extends MapObject implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer> {

    // Enemy의 종류
    public enum EnemyType {
        Normal, Rush, Gunner;

        public static final int COUNT = values().length;

        public static EnemyType getType(int index) {
            return values()[index % COUNT];
        }

    }

    private EnemyType type;

    // Enemy 상태(행동)
    public enum State {
        idle, move, attack, stun;

        public static final int COUNT = values().length;
    }

    State state = State.idle;

    // Status
    private int level;
    private Gun gun;
    private float range;
    private float stunTime;

    private final float DEFAULT_SPEED = 150f;
    private float speed = DEFAULT_SPEED;

    private Paint paint;
    private final int[] rangeColor = {Color.CYAN, Color.YELLOW, Color.RED};

    public void hasDied() {
        GameView.view.getTopScene().remove(MainScene.Layer.enemy, this);
        if (this.gun != null) {
            GameView.view.getTopScene().remove(MainScene.Layer.gun, this.gun);
            this.gun = null;
        }
    }

    // Bitmap
    private final float ENEMY_WIDTH = 100f;
    private final float ENEMY_HEIGHT = ENEMY_WIDTH;
    private Gauge gauge;

    public float getExp() {
        return exp;
    }

    public void setState(State state, float time) {
        if (this.state == state) return;

        switch (state) {
            case idle:
            case move:
                break;
            case attack:
                switch (type) {
                    case Normal:
                    case Gunner:
                        break;
                    case Rush:
                        speed = DEFAULT_SPEED * 8;
                        break;
                }
                break;
            case stun:
                switch (type) {
                    case Normal:
                    case Gunner:
                        break;
                    case Rush:
                        speed = DEFAULT_SPEED;
                        stunTime = time;
                        break;
                }
                break;
        }
        this.state = state;
    }


    private static final int[][] resource_resIds = {
            {R.mipmap.enemy_idle0, R.mipmap.enemy_move0},
            {R.mipmap.enemy_idle1, R.mipmap.enemy_move1},
            {R.mipmap.enemy_idle2, R.mipmap.enemy_move2}
    };

    private static MapObject target;

    public static Enemy get(float x, float y, int level, EnemyType type) {
        return Scene.top().getRecyclable(Enemy.class).init(x, y, level, type);
    }

    private Enemy init(float x, float y, int level, EnemyType type) {
        setPosition(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
        setType(type);
        setState(State.idle, 2.0f);
        this.level = level;

        if (paint == null) {
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(1.5f);
        }

        dx = 0f;
        dy = 0f;

        paint.setColor(rangeColor[type.ordinal()]);

        return this;
    }

    private void setType(EnemyType type) {
        this.type = type;
        if (type == EnemyType.Normal) {
            this.maxLife = this.life = 100 + level * 40;
            this.power = 10 + level * 20;
            this.exp = (float) Math.pow(1.5f, level) * 100;
            this.range = ENEMY_WIDTH * 0.5f;
            this.speed = DEFAULT_SPEED;
        } else if (type == EnemyType.Rush) {
            this.maxLife = this.life = 100 + level * 40;
            this.power = 40 + level * 40;
            this.exp = (float) Math.pow(3.0f, level) * 100;
            this.range = ENEMY_WIDTH * 6.0f;
            this.speed = DEFAULT_SPEED;
        } else if (type == EnemyType.Gunner) {
            this.maxLife = this.life = 100 + level * 40;
            this.power = 10 + level * 20;
            this.exp = (float) Math.pow(2.0f, level) * 100;
            this.gun = Gun.get(this, MainScene.Layer.player, 50, 5, 0);
            this.gun.setFIRE_INTERVAL(2.0f);
            this.gun.setSpeed(200.0f);
            this.gun.setPower(this.power);
            Scene.top().add(this.gun);
            this.gun.setRange(this.range = ENEMY_WIDTH * 6.0f);
            this.speed = DEFAULT_SPEED;
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
        this(resource_resIds[0][0], x, y);
    }

    public Enemy(int mipmapId, float x, float y) {
        super(mipmapId);
    }

    public static void setTarget(Player target) {
        Enemy.target = target;
    }

    @Override
    public void update() {
        // 플레이어와의 상대적 위치 계산
        PointF vec = WarpUtil.getWrappedDelta(x, y, target.getX(), target.getY());
        updateByState(vec);

        // Bitmap은 이동중일시 Move, 아닐시 Idle 로 출력.
        if (dx == 0f && dy == 0f) {
            setImageResourceId(resource_resIds[this.type.ordinal()][0], 4);
        } else {
            setImageResourceId(resource_resIds[this.type.ordinal()][1], 4);
        }
    }

    private void updateByState(PointF vec) {
        // Type에 따른 State 변경 - State는 Bitmap 선택과 관련
        switch (state) {
            case idle:
            case move:
                if (vec.length() < range) {
                    Attack(vec);
                } else {
                    setState(State.move, 2.0f);
                    moveTo(vec);
                }
                super.update();

                // 목표 위치를 넘지 않도록 위치 수정.
                float adjx = x;
                PointF newVec = WarpUtil.getWrappedDelta(x, y, target.getX(), target.getY());
                if ((vec.x < 0 && newVec.x > 0) || (vec.x > 0 && newVec.x < 0)) {
                    adjx = target.getX();
                }
                if (adjx != x) {
                    setPositionTo(adjx, y);
                }
                float adjy = y;
                if ((vec.y < 0 && newVec.y > 0) || (vec.y > 0 && newVec.y < 0)) {
                    adjy = target.getY();
                }
                if (adjy != y) {
                    setPositionTo(x, adjy);
                }
                break;
            case attack:
                switch (type) {
                    case Normal:
                    case Gunner:
                        // 공격 사거리를 벗어날 경우 attack 상태를 벗어난다.
                        super.update();
                        if (vec.length() >= range) {
                            setState(State.idle, 2.0f);
                        }
                        break;
                    case Rush:
                        super.update();
                        newVec = WarpUtil.getWrappedDelta(x, y, target.getX(), target.getY());
                        if (abs(newVec.x) > Metrics.width / 2 || abs(newVec.y) > Metrics.height / 2 ) {
                            setState(State.stun, 2.0f);
                        }
                        break;
                }
                break;
            case stun:
                stunTime -= GameView.frameTime;
                if (stunTime < 0.0f) {
                    setState(State.idle, 2.0f);
                }
                break;
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
        setState(State.attack, 2.0f);
        switch (type) {
            case Normal:
            case Gunner:
                dx = 0f;
                dy = 0f;
                if (gun != null) gun.resetCoolTime();
                break;
            case Rush:
                setState(State.attack, 2.0f);
                moveTo(vec);
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        // super.draw(canvas);
        setDstRectCameraSpace();

        super.draw(canvas);

        if (GameView.drawsDebugStuffs) {
            // Range 출력
            canvas.drawCircle(px, py, range, paint);
        }

        // life 출력
        float barSize = width * 2 / 3;
        if (gauge == null) {
            gauge = new Gauge(0.2f, R.color.enemy_health_fg, R.color.enemy_health_bg);
        }
        if (life < maxLife)
            gauge.draw(canvas, px - barSize / 2, py + barSize / 2 + 30, barSize, life / maxLife);


        // Debug용 이동방향 출력
        canvas.drawLine(px, py, px + dx, py + dy, paint);
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
