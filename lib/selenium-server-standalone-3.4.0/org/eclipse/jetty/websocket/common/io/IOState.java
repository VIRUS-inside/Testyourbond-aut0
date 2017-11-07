package org.eclipse.jetty.websocket.common.io;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.common.CloseInfo;
import org.eclipse.jetty.websocket.common.ConnectionState;




























public class IOState
{
  public static abstract interface ConnectionStateListener
  {
    public abstract void onConnectionStateChange(ConnectionState paramConnectionState);
  }
  
  private static enum CloseHandshakeSource
  {
    NONE, 
    
    LOCAL, 
    
    REMOTE, 
    
    ABNORMAL;
    


    private CloseHandshakeSource() {}
  }
  

  private static final Logger LOG = Log.getLogger(IOState.class);
  private ConnectionState state;
  private final List<ConnectionStateListener> listeners = new CopyOnWriteArrayList();
  




  private boolean inputAvailable;
  




  private boolean outputAvailable;
  




  private CloseHandshakeSource closeHandshakeSource;
  




  private CloseInfo closeInfo;
  



  private AtomicReference<CloseInfo> finalClose = new AtomicReference();
  



  private boolean cleanClose;
  



  public IOState()
  {
    state = ConnectionState.CONNECTING;
    inputAvailable = false;
    outputAvailable = false;
    closeHandshakeSource = CloseHandshakeSource.NONE;
    closeInfo = null;
    cleanClose = false;
  }
  
  public void addListener(ConnectionStateListener listener)
  {
    listeners.add(listener);
  }
  
  public void assertInputOpen() throws IOException
  {
    if (!isInputAvailable())
    {
      throw new IOException("Connection input is closed");
    }
  }
  
  public void assertOutputOpen() throws IOException
  {
    if (!isOutputAvailable())
    {
      throw new IOException("Connection output is closed");
    }
  }
  
  public CloseInfo getCloseInfo()
  {
    CloseInfo ci = (CloseInfo)finalClose.get();
    if (ci != null)
    {
      return ci;
    }
    return closeInfo;
  }
  
  public ConnectionState getConnectionState()
  {
    return state;
  }
  
  public boolean isClosed()
  {
    synchronized (this)
    {
      return state == ConnectionState.CLOSED;
    }
  }
  
  public boolean isInputAvailable()
  {
    return inputAvailable;
  }
  
  public boolean isOpen()
  {
    return !isClosed();
  }
  
  public boolean isOutputAvailable()
  {
    return outputAvailable;
  }
  
  private void notifyStateListeners(ConnectionState state)
  {
    if (LOG.isDebugEnabled())
      LOG.debug("Notify State Listeners: {}", new Object[] { state });
    for (ConnectionStateListener listener : listeners)
    {
      if (LOG.isDebugEnabled())
      {
        LOG.debug("{}.onConnectionStateChange({})", new Object[] { listener.getClass().getSimpleName(), state.name() });
      }
      listener.onConnectionStateChange(state);
    }
  }
  






  public void onAbnormalClose(CloseInfo close)
  {
    if (LOG.isDebugEnabled())
      LOG.debug("onAbnormalClose({})", new Object[] { close });
    ConnectionState event = null;
    synchronized (this)
    {
      if (state == ConnectionState.CLOSED)
      {

        return;
      }
      
      if (state == ConnectionState.OPEN)
      {
        cleanClose = false;
      }
      
      state = ConnectionState.CLOSED;
      finalClose.compareAndSet(null, close);
      inputAvailable = false;
      outputAvailable = false;
      closeHandshakeSource = CloseHandshakeSource.ABNORMAL;
      event = state;
    }
    notifyStateListeners(event);
  }
  




  public void onCloseLocal(CloseInfo closeInfo)
  {
    boolean open = false;
    synchronized (this)
    {
      ConnectionState initialState = state;
      if (LOG.isDebugEnabled())
        LOG.debug("onCloseLocal({}) : {}", new Object[] { closeInfo, initialState });
      if (initialState == ConnectionState.CLOSED)
      {

        if (LOG.isDebugEnabled())
          LOG.debug("already closed", new Object[0]);
        return;
      }
      
      if (initialState == ConnectionState.CONNECTED)
      {

        if (LOG.isDebugEnabled())
          LOG.debug("FastClose in CONNECTED detected", new Object[0]);
        open = true;
      }
    }
    
    if (open) {
      openAndCloseLocal(closeInfo);
    } else {
      closeLocal(closeInfo);
    }
  }
  
