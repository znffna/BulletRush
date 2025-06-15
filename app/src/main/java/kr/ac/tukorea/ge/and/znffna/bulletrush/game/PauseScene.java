package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.app.AlertDialog;
import android.content.DialogInterface;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class PauseScene extends Scene {


    public enum Layer {
        bg, title, touch
    }

    public PauseScene() {
        initLayers(Layer.values().length);

        float w = Metrics.width, h = Metrics.height;
        add(Layer.bg, new Sprite(R.mipmap.trans_50b, w/2, h/2, w, h));

        add(Layer.touch, new Button(R.mipmap.resume, 850f, 100f, 200f, 75f, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {
                pop();
                return false;
            }
        }));

        add(Layer.touch, new Button(R.mipmap.exit, Metrics.width - 200f, 550f, 200f, 100f, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {
                new AlertDialog.Builder(GameView.view.getContext())
                        .setTitle("Confirm")
                        .setMessage("Do you really want to exit the game?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                popAll();
                            }
                        })
                        .create()
                        .show();
                return false;
            }
        }));
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.touch.ordinal();
    }

    // Overridables
    @Override
    public boolean isTransparent() {
        return true;
    }

}
