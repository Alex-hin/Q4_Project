package game;
import java.awt.*;

public class Tile extends Sprite {
    private String letter;
    private int value;
    
    public Tile(String letter, int value, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.letter = letter;
        this.value = value;
    }

    @Override
    public void draw(Graphics g) {
        g.drawRect(x, y, width, height);
        g.drawString(letter + "", x + width/2, y + height/2);
    }
}
