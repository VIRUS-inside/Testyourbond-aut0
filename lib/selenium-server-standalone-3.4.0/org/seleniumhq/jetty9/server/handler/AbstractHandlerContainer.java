package org.seleniumhq.jetty9.server.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.HandlerContainer;
import org.seleniumhq.jetty9.server.Server;






























public abstract class AbstractHandlerContainer
  extends AbstractHandler
  implements HandlerContainer
{
  public AbstractHandlerContainer() {}
  
  public Handler[] getChildHandlers()
  {
    List<Handler> list = new ArrayList();
    expandChildren(list, null);
    return (Handler[])list.toArray(new Handler[list.size()]);
  }
  


  public Handler[] getChildHandlersByClass(Class<?> byclass)
  {
    List<Handler> list = new ArrayList();
    expandChildren(list, byclass);
    return (Handler[])list.toArray(new Handler[list.size()]);
  }
  



  public <T extends Handler> T getChildHandlerByClass(Class<T> byclass)
  {
    List<Handler> list = new ArrayList();
    expandChildren(list, byclass);
    if (list.isEmpty())
      return null;
    return (Handler)list.get(0);
  }
  


  protected void expandChildren(List<Handler> list, Class<?> byClass) {}
  


  protected void expandHandler(Handler handler, List<Handler> list, Class<?> byClass)
  {
    if (handler == null) {
      return;
    }
    if ((byClass == null) || (byClass.isAssignableFrom(handler.getClass()))) {
      list.add(handler);
    }
    if ((handler instanceof AbstractHandlerContainer)) {
      ((AbstractHandlerContainer)handler).expandChildren(list, byClass);
    } else if ((handler instanceof HandlerContainer))
    {
      HandlerContainer container = (HandlerContainer)handler;
      Handler[] handlers = byClass == null ? container.getChildHandlers() : container.getChildHandlersByClass(byClass);
      list.addAll(Arrays.asList(handlers));
    }
  }
  

  public static <T extends HandlerContainer> T findContainerOf(HandlerContainer root, Class<T> type, Handler handler)
  {
    if ((root == null) || (handler == null)) {
      return null;
    }
    Handler[] branches = root.getChildHandlersByClass(type);
    if (branches != null)
    {
      for (Handler h : branches)
      {

        T container = (HandlerContainer)h;
        Handler[] candidates = container.getChildHandlersByClass(handler.getClass());
        if (candidates != null)
        {
          for (Handler c : candidates)
            if (c == handler)
              return container;
        }
      }
    }
    return null;
  }
  


  public void setServer(Server server)
  {
    if (server == getServer()) {
      return;
    }
    if (isStarted()) {
      throw new IllegalStateException("STARTED");
    }
    super.setServer(server);
    Handler[] handlers = getHandlers();
    if (handlers != null) {
      for (Handler h : handlers) {
        h.setServer(server);
      }
    }
  }
}
