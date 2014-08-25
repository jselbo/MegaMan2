package graphics.objects;

import graphics.Direction;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.Arrays;

import application.Utility;

public class PasswordGrid
{
	private static final char[] ALPHABET = 
		{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
		  'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
		  'U', 'V', 'W', 'X', 'Y', 'Z' };
	
	private OrbSelector selector;
	
	private Image orb;
	private Image grid;
	
	private Point corner;
	
	private boolean[][] pwGrid;
	private int numOrbs;
	private int maxOrbs;
	
	public PasswordGrid(int rows, int cols, int maxOrbs)
	{
		this.maxOrbs = maxOrbs;
		
		pwGrid = new boolean[rows][cols];
		
		selector = new OrbSelector(rows, cols);
		
		String imageBase = "res/images/overworld/";
		orb = Utility.loadImage(imageBase + "orb.png");
		grid = Utility.loadImage(imageBase + "grid.png");
		
		numOrbs = 0;
	}
	
	public void paint(Graphics2D g2)
	{
		g2.setColor(Color.white);
		
		// Draw letters
		for (int i = 0; i < pwGrid.length; i++)
		{
			int x = corner.x - 2 * Utility.SPRITE_SIZE;
			int y = (int)((float)Utility.SPRITE_SIZE * (2*i + 1.5f)) + corner.y;
			char c = ALPHABET[i % ALPHABET.length];
			g2.drawString(Character.toString(c), x, y);
		}
		
		// Draw numbers
		for (int i = 0; i < pwGrid[0].length; i++)
		{
			int x = (int)((float)Utility.SPRITE_SIZE * (2*i + 0.5f)) + corner.x;
			int y = corner.y - Utility.SPRITE_SIZE;
			g2.drawString(Integer.toString(i+1), x, y);
		}
		
		// Draw grid and orbs
		for (int i = 0; i < pwGrid.length; i++)
		{
			for (int j = 0; j < pwGrid[i].length; j++)
			{
				int gridX = 2 * Utility.SPRITE_SIZE * j + corner.x;
				int gridY = 2 * Utility.SPRITE_SIZE * i + corner.y;
				g2.drawImage(grid, gridX, gridY, null);
				if (pwGrid[i][j])
					g2.drawImage(orb, gridX + 2, gridY + 2, null);
			}
		}
		
		selector.paint(g2);
	}
	
	public void update(long elapsedTime)
	{
		selector.update(elapsedTime);
	}
	
	public void addOrb()
	{
		if (numOrbs < maxOrbs)
		{
			int row = selector.getRow(), column = selector.getColumn();
			if (!pwGrid[row][column])
			{
				pwGrid[row][column] = true;
				numOrbs++;
			}
		}
	}
	
	public void removeOrb()
	{
		int row = selector.getRow(), column = selector.getColumn();
		if (pwGrid[row][column])
		{
			pwGrid[row][column] = false;
			numOrbs--;
		}
	}
	
	public int numOrbs()
	{
		return numOrbs;
	}
	
	public int maxOrbs()
	{
		return maxOrbs;
	}
	
	public void setSelectorEnabled(boolean enabled)
	{
		selector.setEnabled(enabled);
	}
	
	public void setCorner(Point corner)
	{
		this.corner = corner;
		
		selector.setPosition(corner.x + 2 * Utility.SPRITE_SIZE * selector.getColumn() + 4,
				corner.y + 2 * Utility.SPRITE_SIZE * selector.getRow() + 4);
	}
	
	public void move(Direction direction)
	{
		selector.move(direction);
		
		selector.setPosition(corner.x + 2 * Utility.SPRITE_SIZE * selector.getColumn() + 4,
				corner.y + 2 * Utility.SPRITE_SIZE * selector.getRow() + 4);
	}
	
	public void reset()
	{
		for (boolean[] a : pwGrid)
			Arrays.fill(a, false);
		
		numOrbs = 0;
	}
	
