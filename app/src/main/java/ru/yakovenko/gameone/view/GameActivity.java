package ru.yakovenko.gameone.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static ru.yakovenko.gameone.utils.Constants.HEALTH;
import static ru.yakovenko.gameone.utils.Constants.SPEED;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = GameView.class.getName();
    private GameView mGameView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int speed = intent.getIntExtra(SPEED, 10);
        int health = intent.getIntExtra(HEALTH, 10);
        mGameView = new GameView(this, speed, health);
        setContentView(mGameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        mGameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        mGameView.resume();
    }
}
;