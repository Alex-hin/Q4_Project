import game.*;

import javax.swing.JPanel;
import java.awt.Graphics;


import java.awt.Dimension;


// import KeyListener classes
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;


//If you implement buttons, import those classes, too.




// If you use mouse listening, add that interface, too.
public class Screen extends JPanel implements KeyListener{


	// instance variables
	BoardGame scrabble;


	public Screen(){

		scrabble = new BoardGame();
        setFocusable(true); 
		setLayout(null);
		// add Key listener
		addKeyListener(this);
	}


	@Override
	public Dimension getPreferredSize() {
		//Sets the size of the panel
        	return new Dimension(800,800);
	}
	
	@Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);

		scrabble.drawBoard(g);
		scrabble.drawPlayerTiles(g);

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


}
