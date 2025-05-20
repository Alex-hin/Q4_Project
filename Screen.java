import game.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Screen extends JPanel implements KeyListener, MouseListener, ActionListener {

    // instance variables
    BoardGame scrabble;
    Tile selectedTile;
    private boolean gameStarted = false;
    private boolean gameEnded = false;
    private JButton startButton;
    private JButton endTurnButton;
    private JButton exchangeTilesButton; // Button to exchange tiles
    private JButton passButton; // Button to pass turn
    private JButton restartButton; //Button to restart the game when ended
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
        endTurnButton.setFont(new Font("Arial", Font.BOLD, 16));
        endTurnButton.setVisible(false); 
        add(endTurnButton);
        endTurnButton.addActionListener(this);
        
        // Exchange Tiles Button
        exchangeTilesButton = new JButton("Exchange Tiles");
        exchangeTilesButton.setBounds(620, 700, 150, 40);
        exchangeTilesButton.setFont(new Font("Arial", Font.BOLD, 16));
        exchangeTilesButton.setVisible(false);
        add(exchangeTilesButton);
        exchangeTilesButton.addActionListener(this);
        
        // Pass Button
        passButton = new JButton("Pass");
        passButton.setBounds(620, 750, 150, 40);
        passButton.setFont(new Font("Arial", Font.BOLD, 16));
        passButton.setVisible(false);
        add(passButton);
        passButton.addActionListener(this);
        
        // Status Label
        statusLabel = new JLabel("");
        statusLabel.setBounds(100, 650, 500, 30);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setVisible(false);
        add(statusLabel);
    }

    @Override
    public Dimension getPreferredSize() {
        //Sets the size of the panel
        return new Dimension(800, 800);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameStarted) {
            scrabble.drawStartScreen(g);
        } else {
            scrabble.drawBoard(g);
            scrabble.drawPlayerTiles(g);
            
            // Draw game info
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Player " + (scrabble.getCurrentPlayerIndex() + 1) + "'s Turn", 100, 630);
        }
    }

    // animate a scene
    public void animate() {
        while (true) {
            //pause for .01 second
            try {
                Thread.sleep(10);    // 10 milliseconds
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            repaint();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            gameStarted = true;
            startButton.setVisible(false);
            startButton.setEnabled(false);
            endTurnButton.setVisible(true);
            endTurnButton.setEnabled(true);
            exchangeTilesButton.setVisible(true);
            passButton.setVisible(true);
            statusLabel.setVisible(true);
            statusLabel.setText("Game started! Player 1's turn.");
        } 
        else if (e.getSource() == endTurnButton) {
            if (scrabble.isValidPlay()) {
                int points = scrabble.calculateScore();
                passCounter = 0; // Reset pass counter
                
                statusLabel.setText("Valid play! Player " + 
                                   (scrabble.getCurrentPlayerIndex() + 1) + 
                                   " scored " + points + " points!");
                
                scrabble.endTurn();
                selectedTile = null; // Clear any selected tile
                
                statusLabel.setText("Now Player " + 
                                   (scrabble.getCurrentPlayerIndex() + 1) + 
                                   "'s turn.");
                
                // Check if game over
                if (scrabble.isGameOver()) {
                    endGame();
                }
                
                repaint();
                requestFocusInWindow();
            } else {
                statusLabel.setText("Invalid play! Please try again.");
            }
        }
        else if (e.getSource() == exchangeTilesButton) {
            // Exchange tiles functionality
            // Only allow if the bank has at least 7 tiles left
            if (scrabble.getBankSize() >= 7) {
                scrabble.exchangeAllTiles();
                passCounter = 0; // Reset pass counter
                scrabble.switchPlayer();
                statusLabel.setText("Tiles exchanged. Now Player " + 
                                   (scrabble.getCurrentPlayerIndex() + 1) + 
                                   "'s turn.");
                repaint();
                requestFocusInWindow();
            } else {
                statusLabel.setText("Not enough tiles in bank to exchange!");
            }
        }
        else if (e.getSource() == passButton) {
            // Pass turn
            passCounter++;
            scrabble.switchPlayer();
            selectedTile = null;
            
            statusLabel.setText("Player passed. Now Player " + 
                               (scrabble.getCurrentPlayerIndex() + 1) + 
                               "'s turn.");
            
            // If all players pass consecutively, end the game
            if (passCounter >= 8) { // 2 full rounds of passes
                endGame();
            }
            
            repaint();
            requestFocusInWindow();
        }
    }
    
    private void endGame() {
        int winner = scrabble.getWinner();
        int winnerScore = scrabble.getPlayerScore(winner);
        
        // Disable all game buttons
        endTurnButton.setEnabled(false);
        exchangeTilesButton.setEnabled(false);
        passButton.setEnabled(false);
        
        // Show game over message with winner
        /*JOptionPane.showMessageDialog(this,
                "Game Over!\nPlayer " + (winner + 1) + " wins with " + winnerScore + " points!",
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);*/
    }

    // Keep your existing mouse and key listener methods

    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        
        if (!gameStarted) return;
        
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

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}