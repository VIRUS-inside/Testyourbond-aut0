package org.apache.bcel.verifier;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JList;
import javax.swing.UIManager;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
























































public class GraphicalVerifier
{
  boolean packFrame = false;
  
  public GraphicalVerifier()
  {
    VerifierAppFrame frame = new VerifierAppFrame();
    

    if (packFrame) {
      frame.pack();
    }
    else {
      frame.validate();
    }
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (height > height) {
      height = height;
    }
    if (width > width) {
      width = width;
    }
    frame.setLocation((width - width) / 2, (height - height) / 2);
    frame.setVisible(true);
    
    classNamesJList.setModel(new VerifierFactoryListModel());
    VerifierFactory.getVerifier(Type.OBJECT.getClassName());
    classNamesJList.setSelectedIndex(0);
  }
  
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    new GraphicalVerifier();
  }
}
