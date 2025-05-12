package game;

import java.awt.Color;
import java.awt.Graphics;


// import for playing sound
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;






public class BoardGame{


	// Instance variables
	
	public BoardGame(){
		
	}


	 // Play sound. You can find free .wav files at wavsource.com.
    public void playSound() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File("").getAbsoluteFile()));
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }


    }


	
	public void drawBoard(Graphics g) {
        int rows = 15;
        int cols = 15;
        int cellSize = 40; // You can change this to resize the grid

        // Set the color for the grid lines
        g.setColor(Color.BLACK);

        // Draw horizontal lines
        for (int i = 0; i <= rows; i++) {
            g.drawLine(0, i * cellSize, cols * cellSize, i * cellSize);
        }

        // Draw vertical lines
        for (int j = 0; j <= cols; j++) {
            g.drawLine(j * cellSize, 0, j * cellSize, rows * cellSize);
        }
    }

	
}
