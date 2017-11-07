package org.eclipse.jetty.websocket.common.events;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.WebSocketConnectionListener;
import org.eclipse.jetty.websocket.api.WebSocketFrameListener;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.api.WebSocketPartialListener;
import org.eclipse.jetty.websocket.api.WebSocketPingPongListener;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.Frame.Type;
import org.eclipse.jetty.websocket.common.CloseInfo;
import org.eclipse.jetty.websocket.common.frames.ReadOnlyDelegatedFrame;
import org.eclipse.jetty.websocket.common.message.SimpleBinaryMessage;
import org.eclipse.jetty.websocket.common.message.SimpleTextMessage;
import org.eclipse.jetty.websocket.common.util.Utf8PartialBuilder;






















public class JettyListenerEventDriver
  extends AbstractEventDriver
{
  private static final Logger LOG = Log.getLogger(JettyListenerEventDriver.class);
  private final WebSocketConnectionListener listener;
  private Utf8PartialBuilder utf8Partial;
  private boolean hasCloseBeenCalled = false;
  
  public JettyListenerEventDriver(WebSocketPolicy policy, WebSocketConnectionListener listener)
  {
    super(policy, listener);
    this.listener = listener;
  }
  
  public void onBinaryFrame(ByteBuffer buffer, boolean fin)
    throws IOException
  {
    if ((listener instanceof WebSocketListener))
    {
      if (activeMessage == null)
      {
        activeMessage = new SimpleBinaryMessage(this);
      }
      
      appendMessage(buffer, fin);
    }
    
    if ((listener instanceof WebSocketPartialListener))
    {
      ((WebSocketPartialListener)listener).onWebSocketPartialBinary(buffer.slice().asReadOnlyBuffer(), fin);
    }
  }
  

  public void onBinaryMessage(byte[] data)
  {
    if ((listener instanceof WebSocketListener))
    {
      ((WebSocketListener)listener).onWebSocketBinary(data, 0, data.length);
    }
  }
  

  public void onClose(CloseInfo close)
  {
    if (hasCloseBeenCalled)
    {

      return;
    }
    hasCloseBeenCalled = true;
    
    int statusCode = close.getStatusCode();
    String reason = close.getReason();
    listener.onWebSocketClose(statusCode, reason);
  }
  

  public void onConnect()
  {
    if (LOG.isDebugEnabled())
      LOG.debug("onConnect({})", new Object[] { session });
    listener.onWebSocketConnect(session);
  }
  

  public void onError(Throwable cause)
  {
    listener.onWebSocketError(cause);
  }
  

  public void onFrame(Frame frame)
  {
    if ((listener instanceof WebSocketFrameListener))
    {
      ((WebSocketFrameListener)listener).onWebSocketFrame(new ReadOnlyDelegatedFrame(frame));
    }
    
    if ((listener instanceof WebSocketPingPongListener))
    {
      if (frame.getType() == Frame.Type.PING)
      {
        ((WebSocketPingPongListener)listener).onWebSocketPing(frame.getPayload().asReadOnlyBuffer());
      }
      else if (frame.getType() == Frame.Type.PONG)
      {
        ((WebSocketPingPongListener)listener).onWebSocketPong(frame.getPayload().asReadOnlyBuffer());
      }
    }
  }
  



  public void onInputStream(InputStream stream) {}
  



  public void onReader(Reader reader) {}
  


  public void onTextFrame(ByteBuffer buffer, boolean fin)
    throws IOException
  {
    if ((listener instanceof WebSocketListener))
    {
      if (activeMessage == null)
      {
        activeMessage = new SimpleTextMessage(this);
      }
      
      appendMessage(buffer, fin);
    }
    
    if ((listener instanceof WebSocketPartialListener))
    {
      if (utf8Partial == null)
      {
        utf8Partial = new Utf8PartialBuilder();
      }
      
      String partial = utf8Partial.toPartialString(buffer);
      
      ((WebSocketPartialListener)listener).onWebSocketPartialText(partial, fin);
      
      if (fin)
      {
        partial = null;
      }
    }
  }
  






  public void onTextMessage(String message)
  {
    if ((listener instanceof WebSocketListener))
    {
      ((WebSocketListener)listener).onWebSocketText(message);
    }
  }
  

  public String toString()
  {
    return String.format("%s[%s]", new Object[] { JettyListenerEventDriver.class.getSimpleName(), listener.getClass().getName() });
  }
}
