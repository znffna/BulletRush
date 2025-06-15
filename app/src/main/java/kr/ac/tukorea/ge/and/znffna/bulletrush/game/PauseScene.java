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

        add(Layer.bg, new Sprite(R.mipmap.title, w/2, h / 5, w * 2 / 3, h / 3));

        add(Layer.touch, new Button(R.mipmap.resume, w / 2 + 100, h / 2, 200f, 75f, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {
                if(pressed) pop();
                return false;
            }
        }));

        add(Layer.touch, new Button(R.mipmap.exit, w / 2 - 200, h / 2, 200f, 75f, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {
                if(!pressed) return false;
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

    // Overridables
    @Override
    public boolean onBackPressed() {
        pop();
        return true;
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.touch.ordinal();
    }

    @Override
    public boolean isTransparent() {
        return true;
    }

}
