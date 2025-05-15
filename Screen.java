import game.*;

import javax.swing.*;

//import org.w3c.dom.events.MouseEvent;

import java.awt.*;

// import KeyListener classes
import java.awt.event.*;


//If you implement buttons, import those classes, too.




// If you use mouse listening, add that interface, too.
public class Screen extends JPanel implements KeyListener, MouseListener{


	// instance variables
	BoardGame scrabble;
	Tile selectedTile;
	private boolean gameStarted = false;
	private JButton startButton;


	public Screen(){

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

        // Action listener to handle button clicks
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameStarted = true;
                remove(startButton); // Hide the button
                repaint();
                requestFocusInWindow(); // Ensure key inputs work
            }
        });
	}


	@Override
	public Dimension getPreferredSize() {
		//Sets the size of the panel
        	return new Dimension(800,800);
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
		while(true){
            //pause for .01 second
            try {
                Thread.sleep(10);    // 10 milliseconds
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            


            repaint();
        }
	}


	// interpret key clicks
	public void keyPressed(KeyEvent e){
		
		repaint();
		
	}


	// You must have method signatures for all methods that are
	// part of an interface.
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	public void mouseClicked(MouseEvent e){
		int x = e.getX();
		int y = e.getY();
		int boardX = x / 40;
		int boardY = y / 40;
		
		if(y >= 700 && y <= 740 && x >= 100 && x <= 100 + 40 * scrabble.getNumTiles()){
			int tileX = (x - 100)/40;
			selectedTile = scrabble.getTile(tileX);
		}
		

		System.out.println(selectedTile);
		if(x <= 600 && y <= 600){
			scrabble.playTile(boardX, boardY, selectedTile);
			selectedTile = null;
		}

		repaint();


	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}



}
