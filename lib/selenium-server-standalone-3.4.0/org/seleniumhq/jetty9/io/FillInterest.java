package org.seleniumhq.jetty9.io;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadPendingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;
import org.seleniumhq.jetty9.util.Callback;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.Invocable;
import org.seleniumhq.jetty9.util.thread.Invocable.InvocationType;
























public abstract class FillInterest
{
  private static final Logger LOG = Log.getLogger(FillInterest.class);
  private final AtomicReference<Callback> _interested = new AtomicReference(null);
  


  private Throwable _lastSet;
  



  protected FillInterest() {}
  



  public void register(Callback callback)
    throws ReadPendingException
  {
    if (!tryRegister(callback))
    {
      LOG.warn("Read pending for {} prevented {}", new Object[] { _interested, callback });
      if (LOG.isDebugEnabled())
        LOG.warn("callback set at ", _lastSet);
      throw new ReadPendingException();
    }
  }
  








  public boolean tryRegister(Callback callback)
  {
    if (callback == null) {
      throw new IllegalArgumentException();
    }
    if (!_interested.compareAndSet(null, callback)) {
      return false;
    }
    if (LOG.isDebugEnabled())
    {
      LOG.debug("{} register {}", new Object[] { this, callback });
      _lastSet = new Throwable(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()) + ":" + Thread.currentThread().getName());
    }
    
    try
    {
      if (LOG.isDebugEnabled())
        LOG.debug("{} register {}", new Object[] { this, callback });
      needsFillInterest();
    }
    catch (Throwable e)
    {
      onFail(e);
    }
    
    return true;
  }
  



  public void fillable()
  {
    Callback callback = (Callback)_interested.get();
    if (LOG.isDebugEnabled())
      LOG.debug("{} fillable {}", new Object[] { this, callback });
    if ((callback != null) && (_interested.compareAndSet(callback, null))) {
      callback.succeeded();
    } else if (LOG.isDebugEnabled()) {
      LOG.debug("{} lost race {}", new Object[] { this, callback });
    }
  }
  


  public boolean isInterested()
  {
    return _interested.get() != null;
  }
  
  public Invocable.InvocationType getCallbackInvocationType()
  {
    Callback callback = (Callback)_interested.get();
    return Invocable.getInvocationType(callback);
  }
  






  public boolean onFail(Throwable cause)
  {
    Callback callback = (Callback)_interested.get();
    if ((callback != null) && (_interested.compareAndSet(callback, null)))
    {
      callback.failed(cause);
      return true;
    }
    return false;
  }
  
  public void onClose()
  {
    Callback callback = (Callback)_interested.get();
    if (LOG.isDebugEnabled())
      LOG.debug("{} onClose {}", new Object[] { this, callback });
    if ((callback != null) && (_interested.compareAndSet(callback, null))) {
      callback.failed(new ClosedChannelException());
    }
  }
  
  public String toString()
  {
    return String.format("FillInterest@%x{%b,%s}", new Object[] { Integer.valueOf(hashCode()), Boolean.valueOf(_interested.get() != null ? 1 : false), _interested.get() });
  }
  

  public String toStateString()
  {
    return _interested.get() == null ? "-" : "FI";
  }
  
  protected abstract void needsFillInterest()
    throws IOException;
}
