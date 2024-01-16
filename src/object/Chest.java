package object;

import main.Game;

public class Chest extends GameObject {
    public Chest(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitbox(30, 24);
        xDrawOffSet = (int) (1 * Game.SCALE);
        yDrawOffSet = (int) (8 * Game.SCALE);
        hitbox.x += xDrawOffSet;
        hitbox.y += yDrawOffSet;
    }

    public void update() {
        updateAnimationTick();
    }
}
