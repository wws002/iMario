import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class Controller implements ActionListener, MouseListener, KeyListener
{
  View view;
  Model model;
  boolean keyLeft;
  boolean keyRight;
  boolean keyUp;
  boolean keyC;
  boolean keyD;
  int mouseDownX;
  int mouseDownY;

	Controller(Model m)
	{
    model = m;
	}

  public void mouseEntered(MouseEvent e)     {}
	public void mouseExited(MouseEvent e)      {}
	public void mouseClicked(MouseEvent e)     {}
	public void keyTyped(KeyEvent e)           {}
	public void actionPerformed(ActionEvent e) {}

	void setView(View v)
	{
    view = v;
  }

  public void mousePressed(MouseEvent e)
	{
    mouseDownX = e.getX();
	  mouseDownY = e.getY();
	}

	public void mouseReleased(MouseEvent e)
	{
    if(keyC && !keyD)//make a coin block
    {
      Sprite s = new CoinBlock(mouseDownX + model.scrollPos, mouseDownY, 89, 83, model);
      model.sprites.add(s);
    }

    else if(keyD)//delete a sprite
    {
      for(int i = 0; i < model.sprites.size(); i++)
      {
        Sprite s = model.sprites.get(i);
        if(mouseDownX + model.scrollPos > s.x && mouseDownX + model.scrollPos < s.x + s.w && s.type != "Mario")
          if(mouseDownY > s.y && mouseDownY < s.y + s.h)
            model.sprites.remove(s);
      }
    }

    else//make a brick
    {
      int x1 = mouseDownX;
      int x2 = e.getX();
      int y1 = mouseDownY;
      int y2 = e.getY();
      int left = Math.min(x1, x2);
      int right = Math.max(x1, x2);
      int top = Math.min(y1, y2);
      int bottum = Math.max(y1, y2);

      Sprite s = new Brick(left + model.scrollPos, top, right - left, bottum - top, model);
      model.sprites.add(s);
    }
  }

	public void keyPressed(KeyEvent e)
	{
    switch(e.getKeyCode())
		{
      case KeyEvent.VK_RIGHT: keyRight = true; break;
	  	case KeyEvent.VK_LEFT: keyLeft = true; break;
		  case KeyEvent.VK_S: model.save("sprites.json"); System.out.println("game saved"); break;
		  case KeyEvent.VK_C: keyC = true; break;
      case KeyEvent.VK_D: keyD = true; break;
      case KeyEvent.VK_UP: 
      if(model.mario.onTop == true || model.mario.y == 700)
      {
        keyUp = true;
      } break;
		}
	}

	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: keyRight = false; break;
			case KeyEvent.VK_LEFT: keyLeft = false; break;
			case KeyEvent.VK_UP: keyUp = false; break;
			case KeyEvent.VK_C: keyC = false; break;
      case KeyEvent.VK_D: keyD = false; break;
		}
	}

	void update()
	{
    model.mario.notePrevious();
    model.mario.runningLeft = false;
    model.mario.runningRight = false;

    if(keyRight)//make mario run right
		{
      if(model.mario.sideCollide == false)
        model.backgroundPos--;

      model.mario.runningRight = true;
    }

		if(keyLeft)//make Mario run left
		{
      if(model.mario.sideCollide == false)
        model.backgroundPos++;

      model.mario.runningLeft = true;
    }

		if(keyUp)//make mario jump
		{
      if(model.mario.onTop == true || model.mario.y == 700)
      {
        if (model.mario.vvel == 0 || model.mario.vvel < 0)
        {
          model.mario.vvel = -35;
        }
      }
		}
  }

  void aiUpdate()
  {
    double score_run = model.evaluateAction(Action.run, 0);
    //System.out.println("run "+score_run);
    double score_runLeft = model.evaluateAction(Action.runLeft, 0);
    //System.out.println("runLeft "+score_runLeft);
    double score_jump = model.evaluateAction(Action.jump, 0);
    //System.out.println("jump "+score_jump);

    if(score_run >= score_runLeft && score_run > score_jump)
      model.doAction(Action.run);
    else if(score_jump >= score_runLeft)
      model.doAction(Action.jump);
    else
      model.doAction(Action.runLeft);
  }
}
