import javax.swing.JFrame;
public class Runner {
  
	public static void main(String[] args) {


		JFrame frame = new JFrame("Game Demo");

		//Create panel and add it to the frame
		Screen sc = new Screen();


		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(sc);
		frame.pack();
		frame.setVisible(true);


    }
}
