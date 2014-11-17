package editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.File;

public class MainFrame extends JFrame {
    private boolean initialized = false;
    private Actions actions = new Actions();
    private JTextField filename = new JTextField(), dir = new JTextField();

    public void initialize() {
        initializeGui();
        initializeEvents();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initializeGui() {
        if (initialized)
            return;
        initialized = true;
        this.setSize(500, 400);
        this.setTitle("Mega Man 2 Level Editor");
        Dimension windowSize = this.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width/2 - windowSize.width/2, screenSize.height/2 - windowSize.height/2);
        Container pane = this.getContentPane();
        pane.setLayout(new GridBagLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setResizable(true);
        setupMenuBar();
        setupPanels(pane);
        // TODO: Add UI widgets
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        JMenuItem jmi = new JMenuItem("New File");
        jmi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.META_MASK));
        jmi.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        menu.add(jmi);
        jmi = new JMenuItem("Open");
        jmi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(MainFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //This is where a real application would open the file.
                    System.out.println("Opening: " + file.getName() + ".");
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
        });
        menu.add(jmi);
        jmi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.META_MASK));
        jmi = new JMenuItem("Save");
        menu.add(jmi);
        jmi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.META_MASK));
        jmi = new JMenuItem("Save As...");
        jmi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                // Demonstrate "Save" dialog:
                int rVal = c.showSaveDialog(MainFrame.this);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    filename.setText(c.getSelectedFile().getName());
                    dir.setText(c.getCurrentDirectory().toString());
                }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                    filename.setText("You pressed cancel");
                    dir.setText("");
                }
            }
        });
        jmi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.META_MASK | ActionEvent.SHIFT_MASK));
        menu.add(jmi);
        jmi = new JMenuItem("Export");
        menu.add(jmi);
        menu.addSeparator();
        jmi = new JMenuItem("Close File");
        jmi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.META_MASK));
        menu.add(jmi);
        jmi = new JMenuItem("Exit");
        jmi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.META_MASK));
        menu.add(jmi);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
    }

    private void setupPanels(Container pane) {
        JButton tools = new JButton("stuff");
        //tools.setBackground(Color.BLACK);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 0;
        c.ipady = 40;
        c.gridwidth = 2;
        //c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(tools, c);
    }

    private void initializeEvents() {
        // TODO: Add action listeners, etc
    }

    public class Actions implements ActionListener {
    public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            command = command == null ? "" : command;
            // TODO: add if...if else... for action commands

        }
    }

    public void dispose() {
        // TODO: Save settings
        //super.dispose();
        System.exit(0);
    }

    public void setVisible(boolean b) {
        initialize();
        super.setVisible(b);
    }
}