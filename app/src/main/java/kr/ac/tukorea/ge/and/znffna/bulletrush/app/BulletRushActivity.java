package kr.ac.tukorea.ge.and.znffna.bulletrush.app;

import android.os.Bundle;

import kr.ac.tukorea.ge.and.znffna.bulletrush.BuildConfig;
import kr.ac.tukorea.ge.and.znffna.bulletrush.game.MainScene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class BulletRushActivity extends GameActivity {
    public static final String KEY_WEAPON_ID = "weaponID";
    private GameView gameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameView.drawsDebugStuffs = BuildConfig.DEBUG;
        int weaponId = getIntent().getIntExtra(KEY_WEAPON_ID, 0);
        super.onCreate(savedInstanceState);
        new MainScene(weaponId).push();
    }
}