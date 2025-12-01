import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class InstructionsScreen implements GameScreen {
    
    private Image instructionsImage;
    private long startTime;
    private final long INSTRUCTIONS_DURATION_MS = 5000; 

    public InstructionsScreen() {
        try {
            instructionsImage = ImageIO.read(new File("Instructions.png"));
        } catch (IOException e) {
             System.out.println("Image Load Error InstructionsScreen: " + e.getMessage());
        }
        resetTimer(); 
    }

    public void resetTimer() {
        startTime = System.currentTimeMillis();
    }
    
    @Override
    public void draw(Graphics g) {
        if (instructionsImage != null) {
            g.drawImage(instructionsImage, 0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null);
        }
    }
    
    @Override
    public void update() {

    }
    
    @Override
    public void handleInput() {}

    public boolean isTimeUp() {
        return System.currentTimeMillis() - startTime >= INSTRUCTIONS_DURATION_MS;
    }
}
