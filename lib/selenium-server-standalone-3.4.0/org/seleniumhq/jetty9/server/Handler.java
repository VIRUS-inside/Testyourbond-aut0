package org.seleniumhq.jetty9.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.ManagedOperation;
import org.seleniumhq.jetty9.util.component.Destroyable;
import org.seleniumhq.jetty9.util.component.LifeCycle;

@ManagedObject("Jetty Handler")
public abstract interface Handler
  extends LifeCycle, Destroyable
{
  public abstract void handle(String paramString, Request paramRequest, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws IOException, ServletException;
  
  public abstract void setServer(Server paramServer);
  
  @ManagedAttribute(value="the jetty server for this handler", readonly=true)
  public abstract Server getServer();
  
  @ManagedOperation(value="destroy associated resources", impact="ACTION")
  public abstract void destroy();
}
