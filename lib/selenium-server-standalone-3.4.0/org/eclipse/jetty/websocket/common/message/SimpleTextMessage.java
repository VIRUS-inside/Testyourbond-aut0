package org.eclipse.jetty.websocket.common.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.eclipse.jetty.util.Utf8StringBuilder;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.common.events.EventDriver;


















public class SimpleTextMessage
  implements MessageAppender
{
  private final EventDriver onEvent;
  protected final Utf8StringBuilder utf;
  private int size = 0;
  protected boolean finished;
  
  public SimpleTextMessage(EventDriver onEvent)
  {
    this.onEvent = onEvent;
    utf = new Utf8StringBuilder(1024);
    size = 0;
    finished = false;
  }
  
  public void appendFrame(ByteBuffer payload, boolean isLast)
    throws IOException
  {
    if (finished)
    {
      throw new IOException("Cannot append to finished buffer");
    }
    
    if (payload == null)
    {

      return;
    }
    
    onEvent.getPolicy().assertValidTextMessageSize(size + payload.remaining());
    size += payload.remaining();
    

    utf.append(payload);
  }
  

  public void messageComplete()
  {
    finished = true;
    

    onEvent.onTextMessage(utf.toString());
  }
}
