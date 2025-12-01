import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class Asteroid {

    private Image asteroidImage;
    private int x, y;
    private int width = 40;
    private int height = 40;
    private int dy;
    private boolean isActive = true;
    
    private static final Random RANDOM = new Random();
    
    public Asteroid(int minX, int maxX, int speed) {
        int type = RANDOM.nextInt(4) + 1; 
        
        try {
            asteroidImage = ImageIO.read(new File("Asteroid" + type + ".png"));
        } catch (IOException e) {
            System.out.println("Image Load Error Asteroid: " + e.getMessage());
        }

        this.x = minX + RANDOM.nextInt(maxX - minX - width);
        this.y = -height;
        this.dy = speed;
    }

    public void update() {
        if (isActive) {
            y += dy;
            
            if (y > GamePanel.SCREEN_HEIGHT) {
                isActive = false;
            }
        }
    }

    public void draw(Graphics g) {
        if (isActive && asteroidImage != null) {
            g.drawImage(asteroidImage, x, y, width, height, null);
        }
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void destroy() {
        this.isActive = false;
    }
}
