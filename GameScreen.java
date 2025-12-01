import java.awt.Graphics;

public interface GameScreen {
    
    void update();
    
    void draw(Graphics g);
    
    void handleInput(); 
}
