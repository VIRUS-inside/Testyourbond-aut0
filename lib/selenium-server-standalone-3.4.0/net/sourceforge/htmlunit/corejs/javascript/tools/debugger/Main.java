package net.sourceforge.htmlunit.corejs.javascript.tools.debugger;

import java.awt.Dimension;
import java.io.InputStream;
import java.io.PrintStream;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Kit;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.tools.shell.Global;

















public class Main
{
  private Dim dim;
  private SwingGui debugGui;
  
  public Main(String title)
  {
    dim = new Dim();
    debugGui = new SwingGui(dim, title);
  }
  


  public JFrame getDebugFrame()
  {
    return debugGui;
  }
  


  public void doBreak()
  {
    dim.setBreak();
  }
  


  public void setBreakOnExceptions(boolean value)
  {
    dim.setBreakOnExceptions(value);
    debugGui.getMenubar().getBreakOnExceptions().setSelected(value);
  }
  


  public void setBreakOnEnter(boolean value)
  {
    dim.setBreakOnEnter(value);
    debugGui.getMenubar().getBreakOnEnter().setSelected(value);
  }
  


  public void setBreakOnReturn(boolean value)
  {
    dim.setBreakOnReturn(value);
    debugGui.getMenubar().getBreakOnReturn().setSelected(value);
  }
  


  public void clearAllBreakpoints()
  {
    dim.clearAllBreakpoints();
  }
  


  public void go()
  {
    dim.go();
  }
  


  public void setScope(Scriptable scope)
  {
    setScopeProvider(IProxy.newScopeProvider(scope));
  }
  



  public void setScopeProvider(ScopeProvider p)
  {
    dim.setScopeProvider(p);
  }
  



  public void setSourceProvider(SourceProvider sourceProvider)
  {
    dim.setSourceProvider(sourceProvider);
  }
  



  public void setExitAction(Runnable r)
  {
    debugGui.setExitAction(r);
  }
  



  public InputStream getIn()
  {
    return debugGui.getConsole().getIn();
  }
  



  public PrintStream getOut()
  {
    return debugGui.getConsole().getOut();
  }
  



  public PrintStream getErr()
  {
    return debugGui.getConsole().getErr();
  }
  


  public void pack()
  {
    debugGui.pack();
  }
  


  public void setSize(int w, int h)
  {
    debugGui.setSize(w, h);
  }
  


  public void setVisible(boolean flag)
  {
    debugGui.setVisible(flag);
  }
  


  public boolean isVisible()
  {
    return debugGui.isVisible();
  }
  


  public void dispose()
  {
    clearAllBreakpoints();
    dim.go();
    debugGui.dispose();
    dim = null;
  }
  


  public void attachTo(ContextFactory factory)
  {
    dim.attachTo(factory);
  }
  


  public void detach()
  {
    dim.detach();
  }
  




  public static void main(String[] args)
  {
    Main main = new Main("Rhino JavaScript Debugger");
    main.doBreak();
    main.setExitAction(new IProxy(1));
    
    System.setIn(main.getIn());
    System.setOut(main.getOut());
    System.setErr(main.getErr());
    

    Global global = net.sourceforge.htmlunit.corejs.javascript.tools.shell.Main.getGlobal();
    global.setIn(main.getIn());
    global.setOut(main.getOut());
    global.setErr(main.getErr());
    
    main.attachTo(net.sourceforge.htmlunit.corejs.javascript.tools.shell.Main.shellContextFactory);
    

    main.setScope(global);
    
    main.pack();
    main.setSize(600, 460);
    main.setVisible(true);
    
    net.sourceforge.htmlunit.corejs.javascript.tools.shell.Main.exec(args);
  }
  




  public static Main mainEmbedded(String title)
  {
    ContextFactory factory = ContextFactory.getGlobal();
    Global global = new Global();
    global.init(factory);
    return mainEmbedded(factory, global, title);
  }
  





  public static Main mainEmbedded(ContextFactory factory, Scriptable scope, String title)
  {
    return mainEmbeddedImpl(factory, scope, title);
  }
  





  public static Main mainEmbedded(ContextFactory factory, ScopeProvider scopeProvider, String title)
  {
    return mainEmbeddedImpl(factory, scopeProvider, title);
  }
  



  private static Main mainEmbeddedImpl(ContextFactory factory, Object scopeProvider, String title)
  {
    if (title == null) {
      title = "Rhino JavaScript Debugger (embedded usage)";
    }
    Main main = new Main(title);
    main.doBreak();
    main.setExitAction(new IProxy(1));
    
    main.attachTo(factory);
    if ((scopeProvider instanceof ScopeProvider)) {
      main.setScopeProvider((ScopeProvider)scopeProvider);
    } else {
      Scriptable scope = (Scriptable)scopeProvider;
      if ((scope instanceof Global)) {
        Global global = (Global)scope;
        global.setIn(main.getIn());
        global.setOut(main.getOut());
        global.setErr(main.getErr());
      }
      main.setScope(scope);
    }
    
    main.pack();
    main.setSize(600, 460);
    main.setVisible(true);
    return main;
  }
  




  @Deprecated
  public void setSize(Dimension dimension)
  {
    debugGui.setSize(width, height);
  }
  




  @Deprecated
  public void setOptimizationLevel(int level) {}
  




  @Deprecated
  public void contextEntered(Context cx)
  {
    throw new IllegalStateException();
  }
  



  @Deprecated
  public void contextExited(Context cx)
  {
    throw new IllegalStateException();
  }
  



  @Deprecated
  public void contextCreated(Context cx)
  {
    throw new IllegalStateException();
  }
  



  @Deprecated
  public void contextReleased(Context cx)
  {
    throw new IllegalStateException();
  }
  



  private static class IProxy
    implements Runnable, ScopeProvider
  {
    public static final int EXIT_ACTION = 1;
    


    public static final int SCOPE_PROVIDER = 2;
    


    private final int type;
    


    private Scriptable scope;
    



    public IProxy(int type)
    {
      this.type = type;
    }
    


    public static ScopeProvider newScopeProvider(Scriptable scope)
    {
      IProxy scopeProvider = new IProxy(2);
      scope = scope;
      return scopeProvider;
    }
    




    public void run()
    {
      if (type != 1)
        Kit.codeBug();
      System.exit(0);
    }
    




    public Scriptable getScope()
    {
      if (type != 2)
        Kit.codeBug();
      if (scope == null)
        Kit.codeBug();
      return scope;
    }
  }
}
