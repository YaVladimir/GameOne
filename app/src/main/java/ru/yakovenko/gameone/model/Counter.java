package ru.yakovenko.gameone.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Counter {
    private int mKills;
    private int mHealth;
    private Paint mKillsPaint;
    private Paint mHealthPaint;

    public Counter(int kills, int health) {
        this.mKills = kills;
        this.mHealth = health;

        mKillsPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
        mKillsPaint.setColor(Color.RED);
        mKillsPaint.setTextSize(30);
        mHealthPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
        mHealthPaint.setColor(Color.RED);
        mHealthPaint.setTextSize(30);
    }

    public int getmKills() {
        return mKills;
    }

    public void setmKills(int mKills) {
        this.mKills = mKills;
    }

    public int getmHealth() {
        return mHealth;
    }

    public void setmHealth(int mHealth) {
        this.mHealth = mHealth;
    }

    public void incrementKills() {
        this.mKills += 1;
    }

    public void decrementHealth() {
        this.mHealth -= 1;
    }

    public void onDraw(Canvas canvas) {
        canvas.drawText("Kills: " + mKills, 15, 30, mKillsPaint);
        canvas.drawText("Health: " + mHealth,
                canvas.getWidth() - 150, 30, mHealthPaint);
    }
}
