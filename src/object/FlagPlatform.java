package object;

import main.Game;

public class FlagPlatform extends GameObject{
    public FlagPlatform(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitbox(30, 16);
        hitbox.x += 1 * Game.SCALE;
        hitbox.y += 16 * Game.SCALE;
    }
}
