package org.eclipse.jetty.websocket.common.message;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;























public class MessageReader
  extends InputStreamReader
  implements MessageAppender
{
  private final MessageInputStream stream;
  
  public MessageReader(MessageInputStream stream)
  {
    super(stream, StandardCharsets.UTF_8);
    this.stream = stream;
  }
  
  public void appendFrame(ByteBuffer payload, boolean isLast)
    throws IOException
  {
    stream.appendFrame(payload, isLast);
  }
  

  public void messageComplete()
  {
    stream.messageComplete();
  }
}
