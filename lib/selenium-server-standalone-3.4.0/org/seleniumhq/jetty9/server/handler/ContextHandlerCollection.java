package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.HandlerContainer;
import org.seleniumhq.jetty9.server.HttpChannelState;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.util.ArrayTernaryTrie;
import org.seleniumhq.jetty9.util.ArrayUtil;
import org.seleniumhq.jetty9.util.Trie;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.ManagedOperation;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;





























@ManagedObject("Context Handler Collection")
public class ContextHandlerCollection
  extends HandlerCollection
{
  private static final Logger LOG = Log.getLogger(ContextHandlerCollection.class);
  
  private final ConcurrentMap<ContextHandler, Handler> _contextBranches = new ConcurrentHashMap();
  private volatile Trie<Map.Entry<String, Branch[]>> _pathBranches;
  private Class<? extends ContextHandler> _contextClass = ContextHandler.class;
  

  public ContextHandlerCollection()
  {
    super(true);
  }
  





  @ManagedOperation("update the mapping of context path to context")
  public void mapContexts()
  {
    _contextBranches.clear();
    
    if (getHandlers() == null)
    {
      _pathBranches = new ArrayTernaryTrie(false, 16);
      return;
    }
    

    Map<String, Branch[]> map = new HashMap();
    Object localObject2; String contextPath; Branch[] branches; for (Handler handler : getHandlers())
    {
      Branch branch = new Branch(handler);
      for (localObject2 = branch.getContextPaths().iterator(); ((Iterator)localObject2).hasNext();) { contextPath = (String)((Iterator)localObject2).next();
        
        branches = (Branch[])map.get(contextPath);
        map.put(contextPath, ArrayUtil.addToArray(branches, branch, Branch.class));
      }
      
      localObject2 = branch.getContextHandlers();contextPath = localObject2.length; for (branches = 0; branches < contextPath; branches++) { ContextHandler context = localObject2[branches];
        _contextBranches.putIfAbsent(context, branch.getHandler());
      }
    }
    
    for (??? = map.entrySet().iterator(); ((Iterator)???).hasNext();) { Object entry = (Map.Entry)((Iterator)???).next();
      
      branches = (Branch[])((Map.Entry)entry).getValue();
      Branch[] sorted = new Branch[branches.length];
      int i = 0;
      localObject2 = branches;contextPath = localObject2.length; for (branches = 0; branches < contextPath; branches++) { Branch branch = localObject2[branches];
        if (branch.hasVirtualHost())
          sorted[(i++)] = branch; }
      localObject2 = branches;contextPath = localObject2.length; for (branches = 0; branches < contextPath; branches++) { Branch branch = localObject2[branches];
        if (!branch.hasVirtualHost())
          sorted[(i++)] = branch; }
      ((Map.Entry)entry).setValue(sorted);
    }
    

    int capacity = 512;
    


    Object trie = new ArrayTernaryTrie(false, capacity);
    Object branches = map.entrySet().iterator(); for (;;) { if (!((Iterator)branches).hasNext()) break label450; Map.Entry<String, Branch[]> entry = (Map.Entry)((Iterator)branches).next();
      
      if (!((Trie)trie).put(((String)entry.getKey()).substring(1), entry))
      {
        capacity += 512;
        break;
      }
    }
    

    label450:
    
    if (LOG.isDebugEnabled())
    {
      for (branches = ((Trie)trie).keySet().iterator(); ((Iterator)branches).hasNext();) { String ctx = (String)((Iterator)branches).next();
        LOG.debug("{}->{}", new Object[] { ctx, Arrays.asList((Object[])((Map.Entry)((Trie)trie).get(ctx)).getValue()) });
      } }
    _pathBranches = ((Trie)trie);
  }
  





  public void setHandlers(Handler[] handlers)
  {
    super.setHandlers(handlers);
    if (isStarted()) {
      mapContexts();
    }
  }
  
  protected void doStart()
    throws Exception
  {
    mapContexts();
    super.doStart();
  }
  





  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    Handler[] handlers = getHandlers();
    if ((handlers == null) || (handlers.length == 0)) {
      return;
    }
    HttpChannelState async = baseRequest.getHttpChannelState();
    if (async.isAsync())
    {
      ContextHandler context = async.getContextHandler();
      if (context != null)
      {
        Handler branch = (Handler)_contextBranches.get(context);
        
        if (branch == null) {
          context.handle(target, baseRequest, request, response);
        } else
          branch.handle(target, baseRequest, request, response);
        return;
      }
    }
    



    if (target.startsWith("/"))
    {
      int limit = target.length() - 1;
      
      while (limit >= 0)
      {

        Map.Entry<String, Branch[]> branches = (Map.Entry)_pathBranches.getBest(target, 1, limit);
        

        if (branches == null) {
          break;
        }
        int l = ((String)branches.getKey()).length();
        if ((l == 1) || (target.length() == l) || (target.charAt(l) == '/'))
        {
          for (Branch branch : (Branch[])branches.getValue())
          {
            branch.getHandler().handle(target, baseRequest, request, response);
            if (baseRequest.isHandled()) {
              return;
            }
          }
        }
        limit = l - 2;
      }
      
    }
    else
    {
      for (int i = 0; i < handlers.length; i++)
      {
        handlers[i].handle(target, baseRequest, request, response);
        if (baseRequest.isHandled()) {
          return;
        }
      }
    }
  }
  





  public ContextHandler addContext(String contextPath, String resourceBase)
  {
    try
    {
      ContextHandler context = (ContextHandler)_contextClass.newInstance();
      context.setContextPath(contextPath);
      context.setResourceBase(resourceBase);
      addHandler(context);
      return context;
    }
    catch (Exception e)
    {
      LOG.debug(e);
      throw new Error(e);
    }
  }
  






  public Class<?> getContextClass()
  {
    return _contextClass;
  }
  





  public void setContextClass(Class<? extends ContextHandler> contextClass)
  {
    if ((contextClass == null) || (!ContextHandler.class.isAssignableFrom(contextClass)))
      throw new IllegalArgumentException();
    _contextClass = contextClass;
  }
  

  private static final class Branch
  {
    private final Handler _handler;
    
    private final ContextHandler[] _contexts;
    

    Branch(Handler handler)
    {
      _handler = handler;
      
      if ((handler instanceof ContextHandler))
      {
        _contexts = new ContextHandler[] { (ContextHandler)handler };
      }
      else if ((handler instanceof HandlerContainer))
      {
        Handler[] contexts = ((HandlerContainer)handler).getChildHandlersByClass(ContextHandler.class);
        _contexts = new ContextHandler[contexts.length];
        System.arraycopy(contexts, 0, _contexts, 0, contexts.length);
      }
      else {
        _contexts = new ContextHandler[0];
      }
    }
    
    Set<String> getContextPaths() {
      Set<String> set = new HashSet();
      for (ContextHandler context : _contexts)
        set.add(context.getContextPath());
      return set;
    }
    
    boolean hasVirtualHost()
    {
      for (ContextHandler context : _contexts)
        if ((context.getVirtualHosts() != null) && (context.getVirtualHosts().length > 0))
          return true;
      return false;
    }
    
    ContextHandler[] getContextHandlers()
    {
      return _contexts;
    }
    
    Handler getHandler()
    {
      return _handler;
    }
    

    public String toString()
    {
      return String.format("{%s,%s}", new Object[] { _handler, Arrays.asList(_contexts) });
    }
  }
}
