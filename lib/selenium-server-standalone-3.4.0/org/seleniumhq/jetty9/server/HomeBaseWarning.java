package org.seleniumhq.jetty9.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

























public class HomeBaseWarning
{
  private static final Logger LOG = Log.getLogger(HomeBaseWarning.class);
  
  public HomeBaseWarning()
  {
    boolean showWarn = false;
    
    String home = System.getProperty("jetty.home");
    String base = System.getProperty("jetty.base");
    
    if (StringUtil.isBlank(base))
    {


      return;
    }
    
    Path homePath = new File(home).toPath();
    Path basePath = new File(base).toPath();
    
    try
    {
      showWarn = Files.isSameFile(homePath, basePath);
    }
    catch (IOException e)
    {
      LOG.ignore(e);
      
      return;
    }
    
    if (showWarn)
    {
      StringBuilder warn = new StringBuilder();
      warn.append("This instance of Jetty is not running from a separate {jetty.base} directory");
      warn.append(", this is not recommended.  See documentation at http://www.eclipse.org/jetty/documentation/current/startup.html");
      LOG.warn("{}", new Object[] { warn.toString() });
    }
  }
}
