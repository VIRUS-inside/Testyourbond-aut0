package org.eclipse.jetty.websocket.common.events;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import org.eclipse.jetty.websocket.common.CloseInfo;
import org.eclipse.jetty.websocket.common.WebSocketSession;

public abstract interface EventDriver
  extends IncomingFrames
{
  public abstract WebSocketPolicy getPolicy();
  
  public abstract WebSocketSession getSession();
  
  public abstract BatchMode getBatchMode();
  
  public abstract void onBinaryFrame(ByteBuffer paramByteBuffer, boolean paramBoolean)
    throws IOException;
  
  public abstract void onBinaryMessage(byte[] paramArrayOfByte);
  
  public abstract void onClose(CloseInfo paramCloseInfo);
  
  public abstract void onConnect();
  
  public abstract void onContinuationFrame(ByteBuffer paramByteBuffer, boolean paramBoolean)
    throws IOException;
  
  public abstract void onError(Throwable paramThrowable);
  
  public abstract void onFrame(Frame paramFrame);
  
  public abstract void onInputStream(InputStream paramInputStream)
    throws IOException;
  
  public abstract void onPing(ByteBuffer paramByteBuffer);
  
  public abstract void onPong(ByteBuffer paramByteBuffer);
  
  public abstract void onReader(Reader paramReader)
    throws IOException;
  
  public abstract void onTextFrame(ByteBuffer paramByteBuffer, boolean paramBoolean)
    throws IOException;
  
  public abstract void onTextMessage(String paramString);
  
  public abstract void openSession(WebSocketSession paramWebSocketSession);
}
