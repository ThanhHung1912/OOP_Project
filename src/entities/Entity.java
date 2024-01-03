package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitBox;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitBox(float x, float y, float width, float height) {
        hitBox = new Rectangle2D.Float(x, y, width, height);
    }
    /*protected void updateHitBox() {
        hitBox.x = (int) x;
        hitBox.y = (int) y;
    }*/
    protected void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitBox.x - xLvlOffset , (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }
    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }
}
