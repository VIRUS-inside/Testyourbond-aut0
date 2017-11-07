package org.eclipse.jetty.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.WritePendingException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.Invocable;
import org.eclipse.jetty.util.thread.Invocable.InvocationType;




























public abstract class WriteFlusher
{
  private static final Logger LOG = Log.getLogger(WriteFlusher.class);
  private static final boolean DEBUG = LOG.isDebugEnabled();
  private static final ByteBuffer[] EMPTY_BUFFERS = { BufferUtil.EMPTY_BUFFER };
  private static final EnumMap<StateType, Set<StateType>> __stateTransitions = new EnumMap(StateType.class);
  private static final State __IDLE = new IdleState(null);
  private static final State __WRITING = new WritingState(null);
  private static final State __COMPLETING = new CompletingState(null);
  private final EndPoint _endPoint;
  private final AtomicReference<State> _state = new AtomicReference();
  

  static
  {
    __stateTransitions.put(StateType.IDLE, EnumSet.of(StateType.WRITING));
    __stateTransitions.put(StateType.WRITING, EnumSet.of(StateType.IDLE, StateType.PENDING, StateType.FAILED));
    __stateTransitions.put(StateType.PENDING, EnumSet.of(StateType.COMPLETING, StateType.IDLE));
    __stateTransitions.put(StateType.COMPLETING, EnumSet.of(StateType.IDLE, StateType.PENDING, StateType.FAILED));
    __stateTransitions.put(StateType.FAILED, EnumSet.of(StateType.IDLE));
  }
  






















  protected WriteFlusher(EndPoint endPoint)
  {
    _state.set(__IDLE);
    _endPoint = endPoint;
  }
  
  private static enum StateType
  {
    IDLE, 
    WRITING, 
    PENDING, 
    COMPLETING, 
    FAILED;
    



    private StateType() {}
  }
  


  private boolean updateState(State previous, State next)
  {
    if (!isTransitionAllowed(previous, next)) {
      throw new IllegalStateException();
    }
    boolean updated = _state.compareAndSet(previous, next);
    if (DEBUG)
      LOG.debug("update {}:{}{}{}", new Object[] { this, previous, updated ? "-->" : "!->", next });
    return updated;
  }
  
  private void fail(PendingState pending)
  {
    State current = (State)_state.get();
    if (current.getType() == StateType.FAILED)
    {
      FailedState failed = (FailedState)current;
      if (updateState(failed, __IDLE))
      {
        pending.fail(failed.getCause());
        return;
      }
    }
    throw new IllegalStateException();
  }
  
  private void ignoreFail()
  {
    State current = (State)_state.get();
    while (current.getType() == StateType.FAILED)
    {
      if (updateState(current, __IDLE))
        return;
      current = (State)_state.get();
    }
  }
  
  private boolean isTransitionAllowed(State currentState, State newState)
  {
    Set<StateType> allowedNewStateTypes = (Set)__stateTransitions.get(currentState.getType());
    if (!allowedNewStateTypes.contains(newState.getType()))
    {
      LOG.warn("{}: {} -> {} not allowed", new Object[] { this, currentState, newState });
      return false;
    }
    return true;
  }
  


  private static class State
  {
    private final WriteFlusher.StateType _type;
    

    private State(WriteFlusher.StateType stateType)
    {
      _type = stateType;
    }
    
    public WriteFlusher.StateType getType()
    {
      return _type;
    }
    

    public String toString()
    {
      return String.format("%s", new Object[] { _type });
    }
  }
  


  private static class IdleState
    extends WriteFlusher.State
  {
    private IdleState()
    {
      super(null);
    }
  }
  


  private static class WritingState
    extends WriteFlusher.State
  {
    private WritingState()
    {
      super(null);
    }
  }
  

  private static class FailedState
    extends WriteFlusher.State
  {
    private final Throwable _cause;
    
    private FailedState(Throwable cause)
    {
      super(null);
      _cause = cause;
    }
    
    public Throwable getCause()
    {
      return _cause;
    }
  }
  




  private static class CompletingState
    extends WriteFlusher.State
  {
    private CompletingState()
    {
      super(null);
    }
  }
  

  private class PendingState
    extends WriteFlusher.State
  {
    private final Callback _callback;
    
    private final ByteBuffer[] _buffers;
    

    private PendingState(ByteBuffer[] buffers, Callback callback)
    {
      super(null);
      _buffers = buffers;
      _callback = callback;
    }
    
    public ByteBuffer[] getBuffers()
    {
      return _buffers;
    }
    
    protected boolean fail(Throwable cause)
    {
      if (_callback != null)
      {
        _callback.failed(cause);
        return true;
      }
      return false;
    }
    
    protected void complete()
    {
      if (_callback != null) {
        _callback.succeeded();
      }
    }
    
    Invocable.InvocationType getCallbackInvocationType() {
      return Invocable.getInvocationType(_callback);
    }
    
    public Object getCallback()
    {
      return _callback;
    }
  }
  
  public Invocable.InvocationType getCallbackInvocationType()
  {
    State s = (State)_state.get();
    return (s instanceof PendingState) ? ((PendingState)s)
      .getCallbackInvocationType() : Invocable.InvocationType.BLOCKING;
  }
  



















