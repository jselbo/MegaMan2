package graphics.objects.bosses;

import graphics.Animation;

import java.awt.Image;

import application.Utility;

public class Heatman extends Boss
{
	public Heatman()
	{
		super(loadAnimations());
	}
	
	private static Animation[] loadAnimations()
	{
		Animation[] anims = new Animation[NUM_STATES];
		
		String base = "resources/images/bosses/heatman/";
		
		Image idle = Utility.loadImage(base + "idle.png");
		
		anims[STATE_IDLE] = new Animation();
		anims[STATE_IDLE].addFrame(idle, 0);
		
		anims[STATE_FALL] = new Animation();
		anims[STATE_FALL].addFrame(Utility.loadImage(base + "fall.png"), 0);
		
		Image[] heats = new Image[4];
		for (int i = 0; i < heats.length; i++)
			heats[i] = Utility.loadImage(base + "heat" + (i+1) + ".png");
		anims[STATE_TAUNT] = new Animation(false);
		anims[STATE_TAUNT].addFrame(idle, 1000);
		anims[STATE_TAUNT].addFrame(heats[0], 67);
		anims[STATE_TAUNT].addFrame(heats[1], 33);
		anims[STATE_TAUNT].addFrame(heats[2], 33);
		anims[STATE_TAUNT].addFrame(heats[3], 33);
		anims[STATE_TAUNT].addFrame(heats[2], 33);
		anims[STATE_TAUNT].addFrame(heats[3], 33);
		anims[STATE_TAUNT].addFrame(heats[2], 33);
		anims[STATE_TAUNT].addFrame(heats[3], 33);
		anims[STATE_TAUNT].addFrame(heats[2], 33);
		anims[STATE_TAUNT].addFrame(heats[3], 33);
		anims[STATE_TAUNT].addFrame(heats[2], 33);
		anims[STATE_TAUNT].addFrame(heats[3], 33);
		anims[STATE_TAUNT].addFrame(heats[2], 33);
		anims[STATE_TAUNT].addFrame(heats[3], 33);
		anims[STATE_TAUNT].addFrame(heats[2], 33);
		anims[STATE_TAUNT].addFrame(heats[3], 33);
		anims[STATE_TAUNT].addFrame(heats[2], 33);
		anims[STATE_TAUNT].addFrame(heats[3], 33);
		anims[STATE_TAUNT].addFrame(heats[1], 100);
		
		return anims;
	}
	
	@Override
	public String getName()
	{
		return "CHALLENGE 1";
	}
}