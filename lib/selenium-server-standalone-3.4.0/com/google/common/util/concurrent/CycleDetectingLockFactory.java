package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.j2objc.annotations.Weak;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;























































































































































@Beta
@CanIgnoreReturnValue
@ThreadSafe
@GwtIncompatible
public class CycleDetectingLockFactory
{
  @Beta
  @ThreadSafe
  public static abstract interface Policy
  {
    public abstract void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException paramPotentialDeadlockException);
  }
  
  @Beta
  public static abstract enum Policies
    implements CycleDetectingLockFactory.Policy
  {
    THROW, 
    










    WARN, 
    













    DISABLED;
    


    private Policies() {}
  }
  

  public static CycleDetectingLockFactory newInstance(Policy policy)
  {
    return new CycleDetectingLockFactory(policy);
  }
  


  public ReentrantLock newReentrantLock(String lockName)
  {
    return newReentrantLock(lockName, false);
  }
  



  public ReentrantLock newReentrantLock(String lockName, boolean fair)
  {
    return policy == Policies.DISABLED ? new ReentrantLock(fair) : new CycleDetectingReentrantLock(new LockGraphNode(lockName), fair, null);
  }
  




  public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName)
  {
    return newReentrantReadWriteLock(lockName, false);
  }
  




  public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName, boolean fair)
  {
    return policy == Policies.DISABLED ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock(new LockGraphNode(lockName), fair, null);
  }
  




  private static final ConcurrentMap<Class<? extends Enum>, Map<? extends Enum, LockGraphNode>> lockGraphNodesPerType = new MapMaker().weakKeys().makeMap();
  





  public static <E extends Enum<E>> WithExplicitOrdering<E> newInstanceWithExplicitOrdering(Class<E> enumClass, Policy policy)
  {
    Preconditions.checkNotNull(enumClass);
    Preconditions.checkNotNull(policy);
    
    Map<E, LockGraphNode> lockGraphNodes = getOrCreateNodes(enumClass);
    return new WithExplicitOrdering(policy, lockGraphNodes);
  }
  
  private static Map<? extends Enum, LockGraphNode> getOrCreateNodes(Class<? extends Enum> clazz) {
    Map<? extends Enum, LockGraphNode> existing = (Map)lockGraphNodesPerType.get(clazz);
    if (existing != null) {
      return existing;
    }
    Map<? extends Enum, LockGraphNode> created = createNodes(clazz);
    existing = (Map)lockGraphNodesPerType.putIfAbsent(clazz, created);
    return (Map)MoreObjects.firstNonNull(existing, created);
  }
  





  @VisibleForTesting
  static <E extends Enum<E>> Map<E, LockGraphNode> createNodes(Class<E> clazz)
  {
    EnumMap<E, LockGraphNode> map = Maps.newEnumMap(clazz);
    E[] keys = (Enum[])clazz.getEnumConstants();
    int numKeys = keys.length;
    ArrayList<LockGraphNode> nodes = Lists.newArrayListWithCapacity(numKeys);
    
    for (E key : keys) {
      LockGraphNode node = new LockGraphNode(getLockName(key));
      nodes.add(node);
      map.put(key, node);
    }
    
    for (int i = 1; i < numKeys; i++) {
      ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.THROW, nodes.subList(0, i));
    }
    
    for (int i = 0; i < numKeys - 1; i++) {
      ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.DISABLED, nodes.subList(i + 1, numKeys));
    }
    return Collections.unmodifiableMap(map);
  }
  



  private static String getLockName(Enum<?> rank)
  {
    return rank.getDeclaringClass().getSimpleName() + "." + rank.name();
  }
  




























  @Beta
  public static final class WithExplicitOrdering<E extends Enum<E>>
    extends CycleDetectingLockFactory
  {
    private final Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes;
    




























    @VisibleForTesting
    WithExplicitOrdering(CycleDetectingLockFactory.Policy policy, Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes)
    {
      super(null);
      this.lockGraphNodes = lockGraphNodes;
    }
    


    public ReentrantLock newReentrantLock(E rank)
    {
      return newReentrantLock(rank, false);
    }
    







    public ReentrantLock newReentrantLock(E rank, boolean fair)
    {
      return policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantLock(this, 
      
        (CycleDetectingLockFactory.LockGraphNode)lockGraphNodes.get(rank), fair, null);
    }
    


    public ReentrantReadWriteLock newReentrantReadWriteLock(E rank)
    {
      return newReentrantReadWriteLock(rank, false);
    }
    







    public ReentrantReadWriteLock newReentrantReadWriteLock(E rank, boolean fair)
    {
      return policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantReadWriteLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock(this, 
      
        (CycleDetectingLockFactory.LockGraphNode)lockGraphNodes.get(rank), fair, null);
    }
  }
  


  private static final Logger logger = Logger.getLogger(CycleDetectingLockFactory.class.getName());
  final Policy policy;
  
  private CycleDetectingLockFactory(Policy policy)
  {
    this.policy = ((Policy)Preconditions.checkNotNull(policy));
  }
  






  private static final ThreadLocal<ArrayList<LockGraphNode>> acquiredLocks = new ThreadLocal()
  {
    protected ArrayList<CycleDetectingLockFactory.LockGraphNode> initialValue()
    {
      return Lists.newArrayListWithCapacity(3);
    }
  };
  












  private static class ExampleStackTrace
    extends IllegalStateException
  {
    static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
    

    static final Set<String> EXCLUDED_CLASS_NAMES = ImmutableSet.of(CycleDetectingLockFactory.class
      .getName(), ExampleStackTrace.class
      .getName(), CycleDetectingLockFactory.LockGraphNode.class
      .getName());
    
    ExampleStackTrace(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2) {
      super();
      StackTraceElement[] origStackTrace = getStackTrace();
      int i = 0; for (int n = origStackTrace.length; i < n; i++) {
        if (CycleDetectingLockFactory.WithExplicitOrdering.class.getName().equals(origStackTrace[i].getClassName()))
        {
          setStackTrace(EMPTY_STACK_TRACE);
          break;
        }
        if (!EXCLUDED_CLASS_NAMES.contains(origStackTrace[i].getClassName())) {
          setStackTrace((StackTraceElement[])Arrays.copyOfRange(origStackTrace, i, n));
          break;
        }
      }
    }
  }
  










  @Beta
  public static final class PotentialDeadlockException
    extends CycleDetectingLockFactory.ExampleStackTrace
  {
    private final CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace;
    









    private PotentialDeadlockException(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2, CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace)
    {
      super(node2);
      this.conflictingStackTrace = conflictingStackTrace;
      initCause(conflictingStackTrace);
    }
    
    public CycleDetectingLockFactory.ExampleStackTrace getConflictingStackTrace() {
      return conflictingStackTrace;
    }
    




    public String getMessage()
    {
      StringBuilder message = new StringBuilder(super.getMessage());
      for (Throwable t = conflictingStackTrace; t != null; t = t.getCause()) {
        message.append(", ").append(t.getMessage());
      }
      return message.toString();
    }
  }
  





  private static abstract interface CycleDetectingLock
  {
    public abstract CycleDetectingLockFactory.LockGraphNode getLockGraphNode();
    





    public abstract boolean isAcquiredByCurrentThread();
  }
  





  private static class LockGraphNode
  {
    final Map<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> allowedPriorLocks = new MapMaker()
      .weakKeys().makeMap();
    




    final Map<LockGraphNode, CycleDetectingLockFactory.PotentialDeadlockException> disallowedPriorLocks = new MapMaker()
      .weakKeys().makeMap();
    final String lockName;
    
    LockGraphNode(String lockName)
    {
      this.lockName = ((String)Preconditions.checkNotNull(lockName));
    }
    
    String getLockName() {
      return lockName;
    }
    
    void checkAcquiredLocks(CycleDetectingLockFactory.Policy policy, List<LockGraphNode> acquiredLocks) {
      int i = 0; for (int size = acquiredLocks.size(); i < size; i++) {
        checkAcquiredLock(policy, (LockGraphNode)acquiredLocks.get(i));
      }
    }
    














    void checkAcquiredLock(CycleDetectingLockFactory.Policy policy, LockGraphNode acquiredLock)
    {
      Preconditions.checkState(this != acquiredLock, "Attempted to acquire multiple locks with the same rank %s", acquiredLock
      

        .getLockName());
      
      if (allowedPriorLocks.containsKey(acquiredLock))
      {


        return;
      }
      CycleDetectingLockFactory.PotentialDeadlockException previousDeadlockException = (CycleDetectingLockFactory.PotentialDeadlockException)disallowedPriorLocks.get(acquiredLock);
      if (previousDeadlockException != null)
      {




        CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, previousDeadlockException.getConflictingStackTrace(), null);
        policy.handlePotentialDeadlock(exception);
        return;
      }
      

      Set<LockGraphNode> seen = Sets.newIdentityHashSet();
      CycleDetectingLockFactory.ExampleStackTrace path = acquiredLock.findPathTo(this, seen);
      
      if (path == null)
      {







        allowedPriorLocks.put(acquiredLock, new CycleDetectingLockFactory.ExampleStackTrace(acquiredLock, this));
      }
      else
      {
        CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, path, null);
        
        disallowedPriorLocks.put(acquiredLock, exception);
        policy.handlePotentialDeadlock(exception);
      }
    }
    






    @Nullable
    private CycleDetectingLockFactory.ExampleStackTrace findPathTo(LockGraphNode node, Set<LockGraphNode> seen)
    {
      if (!seen.add(this)) {
        return null;
      }
      CycleDetectingLockFactory.ExampleStackTrace found = (CycleDetectingLockFactory.ExampleStackTrace)allowedPriorLocks.get(node);
      if (found != null) {
        return found;
      }
      
      for (Map.Entry<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> entry : allowedPriorLocks.entrySet()) {
        LockGraphNode preAcquiredLock = (LockGraphNode)entry.getKey();
        found = preAcquiredLock.findPathTo(node, seen);
        if (found != null)
        {


          CycleDetectingLockFactory.ExampleStackTrace path = new CycleDetectingLockFactory.ExampleStackTrace(preAcquiredLock, this);
          path.setStackTrace(((CycleDetectingLockFactory.ExampleStackTrace)entry.getValue()).getStackTrace());
          path.initCause(found);
          return path;
        }
      }
      return null;
    }
  }
  


  private void aboutToAcquire(CycleDetectingLock lock)
  {
    if (!lock.isAcquiredByCurrentThread()) {
      ArrayList<LockGraphNode> acquiredLockList = (ArrayList)acquiredLocks.get();
      LockGraphNode node = lock.getLockGraphNode();
      node.checkAcquiredLocks(policy, acquiredLockList);
      acquiredLockList.add(node);
    }
  }
  




  private static void lockStateChanged(CycleDetectingLock lock)
  {
    if (!lock.isAcquiredByCurrentThread()) {
      ArrayList<LockGraphNode> acquiredLockList = (ArrayList)acquiredLocks.get();
      LockGraphNode node = lock.getLockGraphNode();
      

      for (int i = acquiredLockList.size() - 1; i >= 0; i--) {
        if (acquiredLockList.get(i) == node) {
          acquiredLockList.remove(i);
          break;
        }
      }
    }
  }
  
  final class CycleDetectingReentrantLock extends ReentrantLock implements CycleDetectingLockFactory.CycleDetectingLock
  {
    private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
    
    private CycleDetectingReentrantLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair) {
      super();
      this.lockGraphNode = ((CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode));
    }
    


    public CycleDetectingLockFactory.LockGraphNode getLockGraphNode()
    {
      return lockGraphNode;
    }
    
    public boolean isAcquiredByCurrentThread()
    {
      return isHeldByCurrentThread();
    }
    


    public void lock()
    {
      CycleDetectingLockFactory.this.aboutToAcquire(this);
      try {
        super.lock();
        
        CycleDetectingLockFactory.lockStateChanged(this); } finally { CycleDetectingLockFactory.lockStateChanged(this);
      }
    }
    
    public void lockInterruptibly() throws InterruptedException
    {
      CycleDetectingLockFactory.this.aboutToAcquire(this);
      try {
        super.lockInterruptibly();
        
        CycleDetectingLockFactory.lockStateChanged(this); } finally { CycleDetectingLockFactory.lockStateChanged(this);
      }
    }
    
    public boolean tryLock()
    {
      CycleDetectingLockFactory.this.aboutToAcquire(this);
      try {
        return super.tryLock();
      } finally {
        CycleDetectingLockFactory.lockStateChanged(this);
      }
    }
    
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException
    {
      CycleDetectingLockFactory.this.aboutToAcquire(this);
      try {
        return super.tryLock(timeout, unit);
      } finally {
        CycleDetectingLockFactory.lockStateChanged(this);
      }
    }
    
    public void unlock()
    {
      try {
        super.unlock();
        
        CycleDetectingLockFactory.lockStateChanged(this); } finally { CycleDetectingLockFactory.lockStateChanged(this);
      }
    }
  }
  

  final class CycleDetectingReentrantReadWriteLock
    extends ReentrantReadWriteLock
    implements CycleDetectingLockFactory.CycleDetectingLock
  {
    private final CycleDetectingLockFactory.CycleDetectingReentrantReadLock readLock;
    
    private final CycleDetectingLockFactory.CycleDetectingReentrantWriteLock writeLock;
    
    private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
    
    private CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair)
    {
      super();
      readLock = new CycleDetectingLockFactory.CycleDetectingReentrantReadLock(CycleDetectingLockFactory.this, this);
      writeLock = new CycleDetectingLockFactory.CycleDetectingReentrantWriteLock(CycleDetectingLockFactory.this, this);
      this.lockGraphNode = ((CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode));
    }
    


    public ReentrantReadWriteLock.ReadLock readLock()
    {
      return readLock;
    }
    
    public ReentrantReadWriteLock.WriteLock writeLock()
    {
      return writeLock;
    }
    


    public CycleDetectingLockFactory.LockGraphNode getLockGraphNode()
    {
      return lockGraphNode;
    }
    
    public boolean isAcquiredByCurrentThread()
    {
      return (isWriteLockedByCurrentThread()) || (getReadHoldCount() > 0);
    }
  }
  
  private class CycleDetectingReentrantReadLock extends ReentrantReadWriteLock.ReadLock {
    @Weak
    final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
    
    CycleDetectingReentrantReadLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
      super();
      this.readWriteLock = readWriteLock;
    }
    
    public void lock()
    {
      CycleDetectingLockFactory.this.aboutToAcquire(readWriteLock);
      try {
        super.lock();
        
        CycleDetectingLockFactory.lockStateChanged(readWriteLock); } finally { CycleDetectingLockFactory.lockStateChanged(readWriteLock);
      }
    }
    
    public void lockInterruptibly() throws InterruptedException
    {
      CycleDetectingLockFactory.this.aboutToAcquire(readWriteLock);
      try {
        super.lockInterruptibly();
        
        CycleDetectingLockFactory.lockStateChanged(readWriteLock); } finally { CycleDetectingLockFactory.lockStateChanged(readWriteLock);
      }
    }
    
    public boolean tryLock()
    {
      CycleDetectingLockFactory.this.aboutToAcquire(readWriteLock);
      try {
        return super.tryLock();
      } finally {
        CycleDetectingLockFactory.lockStateChanged(readWriteLock);
      }
    }
    
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException
    {
      CycleDetectingLockFactory.this.aboutToAcquire(readWriteLock);
      try {
        return super.tryLock(timeout, unit);
      } finally {
        CycleDetectingLockFactory.lockStateChanged(readWriteLock);
      }
    }
    
    public void unlock()
    {
      try {
        super.unlock();
        
        CycleDetectingLockFactory.lockStateChanged(readWriteLock); } finally { CycleDetectingLockFactory.lockStateChanged(readWriteLock);
      }
    }
  }
  
  private class CycleDetectingReentrantWriteLock extends ReentrantReadWriteLock.WriteLock {
    @Weak
    final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
    
    CycleDetectingReentrantWriteLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
      super();
      this.readWriteLock = readWriteLock;
    }
    
    public void lock()
    {
      CycleDetectingLockFactory.this.aboutToAcquire(readWriteLock);
      try {
        super.lock();
        
        CycleDetectingLockFactory.lockStateChanged(readWriteLock); } finally { CycleDetectingLockFactory.lockStateChanged(readWriteLock);
      }
    }
    
    public void lockInterruptibly() throws InterruptedException
    {
      CycleDetectingLockFactory.this.aboutToAcquire(readWriteLock);
      try {
        super.lockInterruptibly();
        
        CycleDetectingLockFactory.lockStateChanged(readWriteLock); } finally { CycleDetectingLockFactory.lockStateChanged(readWriteLock);
      }
    }
    
    public boolean tryLock()
    {
      CycleDetectingLockFactory.this.aboutToAcquire(readWriteLock);
      try {
        return super.tryLock();
      } finally {
        CycleDetectingLockFactory.lockStateChanged(readWriteLock);
      }
    }
    
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException
    {
      CycleDetectingLockFactory.this.aboutToAcquire(readWriteLock);
      try {
        return super.tryLock(timeout, unit);
      } finally {
        CycleDetectingLockFactory.lockStateChanged(readWriteLock);
      }
    }
    
    public void unlock()
    {
      try {
        super.unlock();
        
        CycleDetectingLockFactory.lockStateChanged(readWriteLock); } finally { CycleDetectingLockFactory.lockStateChanged(readWriteLock);
      }
    }
  }
}
