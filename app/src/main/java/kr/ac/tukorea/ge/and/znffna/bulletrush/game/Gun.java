package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.RectF;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;

public class Gun extends Sprite implements IRecyclable, ILayerProvider<MainScene.Layer> {

    private float GUN_WIDTH = 224f;
    private float GUN_HEIGHT = 80f;

    private int type;
    private float power = 0f;

    private static final int[] resIds = {
        R.mipmap.assault_rifle, R.mipmap.assault_rifle_left
    };

    public Gun(int mipmapId) {
        super(mipmapId);
    }

    public static Gun get(float x, float y) {
        return get(x,y,0);
    }

    public static Gun get(float x, float y, int type) {
        return Scene.top().getRecyclable(Gun.class).init(x, y, type);
    }

    Gun init(float x, float y, int type) {
        setImageResourceId(resIds[type * 2]);
        setPosition(x, y, GUN_WIDTH, GUN_HEIGHT);
        this.type = type;
        return this;
    }

    public Gun() {
        super(R.mipmap.bullet);
    }

    public void setPosition(float x, float y){
        setPosition(x, y, GUN_WIDTH, GUN_HEIGHT);
    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.gun;
    }

    @Override
    public void onRecycle() {

    }

    public void setType(int type) {
        this.type = type;
        if (type == 0) {
            power = 5f;
        } else {
            power = 5f;
        }
    }
}
