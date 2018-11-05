import java.util.ArrayList;
import java.util.Iterator;

enum Action
{ run, runLeft, jump }

class Model
{
  int scrollPos;
  int backgroundPos = -200;
  int d = 30, k = 8;
  Mario mario;
  ArrayList<Sprite> sprites;

	Model()
	{
    sprites = new ArrayList<Sprite>();
	}

  Model(Model otherModel)
  {
    scrollPos = otherModel.scrollPos;
    backgroundPos = otherModel.backgroundPos;
    d = otherModel.d; k = otherModel.k;
    mario = new Mario(otherModel.mario, this);

    this.sprites = new ArrayList<Sprite>();
    sprites.add(mario);

    Iterator<Sprite> it = otherModel.sprites.iterator();
    while(it.hasNext())
    {
	     Sprite s = it.next();
       if(!s.type.equals("Mario"))
	      this.sprites.add(s.clone(this));
    }
  }

	void update()
	{
    for(int i = 0; i < sprites.size(); i++)
    {
      Sprite s = sprites.get(i);
      s.update();
    }
	}

  double evaluateAction(Action action, int depth)
  {
  	// Evaluate the state
  	if(depth >= d)
  		return mario.x + 5000 * mario.myCoins - 100*mario.jc;

  	// Simulate the action
  	Model copy = new Model(this); // uses the copy constructor
  	copy.doAction(action); // like what Controller.update did before
  	copy.update(); // advance simulated time

  	// Recurse
  	if(depth % k != 0)
  	   return copy.evaluateAction(action, depth + 1);
  	else
  	{
  	   double best = copy.evaluateAction(Action.run, depth + 1);
       best = Math.max(best, copy.evaluateAction(Action.jump, depth + 1));
  	   best = Math.max(best, copy.evaluateAction(Action.runLeft, depth + 1));
  	   return best;
  	}
  }

  void doAction(Action a)
  {
    mario.notePrevious();
    mario.runningLeft = false;
    mario.runningRight = false;

    if(a == Action.run)
    {
      if(mario.sideCollide == false)
        backgroundPos--;

      mario.runningRight = true;
    }

    if(a == Action.runLeft)
    {
      if(mario.sideCollide == false)
        backgroundPos++;

      mario.runningLeft = true;
    }

    if(a == Action.jump)
    {
      if(mario.onTop == true || mario.y == 700)
      {
        if (mario.vvel == 0 || mario.vvel < 0)
        {
          mario.vvel = -35;
        }
      }
    }
  }

  void killMeNow(Sprite s)
  {
    sprites.remove(s);
  }

  void unmarshall(Json ob)
  {
    Json json_sprites = ob.get("sprites");
    for(int i = 0; i < json_sprites.size(); i++)
    {
      Json j = json_sprites.get(i);
      Sprite s; String string;
      string = j.getString("type");
      if (string.equals("Brick"))
        s = new Brick(j, this);
      else if (string.equals("CoinBlock"))
        s = new CoinBlock(j, this);
      else
      {
        mario = new Mario(j, this);
        s = mario;
      }

      sprites.add(s);
    }
  }

  Json marshall()
  {
    Json ob = Json.newObject();
    Json json_sprites = Json.newList();
    ob.add("sprites", json_sprites);
    for(int i = 0; i < sprites.size(); i++)
    {
      Sprite s = sprites.get(i);
      String string;
      Json j = s.marshall();
      string = j.getString("type");
      if(string != "Coin")
        json_sprites.add(j);
    }
    return ob;
  }

  void save(String filename)
  {
    Json ob = marshall();
    ob.save(filename);
  }

  void load(String filename)
  {
    Json ob = Json.load(filename);
    unmarshall(ob);
  }
}
