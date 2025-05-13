package game;

import java.util.*;
import java.awt.*;
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
        giveInitialTiles();
	}
    public void giveInitialTiles() {
        Random rand = new Random();
        for (int i = 0; i < 7 && !bank.isEmpty(); i++) {
            int index = rand.nextInt(bank.size());
            Tile drawn = bank.remove(index);
            drawn.moveTo(100 + i * 40, 700); // Moved to visible area
            p1Tiles.add(drawn);
        }

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
        String filePath = "game/letterBank.txt";

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
        int rows = 15, cols = 15, cellSize = 40;

        // Draw board grid and blank tiles
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * cellSize;
                int y = row * cellSize;
                Tile blank = new Tile("", 0, x, y, cellSize, cellSize);
                blank.draw(g);
            }
        }


    }

    public void drawPlayerTiles(Graphics g) {
        for (Tile t : p1Tiles) {
            t.drawPTiles(g);
        }
    }

    public void drawStartScreen(Graphics g) {//method for drawing the start screen
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 800, 800);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));  
            g.drawString("Scrabble", 320, 80);          
    }

}