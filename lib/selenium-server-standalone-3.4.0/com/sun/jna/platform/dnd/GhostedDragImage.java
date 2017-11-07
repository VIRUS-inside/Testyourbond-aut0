package com.sun.jna.platform.dnd;

import com.sun.jna.platform.WindowUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;



























public class GhostedDragImage
{
  private static final float DEFAULT_ALPHA = 0.5F;
  private Window dragImage;
  private Point origin;
  private static final int SLIDE_INTERVAL = 33;
  
  public GhostedDragImage(Component dragSource, final Icon icon, Point initialScreenLoc, final Point cursorOffset)
  {
    Window parent = (dragSource instanceof Window) ? (Window)dragSource : SwingUtilities.getWindowAncestor(dragSource);
    

    GraphicsConfiguration gc = parent.getGraphicsConfiguration();
    dragImage = new Window(JOptionPane.getRootFrame(), gc) {
      private static final long serialVersionUID = 1L;
      
      public void paint(Graphics g) { icon.paintIcon(this, g, 0, 0); }
      
      public Dimension getPreferredSize() {
        return new Dimension(icon.getIconWidth(), icon.getIconHeight());
      }
      
      public Dimension getMinimumSize() { return getPreferredSize(); }
      
      public Dimension getMaximumSize() {
        return getPreferredSize();
      }
    };
    dragImage.setFocusableWindowState(false);
    dragImage.setName("###overrideRedirect###");
    Icon dragIcon = new Icon() {
      public int getIconHeight() {
        return icon.getIconHeight();
      }
      
      public int getIconWidth() { return icon.getIconWidth(); }
      
      public void paintIcon(Component c, Graphics g, int x, int y) {
        g = g.create();
        Area area = new Area(new Rectangle(x, y, getIconWidth(), getIconHeight()));
        
        area.subtract(new Area(new Rectangle(x + cursorOffsetx - 1, y + cursorOffsety - 1, 3, 3)));
        g.setClip(area);
        icon.paintIcon(c, g, x, y);
        g.dispose();
      }
      
    };
    dragImage.pack();
    WindowUtils.setWindowMask(dragImage, dragIcon);
    WindowUtils.setWindowAlpha(dragImage, 0.5F);
    move(initialScreenLoc);
    dragImage.setVisible(true);
  }
  
  public void setAlpha(float alpha)
  {
    WindowUtils.setWindowAlpha(dragImage, alpha);
  }
  
  public void dispose()
  {
    dragImage.dispose();
    dragImage = null;
  }
  


  public void move(Point screenLocation)
  {
    if (origin == null) {
      origin = screenLocation;
    }
    dragImage.setLocation(x, y);
  }
  

  public void returnToOrigin()
  {
    final Timer timer = new Timer(33, null);
    timer.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Point location = dragImage.getLocationOnScreen();
        Point dst = new Point(origin);
        int dx = (x - x) / 2;
        int dy = (y - y) / 2;
        if ((dx != 0) || (dy != 0)) {
          location.translate(dx, dy);
          move(location);
        }
        else {
          timer.stop();
          dispose();
        }
      }
    });
    timer.setInitialDelay(0);
    timer.start();
  }
}
