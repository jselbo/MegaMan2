package application.dialog;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class AudioDialog extends JDialog {
  private static final long serialVersionUID = 1L;

  public AudioDialog(JFrame owner, String title, boolean modal) {
    super(owner, title, modal);

    AudioPanel panel = new AudioPanel();
    setContentPane(panel);

    pack();
  }

  private class AudioPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public AudioPanel() {
      setLayout(new BorderLayout());

      JPanel mainPanel = new JPanel();
      Border lineBorder = BorderFactory.createLineBorder(new Color(162, 188, 216));
      mainPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Volume Levels"));
      mainPanel.setLayout(new BorderLayout());
      JSlider slider = new JSlider(JSlider.VERTICAL, 0, 127, 127);
      slider.setMajorTickSpacing(25);
      slider.setPaintTicks(true);
      slider.setPaintLabels(false);
      slider.setPaintTrack(true);
      JCheckBox muted = new JCheckBox("Mute sounds", false);
      mainPanel.add(slider, BorderLayout.CENTER);
      mainPanel.add(muted, BorderLayout.SOUTH);

      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
      buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      JButton cancel = new JButton("Cancel");
      JButton ok = new JButton("Ok");
      buttonPanel.add(Box.createHorizontalGlue());
      buttonPanel.add(cancel);
      buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
      buttonPanel.add(ok);

      setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      add(mainPanel, BorderLayout.CENTER);
      add(buttonPanel, BorderLayout.SOUTH);

      /*setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      add(new JLabel("Test"));
      add(Box.createRigidArea(new Dimension(0, 10)));
      add(new JButton("Test"));*/
    }
  }
}