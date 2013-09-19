package graphics.objects.enemies;

import graphics.Animation;
import graphics.Sprite;

public abstract class Enemy extends Sprite
{
	public static int STATE_EXPLODING = 0;
	
	public Enemy(Animation[] anims)
	{
		super(anims);
	}
	
	public abstract String toString();
}