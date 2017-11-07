package net.sourceforge.htmlunit.corejs.javascript.tools.debugger;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;




























































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































class FileTextArea
  extends JTextArea
  implements ActionListener, PopupMenuListener, KeyListener, MouseListener
{
  private static final long serialVersionUID = -25032065448563720L;
  private FileWindow w;
  private FilePopupMenu popup;
  
  public FileTextArea(FileWindow w)
  {
    this.w = w;
    popup = new FilePopupMenu(this);
    popup.addPopupMenuListener(this);
    addMouseListener(this);
    addKeyListener(this);
    setFont(new Font("Monospaced", 0, 12));
  }
  


  public void select(int pos)
  {
    if (pos >= 0) {
      try {
        int line = getLineOfOffset(pos);
        Rectangle rect = modelToView(pos);
        if (rect == null) {
          select(pos, pos);
        } else {
          try {
            Rectangle nrect = modelToView(
              getLineStartOffset(line + 1));
            if (nrect != null) {
              rect = nrect;
            }
          }
          catch (Exception localException) {}
          JViewport vp = (JViewport)getParent();
          Rectangle viewRect = vp.getViewRect();
          if (y + height > y)
          {
            select(pos, pos);
          }
          else {
            y += (height - height) / 2;
            scrollRectToVisible(rect);
            select(pos, pos);
          }
        }
      } catch (BadLocationException exc) {
        select(pos, pos);
      }
    }
  }
  



  private void checkPopup(MouseEvent e)
  {
    if (e.isPopupTrigger()) {
      popup.show(this, e.getX(), e.getY());
    }
  }
  




  public void mousePressed(MouseEvent e)
  {
    checkPopup(e);
  }
  


  public void mouseClicked(MouseEvent e)
  {
    checkPopup(e);
    requestFocus();
    getCaret().setVisible(true);
  }
  




  public void mouseEntered(MouseEvent e) {}
  



  public void mouseExited(MouseEvent e) {}
  



  public void mouseReleased(MouseEvent e)
  {
    checkPopup(e);
  }
  





  public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
  





  public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
  




  public void popupMenuCanceled(PopupMenuEvent e) {}
  




  public void actionPerformed(ActionEvent e)
  {
    int pos = viewToModel(new Point(popup.x, popup.y));
    popup.setVisible(false);
    String cmd = e.getActionCommand();
    int line = -1;
    try {
      line = getLineOfOffset(pos);
    }
    catch (Exception localException) {}
    if (cmd.equals("Set Breakpoint")) {
      w.setBreakPoint(line + 1);
    } else if (cmd.equals("Clear Breakpoint")) {
      w.clearBreakPoint(line + 1);
    } else if (cmd.equals("Run")) {
      w.load();
    }
  }
  




  public void keyPressed(KeyEvent e)
  {
    switch (e.getKeyCode()) {
    case 8: 
    case 9: 
    case 10: 
    case 127: 
      e.consume();
    }
    
  }
  


  public void keyTyped(KeyEvent e)
  {
    e.consume();
  }
  


  public void keyReleased(KeyEvent e)
  {
    e.consume();
  }
}
