package object;

import java.awt.geom.Rectangle2D;

import main.Game;

import static utilz.Constant.Projectiles.*;

public class Projectile extends GameObject {
    private int dir;

    public Projectile(int x, int y, int objectType, int dir) {
        super(x, y, objectType);
        int xOffset = (int) (-3 * Game.SCALE);
        int yOffset = (int) (5 * Game.SCALE);

        if (dir == 1)
            xOffset = (int) (29 * Game.SCALE);
        initHitbox(CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        hitbox.x += xOffset;
        hitbox.y += yOffset;
        this.dir = dir;
    }

    public void updatePos() {
        hitbox.x += dir * SPEED;
    }

    public void setPos(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
    }
}