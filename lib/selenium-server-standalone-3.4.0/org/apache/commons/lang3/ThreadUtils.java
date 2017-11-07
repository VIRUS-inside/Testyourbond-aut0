package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;









































public class ThreadUtils
{
  public static Thread findThreadById(long threadId, ThreadGroup threadGroup)
  {
    if (threadGroup == null) {
      throw new IllegalArgumentException("The thread group must not be null");
    }
    Thread thread = findThreadById(threadId);
    if ((thread != null) && (threadGroup.equals(thread.getThreadGroup()))) {
      return thread;
    }
    return null;
  }
  













  public static Thread findThreadById(long threadId, String threadGroupName)
  {
    if (threadGroupName == null) {
      throw new IllegalArgumentException("The thread group name must not be null");
    }
    Thread thread = findThreadById(threadId);
    if ((thread != null) && (thread.getThreadGroup() != null) && (thread.getThreadGroup().getName().equals(threadGroupName))) {
      return thread;
    }
    return null;
  }
  













  public static Collection<Thread> findThreadsByName(String threadName, ThreadGroup threadGroup)
  {
    return findThreads(threadGroup, false, new NamePredicate(threadName));
  }
  













  public static Collection<Thread> findThreadsByName(String threadName, String threadGroupName)
  {
    if (threadName == null) {
      throw new IllegalArgumentException("The thread name must not be null");
    }
    if (threadGroupName == null) {
      throw new IllegalArgumentException("The thread group name must not be null");
    }
    
    Collection<ThreadGroup> threadGroups = findThreadGroups(new NamePredicate(threadGroupName));
    
    if (threadGroups.isEmpty()) {
      return Collections.emptyList();
    }
    
    Collection<Thread> result = new ArrayList();
    NamePredicate threadNamePredicate = new NamePredicate(threadName);
    for (ThreadGroup group : threadGroups) {
      result.addAll(findThreads(group, false, threadNamePredicate));
    }
    return Collections.unmodifiableCollection(result);
  }
  











  public static Collection<ThreadGroup> findThreadGroupsByName(String threadGroupName)
  {
    return findThreadGroups(new NamePredicate(threadGroupName));
  }
  









  public static Collection<ThreadGroup> getAllThreadGroups()
  {
    return findThreadGroups(ALWAYS_TRUE_PREDICATE);
  }
  






  public static ThreadGroup getSystemThreadGroup()
  {
    ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
    while (threadGroup.getParent() != null) {
      threadGroup = threadGroup.getParent();
    }
    return threadGroup;
  }
  









  public static Collection<Thread> getAllThreads()
  {
    return findThreads(ALWAYS_TRUE_PREDICATE);
  }
  











  public static Collection<Thread> findThreadsByName(String threadName)
  {
    return findThreads(new NamePredicate(threadName));
  }
  











  public static Thread findThreadById(long threadId)
  {
    Collection<Thread> result = findThreads(new ThreadIdPredicate(threadId));
    return result.isEmpty() ? null : (Thread)result.iterator().next();
  }
  












































  public static final AlwaysTruePredicate ALWAYS_TRUE_PREDICATE = new AlwaysTruePredicate(null);
  public ThreadUtils() {}
  
  public static abstract interface ThreadPredicate {
    public abstract boolean test(Thread paramThread);
  }
  
  public static abstract interface ThreadGroupPredicate { public abstract boolean test(ThreadGroup paramThreadGroup);
  }
  
  private static final class AlwaysTruePredicate implements ThreadUtils.ThreadPredicate, ThreadUtils.ThreadGroupPredicate { private AlwaysTruePredicate() {}
    
    public boolean test(ThreadGroup threadGroup) { return true; }
    

    public boolean test(Thread thread)
    {
      return true;
    }
  }
  




  public static class NamePredicate
    implements ThreadUtils.ThreadPredicate, ThreadUtils.ThreadGroupPredicate
  {
    private final String name;
    




    public NamePredicate(String name)
    {
      if (name == null) {
        throw new IllegalArgumentException("The name must not be null");
      }
      this.name = name;
    }
    
    public boolean test(ThreadGroup threadGroup)
    {
      return (threadGroup != null) && (threadGroup.getName().equals(name));
    }
    
    public boolean test(Thread thread)
    {
      return (thread != null) && (thread.getName().equals(name));
    }
  }
  




  public static class ThreadIdPredicate
    implements ThreadUtils.ThreadPredicate
  {
    private final long threadId;
    




    public ThreadIdPredicate(long threadId)
    {
      if (threadId <= 0L) {
        throw new IllegalArgumentException("The thread id must be greater than zero");
      }
      this.threadId = threadId;
    }
    
    public boolean test(Thread thread)
    {
      return (thread != null) && (thread.getId() == threadId);
    }
  }
  











  public static Collection<Thread> findThreads(ThreadPredicate predicate)
  {
    return findThreads(getSystemThreadGroup(), true, predicate);
  }
  










  public static Collection<ThreadGroup> findThreadGroups(ThreadGroupPredicate predicate)
  {
    return findThreadGroups(getSystemThreadGroup(), true, predicate);
  }
  










  public static Collection<Thread> findThreads(ThreadGroup group, boolean recurse, ThreadPredicate predicate)
  {
    if (group == null) {
      throw new IllegalArgumentException("The group must not be null");
    }
    if (predicate == null) {
      throw new IllegalArgumentException("The predicate must not be null");
    }
    
    int count = group.activeCount();
    Thread[] threads;
    do {
      threads = new Thread[count + count / 2 + 1];
      count = group.enumerate(threads, recurse);
    }
    while (count >= threads.length);
    
    List<Thread> result = new ArrayList(count);
    for (int i = 0; i < count; i++) {
      if (predicate.test(threads[i])) {
        result.add(threads[i]);
      }
    }
    return Collections.unmodifiableCollection(result);
  }
  










  public static Collection<ThreadGroup> findThreadGroups(ThreadGroup group, boolean recurse, ThreadGroupPredicate predicate)
  {
    if (group == null) {
      throw new IllegalArgumentException("The group must not be null");
    }
    if (predicate == null) {
      throw new IllegalArgumentException("The predicate must not be null");
    }
    
    int count = group.activeGroupCount();
    ThreadGroup[] threadGroups;
    do {
      threadGroups = new ThreadGroup[count + count / 2 + 1];
      count = group.enumerate(threadGroups, recurse);
    }
    while (count >= threadGroups.length);
    
    List<ThreadGroup> result = new ArrayList(count);
    for (int i = 0; i < count; i++) {
      if (predicate.test(threadGroups[i])) {
        result.add(threadGroups[i]);
      }
    }
    return Collections.unmodifiableCollection(result);
  }
}
