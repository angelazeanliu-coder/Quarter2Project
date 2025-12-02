import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TransitionScreen implements GameScreen {
    
    private Image galaxyImage; 
    private Image loadingText; 
    private Image moonImage;
    
    private long startTime;
    private final long TRANSITION_DURATION_MS = 500; 
    private final double MAX_ROTATION_DEGREES = 30.0; 

    private final int MOON_DRAW_WIDTH = 250;
    private final int MOON_DRAW_HEIGHT = 255; 
    
    private final int LOADING_TEXT_WIDTH = 810;
    private final int LOADING_TEXT_HEIGHT = 188;

    public TransitionScreen() {
        try {
            galaxyImage = ImageIO.read(new File("GalaxyBG.PNG"));
            loadingText = ImageIO.read(new File("LoadingText.png")); 
            moonImage = ImageIO.read(new File("MoonWithStar.png")); 
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
        Graphics2D g2d = (Graphics2D) g;

        if (galaxyImage != null) {
            g2d.drawImage(galaxyImage, 0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null); 
        }
        
        if (moonImage != null) {
            long timeElapsed = Math.min(System.currentTimeMillis() - startTime, TRANSITION_DURATION_MS);
            double percentage = (double) timeElapsed / TRANSITION_DURATION_MS;
            double currentAngle = percentage * MAX_ROTATION_DEGREES; 
            
            int drawX = (GamePanel.SCREEN_WIDTH - MOON_DRAW_WIDTH) / 2; 
            int drawY = 200;

            //save rotations
            AffineTransform oldTransform = g2d.getTransform();

            //apply rotations
            g2d.rotate(
                Math.toRadians(currentAngle), 
                drawX + MOON_DRAW_WIDTH / 2, 
                drawY + MOON_DRAW_HEIGHT / 2
            );

            g2d.drawImage(moonImage, drawX, drawY, MOON_DRAW_WIDTH, MOON_DRAW_HEIGHT, null);
            //go back to old tranformation
            g2d.setTransform(oldTransform);
        }

        if (loadingText != null) {
            int x = (GamePanel.SCREEN_WIDTH - LOADING_TEXT_WIDTH) / 2;
            int y = GamePanel.SCREEN_HEIGHT / 2 + 100; 
            
            g2d.drawImage(loadingText, x, y, LOADING_TEXT_WIDTH, LOADING_TEXT_HEIGHT, null);
        }
    }

    @Override
    public void handleInput() {}
    
    public boolean isTransitionComplete() {
        return System.currentTimeMillis() - startTime >= TRANSITION_DURATION_MS;
    }
    
}
