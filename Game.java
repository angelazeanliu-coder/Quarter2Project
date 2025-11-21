import java.awt.Color;
import java.awt.Graphics;


public class Game {
    private Player p1;
    private Player2 p2;
    public boolean startClicked, level1, level2;

    public Game(){
        p1 = new Player();
        p2 = new Player2();
    }
    
    public void drawMe(Graphics g){
        //background
        //draw the player

        if(level1 || level2){
            p1.drawMe(g);
            p2.drawMe(g);
        }
       
    }

    public void movePlayer(String direction){
        p1.move(direction);
    }
    public void movePlayer2(String direction){
        p2.move(direction);
    }



}
