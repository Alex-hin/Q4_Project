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
    private JButton startButton;
    private JButton endTurnButton; // Added end turn button

    public Screen() {
        scrabble = new BoardGame();
        setFocusable(true);
        setLayout(null);
        // add Key listener
        addKeyListener(this);
        addMouseListener(this);

        startButton = new JButton("Start Game");
        startButton.setBounds(300, 350, 200, 60);
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        add(startButton);
		startButton.addActionListener(this);
		startButton.setVisible(true);
		startButton.setEnabled(true);

        endTurnButton = new JButton("End Turn"); 
        endTurnButton.setBounds(700, 700, 100, 100); 
        endTurnButton.setFont(new Font("Arial", Font.BOLD, 24));
        endTurnButton.setVisible(false); 
        add(endTurnButton);
        endTurnButton.addActionListener(this);
        
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

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == startButton){
			gameStarted = true;
			startButton.setVisible(false);
			startButton.setEnabled(false);
			endTurnButton.setVisible(true);
			endTurnButton.setEnabled(true);
		} else if (e.getSource() == endTurnButton){
			if (scrabble.isValidPlay()) {
				scrabble.endTurn();
				scrabble.givePlayerNewTiles(); //give player new tiles
				scrabble.resetNewTileCounter();
				selectedTile = null; // Clear any selected tile
				repaint();
				requestFocusInWindow();
			}
		}
	}

    // interpret key clicks
    public void keyPressed(KeyEvent e) {

        repaint();

    }

    // You must have method signatures for all methods that are
    // part of an interface.
    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int boardX = x / 40;
        int boardY = y / 40;

        if (y >= 700 && y <= 740 && x >= 100 && x <= 100 + 40 * scrabble.getNumTiles()) {
            int tileX = (x - 100) / 40;
            selectedTile = scrabble.getTile(tileX);
        }

        System.out.println("Selected Tile: " + selectedTile + " at board position: " + boardX + ", " + boardY); // Debugging
        if (x <= 600 && y <= 600 && selectedTile != null) {
            scrabble.playTile(boardY, boardX, selectedTile);
            selectedTile.setLoc(boardX * 40, boardY * 40);
            selectedTile = null;
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
}