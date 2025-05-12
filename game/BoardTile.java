package game;

public class BoardTile extends Sprite {
    private String multiplier; // e.g. "DL", "TL", "DW", etc.

    public BoardTile(int x, int y, int width, int height, String multiplier) {
        super(x, y, width, height);
        this.multiplier = multiplier;
    }

    @Override
    public void draw(Graphics g) {
        g.drawRect(x, y, width, height);
        g.drawString(multiplier, x + 5, y + 15);
    }
}
