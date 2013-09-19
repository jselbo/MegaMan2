package graphics;

import graphics.TextAnimation.AnimFrame;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * Derived from the Sprite class
 */
public class TextSprite
{
    private TextAnimation anim;
    private Point2D[] offsets;
    private Font font;
    protected float x, y;
    private boolean enabled;
    
    public TextSprite(TextAnimation anim)
    {
    	this(anim, null);
    }
    
    public TextSprite(TextAnimation anim, Font font)
    {
    	setAnimation(anim);
    	this.font = font;
    	enabled = true;
    }
    
    public void setAnimation(TextAnimation anim)
    {
    	setAnimation(anim, null);
    }
    
    public void setAnimation(TextAnimation anim, Point2D[] offsets)
    {
    	this.anim = anim;
    	this.offsets = offsets;
    }
    
    public TextAnimation getAnimation()
    {
    	return anim;
    }
    
    public void update(long elapsedTime)
    {
        int initFrame = anim.getCurrentIndex();
        anim.update(elapsedTime);
        int finalFrame = anim.getCurrentIndex();
        if (offsets != null && initFrame != finalFrame)
        {
        	x += (float)offsets[finalFrame].getX() - (float)offsets[initFrame].getX();
        	y += (float)offsets[finalFrame].getY() - (float)offsets[initFrame].getY();
        }
    }
    
    public void paint(Graphics2D g2)
    {
    	if (enabled)
    	{
	    	AnimFrame frame = anim.getFrame();
	    	if (frame != null)
	    	{
	    		if (font != null)
	    			g2.setFont(font);
	    		g2.setColor(frame.color);
	    		g2.drawString(anim.getText(), x, y);
	    	}
    	}
    }
    
    public void setLocation(float x, float y)
    {
    	this.x = x;
    	this.y = y;
    }
    
    public void setLocation(Point2D location)
    {
    	if (location != null)
    	{
    		this.x = (float)location.getX();
    		this.y = (float)location.getY();
    	}
    }
    
    public Point2D getLocation()
    {
    	return new Point2D.Float(x, y);
    }
    
    public void setX(float x)
    {
        this.x = x;
    }
    
    public float getX()
    {
        return x;
    }
    
    public void setY(float y)
    {
        this.y = y;
    }
    
    public float getY()
    {
        return y;
    }
    
    public AnimFrame getFrame()
    {
        return anim.getFrame();
    }
    
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	@Override
    public Object clone()
    {
        return new TextSprite(anim, font);
    }
}