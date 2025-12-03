import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet {
    
    private int x, y;
    private final int width = 10;
    private final int height = 20;
    private final int speed = 15;
    private boolean isActive = true;
    
    private static final Color LAZER_COLOR = new Color(221, 161, 187);
    
    public Bullet(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }
    
    public void update() {
        y -= speed;
        
        if (y < 0) {
            isActive = false;
        }
    }
    
    public void draw(Graphics g) {
        if (isActive) {
            g.setColor(LAZER_COLOR);
            g.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void deactivate() {
        isActive = false;
    }
}
