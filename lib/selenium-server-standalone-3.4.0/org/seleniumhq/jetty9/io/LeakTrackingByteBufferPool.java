package org.seleniumhq.jetty9.io;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.LeakDetector;
import org.seleniumhq.jetty9.util.LeakDetector.LeakInfo;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

















public class LeakTrackingByteBufferPool
  extends ContainerLifeCycle
  implements ByteBufferPool
{
  private static final Logger LOG = Log.getLogger(LeakTrackingByteBufferPool.class);
  
  private final LeakDetector<ByteBuffer> leakDetector = new LeakDetector()
  {
    public String id(ByteBuffer resource)
    {
      return BufferUtil.toIDString(resource);
    }
    

    protected void leaked(LeakDetector<ByteBuffer>.LeakInfo leakInfo)
    {
      leaked.incrementAndGet();
      LeakTrackingByteBufferPool.this.leaked(leakInfo);
    }
  };
  
  private static final boolean NOISY = Boolean.getBoolean(LeakTrackingByteBufferPool.class.getName() + ".NOISY");
  private final ByteBufferPool delegate;
  private final AtomicLong leakedReleases = new AtomicLong(0L);
  private final AtomicLong leakedAcquires = new AtomicLong(0L);
  private final AtomicLong leaked = new AtomicLong(0L);
  
  public LeakTrackingByteBufferPool(ByteBufferPool delegate)
  {
    this.delegate = delegate;
    addBean(leakDetector);
    addBean(delegate);
  }
  

  public ByteBuffer acquire(int size, boolean direct)
  {
    ByteBuffer buffer = delegate.acquire(size, direct);
    boolean leaked = leakDetector.acquired(buffer);
    if ((NOISY) || (!leaked))
    {
      leakedAcquires.incrementAndGet();
      LOG.info(String.format("ByteBuffer acquire %s leaked.acquired=%s", new Object[] { leakDetector.id(buffer), leaked ? "normal" : "LEAK" }), new Throwable("LeakStack.Acquire"));
    }
    
    return buffer;
  }
  

  public void release(ByteBuffer buffer)
  {
    if (buffer == null)
      return;
    boolean leaked = leakDetector.released(buffer);
    if ((NOISY) || (!leaked))
    {
      leakedReleases.incrementAndGet();
      LOG.info(String.format("ByteBuffer release %s leaked.released=%s", new Object[] { leakDetector.id(buffer), leaked ? "normal" : "LEAK" }), new Throwable("LeakStack.Release"));
    }
    
    delegate.release(buffer);
  }
  
  public void clearTracking()
  {
    leakedAcquires.set(0L);
    leakedReleases.set(0L);
  }
  



  public long getLeakedAcquires()
  {
    return leakedAcquires.get();
  }
  



  public long getLeakedReleases()
  {
    return leakedReleases.get();
  }
  



  public long getLeakedResources()
  {
    return leaked.get();
  }
  
  protected void leaked(LeakDetector<ByteBuffer>.LeakInfo leakInfo)
  {
    LOG.warn("ByteBuffer " + leakInfo.getResourceDescription() + " leaked at:", leakInfo.getStackFrames());
  }
}
