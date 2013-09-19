package graphics.objects;

import graphics.Animation;
import graphics.Direction;
import graphics.Sprite;
import application.Utility;

public class Bullet extends Sprite
{
	public Bullet(float x, float y, float vx, float vy, Direction direction)
	{
		super(loadAnimations());
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.direction = direction;
	}
	
	private static Animation[] loadAnimations()
	{
		Animation[] anims = new Animation[1];
		
		anims[0] = new Animation();
		anims[0].addFrame(Utility.loadImage("resources/images/megaman/bullet.png"), 10);
		
		return anims;
	}
}