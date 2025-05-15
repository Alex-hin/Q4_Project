package game;

import java.awt.*;

public class DoubleLetterTile extends Tile {
    public DoubleLetterTile(int x, int y, int width, int height) {
        super("", 0, x, y, width, height);
    }

    @Override
    public void draw(Graphics g) {
        // Light Blue background for Double Letter tiles
        g.setColor(new Color(200, 220, 255)); // Light blue-gray color as in the image
        g.fillRect(x, y, width, height);
        
        // Draw border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        
        // Draw text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString("DL", x + 5, y + 25);
    }
}