import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList; 

public class RoundScreen implements GameScreen {
    
    private enum RoundState {
        READY, PLAYING, FINISHED
    }
    private RoundState currentState = RoundState.READY; 

    private int roundNumber; 
    private boolean roundFinished = false;
    // roundWinner: 1=P1 win, 2=P2 win, 0=same score
    private int roundWinner = 0; 
    
    private Image backgroundImage;
    private Image readyImage; 
    private long stateTimer;
    private final long READY_DURATION_MS = 3000; 
    private final long ROUND_DURATION_SECONDS = 30; 

    private Asteroid[] activeAsteroids;
    
    private final int ASTEROID_SPAWN_INTERVAL = 90; 
    private int spawnTimer = 0;

    private int maxAsteroidsToSpawn; 
    private int asteroidsSpawned = 0; 
    
    private final int ASTEROID_BASE_SPEED = 3; 

    private int spawnAlternator = 0; 

    private Player p1; 
    private Player p2;

    private Color pink = new Color(221,161,187);
    private Color gold = new Color(233,213,174);
    private Color palePink = new Color(236,201,200);
    private Color purple = new Color(156,135,217);
    
    private static final Random RANDOM = new Random();

    public int getRoundNumber() {
        return roundNumber;
    }

    public RoundScreen(int round) {
        this.roundNumber = round;
        
        String backgroundPath;
        if (round == 1) {
            backgroundPath = "GameBG.png";
            maxAsteroidsToSpawn = 16; 
        } else if (round == 2) {
            backgroundPath = "Game2BG.png"; 
            maxAsteroidsToSpawn = 26; 
        } else {
            backgroundPath = "Game3BG.png";
            maxAsteroidsToSpawn = 36; 
        }
        // ⬆️⬆️ 恢复背景切换逻辑 ⬆️⬆️
        
        String readyImagePath = "Round" + round + ".png"; 
        
        try {
            backgroundImage = ImageIO.read(new File(backgroundPath));
            readyImage = ImageIO.read(new File(readyImagePath));
        } catch (IOException e) {
            System.out.println("Image Load Error RoundScreen (" + backgroundPath + "): " + e.getMessage());
        }
        
        activeAsteroids = new Asteroid[maxAsteroidsToSpawn];
        
        int middleX = GamePanel.SCREEN_WIDTH / 2;
        
        p1 = new Player(1, 0, middleX); 
        p2 = new Player(2, middleX, GamePanel.SCREEN_WIDTH); 

        stateTimer = System.currentTimeMillis();
    }
    
    
    private void spawnAsteroid() {
        if (asteroidsSpawned >= maxAsteroidsToSpawn) {
            return; 
        }
        
        int middleX = GamePanel.SCREEN_WIDTH / 2;
        int boundaryMinX;
        int boundaryMaxX;
        int speedVariance;

        if (roundNumber == 1) speedVariance = 0;
        else if (roundNumber == 2) speedVariance = 1;
        else speedVariance = 2;


        if (spawnAlternator == 0) {
            boundaryMinX = 0;
            boundaryMaxX = middleX;
            spawnAlternator = 1; 
        } else {
            boundaryMinX = middleX;
            boundaryMaxX = GamePanel.SCREEN_WIDTH;
            spawnAlternator = 0; 
        }
        
        int finalSpeed = ASTEROID_BASE_SPEED + RANDOM.nextInt(speedVariance + 1);
        
        activeAsteroids[asteroidsSpawned] = new Asteroid(boundaryMinX, boundaryMaxX, finalSpeed);
        
        asteroidsSpawned++;
    }


    private void determineWinner() {
        int score1 = p1.getScore();
        int score2 = p2.getScore();

        if (score1 > score2) {
            roundWinner = 1; 
        } else if (score2 > score1) {
            roundWinner = 2; 
        } else {
            roundWinner = 0;
        }
        roundFinished = true;
        currentState = RoundState.FINISHED;
    }


