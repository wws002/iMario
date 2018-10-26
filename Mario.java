import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.Font;

class Mario extends Sprite
{
  int mframe;
  int jc;
  boolean facingRight = true;
  boolean sideCollide = false;
  boolean runningRight = false;
  boolean runningLeft = false;

  Image[] marios = null;

  Mario(Json ob, Model m)
  {
    super((int)ob.getLong("x"), (int)ob.getLong("y"), 60, 95, "Mario", m, 0, 0);
    model.scrollPos = x - 470;

    if(marios == null)
    {
      marios = new Image[10];
      try
      {
        marios[0] = ImageIO.read(new File("mario1.png"));
        marios[1] = ImageIO.read(new File("mario2.png"));
        marios[2] = ImageIO.read(new File("mario3.png"));
        marios[3] = ImageIO.read(new File("mario4.png"));
        marios[4] = ImageIO.read(new File("mario5.png"));
        marios[5] = ImageIO.read(new File("mario1left.png"));
        marios[6] = ImageIO.read(new File("mario2left.png"));
        marios[7] = ImageIO.read(new File("mario3left.png"));
        marios[8] = ImageIO.read(new File("mario4left.png"));
        marios[9] = ImageIO.read(new File("mario5left.png"));
      }
      catch(Exception e)
      {
        e.printStackTrace(System.err);
        System.exit(1);
      }
    }
  }

  Mario(Mario oldMario, Model model)
  {
    super(oldMario.x, oldMario.y, oldMario.w, oldMario.h, "Mario", model, oldMario.vvel, oldMario.hvel);
    onTop = oldMario.onTop;
    wasOnTop = oldMario.wasOnTop;
    wasOnGround = oldMario.wasOnGround;
    prev_x = oldMario.prev_x;
    prev_y = oldMario.prev_y;
    myCoins = oldMario.myCoins;
    prev_vvel = oldMario.prev_vvel;
    prev_hvel = oldMario.prev_hvel;
    mframe = oldMario.mframe;
    jc = oldMario.jc;
    facingRight = oldMario.facingRight;
    sideCollide = oldMario.sideCollide;
    runningRight = oldMario.runningRight;
    runningLeft = oldMario.runningLeft;
    model.scrollPos = oldMario.x - 470;
  }

  Json marshall()
  {
    Json ob = Json.newObject();
    ob.add("type", "Mario");
    ob.add("x", x);
    ob.add("y", y);
    return ob;
  }

  Sprite clone(Model newModel)
  {
    return new Mario(this, newModel);
  }

  void leaveBlock(int _x, int _y, int _w, int _h)
  {
    if (y + h > _y && prev_y + h <= _y) //collision from the top
    {
      vvel = 0;
      y = _y - h;
      onTop = true;
    }
    if (y < _y + _h && prev_y >= _y + _h) //collision from the bottum
    {
      y = _y + _h;
      vvel = 0.1;
    }
    if (x + w > _x && prev_x + w <= _x) //collision from the left
    {
      sideCollide = true;
      runningRight = false;
      model.scrollPos = _x - 470 - w;
      x = _x - w;
    }
    if ( x < _x + _w && prev_x >= _x + _w) //collision from the right
    {
      sideCollide = true;
      runningLeft = false;
      model.scrollPos = _x - 470 + _w;
      x = _x + _w;
    }
  }

  void update()
  {
    onTop = false;
    sideCollide = false;
    vvel += 3.14159;
    y += vvel;

    if((wasOnTop || wasOnGround) && vvel < 0)
      jc++;
    wasOnTop = false;
    wasOnGround = false;

    if(runningRight)
    {
      x+=12;
      facingRight = true;
      model.scrollPos += 12;
    }

    if(runningLeft)
    {
      x-=12;
      facingRight = false;
      model.scrollPos -= 12;
    }

    if(y > 700)//if he's on the ground, put him there
    {
      y = 700;
      vvel = 0;
    }

    // collision detection
    for(int i = 0; i < model.sprites.size(); i++)
    {
      Sprite s = model.sprites.get(i);
      if(x + w == s.x || x == s.x + s.w)
        sideCollide = true;

      if(s.type != "Mario" && s.type != "Coin")
    	  if (collision(s.x, s.y, s.w, s.h))
    		  leaveBlock(s.x, s.y, s.w, s.h);
    }
  }


  void drawMe(int x, int y, int w, int h, Graphics g, int scrollPos)
  {
    //draw myCoins
    g.setColor(new Color(0, 0, 0));
    g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
    g.drawString("my coins: "+myCoins, 10, 30);

    //animate mario
	  if (facingRight) //if he's facing right, animate right
	  {
	    mframe++; if (mframe > 4) mframe = 0;

      //if keyRight and mario is on a surface, draw him running
	    if(runningRight && (onTop || y == 700))
	      g.drawImage(marios[mframe], 470, y, null);

	    //else draw him standing still
	    else g.drawImage(marios[3], 470, y, null);
	  }
	  else //if he's facing left,  animate left
	  {
	    mframe++; if (mframe < 5 || mframe > 9) mframe = 5;

	    //if keyLeft and mario is on a surface, draw him running
	    if(runningLeft && (onTop || y == 700))
	      g.drawImage(marios[mframe], 470, y, null);

	    //else draw him standing still
	    else g.drawImage(marios[8], 470, y, null);
	  }
  }
}
