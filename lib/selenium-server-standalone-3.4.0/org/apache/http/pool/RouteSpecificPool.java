package org.apache.http.pool;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.Future;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;




























abstract class RouteSpecificPool<T, C, E extends PoolEntry<T, C>>
{
  private final T route;
  private final Set<E> leased;
  private final LinkedList<E> available;
  private final LinkedList<Future<E>> pending;
  
  RouteSpecificPool(T route)
  {
    this.route = route;
    leased = new HashSet();
    available = new LinkedList();
    pending = new LinkedList();
  }
  
  protected abstract E createEntry(C paramC);
  
  public final T getRoute() {
    return route;
  }
  
  public int getLeasedCount() {
    return leased.size();
  }
  
  public int getPendingCount() {
    return pending.size();
  }
  
  public int getAvailableCount() {
    return available.size();
  }
  
  public int getAllocatedCount() {
    return available.size() + leased.size();
  }
  
  public E getFree(Object state) {
    if (!available.isEmpty()) {
      if (state != null) {
        Iterator<E> it = available.iterator();
        while (it.hasNext()) {
          E entry = (PoolEntry)it.next();
          if (state.equals(entry.getState())) {
            it.remove();
            leased.add(entry);
            return entry;
          }
        }
      }
      Iterator<E> it = available.iterator();
      while (it.hasNext()) {
        E entry = (PoolEntry)it.next();
        if (entry.getState() == null) {
          it.remove();
          leased.add(entry);
          return entry;
        }
      }
    }
    return null;
  }
  
  public E getLastUsed() {
    if (!available.isEmpty()) {
      return (PoolEntry)available.getLast();
    }
    return null;
  }
  
  public boolean remove(E entry)
  {
    Args.notNull(entry, "Pool entry");
    if ((!available.remove(entry)) && 
      (!leased.remove(entry))) {
      return false;
    }
    
    return true;
  }
  
  public void free(E entry, boolean reusable) {
    Args.notNull(entry, "Pool entry");
    boolean found = leased.remove(entry);
    Asserts.check(found, "Entry %s has not been leased from this pool", entry);
    if (reusable) {
      available.addFirst(entry);
    }
  }
  
  public E add(C conn) {
    E entry = createEntry(conn);
    leased.add(entry);
    return entry;
  }
  
  public void queue(Future<E> future) {
    if (future == null) {
      return;
    }
    pending.add(future);
  }
  
  public Future<E> nextPending() {
    return (Future)pending.poll();
  }
  
  public void unqueue(Future<E> future) {
    if (future == null) {
      return;
    }
    
    pending.remove(future);
  }
  
  public void shutdown() {
    for (Future<E> future : pending) {
      future.cancel(true);
    }
    pending.clear();
    for (E entry : available) {
      entry.close();
    }
    available.clear();
    for (E entry : leased) {
      entry.close();
    }
    leased.clear();
  }
  
  public String toString()
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append("[route: ");
    buffer.append(route);
    buffer.append("][leased: ");
    buffer.append(leased.size());
    buffer.append("][available: ");
    buffer.append(available.size());
    buffer.append("][pending: ");
    buffer.append(pending.size());
    buffer.append("]");
    return buffer.toString();
  }
}
