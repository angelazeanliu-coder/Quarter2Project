import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Game {
    
    private static final Color PINK = new Color(221, 161, 187);
    private static final Color GOLD = new Color(233, 213, 174);
    private static final Color PALE_PINK = new Color(236, 201, 200);
    private static final Color PURPLE = new Color(156, 135, 217);
    
    private enum GameState {
        MENU, PLAYING, INSTRUCTIONS, ROUND_OVER, GAME_OVER, TRANSITION 
    }
    private GameState currentState = GameState.MENU;
    private GameState nextState = GameState.MENU; 
    
    private MenuScreen menuScreen;
    private InstructionsScreen instructionsScreen;
    private RoundScreen currentRoundScreen;
    private GameOverScreen gameOverScreen;
    private TransitionScreen transitionScreen; 
    
    private Image roundOverBackground;

    private int currentRound = 1;
    private int p1RoundsWon = 0;
    private int p2RoundsWon = 0;
    
    private long roundOverStartTime;
    private final long ROUND_OVER_DURATION_MS = 3000; 

    private long gameOverStartTime;
    private final long GAME_OVER_DURATION_MS = 5000; 
    
    public Game() {
        menuScreen = new MenuScreen();
        instructionsScreen = new InstructionsScreen(); 
        gameOverScreen = new GameOverScreen();
        transitionScreen = new TransitionScreen(); 
        
        try {
            roundOverBackground = ImageIO.read(new File("GalaxyBG.PNG"));
        } catch (IOException e) {
            System.out.println("Image Load Error Game: GalaxyBG.PNG not found. " + e.getMessage());
        }
    }

    public void updateGame() {
        switch (currentState) {
            case MENU:
                menuScreen.update();
                checkMenuAction();
                break;
            case PLAYING:
                if (currentRoundScreen != null) {
                    currentRoundScreen.update();
                    checkRoundEnd();
                } else {
                    startTransition(GameState.MENU);
                }
                break;
            case INSTRUCTIONS:
                instructionsScreen.update();
                if (instructionsScreen.isTimeUp()) {
                    startTransition(GameState.MENU);
                }
                break;
            case ROUND_OVER:
                if (System.currentTimeMillis() - roundOverStartTime >= ROUND_OVER_DURATION_MS) {
                    checkNextRoundTransition();
                }
                break;
            case GAME_OVER:
                if (System.currentTimeMillis() - gameOverStartTime >= GAME_OVER_DURATION_MS) {
                    startTransition(GameState.MENU); 
                }
                break;
            case TRANSITION:
                transitionScreen.update();
                if (transitionScreen.isTransitionComplete()) {
                    currentState = nextState;
                    if (currentState == GameState.PLAYING) {
                        currentRoundScreen = new RoundScreen(currentRound);
                    } else if (currentState == GameState.ROUND_OVER) {
                        roundOverStartTime = System.currentTimeMillis();
                    } else if (currentState == GameState.GAME_OVER) {
                        gameOverStartTime = System.currentTimeMillis(); 
                    } 
                }
                break;
        }
    }

    public void draw(Graphics2D g2d) {
        switch (currentState) {
            case MENU:
                menuScreen.draw(g2d);
                break;
            case PLAYING:
                if (currentRoundScreen != null) {
                    currentRoundScreen.draw(g2d);
                }
                break;
            case INSTRUCTIONS:
                instructionsScreen.draw(g2d);
                break;
            case ROUND_OVER:
                drawRoundOver(g2d);
                break;
            case GAME_OVER:
                long timeElapsed = System.currentTimeMillis() - gameOverStartTime;
                long timeRemainingSeconds = Math.max(0, (GAME_OVER_DURATION_MS - timeElapsed) / 1000);
                
                gameOverScreen.draw(g2d, p1RoundsWon, p2RoundsWon, (int)timeRemainingSeconds);
                break;
            case TRANSITION:
                transitionScreen.draw(g2d);
                break;
        }
    }
    
    private void startTransition(GameState targetState) {
        nextState = targetState;
        transitionScreen.startTransition();
        currentState = GameState.TRANSITION;
    }
    
    private void checkMenuAction() {
        if (menuScreen.getNextAction() == MenuScreen.MenuAction.START_GAME) {
            currentRound = 1;
            p1RoundsWon = 0;
            p2RoundsWon = 0;
            startTransition(GameState.PLAYING);
            menuScreen.resetAction();
        } else if (menuScreen.getNextAction() == MenuScreen.MenuAction.SHOW_INSTRUCTIONS) {
            instructionsScreen.resetTimer(); 
            startTransition(GameState.INSTRUCTIONS);
            menuScreen.resetAction();
        }
    }
    
    private void checkRoundEnd() {
        if (currentRoundScreen.isRoundFinished()) {
            int winner = currentRoundScreen.getWinner();
            if (winner == 1) {
                p1RoundsWon++;
            } else if (winner == 2) {
                p2RoundsWon++;
            } else if (winner == 0) {
                //handling same scores
                p1RoundsWon++;
                p2RoundsWon++;
            }
            
            startTransition(GameState.ROUND_OVER);
        }
    }

    private void checkNextRoundTransition() {
        if (currentRound == 3) {
            startTransition(GameState.GAME_OVER);
        } else {
            currentRound++;
            startTransition(GameState.PLAYING);
        }
    }
    
    public void drawRoundOver(Graphics g) {
        if (roundOverBackground != null) {
            g.drawImage(roundOverBackground, 0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT, null);
        } else {
            g.setColor(new Color(39, 30, 75)); 
            g.fillRect(0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT);
        }
        
        g.setColor(GOLD); 
        g.setFont(new Font("Arial", Font.BOLD, 80));

        int winnerID = currentRoundScreen.getWinner();
        String resultText;

        if (winnerID == 1) {
            resultText = "P1 WINS!";
        } else if (winnerID == 2) {
            resultText = "P2 WINS!";
        } else { // winnerID == 0 (tie)
            resultText = "DRAW!";
        }
        
        g.drawString("ROUND " + currentRoundScreen.getRoundNumber() + " OVER", 350, 300); 
        
        if (winnerID == 0) {
            g.drawString(resultText, 550, 450); 
        } else {
            g.drawString(resultText, 450, 450);
        }

        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Current Score: P1 - " + p1RoundsWon + " | P2 - " + p2RoundsWon, 450, 600);
    }
        
    public void handleMouseInput(MouseEvent e) {
        if (currentState == GameState.MENU) {
            menuScreen.handleMouseClick(e);
        }
    }
    
    public void handleKeyPress(KeyEvent e) {
        if (currentState == GameState.PLAYING && currentRoundScreen != null) {
            currentRoundScreen.handleKeyPress(e);
        }
        
        //key 9
        if (e.getKeyCode() == KeyEvent.VK_9) {
            if (currentState == GameState.PLAYING) {
                if(currentRoundScreen != null) {
                    currentRoundScreen.forceRoundEnd(1); 
                }
            } else if (currentState == GameState.ROUND_OVER) {
                roundOverStartTime = 0; 
            } else if (currentState == GameState.MENU) {
                currentRound = 3; //make current round 3
                p1RoundsWon = 2; 
                p2RoundsWon = 0; 
                startTransition(GameState.GAME_OVER);
            }
        }
    }
    
    public void handleKeyRelease(KeyEvent e) {
        if (currentState == GameState.PLAYING) {
            currentRoundScreen.handleKeyRelease(e);
        }
    }
}
