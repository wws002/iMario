import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;

public class Brick extends Sprite
{
  Image brickPic;

  Brick(int _x, int _y, int _w, int _h, Model m)
  {
    super(_x, _y, _w, _h, "Brick", m, 0, 0);

    try
    {
      brickPic = ImageIO.read(new File("brickPic.png"));
    }
    catch(Exception e)
    {
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }

  Brick(Json ob, Model m)
  {
    super((int)ob.getLong("x"), (int)ob.getLong("y"), (int)ob.getLong("w"), (int)ob.getLong("h"), "Brick", m, 0, 0);
    type = (String)ob.getString("type");

    try
    {
      brickPic = ImageIO.read(new File("brickPic.png"));
    }
    catch(Exception e)
    {
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }

  Brick(Brick b, Model m)
  {
    super(b.x, b.y, b.w, b.h, "Brick", m, 0, 0);

  }

  Json marshall()
  {
    Json ob = Json.newObject();
    ob.add("type", "Brick");
    ob.add("x", x);
    ob.add("y", y);
    ob.add("w", w);
    ob.add("h", h);
    return ob;
  }

  Sprite clone(Model newModel)
  {
    return new Brick(this, newModel);
  }

  void update() {}

  void drawMe(int xx, int yy, int ww, int hh, Graphics g, int scrollPos)
  {
    g.drawImage(brickPic, xx - scrollPos, yy, ww, hh, null);
  }
}
