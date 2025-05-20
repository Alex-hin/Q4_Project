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
    private JButton startButton;
    private JButton endTurnButton;
    private JButton exchangeTilesButton; // Button to exchange tiles
    private JButton passButton; // Button to pass turn
    private JButton restartButton; // Button to restart the game when ended
    private JLabel statusLabel; // Status messages for players
    
    private int passCounter = 0; // Count consecutive passes
    
    public Screen() {
        scrabble = new BoardGame();
        setFocusable(true);
        setLayout(null);
        // add Key listener
        addKeyListener(this);
        addMouseListener(this);
        
        // Start Game Button
        startButton = new JButton("Start Game");
        startButton.setBounds(300, 350, 200, 60);
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        add(startButton);
        startButton.addActionListener(this);
        startButton.setVisible(true);
        startButton.setEnabled(true);
        
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
        if (!gameStarted) {
            scrabble.drawStartScreen(g);
        } else if (gameEnded) {
            drawEndScreen(g);
        } else {
            scrabble.drawBoard(g);
        }
    }
    
    private void drawEndScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 800);
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
    
    private void startGame() {
        gameStarted = true;
        startButton.setVisible(false);
        endTurnButton.setVisible(true);
        exchangeTilesButton.setVisible(true);
        passButton.setVisible(true);
        statusLabel.setVisible(true);
        updateStatusLabel("Player 1's turn - Place tiles and end turn");
    }
    
    private void endTurn() {
        if (scrabble.isValidPlay()) {
            int score = scrabble.calculateScore();
            scrabble.endTurn();
            passCounter = 0; // Reset pass counter as a valid move was made
            
            // Check if game is over
            checkGameEnd();
            
            if (!gameEnded) {
                updateStatusLabel("Player " + (scrabble.getCurrentPlayerIndex() + 1) + "'s turn");
            }
        } else {
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
    }
    
    public void keyReleased(KeyEvent e) {
    }
    
    public void mouseClicked(MouseEvent e) {
        if (!gameStarted || gameEnded) {
            return; // Don't process clicks if game not started or already ended
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


    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
}

