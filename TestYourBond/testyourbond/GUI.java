package testyourbond;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import zTools.zButton;

public class GUI extends javax.swing.JFrame
{
  int xMouse;
  int yMouse;
  int rowCount;
  String pageLink;
  org.jsoup.nodes.Document data = null;
  private JLabel jLabel1;
  
  public GUI() {
    initComponents();
    setLocationRelativeTo(null); }
  
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JPanel jPanel1;
  private JPanel jPanel2;
  private void onPaste() { java.awt.datatransfer.Clipboard c = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
    java.awt.datatransfer.Transferable t = c.getContents(this);
    if (t == null) {
      return;
    }
    try {
      txt_url.setText((String)t.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void openWebpage(String urlString) {
    try {
      java.awt.Desktop.getDesktop().browse(new java.net.URL(urlString).toURI());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  


  private void initComponents()
  {
    jPanel1 = new JPanel();
    jLabel4 = new JLabel();
    jPanel3 = new JPanel();
    txt_url = new JTextField();
    jTextField2 = new JTextField();
    zButton1 = new zButton();
    zButton2 = new zButton();
    jTextField3 = new JTextField();
    txt_name = new JTextField();
    jPanel4 = new JPanel();
    jLabel5 = new JLabel();
    jPanel2 = new JPanel();
    jLabel2 = new JLabel();
    jLabel3 = new JLabel();
    jLabel1 = new JLabel();
    
    setDefaultCloseOperation(3);
    setUndecorated(true);
    
    jPanel1.setBackground(new Color(46, 46, 46));
    jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(33, 33, 33)));
    jPanel1.setForeground(new Color(46, 46, 46));
    
    jLabel4.setIcon(new javax.swing.ImageIcon("C:\\Users\\AZ\\Downloads\\safe_image.png"));
    jLabel4.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        GUI.this.jLabel4MouseClicked(evt);
      }
      
    });
    jPanel3.setBackground(new Color(46, 46, 46));
    
    txt_url.setBackground(new Color(60, 60, 60));
    txt_url.setForeground(new Color(255, 255, 255));
    txt_url.setHorizontalAlignment(0);
    txt_url.setText("Enter TestYouBond URL");
    txt_url.setBorder(null);
    txt_url.setPreferredSize(new java.awt.Dimension(91, 30));
    txt_url.setSelectionColor(new Color(195, 237, 245));
    txt_url.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        GUI.this.txt_urlActionPerformed(evt);
      }
      
    });
    jTextField2.setEditable(false);
    jTextField2.setBackground(new Color(122, 208, 203));
    jTextField2.setForeground(new Color(53, 130, 125));
    jTextField2.setHorizontalAlignment(0);
    jTextField2.setText("URL");
    jTextField2.setFocusable(false);
    jTextField2.setPreferredSize(new java.awt.Dimension(19, 30));
    jTextField2.setSelectionColor(new Color(195, 237, 245));
    jTextField2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        GUI.this.jTextField2ActionPerformed(evt);
      }
      
    });
    zButton1.setBackground(new Color(77, 186, 180));
    zButton1.setText("Paste");
    zButton1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        GUI.this.zButton1ActionPerformed(evt);
      }
      
    });
    zButton2.setBackground(new Color(53, 130, 125));
    zButton2.setText("Answer Now");
    zButton2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        GUI.this.zButton2ActionPerformed(evt);
      }
      
    });
    jTextField3.setEditable(false);
    jTextField3.setBackground(new Color(122, 208, 203));
    jTextField3.setForeground(new Color(53, 130, 125));
    jTextField3.setHorizontalAlignment(0);
    jTextField3.setText("Your Name");
    jTextField3.setFocusable(false);
    jTextField3.setPreferredSize(new java.awt.Dimension(19, 30));
    jTextField3.setSelectionColor(new Color(195, 237, 245));
    jTextField3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        GUI.this.jTextField3ActionPerformed(evt);
      }
      
    });
    txt_name.setBackground(new Color(60, 60, 60));
    txt_name.setForeground(new Color(255, 255, 255));
    txt_name.setHorizontalAlignment(0);
    txt_name.setBorder(null);
    txt_name.setPreferredSize(new java.awt.Dimension(91, 30));
    txt_name.setSelectionColor(new Color(195, 237, 245));
    txt_name.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        GUI.this.txt_nameActionPerformed(evt);
      }
      
    });
    GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(jPanel3Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
      .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
      .addComponent(jTextField2, -2, 60, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(txt_url, -2, 205, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(zButton1, -2, 68, -2))
      .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
      .addComponent(zButton2, -2, -1, -2)
      .addGroup(jPanel3Layout.createSequentialGroup()
      .addComponent(jTextField3, -2, 60, -2)
      .addGap(0, 0, 0)
      .addComponent(txt_name, -2, 287, -2))))
      .addContainerGap(94, 32767)));
    
    jPanel3Layout.setVerticalGroup(jPanel3Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
      .addContainerGap()
      .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(txt_url, -2, 32, -2)
      .addComponent(jTextField2, -2, 32, -2)
      .addComponent(zButton1, -2, -1, -2))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(txt_name, -2, 32, -2)
      .addComponent(jTextField3, -2, 32, -2))
      .addGap(18, 18, 18)
      .addComponent(zButton2, -2, 40, -2)
      .addGap(50, 50, 50)));
    

    jLabel5.setForeground(new Color(15, 105, 142));
    jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/freelancer.com_logo2.png")));
    jLabel5.setText("                                                   Zaki Developer - Freelancer.com");
    jLabel5.setCursor(new java.awt.Cursor(12));
    jLabel5.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        GUI.this.jLabel5MouseClicked(evt);
      }
      
    });
    GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(jPanel4Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
      .addContainerGap()
      .addComponent(jLabel5, -1, -1, 32767)
      .addContainerGap()));
    
    jPanel4Layout.setVerticalGroup(jPanel4Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
      .addGap(0, 0, 32767)
      .addComponent(jLabel5)));
    

    GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(jPanel1Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(jPanel4, -1, -1, 32767)
      .addGroup(jPanel1Layout.createSequentialGroup()
      .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
      .addGap(32, 32, 32)
      .addComponent(jPanel3, -2, -1, -2))
      .addGroup(jPanel1Layout.createSequentialGroup()
      .addGap(200, 200, 200)
      .addComponent(jLabel4)))
      .addContainerGap(-1, 32767)));
    
    jPanel1Layout.setVerticalGroup(jPanel1Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
      .addGap(20, 20, 20)
      .addComponent(jLabel4)
      .addGap(18, 18, 18)
      .addComponent(jPanel3, -2, -1, -2)
      .addGap(13, 13, 13)
      .addComponent(jPanel4, -2, -1, -2)
      .addGap(0, 0, 0)));
    

    jPanel2.setBackground(new Color(68, 68, 68));
    jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new Color(33, 33, 33)));
    jPanel2.setForeground(new Color(46, 46, 46));
    jPanel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
      public void mouseDragged(MouseEvent evt) {
        GUI.this.jPanel2MouseDragged(evt);
      }
    });
    jPanel2.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent evt) {
        GUI.this.jPanel2MousePressed(evt);
      }
      
    });
    jLabel2.setHorizontalAlignment(0);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cancel (4).png")));
    jLabel2.setCursor(new java.awt.Cursor(12));
    jLabel2.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        GUI.this.jLabel2MouseClicked(evt);
      }
      
    });
    jLabel3.setHorizontalAlignment(0);
    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/info-sign.png")));
    jLabel3.setCursor(new java.awt.Cursor(12));
    jLabel3.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        GUI.this.jLabel3MouseClicked(evt);
      }
      
    });
    jLabel1.setForeground(new Color(204, 205, 207));
    jLabel1.setText("[ TestYouBond Hack ]");
    
    GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
      .addGap(175, 175, 175)
      .addComponent(jLabel1)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767)
      .addComponent(jLabel3, -2, 30, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(jLabel2, -2, 30, -2)
      .addContainerGap()));
    
    jPanel2Layout.setVerticalGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(jLabel1, -1, -1, 32767)
      .addComponent(jLabel3, GroupLayout.Alignment.TRAILING, -1, 34, 32767)
      .addComponent(jLabel2, GroupLayout.Alignment.TRAILING, -1, -1, 32767));
    

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(jPanel2, -1, -1, 32767)
      .addComponent(jPanel1, -1, -1, 32767));
    
    layout.setVerticalGroup(layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
      .addGap(0, 0, 0)
      .addComponent(jPanel2, -2, -1, -2)
      .addGap(0, 0, 0)
      .addComponent(jPanel1, -2, -1, -2)));
    

    pack();
  }
  

  private void jTextField2ActionPerformed(ActionEvent evt) {}
  
  private void zButton1ActionPerformed(ActionEvent evt)
  {
    onPaste();
  }
  
  private void zButton2ActionPerformed(ActionEvent evt)
  {
    String url = txt_url.getText();
    String name = txt_name.getText();
    new TestYourBond(url, name);
  }
  
  private void jLabel5MouseClicked(MouseEvent evt) {
    openWebpage("https://www.freelancer.com/u/aza577f4c3772a96.html");
  }
  
  private void jLabel2MouseClicked(MouseEvent evt) {
    System.exit(0);
  }
  
  private void jLabel3MouseClicked(MouseEvent evt) {
    openWebpage("https://www.freelancer.com/u/aza577f4c3772a96.html");
  }
  
  private void jPanel2MouseDragged(MouseEvent evt) {
    int x = evt.getXOnScreen();
    int y = evt.getYOnScreen();
    setLocation(x - xMouse, y - yMouse);
  }
  
  private void jPanel2MousePressed(MouseEvent evt) {
    xMouse = evt.getX();
    yMouse = evt.getY();
  }
  

  private JPanel jPanel3;
  
  private JPanel jPanel4;
  
  private JTextField jTextField2;
  
  private JTextField jTextField3;
  
  private JTextField txt_name;
  private JTextField txt_url;
  private zButton zButton1;
  public zButton zButton2;
  private void txt_urlActionPerformed(ActionEvent evt) {}
  
  private void jTextField3ActionPerformed(ActionEvent evt) {}
  
  private void txt_nameActionPerformed(ActionEvent evt) {}
  
  private void jLabel4MouseClicked(MouseEvent evt) {}
  
  public static void main(String[] args)
  {
    try
    {
      for (javax.swing.UIManager.LookAndFeelInfo info : ) {
        if ("Windows".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    java.awt.EventQueue.invokeLater(new Runnable()
    {
      public void run() {
        new GUI().setVisible(true);
      }
    });
  }
}
