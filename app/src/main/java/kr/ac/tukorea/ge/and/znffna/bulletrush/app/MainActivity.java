package kr.ac.tukorea.ge.and.znffna.bulletrush.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.tukorea.ge.and.znffna.bulletrush.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            startActivity(new Intent(this, BulletRushActivity.class));
//        }
//        return super.onTouchEvent(event);
//    }

    public void onBtnStartGame(View view) {
        startActivity(new Intent(this, BulletRushActivity.class));
    }
}