  public void write(Callback callback, ByteBuffer... buffers)
    throws WritePendingException
  {
    if (DEBUG) {
      LOG.debug("write: {} {}", new Object[] { this, BufferUtil.toDetailString(buffers) });
    }
    if (!updateState(__IDLE, __WRITING)) {
      throw new WritePendingException();
    }
    try
    {
      buffers = flush(buffers);
      

      if (buffers != null)
      {
        if (DEBUG)
          LOG.debug("flushed incomplete", new Object[0]);
        PendingState pending = new PendingState(buffers, callback, null);
        if (updateState(__WRITING, pending)) {
          onIncompleteFlush();
        } else
          fail(pending);
        return;
      }
      

      if (!updateState(__WRITING, __IDLE))
        ignoreFail();
      if (callback != null) {
        callback.succeeded();
      }
    }
    catch (IOException e) {
      if (DEBUG)
        LOG.debug("write exception", e);
      if (updateState(__WRITING, __IDLE))
      {
        if (callback != null) {
          callback.failed(e);
        }
      } else {
        fail(new PendingState(buffers, callback, null));
      }
    }
  }
  








  public void completeWrite()
  {
    if (DEBUG) {
      LOG.debug("completeWrite: {}", new Object[] { this });
    }
    State previous = (State)_state.get();
    
    if (previous.getType() != StateType.PENDING) {
      return;
    }
    PendingState pending = (PendingState)previous;
    if (!updateState(pending, __COMPLETING)) {
      return;
    }
    try
    {
      ByteBuffer[] buffers = pending.getBuffers();
      
      buffers = flush(buffers);
      

      if (buffers != null)
      {
        if (DEBUG)
          LOG.debug("flushed incomplete {}", new Object[] { BufferUtil.toDetailString(buffers) });
        if (buffers != pending.getBuffers())
          pending = new PendingState(buffers, _callback, null);
        if (updateState(__COMPLETING, pending)) {
          onIncompleteFlush();
        } else
          fail(pending);
        return;
      }
      

      if (!updateState(__COMPLETING, __IDLE))
        ignoreFail();
      pending.complete();
    }
    catch (IOException e)
    {
      if (DEBUG)
        LOG.debug("completeWrite exception", e);
      if (updateState(__COMPLETING, __IDLE)) {
        pending.fail(e);
      } else {
        fail(pending);
      }
    }
  }
  





  protected ByteBuffer[] flush(ByteBuffer[] buffers)
    throws IOException
  {
    boolean progress = true;
    while ((progress) && (buffers != null))
    {
      int before = buffers.length == 0 ? 0 : buffers[0].remaining();
      boolean flushed = _endPoint.flush(buffers);
      int r = buffers.length == 0 ? 0 : buffers[0].remaining();
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("Flushed={} {}/{}+{} {}", new Object[] { Boolean.valueOf(flushed), Integer.valueOf(before - r), Integer.valueOf(before), Integer.valueOf(buffers.length - 1), this });
      }
      if (flushed) {
        return null;
      }
      progress = before != r;
      
      int not_empty = 0;
      while (r == 0)
      {
        not_empty++; if (not_empty == buffers.length)
        {
          buffers = null;
          not_empty = 0;
          break;
        }
        progress = true;
        r = buffers[not_empty].remaining();
      }
      
      if (not_empty > 0) {
        buffers = (ByteBuffer[])Arrays.copyOfRange(buffers, not_empty, buffers.length);
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("!fully flushed {}", new Object[] { this });
    }
    


    return buffers == null ? EMPTY_BUFFERS : buffers;
  }
  






  public boolean onFail(Throwable cause)
  {
    for (;;)
    {
      State current = (State)_state.get();
      switch (1.$SwitchMap$org$eclipse$jetty$io$WriteFlusher$StateType[current.getType().ordinal()])
      {
      case 1: 
      case 2: 
        if (DEBUG)
          LOG.debug("ignored: {} {}", new Object[] { this, cause });
        return false;
      
      case 3: 
        if (DEBUG) {
          LOG.debug("failed: {} {}", new Object[] { this, cause });
        }
        PendingState pending = (PendingState)current;
        if (updateState(pending, __IDLE)) {
          return pending.fail(cause);
        }
        break;
      default: 
        if (DEBUG) {
          LOG.debug("failed: {} {}", new Object[] { this, cause });
        }
        if (updateState(current, new FailedState(cause, null))) {
          return false;
        }
        break;
      }
    }
  }
  
  public void onClose() {
    onFail(new ClosedChannelException());
  }
  
  boolean isIdle()
  {
    return ((State)_state.get()).getType() == StateType.IDLE;
  }
  
  public boolean isInProgress()
  {
    switch (1.$SwitchMap$org$eclipse$jetty$io$WriteFlusher$StateType[((State)_state.get()).getType().ordinal()])
    {
    case 3: 
    case 4: 
    case 5: 
      return true;
    }
    return false;
  }
  


  public String toString()
  {
    State s = (State)_state.get();
    return String.format("WriteFlusher@%x{%s}->%s", new Object[] { Integer.valueOf(hashCode()), s, (s instanceof PendingState) ? ((PendingState)s).getCallback() : null });
  }
  
  public String toStateString()
  {
    switch (1.$SwitchMap$org$eclipse$jetty$io$WriteFlusher$StateType[((State)_state.get()).getType().ordinal()])
    {
    case 4: 
      return "W";
    case 3: 
      return "P";
    case 5: 
      return "C";
    case 1: 
      return "-";
    case 2: 
      return "F";
    }
    return "?";
  }
  
  protected abstract void onIncompleteFlush();
}
