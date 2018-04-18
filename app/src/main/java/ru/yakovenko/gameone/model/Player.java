package ru.yakovenko.gameone.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import ru.yakovenko.gameone.view.GameView;

public class Player {
    private static final String TAG = Player.class.getName();
    private GameView gameView;
    private Bitmap mBitmap;
    private int mX;
    private int mY;

    public Player(GameView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.mBitmap = bmp;

        this.mX = 20;
        this.mY = gameView.getHeight() / 2;
        Log.d(TAG, "mY = " + mY);
    }

    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mX, mY, null);
    }
}
