package ru.yakovenko.gameone.view;

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

import ru.yakovenko.gameone.R;
import ru.yakovenko.gameone.model.Bullet;
import ru.yakovenko.gameone.model.Counter;
import ru.yakovenko.gameone.model.Enemy;
import ru.yakovenko.gameone.utils.Utils;

public class GameView extends SurfaceView implements Runnable {
    private static final String TAG = GameView.class.getName();
    public int shotX;
    public int shotY;
    private int mSpeed;
    private GameThread mThread;
    private boolean mRunning = false;
    private CopyOnWriteArrayList<Bullet> mBullets = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Enemy> mEnemies = new CopyOnWriteArrayList<>();
    private Bitmap mNinjaBmp;
    private Bitmap mEnemyBmp;
    private Thread thread = new Thread(this);
    private boolean mIsRunning;
    private Counter mCounter;

    public GameView(Context context, int speed, int health) {
        super(context);
        this.mSpeed = speed;
        this.mThread = new GameThread(this);
        this.mCounter = new Counter(0, health);
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mThread.setRunning(true);
                mIsRunning = true;
                mThread.start();
                thread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mThread.setRunning(false);
                mIsRunning = false;
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
        mCounter.onDraw(canvas);
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
                mCounter.decrementHealth();
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
        while (mIsRunning) {
            Random random = new Random();
            try {
                if (!thread.isInterrupted()) {
                    Thread.sleep(random.nextInt(2000));
                    mEnemies.add(new Enemy(this, mEnemyBmp, mSpeed));
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "Couldn't sleep thread", e);
            }
        }
    }

    public void pause() {
        mIsRunning = false;
        mRunning = false;
        Log.v(TAG, "pause");
        while (true) {
            try {
                thread.join();
                mThread.join();
            } catch (InterruptedException e) {
                Log.v("pause()", e.toString());
            }
        }
    }

    public void resume() {
        mIsRunning = true;
        mRunning = true;

        if (thread == null && mThread == null) {
            thread = new Thread(this);
            this.mThread = new GameThread(this);
            Log.v("resume()", "new thread started");
        } else {
            Log.v("resume()", "new thread not started as t!=null");
        }
    }

    private void checkCollision() {
        for (Bullet bullet : mBullets) {
            for (Enemy enemy : mEnemies) {
                if (Utils.isCollision(bullet, enemy)) {
                    mEnemies.remove(enemy);
                    mBullets.remove(bullet);
                    mCounter.incrementKills();
                }
            }
        }
    }

    /**
     * Проверяет количество оставшихся жизней, если их количество <= 0,
     * прекращает отрисовывать Canvas
     */
    private void checkHealth() {
        if (mCounter.getmHealth() <= 0) {
            mThread.setRunning(false);
            mIsRunning = false;
            finish();
        }
    }

    private void finish() {
        /*final Context context = this.getContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getText(R.string.lose_dialog_title));
        View view = inflate(context, R.layout.finish, null);
        builder.setView(view);
        final Button btnFinish = view.findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btnFinish.getId()) {
                    ((Activity) context).finish();
                }
            }
        });
        AlertDialog finishDialog = builder.create();
        finishDialog.show();*/
        /*Toast.makeText(context,
                "Ты проиграл, лошара. Твой финальный счет: " + mCounter.getmKills(),
                Toast.LENGTH_LONG).show();*/
        forceLayout();
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
            while (mRunning && !thread.isInterrupted() && !mThread.isInterrupted()) {
                canvas = gameView.getHolder().lockCanvas();
                if (canvas != null) {
                    synchronized (gameView.getHolder()) {
                        gameView.onDraw(canvas);
                        checkCollision();
                        checkHealth();
                    }
                    gameView.getHolder().unlockCanvasAndPost(canvas);
                }
            }
        }
    }

}
