import java.awt.Color;
import java.awt.Graphics;

public class Player {

    private int x, y, width, height;

    Asteroid asteroid;

    public Player() {
        x = 500;
        y = 750;
        width = 30;
        height = 30;

    }

    public void drawMe(Graphics g) {
        g.fillOval(x, y, width, height);
    }

    public void move(String direction) {
        if (direction.equals("right")) {
            x += 10;
        }
        if (direction.equals("left")) {
            x -= 10;
        }

        // Colision
        if (x <= 0) {
            x = 0;
        }
        if (x >= 600) {
            x = 600;
        }

    }

    public void checkp1Collisions() {

        asteroid = new Asteroid();

        // Asteroid collision
        if (asteroidCollision(asteroid.getX(), asteroid.getY(), asteroid.getWidth(), asteroid.getLength())) {
            // Should kill/lose life for player
        }
    }

    public boolean asteroidCollision(int x, int y, int width, int height) {

        return false;
    }

}
