package game;

import java.util.*;
import java.awt.*;
import java.io.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class BoardGame {

    // Instance variables
    private ArrayList<Tile> bank;
    private ArrayList<Tile>[] playerTiles; // Array of player tile lists
    private ArrayList<Integer> playerScores; // Track scores for each player
    private ArrayList<String> allWords;
    private int currentPlayerIndex; // Current player (0-3)
    private Tile[][] board;
    private int[][] newTiles;
    private static int counter;
    private static final int BOARD_SIZE = 15;
    private Tile[][] specialTiles;
    private boolean firstPlay; // Flag to check if it's the first play
    
    @SuppressWarnings("unchecked")
    public BoardGame() {
        // Initialize player tiles and scores for 4 players
        playerTiles = new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            playerTiles[i] = new ArrayList<Tile>();
        }
        playerScores = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            playerScores.add(0);
        }
        
        bank = new ArrayList<Tile>();
        allWords = new ArrayList<String>();
        
        currentPlayerIndex = 0; // Start with player 1
        board = new Tile[BOARD_SIZE][BOARD_SIZE];
        newTiles = new int[2][7];
        specialTiles = new Tile[BOARD_SIZE][BOARD_SIZE];
        counter = 0;
        firstPlay = true;

        readBank();
        readWords();
        giveInitialTiles();
        initializeSpecialTiles();
    }

    private void readBank() {
        String filePath = "game/letterBank.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                bank.add(new Tile(line.substring(0, 1), Integer.parseInt(line.substring(2)), 0, 0, 40, 40));
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private void readWords() {
        String filePath = "game/words.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                allWords.add(line);
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private void initializeSpecialTiles() {
        int cellSize = 40;
 
 
        // Triple Word Score Tiles (Red)
        // Corners and middle edges
        specialTiles[0][0] = new TripleWordTile(0, 0, cellSize, cellSize);
        specialTiles[0][7] = new TripleWordTile(0, 7 * cellSize, cellSize, cellSize);
        specialTiles[0][14] = new TripleWordTile(0, 14 * cellSize, cellSize, cellSize);
        specialTiles[7][0] = new TripleWordTile(7 * cellSize, 0, cellSize, cellSize);
        specialTiles[7][14] = new TripleWordTile(7 * cellSize, 14 * cellSize, cellSize, cellSize);
        specialTiles[14][0] = new TripleWordTile(14 * cellSize, 0, cellSize, cellSize);
        specialTiles[14][7] = new TripleWordTile(14 * cellSize, 7 * cellSize, cellSize, cellSize);
        specialTiles[14][14] = new TripleWordTile(14 * cellSize, 14 * cellSize, cellSize, cellSize);
 
 
        // Double Word Score Tiles (Pink/Light Red)
        // The diagonal pattern
        for (int i = 1; i < 5; i++) {
            specialTiles[i][i] = new DoubleWordTile(i * cellSize, i * cellSize, cellSize, cellSize);
            specialTiles[i][14 - i] = new DoubleWordTile(i * cellSize, (14 - i) * cellSize, cellSize, cellSize);
            specialTiles[14 - i][i] = new DoubleWordTile((14 - i) * cellSize, i * cellSize, cellSize, cellSize);
            specialTiles[14 - i][14 - i] = new DoubleWordTile((14 - i) * cellSize, (14 - i) * cellSize, cellSize, cellSize);
        }
 
 
        // Also add the specific double word tile in the middle of the board
        specialTiles[7][7] = new DoubleWordTile(7 * cellSize, 7 * cellSize, cellSize, cellSize);
 
 
        // Triple Letter Score Tiles (Blue)
        // Based on the board image
        specialTiles[1][5] = new TripleLetterTile(5 * cellSize, 1 * cellSize, cellSize, cellSize);
        specialTiles[1][9] = new TripleLetterTile(9 * cellSize, 1 * cellSize, cellSize, cellSize);
        specialTiles[5][1] = new TripleLetterTile(1 * cellSize, 5 * cellSize, cellSize, cellSize);
        specialTiles[5][5] = new TripleLetterTile(5 * cellSize, 5 * cellSize, cellSize, cellSize);
        specialTiles[5][9] = new TripleLetterTile(9 * cellSize, 5 * cellSize, cellSize, cellSize);
        specialTiles[5][13] = new TripleLetterTile(13 * cellSize, 5 * cellSize, cellSize, cellSize);
        specialTiles[9][1] = new TripleLetterTile(1 * cellSize, 9 * cellSize, cellSize, cellSize);
        specialTiles[9][5] = new TripleLetterTile(5 * cellSize, 9 * cellSize, cellSize, cellSize);
        specialTiles[9][9] = new TripleLetterTile(9 * cellSize, 9 * cellSize, cellSize, cellSize);
        specialTiles[9][13] = new TripleLetterTile(13 * cellSize, 9 * cellSize, cellSize, cellSize);
        specialTiles[13][5] = new TripleLetterTile(5 * cellSize, 13 * cellSize, cellSize, cellSize);
        specialTiles[13][9] = new TripleLetterTile(9 * cellSize, 13 * cellSize, cellSize, cellSize);
 
 
        // Double Letter Score Tiles (Light Blue)
        // Based on the board image
        specialTiles[0][3] = new DoubleLetterTile(3 * cellSize, 0, cellSize, cellSize);
        specialTiles[0][11] = new DoubleLetterTile(11 * cellSize, 0, cellSize, cellSize);
        specialTiles[2][6] = new DoubleLetterTile(6 * cellSize, 2 * cellSize, cellSize, cellSize);
        specialTiles[2][8] = new DoubleLetterTile(8 * cellSize, 2 * cellSize, cellSize, cellSize);
        specialTiles[3][0] = new DoubleLetterTile(0, 3 * cellSize, cellSize, cellSize);
        specialTiles[3][7] = new DoubleLetterTile(7 * cellSize, 3 * cellSize, cellSize, cellSize);
        specialTiles[3][14] = new DoubleLetterTile(14 * cellSize, 3 * cellSize, cellSize, cellSize);
        specialTiles[6][2] = new DoubleLetterTile(2 * cellSize, 6 * cellSize, cellSize, cellSize);
        specialTiles[6][6] = new DoubleLetterTile(6 * cellSize, 6 * cellSize, cellSize, cellSize);
        specialTiles[6][8] = new DoubleLetterTile(8 * cellSize, 6 * cellSize, cellSize, cellSize);
        specialTiles[6][12] = new DoubleLetterTile(12 * cellSize, 6 * cellSize, cellSize, cellSize);
        specialTiles[7][3] = new DoubleLetterTile(3 * cellSize, 7 * cellSize, cellSize, cellSize);
        specialTiles[7][11] = new DoubleLetterTile(11 * cellSize, 7 * cellSize, cellSize, cellSize);
        specialTiles[8][2] = new DoubleLetterTile(2 * cellSize, 8 * cellSize, cellSize, cellSize);
        specialTiles[8][6] = new DoubleLetterTile(6 * cellSize, 8 * cellSize, cellSize, cellSize);
        specialTiles[8][8] = new DoubleLetterTile(8 * cellSize, 8 * cellSize, cellSize, cellSize);
        specialTiles[8][12] = new DoubleLetterTile(12 * cellSize, 8 * cellSize, cellSize, cellSize);
        specialTiles[11][0] = new DoubleLetterTile(0, 11 * cellSize, cellSize, cellSize);
        specialTiles[11][7] = new DoubleLetterTile(7 * cellSize, 11 * cellSize, cellSize, cellSize);
        specialTiles[11][14] = new DoubleLetterTile(14 * cellSize, 11 * cellSize, cellSize, cellSize);
        specialTiles[12][6] = new DoubleLetterTile(6 * cellSize, 12 * cellSize, cellSize, cellSize);
        specialTiles[12][8] = new DoubleLetterTile(8 * cellSize, 12 * cellSize, cellSize, cellSize);
        specialTiles[14][3] = new DoubleLetterTile(3 * cellSize, 14 * cellSize, cellSize, cellSize);
        specialTiles[14][11] = new DoubleLetterTile(11 * cellSize, 14 * cellSize, cellSize, cellSize);
    }

    public void giveInitialTiles() {
        // Give initial tiles to all 4 players
        Random rand = new Random();
        for (int player = 0; player < 4; player++) {
            while (playerTiles[player].size() < 7 && bank.size() > 0) {
                int index = rand.nextInt(bank.size());
                Tile drawn = bank.remove(index);
                drawn.moveTo(100 + playerTiles[player].size() * 40, 700);
                playerTiles[player].add(drawn);
            }
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

    public void drawBoard(Graphics g) {
        int rows = 15, cols = 15, cellSize = 40;

        // Draw board base color (green)
        g.setColor(new Color(0, 110, 0)); // Dark green background
        g.fillRect(0, 0, cols * cellSize, rows * cellSize);

        // Draw grid lines
        g.setColor(Color.WHITE);
        for (int i = 0; i <= rows; i++) {
            g.drawLine(0, i * cellSize, cols * cellSize, i * cellSize);
        }
        for (int i = 0; i <= cols; i++) {
            g.drawLine(i * cellSize, 0, i * cellSize, rows * cellSize);
        }

        // Draw special tiles
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (specialTiles[row][col] != null) {
                    specialTiles[row][col].draw(g);
                }
            }
        }

        drawTilesBoard(g);
        drawPlayerTiles(g);
    }

    private void drawTilesBoard(Graphics g) {
        for (int i = 0; i < board.length; i++) {
            for (int f = 0; f < board[i].length; f++) {
                if (board[i][f] != null) {
                    board[i][f].setLoc(f * 40, i * 40); // Corrected x and y for board
                    board[i][f].drawPTiles(g);
                }
            }
        }
    }
        
    // Modified to display current player's tiles
    public void drawPlayerTiles(Graphics g) {
        for (int i = 0; i < getCurrentPlayerTiles().size(); i++) {
            getCurrentPlayerTiles().get(i).setLoc(100 + 40 * i, 700);
            getCurrentPlayerTiles().get(i).drawPTiles(g);
        }
        
        // Show current player and scores
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Player " + (currentPlayerIndex + 1) + "'s Turn", 600, 650);
        
        // Draw all player scores
        for (int i = 0; i < 4; i++) {
            String scoreText = "Player " + (i + 1) + ": " + playerScores.get(i) + " points";
            g.drawString(scoreText, 615, 450 + (i * 25));
        }
    }
    
    public ArrayList<Tile> getCurrentPlayerTiles() {
        return playerTiles[currentPlayerIndex];
    }
    
    public int getNumTiles() {
        return getCurrentPlayerTiles().size();
    }
    
    public Tile getTile(int index) {
        if (index >= 0 && index < getCurrentPlayerTiles().size()) {
            return getCurrentPlayerTiles().get(index);
        }
        return null; // Handle out of bounds
    }
    
    public void playTile(int x, int y, Tile tile) {
        if (x >= 0 && x < board.length && y >= 0 && y < board[0].length && board[x][y] == null) {
            board[x][y] = tile;
            if (counter < newTiles[0].length) {
                newTiles[0][counter] = x;
                newTiles[1][counter] = y;
                counter++;
            }
            // Remove the tile from the player's hand
            getCurrentPlayerTiles().remove(tile);
        }
    }
    
    // Modified to check if first play is in the center
    public boolean isValidPlay() {
        boolean bool = true;
        if (counter == 0) { // No new tiles played
            bool = false;
        }
        
        // First play must include the center square
        if (firstPlay) {
            boolean centerUsed = false;
            for (int i = 0; i < counter; i++) {
                if (newTiles[0][i] == 7 && newTiles[1][i] == 7) {
                    centerUsed = true;
                    break;
                }
            }
            if (!centerUsed) {
                bool = false;
            }
        } else {
            // For subsequent plays, check if new tiles connect to existing tiles
            boolean connected = false;
            for (int i = 0; i < counter; i++) {
                int row = newTiles[0][i];
                int col = newTiles[1][i];
                
                // Check adjacent cells (up, down, left, right)
                if ((row > 0 && board[row-1][col] != null && !isNewTile(row-1, col)) ||
                    (row < BOARD_SIZE-1 && board[row+1][col] != null && !isNewTile(row+1, col)) ||
                    (col > 0 && board[row][col-1] != null && !isNewTile(row, col-1)) ||
                    (col < BOARD_SIZE-1 && board[row][col+1] != null && !isNewTile(row, col+1))) {
                    connected = true;
                    break;
                }
            }
            if (!connected) {
                bool = false;
            }
        }

        if (!isStraightLine()) {
            bool = false;
        }

        if (!isContinuous()) {
            bool = false;
        }

        // Check if all words formed are valid
        if (!areWordsValid()) {
            bool = false;
        }

        if(!bool){
            for(int i = 0; i < 7; i++){
                if(!(newTiles[0][i] == 0 && newTiles[1][i] == 0 && board[0][0] == null)){
                    playerTiles[currentPlayerIndex].add(board[newTiles[0][i]][newTiles[1][i]]);
                    board[newTiles[0][i]][newTiles[1][i]] = null;
                }
            }
        }



        return bool;
    }

    private boolean isValidWord(String word) {
        if (word == null || word.length() < 2){
            return false;
        }

        // Binary search
        int low = 0;
        int high = allWords.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2; // To prevent potential overflow
            int comparisonResult = allWords.get(mid).compareTo(word);

            if (comparisonResult == 0) {
                return true; // Target found at index mid
            } else if (comparisonResult < 0) {
                low = mid + 1; // Target is in the right half
            } else {
                high = mid - 1; // Target is in the left half
            }
        }

        return false;
    }

    private String[] getFormedWords() {
        String[] words = new String[10]; // Assume a max of 10 words formed
        int wordCount = 0;

        if (counter == 0){
            return new String[0];
        }
        int firstRow = newTiles[0][0];
        int firstCol = newTiles[1][0];
        boolean horizontal = (counter > 1) ? (newTiles[0][0] == newTiles[0][1]) : false;
        boolean vertical = (counter > 1) ? (newTiles[1][0] == newTiles[1][1]) : false;

        if (counter == 1) {
            horizontal = true;
            vertical = true;
        }

        // Check for horizontal word(s)
        if (horizontal) {
            int startCol = firstCol;
            int endCol = firstCol;
            //find the beginning of the word
            while (startCol > 0 && board[firstRow][startCol - 1] != null) {
                startCol--;
            }
            //find the end of the word
            while (endCol < BOARD_SIZE - 1 && board[firstRow][endCol + 1] != null) {
                endCol++;
            }
            String word = "";
            for (int c = startCol; c <= endCol; c++) {
                word += board[firstRow][c].getLetter();
            }
            if (word.length() > 1) {
                words[wordCount++] = word;
            }

            //check vertical words
            for (int col = firstCol; col < counter + firstCol; col++) {
                int startRow = firstRow;
                int endRow = firstRow;

                while (startRow > 0 && board[startRow - 1][col] != null) {
                    startRow--;
                }
                while (endRow < BOARD_SIZE - 1 && board[endRow + 1][col] != null) {
                    endRow++;
                }
                String wordV = "";
                if (endRow != startRow) {
                    for (int r = startRow; r <= endRow; r++) {
                        wordV += board[r][col].getLetter();
                    }
                    if (wordV.length() > 1) {
                        words[wordCount++] = wordV;
                    }
                }
            }

        }

        // Check for vertical word(s)
        if (vertical) {
            int startRow = firstRow;
            int endRow = firstRow;

            while (startRow > 0 && board[startRow - 1][firstCol] != null) {
                startRow--;
            }
            while (endRow < BOARD_SIZE - 1 && board[endRow + 1][firstCol] != null) {
                endRow++;
            }
            String word = "";
            for (int r = startRow; r <= endRow; r++) {
                word += board[r][firstCol].getLetter();
            }
            if (word.length() > 1) {
                words[wordCount++] = word;
            }

            //check horizontal words
            for (int row = firstRow; row < counter + firstRow; row++) {
                int startCol = firstCol;
                int endCol = firstCol;

                while (startCol > 0 && board[row][startCol - 1] != null) {
                    startCol--;
                }
                while (endCol < BOARD_SIZE - 1 && board[row][endCol + 1] != null) {
                    endCol++;
                }
                String wordH = "";
                if (endCol != startCol) {
                    for (int c = startCol; c <= endCol; c++) {
                        wordH += board[row][c].getLetter();
                    }
                    if (wordH.length() > 1) {
                        words[wordCount++] = wordH;
                    }
                }
            }
        }

        if (wordCount > 0) {
            String[] result = new String[wordCount];
            for (int i = 0; i < wordCount; i++) {
                result[i] = words[i];
            }
            return result;
        } else {
            return new String[0];
        }

    }

    private boolean areWordsValid() {
        String[] words = getFormedWords();
        if (words == null) {
            return false;
        }
        for (String word : words) {
            if (!isValidWord(word)) {
                return false;
            }
        }
        return true;
    }

    private void sortList(ArrayList<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j) > list.get(j + 1)) {
                    int temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    private boolean isContinuous() {
        if (counter <= 1) { // Only one tile played, it's continuous
            return true;
        }

        // If in the same row
        if (newTiles[0][0] == newTiles[0][1]) {
            int row = newTiles[0][0];
            ArrayList<Integer> cols = new ArrayList<>();
            for (int i = 0; i < counter; i++) {
                cols.add(newTiles[1][i]);
            }
            sortList(cols); // Sort without Collections.sort

            for (int i = 0; i < cols.size() - 1; i++) {
                if (cols.get(i + 1) - cols.get(i) > 1) {
                    // Check for empty spaces between the played tiles
                    for (int j = cols.get(i) + 1; j < cols.get(i + 1); j++) {
                        if (board[row][j] == null) {
                            return false; // Gap in the played tiles
                        }
                    }
                }
            }
        }
        // If in the same column
        else if (newTiles[1][0] == newTiles[1][1]) {
            int col = newTiles[1][0];
            ArrayList<Integer> rows = new ArrayList<>();
            for (int i = 0; i < counter; i++) {
                rows.add(newTiles[0][i]);
            }
            sortList(rows); // Sort without Collections.sort

            for (int i = 0; i < rows.size() - 1; i++) {
                if (rows.get(i + 1) - rows.get(i) > 1) {
                    // Check for empty spaces between the played tiles
                    for (int j = rows.get(i) + 1; j < rows.get(i + 1); j++) {
                        if (board[j][col] == null) {
                            return false; // Gap in the played tiles
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean isStraightLine() {
        if (counter <= 1) { // Only one tile played, it's a straight line
            return true;
        }

        // Check if all new tiles are in the same row or same column
        int firstRow = newTiles[0][0];
        int firstCol = newTiles[1][0];
        boolean sameRow = true;
        boolean sameCol = true;

        for (int i = 1; i < counter; i++) {
            if (newTiles[0][i] != firstRow) {
                sameRow = false;
            }
            if (newTiles[1][i] != firstCol) {
                sameCol = false;
            }
        }

        return sameRow || sameCol;
    }
    
    private boolean isNewTile(int row, int col) {
        for (int i = 0; i < counter; i++) {
            if (newTiles[0][i] == row && newTiles[1][i] == col) {
                return true;
            }
        }
        return false;
    }
    
    // SCORING SYSTEM
    // Calculate score for the current play
    public int calculateScore() {
        int score = 0;
        String[] words = getFormedWords();
        
        if (words == null || words.length == 0) {
            return 0;
        }
        
        // For each word formed
        for (String word : words) {
            int wordScore = calculateWordScore(word);
            score += wordScore;
        }
        
        // Bonus for using all 7 tiles
        if (counter == 7) {
            score += 50;
        }
        
        return score;
    }
    
    private int calculateWordScore(String word) {
        int score = 0;
        int wordMultiplier = 1;
        
        // First, find the position of the word on the board
        int[] position = findWordPosition(word);
        if (position == null) {
            // Could not find word position
            return 0;
        }
        
        int startRow = position[0];
        int startCol = position[1];
        boolean isHorizontal = position[2] == 1;
        
        for (int i = 0; i < word.length(); i++) {
            int row = isHorizontal ? startRow : startRow + i;
            int col = isHorizontal ? startCol + i : startCol;
            
            // Get letter score
            Tile letterTile = board[row][col];
            int letterValue = letterTile.getValue();
            int letterMultiplier = 1;
            
            // Check for letter multipliers (only apply if it's a new tile)
            boolean isNewTilePlaced = false;
            for (int j = 0; j < counter; j++) {
                if (newTiles[0][j] == row && newTiles[1][j] == col) {
                    isNewTilePlaced = true;
                    break;
                }
            }
            
            if (isNewTilePlaced) {
                // Check special tile multipliers
                if (specialTiles[row][col] instanceof DoubleLetterTile) {
                    letterMultiplier = 2;
                } else if (specialTiles[row][col] instanceof TripleLetterTile) {
                    letterMultiplier = 3;
                } else if (specialTiles[row][col] instanceof DoubleWordTile) {
                    wordMultiplier *= 2;
                } else if (specialTiles[row][col] instanceof TripleWordTile) {
                    wordMultiplier *= 3;
                }
            }
            
            // Add letter score with multiplier
            score += letterValue * letterMultiplier;
        }
        
        // Apply word multiplier
        score *= wordMultiplier;
        
        return score;
    }
    
    private int[] findWordPosition(String word) {
        // Find where this word exists on the board
        // Returns [row, col, isHorizontal]
        
        // Check horizontal
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col <= BOARD_SIZE - word.length(); col++) {
                boolean match = true;
                for (int i = 0; i < word.length(); i++) {
                    if (board[row][col + i] == null || 
                        !board[row][col + i].getLetter().equals(word.substring(i, i+1))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return new int[] {row, col, 1}; // 1 means horizontal
                }
            }
        }
        
        // Check vertical
        for (int col = 0; col < BOARD_SIZE; col++) {
            for (int row = 0; row <= BOARD_SIZE - word.length(); row++) {
                boolean match = true;
                for (int i = 0; i < word.length(); i++) {
                    if (board[row + i][col] == null || 
                        !board[row + i][col].getLetter().equals(word.substring(i, i+1))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return new int[] {row, col, 0}; // 0 means vertical
                }
            }
        }
        
        return null; // Word not found on board
    }
    
    // End turn method with scoring
    public void endTurn() {
        // Calculate and add score for this play
        int turnScore = calculateScore();
        playerScores.set(currentPlayerIndex, playerScores.get(currentPlayerIndex) + turnScore);
        
        // If it was the first play, mark it as done
        if (firstPlay) {
            firstPlay = false;
        }
        
        // Refill the current player's hand
        replenishTiles(getCurrentPlayerTiles());
        
        // Switch to the next player
        switchPlayer();
        
        // Reset new tile counter
        resetNewTileCounter();
    }

    public void resetNewTileCounter() {
        counter = 0;
        newTiles = new int[2][7]; // Reset the array as well
    }
    
    private void replenishTiles(ArrayList<Tile> playerTiles) {
        Random rand = new Random();
        while (playerTiles.size() < 7 && bank.size() > 0) {
            int index = rand.nextInt(bank.size());
            Tile drawn = bank.remove(index);
            drawn.moveTo(100 + playerTiles.size() * 40, 700);
            playerTiles.add(drawn);
        }
    }
    
    public void switchPlayer() {
        // Move to next player (cycling through 0-3)
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }
    
    // Add method to check if game is over
    public boolean isGameOver() {
        // Game is over if any player has used all tiles and bank is empty
        return getCurrentPlayerTiles().isEmpty() && bank.isEmpty();
    }
    
    // Method to get winner
    public int getWinner() {
        int maxScore = -1;
        int winnerIndex = -1;
        for (int i = 0; i < 4; i++) {
            if (playerScores.get(i) > maxScore) {
                maxScore = playerScores.get(i);
                winnerIndex = i;
            }
        }
        return winnerIndex;
    }
    
    // Get player score
    public int getPlayerScore(int playerIndex) {
        return playerScores.get(playerIndex);
    }
    
    // Get current player index
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    
    // Keep all your other existing methods

    public int getBankSize() {
        return bank.size();
    }
    
    public void exchangeAllTiles() {
        // Put current tiles back in the bank
        Random rand = new Random();
        ArrayList<Tile> currentTiles = getCurrentPlayerTiles();
        
        // First remove all current tiles
        ArrayList<Tile> tilesToExchange = new ArrayList<>(currentTiles);
        for (Tile tile : tilesToExchange) {
            bank.add(tile);
        }
        currentTiles.clear();
        
        // Then draw new tiles
        while (currentTiles.size() < 7 && bank.size() > 0) {
            int index = rand.nextInt(bank.size());
            Tile drawn = bank.remove(index);
            drawn.moveTo(100 + currentTiles.size() * 40, 700);
            currentTiles.add(drawn);
        }
    }
}