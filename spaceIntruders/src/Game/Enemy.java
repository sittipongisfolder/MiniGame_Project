
package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author Spartan Tech
 */
public class Enemy extends MovingGameObject {

    ImageIcon alien1 = new ImageIcon("images/alien4.png");
    ImageIcon alien2 = new ImageIcon("images/alien5.png");
    ImageIcon alien3 = new ImageIcon("images/alien6.png");
    ImageIcon alienBoss = new ImageIcon("images/BOSS.png");
    ImageIcon alienBoss2 = new ImageIcon("images/BOSS.png");
    ImageIcon alienBoss3 = new ImageIcon("images/BOSS.png");
    
    private int enemytype, width, height;

    
    // Constuctor for any enemy
    public Enemy(int xPosition, int yPosition, int xVelocity, int yVelocity, int enemyType, Color color, int width, int height) {
        super(xPosition, yPosition, xVelocity, yVelocity, color);
        this.enemytype = enemyType;
        this.width = width;
        this.height = height;
    }
    
    @Override
    // Draws alien
    public void draw(Graphics g) {
        // Varient 1
        if (this.enemytype % 3 == 0) {
            alien1.paintIcon(null, g, this.getXPosition(), this.getYPosition());
        // Varient 2
        } else if (this.enemytype % 3 == 1 && this.enemytype != 100) {
            alien2.paintIcon(null, g, this.getXPosition(), this.getYPosition());
        // Varient 3
        } else if (this.enemytype % 3 == 2) {
            alien3.paintIcon(null, g, this.getXPosition(), this.getYPosition());
        // Boss Enemy
        } if (this.enemytype == 100)
        {
            if(GamePanel.getBossHealth()>20){
                alienBoss.paintIcon(null, g, this.getXPosition(), this.getYPosition());
            }
            else if(GamePanel.getBossHealth()>10){
                alienBoss2.paintIcon(null, g, this.getXPosition(), this.getYPosition());
            }
            else if(GamePanel.getBossHealth()>0){
                alienBoss3.paintIcon(null, g, this.getXPosition(), this.getYPosition());
            }
        }
    }

    // Gets the hitbox for normal eneimes
    @Override
    public Rectangle getBounds() {
        Rectangle enemyHitBox = new Rectangle(this.getXPosition(), this.getYPosition(), width, height);
        return enemyHitBox;
    }

    // Used to move all enemies
    @Override
    public void move() {
        xPos += xVel;
    }

}
