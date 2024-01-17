package object;

import main.Game;

public class Key extends GameObject{
    public Key(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitbox(8,15);
        doAnimation = true;
        xDrawOffSet = (int) (8 * Game.SCALE);
        yDrawOffSet = (int) (5 * Game.SCALE);
        hitbox.x += 12 * Game.SCALE;
        hitbox.y += 12 * Game.SCALE;
    }
    public void update() {
        if (doAnimation)
            updateAnimationTick();
    }
}
