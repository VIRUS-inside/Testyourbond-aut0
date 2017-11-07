package zTools;

import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JLabel;

public class zPanel extends javax.swing.JPanel
{
  private javax.swing.JPanel panel;
  private JLabel jLabel1;
  
  public zPanel() {}
  
  public zPanel(String name)
  {
    MyEvents();
    MyContent();
    

    setSize(50, 100);
    setBackground(new java.awt.Color(255, 102, 102));
    jLabel1.setText(name);
  }
  

  private void MyEvents()
  {
    addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(MouseEvent evt) {
        zPanel.this.ItemMouseEntered(evt);
      }
      
      public void mouseExited(MouseEvent evt) { zPanel.this.ItemMouseExited(evt); }
    });
  }
  
  private void ItemMouseExited(MouseEvent evt)
  {
    setBackground(new java.awt.Color(255, 102, 102));
  }
  
  private void ItemMouseEntered(MouseEvent evt) { setBackground(new java.awt.Color(255, 50, 100)); }
  

  public void MyContent()
  {
    setjLabel1(new JLabel());
    

    GroupLayout MyPanelLayout = new GroupLayout(this);
    setLayout(MyPanelLayout);
    MyPanelLayout.setHorizontalGroup(MyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(MyPanelLayout.createSequentialGroup()
      .addGap(36, 36, 36)
      .addComponent(getjLabel1())
      .addContainerGap(39, 32767)));
    
    MyPanelLayout.setVerticalGroup(MyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(MyPanelLayout.createSequentialGroup()
      .addContainerGap()
      .addComponent(getjLabel1())
      .addContainerGap(106, 32767)));
  }
  



  public javax.swing.JPanel getPanel()
  {
    return panel;
  }
  


  public void setPanel(javax.swing.JPanel panel)
  {
    this.panel = panel;
  }
  


  public JLabel getjLabel1()
  {
    return jLabel1;
  }
  


  public void setjLabel1(JLabel jLabel1)
  {
    this.jLabel1 = jLabel1;
  }
}
