import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class Player {

    private int playerID;
    private Image shipImage;
    
    private int x, y;
    private int width = 100;
    private int height = 100;
    private int dx = 0;
    private final int SPEED = 10;
    
    private int minX, maxX;
    private int leftKey, rightKey, fireKey;
    
    private int score = 0;
    private int shotTimer = 0;
    private final int BULLET_COOLDOWN = 20; 
    
    private ArrayList<Bullet> bullets;

    public Player(int id, int boundaryMinX, int boundaryMaxX) {
        this.playerID = id;
        this.minX = boundaryMinX;
        this.maxX = boundaryMaxX - width;

        this.y = GamePanel.SCREEN_HEIGHT - height - 50; 
        this.x = (minX + maxX) / 2;
        
        bullets = new ArrayList<>();

        if (id == 1) {
            try {
                shipImage = ImageIO.read(new File("Player1.png"));
            } catch (IOException e) {
                System.out.println("Image Load Error Player1: " + e.getMessage());
            }
            this.leftKey = KeyEvent.VK_A;
            this.rightKey = KeyEvent.VK_D;
            this.fireKey = KeyEvent.VK_W;
        } else {
            try {
                shipImage = ImageIO.read(new File("Player2.png"));
            } catch (IOException e) {
                System.out.println("Image Load Error Player2: " + e.getMessage());
            }
            this.leftKey = KeyEvent.VK_LEFT;
            this.rightKey = KeyEvent.VK_RIGHT;
            this.fireKey = KeyEvent.VK_UP;
        }
    }

    public void update() {
        x += dx;
        
        if (x < minX) {
            x = minX;
        }
        if (x > maxX) {
            x = maxX;
        }
        
        if (shotTimer > 0) {
            shotTimer--;
        }
        
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.update();
            if (!b.isActive()) {
                bullets.remove(i);
                i--;
            }
        }
    }

    public void draw(Graphics g) {
        if (shipImage != null) {
            g.drawImage(shipImage, x, y, width, height, null);
        } else {
            g.setColor((playerID == 1) ? Color.BLUE : Color.RED);
            g.fillRect(x, y, width, height);
        }
        
        for (Bullet b : bullets) {
            b.draw(g);
        }
    }
    
    public void shoot() {
        if (shotTimer <= 0) {
            int bulletX = x + width / 2 - 5; 
            int bulletY = y;
            bullets.add(new Bullet(bulletX, bulletY));
            shotTimer = BULLET_COOLDOWN;
        }
    }
    
    public void handleKeyPress(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == leftKey) {
            dx = -SPEED;
        } else if (key == rightKey) {
            dx = SPEED;
        } else if (key == fireKey) {
            shoot();
        }
    }

    public void handleKeyRelease(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == leftKey && dx < 0) {
            dx = 0;
        } else if (key == rightKey && dx > 0) {
            dx = 0;
        }
    }
    
    public int getScore() {
        return score;
    }
    
    public void incrementScore(int points) {
        score += points;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
}
