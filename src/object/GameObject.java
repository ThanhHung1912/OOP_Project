package object;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constant.ObjectConstant.*;
import static utilz.Constant.TICKS_PER_ANI;

public class GameObject {
    protected int x,y, objectType;
    protected Rectangle2D. Float hitbox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIndex;
    protected int xDrawOffSet, yDrawOffSet;
    public GameObject (int x, int y, int objectType){
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= TICKS_PER_ANI) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(objectType)) {
                if (objectType == BARREL || objectType == BOX){
                    doAnimation = false;
                    active = false;
                    aniIndex = 0;
                }
                else if (objectType == CANNON_LEFT || objectType == CANNON_RIGHT) {
                    doAnimation = false;
                    aniIndex = 0;
                }
                else if (objectType == TREASURE_CHEST) {
                    doAnimation = false;
                    aniIndex--;
                }
                else if (objectType == BLUE_POTION || objectType == RED_POTION || objectType == KEY) {
                    aniIndex = 0;
                }
            }
        }
    }
    public void reset(){
        aniIndex = 0;
        aniTick = 0;
        active = true;
        if (objectType == BARREL||objectType == BOX || objectType == CANNON_LEFT || objectType == CANNON_RIGHT || objectType == TREASURE_CHEST){
            doAnimation = false;
        }
        else{
        doAnimation = true;}
    }
    protected void initHitbox (int width, int height){
        hitbox = new Rectangle2D.Float(x, y, (int)(width* Game.SCALE), (int)(height*Game.SCALE));
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
    public int getAniTick() {
        return aniTick;
    }
    public void setAnimation (boolean a){
        this.doAnimation = a;
    }}
