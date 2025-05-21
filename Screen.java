import game.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Screen extends JPanel implements KeyListener, MouseListener, ActionListener {
    // instance variables
    BoardGame scrabble;
    Tile selectedTile;
    private int winner = -1;
    private int winnerScore = 0;
    private boolean gameStarted = false;
    private boolean gameEnded = false;
    private boolean showingHowToPlay = false; // Track if How to Play screen is showing
    private JButton startButton;
    private JButton endTurnButton;
    private JButton exchangeTilesButton; // Button to exchange tiles
    private JButton passButton; // Button to pass turn
    private JButton restartButton; // Button to restart the game when ended
    private JButton howToPlayButton; // Button to show How to Play screen
    private JButton backButton; // Button to return from How to Play screen
    private JLabel statusLabel; // Status messages for players
    private Confetti confetti;
    private int passCounter = 0; // Count consecutive passes
    
    // Colors for the How to Play screen
    private final Color DW_COLOR = new Color(255, 150, 150); // Double word - light red
    private final Color TW_COLOR = new Color(255, 100, 100); // Triple word - darker red
    private final Color DL_COLOR = new Color(150, 150, 255); // Double letter - light blue
    private final Color TL_COLOR = new Color(100, 100, 255); // Triple letter - darker blue
    
    public Screen() {
        scrabble = new BoardGame();
        setFocusable(true);
        setLayout(null);
        // add Key listener
        addKeyListener(this);
        addMouseListener(this);
        confetti = new Confetti(800, 800);
        // Start Game Button
        startButton = new JButton("Start Game");
        startButton.setBounds(300, 350, 200, 60);
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        add(startButton);
        startButton.addActionListener(this);
        startButton.setVisible(true);
        startButton.setEnabled(true);
        
        // How To Play Button
        howToPlayButton = new JButton("How to Play");
        howToPlayButton.setBounds(300, 430, 200, 60);
        howToPlayButton.setFont(new Font("Arial", Font.BOLD, 24));
        add(howToPlayButton);
        howToPlayButton.addActionListener(this);
        howToPlayButton.setVisible(true);
        
        // Back Button (for How to Play screen)
        backButton = new JButton("Back");
        backButton.setBounds(300, 700, 200, 60);
        backButton.setFont(new Font("Arial", Font.BOLD, 24));
        backButton.addActionListener(this);
        add(backButton);
        backButton.setVisible(false);
        
        // End Turn Button
        endTurnButton = new JButton("End Turn");
        endTurnButton.setBounds(620, 650, 150, 40);
        endTurnButton.addActionListener(this);
        add(endTurnButton);
        endTurnButton.setVisible(false);
        
        // Exchange Tiles Button
        exchangeTilesButton = new JButton("Exchange Tiles");
        exchangeTilesButton.setBounds(620, 700, 150, 40);
        exchangeTilesButton.addActionListener(this);
        add(exchangeTilesButton);
        exchangeTilesButton.setVisible(false);
        
        // Pass Button
        passButton = new JButton("Pass");
        passButton.setBounds(620, 750, 150, 40);
        passButton.addActionListener(this);
        add(passButton);
        passButton.setVisible(false);
        
        // Restart Button (for game end)
        restartButton = new JButton("Play Again");
        restartButton.setBounds(300, 500, 200, 60);
        restartButton.setFont(new Font("Arial", Font.BOLD, 24));
        restartButton.addActionListener(this);
        add(restartButton);
        restartButton.setVisible(false);
        
        // Status Label
        statusLabel = new JLabel("");
        statusLabel.setBounds(100, 650, 500, 40);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(statusLabel);
        statusLabel.setVisible(false);
        
        setPreferredSize(new Dimension(800, 800));
    }
    
    public void animate() {
        while (true) {
            // update the components
            if (confetti != null) {
                confetti.update();
            }
            // draw the screen
            repaint();
            // sleep for 30 milliseconds
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (showingHowToPlay) {
            drawHowToPlayScreen(g);
        } else if (!gameStarted) {
            scrabble.drawStartScreen(g);
        } else if (gameEnded) {
            drawEndScreen(g);
        } else {
            scrabble.drawBoard(g);
        }
    }
    
    private void drawHowToPlayScreen(Graphics g) {
        // Draw background
        g.setColor(new Color(0, 100, 0)); // Dark green background
        g.fillRect(0, 0, 800, 800);
        
        // Draw title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("How to Play Scrabble", 160, 80);
        
        // Draw main instructions
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Game Overview:", 50, 130);
        
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        String[] instructions = {
            "• It's a 15x15 board with standard Scrabble rules. The game includes a word list of all possible words.",
            "• There are 4 players who take turns in a circle, each forming words on the board.",
            "• Players connect letters to existing words, scoring points based on letter values and special squares.",
            "• The game ends when all tiles are used or no more valid moves can be made.",
            "• The winner is the player with the most points at the end of the game."
        };
        
        for (int i = 0; i < instructions.length; i++) {
            g.drawString(instructions[i], 50, 160 + (i * 30));
        }
        
        // Draw board special squares section
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Board Special Squares:", 50, 330);
        
        // Draw special square examples
        int squareX = 50;
        int squareY = 350;
        int squareSize = 30;
        int spacing = 40;
        
        // Double Word square
        g.setColor(DW_COLOR);
        g.fillRect(squareX, squareY, squareSize, squareSize);
        g.setColor(Color.BLACK);
        g.drawRect(squareX, squareY, squareSize, squareSize);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("DW - Double Word Score", squareX + squareSize + 10, squareY + 20);
        
        // Triple Word square
        g.setColor(TW_COLOR);
        g.fillRect(squareX, squareY + spacing, squareSize, squareSize);
        g.setColor(Color.BLACK);
        g.drawRect(squareX, squareY + spacing, squareSize, squareSize);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("TW - Triple Word Score", squareX + squareSize + 10, squareY + spacing + 20);
        
        // Double Letter square
        g.setColor(DL_COLOR);
        g.fillRect(squareX, squareY + spacing * 2, squareSize, squareSize);
        g.setColor(Color.BLACK);
        g.drawRect(squareX, squareY + spacing * 2, squareSize, squareSize);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("DL - Double Letter Score", squareX + squareSize + 10, squareY + spacing * 2 + 20);
        
        // Triple Letter square
        g.setColor(TL_COLOR);
        g.fillRect(squareX, squareY + spacing * 3, squareSize, squareSize);
        g.setColor(Color.BLACK);
        g.drawRect(squareX, squareY + spacing * 3, squareSize, squareSize);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("TL - Triple Letter Score", squareX + squareSize + 10, squareY + spacing * 3 + 20);
        
        // Draw how to play section
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("How to Play:", 50, 520);
        
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        String[] gameplay = {
            "1. Select a tile from your rack at the bottom of the screen.",
            "2. Click on the board to place the selected tile.",
            "3. Continue placing tiles to form a word connected to existing words.",
            "4. Click \"End Turn\" to calculate your score and get new tiles.",
            "5. Use \"Exchange Tiles\" to swap your current tiles or \"Pass\" to skip your turn."
        };
        
        for (int i = 0; i < gameplay.length; i++) {
            g.drawString(gameplay[i], 50, 550 + (i * 30));
        }
        
        // Draw a mini board example
        drawMiniBoard(g, 500, 360, 240, 240);
    }
    
    private void drawMiniBoard(Graphics g, int x, int y, int width, int height) {
        int rows = 7;
        int cols = 7;
        int cellSize = width / cols;
        
        // Sample board pattern (center and a few special squares)
        String[][] boardPattern = {
            {"", "", "DL", "", "DL", "", ""},
            {"", "DW", "", "", "", "DW", ""},
            {"DL", "", "", "DL", "", "", "DL"},
            {"", "", "DL", "*", "DL", "", ""},
            {"DL", "", "", "DL", "", "", "DL"},
            {"", "DW", "", "", "", "DW", ""},
            {"", "", "DL", "", "DL", "", ""}
        };
        
        // Draw cells
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int cellX = x + c * cellSize;
                int cellY = y + r * cellSize;
                
                // Determine cell color based on special square
                String cellType = boardPattern[r][c];
                if (cellType.equals("DW")) {
                    g.setColor(DW_COLOR);
                } else if (cellType.equals("TW")) {
                    g.setColor(TW_COLOR);
                } else if (cellType.equals("DL")) {
                    g.setColor(DL_COLOR);
                } else if (cellType.equals("TL")) {
                    g.setColor(TL_COLOR);
                } else if (cellType.equals("*")) {
                    g.setColor(DW_COLOR); // Center is usually a DW
                } else {
                    g.setColor(new Color(240, 240, 200)); // Default cell color
                }
                
                // Draw cell
                g.fillRect(cellX, cellY, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(cellX, cellY, cellSize, cellSize);
                
                // Draw cell label if it's a special cell
                if (!cellType.isEmpty()) {
                    g.setFont(new Font("Arial", Font.BOLD, 12));
                    if (cellType.equals("*")) {
                        g.drawString("★", cellX + cellSize/2 - 5, cellY + cellSize/2 + 5);
                    } else {
                        g.drawString(cellType, cellX + 5, cellY + cellSize/2 + 5);
                    }
                }
            }
        }
        
        // Add sample word
        String[] sampleWord = {"S", "C", "R", "A", "B", "B", "L", "E"};
        g.setFont(new Font("Arial", Font.BOLD, 16));
        for (int i = 0; i < sampleWord.length && i < cols; i++) {
            int tileX = x + i * cellSize;
            int tileY = y + 3 * cellSize; // Place in the middle row
            
            // Draw tile
            g.setColor(new Color(250, 230, 150)); // Tile color
            g.fillRect(tileX, tileY, cellSize, cellSize);
            g.setColor(Color.BLACK);
            g.drawRect(tileX, tileY, cellSize, cellSize);
            
            // Draw letter
            g.drawString(sampleWord[i], tileX + cellSize/2 - 5, tileY + cellSize/2 + 5);
        }
    }
    
    private void drawEndScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 800);
        
        // Draw confetti
        if (confetti != null) {
            confetti.draw(g);
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("Game Over!", 250, 200);
        g.setFont(new Font("Arial", Font.PLAIN, 36));
        g.drawString("Player " + (winner + 1) + " wins!", 280, 280);
        g.drawString("Score: " + winnerScore, 320, 340);
        // Also display all player scores
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        for (int i = 0; i < 4; i++) {
            String scoreText = "Player " + (i + 1) + ": " + scrabble.getPlayerScore(i) + " points";
            g.drawString(scoreText, 300, 400 + (i * 30));
        }
    }
        
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startGame();
        } else if (e.getSource() == howToPlayButton) {
            showHowToPlay();
        } else if (e.getSource() == backButton) {
            hideHowToPlay();
        } else if (e.getSource() == endTurnButton) {
            endTurn();
        } else if (e.getSource() == exchangeTilesButton) {
            exchangeTiles();
        } else if (e.getSource() == passButton) {
            passTurn();
        } else if (e.getSource() == restartButton) {
            restartGame();
        }
    }
    
    private void showHowToPlay() {
        showingHowToPlay = true;
        startButton.setVisible(false);
        howToPlayButton.setVisible(false);
        backButton.setVisible(true);
    }
    
    private void hideHowToPlay() {
        showingHowToPlay = false;
        startButton.setVisible(true);
        howToPlayButton.setVisible(true);
        backButton.setVisible(false);
    }
    
    private void startGame() {
        gameStarted = true;
        startButton.setVisible(false);
        howToPlayButton.setVisible(false);
        endTurnButton.setVisible(true);
        exchangeTilesButton.setVisible(true);
        passButton.setVisible(true);
        statusLabel.setVisible(true);
        updateStatusLabel("Player 1's turn - Place tiles and end turn");
    }
    
    private void endTurn() {
        if (scrabble.isValidPlay()) {
            int score = scrabble.calculateScore();
            scrabble.playValidSound();
            scrabble.endTurn();
            passCounter = 0; // Reset pass counter as a valid move was made
            
            // Check if game is over
            checkGameEnd();
            
            if (!gameEnded) {
                updateStatusLabel("Player " + (scrabble.getCurrentPlayerIndex() + 1) + "'s turn");
            }
        } else {
            scrabble.playInvalidSound();
            updateStatusLabel("Invalid play! Try again.");
        }
    }
    
    private void exchangeTiles() {
        if (scrabble.getBankSize() >= 7) {
            scrabble.exchangeAllTiles();
            passTurn(); // Exchange counts as a turn
            updateStatusLabel("Tiles exchanged. Player " + (scrabble.getCurrentPlayerIndex() + 1) + "'s turn");
        } else {
            updateStatusLabel("Not enough tiles in bank to exchange!");
        }
    }
    
    private void passTurn() {
        passCounter++;
        scrabble.switchPlayer();
        scrabble.resetNewTileCounter();
        
        // If all 4 players pass in a row, end the game
        if (passCounter >= 4) {
            endGame();
        } else {
            updateStatusLabel("Turn passed. Player " + (scrabble.getCurrentPlayerIndex() + 1) + "'s turn");
        }
    }
    
    private void checkGameEnd() {
        if (scrabble.isGameOver()) {
            endGame();
        }
    }
    
    private void endGame() {
        gameEnded = true;
        winner = scrabble.getWinner();
        winnerScore = scrabble.getPlayerScore(winner);
        
        // Start confetti animation
        confetti.start();
        
        // Hide game buttons
        endTurnButton.setVisible(false);
        exchangeTilesButton.setVisible(false);
        passButton.setVisible(false);
        statusLabel.setVisible(false);
        
        // Show restart button
        restartButton.setVisible(true);
    }
    
    private void restartGame() {
        // Reset game state
        scrabble = new BoardGame();
        selectedTile = null;
        gameEnded = false;
        passCounter = 0;
        
        // Reset confetti
        confetti = new Confetti(800, 800);
        
        // Hide restart button
        restartButton.setVisible(false);
        
        // Start a new game
        startGame();
    }
    
    private void updateStatusLabel(String message) {
        statusLabel.setText(message);
    }
    
    public void keyTyped(KeyEvent e) {
    }
    
    public void keyPressed(KeyEvent e) {
        //change key code to 112 when submitting
        if(e.getKeyCode() == 79){
            if (showingHowToPlay) {
                hideHowToPlay();
            } else if(!gameStarted){
                startGame();
            } else if (!gameEnded){
                endGame();
            } else {
                restartGame();
            }
        }
    }
    
    public void keyReleased(KeyEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (showingHowToPlay || !gameStarted || gameEnded) {
            return; // Don't process clicks if showing How to Play or game not started or already ended
        }

        int x = e.getX();
        int y = e.getY();
        
        int boardX = x / 40;
        int boardY = y / 40;

        // Select a tile from the player's rack
        if (y >= 700 && y <= 740 && x >= 100 && x <= 100 + 40 * scrabble.getNumTiles()) {
            int tileX = (x - 100) / 40;
            selectedTile = scrabble.getTile(tileX);
            statusLabel.setText("Tile selected: " + selectedTile.getLetter());
        }

        // Place the selected tile on the board
        if (x <= 600 && y <= 600 && selectedTile != null) {
            scrabble.playTile(boardY, boardX, selectedTile);
            selectedTile = null;
            statusLabel.setText("Tile placed. Select another or end your turn.");
        }

        repaint();
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
}
