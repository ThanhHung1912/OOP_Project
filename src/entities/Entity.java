package entities;

import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static main.Game.SCALE;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitBox;
    protected Rectangle2D.Float attackBox;

    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected float walkSpeed;
    protected int aniTick = 0, aniIndex = 0;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, (int) (width * SCALE), (int) (height * SCALE));
    }
    protected void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitBox.x - xLvlOffset , (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }
    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int)attackBox.x - xLvlOffset,(int) attackBox.y,(int) attackBox.width,(int) attackBox.height);
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }
    public int getState() {
        return state;
    }
    public int getAniIndex() {
        return aniIndex;
    }
}
