package ru.yakovenko.gameone.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

import ru.yakovenko.gameone.view.GameView;

public class Enemy {
    private static final String TAG = Enemy.class.getName();
    private int mX;
    private int mY;
    private int mSpeed;
    private int mWidth;
    private int mHeight;

    private GameView mGameView;
    private Bitmap mBitmap;

    public Enemy(GameView gameView, Bitmap bitmap, int speed) {
        this.mGameView = gameView;
        this.mBitmap = bitmap;

        Random random = new Random();
        this.mWidth = bitmap.getWidth();
        this.mHeight = bitmap.getHeight();
        this.mX = gameView.getWidth();
        this.mY = random.nextInt(gameView.getHeight() - mHeight);
        this.mSpeed = speed / 2 + random.nextInt(speed);

        Log.d(TAG, String.format("Create Enemy: mX = %s, xY = %s, mSpeed = %s", mX, mY, mSpeed));
    }

    private void update() {
        mX -= mSpeed;
    }

    public void onDraw(Canvas canvas) {
        update();
        canvas.drawBitmap(mBitmap, mX, mY, null);
    }

    public int getmWidth() {
        return mWidth;
    }

    public int getmHeight() {
        return mHeight;
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
