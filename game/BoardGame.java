package game;

import java.util.*;
import java.awt.*;

// import for playing sound
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;




public class BoardGame{


	// Instance variables
    private ArrayList<Tile> bank, p1Tiles, currentPlayer, oldTiles;
    private ArrayList<String> allWords;
    private Tile[][] board;
    private int[][] newTiles;
    private static int counter;

	
    
    public BoardGame(){
		p1Tiles = new ArrayList<Tile>();
        bank = new ArrayList<Tile>();
        allWords = new ArrayList<String>();
        currentPlayer = p1Tiles;
        oldTiles = currentPlayer;
        board = new Tile[15][15];
        newTiles = new int[2][7];

        readBank();
        readWords();
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

    private void readWords(){
        String filePath = "game/words.txt";

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = br.readLine()) != null) {
                allWords.add(line);
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

        drawTilesBoard(g);
        drawPlayerTiles(g);


    }

    public void drawPlayerTiles(Graphics g) {
        for(int i = 0; i < currentPlayer.size(); i++){
            currentPlayer.get(i).setLoc(100 + 40 * i);
            currentPlayer.get(i).drawPTiles(g);
        }
    }

    public void drawStartScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 800);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));  
        g.drawString("Scrabble", 270, 200); 

        g.setFont(new Font("Arial", Font.PLAIN, 24));  
        g.drawString("Click the button to start the game", 220, 280);
    }


    public int getNumTiles(){
        return currentPlayer.size();
    }

    public Tile getTile(int index){
        return currentPlayer.get(index);
    }

    public void playTile(int x, int y, Tile tile){
        if(board[x][y] == null){
            board[x][y] = tile;
            newTiles[0][counter] = x;
            newTiles[1][counter] = y;
            counter++;
            currentPlayer.remove(tile);
        }
    }

    private void drawTilesBoard(Graphics g){
        for(int i = 0; i < board.length; i++){
            for(int f = 0; f < board[i].length; f++){
                if(board[i][f]!= null){
                    board[i][f].setLoc(i * 40, f * 40);
                    board[i][f].drawPTiles(g);
                }
                
            }
        }
    }

    public boolean isValidPlay(){
        if(counter == 0){
            return false;
        }

        if(!isStraightLine()){
            return false;
        }

        if(!isContinuous()){
            return false;
        }




        return true;
    }

    private boolean isStraightLine(){
        int newTileX = newTiles[0][0];
        int NewTileY = newTiles[1][0];
        for(int i = 1; i < newTiles.length; i++){
            if(!(newTiles[0][i] == newTileX) || (newTiles[1][i] == NewTileY)){
                return false;
            }
        }

        return true;
    }

    private boolean isContinuous() {
        if (counter <= 1) {
            return true;
        }

        if (newTiles[0][0] == newTiles[0][1]) {
            int row = newTiles[0][0];
            int[] cols = new int[counter];
            for (int i = 0; i < counter; i++) {
                cols[i] = newTiles[1][i];
            }
            
            for (int i = 0; i < counter - 1; i++) {
                for (int j = 0; j < counter - i - 1; j++) {
                    if (cols[j] > cols[j + 1]) {
                        int temp = cols[j];
                        cols[j] = cols[j + 1];
                        cols[j + 1] = temp;
                    }
                }
            }

            for (int i = 0; i < counter - 1; i++) {
                if (cols[i + 1] - cols[i] > 1) {
                    for (int j = cols[i] + 1; j < cols[i + 1]; j++) {
                        if (board[row][j] == null) {
                            return false;
                        }
                    }
                }
            }
        } else if (newTiles[1][0] == newTiles[1][1]) {
            int col = newTiles[1][0];
            int[] rows = new int[counter];
            for (int i = 0; i < counter; i++) {
                rows[i] = newTiles[0][i];
            }
            for (int i = 0; i < counter - 1; i++) {
                for (int j = 0; j < counter - i - 1; j++) {
                    if (rows[j] > rows[j + 1]) {
                        int temp = rows[j];
                        rows[j] = rows[j + 1];
                        rows[j + 1] = temp;
                    }
                }
            }

            for (int i = 0; i < counter - 1; i++) {
                if (rows[i + 1] - rows[i] > 1) {
                    for (int j = rows[i] + 1; j < rows[i + 1]; j++) {
                        if (board[j][col] == null) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public void resetNewTileCounter() {
        counter = 0;
        newTiles = new int[2][7];
    }

    
}

