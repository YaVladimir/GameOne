package ru.yakovenko.gameone.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import ru.yakovenko.gameone.R;

import static ru.yakovenko.gameone.utils.Constants.HEALTH;
import static ru.yakovenko.gameone.utils.Constants.SPEED;

public class MainActivity extends AppCompatActivity {
    private int mSpeed;
    private int mHealth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio1:
                        mSpeed = 5;
                        mHealth = 20;
                        break;
                    case R.id.radio2:
                        mSpeed = 15;
                        mHealth = 10;
                        break;
                    default:
                        break;
                }
            }
        });

        final Button btnNewGame = findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() != -1 && mSpeed != 0 && mHealth != 0) {

                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putExtra(HEALTH, mHealth);
                    intent.putExtra(SPEED, mSpeed);

                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Необходимо выбрвть сложность",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
