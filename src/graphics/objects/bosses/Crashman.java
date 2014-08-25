package graphics.objects.bosses;

import graphics.Animation;

import java.awt.Image;

import application.Utility;

public class Crashman extends Boss
{
	public Crashman()
	{
		super(loadAnimations());
	}
	
	private static Animation[] loadAnimations()
	{
		Animation[] anims = new Animation[NUM_STATES];
		
		String base = "res/images/bosses/crashman/";
		
		Image idle = Utility.loadImage(base + "idle.png");
		
		anims[STATE_IDLE] = new Animation();
		anims[STATE_IDLE].addFrame(idle, 0);
		
		anims[STATE_FALL] = new Animation();
		anims[STATE_FALL].addFrame(Utility.loadImage(base + "fall.png"), 0);
		
		anims[STATE_TAUNT] = new Animation(false);
		anims[STATE_TAUNT].addFrame(idle, 1000);
		anims[STATE_TAUNT].addFrame(Utility.loadImage(base + "pose1.png"), 300);
		anims[STATE_TAUNT].addFrame(Utility.loadImage(base + "pose2.png"), 100);
		
		return anims;
	}
	
	@Override
	public String getName()
	{
		return "Crashman";
	}
}