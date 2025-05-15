package game;

import java.awt.*;

public class TripleWordTile extends Tile {
    public TripleWordTile(int x, int y, int width, int height) {
        super("", 0, x, y, width, height);
    }

    @Override
    public void draw(Graphics g) {
        // Red background for Triple Word tiles
        g.setColor(new Color(220, 0, 0));
        g.fillRect(x, y, width, height);
        
        // Draw border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        
        // Draw text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString("TW", x + 4, y + 25);
    }
}