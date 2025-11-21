import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Screen extends JPanel implements KeyListener, ActionListener {

    Game game;
    public boolean p1_left = false;
    public boolean p1_right = false;
    public boolean p2_left = false;
    public boolean p2_right = false;


    private int clickCount = 0;

    public Screen(){
        game = new Game();

        setLayout(null);
        

        addKeyListener(this);
        setFocusable(true);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1400,960);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.drawMe(g);
    } 

    public void keyPressed(KeyEvent e){

    if (e.getKeyCode() == 39) p2_right = true;
    if (e.getKeyCode() == 37) p2_left = true;
    if (e.getKeyCode() == 68) p1_right = true;
    if (e.getKeyCode() == 65) p1_left = true;
 

    if (p1_right) {
        game.movePlayer( "right");
    } else if (p1_left) {
        game.movePlayer( "left");
    }
    
    if (p2_right) {
        game.movePlayer2("right");
    } else if (p2_left) {
        game.movePlayer2( "left");
    }

    repaint();
}

    public void keyReleased(KeyEvent e){
    if (e.getKeyCode() == 39) p2_right = false;
    if (e.getKeyCode() == 37) p2_left = false;
    if (e.getKeyCode() == 68) p1_right = false;
    if (e.getKeyCode() == 65) p1_left = false;
    
}

    public void keyTyped(KeyEvent e){

    }
    
    public void actionPerformed(ActionEvent e) {
       
    }
}