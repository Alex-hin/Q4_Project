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
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.drawString(letter, x + width / 2 - 5, y + height / 2 + 5);
        // Add value display in the bottom right corner
        g.setFont(new Font("Arial", Font.PLAIN, 8));
        g.drawString(String.valueOf(value), x + width - 10, y + height - 2);
    }

    public void drawPTiles(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        // Draw the letter
        g.setFont(new Font("Arial", Font.BOLD, 18));  
        g.drawString(letter, x + width / 2 - 5, y + height / 2 + 6);
        // Draw the value in small text in bottom right
        g.setFont(new Font("Arial", Font.PLAIN, 8));
        g.drawString(String.valueOf(value), x + width - 10, y + height - 2);
    }

    public void setLoc(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setLoc(int x){
        this.x = x;
    }

    public String toString(){
        return letter + "";
    }

    public int getXPos(){
        return x;
    }

    public String getLetter(){
        return letter;
    }
    
    public int getValue() {
        return value;
    }
}