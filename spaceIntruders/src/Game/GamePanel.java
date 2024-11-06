
package Game;

import Controller.KeyboardController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.awt.Font;
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

/**
 *
 * @author Spartan Tech
 */
public class GamePanel extends JPanel {

    // Required components. Do not remove!
    private Timer gameTimer;
    private KeyboardController controller;
    // Controls size of game window and framerate
    private final int gameWidth = 800;
    private final int gameHeight = 800;
    private final int framesPerSecond = 120;

    // Added Counters
    Random r = new Random();
    private int score = 0;
    private int level = 1;
    private int numberOfLives = 3;
    private int highScore;
    private int markerX, markerY;
    private static int bossHealth = 30;
    File f = new File("Highscore.txt");

    // Added Objects
    private Ship playerShip;
    private Ship singleLife;
    private Ship bonusEnemy;
    private Enemy enemy;
    //private Shield shield;
    private Bullet bullet;
    private Beam beam, beam2, beam3;

    // Added Booleans
    private boolean newBulletCanFire = true;
    private boolean newBeamCanFire = true;
    private boolean newBonusEnemy = true;
    private boolean hitMarker = false;

    // Added Array Lists
    private ArrayList<Ship> lifeList = new ArrayList();
    private ArrayList<Ship> bonusEnemyList = new ArrayList();
    private ArrayList<Enemy> enemyList = new ArrayList();
    private ArrayList<Beam> beamList = new ArrayList();
    private ImageIcon background = new ImageIcon("images/MAP.jpg");

    // Added Audio files and streams
    private File beamSound = new File("sounds/alienBeam.wav");

