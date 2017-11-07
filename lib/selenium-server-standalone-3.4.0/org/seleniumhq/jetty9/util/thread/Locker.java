package org.seleniumhq.jetty9.util.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;





























public class Locker
{
  private static final Lock LOCKED = new Lock();
  private final ReentrantLock _lock = new ReentrantLock();
  private final Lock _unlock = new UnLock();
  

  public Locker() {}
  

  public Lock lock()
  {
    if (_lock.isHeldByCurrentThread())
      throw new IllegalStateException("Locker is not reentrant");
    _lock.lock();
    return _unlock;
  }
  
  public Lock lockIfNotHeld()
  {
    if (_lock.isHeldByCurrentThread())
      return LOCKED;
    _lock.lock();
    return _unlock;
  }
  
  public boolean isLocked()
  {
    return _lock.isLocked();
  }
  



  public class UnLock
    extends Locker.Lock
  {
    public UnLock() {}
    



    public void close()
    {
      _lock.unlock();
    }
  }
  
  public Condition newCondition()
  {
    return _lock.newCondition();
  }
  
  public static class Lock
    implements AutoCloseable
  {
    public Lock() {}
    
    public void close() {}
  }
}
