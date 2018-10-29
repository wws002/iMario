import java.awt.Graphics;

abstract class Sprite
{
  boolean onTop, wasOnTop, wasOnGround;
  int x, y, w, h;
  int prev_x, prev_y;
  int myCoins;
  double vvel, hvel;
  double prev_vvel, prev_hvel;
  String type;
  Model model;

  Sprite(int xx, int yy, int ww, int hh, String _type, Model _model, double _vvel, double _hvel)
  {
    x = xx;
    y = yy;
    w = ww;
    h = hh;
    vvel = _vvel;
    hvel = _hvel;
    type = _type;
    model = _model;
  }

  public void notePrevious()
  {
    prev_x = x;
    prev_y = y;
    prev_vvel = vvel;
    if(onTop)
      wasOnTop = true;
    if(y == 700)
      wasOnGround = true;
  }

  boolean collision(int _x, int _y, int _w, int _h)
  {
    if (x + w <= _x)
      return false;
    if (x >= _x + _w)
      return false;
    if (y + h <= _y)
      return false;
    if (y >= _y + _h)
      return false;

    return true;
  }

  abstract Sprite clone(Model newModel);
  abstract void update();
  abstract Json marshall();
  abstract void drawMe(int x, int y, int w, int h, Graphics g, int scrollPos);
}
