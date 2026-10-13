import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean isRUnning = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;

    private int playerX=310;

    private int ballposX=120;
    private int ballposY=350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map;

    public GamePlay(){
        map = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g){
        // 1. Paint the entire background black first! (Assuming your window size is roughly 692x592)
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 700, 600);

        map.draw((Graphics2D)g);

        // 2. (Optional) If you still want those wall lines to be a different color,
        // you can draw them here, or remove them if you don't want them.
        g.setColor(Color.DARK_GRAY); // or BLACK if you want them invisible
        g.fillRect(0, 0, 5, 595);
        g.fillRect(0, 0, 695, 5);
        g.fillRect(697, 0, 5, 590);

        // 3. Draw the paddle
        g.setColor(Color.WHITE);
        g.fillRect(playerX, 550, 100, 8);

        // 4. Draw the ball
        g.fillOval(ballposX, ballposY, 20, 20);

        g.dispose();
    }

    public void moveRight(){
        isRUnning=true;
        playerX+=20;
    }

    public void moveLeft(){
        isRUnning=true;
        playerX-=20;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        repaint();
        //Ball - Pedal interaction
        if(isRUnning){
            if(new Rectangle(ballposX, ballposY, 20, 30).intersects(new Rectangle(playerX, 550, 100, 8))){
                ballYdir=-ballYdir;
            }
            ballposX+=ballXdir;
            ballposY+=ballYdir;
            if(ballposX<0){
                ballXdir=-ballXdir;
            }
            if(ballposY<0){
                ballYdir=-ballYdir;
            }

            if(ballposX>670){
                ballXdir=-ballXdir;
            }

        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            if(playerX>=590){
                playerX=590;
            }else{
                moveRight();
            }
        }

        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            if(playerX<=10){
                playerX=10;
            }else{
                moveLeft();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
