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

import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameView extends SurfaceView {
    private static final String TAG = GameView.class.getName();
    public int shotX;
    public int shotY;
    private GameThread mThread;
    private boolean mRunning = false;
    private CopyOnWriteArrayList<Bullet> mBullets = new CopyOnWriteArrayList<>();
    private Player mPlayer;
    private Bitmap mNinjaBmp;

    public GameView(Context context) {
        super(context);
        this.mThread = new GameThread(this);
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mThread.setRunning(true);
                mThread.start();
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
                        retry = false;
                    } catch (InterruptedException e) {
                        Log.e(TAG, "Couldn't join thread: {}", e);
                    }
                }
            }
        });
        mNinjaBmp = BitmapFactory.decodeResource(getResources(), R.drawable.nindja);
        mPlayer = new Player(this, mNinjaBmp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        ListIterator<Bullet> listIterator = mBullets.listIterator();
        while (listIterator.hasNext()) {
            Bullet bullet = listIterator.next();
            if (bullet.getmX() <= canvas.getWidth()) {
                bullet.onDraw(canvas);
            } else {
                mBullets.remove(bullet);
            }
        }
        canvas.drawBitmap(mNinjaBmp, 5, 120, null);
    }

    private Bullet createSprite(int resource) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resource);
        return new Bullet(this, bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        shotX = (int) event.getX();
        shotY = (int) event.getY();

        //if (MotionEvent.ACTION_DOWN == event.getAction()) {
        mBullets.add(createSprite(R.drawable.ammo));
        //}
        return true;
    }

    private class GameThread extends Thread {
        private GameView gameView;

        private Canvas canvas;

        public GameThread(GameView gameView) {
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
                }
                gameView.getHolder().unlockCanvasAndPost(canvas);
            }
        }

    }
}
