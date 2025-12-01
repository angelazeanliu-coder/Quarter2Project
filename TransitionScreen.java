import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TransitionScreen implements GameScreen {
    
    private Image galaxyImage; 
    private Image loadingText; 
    
    private long startTime;
    private final long TRANSITION_DURATION_MS = 300; 

    public TransitionScreen() {
        try {
            galaxyImage = ImageIO.read(new File("GalaxyBG.PNG"));
            loadingText = ImageIO.read(new File("LoadingText.png"));
        } catch (IOException e) {
             System.out.println("Transition Screen Image Load Error: " + e.getMessage());
        }
    }

    public void startTransition() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics g) {
        if (galaxyImage != null) {
            g.drawImage(galaxyImage, 0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null); 
        }
        
        if (loadingText != null) {
            int x = (GamePanel.SCREEN_WIDTH - loadingText.getWidth(null)) / 2;
            int y = (GamePanel.SCREEN_HEIGHT - loadingText.getHeight(null)) / 2;
            g.drawImage(loadingText, x, y, null);
        }
    }

    @Override
    public void handleInput() {}
    
    public boolean isTransitionComplete() {
        return System.currentTimeMillis() - startTime >= TRANSITION_DURATION_MS;
    }
}
