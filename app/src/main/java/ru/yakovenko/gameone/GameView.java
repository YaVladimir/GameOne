package ru.yakovenko.gameone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import ru.yakovenko.gameone.model.Bullet;
import ru.yakovenko.gameone.model.Enemy;

public class GameView extends SurfaceView implements Runnable {
    private static final String TAG = GameView.class.getName();
    public int shotX;
    public int shotY;
    private GameThread mThread;
    private boolean mRunning = false;
    private CopyOnWriteArrayList<Bullet> mBullets = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Enemy> mEnemies = new CopyOnWriteArrayList<>();
    private Bitmap mNinjaBmp;
    private Bitmap mEnemyBmp;
    private Thread thread = new Thread(this);

    public GameView(Context context) {
        super(context);
        this.mThread = new GameThread(this);
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mThread.setRunning(true);
                mThread.start();
                thread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mThread.setRunning(false);
                boolean retry = true;
                while (retry) {
                    try {
                        mThread.join();
                        thread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                        Log.e(TAG, "Couldn't join thread: {}", e);
                    }
                }
            }
        });
        mNinjaBmp = BitmapFactory.decodeResource(getResources(), R.drawable.nindja);
        mEnemyBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(mNinjaBmp, 5, canvas.getHeight() / 2, null);

        for (Bullet bullet : mBullets) {
            if (bullet.getmX() <= canvas.getWidth()) {
                bullet.onDraw(canvas);
            } else {
                boolean remove = mBullets.remove(bullet);
                Log.d(TAG, String.format("Remove bullet %s - %s", bullet, remove));
            }
        }

        for (Enemy enemy : mEnemies) {
            if (enemy.getmX() >= 0) {
                enemy.onDraw(canvas);
            } else {
                boolean remove = mEnemies.remove(enemy);
                Log.d(TAG, String.format("Remove enemy %s - %s", enemy, remove));
            }
        }
    }

    private Bullet createSprite(int resource) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resource);
        return new Bullet(this, bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        shotX = (int) event.getX();
        shotY = (int) event.getY();

        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            mBullets.add(createSprite(R.drawable.ammo));
        }
        return true;
    }

    @Override
    public void run() {
        while (true) {
            Random random = new Random();
            try {
                Thread.sleep(random.nextInt(2000));
                mEnemies.add(new Enemy(this, mEnemyBmp));
            } catch (InterruptedException e) {
                Log.e(TAG, "Couldn't sleep thread", e);
            }
        }
    }

    private void checkCollision() {
        for (Bullet bullet : mBullets) {
            for (Enemy enemy : mEnemies) {
                if (Utils.isCollision(bullet, enemy)) {
                    mEnemies.remove(enemy);
                    mBullets.remove(bullet);
                }
            }
        }
    }

    private class GameThread extends Thread {
        private GameView gameView;

        private Canvas canvas;

        GameThread(GameView gameView) {
            this.gameView = gameView;
        }

        public void setRunning(boolean run) {
            mRunning = run;
        }

        @SuppressLint("WrongCall")
        @Override
        public void run() {
            while (mRunning) {
                canvas = gameView.getHolder().lockCanvas();
                synchronized (gameView.getHolder()) {
                    gameView.onDraw(canvas);
                    checkCollision();
                }
                gameView.getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }
}
