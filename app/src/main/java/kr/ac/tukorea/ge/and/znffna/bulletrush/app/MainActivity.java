package kr.ac.tukorea.ge.and.znffna.bulletrush.app;

import static kr.ac.tukorea.ge.and.znffna.bulletrush.game.MainScene.Layer.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;
import kr.ac.tukorea.ge.and.znffna.bulletrush.databinding.ActivityMainBinding;
import kr.ac.tukorea.ge.and.znffna.bulletrush.game.Gun;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.Sound;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding ui;
    private int cookieIndex;
    private BitmapFactory.Options opts;

    protected MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());

        setWeaponIndex(0);
    }

    public void onBtnStartGame(View view) {
        Intent intent = new Intent(this, BulletRushActivity.class);
        intent.putExtra(BulletRushActivity.KEY_WEAPON_ID, cookieIndex);
        startActivity(intent);
    }

    private void setWeaponIndex(int index){
        this.cookieIndex = index;

        int weaponResID = Gun.resIds[index];
        if (opts == null) {
            opts = new BitmapFactory.Options();
            opts.inScaled = false;
        }

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), weaponResID, opts);
        ui.weaponImageView.setImageBitmap(bmp);

        ui.prevWeaponButton.setEnabled(index > 0);
        ui.nextWeaponButton.setEnabled(index < Gun.resIds.length - 1);
    }

    public void onBtnPreviousWeapon(View view) {
        setWeaponIndex(cookieIndex - 1);
    }

    public void onBtnNextWeapon(View view) {
        setWeaponIndex(cookieIndex + 1);
    }

    @Override
    protected void onStart() {
        mediaPlayer = MediaPlayer.create(this, R.raw.title_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        super.onStart();
    }

    @Override
    protected void onResume() {
        mediaPlayer.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mediaPlayer.stop();
        mediaPlayer = null;
        super.onStop();
    }

}