	/**
	 * Password parser logic based on http://www.mmhp.net/Passwords/PassCrack2.html
	 * 
	 * @return the GameState indicated by the password grid, or null if invalid pattern
	 */
	/*public GameState getGameState()
	{
		if (numOrbs != maxOrbs)
			return null;
		
		GameState state = new GameState();
		
		int numTanks = -1;
		for (int i = 0; i < pwGrid[0].length; i++)
		{
			if (pwGrid[0][i])
			{
				numTanks = i;
				break;
			}
		}
		if (numTanks == -1)
			return null;
		state.setEnergyTanks(numTanks);
		
		switch (numTanks)
		{
		case 0:
			if (pwGrid[4][2])
				state.setBeaten(GameState.AIRMAN, true);
			else if (pwGrid[3][1])
				state.setBeaten(GameState.AIRMAN, false);
			else
				return null;
			
			if (pwGrid[3][0])
				state.setBeaten(GameState.BUBBLEMAN, true);
			else if (pwGrid[2][2])
				state.setBeaten(GameState.BUBBLEMAN, false);
			else
				return null;
			
			if (pwGrid[2][4])
				state.setBeaten(GameState.CRASHMAN, true);
			else if (pwGrid[4][1])
				state.setBeaten(GameState.CRASHMAN, false);
			else
				return null;
			
			if (pwGrid[2][0])
				state.setBeaten(GameState.FLASHMAN, true);
			else if (pwGrid[4][3])
				state.setBeaten(GameState.FLASHMAN, false);
			else
				return null;
			
			if (pwGrid[1][1])
				state.setBeaten(GameState.HEATMAN, true);
			else if (pwGrid[3][4])
				state.setBeaten(GameState.HEATMAN, false);
			else
				return null;
			
			if (pwGrid[4][4])
				state.setBeaten(GameState.METALMAN, true);
			else if (pwGrid[4][0])
				state.setBeaten(GameState.METALMAN, false);
			else
				return null;
			
			if (pwGrid[1][3])
				state.setBeaten(GameState.QUICKMAN, true);
			else if (pwGrid[2][3])
				state.setBeaten(GameState.QUICKMAN, false);
			else
				return null;
			
			if (pwGrid[3][2])
				state.setBeaten(GameState.WOODMAN, true);
			else if (pwGrid[2][4])
				state.setBeaten(GameState.WOODMAN, false);
			else
				return null;
			break;
		case 1:
			if (pwGrid[4][3])
				state.setBeaten(GameState.AIRMAN, true);
			else if (pwGrid[3][2])
				state.setBeaten(GameState.AIRMAN, false);
			else
				return null;
			
			if (pwGrid[3][1])
				state.setBeaten(GameState.BUBBLEMAN, true);
			else if (pwGrid[2][3])
				state.setBeaten(GameState.BUBBLEMAN, false);
			else
				return null;
			
			if (pwGrid[3][0])
				state.setBeaten(GameState.CRASHMAN, true);
			else if (pwGrid[4][2])
				state.setBeaten(GameState.CRASHMAN, false);
			else
				return null;
			
			if (pwGrid[2][1])
				state.setBeaten(GameState.FLASHMAN, true);
			else if (pwGrid[4][4])
				state.setBeaten(GameState.FLASHMAN, false);
			else
				return null;
			
			if (pwGrid[1][2])
				state.setBeaten(GameState.HEATMAN, true);
			else if (pwGrid[4][0])
				state.setBeaten(GameState.HEATMAN, false);
			else
				return null;
			
			if (pwGrid[1][0])
				state.setBeaten(GameState.METALMAN, true);
			else if (pwGrid[4][1])
				state.setBeaten(GameState.METALMAN, false);
			else
				return null;
			
			if (pwGrid[1][4])
				state.setBeaten(GameState.QUICKMAN, true);
			else if (pwGrid[2][4])
				state.setBeaten(GameState.QUICKMAN, false);
			else
				return null;
			
			if (pwGrid[3][3])
				state.setBeaten(GameState.WOODMAN, true);
			else if (pwGrid[2][0])
				state.setBeaten(GameState.WOODMAN, false);
			else
				return null;
			break;
		case 2:
			if (pwGrid[4][4])
				state.setBeaten(GameState.AIRMAN, true);
			else if (pwGrid[3][3])
				state.setBeaten(GameState.AIRMAN, false);
			else
				return null;
			
			if (pwGrid[3][2])
				state.setBeaten(GameState.BUBBLEMAN, true);
			else if (pwGrid[2][4])
				state.setBeaten(GameState.BUBBLEMAN, false);
			else
				return null;
			
			if (pwGrid[3][1])
				state.setBeaten(GameState.CRASHMAN, true);
			else if (pwGrid[4][3])
				state.setBeaten(GameState.CRASHMAN, false);
			else
				return null;
			
			if (pwGrid[2][2])
				state.setBeaten(GameState.FLASHMAN, true);
			else if (pwGrid[1][0])
				state.setBeaten(GameState.FLASHMAN, false);
			else
				return null;
			
			if (pwGrid[1][3])
				state.setBeaten(GameState.HEATMAN, true);
			else if (pwGrid[4][1])
				state.setBeaten(GameState.HEATMAN, false);
			else
				return null;
			
			if (pwGrid[1][1])
				state.setBeaten(GameState.METALMAN, true);
			else if (pwGrid[4][2])
				state.setBeaten(GameState.METALMAN, false);
			else
				return null;
			
			if (pwGrid[2][0])
				state.setBeaten(GameState.QUICKMAN, true);
			else if (pwGrid[3][0])
				state.setBeaten(GameState.QUICKMAN, false);
			else
				return null;
			
			if (pwGrid[3][4])
				state.setBeaten(GameState.WOODMAN, true);
			else if (pwGrid[2][1])
				state.setBeaten(GameState.WOODMAN, false);
			else
				return null;
			break;
		case 3:
			if (pwGrid[4][2])
				state.setBeaten(GameState.AIRMAN, true);
			else if (pwGrid[3][1])
				state.setBeaten(GameState.AIRMAN, false);
			else
				return null;
			
			if (pwGrid[3][0])
				state.setBeaten(GameState.BUBBLEMAN, true);
			else if (pwGrid[2][2])
				state.setBeaten(GameState.BUBBLEMAN, false);
			else
				return null;
			
			if (pwGrid[2][4])
				state.setBeaten(GameState.CRASHMAN, true);
			else if (pwGrid[4][1])
				state.setBeaten(GameState.CRASHMAN, false);
			else
				return null;
			
			if (pwGrid[2][0])
				state.setBeaten(GameState.FLASHMAN, true);
			else if (pwGrid[4][3])
				state.setBeaten(GameState.FLASHMAN, false);
			else
				return null;
			
			if (pwGrid[1][1])
				state.setBeaten(GameState.HEATMAN, true);
			else if (pwGrid[3][4])
				state.setBeaten(GameState.HEATMAN, false);
			else
				return null;
			
			if (pwGrid[4][4])
				state.setBeaten(GameState.METALMAN, true);
			else if (pwGrid[4][0])
				state.setBeaten(GameState.METALMAN, false);
			else
				return null;
			
			if (pwGrid[1][3])
				state.setBeaten(GameState.QUICKMAN, true);
			else if (pwGrid[2][3])
				state.setBeaten(GameState.QUICKMAN, false);
			else
				return null;
			
			if (pwGrid[3][2])
				state.setBeaten(GameState.WOODMAN, true);
			else if (pwGrid[2][4])
				state.setBeaten(GameState.WOODMAN, false);
			else
				return null;
			break;
		case 4:
			if (pwGrid[4][2])
				state.setBeaten(GameState.AIRMAN, true);
			else if (pwGrid[3][1])
				state.setBeaten(GameState.AIRMAN, false);
			else
				return null;
			
			if (pwGrid[3][0])
				state.setBeaten(GameState.BUBBLEMAN, true);
			else if (pwGrid[2][2])
				state.setBeaten(GameState.BUBBLEMAN, false);
			else
				return null;
			
			if (pwGrid[2][4])
				state.setBeaten(GameState.CRASHMAN, true);
			else if (pwGrid[4][1])
				state.setBeaten(GameState.CRASHMAN, false);
			else
				return null;
			
			if (pwGrid[2][0])
				state.setBeaten(GameState.FLASHMAN, true);
			else if (pwGrid[4][3])
				state.setBeaten(GameState.FLASHMAN, false);
			else
				return null;
			
			if (pwGrid[1][1])
				state.setBeaten(GameState.HEATMAN, true);
			else if (pwGrid[3][4])
				state.setBeaten(GameState.HEATMAN, false);
			else
				return null;
			
			if (pwGrid[4][4])
				state.setBeaten(GameState.METALMAN, true);
			else if (pwGrid[4][0])
				state.setBeaten(GameState.METALMAN, false);
			else
				return null;
			
			if (pwGrid[1][3])
				state.setBeaten(GameState.QUICKMAN, true);
			else if (pwGrid[2][3])
				state.setBeaten(GameState.QUICKMAN, false);
			else
				return null;
			
			if (pwGrid[3][2])
				state.setBeaten(GameState.WOODMAN, true);
			else if (pwGrid[2][4])
				state.setBeaten(GameState.WOODMAN, false);
			else
				return null;
			break;
		}
		
		return state;
	}*/
}