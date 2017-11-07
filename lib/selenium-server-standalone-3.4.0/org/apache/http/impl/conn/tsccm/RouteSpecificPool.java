package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.LangUtils;






































@Deprecated
public class RouteSpecificPool
{
  private final Log log = LogFactory.getLog(getClass());
  


  protected final HttpRoute route;
  


  protected final int maxEntries;
  


  protected final ConnPerRoute connPerRoute;
  

  protected final LinkedList<BasicPoolEntry> freeEntries;
  

  protected final Queue<WaitingThread> waitingThreads;
  

  protected int numEntries;
  


  @Deprecated
  public RouteSpecificPool(HttpRoute route, int maxEntries)
  {
    this.route = route;
    this.maxEntries = maxEntries;
    connPerRoute = new ConnPerRoute()
    {
      public int getMaxForRoute(HttpRoute unused) {
        return maxEntries;
      }
    };
    freeEntries = new LinkedList();
    waitingThreads = new LinkedList();
    numEntries = 0;
  }
  






  public RouteSpecificPool(HttpRoute route, ConnPerRoute connPerRoute)
  {
    this.route = route;
    this.connPerRoute = connPerRoute;
    maxEntries = connPerRoute.getMaxForRoute(route);
    freeEntries = new LinkedList();
    waitingThreads = new LinkedList();
    numEntries = 0;
  }
  





  public final HttpRoute getRoute()
  {
    return route;
  }
  





  public final int getMaxEntries()
  {
    return maxEntries;
  }
  








  public boolean isUnused()
  {
    return (numEntries < 1) && (waitingThreads.isEmpty());
  }
  





  public int getCapacity()
  {
    return connPerRoute.getMaxForRoute(route) - numEntries;
  }
  







  public final int getEntryCount()
  {
    return numEntries;
  }
  





  public BasicPoolEntry allocEntry(Object state)
  {
    if (!freeEntries.isEmpty()) {
      ListIterator<BasicPoolEntry> it = freeEntries.listIterator(freeEntries.size());
      while (it.hasPrevious()) {
        BasicPoolEntry entry = (BasicPoolEntry)it.previous();
        if ((entry.getState() == null) || (LangUtils.equals(state, entry.getState()))) {
          it.remove();
          return entry;
        }
      }
    }
    if ((getCapacity() == 0) && (!freeEntries.isEmpty())) {
      BasicPoolEntry entry = (BasicPoolEntry)freeEntries.remove();
      entry.shutdownEntry();
      OperatedClientConnection conn = entry.getConnection();
      try {
        conn.close();
      } catch (IOException ex) {
        log.debug("I/O error closing connection", ex);
      }
      return entry;
    }
    return null;
  }
  






  public void freeEntry(BasicPoolEntry entry)
  {
    if (numEntries < 1) {
      throw new IllegalStateException("No entry created for this pool. " + route);
    }
    
    if (numEntries <= freeEntries.size()) {
      throw new IllegalStateException("No entry allocated from this pool. " + route);
    }
    
    freeEntries.add(entry);
  }
  








  public void createdEntry(BasicPoolEntry entry)
  {
    Args.check(route.equals(entry.getPlannedRoute()), "Entry not planned for this pool");
    numEntries += 1;
  }
  











  public boolean deleteEntry(BasicPoolEntry entry)
  {
    boolean found = freeEntries.remove(entry);
    if (found) {
      numEntries -= 1;
    }
    return found;
  }
  






  public void dropEntry()
  {
    Asserts.check(numEntries > 0, "There is no entry that could be dropped");
    numEntries -= 1;
  }
  








  public void queueThread(WaitingThread wt)
  {
    Args.notNull(wt, "Waiting thread");
    waitingThreads.add(wt);
  }
  






  public boolean hasThread()
  {
    return !waitingThreads.isEmpty();
  }
  





  public WaitingThread nextThread()
  {
    return (WaitingThread)waitingThreads.peek();
  }
  





  public void removeThread(WaitingThread wt)
  {
    if (wt == null) {
      return;
    }
    
    waitingThreads.remove(wt);
  }
}