    @Override
    public void update() {
        if (currentState == RoundState.READY) {
            if (System.currentTimeMillis() - stateTimer >= READY_DURATION_MS) {
                currentState = RoundState.PLAYING;
                stateTimer = System.currentTimeMillis(); 
            }
            return;
        }
        

        if (System.currentTimeMillis() - stateTimer >= ROUND_DURATION_SECONDS * 1000) {
            determineWinner();
            return;
        }
        
        if (asteroidsSpawned < maxAsteroidsToSpawn) {
            spawnTimer++;
            if (spawnTimer >= ASTEROID_SPAWN_INTERVAL) {
                spawnAsteroid();
                spawnTimer = 0;
            }
        }
        
        p1.update();
        p2.update();
        
        checkCollisions(p1);
        checkCollisions(p2);
        
        int currentActiveCount = 0;
        for (int i = 0; i < activeAsteroids.length; i++) {
            Asteroid a = activeAsteroids[i];
            
            if (a != null) {
                a.update();
                
                if (!a.isActive()) {
                    activeAsteroids[i] = null; 
                } else {
                    currentActiveCount++;
                }
            }
        }
        
        if (asteroidsSpawned >= maxAsteroidsToSpawn && currentActiveCount == 0 && currentState == RoundState.PLAYING) {
            determineWinner();
        }
    }
    
    private void checkCollisions(Player p) {
        for (int i = 0; i < p.getBullets().size(); i++) {
            Bullet b = p.getBullets().get(i);
            if (!b.isActive()) continue;
            
            for (int j = 0; j < activeAsteroids.length; j++) {
                Asteroid a = activeAsteroids[j];

                if (a != null && a.isActive()) {
                    if (b.getBounds().intersects(a.getBounds())) {
                        b.deactivate();
                        a.destroy();
                        p.incrementScore(100);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null);
        } else {
             g.setColor(Color.LIGHT_GRAY);
             g.fillRect(0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT);
        }
        
        if (currentState == RoundState.READY) {
            if (readyImage != null) {
                int x = (GamePanel.SCREEN_WIDTH - readyImage.getWidth(null)) / 2;
                int y = (GamePanel.SCREEN_HEIGHT - readyImage.getHeight(null)) / 2;
                g.drawImage(readyImage, x, y, null);
            }
            return; 
        }

        g.setColor(Color.WHITE);
        g.drawLine(GamePanel.SCREEN_WIDTH / 2, 0, GamePanel.SCREEN_WIDTH / 2, GamePanel.SCREEN_HEIGHT);
        
        int asteroidsActive = 0;
        for (Asteroid a : activeAsteroids) {
            if (a != null) {
                a.draw(g);
                asteroidsActive++;
            }
        }
        
        p1.draw(g);
        p2.draw(g);
        
        g.setColor(gold);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("ROUND " + roundNumber, 50, 50);
        g.drawString("P1 Score: " + p1.getScore(), 50, 90);
        g.drawString("P2 Score: " + p2.getScore(), GamePanel.SCREEN_WIDTH - 250, 90);
        
        g.drawString("Asteroids Active: " + asteroidsActive, 50, 130);

        long timeElapsed = (System.currentTimeMillis() - stateTimer) / 1000;
        long timeRemaining = ROUND_DURATION_SECONDS - timeElapsed;
        
        g.drawString("Time: " + timeRemaining, GamePanel.SCREEN_WIDTH / 2 - 50, 50);

    }

    @Override
    public void handleInput() {}
    
    public void handleKeyPress(KeyEvent e) {
        if (currentState == RoundState.PLAYING) {
            p1.handleKeyPress(e);
            p2.handleKeyPress(e);
        }
    }
    
    public void handleKeyRelease(KeyEvent e) {
        if (currentState == RoundState.PLAYING) {
            p1.handleKeyRelease(e);
            p2.handleKeyRelease(e);
        }
    }

    public boolean isRoundFinished() {
        return roundFinished;
    }
    
    public int getWinner() {
        return roundWinner; 
    }
    
    public void forceRoundEnd(int winnerID) {
        if (winnerID == 1) {
            p1.incrementScore(1000); 
        } else if (winnerID == 2) {
            p2.incrementScore(1000);
        } 
        roundFinished = true;
        roundWinner = winnerID;
    }
}
