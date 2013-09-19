package graphics.objects.bosses;

import graphics.Animation;

import java.awt.Image;

import application.Utility;

public class Airman extends Boss
{
	public Airman()
	{
		super(loadAnimations());
	}
	
	private static Animation[] loadAnimations()
	{
		Animation[] anims = new Animation[NUM_STATES];
		
		String base = "resources/images/bosses/airman/";
		
		Image idle = Utility.loadImage(base + "idle.png");
		Image blow1 = Utility.loadImage(base + "blow1.png");
		Image blow2 = Utility.loadImage(base + "blow2.png");
		
		anims[STATE_IDLE] = new Animation();
		anims[STATE_IDLE].addFrame(idle, 0);
		
		anims[STATE_FALL] = new Animation();
		anims[STATE_FALL].addFrame(Utility.loadImage(base + "fall.png"), 0);
		
		anims[STATE_TAUNT] = new Animation(false);
		anims[STATE_TAUNT].addFrame(idle, 1000);
		anims[STATE_TAUNT].addFrame(blow1, 50);
		anims[STATE_TAUNT].addFrame(blow2, 50);
		anims[STATE_TAUNT].addFrame(blow1, 50);
		anims[STATE_TAUNT].addFrame(blow2, 50);
		anims[STATE_TAUNT].addFrame(idle, 300);
		anims[STATE_TAUNT].addFrame(blow1, 50);
		anims[STATE_TAUNT].addFrame(blow2, 50);
		anims[STATE_TAUNT].addFrame(blow1, 50);
		anims[STATE_TAUNT].addFrame(blow2, 50);
		anims[STATE_TAUNT].addFrame(idle, 1000);
		
		return anims;
	}
	
	@Override
	public String getName()
	{
		return "Airman";
	}
}