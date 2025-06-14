package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.graphics.Canvas;

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class HitPopup implements IGameObject, ILayerProvider<MainScene.Layer> {

    private String str;
    private float startX;
    private float startY;
    private int fontSize;
    private float lifeTime = 1.0f;

    public HitPopup(String str, float startX, float startY, int fontSize, float dx, float dy) {
        this(str, startX, startY, fontSize, dx, dy, 1.0f);
    }

    public void setVelocity(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    private float dx = 0f;
    private float dy = 0f;

    public HitPopup(String str, float startX, float startY, int fontSize) {
        this(str, startX, startY, fontSize, 0f, 0f, 1.0f);
    }

    public HitPopup(String str, float startX, float startY, int fontSize, float dx, float dy, float lifeTime) {
        init(str, startX, startY, fontSize, dx, dy, lifeTime);
    }

    private void init(String str, float startX, float startY, int fontSize, float dx, float dy, float lifeTime) {
        this.str = str;
        this.startX = startX;
        this.startY = startY;
        this.fontSize = fontSize;
        setVelocity(dx, dy);
        this.lifeTime = lifeTime;
    }

    @Override
    public void update() {
        startX += dx * GameView.frameTime;
        startY += dy * GameView.frameTime;
        lifeTime -= GameView.frameTime;
        if(lifeTime < 0.0f){
            Scene.top().remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        TextHelper.drawFontString(canvas, str, (int)startX, (int)startY, fontSize);
    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.ui;
    }
}
