package ru.yakovenko.gameone.utils;

import ru.yakovenko.gameone.model.Bullet;
import ru.yakovenko.gameone.model.Enemy;

public class Utils {
    /**
     * Проверяет, пересеклись ли снаряд и враг
     *
     * @param bullet снаряд
     * @param enemy  враг
     * @return true, если снаряд попал во врага
     */
    public static boolean isCollision(Bullet bullet, Enemy enemy) {
        return ((Math.abs(bullet.getmX() - enemy.getmX()))
                <= (bullet.getmWidth() + enemy.getmWidth()) / 2f)
                && (Math.abs(bullet.getmY() - enemy.getmY())
                <= (bullet.getmHeight() + enemy.getmHeight()) / 2f);
    }
}
