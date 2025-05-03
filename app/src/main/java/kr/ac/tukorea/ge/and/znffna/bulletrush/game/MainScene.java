package kr.ac.tukorea.ge.and.znffna.bulletrush.game;

import android.view.MotionEvent;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.JoyStick;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;

public class MainScene extends Scene {

    private final Player player;
    private final JoyStick joyStick;

    public MainScene() {
        joyStick = new JoyStick(R.mipmap.joystick_bg, R.mipmap.joystick_thumb, 100, 1500, 100, 30, 80);
        player = new Player(joyStick);
        Enemy.setTarget(player);

        add(player);
        add(joyStick);

        add(new Bullet(0));
//        add(new Enemy(0, 100, 100));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return joyStick.onTouch(event);
    }
}