    private AudioStream beamSoundAudio;
    private InputStream beamSoundInput;
 
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// EXTRA METHODS
    
// Used in the Enemy class to help with the draw method for the boss
    public static int getBossHealth() {
        return bossHealth;
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETUP GAME

    public final void setupGame() {

        // Sets enemies for normal levels
        if (level != 3 && level != 6 && level != 9 && level != 12) {
            // Six rows
            for (int row = 0; row < 4; row++) {
                // 5 columns
                for (int column = 0; column < 3; column++) {       
                    enemy = new Enemy((20 + (row * 100)), (20 + (column * 60)), level, 0, column, null, 40, 40); // Enemy speed will increase each level
                    enemyList.add(enemy);
                }
            }
        }
        // Sets enemy for boss levels
       
        if (level == 3 || level == 6 || level == 9 || level == 12) {
            //AudioPlayer.player.start(bossSoundAudio); // Plays boss roar
            enemy = new Enemy(20, 20, 3, 0, 100, null, 150, 150);
            enemyList.add(enemy);
        }
        
        
        /*if (level == 3) {
        background = new ImageIcon("images/backgroundSkin.jpg"); // เปลี่ยนเป็นไฟล์ภาพที่ต้องการ
        }*/
        // Resets all controller movement
        controller.resetController();

        // Sets the player's ship values   
        playerShip = new Ship(375, 730, null, controller);

        // Sets the life counter Ships
        for (int column = 0; column < numberOfLives; column++) {
            singleLife = new Ship(48 + (column * 20), 10, Color.WHITE, null);
            lifeList.add(singleLife);
        }
        if (level == 1) {
        background = new ImageIcon("images/MAP.jpg"); 
        }
        if (level == 4) {
        background = new ImageIcon("images/MAP1.png");
        }
        if (level == 7) {
        background = new ImageIcon("images/MAP2.png");
        }
        if (level == 10) {
        background = new ImageIcon("images/MAP4.png"); 
        }
        // Gives directions on level 1
        if (level == 1) {
            JOptionPane.showMessageDialog(null, "Welcome to Space Intruders!");
        }
       
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// PAINT
    @Override
    public void paint(Graphics g) {

        // Sets background image
        background.paintIcon(null, g, 0, -150);

       
        // Draws the player's ship
        playerShip.draw(g);

        

        //Draws 3 different kinds of aliens
        try {
            for (int index = 0; index < enemyList.size(); index++) {
                enemyList.get(index).draw(g);
            }

        } catch (IndexOutOfBoundsException e) {
        }

        // Draw a bullet on space bar press
        if (controller.getKeyStatus(32)) {
            if (newBulletCanFire) {
                bullet = new Bullet(playerShip.getXPosition() + 22, playerShip.getYPosition() - 20, 0, Color.RED);
                newBulletCanFire = false;
            }
        }
        // Only attempts to draw bullet after key press
        if (bullet != null) {
            bullet.draw(g);
        }

        // Generates random beams shot from enemies
        if (level != 3 && level != 6 && level != 9 && level != 12) {
            if (newBeamCanFire) {
                for (int index = 0; index < enemyList.size(); index++) {
                    if (r.nextInt(30) == index) {
                        beam = new Beam(enemyList.get(index).getXPosition(), enemyList.get(index).getYPosition(), 0, Color.YELLOW);
                        beamList.add(beam);
                        
                    }
                    newBeamCanFire = false;
                }
            }
        }
        // Generates beams at a faster rate for boss
        if (level == 3 || level == 6 || level == 9 || level == 12) {
            if (newBeamCanFire) {
                for (int index = 0; index < enemyList.size(); index++) {
                    if (r.nextInt(5) == index) {
                        beam = new Beam(enemyList.get(index).getXPosition() + 75, enemyList.get(index).getYPosition() + 140, 0, Color.YELLOW);
                        beam2 = new Beam(enemyList.get(index).getXPosition(), enemyList.get(index).getYPosition() + 110, 0, Color.YELLOW);
                        beam3 = new Beam(enemyList.get(index).getXPosition() + 150, enemyList.get(index).getYPosition() + 110, 0, Color.YELLOW);
                        beamList.add(beam);
                        beamList.add(beam2);
                        beamList.add(beam3);
                        
                    }
                    newBeamCanFire = false;
                }
            }
        }
        // Draws the generated beams
        for (int index = 0; index < beamList.size(); index++) {
            beamList.get(index).draw(g);
        }
        // Generates random bonus enemy
        if (newBonusEnemy) {
            if (r.nextInt(3000) == 1500) {
                bonusEnemy = new Ship(-50, 30, Color.RED, null);
                bonusEnemyList.add(bonusEnemy);
                newBonusEnemy = false;
            }
        }
        // Draws bonus enemy
        for (int index = 0; index < bonusEnemyList.size(); index++) {
            bonusEnemyList.get(index).bonusDraw(g);
        }

        // Sets the score display
        g.setColor(Color.RED);
        g.drawString("Score: " + score, 260, 20);

        // Sets the life counter display
        g.setColor(Color.RED);
        g.drawString("Lives:", 11, 20);
        for (int index = 0; index < lifeList.size(); index++) {
            lifeList.get(index).lifeDraw(g);
        }
        // Sets level display
        g.setColor(Color.RED);
        g.drawString("Level " + level, 750, 20);

        // Sets Highscore display
        g.setColor(Color.RED);
        g.drawString("Highscore: " + highScore, 440, 20);

        // Draws a health display for boss level
        if (level == 3 || level == 6 || level == 9 || level == 12) {
            g.setColor(Color.RED);
            Font font = new Font("Arial", Font.BOLD, 30);
            g.setFont(font);
            g.drawString("Boss Health: " + bossHealth, 300, 600);
        }
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// UPDATE GAME STATE
    
    public void updateGameState(int frameNumber) {

        // Allows player to move left and right
        playerShip.move();

        // Updates highscore
        try {
            Scanner fileScan = new Scanner(f);
            while (fileScan.hasNextInt()) {
                String nextLine = fileScan.nextLine();
                Scanner lineScan = new Scanner(nextLine);
                highScore = lineScan.nextInt();
            }
        } catch (FileNotFoundException e) {
        }
        // Adds option to reset highScore
        if (controller.getKeyStatus(82)) {
            int answer = JOptionPane.showConfirmDialog(null, "Would you like to reset the high score?", ":)", 0);
            controller.resetController();
            if (answer == 0) {
                try {
                    String scoreString = Integer.toString(0);
                    PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
                    pw.write(scoreString);
                    pw.close();
                } catch (FileNotFoundException e) {
                }
            }
        }
        // Updates the high score text file if your score exceeds the previous high score
        try {
            if (score > highScore) {
                String scoreString = Integer.toString(score);
                PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
                pw.write(scoreString);
                pw.close();
            }
        } catch (FileNotFoundException e) {
        }

        // Makes enemies move and change direction at borders
        if ((enemyList.get(enemyList.size() - 1).getXPosition() + enemyList.get(enemyList.size() - 1).getXVelocity()) > 760 || (enemyList.get(0).getXPosition() + enemyList.get(0).getXVelocity()) < 0) {
            for (int index = 0; index < enemyList.size(); index++) {
                enemyList.get(index).setXVelocity(enemyList.get(index).getXVelocity() * -1);
                enemyList.get(index).setYPosition(enemyList.get(index).getYPosition() + 10);
            }
        } else {
            for (int index = 0; index < enemyList.size(); index++) {
                enemyList.get(index).move();
            }
        }

        // bullet speed
        if (bullet != null) {
            bullet.setYPosition(bullet.getYPosition() - 22);
            if (bullet.getYPosition() < 0) {
                newBulletCanFire = true;
            }

            // Checks for collisions with normal enemies
            for (int index = 0; index < enemyList.size(); index++) {
                if (bullet.isColliding(enemyList.get(index))) {
                    //AudioPlayer.player.start(hitSoundAudio); // Plays hitmarker sound if you hit an enemy
                    bullet = new Bullet(0, 0, 0, null);
                    newBulletCanFire = true;
                    // Updates score for normal levels
                    if (level != 3 && level != 6 && level != 9 && level != 12) {
                        score += 100;
                        hitMarker = true;
                        markerX = enemyList.get(index).getXPosition(); // Gets positions that the "+ 100" spawns off of
                        markerY = enemyList.get(index).getYPosition();
                        enemyList.remove(index);

                    }
                    // Updates score for boss levels
                    if (level == 3 || level == 6 || level == 9 || level == 12) {
                        hitMarker = true;
                        markerX = enemyList.get(index).getXPosition(); // Gets positions that the "- 1" spawns off of
                        markerY = enemyList.get(index).getYPosition() + 165;
                        bossHealth -= 1;
                        if (bossHealth == 0) {
                            enemyList.remove(index);
                            score += 9000;// Bonus score for defeating boss
                        }
                    }
                }
            }

           
        }
        // Moves bonus enemy
        if (!bonusEnemyList.isEmpty()) {
            for (int index = 0; index < bonusEnemyList.size(); index++) {
                bonusEnemyList.get(index).setXPosition(bonusEnemyList.get(index).getXPosition() + (2));
                if (bonusEnemyList.get(index).getXPosition() > 800) {
                    bonusEnemyList.remove(index);
                    newBonusEnemy = true;
                }
            }
            // bonus enemy and bullet collsion
            for (int index = 0; index < bonusEnemyList.size(); index++) {
                if (bullet != null) {
                    if (bonusEnemyList.get(index).isColliding(bullet)) {
                        bonusEnemyList.remove(index);
                        bullet = new Bullet(0, 0, 0, null);
                        newBulletCanFire = true;
                        newBonusEnemy = true;
                        //AudioPlayer.player.start(bonusSoundAudio); // Plays sound if player hits a bonus enemy
                        score += 5000; // add bonus to score on hit
                    }
                }
            }
        }

        // Moves beams on normal levels
        if (level != 3 && level != 6 && level != 9 && level != 12) {
            if (beam != null) {
                for (int index = 0; index < beamList.size(); index++) {
                    beamList.get(index).setYPosition(beamList.get(index).getYPosition() + (4));
                    if (beamList.get(index).getYPosition() > 800) {
                        beamList.remove(index);
                    }
                }
            }
        }
        // Moves beams at a faster speed for boss
        if (level == 3 || level == 6 || level == 9 || level == 12) {
            if (beam != null) {
                for (int index = 0; index < beamList.size(); index++) {
                    beamList.get(index).setYPosition(beamList.get(index).getYPosition() + (2 * level)); //Boss beam speed will increase each level
                    if (beamList.get(index).getYPosition() > 800) {
                        beamList.remove(index);
                    }
                }
            }
        }

       
      

        // Checks for beam and player collisions
        for (int index = 0; index < beamList.size(); index++) {
            if (beamList.get(index).isColliding(playerShip)) {
                beamList.remove(index);
                //AudioPlayer.player.start(damageSoundAudio); // Plays damage sound
                lifeList.remove(lifeList.size() - 1); // Removes life if hit by bullet
            }
        }

        // Paces beam shooting by only allowing new beams to be fired once all old beams are off screen or have collided
        if (beamList.isEmpty()) {
            newBeamCanFire = true;
        }

        
       
        //Updates the life counter display 
        if (playerShip.isColliding) {
            int index = lifeList.size() - 1;
            lifeList.remove(index);
        } 
        // Ends game if player runs out of lives
        else if (lifeList.isEmpty()) {
            //AudioPlayer.player.start(deathSoundAudio); // Plays death sound when you run out of lives
            // Gives the player an option to play again or exit
            int answer = JOptionPane.showConfirmDialog(null, "Would you like to play again?", "You lost the game with " + score + " points", 0);
            // If they choose to play again, this resets every element in the game
            if (answer == 0) {
                lifeList.clear();
                enemyList.clear();
                beamList.clear();
                bonusEnemyList.clear();
                score = 0;
                level = 1;
                bossHealth = 30;
                numberOfLives = 3;
                newBulletCanFire = true;
                newBeamCanFire = true;
                newBonusEnemy = true;
                setupGame();
            }
            // If they choose not to play again, it closes the game
            if (answer == 1) {
                System.exit(0);
            }
        }

        // Goes to next level, resets all lists, sets all counters to correct values
        if (enemyList.isEmpty()) {
            beamList.clear();
            //shieldList.clear();
            bonusEnemyList.clear();
            lifeList.clear();
            level += 1;
            bossHealth = 10;
            setupGame();
            //AudioPlayer.player.start(levelUpSoundAudio); // Plays level up sound
        }
        
        // All streams needed for every sound in the game
        try {
            beamSoundInput = new FileInputStream(beamSound);
            //beamSoundAudio = new AudioStream(beamSoundInput);

        } catch (IOException e) {
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// GAME PANEL    
    
    public GamePanel() {
        // Set the size of the Panel
        this.setSize(gameWidth, gameHeight);
        this.setPreferredSize(new Dimension(gameWidth, gameHeight));
        this.setBackground(Color.BLACK);

        // Register KeyboardController as KeyListener
        controller = new KeyboardController();
        this.addKeyListener(controller);

        // Call setupGame to initialize fields
        this.setupGame();
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    /**
     * Method to start the Timer that drives the animation for the game. It is
     * not necessary for you to modify this code unless you need to in order to
     * add some functionality.
     */
    public void start() {
        // Set up a new Timer to repeat every 20 milliseconds (50 FPS)
        gameTimer = new Timer(1000 / framesPerSecond, new ActionListener() {

            // Tracks the number of frames that have been produced.
            // May be useful for limiting action rates
            private int frameNumber = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the game's state and repaint the screen
                updateGameState(frameNumber++);
                repaint();
            }
        });
        Timer gameTimerHitMarker = new Timer(1000, new ActionListener() {

            // Tracks the number of frames that have been produced.
            // May be useful for limiting action rates
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the game's state and repaint the screen
                hitMarker = false;
            }
        });

        gameTimer.setRepeats(true);
        gameTimer.start();
        gameTimerHitMarker.setRepeats(true);
        gameTimerHitMarker.start();
    }

}
    