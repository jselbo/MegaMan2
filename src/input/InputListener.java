package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class InputListener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	// Key input
	public static final int RELEASED = 0;
	public static final int PRESSED = 1;
	public static final int WAITING_FOR_RELEASE = 2;
	
	private int[] keyStates;
	
	// Mouse input
	private int mouseState;
	private int mouseX, mouseY;
	
	public InputListener()
	{
		keyStates = new int[600];
	}
	
	public void clearKeyStates()
	{
		for (int i = 0; i < keyStates.length; i++)
			keyStates[i] = RELEASED;
	}
	
	/**
	 * Returns the state of the queried key. This method continually returns 
	 * true until the key is released.
	 * @param keyCode a key code defined by KeyEvent
	 * @return true if the key is pressed, false otherwise
	 */
	public boolean softKeyQuery(int keyCode)
	{
		return (keyStates[keyCode] != RELEASED);
	}
	
	/**
	 * Returns the state of the queried key. This method returns true only
	 * on the initial key press. Once a query has been made on a pressed 
	 * key, this method will not return true until the key has been released 
	 * and pressed again.
	 * @param keyCode a key code defined by KeyEvent
	 * @return true if the key is pressed, false otherwise
	 */
	public boolean hardKeyQuery(int keyCode)
	{
		boolean pressed = (keyStates[keyCode] == PRESSED);
		if (pressed)
			keyStates[keyCode] = WAITING_FOR_RELEASE;
		
		return pressed;
	}
	
	public boolean softMouseQuery()
	{
		return (mouseState != RELEASED);
	}
	
	public boolean hardMouseQuery()
	{
		boolean pressed = (mouseState == PRESSED);
		if (pressed)
			mouseState = WAITING_FOR_RELEASE;
		
		return pressed;
	}
	
	public int getMouseX()
	{
		return mouseX;
	}
	
	public int getMouseY()
	{
		return mouseY;
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		if (keyStates[keyCode] == RELEASED)
			keyStates[keyCode] = PRESSED;
		
		e.consume();
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		keyStates[e.getKeyCode()] = RELEASED;
		
		e.consume();
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		e.consume();
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseMoved(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		mouseState = PRESSED;
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		mouseState = RELEASED;
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
		// Do nothing
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		// Do nothing
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		// Do nothing
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		// Do nothing
	}
}