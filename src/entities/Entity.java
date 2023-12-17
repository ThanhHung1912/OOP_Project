package entities;

public abstract class Entity {
    protected double x, y;
    protected int width, height;

    public Entity(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


}
