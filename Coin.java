import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;

public class Coin extends Sprite
{
  Image coin;

  Coin(double _vvel, double _hvel, int _x, int _y, Model m)
  {
    super(_x, _y, 0, 0, "Coin", m, _vvel, _hvel);//coin does not have width or height

    try
    {
      coin = ImageIO.read(new File("coin.png"));
    }
    catch(Exception e)
    {
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }

  Coin(Coin c, Model m)
  {
    super(c.x, c.y, 0, 0, "Coin", m, c.vvel, c.hvel);
  }

  Json marshall()
  {
    Json ob = Json.newObject();
    ob.add("type", "Coin");
    return ob;
  }

  Sprite clone(Model newModel)
  {
    return new Coin(this, newModel);
  }

  void update()
  {
    if(y > 1000)
    {
      model.killMeNow(this);
    }

    vvel+=3.14159;
    y += vvel;
    x += hvel;
  }

  void drawMe(int x, int y, int w, int h, Graphics g, int scrollPos)
  {
    g.drawImage(coin, x - scrollPos, y, null);
  }
}
