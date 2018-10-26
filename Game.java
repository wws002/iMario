import javax.swing.JFrame;
import java.awt.Toolkit;

public class Game extends JFrame
{
  Model model = new Model();
  Controller controller = new Controller(model);
  View view = new View(controller, model);

	public Game()
	{
		this.setTitle("Mario");
		this.setSize(1000, 1000);
		this.setFocusable(true);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		view.addMouseListener(controller);
		this.addKeyListener(controller);
	}

  public void run()
  {
    model.load("sprites.json");
    while(true)
    {
      controller.aiUpdate();
      //controller.update();
      model.update();
      view.repaint(); // Indirectly calls View.paintComponent
      Toolkit.getDefaultToolkit().sync(); // Updates screen

      // Go to sleep for 30 miliseconds
      try
      {
        Thread.sleep(30);
      }
      catch(Exception e)
      {
        e.printStackTrace();
        System.exit(1);
      }
    }
  }

  public static void main(String[] args)
	{
		Game g = new Game();
		g.run();
	}
}
