package application;

import input.InputListener;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.sound.sampled.AudioFormat;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import panels.GamePanel;
import panels.IntroPanel;
import panels.LevelPanel;
import panels.LevelSelectPanel;
import panels.PasswordPanel;
import application.dialog.AboutDialog;
import application.dialog.AudioDialog;
import audio.MidiPlayer;
import audio.SoundManager;

public class GameManager implements Runnable
{
	private static final int SLEEP_TIME = 10; // (int)(1000f / 30f); // (1 second) divided by (frames per second)
	private static final int START_PANEL = 0;
	
	// uncompressed, 44100Hz, 16-bit, stereo, signed, little-endian
	private static final AudioFormat PLAYBACK_FORMAT = new AudioFormat(44100, 16, 2, true, false);
	
	// GUI elements
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenuItem audioMenuItem, resetMenuItem, aboutMenuItem;
	
	private GamePanel[] panels;
	private GameState state;
	private int currentPanel; // 0=IntroPanel, 1=PasswordPanel, 2=LevelSelectPanel, 3=LevelPanel
	
	private boolean reset; // flagged by resetMenuItem click
	
	private JDialog audioDialog, aboutDialog;
	
	// Input elements
	private InputListener inputListener;
	
	// Audio elements
	private SoundManager soundManager;
	private MidiPlayer midiPlayer;
	
	private Properties props;
	
	// Other variables
	private boolean running;
	
	public GameManager(JFrame frame)
	{
		this.frame = frame;
		
		menuBar = new JMenuBar();
		initializeMenuBar();
		
		audioDialog = null;
		
		running = false;
		
		inputListener = new InputListener();
		
		soundManager = new SoundManager(PLAYBACK_FORMAT);
		midiPlayer = new MidiPlayer();
		
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		state = new GameState(frame, screenDim, new Dimension(256 * 2, 240 * 2), 
				inputListener, soundManager, midiPlayer); // 16x15 blocks, 32 pixels each
		
		loadProperties();
		
		panels = new GamePanel[4];
		currentPanel = START_PANEL;
		setPanel(currentPanel);
	}
	
	private void loadProperties()
	{
		props = new Properties();
		
		FileInputStream in = null;
		try {
			in = new FileInputStream("resources/config.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int volume = Integer.parseInt(props.getProperty("volumeLevel"));
		midiPlayer.setVolume(volume);
		
		boolean muted = Boolean.parseBoolean(props.getProperty("volumeMuted"));
		midiPlayer.setMuted(muted);
	}
	
	private void initializeMenuBar()
	{
		ActionListener listener = new MenuItemListener();
		
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('o');
		
		audioMenuItem = new JMenuItem("Audio...");
		audioMenuItem.addActionListener(listener);
		audioMenuItem.setMnemonic('a');
		resetMenuItem = new JMenuItem("Reset");
		resetMenuItem.addActionListener(listener);
		resetMenuItem.setMnemonic('r');
		aboutMenuItem = new JMenuItem("About...");
		aboutMenuItem.addActionListener(listener);
		aboutMenuItem.setMnemonic('b');
		
		//optionsMenu.add(audioMenuItem);
		gameMenu.add(resetMenuItem);
		gameMenu.add(aboutMenuItem);
		
		menuBar.add(gameMenu);
	}
	
	public JMenuBar getJMenuBar()
	{
		return menuBar;
	}
	
	public void start()
	{
		running = true;
		new Thread(this).start();
	}
	
	public void run()
	{
		long currentTime = System.currentTimeMillis();
		
		while (running)
		{
			long elapsedTime = System.currentTimeMillis() - currentTime;
			currentTime += elapsedTime;
			
			update(elapsedTime);
			
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException ex) {}
		}
	}
	
	private void update(long elapsedTime)
	{
		/*if (inputListener.hardKeyQuery(KeyEvent.VK_ESCAPE))
		{
			running = false;
			
			device.setFullScreenWindow(null);
			
			return;
		}*/
		
		panels[currentPanel].updateGame(elapsedTime);
		
		// Check if panel is done
		if (panels[currentPanel].isDone())
		{
			switch (currentPanel)
			{
			case 0: setPanel(1);
				break;
			case 1: setPanel(2);
				break;
			case 2: setPanel(3);
				break;
			case 3: setPanel(3);
				break;
			}
		}
		else if (reset)
		{
			reset = false;
			setPanel(START_PANEL);
		}
		
		panels[currentPanel].repaint(); // repaint after trying to switch panels to prevent
										// NullPointerExceptions from the Graphics object (delayed painting)
	}
	
	private void setPanel(int destinationPanel)
	{
		// Remove the current panel
		if (panels[currentPanel] != null)
		{
			panels[currentPanel].removeKeyListener(inputListener);
			panels[currentPanel].removeMouseListener(inputListener);
			panels[currentPanel].removeMouseMotionListener(inputListener);
			panels[currentPanel].removeMouseWheelListener(inputListener);
			
			frame.getContentPane().remove(panels[currentPanel]);
		}
		
		inputListener.clearKeyStates();
		
		// Initialize the next panel if it hasn't been created yet
		if (panels[destinationPanel] == null)
		{
			switch (destinationPanel)
			{
			case 0: panels[destinationPanel] = new IntroPanel(state);
				break;
			case 1: panels[destinationPanel] = new PasswordPanel(state);
				break;
			case 2: panels[destinationPanel] = new LevelSelectPanel(state);
				break;
			case 3: panels[destinationPanel] = new LevelPanel(state);
				break;
			}
		}
		
		panels[destinationPanel].addKeyListener(inputListener);
		panels[destinationPanel].addMouseListener(inputListener);
		panels[destinationPanel].addMouseMotionListener(inputListener);
		panels[destinationPanel].addMouseWheelListener(inputListener);
		
		panels[destinationPanel].start();
		
		// Add the new panel
		frame.getContentPane().add(panels[destinationPanel]);
		frame.validate();
		
		panels[destinationPanel].requestFocus();
		
		currentPanel = destinationPanel;
	}
	
	private class MenuItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Object source = e.getSource();
			if (source.equals(audioMenuItem))
			{
				if (audioDialog == null)
					audioDialog = new AudioDialog(frame, "Audio Options", true);
				
				audioDialog.setLocationRelativeTo(frame);
				audioDialog.setVisible(true);
			}
			else if (source.equals(resetMenuItem))
			{
				reset = true;
			}
			else if (source.equals(aboutMenuItem))
			{
				if (aboutDialog == null)
					aboutDialog = new AboutDialog(frame);
				
				aboutDialog.setLocationRelativeTo(frame);
				aboutDialog.setVisible(true);
			}
		}
	}
}