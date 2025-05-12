package game;

public class Sprite {
    protected int x, y; // position on screen
    protected int width, height;

    public Sprite(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        // Default draw method (can be overridden)
    }

    public void moveTo(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
}
