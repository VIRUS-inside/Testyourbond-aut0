package zTools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicButtonUI;

class zButtonUI extends BasicButtonUI
{
  zButtonUI() {}
  
  public void installUI(JComponent c)
  {
    super.installUI(c);
    AbstractButton button = (AbstractButton)c;
    button.setOpaque(false);
    button.setBorder(new javax.swing.border.EmptyBorder(7, 10, 7, 10));
    button.setFont(new java.awt.Font("Calibri", 0, 14));
    button.setBackground(getColor());
    button.setForeground(Color.white);
  }
  

  public static Color getColor()
  {
    Color c = new Color(16767556);
    return c;
  }
  

  public void paint(Graphics g, JComponent c)
  {
    AbstractButton b = (AbstractButton)c;
    paintBackground(g, b, b.getModel().isPressed() ? 2 : 0);
    super.paint(g, c);
  }
  
  private void paintBackground(Graphics g, JComponent c, int yOffset) {
    Dimension size = c.getSize();
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setColor(c.getBackground().darker());
    g.fillRoundRect(0, yOffset, width, height - yOffset, 5, 5);
    g.setColor(c.getBackground());
    g.fillRoundRect(0, yOffset, width, height + yOffset - 4, 5, 5);
  }
  
  public static void main(String[] args) {
    JFrame f = new JFrame("Button UI Test");
    f.setDefaultCloseOperation(2);
    JPanel p = new JPanel();
    p.setBackground(Color.white);
    f.setContentPane(p);
    
    p.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
    
    for (int i = 1; i <= 5; i++) {
      JButton button = new JButton(" #" + i);
      button.setFont(new java.awt.Font("Calibri", 0, 14));
      button.setBackground(new Color(371429));
      button.setForeground(Color.white);
      
      button.setUI(new zButtonUI());
      
      p.add(button);
    }
    
    f.pack();
    f.setLocation(500, 500);
    f.setVisible(true);
  }
}
