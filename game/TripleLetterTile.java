package game;

import java.awt.*;

public class TripleLetterTile extends Tile {
    public TripleLetterTile(int x, int y, int width, int height) {
        super("", 0, x, y, width, height);
    }

    @Override
    public void draw(Graphics g) {
        // Blue background for Triple Letter tiles
        g.setColor(new Color(100, 180, 255)); // Light blue color as in the image
        g.fillRect(x, y, width, height);
        
        // Draw border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        
        // Draw text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString("TL", x + 6, y + 25);
    }
}