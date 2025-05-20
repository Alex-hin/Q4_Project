package game;

import java.awt.*;

public class Confetti {
    private static final int MAX_CONFETTI = 150;
    private int[] x;
    private int[] y;
    private int[] speed;
    private int[] size;
    private Color[] colors;
    private int[] rotation;
    private int[] rotationSpeed;
    private int width;
    private int height;
    private boolean active;

    public Confetti(int screenWidth, int screenHeight) {
        this.width = screenWidth;
        this.height = screenHeight;
        this.active = false;
        
        x = new int[MAX_CONFETTI];
        y = new int[MAX_CONFETTI];
        speed = new int[MAX_CONFETTI];
        size = new int[MAX_CONFETTI];
        colors = new Color[MAX_CONFETTI];
        rotation = new int[MAX_CONFETTI];
        rotationSpeed = new int[MAX_CONFETTI];
    }
    
    public void start() {
        active = true;
        resetConfetti();
    }
    
    public void stop() {
        active = false;
    }
    
    public boolean isActive() {
        return active;
    }
    
    private void resetConfetti() {
        // Initialize confetti pieces with random properties
        for (int i = 0; i < MAX_CONFETTI; i++) {
            x[i] = (int)(Math.random() * width);
            y[i] = -((int)(Math.random() * height)); // Start above screen
            speed[i] = 2 + (int)(Math.random() * 5);
            size[i] = 5 + (int)(Math.random() * 10);
            rotation[i] = (int)(Math.random() * 360);
            rotationSpeed[i] = -3 + (int)(Math.random() * 7);
            
            // Create random festive colors
            switch ((int)(Math.random() * 6)) {
                case 0:
                    colors[i] = Color.RED;
                    break;
                case 1:
                    colors[i] = Color.BLUE;
                    break;
                case 2:
                    colors[i] = Color.GREEN;
                    break;
                case 3:
                    colors[i] = Color.YELLOW;
                    break;
                case 4:
                    colors[i] = Color.ORANGE;
                    break;
                default:
                    colors[i] = Color.MAGENTA;
                    break;
            }
        }
    }
    
    public void update() {
        if (!active) return;
        
        boolean allOffScreen = true;
        
        for (int i = 0; i < MAX_CONFETTI; i++) {
            // Update position
            y[i] += speed[i];
            
            // Update rotation
            rotation[i] += rotationSpeed[i];
            if (rotation[i] > 360) rotation[i] -= 360;
            if (rotation[i] < 0) rotation[i] += 360;
            
            // Check if any confetti is still on screen
            if (y[i] < height) {
                allOffScreen = false;
            }
            
            // Reset confetti that's gone off screen
            if (y[i] > height + 50) {
            }
        }
        
        // Stop animation if all confetti has fallen off screen
        if (allOffScreen) {
            active = false;
        }
    }
    
    public void draw(Graphics g) {
        if (!active) return;
        
        Graphics2D g2d = (Graphics2D) g;
        
        for (int i = 0; i < MAX_CONFETTI; i++) {
            if (y[i] < height && y[i] > -size[i]) {
                g.setColor(colors[i]);
                
                // Save original state
                Color origColor = g2d.getColor();
                
                // Draw confetti piece
                drawRotatedRectangle(g, x[i], y[i], size[i], size[i] * 2, rotation[i], colors[i]);
                
                // Restore original state
                g2d.setColor(origColor);
            }
        }
    }
    
    private void drawRotatedRectangle(Graphics g, int x, int y, int width, int height, int angle, Color color) {
        // Using Math trigonometry
        double radians = Math.toRadians(angle);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        
        // Calculate the four corners of the rectangle
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        
        // Center of rotation
        int centerX = x + width/2;
        int centerY = y + height/2;
        
        // Top-left
        xPoints[0] = (int)(centerX + (-width/2 * cos - -height/2 * sin));
        yPoints[0] = (int)(centerY + (-width/2 * sin + -height/2 * cos));
        
        // Top-right
        xPoints[1] = (int)(centerX + (width/2 * cos - -height/2 * sin));
        yPoints[1] = (int)(centerY + (width/2 * sin + -height/2 * cos));
        
        // Bottom-right
        xPoints[2] = (int)(centerX + (width/2 * cos - height/2 * sin));
        yPoints[2] = (int)(centerY + (width/2 * sin + height/2 * cos));
        
        // Bottom-left
        xPoints[3] = (int)(centerX + (-width/2 * cos - height/2 * sin));
        yPoints[3] = (int)(centerY + (-width/2 * sin + height/2 * cos));
        
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, 4);
    }
}