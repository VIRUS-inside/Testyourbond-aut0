package zTools;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.SequentialGroup;

public class JFrame extends javax.swing.JFrame
{
  private zButton zButton1;
  private zButton zButton2;
  private zButton zButton3;
  private zButton zButton4;
  private zButton zButton5;
  private zButton zButton6;
  private zButton zButton7;
  private zButton zButton8;
  private zControlBar zControlBar1;
  
  public JFrame()
  {
    initComponents();
    zControlBar1.setFrame(this);
  }
  







  private void initComponents()
  {
    zButton5 = new zButton();
    zButton1 = new zButton();
    zControlBar1 = new zControlBar();
    zButton3 = new zButton();
    zButton4 = new zButton();
    zButton6 = new zButton();
    zButton7 = new zButton();
    zButton8 = new zButton();
    zButton2 = new zButton();
    
    setDefaultCloseOperation(3);
    setUndecorated(true);
    
    zButton5.setBackground(new java.awt.Color(255, 51, 51));
    zButton5.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        JFrame.this.zButton5ActionPerformed(evt);
      }
      
    });
    zButton1.setBackground(new java.awt.Color(0, 153, 255));
    
    GroupLayout zControlBar1Layout = new GroupLayout(zControlBar1);
    zControlBar1.setLayout(zControlBar1Layout);
    zControlBar1Layout.setHorizontalGroup(zControlBar1Layout
      .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, 32767));
    
    zControlBar1Layout.setVerticalGroup(zControlBar1Layout
      .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 32, 32767));
    

    zButton3.setBackground(new java.awt.Color(0, 153, 255));
    
    zButton4.setBackground(new java.awt.Color(0, 153, 255));
    
    zButton6.setBackground(new java.awt.Color(0, 153, 255));
    
    zButton7.setBackground(new java.awt.Color(255, 51, 51));
    zButton7.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        JFrame.this.zButton7ActionPerformed(evt);
      }
      
    });
    zButton8.setBackground(new java.awt.Color(255, 51, 51));
    zButton8.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        JFrame.this.zButton8ActionPerformed(evt);
      }
      
    });
    zButton2.setBackground(new java.awt.Color(102, 255, 51));
    
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout
      .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(zControlBar1, -1, -1, 32767)
      .addGroup(layout.createSequentialGroup()
      .addContainerGap()
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
      .addComponent(zButton1, -1, -1, 32767)
      .addComponent(zButton3, -1, 143, 32767)
      .addComponent(zButton6, -1, 143, 32767)
      .addComponent(zButton4, -1, 143, 32767)
      .addComponent(zButton5, -1, -1, 32767)
      .addComponent(zButton7, -1, -1, 32767)
      .addComponent(zButton8, -1, -1, 32767))
      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, 32767)
      .addComponent(zButton2, -2, -1, -2)
      .addGap(51, 51, 51)));
    
    layout.setVerticalGroup(layout
      .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
      .addComponent(zControlBar1, -2, -1, -2)
      .addGap(34, 34, 34)
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
      .addGroup(layout.createSequentialGroup()
      .addComponent(zButton1, -2, -1, -2)
      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(zButton3, -2, -1, -2)
      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(zButton6, -2, -1, -2)
      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(zButton4, -2, -1, -2)
      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(zButton5, -2, 40, -2)
      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(zButton7, -2, 40, -2)
      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(zButton8, -2, 40, -2))
      .addComponent(zButton2, -2, 62, -2))
      .addContainerGap(16, 32767)));
    

    pack();
  }
  



  private void zButton5ActionPerformed(java.awt.event.ActionEvent evt) {}
  



  private void zButton7ActionPerformed(java.awt.event.ActionEvent evt) {}
  



  private void zButton8ActionPerformed(java.awt.event.ActionEvent evt) {}
  



  public static void main(String[] args)
  {
    try
    {
      for (javax.swing.UIManager.LookAndFeelInfo info : ) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    


    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new JFrame().setVisible(true);
      }
    });
  }
}
