package graphics.objects;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import application.Utility;

public class Remix
{
	private Image img;
	private int x, y;
	private float cycle;
	private long time;
	
	public Remix(int x, int y)
	{
		img = Utility.loadImage("resources/images/overworld/remix.png");
		this.x = x;
		this.y = y;
	}
	
	public void update(long elapsedTime)
	{
		time += elapsedTime;
		cycle = 0.1f*(float)Math.sin(0.00495*time)+0.9f;
	}
	
	public void paint(Graphics2D g2)
	{
		AffineTransform at = new AffineTransform();
		at.translate(x, y);
		at.scale(cycle, cycle);
		g2.drawImage(img, at, null);
	}
}