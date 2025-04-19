package kr.ac.tukorea.ge.and.znffna.bulletrush.app;

import android.os.Bundle;
import android.view.View;

import kr.ac.tukorea.ge.and.znffna.bulletrush.BuildConfig;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class BulletRushActivity extends GameActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameView.drawsDebugStuffs = BuildConfig.DEBUG;
        super.onCreate(savedInstanceState);
        new MainScene().push();
    }

    public void setFullScreen() {
        int flags = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        gameView.setSystemUiVisibility(flags);
    }
}