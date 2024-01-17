package object;

import main.Game;

import static utilz.Constant.GRAVITY;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.IsEntityOnFloor;

public class Padlock extends GameObject{
    private float ySpeed = 0;
    public Padlock(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitbox(14, 13);
        xDrawOffSet = (int) (9 * Game.SCALE);
        yDrawOffSet = (int) (9 * Game.SCALE);
    }
    public void update(int[][] lvlData) {
        updatePos(lvlData);
    }

    private void updatePos(int[][] lvlData) {
        ySpeed += GRAVITY;
        if (!IsEntityOnFloor(hitbox, lvlData)) {
            hitbox.y += ySpeed;
        }
    }
}
