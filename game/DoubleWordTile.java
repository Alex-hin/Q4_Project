package game;

import java.awt.*;

public class DoubleWordTile extends Tile {
    public DoubleWordTile(int x, int y, int width, int height) {
        super("", 0, x, y, width, height);
    }

    @Override
    public void draw(Graphics g) {
        // Pink/Light Red background for Double Word tiles
        g.setColor(new Color(255, 200, 100)); // Gold/yellow color as in the image
        g.fillRect(x, y, width, height);
        
        // Draw border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        
        // Draw text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString("DW", x + 5, y + 25);
    }
}