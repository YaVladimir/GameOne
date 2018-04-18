package ru.yakovenko.gameone.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ru.yakovenko.gameone.GameView;

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

        this.mX = 5;
        this.mY = gameView.getHeight() / 2;

        this.mWidth = bitmap.getWidth();
        this.mHeight = bitmap.getHeight();
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

    public int getmSpeed() {
        return mSpeed;
    }

    public int getmWidth() {
        return mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }

    public double getmAngle() {
        return mAngle;
    }
}
