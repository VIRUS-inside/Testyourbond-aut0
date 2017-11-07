package net.sourceforge.htmlunit.corejs.javascript.tools.debugger;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import javax.swing.DesktopManager;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import net.sourceforge.htmlunit.corejs.javascript.Kit;
import net.sourceforge.htmlunit.corejs.javascript.SecurityUtilities;
import net.sourceforge.htmlunit.corejs.javascript.tools.shell.ConsoleTextArea;

























































public class SwingGui
  extends JFrame
  implements GuiCallback
{
  private static final long serialVersionUID = -8217029773456711621L;
  Dim dim;
  private Runnable exitAction;
  private JDesktopPane desk;
  private ContextWindow context;
  private Menubar menubar;
  private JToolBar toolBar;
  private JSInternalConsole console;
  private JSplitPane split1;
  private JLabel statusBar;
  private final Map<String, JFrame> toplevels = Collections.synchronizedMap(new HashMap());
  




  private final Map<String, FileWindow> fileWindows = Collections.synchronizedMap(new TreeMap());
  



  private FileWindow currentWindow;
  



  JFileChooser dlg;
  



  private EventQueue awtEventQueue;
  



  public SwingGui(Dim dim, String title)
  {
    super(title);
    this.dim = dim;
    init();
    dim.setGuiCallback(this);
  }
  


  public Menubar getMenubar()
  {
    return menubar;
  }
  



  public void setExitAction(Runnable r)
  {
    exitAction = r;
  }
  


  public JSInternalConsole getConsole()
  {
    return console;
  }
  



  public void setVisible(boolean b)
  {
    super.setVisible(b);
    if (b)
    {
      console.consoleTextArea.requestFocus();
      context.split.setDividerLocation(0.5D);
      try {
        console.setMaximum(true);
        console.setSelected(true);
        console.show();
        console.consoleTextArea.requestFocus();
      }
      catch (Exception localException) {}
    }
  }
  


  void addTopLevel(String key, JFrame frame)
  {
    if (frame != this) {
      toplevels.put(key, frame);
    }
  }
  


  private void init()
  {
    menubar = new Menubar(this);
    setJMenuBar(menubar);
    toolBar = new JToolBar();
    


    String[] toolTips = { "Break (Pause)", "Go (F5)", "Step Into (F11)", "Step Over (F7)", "Step Out (F8)" };
    
    int count = 0;
    JButton breakButton; JButton button = breakButton = new JButton("Break");
    button.setToolTipText("Break");
    button.setActionCommand("Break");
    button.addActionListener(menubar);
    button.setEnabled(true);
    button.setToolTipText(toolTips[(count++)]);
    JButton goButton;
    button = goButton = new JButton("Go");
    button.setToolTipText("Go");
    button.setActionCommand("Go");
    button.addActionListener(menubar);
    button.setEnabled(false);
    button.setToolTipText(toolTips[(count++)]);
    JButton stepIntoButton;
    button = stepIntoButton = new JButton("Step Into");
    button.setToolTipText("Step Into");
    button.setActionCommand("Step Into");
    button.addActionListener(menubar);
    button.setEnabled(false);
    button.setToolTipText(toolTips[(count++)]);
    JButton stepOverButton;
    button = stepOverButton = new JButton("Step Over");
    button.setToolTipText("Step Over");
    button.setActionCommand("Step Over");
    button.setEnabled(false);
    button.addActionListener(menubar);
    button.setToolTipText(toolTips[(count++)]);
    JButton stepOutButton;
    button = stepOutButton = new JButton("Step Out");
    button.setToolTipText("Step Out");
    button.setActionCommand("Step Out");
    button.setEnabled(false);
    button.addActionListener(menubar);
    button.setToolTipText(toolTips[(count++)]);
    
    Dimension dim = stepOverButton.getPreferredSize();
    breakButton.setPreferredSize(dim);
    breakButton.setMinimumSize(dim);
    breakButton.setMaximumSize(dim);
    breakButton.setSize(dim);
    goButton.setPreferredSize(dim);
    goButton.setMinimumSize(dim);
    goButton.setMaximumSize(dim);
    stepIntoButton.setPreferredSize(dim);
    stepIntoButton.setMinimumSize(dim);
    stepIntoButton.setMaximumSize(dim);
    stepOverButton.setPreferredSize(dim);
    stepOverButton.setMinimumSize(dim);
    stepOverButton.setMaximumSize(dim);
    stepOutButton.setPreferredSize(dim);
    stepOutButton.setMinimumSize(dim);
    stepOutButton.setMaximumSize(dim);
    toolBar.add(breakButton);
    toolBar.add(goButton);
    toolBar.add(stepIntoButton);
    toolBar.add(stepOverButton);
    toolBar.add(stepOutButton);
    
    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout());
    getContentPane().add(toolBar, "North");
    getContentPane().add(contentPane, "Center");
    desk = new JDesktopPane();
    desk.setPreferredSize(new Dimension(600, 300));
    desk.setMinimumSize(new Dimension(150, 50));
    desk.add(this.console = new JSInternalConsole("JavaScript Console"));
    context = new ContextWindow(this);
    context.setPreferredSize(new Dimension(600, 120));
    context.setMinimumSize(new Dimension(50, 50));
    
    split1 = new JSplitPane(0, desk, context);
    split1.setOneTouchExpandable(true);
    setResizeWeight(split1, 0.66D);
    contentPane.add(split1, "Center");
    statusBar = new JLabel();
    statusBar.setText("Thread: ");
    contentPane.add(statusBar, "South");
    dlg = new JFileChooser();
    
    FileFilter filter = new FileFilter()
    {
      public boolean accept(File f) {
        if (f.isDirectory()) {
          return true;
        }
        String n = f.getName();
        int i = n.lastIndexOf('.');
        if ((i > 0) && (i < n.length() - 1)) {
          String ext = n.substring(i + 1).toLowerCase();
          if (ext.equals("js")) {
            return true;
          }
        }
        return false;
      }
      
      public String getDescription()
      {
        return "JavaScript Files (*.js)";
      }
    };
    dlg.addChoosableFileFilter(filter);
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {
        SwingGui.this.exit();
      }
    });
  }
  


  private void exit()
  {
    if (exitAction != null) {
      SwingUtilities.invokeLater(exitAction);
    }
    dim.setReturnValue(5);
  }
  


  FileWindow getFileWindow(String url)
  {
    if ((url == null) || (url.equals("<stdin>"))) {
      return null;
    }
    return (FileWindow)fileWindows.get(url);
  }
  


  static String getShortName(String url)
  {
    int lastSlash = url.lastIndexOf('/');
    if (lastSlash < 0) {
      lastSlash = url.lastIndexOf('\\');
    }
    String shortName = url;
    if ((lastSlash >= 0) && (lastSlash + 1 < url.length())) {
      shortName = url.substring(lastSlash + 1);
    }
    return shortName;
  }
  


  void removeWindow(FileWindow w)
  {
    fileWindows.remove(w.getUrl());
    JMenu windowMenu = getWindowMenu();
    int count = windowMenu.getItemCount();
    JMenuItem lastItem = windowMenu.getItem(count - 1);
    String name = getShortName(w.getUrl());
    for (int i = 5; i < count; i++) {
      JMenuItem item = windowMenu.getItem(i);
      if (item != null)
      {
        String text = item.getText();
        

        int pos = text.indexOf(' ');
        if (text.substring(pos + 1).equals(name)) {
          windowMenu.remove(item);
          




          if (count == 6)
          {
            windowMenu.remove(4); break;
          }
          int j = i - 4;
          for (; i < count - 1; i++) {
            JMenuItem thisItem = windowMenu.getItem(i);
            if (thisItem != null)
            {

              text = thisItem.getText();
              if (text.equals("More Windows...")) {
                break;
              }
              pos = text.indexOf(' ');
              thisItem.setText((char)(48 + j) + " " + text
                .substring(pos + 1));
              thisItem.setMnemonic(48 + j);
              j++;
            }
          }
          
          if ((count - 6 == 0) && (lastItem != item) && 
            (lastItem.getText().equals("More Windows..."))) {
            windowMenu.remove(lastItem);
          }
          

          break;
        }
      } }
    windowMenu.revalidate();
  }
  


  void showStopLine(Dim.StackFrame frame)
  {
    String sourceName = frame.getUrl();
    if ((sourceName == null) || (sourceName.equals("<stdin>"))) {
      if (console.isVisible()) {
        console.show();
      }
    } else {
      showFileWindow(sourceName, -1);
      int lineNumber = frame.getLineNumber();
      FileWindow w = getFileWindow(sourceName);
      if (w != null) {
        setFilePosition(w, lineNumber);
      }
    }
  }
  



  protected void showFileWindow(String sourceUrl, int lineNumber)
  {
    FileWindow w;
    


    FileWindow w;
    

    if (sourceUrl != null) {
      w = getFileWindow(sourceUrl);
    } else {
      JInternalFrame f = getSelectedFrame();
      FileWindow w; if ((f != null) && ((f instanceof FileWindow))) {
        w = (FileWindow)f;
      } else {
        w = currentWindow;
      }
    }
    if (w == null) {
      Dim.SourceInfo si = dim.sourceInfo(sourceUrl);
      createFileWindow(si, -1);
      w = getFileWindow(sourceUrl);
    }
    if (lineNumber > -1) {
      int start = w.getPosition(lineNumber - 1);
      int end = w.getPosition(lineNumber) - 1;
      if (start <= 0) {
        return;
      }
      textArea.select(start);
      textArea.setCaretPosition(start);
      textArea.moveCaretPosition(end);
    }
    try {
      if (w.isIcon()) {
        w.setIcon(false);
      }
      w.setVisible(true);
      w.moveToFront();
      w.setSelected(true);
      requestFocus();
      w.requestFocus();
      textArea.requestFocus();
    }
    catch (Exception localException) {}
  }
  


  protected void createFileWindow(Dim.SourceInfo sourceInfo, int line)
  {
    boolean activate = true;
    
    String url = sourceInfo.url();
    FileWindow w = new FileWindow(this, sourceInfo);
    fileWindows.put(url, w);
    if (line != -1) {
      if (currentWindow != null) {
        currentWindow.setPosition(-1);
      }
      try {
        w.setPosition(textArea.getLineStartOffset(line - 1));
      } catch (BadLocationException exc) {
        try {
          w.setPosition(textArea.getLineStartOffset(0));
        } catch (BadLocationException ee) {
          w.setPosition(-1);
        }
      }
    }
    desk.add(w);
    if (line != -1) {
      currentWindow = w;
    }
    menubar.addFile(url);
    w.setVisible(true);
    
    if (activate) {
      try {
        w.setMaximum(true);
        w.setSelected(true);
        w.moveToFront();
      }
      catch (Exception localException) {}
    }
  }
  









  protected boolean updateFileWindow(Dim.SourceInfo sourceInfo)
  {
    String fileName = sourceInfo.url();
    FileWindow w = getFileWindow(fileName);
    if (w != null) {
      w.updateText(sourceInfo);
      w.show();
      return true;
    }
    return false;
  }
  



  private void setFilePosition(FileWindow w, int line)
  {
    boolean activate = true;
    JTextArea ta = textArea;
    try {
      if (line == -1) {
        w.setPosition(-1);
        if (currentWindow == w) {
          currentWindow = null;
        }
      } else {
        int loc = ta.getLineStartOffset(line - 1);
        if ((currentWindow != null) && (currentWindow != w)) {
          currentWindow.setPosition(-1);
        }
        w.setPosition(loc);
        currentWindow = w;
      }
    }
    catch (BadLocationException localBadLocationException) {}
    
    if (activate) {
      if (w.isIcon()) {
        desk.getDesktopManager().deiconifyFrame(w);
      }
      desk.getDesktopManager().activateFrame(w);
      try {
        w.show();
        w.toFront();
        w.setSelected(true);
      }
      catch (Exception localException) {}
    }
  }
  



  void enterInterruptImpl(Dim.StackFrame lastFrame, String threadTitle, String alertMessage)
  {
    statusBar.setText("Thread: " + threadTitle);
    
    showStopLine(lastFrame);
    
    if (alertMessage != null) {
      MessageDialogWrapper.showMessageDialog(this, alertMessage, "Exception in Script", 0);
    }
    

    updateEnabled(true);
    
    Dim.ContextData contextData = lastFrame.contextData();
    
    JComboBox ctx = context.context;
    List<String> toolTips = context.toolTips;
    context.disableUpdate();
    int frameCount = contextData.frameCount();
    ctx.removeAllItems();
    

    ctx.setSelectedItem(null);
    toolTips.clear();
    for (int i = 0; i < frameCount; i++) {
      Dim.StackFrame frame = contextData.getFrame(i);
      String url = frame.getUrl();
      int lineNumber = frame.getLineNumber();
      String shortName = url;
      if (url.length() > 20) {
        shortName = "..." + url.substring(url.length() - 17);
      }
      String location = "\"" + shortName + "\", line " + lineNumber;
      ctx.insertItemAt(location, i);
      location = "\"" + url + "\", line " + lineNumber;
      toolTips.add(location);
    }
    context.enableUpdate();
    ctx.setSelectedIndex(0);
    ctx.setMinimumSize(new Dimension(50, getMinimumSizeheight));
  }
  


  private JMenu getWindowMenu()
  {
    return menubar.getMenu(3);
  }
  


  private String chooseFile(String title)
  {
    dlg.setDialogTitle(title);
    File CWD = null;
    String dir = SecurityUtilities.getSystemProperty("user.dir");
    if (dir != null) {
      CWD = new File(dir);
    }
    if (CWD != null) {
      dlg.setCurrentDirectory(CWD);
    }
    int returnVal = dlg.showOpenDialog(this);
    if (returnVal == 0) {
      try {
        String result = dlg.getSelectedFile().getCanonicalPath();
        CWD = dlg.getSelectedFile().getParentFile();
        Properties props = System.getProperties();
        props.put("user.dir", CWD.getPath());
        System.setProperties(props);
        return result;
      }
      catch (IOException localIOException) {}catch (SecurityException localSecurityException) {}
    }
    
    return null;
  }
  


  private JInternalFrame getSelectedFrame()
  {
    JInternalFrame[] frames = desk.getAllFrames();
    for (int i = 0; i < frames.length; i++) {
      if (frames[i].isShowing()) {
        return frames[i];
      }
    }
    return frames[(frames.length - 1)];
  }
  



  private void updateEnabled(boolean interrupted)
  {
    ((Menubar)getJMenuBar()).updateEnabled(interrupted);
    int ci = 0; for (int cc = toolBar.getComponentCount(); ci < cc; ci++) { boolean enableButton;
      boolean enableButton;
      if (ci == 0)
      {
        enableButton = !interrupted;
      } else {
        enableButton = interrupted;
      }
      toolBar.getComponent(ci).setEnabled(enableButton);
    }
    if (interrupted) {
      toolBar.setEnabled(true);
      
      int state = getExtendedState();
      if (state == 1) {
        setExtendedState(0);
      }
      toFront();
      context.setEnabled(true);
    } else {
      if (currentWindow != null)
        currentWindow.setPosition(-1);
      context.setEnabled(false);
    }
  }
  


  static void setResizeWeight(JSplitPane pane, double weight)
  {
    try
    {
      Method m = JSplitPane.class.getMethod("setResizeWeight", new Class[] { Double.TYPE });
      
      m.invoke(pane, new Object[] { new Double(weight) });
    }
    catch (NoSuchMethodException localNoSuchMethodException) {}catch (IllegalAccessException localIllegalAccessException) {}catch (InvocationTargetException localInvocationTargetException) {}
  }
  


  private String readFile(String fileName)
  {
    String text;
    
    try
    {
      Reader r = new FileReader(fileName);
      try {
        text = Kit.readReader(r);
      } finally { String text;
        r.close();
      }
    } catch (IOException ex) { String text;
      MessageDialogWrapper.showMessageDialog(this, ex.getMessage(), "Error reading " + fileName, 0);
      
      text = null;
    }
    return text;
  }
  




  public void updateSourceText(Dim.SourceInfo sourceInfo)
  {
    RunProxy proxy = new RunProxy(this, 3);
    sourceInfo = sourceInfo;
    SwingUtilities.invokeLater(proxy);
  }
  



  public void enterInterrupt(Dim.StackFrame lastFrame, String threadTitle, String alertMessage)
  {
    if (SwingUtilities.isEventDispatchThread()) {
      enterInterruptImpl(lastFrame, threadTitle, alertMessage);
    } else {
      RunProxy proxy = new RunProxy(this, 4);
      lastFrame = lastFrame;
      threadTitle = threadTitle;
      alertMessage = alertMessage;
      SwingUtilities.invokeLater(proxy);
    }
  }
  


  public boolean isGuiEventThread()
  {
    return SwingUtilities.isEventDispatchThread();
  }
  

  public void dispatchNextGuiEvent()
    throws InterruptedException
  {
    EventQueue queue = awtEventQueue;
    if (queue == null) {
      queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      awtEventQueue = queue;
    }
    AWTEvent event = queue.getNextEvent();
    if ((event instanceof ActiveEvent)) {
      ((ActiveEvent)event).dispatch();
    } else {
      Object source = event.getSource();
      if ((source instanceof Component)) {
        Component comp = (Component)source;
        comp.dispatchEvent(event);
      } else if ((source instanceof MenuComponent)) {
        ((MenuComponent)source).dispatchEvent(event);
      }
    }
  }
  




  public void actionPerformed(ActionEvent e)
  {
    String cmd = e.getActionCommand();
    int returnValue = -1;
    if ((cmd.equals("Cut")) || (cmd.equals("Copy")) || (cmd.equals("Paste"))) {
      JInternalFrame f = getSelectedFrame();
      if ((f != null) && ((f instanceof ActionListener))) {
        ((ActionListener)f).actionPerformed(e);
      }
    } else if (cmd.equals("Step Over")) {
      returnValue = 0;
    } else if (cmd.equals("Step Into")) {
      returnValue = 1;
    } else if (cmd.equals("Step Out")) {
      returnValue = 2;
    } else if (cmd.equals("Go")) {
      returnValue = 3;
    } else if (cmd.equals("Break")) {
      dim.setBreak();
    } else if (cmd.equals("Exit")) {
      exit();
    } else if (cmd.equals("Open")) {
      String fileName = chooseFile("Select a file to compile");
      if (fileName != null) {
        String text = readFile(fileName);
        if (text != null) {
          RunProxy proxy = new RunProxy(this, 1);
          fileName = fileName;
          text = text;
          new Thread(proxy).start();
        }
      }
    } else if (cmd.equals("Load")) {
      String fileName = chooseFile("Select a file to execute");
      if (fileName != null) {
        String text = readFile(fileName);
        if (text != null) {
          RunProxy proxy = new RunProxy(this, 2);
          fileName = fileName;
          text = text;
          new Thread(proxy).start();
        }
      }
    } else if (cmd.equals("More Windows...")) {
      MoreWindows dlg = new MoreWindows(this, fileWindows, "Window", "Files");
      
      dlg.showDialog(this);
    } else if (cmd.equals("Console")) {
      if (console.isIcon()) {
        desk.getDesktopManager().deiconifyFrame(console);
      }
      console.show();
      desk.getDesktopManager().activateFrame(console);
      console.consoleTextArea.requestFocus();
    } else if ((!cmd.equals("Cut")) && 
      (!cmd.equals("Copy")) && 
      (!cmd.equals("Paste"))) {
      if (cmd.equals("Go to function...")) {
        FindFunction dlg = new FindFunction(this, "Go to function", "Function");
        
        dlg.showDialog(this);
      } else if (cmd.equals("Go to line...")) {
        String s = (String)JOptionPane.showInputDialog(this, "Line number", "Go to line...", 3, null, null, null);
        

        if (s == null) {
          return;
        }
        try {
          int line = Integer.parseInt(s);
          showFileWindow(null, line);

        }
        catch (NumberFormatException localNumberFormatException) {}
      }
      else if (cmd.equals("Tile")) {
        JInternalFrame[] frames = desk.getAllFrames();
        int count = frames.length;
        int cols;
        int rows = cols = (int)Math.sqrt(count);
        if (rows * cols < count) {
          cols++;
          if (rows * cols < count) {
            rows++;
          }
        }
        Dimension size = desk.getSize();
        int w = width / cols;
        int h = height / rows;
        int x = 0;
        int y = 0;
        for (int i = 0; i < rows; i++) {
          for (int j = 0; j < cols; j++) {
            int index = i * cols + j;
            if (index >= frames.length) {
              break;
            }
            JInternalFrame f = frames[index];
            try {
              f.setIcon(false);
              f.setMaximum(false);
            }
            catch (Exception localException) {}
            desk.getDesktopManager().setBoundsForFrame(f, x, y, w, h);
            x += w;
          }
          y += h;
          x = 0;
        }
      } else if (cmd.equals("Cascade")) {
        JInternalFrame[] frames = desk.getAllFrames();
        int count = frames.length;
        int y;
        int x = y = 0;
        int h = desk.getHeight();
        int d = h / count;
        if (d > 30)
          d = 30;
        for (int i = count - 1; i >= 0; y += d) {
          JInternalFrame f = frames[i];
          try {
            f.setIcon(false);
            f.setMaximum(false);
          }
          catch (Exception localException1) {}
          Dimension dimen = f.getPreferredSize();
          int w = width;
          h = height;
          desk.getDesktopManager().setBoundsForFrame(f, x, y, w, h);i--;x += d;
        }
        
      }
      else
      {
        Object obj = getFileWindow(cmd);
        if (obj != null) {
          FileWindow w = (FileWindow)obj;
          try {
            if (w.isIcon()) {
              w.setIcon(false);
            }
            w.setVisible(true);
            w.moveToFront();
            w.setSelected(true);
          } catch (Exception localException2) {}
        }
      }
    }
    if (returnValue != -1) {
      updateEnabled(false);
      dim.setReturnValue(returnValue);
    }
  }
}
