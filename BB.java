/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bb;

import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel; 

public class BB extends JPanel implements KeyListener, ActionListener, Runnable  { // runnable moves ball 
    
	//////////////////private static final long serialVersionUID = 1L;  ****************
    
	// movement keys..
	private static boolean right = false;
	private static boolean left = false;

        
        //bricks size
	private static final int brickWidth = 40; 
	private static final int brickHeight = 40;
	// variables declaration for brick...............................
	// ===============================================================
	// declaring ball, paddle,bricks // rectangles is used for sizing 
	private Rectangle Ball;//          
	private Rectangle Dash;//
	private Rectangle[] Brick;
	private Rectangle background = new Rectangle(0, 0, 350, 450); // window size 
//reverses......==>
	private int movex = -1;
	private int movey = -1;  // ball movement on xy 
	private boolean ballFallDown = false;
	private boolean bricksOver = false;
	private int count = 0;
	private String status;
	private JButton button;

	private static enum STATUS {
		START, END
	}


	private static boolean RUNNING = false; // moves ba

	BB() {
		initializeVariables();
		JFrame frame = new JFrame();
		button = new JButton(STATUS.START.name());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); /// closing button 


		this.setPreferredSize(new Dimension(background.width, background.height)); /// size of window 
		frame.getContentPane().add(this); /// created a layer and added sizes through the dim above with the help of THIS
		frame.pack(); // makes sure the sizing is propotional 

		frame.add(button, BorderLayout.SOUTH); /// the location of the button

		frame.setVisible(true); /// makes window visible

		button.addActionListener(this); // كل مره نسوي رن راح يعيد الامر الموضوع  this 

		this.addKeyListener(this); // responds to the keys
		this.setFocusable(true); // what the user interacts with 
	}

	public static void main(String[] args) {
		new BB();

	}

	// declaring ball, paddle,bricks

	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(background.x, background.y, background.width, background.height); // fills the color to the background 
		g.setColor(Color.BLACK);
		g.fillOval(Ball.x, Ball.y, Ball.width, Ball.height); 
		g.setColor(Color.CYAN);
		g.fill3DRect(Dash.x, Dash.y, Dash.width, Dash.height, true); // sizing 

		// this will paint below the peddle
		g.setColor(Color.GRAY);
		g.fillRect(0, 251, background.width, 200);

		
		g.setColor(Color.PINK);
		g.drawRect(0, 0, background.width - 1, 250); // this draw border line
                
		 for (int i = 0; i < Brick.length; i++) { // actual drawing of bricks 
			if (Brick[i] != null) {
				g.fill3DRect(Brick[i].x, Brick[i].y, Brick[i].width, Brick[i].height, true);
			}
		} 

                 
	/****/	if (ballFallDown == true || bricksOver == true) {
			Font f = new Font("Arial", Font.BOLD, 20);
			g.setFont(f);
			g.drawString(status, 70, 120);
			ballFallDown = false;
			bricksOver = false;
		}

	}


	public void run() {

		// == ball reverses when touches the brick=======
//ballFallDown == false && bricksOver == false
		while (RUNNING) {
			

//   if(gameOver == true){return;}
			for (int i = 0; i < Brick.length; i++) {
				if (Brick[i] != null) {
					if (Brick[i].intersects(Ball)) {
						Brick[i] = null;
						// movex = -movex;
						movey = -movey;
						count++;
					} 
				} 
			} 

			// /////////// =================================

			if (count == Brick.length) {// check if ball hits all bricks
				bricksOver = true;
				status = "YOU WON THE GAME";
                                
                                repaint();
                            sleep(); //slows down ball 
				continue; 
				
			}
		
			repaint();
			Ball.x += movex; /// the movement of ball based on the axis
			Ball.y += movey; 

			if (left == true) {   // uses dash left 

				Dash.x -= 3; // uses dash three points 
				right = false; // stops dash right
			}
			if (right == true) {
				Dash.x += 3;
				left = false;
			}
			  if (Dash.x <= 4) {
				Dash.x = 4;
			} else if (Dash.x >= 298) {
				Dash.x = 298;
			}    /// يحددالمساحة اللي تحرك فيها الداش 
                          
			
			if (Ball.intersects(Dash)) { // Ball reverses when strikes the bat
				movey = -movey;
		
			}
	
			
			if (Ball.x <= 0 || background.width - Ball.width <= Ball.x) { // ball reverses when touches left and right boundary
				movex = -movex;
			} 
                        
                        
			if (Ball.y <= 0) {   //  bally + Ball.height >= 250
				movey = -movey;
			} // if ends here.....
			if (Ball.y >= 250) {// when ball falls below bat game is over...
				ballFallDown = true;
				status = "YOU LOST THE GAME";
				repaint();
				button.setText(STATUS.START.toString());
				break;
			}

			sleep();
		} // while loop ends here

	}
	
	private void sleep() {
		try {
			Thread.sleep(10);
		} catch (Exception ex) {
		} 
	}

 
	@Override  // identify identical methods in different classes, and uses the method in the current class
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT) {
			left = true;
			
		}

		if (keyCode == KeyEvent.VK_RIGHT) {
			right = true;
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT) {
			left = false;
		}

		if (keyCode == KeyEvent.VK_RIGHT) {
			right = false;
		}
	}

	//@Override
	 //public void keyTyped(KeyEvent arg0) {

	//}

	@Override
	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		if (str.equals(STATUS.START.toString())) {
			button.setText(STATUS.END.toString());
			this.startGame();
		}
		
	

		if (str.equals(STATUS.END.toString())) {
			RUNNING = false;
			button.setText(STATUS.START.toString());		}

	}

	public void startGame() {
		requestFocus(true);
		initializeVariables();
		Thread t = new Thread(this);
		t.start();
	}

	public void initializeVariables() {

		int brickx = 40; // position of bricks in terms x
		int bricky = 40; // position of bricks in terms y
		RUNNING = true;
		// x = 160, y = 218, width = 5, height = 5
		Ball = new Rectangle(160, 218, 10, 10);     //////// startin point + size ball
		// x = 160, y = 245, width = 40, height = 5

		Dash = new Rectangle(160, 245, 80, 5); //////// starting point + size ball

		Brick = new Rectangle[12];
		// //////////// =====Creating bricks for the game===>.....
		createBricks(brickx, bricky);
		// ===========BRICKS created for the game new ready to use===
		movex = -1; //how many points the ball moves
		movey = -1; // how many points the ball moves
		ballFallDown = false;
		bricksOver = false;
		count = 0;
		status = null;

	}

	public void createBricks(int brickx, int bricky) {

		for (int i = 0; i < Brick.length; i++) {
			Brick[i] = new Rectangle(brickx, bricky, brickWidth, brickHeight);
			if (i == 6) {
				 brickx = 40;
				bricky = (bricky + brickHeight + 2);

			}
			if (i == 11) {
				 
				bricky = (bricky + brickHeight + 2);

			}
			brickx += (brickWidth + 1); /// moves 40 + 1 points into the new line of bricks  
		}
	}

}

