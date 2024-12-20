import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;


    //IMAGES
    Image Background;
    Image FloppyBird;
    Image Tpipes;
    Image Bpipes;
    //Bird
    int BirdX = boardWidth/8;
    int BirdY = boardHeight/2;
    int BirdW = 34;
    int BirdH = 24;
    
    class BIRD{
        int X = BirdX;
        int Y = BirdY;
        int W = BirdW;
        int H = BirdH;
        Image img;

        public BIRD(Image img) {
            this.img = img;
        }
        
    }
    //PIPES
    int pipex = boardWidth;
    int pipey = 0;
    int pipew = 64; //scaled 1/6
    int pipeh = 512;

    class Pipe{
        int X = pipex;
        int Y = pipey;
        int W = pipew;
        int H = pipeh;
        Image img;
        boolean passed = false ; 
        Pipe(Image img){
            this.img = img;
        }
    }
    
    //GAME LOGIC
    BIRD bird;
    int velocityY = 0;//move bird up/down speed
    int velocityX = -4;//move pipe to the left speed(sumilating bird moving right)
    int gravity = 1;

    boolean GameOver = false ; 
    double score = 0;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameloop;
    Timer placePipesTimer;


     GamePanel() {
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        //setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);


        //LOAD IMAGES
        Background = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        FloppyBird = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        Tpipes = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        Bpipes = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        //bird
        bird = new BIRD(FloppyBird);

        //pipes
        pipes = new ArrayList<Pipe>();

        //game timer
        gameloop = new Timer(1000/60,this);
        gameloop.start();

        //pipes timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipesTimer.start();

    }
    public void placePipes(){
        //(0-1)*pipeheight/2 -> (0-256)
        //128
        //0 - 128 --> 1/4 pipeheight -> 3/4 pipeheight
        int randomPipeY = (int)(pipey - pipeh/8 - Math.random()*(pipeh/2));
        int openingSpace = boardHeight/4;
        
        Pipe topPipe = new Pipe(Tpipes);
        topPipe.Y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(Bpipes);
        bottomPipe.Y = topPipe.Y + pipeh + openingSpace;
        pipes.add(bottomPipe);

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //background
        g.drawImage(Background,0,0,boardWidth,boardHeight,null);
        //Bird
        g.drawImage(bird.img,bird.X,bird.Y,bird.W,bird.H,null); 
        //pipes 
        for (int i = 0; i <pipes.size() ; i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.X, pipe.Y, pipe.W,pipe.H,null);
            
        }
        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN,32));
        if(GameOver){
            g.drawString("GAME OVER! ", 10, 35);
            g.drawString("SCORE : "+ String.valueOf((int)score), 10, 70);
        }
        else{
            g.drawString(String.valueOf((int)score), 10, 35);
        }

    }

    public void move (){
        //bird
        velocityY += gravity;
        bird.Y += velocityY;
        bird.Y = Math.max(bird.Y,0);
        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.X += velocityX;
            
            if(!pipe.passed && bird.X > pipe.X + pipe.W){
                pipe.passed = true;
                score += 0.5; //because there is 2 pipes so the score is 1
            }
            if(collision(bird, pipe)){
                GameOver = true;
            }
        }
        if(bird.Y > boardHeight){
            GameOver = true;
        }

    }
    public boolean collision(BIRD a,Pipe b){
        return  a.X < b.X + b.W &&      //a's top corner dont reach b's top right corner
                a.X + a.W > b.X &&      //a's top right corner dont reach b's top left corner
                a.Y < b.Y + b.H &&      //a's top left dont reach b's bottom left corner
                a.Y + a.H > b.Y ;       //a's bottom left dont reach b's top left corner
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;
            if(GameOver){
                //restart the game by resetting the condition
                bird.Y = BirdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                GameOver = false;
                gameloop.start();
                placePipesTimer.start();
            }
        }
    }




    //not needed
    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(GameOver){
            placePipesTimer.stop();
            gameloop.stop();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
        
    }
}