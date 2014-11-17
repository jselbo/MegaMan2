package application.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import application.Utility;

public class AboutDialog extends JDialog
{
  private static final long serialVersionUID = 1L;

  public AboutDialog(JFrame parent)
  {
    super(parent, "About Mega Man 2", false);

    AboutPanel panel = new AboutPanel();
    setContentPane(panel);

    pack();
  }

  private class AboutPanel extends JPanel
  {
    private static final long serialVersionUID = 1L;
    private final Dimension dim;
    private Image bg;
    private String[] text;

    public AboutPanel()
    {
      bg = Utility.loadImage("res/images/overworld/about_background.png");

      dim = new Dimension(bg.getWidth(null) + 100, bg.getHeight(null) + 275);
      setPreferredSize(dim);
      setBackground(Color.white);

      text = new String[13];
      text[0] = "Mega Man 2 (Java Remix) was my attempt to faithfully recreate ";
      text[1] = "the NES Mega Man 2 game in the Java programming language. I wanted";
      text[2] = "to copy every level, sound, and enemy, and emulate every little";
      text[3] = "detail that existed in the game... That was the original goal anyway.";
      text[4] = "";
      text[5] = "I worked for months forming a game engine, finding sprites,";
      text[6] = "and perfecting the introduction sequence. Finally I had finished,";
      text[7] = "with no progress made on the actual gameplay portion. So, I ";
      text[8] = "threw together a mess of code whose gameplay does not deserve";
      text[9] = "to bear the honored title of Mega Man. But for the sake of ";
      text[10] = "brevity, I present to you, my beloved player, my humble creation.";
      text[11] = "";
      text[12] = "Enjoy.";
    }

    @Override
    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

      g2.drawImage(bg, 50, 15, null);

      Font font = new Font("Calibri", Font.BOLD, 16);
      g2.setFont(font);
      g2.setColor(Color.black);
      for (int i = 0; i < text.length; i++)
        g2.drawString(text[i], Utility.xCenterText(text[i], font, dim), 30 + bg.getHeight(null) + 20*i);
    }
  }
}