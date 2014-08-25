package graphics.objects.enemies;

import graphics.Animation;
import graphics.Direction;
import application.GameState;
import application.Utility;

public class Spring extends Enemy
{
	public static final int STATE_IDLE = 1;
	public static final int STATE_BOUNCING = 2;
	
	private int type;
	private long time;
	private long goTime;
	private float velocity;
	
	public Spring(int y, Direction direction, int type, int difficulty, long goTime)
	{
		super(loadAnimations(type));
		
		setState(STATE_IDLE);
		
		if (direction == Direction.RIGHT)
			x = 2;
		else
			x = 510 - this.getWidth();
		this.y = y;
		this.direction = direction;
		this.type = type;
		
		if (type == 0)
		{
			if (difficulty == GameState.DIFFICULT)
				velocity = 0.6f;
			else
				velocity = 0.3f;
		}
		else
		{
			if (difficulty == GameState.DIFFICULT)
				velocity = 0.5f;
			else
				velocity = 0.2f;
		}
		
		this.goTime = goTime;
	}
	
	public int getType()
	{
		return type;
	}
	
	private static Animation[] loadAnimations(int type)
	{
		Animation[] anims = new Animation[3];
		
		String base = "res/images/enemies/";
		anims[STATE_EXPLODING] = new Animation(false);
		anims[STATE_EXPLODING].addFrame(Utility.loadImage(base + "explode1.png"), 200);
		anims[STATE_EXPLODING].addFrame(Utility.loadImage(base + "explode2.png"), 200);
		
		char c;
		if (type == 0)
			c = 'a';
		else
			c = 'b';
		anims[STATE_IDLE] = new Animation();
		anims[STATE_IDLE].addFrame(Utility.loadImage(base + "spring" + c + "1.png"), 100);
		
		anims[STATE_BOUNCING] = new Animation();
		anims[STATE_BOUNCING].addFrame(Utility.loadImage(base + "spring" + c + "1.png"), 177);
		anims[STATE_BOUNCING].addFrame(Utility.loadImage(base + "spring" + c + "2.png"), 177);
		anims[STATE_BOUNCING].addFrame(Utility.loadImage(base + "spring" + c + "3.png"), 177);
		anims[STATE_BOUNCING].addFrame(Utility.loadImage(base + "spring" + c + "2.png"), 177);
		anims[STATE_BOUNCING].addFrame(Utility.loadImage(base + "spring" + c + "1.png"), 177);
		anims[STATE_BOUNCING].addFrame(Utility.loadImage(base + "spring" + c + "4.png"), 177);
		anims[STATE_BOUNCING].addFrame(Utility.loadImage(base + "spring" + c + "5.png"), 177);
		anims[STATE_BOUNCING].addFrame(Utility.loadImage(base + "spring" + c + "4.png"), 177);
		
		return anims;
	}
	
	@Override
	public void update(long elapsedTime)
	{
		super.update(elapsedTime);
		
		if (getState() != STATE_IDLE)
			return;
		
		if (time >= goTime)
		{
			setState(STATE_BOUNCING);
			vx = direction == Direction.RIGHT ? velocity : -velocity;
		}
		else
			time += elapsedTime;
	}

	@Override
	public String toString()
	{
		return "Spring";
	}
}