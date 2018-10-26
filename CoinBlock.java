import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Random;

public class CoinBlock extends Sprite
{
  Image block;
  Image emptyBlock;
  int coinCount = 5;

  CoinBlock(int _x, int _y, int _w, int _h, Model m)
  {
    super(_x, _y, _w, _h, "CoinBlock", m, 0, 0);//vvel and hvvel are always 0

    try
    {
      block = ImageIO.read(new File("coinBlock2.png"));
      emptyBlock = ImageIO.read(new File("emptyCoinBlock2.png"));
    }
    catch(Exception e)
    {
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }

  CoinBlock(Json ob, Model m)
  {
    super((int)ob.getLong("x"), (int)ob.getLong("y"), (int)ob.getLong("w"), (int)ob.getLong("h"), (String)ob.getString("type"), m, 0, 0);

    try
    {
      block = ImageIO.read(new File("coinBlock2.png"));
      emptyBlock = ImageIO.read(new File("emptyCoinBlock2.png"));
    }
    catch(Exception e)
    {
      e.printStackTrace(System.err);
      System.exit(1);
    }
  }

  CoinBlock(CoinBlock cb, Model m)
  {
    super(cb.x, cb.y, cb.w, cb.h, "CoinBlock", m, 0, 0);
    coinCount = cb.coinCount;
  }

  Json marshall()
  {
    Json ob = Json.newObject();
    ob.add("type", "CoinBlock");
    ob.add("x", x);
    ob.add("y", y);
    ob.add("w", w);
    ob.add("h", h);
    return ob;
  }

  Sprite clone(Model newModel)
  {
    return new CoinBlock(this, newModel);
  }

  void update()
  {
    //if mario's head bumps the bottum of the block
    if (model.mario.y == y + h && model.mario.x + model.mario.w > x && model.mario.x < x + w)
    {
      //increment myCoins
      for (int i = 0; i < model.sprites.size(); i++)
      {
        Sprite s = model.sprites.get(i);
        if (s.type.equals("Mario") && coinCount > 0)
          s.myCoins++;
      }

      Random rand = new Random();
      model.mario.vvel = 0.1;
      if(coinCount > 0)
      {
        double vvelocity = -30.0, hvelocity = 0.0;
        double n = rand.nextInt(50) + 1;
        if(n%2 == 0)
          hvelocity = n%15.0;
        else
          hvelocity = -n%15.0;

        Sprite s = new Coin(vvelocity, hvelocity, x, y - 40, model);
        model.sprites.add(s);
        coinCount--;
      }
    }
  }

  void drawMe(int x, int y, int w, int h, Graphics g, int scrollPos)
  {
    if(coinCount > 0)
      g.drawImage(block, x - model.scrollPos, y, 89, 83, null);
    else
      g.drawImage(emptyBlock, x - model.scrollPos, y, 89, 83, null);
  }
}
