package ru.yakovenko.gameone;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bullet {
    private Bitmap mBitmap;

    private int mX;
    private int mY;
    private int mSpeed;
    private int mWidth;
    private int mHeight;
    private double mAngle;

    private GameView mGameView;

    public Bullet(GameView gameView, Bitmap bitmap) {
        this.mGameView = gameView;
        this.mBitmap = bitmap;

        this.mX = 0;
        this.mY = 120;

        this.mWidth = 27;
        this.mHeight = 40;
        this.mSpeed = 25;

        mAngle = Math.atan((double) (mY - gameView.shotY) / (mX - gameView.shotX));
    }

    private void update() {
        mX += mSpeed * Math.cos(mAngle);
        mY += mSpeed * Math.sin(mAngle);
    }

    public void onDraw(Canvas canvas) {
        update();
        canvas.drawBitmap(mBitmap, mX, mY, null);
    }

    public int getmX() {
        return mX;
    }

    public void setmX(int mX) {
        this.mX = mX;
    }

    public int getmY() {
        return mY;
    }

    public void setmY(int mY) {
        this.mY = mY;
    }
}
