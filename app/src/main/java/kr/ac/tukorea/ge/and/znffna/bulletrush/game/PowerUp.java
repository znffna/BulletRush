package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;

public class PowerUp extends MapObject implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer> {

    private static final int[] resIds = {
            R.mipmap.power_hp, R.mipmap.power_atk, R.mipmap.power_as, R.mipmap.power_speed
    };
    private static final float POWERUP_WIDTH = 50f;
    private static final float POWERUP_HEIGHT = POWERUP_WIDTH;

    public float getValue() {
        return value;
    }

    private float value;

    public void setType(int type) {
        this.type = PowerType.getType(type);
        setImageResourceId(resIds[type]);

        switch (this.type){
            case attack:
            case hp:
            case moveSpeed:
                value = 10f;
                break;
            case attackSpeed:
                value = 0.1f;
                break;
        }
    }

    public enum State {
        idle, move, attack, stun;

        public static final int COUNT = values().length;
    }
    public enum PowerType{
        hp, attack, attackSpeed, moveSpeed;

        public final static int COUNT = values().length;
        public static PowerType getType(int index) {
            return values()[index];
        }
    }
    private PowerType type;

    public PowerUp() {
        this(0);
    }
    public PowerUp(int type) {
        super(resIds[type]);
    }

    public static PowerUp get(float x, float y, int type) {
        return Scene.top().getRecyclable(PowerUp.class).init(x, y, type);
    }

    private PowerUp init(float x, float y, int type) {
        setPosition(x,y, POWERUP_WIDTH, POWERUP_HEIGHT);
        setType(type);
        return this;
    }


    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.item;
    }

    @Override
    public void onRecycle() {
    }

    public PowerType getType() {
        return type;
    }
}
