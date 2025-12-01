import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MenuScreen implements GameScreen {
    
    private Image openingBG; 
    private Image startButtonImage;
    private Image instructionButtonImage;
    private Rectangle startButtonBounds;
    private Rectangle instructionButtonBounds; 
    
    private Image moonImage;
    private double rotationAngle = 0;
    private int moonWidth = 3000; 
    private int moonHeight = 2000; 
    private int moonX = -800; // 
    private int moonY = 300; // 
    
    public enum MenuAction {
        NONE, START_GAME, QUIT, SHOW_INSTRUCTIONS
    }

    private MenuAction nextAction = MenuAction.NONE;

    public MenuScreen() {
        try {
            openingBG = ImageIO.read(new File("OpeningBG.png"));
            startButtonImage = ImageIO.read(new File("StartButton.png")); 
            instructionButtonImage = ImageIO.read(new File("InstructionsButton.png"));
            moonImage = ImageIO.read(new File("Moon.png"));
        } catch (IOException e) {
            System.out.println("Image Load Error in MenuScreen: Can't read input file! " + e.getMessage());
        }
        
        int startButtonWidth = 200; 
        int startButtonHeight = 50;
        int screenWidth = GamePanel.SCREEN_WIDTH; 
        
        int buttonX = (screenWidth - startButtonWidth) / 2;
        int buttonY = 450; 
        
        startButtonBounds = new Rectangle(buttonX, buttonY, startButtonWidth, startButtonHeight);
        
        int helpButtonSize = 100; 
        int helpX = screenWidth - helpButtonSize - 40; 
        int helpY = 40; 
        instructionButtonBounds = new Rectangle(helpX, helpY, helpButtonSize, helpButtonSize);
    }

    @Override
    public void update() {
        rotationAngle += 0.5; 
        if (rotationAngle >= 360) {
            rotationAngle -= 360;
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (openingBG != null) {
            g2d.drawImage(openingBG, 0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null); 
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT);
        }

        if (moonImage != null) {
            AffineTransform oldTransform = g2d.getTransform();
            g2d.translate(moonX + moonWidth / 2, moonY + moonHeight / 2);
            g2d.rotate(Math.toRadians(rotationAngle));
            g2d.drawImage(moonImage, -moonWidth / 2, -moonHeight / 2, moonWidth, moonHeight, null);
            g2d.setTransform(oldTransform);
        }
        
        if (startButtonImage != null) {
            g2d.drawImage(startButtonImage, 
                        startButtonBounds.x, 
                        startButtonBounds.y, 
                        startButtonBounds.width, 
                        startButtonBounds.height, 
                        null);
        } else {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(startButtonBounds.x, startButtonBounds.y, startButtonBounds.width, startButtonBounds.height);
        }

        if (instructionButtonImage != null) {
            g2d.drawImage(instructionButtonImage, instructionButtonBounds.x, instructionButtonBounds.y, instructionButtonBounds.width, instructionButtonBounds.height, null);
        } else {
            g2d.setColor(Color.CYAN);
            g2d.fillOval(instructionButtonBounds.x, instructionButtonBounds.y, instructionButtonBounds.width, instructionButtonBounds.height);
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 40)); 
            g2d.drawString("?", instructionButtonBounds.x + (instructionButtonBounds.width / 2) - 10, instructionButtonBounds.y + (instructionButtonBounds.height / 2) + 15);
        }
    }

    @Override
    public void handleInput() {}
    
    public void handleMouseClick(MouseEvent e) {
        if (startButtonBounds.contains(e.getPoint())) {
            nextAction = MenuAction.START_GAME;
        } else if (instructionButtonBounds.contains(e.getPoint())) {
            nextAction = MenuAction.SHOW_INSTRUCTIONS;
        }

    }
    
    public MenuAction getNextAction() {
        return nextAction;
    }
    
    public void resetAction() {
        nextAction = MenuAction.NONE;
    }
}
