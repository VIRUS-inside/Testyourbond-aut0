package zTools;

import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JLabel;

public class MyPanel extends javax.swing.JPanel
{
  private JLabel LTitle;
  private String Title = "Title";
  
  public MyPanel() {
    MyEvents();
    MyContent();
    
    setSize(50, 100);
    setBackground(new Color(255, 102, 102));
    LTitle.setText(Title);
  }
  
  public MyPanel(String txt)
  {
    MyEvents();
    MyContent();
    
    setSize(50, 100);
    setBackground(new Color(255, 102, 102));
    Title = txt;
    LTitle.setText(Title);
  }
  

  private void MyEvents()
  {
    addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(MouseEvent evt) {
        MyPanel.this.ItemMouseEntered(evt);
      }
      
      public void mouseExited(MouseEvent evt) {
        MyPanel.this.ItemMouseExited(evt);
      }
    });
  }
  
  private void ItemMouseExited(MouseEvent evt) {
    setBackground(new Color(255, 102, 102));
  }
  
  private void ItemMouseEntered(MouseEvent evt) {
    setBackground(new Color(255, 50, 100));
  }
  
  public void MyContent()
  {
    setLTitle(new JLabel());
    
    GroupLayout MyPanelLayout = new GroupLayout(this);
    setLayout(MyPanelLayout);
    MyPanelLayout.setHorizontalGroup(MyPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(MyPanelLayout.createSequentialGroup()
      .addGap(36, 36, 36)
      .addComponent(getLTitle())
      .addContainerGap(39, 32767)));
    
    MyPanelLayout.setVerticalGroup(MyPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(MyPanelLayout.createSequentialGroup()
      .addContainerGap()
      .addComponent(getLTitle())
      .addContainerGap(106, 32767)));
  }
  



  public JLabel getLTitle()
  {
    return LTitle;
  }
  


  public void setLTitle(JLabel LTitle)
  {
    this.LTitle = LTitle;
  }
  


  public String getTitle()
  {
    return Title;
  }
  


  public void setTitle(String Title)
  {
    this.Title = Title;
  }
}
