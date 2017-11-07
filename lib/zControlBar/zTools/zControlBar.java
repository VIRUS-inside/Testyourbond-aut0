package zTools;

import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;

public class zControlBar extends javax.swing.JPanel
{
  private int xMouse;
  private int yMouse;
  private javax.swing.JFrame frame = null;
  
  public zControlBar() {
    set();
  }
  
  private void set() {
    setBackground(new java.awt.Color(0, 0, 0));
    addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
      public void mouseDragged(MouseEvent evt) {
        zControlBar.this.Mouse_Dragged(evt);
      }
    });
    addMouseListener(new java.awt.event.MouseAdapter() {
      public void mousePressed(MouseEvent evt) {
        zControlBar.this.Mouse_Pressed(evt);
      }
      
    });
    GroupLayout zBarLayout = new GroupLayout(this);
    setLayout(zBarLayout);
    zBarLayout.setHorizontalGroup(zBarLayout
      .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 610, 32767));
    
    zBarLayout.setVerticalGroup(zBarLayout
      .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 30, 32767));
  }
  
  private void Mouse_Dragged(MouseEvent evt)
  {
    int x = evt.getXOnScreen();
    int y = evt.getYOnScreen();
    getFrame().setLocation(x - getxMouse(), y - getyMouse());
  }
  
  private void Mouse_Pressed(MouseEvent evt) {
    setxMouse(evt.getX());
    setyMouse(evt.getY());
  }
  


  public int getxMouse()
  {
    return xMouse;
  }
  


  public void setxMouse(int xMouse)
  {
    this.xMouse = xMouse;
  }
  


  public int getyMouse()
  {
    return yMouse;
  }
  


  public void setyMouse(int yMouse)
  {
    this.yMouse = yMouse;
  }
  


  public javax.swing.JFrame getFrame()
  {
    return frame;
  }
  


  public void setFrame(javax.swing.JFrame frame)
  {
    this.frame = frame;
  }
}
