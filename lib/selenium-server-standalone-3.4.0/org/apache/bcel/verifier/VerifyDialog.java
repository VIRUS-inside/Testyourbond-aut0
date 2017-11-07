package org.apache.bcel.verifier;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.EventObject;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;




















































public class VerifyDialog
  extends JDialog
{
  private JPanel ivjJDialogContentPane = null;
  
  private JPanel ivjPass1Panel = null;
  
  private JPanel ivjPass2Panel = null;
  
  private JPanel ivjPass3Panel = null;
  
  private JButton ivjPass1Button = null;
  
  private JButton ivjPass2Button = null;
  
  private JButton ivjPass3Button = null;
  
  IvjEventHandler ivjEventHandler = new IvjEventHandler();
  





  private String class_name = "java.lang.Object";
  
  private static int classes_to_verify;
  

  class IvjEventHandler
    implements ActionListener
  {
    IvjEventHandler() {}
    
    public void actionPerformed(ActionEvent e)
    {
      if (e.getSource() == VerifyDialog.this.getPass1Button())
        VerifyDialog.this.connEtoC1(e);
      if (e.getSource() == VerifyDialog.this.getPass2Button())
        VerifyDialog.this.connEtoC2(e);
      if (e.getSource() == VerifyDialog.this.getPass3Button())
        VerifyDialog.this.connEtoC3(e);
      if (e.getSource() == VerifyDialog.this.getFlushButton())
        VerifyDialog.this.connEtoC4(e);
    }
  }
  
  private JButton ivjFlushButton = null;
  
  public VerifyDialog()
  {
    initialize();
  }
  
  public VerifyDialog(Dialog owner)
  {
    super(owner);
  }
  
  public VerifyDialog(Dialog owner, String title)
  {
    super(owner, title);
  }
  
  public VerifyDialog(Dialog owner, String title, boolean modal)
  {
    super(owner, title, modal);
  }
  
  public VerifyDialog(Dialog owner, boolean modal)
  {
    super(owner, modal);
  }
  
  public VerifyDialog(Frame owner)
  {
    super(owner);
  }
  
  public VerifyDialog(Frame owner, String title)
  {
    super(owner, title);
  }
  
  public VerifyDialog(Frame owner, String title, boolean modal)
  {
    super(owner, title, modal);
  }
  
  public VerifyDialog(Frame owner, boolean modal)
  {
    super(owner, modal);
  }
  






  public VerifyDialog(String fully_qualified_class_name)
  {
    int dotclasspos = fully_qualified_class_name.lastIndexOf(".class");
    if (dotclasspos != -1) fully_qualified_class_name = fully_qualified_class_name.substring(0, dotclasspos);
    fully_qualified_class_name = fully_qualified_class_name.replace('/', '.');
    
    class_name = fully_qualified_class_name;
    initialize();
  }
  


  private void connEtoC1(ActionEvent arg1)
  {
    try
    {
      pass1Button_ActionPerformed(arg1);

    }
    catch (Throwable ivjExc)
    {

      handleException(ivjExc);
    }
  }
  

  private void connEtoC2(ActionEvent arg1)
  {
    try
    {
      pass2Button_ActionPerformed(arg1);

    }
    catch (Throwable ivjExc)
    {

      handleException(ivjExc);
    }
  }
  

  private void connEtoC3(ActionEvent arg1)
  {
    try
    {
      pass4Button_ActionPerformed(arg1);

    }
    catch (Throwable ivjExc)
    {

      handleException(ivjExc);
    }
  }
  

  private void connEtoC4(ActionEvent arg1)
  {
    try
    {
      flushButton_ActionPerformed(arg1);

    }
    catch (Throwable ivjExc)
    {

      handleException(ivjExc);
    }
  }
  
  public void flushButton_ActionPerformed(ActionEvent actionEvent)
  {
    VerifierFactory.getVerifier(class_name).flush();
    Repository.removeClass(class_name);
    getPass1Panel().setBackground(Color.gray);
    getPass1Panel().repaint();
    getPass2Panel().setBackground(Color.gray);
    getPass2Panel().repaint();
    getPass3Panel().setBackground(Color.gray);
    getPass3Panel().repaint();
  }
  
  private JButton getFlushButton()
  {
    if (ivjFlushButton == null) {
      try {
        ivjFlushButton = new JButton();
        ivjFlushButton.setName("FlushButton");
        ivjFlushButton.setText("Flush: Forget old verification results");
        ivjFlushButton.setBackground(SystemColor.controlHighlight);
        ivjFlushButton.setBounds(60, 215, 300, 30);
        ivjFlushButton.setForeground(Color.red);
        ivjFlushButton.setActionCommand("FlushButton");

      }
      catch (Throwable ivjExc)
      {

        handleException(ivjExc);
      }
    }
    return ivjFlushButton;
  }
  
  private JPanel getJDialogContentPane()
  {
    if (ivjJDialogContentPane == null) {
      try {
        ivjJDialogContentPane = new JPanel();
        ivjJDialogContentPane.setName("JDialogContentPane");
        ivjJDialogContentPane.setLayout(null);
        getJDialogContentPane().add(getPass1Panel(), getPass1Panel().getName());
        getJDialogContentPane().add(getPass3Panel(), getPass3Panel().getName());
        getJDialogContentPane().add(getPass2Panel(), getPass2Panel().getName());
        getJDialogContentPane().add(getPass1Button(), getPass1Button().getName());
        getJDialogContentPane().add(getPass2Button(), getPass2Button().getName());
        getJDialogContentPane().add(getPass3Button(), getPass3Button().getName());
        getJDialogContentPane().add(getFlushButton(), getFlushButton().getName());

      }
      catch (Throwable ivjExc)
      {

        handleException(ivjExc);
      }
    }
    return ivjJDialogContentPane;
  }
  
  private JButton getPass1Button()
  {
    if (ivjPass1Button == null) {
      try {
        ivjPass1Button = new JButton();
        ivjPass1Button.setName("Pass1Button");
        ivjPass1Button.setText("Pass1: Verify binary layout of .class file");
        ivjPass1Button.setBackground(SystemColor.controlHighlight);
        ivjPass1Button.setBounds(100, 40, 300, 30);
        ivjPass1Button.setActionCommand("Button1");

      }
      catch (Throwable ivjExc)
      {

        handleException(ivjExc);
      }
    }
    return ivjPass1Button;
  }
  
  private JPanel getPass1Panel()
  {
    if (ivjPass1Panel == null) {
      try {
        ivjPass1Panel = new JPanel();
        ivjPass1Panel.setName("Pass1Panel");
        ivjPass1Panel.setLayout(null);
        ivjPass1Panel.setBackground(SystemColor.controlShadow);
        ivjPass1Panel.setBounds(30, 30, 50, 50);

      }
      catch (Throwable ivjExc)
      {

        handleException(ivjExc);
      }
    }
    return ivjPass1Panel;
  }
  
  private JButton getPass2Button()
  {
    if (ivjPass2Button == null) {
      try {
        ivjPass2Button = new JButton();
        ivjPass2Button.setName("Pass2Button");
        ivjPass2Button.setText("Pass 2: Verify static .class file constraints");
        ivjPass2Button.setBackground(SystemColor.controlHighlight);
        ivjPass2Button.setBounds(100, 100, 300, 30);
        ivjPass2Button.setActionCommand("Button2");

      }
      catch (Throwable ivjExc)
      {

        handleException(ivjExc);
      }
    }
    return ivjPass2Button;
  }
  
  private JPanel getPass2Panel()
  {
    if (ivjPass2Panel == null) {
      try {
        ivjPass2Panel = new JPanel();
        ivjPass2Panel.setName("Pass2Panel");
        ivjPass2Panel.setLayout(null);
        ivjPass2Panel.setBackground(SystemColor.controlShadow);
        ivjPass2Panel.setBounds(30, 90, 50, 50);

      }
      catch (Throwable ivjExc)
      {

        handleException(ivjExc);
      }
    }
    return ivjPass2Panel;
  }
  
  private JButton getPass3Button()
  {
    if (ivjPass3Button == null) {
      try {
        ivjPass3Button = new JButton();
        ivjPass3Button.setName("Pass3Button");
        ivjPass3Button.setText("Passes 3a+3b: Verify code arrays");
        ivjPass3Button.setBackground(SystemColor.controlHighlight);
        ivjPass3Button.setBounds(100, 160, 300, 30);
        ivjPass3Button.setActionCommand("Button2");

      }
      catch (Throwable ivjExc)
      {

        handleException(ivjExc);
      }
    }
    return ivjPass3Button;
  }
  
  private JPanel getPass3Panel()
  {
    if (ivjPass3Panel == null) {
      try {
        ivjPass3Panel = new JPanel();
        ivjPass3Panel.setName("Pass3Panel");
        ivjPass3Panel.setLayout(null);
        ivjPass3Panel.setBackground(SystemColor.controlShadow);
        ivjPass3Panel.setBounds(30, 150, 50, 50);

      }
      catch (Throwable ivjExc)
      {

        handleException(ivjExc);
      }
    }
    return ivjPass3Panel;
  }
  


  private void handleException(Throwable exception)
  {
    System.out.println("--------- UNCAUGHT EXCEPTION ---------");
    exception.printStackTrace(System.out);
  }
  


  private void initConnections()
    throws Exception
  {
    getPass1Button().addActionListener(ivjEventHandler);
    getPass2Button().addActionListener(ivjEventHandler);
    getPass3Button().addActionListener(ivjEventHandler);
    getFlushButton().addActionListener(ivjEventHandler);
  }
  

  private void initialize()
  {
    try
    {
      setName("VerifyDialog");
      setDefaultCloseOperation(2);
      setSize(430, 280);
      setVisible(true);
      setModal(true);
      setResizable(false);
      setContentPane(getJDialogContentPane());
      initConnections();
    } catch (Throwable ivjExc) {
      handleException(ivjExc);
    }
    
    setTitle("'" + class_name + "' verification - JustIce / BCEL");
  }
  





  public static void main(String[] args)
  {
    classes_to_verify = args.length;
    
    for (int i = 0; i < args.length; i++)
    {
      try
      {
        VerifyDialog aVerifyDialog = new VerifyDialog(args[i]);
        aVerifyDialog.setModal(true);
        aVerifyDialog.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            VerifyDialog.access$810();
            if (VerifyDialog.classes_to_verify == 0) System.exit(0);
          }
        });
        aVerifyDialog.setVisible(true);
      } catch (Throwable exception) {
        System.err.println("Exception occurred in main() of javax.swing.JDialog");
        exception.printStackTrace(System.out);
      }
    }
  }
  

  public void pass1Button_ActionPerformed(ActionEvent actionEvent)
  {
    Verifier v = VerifierFactory.getVerifier(class_name);
    VerificationResult vr = v.doPass1();
    if (vr.getStatus() == 1) {
      getPass1Panel().setBackground(Color.green);
      getPass1Panel().repaint();
    }
    if (vr.getStatus() == 2) {
      getPass1Panel().setBackground(Color.red);
      getPass1Panel().repaint();
    }
  }
  
  public void pass2Button_ActionPerformed(ActionEvent actionEvent)
  {
    pass1Button_ActionPerformed(actionEvent);
    
    Verifier v = VerifierFactory.getVerifier(class_name);
    VerificationResult vr = v.doPass2();
    if (vr.getStatus() == 1) {
      getPass2Panel().setBackground(Color.green);
      getPass2Panel().repaint();
    }
    if (vr.getStatus() == 0) {
      getPass2Panel().setBackground(Color.yellow);
      getPass2Panel().repaint();
    }
    if (vr.getStatus() == 2) {
      getPass2Panel().setBackground(Color.red);
      getPass2Panel().repaint();
    }
  }
  

  public void pass4Button_ActionPerformed(ActionEvent actionEvent)
  {
    pass2Button_ActionPerformed(actionEvent);
    

    Color color = Color.green;
    
    Verifier v = VerifierFactory.getVerifier(class_name);
    VerificationResult vr = v.doPass2();
    if (vr.getStatus() == 1) {
      JavaClass jc = Repository.lookupClass(class_name);
      int nr = jc.getMethods().length;
      for (int i = 0; i < nr; i++) {
        vr = v.doPass3b(i);
        if (vr.getStatus() != 1) {
          color = Color.red;
          break;
        }
      }
    }
    else {
      color = Color.yellow;
    }
    
    getPass3Panel().setBackground(color);
    getPass3Panel().repaint();
  }
}
