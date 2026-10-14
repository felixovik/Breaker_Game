import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean isRUnning = false;
    private int score = 0;
    private int gameLevel = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 6;
    private boolean win = false;
    SoundManager sd = new SoundManager();
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

        g.setColor(Color.MAGENTA);
        g.setFont(new Font("serif",Font.BOLD, 25));
        g.drawString("Score:"+ score, 590, 30);

        if(totalBricks<=0 && gameLevel<3){
            isRUnning=false;
            win=true;
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 700, 600);

            g.setColor(Color.GREEN);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Level Complete! :)", 190,100);
            g.drawString("Score: " + score, 190, 250);

            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Press Enter to Restart!",230,400);

            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Level: " + gameLevel + "/3" ,50,500);

        } else if (totalBricks<=0 && gameLevel==3) {
            isRUnning=false;
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 700, 600);

            g.setColor(Color.GREEN);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("YOU COMPLETED THE GAME!", 190,100);
            g.drawString("Score: " + score, 190, 250);

            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Press Enter for a Surprise!",230,400);

            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Level: " + gameLevel + "/3" ,50,500);
        }

        if(ballposY>570){
            isRUnning=false;
            ballXdir=0;
            ballYdir=0;
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 700, 600);

            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.ITALIC, 30));
            g.drawString("GAME OVER!!!", 190,100);
            g.drawString("Score: " + score, 190, 250);

            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Press Enter to Restart!",230,400);

            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Level: " + gameLevel + "/3" ,50,500);
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void moveRight(){
        isRUnning=true;
        playerX+=40;
    }

    public void moveLeft(){
        isRUnning=true;
        playerX-=40;
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
            for(int i=0; i<map.map.length; i++){
                for(int j=0; j<map.map[0].length; j++){
                    if(map.map[i][j]>0){
                        int brickX = j*map.brickWidth+80;
                        int brickY=i*map.brickHeight+50;
                        int brickWidth=map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20,20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            sd.playBrickSound();
                            score+=5;
                            if(ballposX+19<=brickRect.x || ballposX+1>=brickRect.x + brickRect.width){
                                ballXdir = -ballXdir;
                            }else{
                                ballYdir=ballYdir;
                            }
                        }
                    }
                }
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
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!isRUnning) {
                if (!win) {
                    // Player lost: Retry the CURRENT level (rebuild bricks for this level)
                    setupLevel(gameLevel);
                } else {
                    // Player won: Advance to the NEXT level
                    gameLevel++;
                    setupLevel(gameLevel);
                }
            }
        }
    }

    // A single method that handles setting up the ball, score, and bricks for any level
    public void setupLevel(int level) {
        // 1. Reset the ball, score, and game state
        isRUnning = true;
        win = false;
        ballposX = 120;
        ballposY = 350;
        ballXdir = -1;
        ballYdir = -2;
        score = 0;

        // 2. Build the correct bricks based on which level we are entering/retrying
        if(level ==0){
            totalBricks = 21;
            map = new MapGenerator(3, 7);
        }
        else if (level == 1) {
            totalBricks = 28;
            map = new MapGenerator(4, 7);
        } else if (level == 2) {
            totalBricks = 35;
            map = new MapGenerator(5, 7);
        } else if (level >= 3) {
            openWebpage("https://www.youtube.com/watch?v=EE-xtCF3T94");
            System.exit(0);
        }

        repaint();
    }

    //Opens a webpage with a funny sussy pokemon video.
    public static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
