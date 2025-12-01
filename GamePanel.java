import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class GamePanel extends JPanel implements KeyListener, Runnable {

    public static final int ORIGINAL_WIDTH = 1400;
    public static final int ORIGINAL_HEIGHT = 960;
    //assume screen width = original width
    public static final int SCREEN_WIDTH = ORIGINAL_WIDTH;
    public static final int SCREEN_HEIGHT = ORIGINAL_HEIGHT;

    private Game game;
    private Thread gameThread;
    //store translate and scale factor
    private double currentScaleFactor = 1.0;
    private int currentOffsetX = 0;
    private int currentOffsetY = 0;

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(this);
        
        game = new Game(); 

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int screenX = e.getX();
                int screenY = e.getY();

                //calculate translate x&y
                double translatedX = screenX - currentOffsetX;
                double translatedY = screenY - currentOffsetY;

                int gameX = (int) (translatedX / currentScaleFactor);
                int gameY = (int) (translatedY / currentScaleFactor);

                //create new mouse event
                MouseEvent transformedEvent = new MouseEvent(
                    (java.awt.Component) e.getSource(),
                    e.getID(),
                    e.getWhen(),
                    e.getModifiersEx(),
                    gameX, //adjusted x
                    gameY, //adjusted y
                    e.getClickCount(),
                    e.isPopupTrigger()
                );

                //send the adjusted events to game
                game.handleMouseInput(transformedEvent);
                requestFocusInWindow();
            }
        });
    }

    public void animate() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        int currentWidth = getWidth();
        int currentHeight = getHeight();

        double scaleX = (double) currentWidth / ORIGINAL_WIDTH;
        double scaleY = (double) currentHeight / ORIGINAL_HEIGHT;
        
        currentScaleFactor = scaleFactor;

        g2d.scale(scaleFactor, scaleFactor);

        int scaledWidth = (int) (ORIGINAL_WIDTH * scaleFactor);
        int scaledHeight = (int) (ORIGINAL_HEIGHT * scaleFactor);
        
        int offsetX = (currentWidth - scaledWidth) / 2;
        int offsetY = (currentHeight - scaledHeight) / 2;
        
        currentOffsetX = offsetX;
        currentOffsetY = offsetY;
        
        g2d.translate(currentOffsetX / currentScaleFactor, currentOffsetY / currentScaleFactor);

        game.draw(g2d); 
        
        g2d.dispose(); 
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        game.handleKeyPress(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        game.handleKeyRelease(e);
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / 60.0;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                game.updateGame();
                repaint();
                delta--;
            }
        }
    }
}