  private void openAndCloseLocal(CloseInfo closeInfo)
  {
    onOpened();
    if (LOG.isDebugEnabled())
      LOG.debug("FastClose continuing with Closure", new Object[0]);
    closeLocal(closeInfo);
  }
  
  private void closeLocal(CloseInfo closeInfo)
  {
    ConnectionState event = null;
    ConnectionState abnormalEvent = null;
    synchronized (this)
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug("onCloseLocal(), input={}, output={}", new Object[] { Boolean.valueOf(inputAvailable), Boolean.valueOf(outputAvailable) });
      }
      this.closeInfo = closeInfo;
      

      outputAvailable = false;
      
      if (closeHandshakeSource == CloseHandshakeSource.NONE)
      {
        closeHandshakeSource = CloseHandshakeSource.LOCAL;
      }
      
      if (!inputAvailable)
      {
        if (LOG.isDebugEnabled())
          LOG.debug("Close Handshake satisfied, disconnecting", new Object[0]);
        cleanClose = true;
        state = ConnectionState.CLOSED;
        finalClose.compareAndSet(null, closeInfo);
        event = state;
      }
      else if (state == ConnectionState.OPEN)
      {

        state = ConnectionState.CLOSING;
        event = state;
        

        if (closeInfo.isAbnormal())
        {
          abnormalEvent = ConnectionState.CLOSED;
          finalClose.compareAndSet(null, closeInfo);
          cleanClose = false;
          outputAvailable = false;
          inputAvailable = false;
          closeHandshakeSource = CloseHandshakeSource.ABNORMAL;
        }
      }
    }
    

    if (event != null)
    {
      notifyStateListeners(event);
      if (abnormalEvent != null)
      {
        notifyStateListeners(abnormalEvent);
      }
    }
  }
  




  public void onCloseRemote(CloseInfo closeInfo)
  {
    if (LOG.isDebugEnabled())
      LOG.debug("onCloseRemote({})", new Object[] { closeInfo });
    ConnectionState event = null;
    synchronized (this)
    {
      if (state == ConnectionState.CLOSED)
      {

        return;
      }
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("onCloseRemote(), input={}, output={}", new Object[] { Boolean.valueOf(inputAvailable), Boolean.valueOf(outputAvailable) });
      }
      this.closeInfo = closeInfo;
      

      inputAvailable = false;
      
      if (closeHandshakeSource == CloseHandshakeSource.NONE)
      {
        closeHandshakeSource = CloseHandshakeSource.REMOTE;
      }
      
      if (!outputAvailable)
      {
        LOG.debug("Close Handshake satisfied, disconnecting", new Object[0]);
        cleanClose = true;
        state = ConnectionState.CLOSED;
        finalClose.compareAndSet(null, closeInfo);
        event = state;
      }
      else if (state == ConnectionState.OPEN)
      {

        state = ConnectionState.CLOSING;
        event = state;
      }
    }
    

    if (event != null)
    {
      notifyStateListeners(event);
    }
  }
  





  public void onConnected()
  {
    ConnectionState event = null;
    synchronized (this)
    {
      if (state != ConnectionState.CONNECTING)
      {
        LOG.debug("Unable to set to connected, not in CONNECTING state: {}", new Object[] { state });
        return;
      }
      
      state = ConnectionState.CONNECTED;
      inputAvailable = false;
      outputAvailable = true;
      event = state;
    }
    notifyStateListeners(event);
  }
  



  public void onFailedUpgrade()
  {
    assert (state == ConnectionState.CONNECTING);
    ConnectionState event = null;
    synchronized (this)
    {
      state = ConnectionState.CLOSED;
      cleanClose = false;
      inputAvailable = false;
      outputAvailable = false;
      event = state;
    }
    notifyStateListeners(event);
  }
  



  public void onOpened()
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("onOpened()", new Object[0]);
    }
    ConnectionState event = null;
    synchronized (this)
    {
      if (state == ConnectionState.OPEN)
      {

        return;
      }
      
      if (state != ConnectionState.CONNECTED)
      {
        LOG.debug("Unable to open, not in CONNECTED state: {}", new Object[] { state });
        return;
      }
      
      state = ConnectionState.OPEN;
      inputAvailable = true;
      outputAvailable = true;
      event = state;
    }
    notifyStateListeners(event);
  }
  






  public void onReadFailure(Throwable t)
  {
    ConnectionState event = null;
    synchronized (this)
    {
      if (state == ConnectionState.CLOSED)
      {

        return;
      }
      

      String reason = "WebSocket Read Failure";
      if ((t instanceof EOFException))
      {
        reason = "WebSocket Read EOF";
        Throwable cause = t.getCause();
        if ((cause != null) && (StringUtil.isNotBlank(cause.getMessage())))
        {
          reason = "EOF: " + cause.getMessage();
        }
        

      }
      else if (StringUtil.isNotBlank(t.getMessage()))
      {
        reason = t.getMessage();
      }
      

      CloseInfo close = new CloseInfo(1006, reason);
      
      finalClose.compareAndSet(null, close);
      
      cleanClose = false;
      state = ConnectionState.CLOSED;
      closeInfo = close;
      inputAvailable = false;
      outputAvailable = false;
      closeHandshakeSource = CloseHandshakeSource.ABNORMAL;
      event = state;
    }
    notifyStateListeners(event);
  }
  






  public void onWriteFailure(Throwable t)
  {
    ConnectionState event = null;
    synchronized (this)
    {
      if (state == ConnectionState.CLOSED)
      {

        return;
      }
      

      String reason = "WebSocket Write Failure";
      if ((t instanceof EOFException))
      {
        reason = "WebSocket Write EOF";
        Throwable cause = t.getCause();
        if ((cause != null) && (StringUtil.isNotBlank(cause.getMessage())))
        {
          reason = "EOF: " + cause.getMessage();
        }
        

      }
      else if (StringUtil.isNotBlank(t.getMessage()))
      {
        reason = t.getMessage();
      }
      

      CloseInfo close = new CloseInfo(1006, reason);
      
      finalClose.compareAndSet(null, close);
      
      cleanClose = false;
      state = ConnectionState.CLOSED;
      inputAvailable = false;
      outputAvailable = false;
      closeHandshakeSource = CloseHandshakeSource.ABNORMAL;
      event = state;
    }
    notifyStateListeners(event);
  }
  
  public void onDisconnected()
  {
    ConnectionState event = null;
    synchronized (this)
    {
      if (state == ConnectionState.CLOSED)
      {

        return;
      }
      
      CloseInfo close = new CloseInfo(1006, "Disconnected");
      
      cleanClose = false;
      state = ConnectionState.CLOSED;
      closeInfo = close;
      inputAvailable = false;
      outputAvailable = false;
      closeHandshakeSource = CloseHandshakeSource.ABNORMAL;
      event = state;
    }
    notifyStateListeners(event);
  }
  

  public String toString()
  {
    StringBuilder str = new StringBuilder();
    str.append(getClass().getSimpleName());
    str.append("@").append(Integer.toHexString(hashCode()));
    str.append("[").append(state);
    str.append(',');
    if (!inputAvailable)
    {
      str.append('!');
    }
    str.append("in,");
    if (!outputAvailable)
    {
      str.append('!');
    }
    str.append("out");
    if ((state == ConnectionState.CLOSED) || (state == ConnectionState.CLOSING))
    {
      CloseInfo ci = (CloseInfo)finalClose.get();
      if (ci != null)
      {
        str.append(",finalClose=").append(ci);
      }
      else
      {
        str.append(",close=").append(closeInfo);
      }
      str.append(",clean=").append(cleanClose);
      str.append(",closeSource=").append(closeHandshakeSource);
    }
    str.append(']');
    return str.toString();
  }
  
  public boolean wasAbnormalClose()
  {
    return closeHandshakeSource == CloseHandshakeSource.ABNORMAL;
  }
  
  public boolean wasCleanClose()
  {
    return cleanClose;
  }
  
  public boolean wasLocalCloseInitiated()
  {
    return closeHandshakeSource == CloseHandshakeSource.LOCAL;
  }
  
  public boolean wasRemoteCloseInitiated()
  {
    return closeHandshakeSource == CloseHandshakeSource.REMOTE;
  }
}
