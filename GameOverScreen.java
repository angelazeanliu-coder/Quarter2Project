import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameOverScreen implements GameScreen {
    
    private static final Color GOLD = new Color(233, 213, 174);
    
    private Image galaxyImage;
    
    public GameOverScreen() {
        try {
            galaxyImage = ImageIO.read(new File("GalaxyBG.PNG"));
        } catch (IOException e) {
             System.out.println("Image Load Error GameOverScreen: " + e.getMessage());
        }
    }

    @Override
    public void update() {
    }

    public void draw(Graphics g, int p1RoundsWon, int p2RoundsWon, int timeRemainingSeconds) {
        
        if (galaxyImage != null) {
            g.drawImage(galaxyImage, 0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null);
        }

        g.setColor(GOLD); 
        g.setFont(new Font("Arial", Font.BOLD, 100));
        g.drawString("GAME OVER", 380, 200);

        String winnerText;
        if (p1RoundsWon > p2RoundsWon) {
            winnerText = "PLAYER 1 WINS!";
        } else if (p2RoundsWon > p1RoundsWon) {
            winnerText = "PLAYER 2 WINS!";
        } else {
            winnerText = "TIE GAME!";
        }
        
        g.setFont(new Font("Arial", Font.BOLD, 60));
        g.drawString(winnerText, 400, 350);
        
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Final Score: P1 - " + p1RoundsWon + " | P2 - " + p2RoundsWon, 450, 450);

        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Returning to Main Menu in " + timeRemainingSeconds + "...", 450, 750);

    }


    @Override
    public void draw(Graphics g) {
        draw(g, 0, 0, 0); 
    }
    
    @Override
    public void handleInput() {}
}
