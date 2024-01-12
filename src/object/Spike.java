package object;
import main.Game;

public class Spike extends GameObject{

    public Spike(int x, int y, int objType) {
        super(x, y, objType);

        initHitbox(32, 16);
        xDrawOffSet = 0;
        yDrawOffSet = (int)(Game.SCALE * 16);
        hitbox.y += yDrawOffSet;

    }

}