package object;

import main.Game;
import static utilz.Constant.ObjectConstant.*;

import static utilz.Constant.ObjectConstant.TREASURE_CHEST;

public class Chest extends GameObject {
    private boolean isUnlocked;

    public Chest(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitbox(30, 24);
        xDrawOffSet = (int) (1 * Game.SCALE);
        yDrawOffSet = (int) (8 * Game.SCALE);
        hitbox.x += xDrawOffSet;
        hitbox.y += yDrawOffSet;
    }

    public void update() {
        if (doAnimation) {
            updateAnimationTick();
            if (aniIndex == GetSpriteAmount(TREASURE_CHEST) - 1) {
                isUnlocked = true;
            }
        }
    }
    public void setUnlocked(boolean isUnlocked) {
        this.isUnlocked = isUnlocked;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }
}
