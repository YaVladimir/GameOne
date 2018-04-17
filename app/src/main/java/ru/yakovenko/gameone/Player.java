package ru.yakovenko.gameone;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player {
    private GameView gameView;
    private Bitmap mBitmap;
    private int mX;
    private int mY;

    public Player(GameView gameView, Bitmap bmp) {
        this.gameView = gameView;
        this.mBitmap = bmp;

        this.mX = 0;
        this.mY = gameView.getHeight() / 2;
    }

    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mX, mY, null);
    }
}
