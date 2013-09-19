package graphics;

import java.awt.Image;

public abstract class ImageContainer
{
	private final Image image;
	private float xOffset, yOffset;
	
	public ImageContainer(Image image, float xOffset, float yOffset)
	{
		this.image = image;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public Image image()
	{
		return image;
	}
	
	public float xOffset()
	{
		return xOffset;
	}
	
	public float yOffset()
	{
		return yOffset;
	}
}