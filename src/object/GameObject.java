package object;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static main.Game.UPS;
import static utilz.Constant.ANIMATION_PER_SECOND;
import static utilz.Constant.ObjectConstant.*;

public class GameObject {
    protected int x,y, objectType;
    protected Rectangle2D. Float hitbox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIndex;
    protected int xDrawOffSet, yDrawOffSet;
    public GameObject (int x, int y, int objectType){
        this.x=x;
        this.y =y;
        this.objectType = objectType;
    }
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= UPS / ANIMATION_PER_SECOND) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(objectType)) {
                aniIndex = 0;
                if (objectType == BARREL||objectType == BOX){
                    doAnimation = false;
                    active = false;
                }
            }
        }
    }
    public void reset(){
        aniIndex =0;
        aniTick =0;
        active = true;
        if (objectType == BARREL||objectType == BOX){
            doAnimation = false;
        }
        else{
        doAnimation = true;}
    }
    protected void initHitbox (int width, int height){
        hitbox = new Rectangle2D.Float(x,y,(int)(width* Game.SCALE), (int)(height*Game.SCALE));
    }
    public void drawHitbox (Graphics g, int xLvlOffSet){
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffSet, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }
    public int getObjectType(){
        return objectType;
    }
    public void setObjectType(int objectType){
        this.objectType = objectType;
    }
    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }
    public boolean isActive(){
        return active;
    }
    public int getxDrawOffSet(){
        return xDrawOffSet;
    }
    public int getyDrawOffSet(){
        return yDrawOffSet;
    }
    public void setActive (boolean x){
        active = x;
    }
    public int getAniIndex(){
        return aniIndex;
    }
}
