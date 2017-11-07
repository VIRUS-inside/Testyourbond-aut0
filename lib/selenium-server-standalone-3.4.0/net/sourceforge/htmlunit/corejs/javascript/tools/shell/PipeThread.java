package net.sourceforge.htmlunit.corejs.javascript.tools.shell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.sourceforge.htmlunit.corejs.javascript.Context;
















































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































class PipeThread
  extends Thread
{
  private boolean fromProcess;
  private InputStream from;
  private OutputStream to;
  
  PipeThread(boolean fromProcess, InputStream from, OutputStream to)
  {
    setDaemon(true);
    this.fromProcess = fromProcess;
    this.from = from;
    this.to = to;
  }
  
  public void run()
  {
    try {
      Global.pipe(fromProcess, from, to);
    } catch (IOException ex) {
      throw Context.throwAsScriptRuntimeEx(ex);
    }
  }
}