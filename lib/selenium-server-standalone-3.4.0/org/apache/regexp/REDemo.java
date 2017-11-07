package org.apache.regexp;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.CharArrayWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.EventObject;
import javax.swing.JFrame;
















































public class REDemo
  extends Applet
  implements TextListener
{
  RE r = new RE();
  REDebugCompiler compiler = new REDebugCompiler();
  

  TextField fieldRE;
  

  TextField fieldMatch;
  

  TextArea outRE;
  
  TextArea outMatch;
  

  public void init()
  {
    GridBagLayout localGridBagLayout = new GridBagLayout();
    setLayout(localGridBagLayout);
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    insets = new Insets(5, 5, 5, 5);
    anchor = 13;
    localGridBagLayout.setConstraints(add(new Label("Regular expression:", 2)), localGridBagConstraints);
    gridy = 0;
    anchor = 17;
    localGridBagLayout.setConstraints(add(this.fieldRE = new TextField("\\[([:javastart:][:javapart:]*)\\]", 40)), localGridBagConstraints);
    gridx = 0;
    gridy = -1;
    anchor = 13;
    localGridBagLayout.setConstraints(add(new Label("String:", 2)), localGridBagConstraints);
    gridy = 1;
    gridx = -1;
    anchor = 17;
    localGridBagLayout.setConstraints(add(this.fieldMatch = new TextField("aaa([foo])aaa", 40)), localGridBagConstraints);
    gridy = 2;
    gridx = -1;
    fill = 1;
    weighty = 1.0D;
    weightx = 1.0D;
    localGridBagLayout.setConstraints(add(this.outRE = new TextArea()), localGridBagConstraints);
    gridy = 2;
    gridx = -1;
    localGridBagLayout.setConstraints(add(this.outMatch = new TextArea()), localGridBagConstraints);
    

    fieldRE.addTextListener(this);
    fieldMatch.addTextListener(this);
    

    textValueChanged(null);
  }
  




  void sayRE(String paramString)
  {
    outRE.setText(paramString);
  }
  




  void sayMatch(String paramString)
  {
    outMatch.setText(paramString);
  }
  




  String throwableToString(Throwable paramThrowable)
  {
    String str1 = paramThrowable.getClass().getName();
    String str2;
    if ((str2 = paramThrowable.getMessage()) != null)
    {
      str1 = str1 + "\n" + str2;
    }
    return str1;
  }
  





  void updateRE(String paramString)
  {
    try
    {
      r.setProgram(compiler.compile(paramString));
      

      CharArrayWriter localCharArrayWriter = new CharArrayWriter();
      compiler.dumpProgram(new PrintWriter(localCharArrayWriter));
      sayRE(localCharArrayWriter.toString());
      System.out.println(localCharArrayWriter);
    }
    catch (Exception localException)
    {
      r.setProgram(null);
      sayRE(throwableToString(localException));
    }
    catch (Throwable localThrowable)
    {
      r.setProgram(null);
      sayRE(throwableToString(localThrowable));
    }
  }
  






  void updateMatch(String paramString)
  {
    try
    {
      if (r.match(paramString))
      {

        String str = "Matches.\n\n";
        

        for (int i = 0; i < r.getParenCount(); i++)
        {
          str = str + "$" + i + " = " + r.getParen(i) + "\n";
        }
        sayMatch(str);

      }
      else
      {
        sayMatch("Does not match");
      }
    }
    catch (Throwable localThrowable)
    {
      sayMatch(throwableToString(localThrowable));
    }
  }
  





  public void textValueChanged(TextEvent paramTextEvent)
  {
    if ((paramTextEvent == null) || (paramTextEvent.getSource() == fieldRE))
    {

      updateRE(fieldRE.getText());
    }
    

    updateMatch(fieldMatch.getText());
  }
  




  public static void main(String[] paramArrayOfString)
  {
    JFrame localJFrame = new JFrame("RE Demo");
    
    localJFrame.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        System.exit(0);
      }
    });
    Container localContainer = localJFrame.getContentPane();
    localContainer.setLayout(new FlowLayout());
    REDemo localREDemo = new REDemo();
    localContainer.add(localREDemo);
    localREDemo.init();
    localJFrame.pack();
    localJFrame.setVisible(true);
  }
  
  public REDemo() {}
}
