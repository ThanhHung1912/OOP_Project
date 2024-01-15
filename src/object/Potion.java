package object;

import main.Game;

public class Potion extends GameObject{
    private float hoverOffset;
    private int maxHoverOffset, hoverDir = 1;
    public Potion(int x, int y, int objectType) {
        super((int) (x + 10 * Game.SCALE), (int) (y + 10 * Game.SCALE), objectType);
        doAnimation = true;
        initHitbox(7, 14);
        xDrawOffSet = (int)(3 * Game.SCALE);
        yDrawOffSet = (int)(2 * Game.SCALE);
        maxHoverOffset = (int)(10* Game.SCALE);
    }
    public void update(){
        updateAnimationTick();
        updateHover();
    }

    private void updateHover() {
        hoverOffset += (0.075f * Game.SCALE * hoverDir);
        if (hoverOffset >= maxHoverOffset){
            hoverOffset = -1;
        }
        else if(hoverOffset <0){
            hoverDir = 1;
        }
        hitbox.y = y + hoverOffset;
    }
}
