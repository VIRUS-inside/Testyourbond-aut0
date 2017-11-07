package net.sourceforge.htmlunit.corejs.javascript.tools.shell;

import java.io.InputStream;
import java.nio.charset.Charset;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;










@Deprecated
public class ShellLine
{
  public ShellLine() {}
  
  @Deprecated
  public static InputStream getStream(Scriptable scope)
  {
    ShellConsole console = ShellConsole.getConsole(scope, 
      Charset.defaultCharset());
    return console != null ? console.getIn() : null;
  }
}
