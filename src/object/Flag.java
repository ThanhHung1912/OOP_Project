package object;

import main.Game;

import static main.Game.TILES_SIZE;

public class Flag extends GameObject{
    public Flag(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation = true;

        initHitbox(34, 93);
        hitbox.x += 14 * Game.SCALE;
        hitbox.y -= 77 * Game.SCALE;
    }
    public void update(){
        if (doAnimation) {
            updateAnimationTick();
        }
    }
}
