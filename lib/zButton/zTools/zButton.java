package zTools;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class zButton extends javax.swing.JButton
{
  private Color color = Color.WHITE;
  
  public zButton() { setUI(new zButtonUI());
    
    set();
  }
  
  private void set()
  {
    setText("zButton1");
    addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(MouseEvent evt) {
        zButton.this.Mouse_in(evt);
      }
      
      public void mouseExited(MouseEvent evt) { zButton.this.Mouse_out(evt); }
    });
  }
  
  private void Mouse_in(MouseEvent evt)
  {
    setForeground(getBackground().darker());
  }
  
  private void Mouse_out(MouseEvent evt)
  {
    setForeground(getColor());
  }
  











  public Color getColor()
  {
    return color;
  }
  


  public void setColor(Color color)
  {
    this.color = color;
  }
}
