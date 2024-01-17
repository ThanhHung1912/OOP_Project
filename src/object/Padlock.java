package object;

import main.Game;

import static utilz.Constant.GRAVITY;
import static utilz.HelpMethods.*;

public class Padlock extends GameObject{
    private float ySpeed = 0.5f;
    private float xSpeed = 0.75f * Game.SCALE;
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
        ySpeed += (float) (GRAVITY * 0.2);
        xSpeed -= 0.02f * Game.SCALE;
        if (!IsEntityOnFloor(hitbox, lvlData)) {
            if (CanMoveHere(hitbox.x, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += ySpeed;
                if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
                    hitbox.x += xSpeed;
                else {
                    hitbox.x = GetCollisionGround(hitbox, xSpeed);
                }
            }
            else {
                hitbox.y = GetCollisionRoof(hitbox, ySpeed);
            }
        }
    }
}
