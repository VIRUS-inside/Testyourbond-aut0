package org.seleniumhq.jetty9.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import org.seleniumhq.jetty9.server.handler.ContextHandler.Context;
import org.seleniumhq.jetty9.util.MultiException;
import org.seleniumhq.jetty9.util.MultiPartInputStreamParser;


















public class MultiPartCleanerListener
  implements ServletRequestListener
{
  public static final MultiPartCleanerListener INSTANCE = new MultiPartCleanerListener();
  


  protected MultiPartCleanerListener() {}
  


  public void requestDestroyed(ServletRequestEvent sre)
  {
    MultiPartInputStreamParser mpis = (MultiPartInputStreamParser)sre.getServletRequest().getAttribute("org.seleniumhq.jetty9.multiPartInputStream");
    if (mpis != null)
    {
      ContextHandler.Context context = (ContextHandler.Context)sre.getServletRequest().getAttribute("org.seleniumhq.jetty9.multiPartContext");
      

      if (context == sre.getServletContext())
      {
        try
        {
          mpis.deleteParts();
        }
        catch (MultiException e)
        {
          sre.getServletContext().log("Errors deleting multipart tmp files", e);
        }
      }
    }
  }
  
  public void requestInitialized(ServletRequestEvent sre) {}
}
