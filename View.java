import javax.swing.JPanel;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

class View extends JPanel
{
	Model model;
	Controller controller;
	Image background = null;

	View(Controller c, Model m)
	{
    model = m;
    controller = c;
    controller.setView(this);

    try
    {
      background = ImageIO.read(new File("background.png"));
    }
    catch(Exception e)
    {
      e.printStackTrace(System.err);
      System.exit(1);
    }
	}

  public void paintComponent(Graphics g)
  {
    //clear screen
    g.setColor(new Color(128, 255, 255));
    g.fillRect(0, 0, this.getWidth(), this.getHeight());

    //draw the background
    g.drawImage(background, model.backgroundPos, -50, 1600, 1200, null);

    //draw the ground
    g.setColor(new Color(50, 200, 64));
    g.fillRect(0, 795, 2500, 595);

    //draw the sprites
    for (int i = 0; i < model.sprites.size(); i++)
    {
      Sprite s = model.sprites.get(i);
      s.drawMe(s.x, s.y, s.w, s.h, g, model.scrollPos);
    }

		//congradulate the user if they've won
		if(model.scrollPos > 4000 && model.mario.myCoins == 20)
		{
			g.setColor(new Color(0, 0, 0));
	    g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
	    g.drawString("you did it!", 100, 100);
		}
  }
}
