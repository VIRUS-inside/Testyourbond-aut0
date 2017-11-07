package org.eclipse.jetty.websocket.common.events;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.common.CloseInfo;
import org.eclipse.jetty.websocket.common.events.annotated.CallableMethod;
import org.eclipse.jetty.websocket.common.events.annotated.OptionalSessionCallableMethod;
import org.eclipse.jetty.websocket.common.message.MessageAppender;
import org.eclipse.jetty.websocket.common.message.MessageInputStream;
import org.eclipse.jetty.websocket.common.message.MessageReader;
import org.eclipse.jetty.websocket.common.message.SimpleBinaryMessage;
import org.eclipse.jetty.websocket.common.message.SimpleTextMessage;




















public class JettyAnnotatedEventDriver
  extends AbstractEventDriver
{
  private final JettyAnnotatedMetadata events;
  private boolean hasCloseBeenCalled = false;
  private BatchMode batchMode;
  
  public JettyAnnotatedEventDriver(WebSocketPolicy policy, Object websocket, JettyAnnotatedMetadata events)
  {
    super(policy, websocket);
    this.events = events;
    
    WebSocket anno = (WebSocket)websocket.getClass().getAnnotation(WebSocket.class);
    
    if (anno.maxTextMessageSize() > 0)
    {
      this.policy.setMaxTextMessageSize(anno.maxTextMessageSize());
    }
    if (anno.maxBinaryMessageSize() > 0)
    {
      this.policy.setMaxBinaryMessageSize(anno.maxBinaryMessageSize());
    }
    if (anno.inputBufferSize() > 0)
    {
      this.policy.setInputBufferSize(anno.inputBufferSize());
    }
    if (anno.maxIdleTime() > 0)
    {
      this.policy.setIdleTimeout(anno.maxIdleTime());
    }
    batchMode = anno.batchMode();
  }
  

  public BatchMode getBatchMode()
  {
    return batchMode;
  }
  
  public void onBinaryFrame(ByteBuffer buffer, boolean fin)
    throws IOException
  {
    if (events.onBinary == null)
    {

      return;
    }
    
    if (activeMessage == null)
    {
      if (events.onBinary.isStreaming())
      {
        activeMessage = new MessageInputStream();
        final MessageAppender msg = activeMessage;
        dispatch(new Runnable()
        {

          public void run()
          {
            try
            {
              events.onBinary.call(websocket, session, new Object[] { msg });

            }
            catch (Throwable t)
            {
              onError(t);
            }
          }
        });
      }
      else
      {
        activeMessage = new SimpleBinaryMessage(this);
      }
    }
    
    appendMessage(buffer, fin);
  }
  

  public void onBinaryMessage(byte[] data)
  {
    if (events.onBinary != null)
    {
      events.onBinary.call(websocket, session, new Object[] { data, Integer.valueOf(0), Integer.valueOf(data.length) });
    }
  }
  

  public void onClose(CloseInfo close)
  {
    if (hasCloseBeenCalled)
    {

      return;
    }
    hasCloseBeenCalled = true;
    if (events.onClose != null)
    {
      events.onClose.call(websocket, session, new Object[] { Integer.valueOf(close.getStatusCode()), close.getReason() });
    }
  }
  

  public void onConnect()
  {
    if (events.onConnect != null)
    {
      events.onConnect.call(websocket, new Object[] { session });
    }
  }
  

  public void onError(Throwable cause)
  {
    if (events.onError != null)
    {
      events.onError.call(websocket, session, new Object[] { cause });
    }
  }
  

  public void onFrame(Frame frame)
  {
    if (events.onFrame != null)
    {
      events.onFrame.call(websocket, session, new Object[] { frame });
    }
  }
  

  public void onInputStream(InputStream stream)
  {
    if (events.onBinary != null)
    {
      events.onBinary.call(websocket, session, new Object[] { stream });
    }
  }
  

  public void onReader(Reader reader)
  {
    if (events.onText != null)
    {
      events.onText.call(websocket, session, new Object[] { reader });
    }
  }
  
  public void onTextFrame(ByteBuffer buffer, boolean fin)
    throws IOException
  {
    if (events.onText == null)
    {

      return;
    }
    
    if (activeMessage == null)
    {
      if (events.onText.isStreaming())
      {
        activeMessage = new MessageReader(new MessageInputStream());
        final MessageAppender msg = activeMessage;
        dispatch(new Runnable()
        {

          public void run()
          {
            try
            {
              events.onText.call(websocket, session, new Object[] { msg });

            }
            catch (Throwable t)
            {
              onError(t);
            }
          }
        });
      }
      else
      {
        activeMessage = new SimpleTextMessage(this);
      }
    }
    
    appendMessage(buffer, fin);
  }
  

  public void onTextMessage(String message)
  {
    if (events.onText != null)
    {
      events.onText.call(websocket, session, new Object[] { message });
    }
  }
  

  public String toString()
  {
    return String.format("%s[%s]", new Object[] { getClass().getSimpleName(), websocket });
  }
}
