package object;

import main.Game;

import static utilz.Constant.ObjectConstant.*;

public class GameContainer extends GameObject{

    public GameContainer(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitbox();
    }

    private void createHitbox() {
        if (objectType == BOX){
            initHitbox(25, 18);
            xDrawOffSet = (int)(7* Game.SCALE);
            yDrawOffSet = (int)(12* Game.SCALE);
        }
        else{
            initHitbox(25, 25);
            xDrawOffSet = (int)(8* Game.SCALE);
            yDrawOffSet = (int)(5* Game.SCALE);
        }
        hitbox.y += yDrawOffSet + (int)(Game.SCALE *2);
        hitbox.x += xDrawOffSet /2;
    }

    public void update(){
        if (doAnimation){
            updateAnimationTick();
        }
    }
}
