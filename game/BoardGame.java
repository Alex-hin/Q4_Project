package game;

import java.util.*;
import java.awt.Color;
import java.awt.Graphics;


// import for playing sound
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;




public class BoardGame{


	// Instance variables
    private ArrayList<Tile> bank;
	private ArrayList<Tile> p1Tiles;
	public BoardGame(){
		p1Tiles = new ArrayList<Tile>();
        bank = new ArrayList<Tile>();

        readBank();

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

    private void readBank(){
        String filePath = "game/letterBank";

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = br.readLine()) != null) {
                bank.add(new Tile(line.substring(0,1), Integer.parseInt(line.substring(2)), 0, 0, 40, 40));
            }

        } catch (IOException e){
            System.err.println("Error reading file: " + e.getMessage());
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
