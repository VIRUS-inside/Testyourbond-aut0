package org.eclipse.jetty.websocket.common.events;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.DecoratedObjectFactory;
import org.eclipse.jetty.util.Utf8Appendable.NotUtf8Exception;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.CloseException;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import org.eclipse.jetty.websocket.common.CloseInfo;
import org.eclipse.jetty.websocket.common.LogicalConnection;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import org.eclipse.jetty.websocket.common.frames.CloseFrame;
import org.eclipse.jetty.websocket.common.io.IOState;
import org.eclipse.jetty.websocket.common.message.MessageAppender;
import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;


















public abstract class AbstractEventDriver
  extends AbstractLifeCycle
  implements IncomingFrames, EventDriver
{
  private static final Logger LOG = Log.getLogger(AbstractEventDriver.class);
  protected final Logger TARGET_LOG;
  protected WebSocketPolicy policy;
  protected final Object websocket;
  protected WebSocketSession session;
  protected MessageAppender activeMessage;
  
  public AbstractEventDriver(WebSocketPolicy policy, Object websocket)
  {
    this.policy = policy;
    this.websocket = websocket;
    TARGET_LOG = Log.getLogger(websocket.getClass());
  }
  
  protected void appendMessage(ByteBuffer buffer, boolean fin) throws IOException
  {
    activeMessage.appendFrame(buffer, fin);
    
    if (fin)
    {
      activeMessage.messageComplete();
      activeMessage = null;
    }
  }
  
  protected void dispatch(Runnable runnable)
  {
    session.dispatch(runnable);
  }
  

  public WebSocketPolicy getPolicy()
  {
    return policy;
  }
  

  public WebSocketSession getSession()
  {
    return session;
  }
  

  public final void incomingError(Throwable e)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("incomingError(" + e.getClass().getName() + ")", e);
    }
    
    onError(e);
  }
  

  public void incomingFrame(Frame frame)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("incomingFrame({})", new Object[] { frame });
    }
    
    try
    {
      onFrame(frame);
      
      byte opcode = frame.getOpCode();
      switch (opcode)
      {

      case 8: 
        boolean validate = true;
        CloseFrame closeframe = (CloseFrame)frame;
        CloseInfo close = new CloseInfo(closeframe, validate);
        

        session.getConnection().getIOState().onCloseRemote(close);
        
        return;
      

      case 9: 
        if (LOG.isDebugEnabled())
        {
          LOG.debug("PING: {}", new Object[] { BufferUtil.toDetailString(frame.getPayload()) });
        }
        ByteBuffer pongBuf;
        if (frame.hasPayload())
        {
          ByteBuffer pongBuf = ByteBuffer.allocate(frame.getPayload().remaining());
          BufferUtil.put(frame.getPayload().slice(), pongBuf);
          BufferUtil.flipToFlush(pongBuf, 0);
        }
        else
        {
          pongBuf = ByteBuffer.allocate(0);
        }
        onPing(frame.getPayload());
        session.getRemote().sendPong(pongBuf);
        break;
      

      case 10: 
        if (LOG.isDebugEnabled())
        {
          LOG.debug("PONG: {}", new Object[] { BufferUtil.toDetailString(frame.getPayload()) });
        }
        onPong(frame.getPayload());
        break;
      

      case 2: 
        onBinaryFrame(frame.getPayload(), frame.isFin());
        return;
      

      case 1: 
        onTextFrame(frame.getPayload(), frame.isFin());
        return;
      

      case 0: 
        onContinuationFrame(frame.getPayload(), frame.isFin());
        return;
      case 3: case 4: 
      case 5: case 6: 
      case 7: default: 
        if (LOG.isDebugEnabled()) {
          LOG.debug("Unhandled OpCode: {}", opcode);
        }
        break;
      }
      
    } catch (Utf8Appendable.NotUtf8Exception e) {
      terminateConnection(1007, e.getMessage());
    }
    catch (CloseException e)
    {
      terminateConnection(e.getStatusCode(), e.getMessage());
    }
    catch (Throwable t)
    {
      unhandled(t);
    }
  }
  
  public void onContinuationFrame(ByteBuffer buffer, boolean fin)
    throws IOException
  {
    if (activeMessage == null)
    {
      throw new IOException("Out of order Continuation frame encountered");
    }
    
    appendMessage(buffer, fin);
  }
  



  public void onPong(ByteBuffer buffer) {}
  



  public void onPing(ByteBuffer buffer) {}
  



  public BatchMode getBatchMode()
  {
    return null;
  }
  

  public void openSession(WebSocketSession session)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("openSession({})", new Object[] { session });
      LOG.debug("objectFactory={}", new Object[] { session.getContainerScope().getObjectFactory() });
    }
    this.session = session;
    this.session.getContainerScope().getObjectFactory().decorate(websocket);
    
    try
    {
      onConnect();
    }
    catch (Throwable t)
    {
      this.session.notifyError(t);
      throw t;
    }
  }
  
  protected void terminateConnection(int statusCode, String rawreason)
  {
    if (LOG.isDebugEnabled())
      LOG.debug("terminateConnection({},{})", new Object[] { Integer.valueOf(statusCode), rawreason });
    session.close(statusCode, CloseFrame.truncate(rawreason));
  }
  
  private void unhandled(Throwable t)
  {
    TARGET_LOG.warn("Unhandled Error (closing connection)", t);
    onError(t);
    
    if ((t instanceof CloseException))
    {
      terminateConnection(((CloseException)t).getStatusCode(), t.getClass().getSimpleName());
      return;
    }
    

    switch (1.$SwitchMap$org$eclipse$jetty$websocket$api$WebSocketBehavior[policy.getBehavior().ordinal()])
    {
    case 1: 
      terminateConnection(1011, t.getClass().getSimpleName());
      break;
    case 2: 
      terminateConnection(1008, t.getClass().getSimpleName());
    }
  }
}
