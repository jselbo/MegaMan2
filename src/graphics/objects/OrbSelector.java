package graphics.objects;

import graphics.Animation;
import graphics.Direction;
import graphics.Sprite;

import java.awt.Graphics2D;
import java.awt.Image;

import application.Utility;

public class OrbSelector extends Sprite
{
	private int maxRow, maxCol;
	private int row, col;
	private boolean enabled;
	
	public OrbSelector(int maxRows, int maxCols)
	{
		super(loadAnimations());
		
		maxRow = maxRows - 1;
		maxCol = maxCols - 1;
		
		row = 0;
		col = 0;
		
		enabled = true;
	}
	
	@Override
	public void paint(Graphics2D g2)
	{
		if (enabled)
			super.paint(g2);
	}
	
	private static Animation[] loadAnimations()
	{
		Animation[] anims = new Animation[1];
		
		Animation anim = new Animation();
		String base = "resources/images/overworld/";
		Image[] images = new Image[3];
		for (int i = 0; i < images.length; i++)
			images[i] = Utility.loadImage(base + "orb_selector" + (i+1) + ".png");
		anim.addFrame(images[0], 100);
		anim.addFrame(images[1], 33);
		anim.addFrame(images[2], 33);
		anim.addFrame(null, 33);
		anim.addFrame(images[2], 33);
		anim.addFrame(images[1], 33);
		anims[0] = anim;
		
		return anims;
	}
	
	public void move(Direction direction)
	{
		switch (direction)
		{
		case LEFT:
			col--;
			if (col < 0)
				col = maxCol;
			break;
		case RIGHT:
			col++;
			if (col > maxCol)
				col = 0;
			break;
		case UP:
			row--;
			if (row < 0)
				row = maxRow;
			break;
		case DOWN:
			row++;
			if (row > maxRow)
				row = 0;
			break;
		}
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getColumn()
	{
		return col;
	}
}