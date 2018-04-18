package ru.yakovenko.gameone;

import ru.yakovenko.gameone.model.Bullet;
import ru.yakovenko.gameone.model.Enemy;

public class Utils {
    public static boolean isCollision(Bullet bullet, Enemy enemy) {
        return ((Math.abs(bullet.getmX() - enemy.getmX()))
                <= (bullet.getmWidth() + enemy.getmWidth()) / 2f)
                && (Math.abs(bullet.getmY() - enemy.getmY())
                <= (bullet.getmHeight() + enemy.getmHeight()) / 2f);
    }
}
