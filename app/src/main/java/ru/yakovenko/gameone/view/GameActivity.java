package ru.yakovenko.gameone.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import static ru.yakovenko.gameone.utils.Constants.HEALTH;
import static ru.yakovenko.gameone.utils.Constants.SPEED;

public class GameActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int speed = intent.getIntExtra(SPEED, 10);
        int health = intent.getIntExtra(HEALTH, 10);

        setContentView(new GameView(this, speed, health));
    }

}
