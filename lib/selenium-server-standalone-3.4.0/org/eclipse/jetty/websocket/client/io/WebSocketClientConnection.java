package org.eclipse.jetty.websocket.client.io;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.util.thread.Scheduler;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import org.eclipse.jetty.websocket.client.masks.Masker;
import org.eclipse.jetty.websocket.client.masks.RandomMasker;
import org.eclipse.jetty.websocket.common.Parser;
import org.eclipse.jetty.websocket.common.WebSocketFrame;
import org.eclipse.jetty.websocket.common.io.AbstractWebSocketConnection;





















public class WebSocketClientConnection
  extends AbstractWebSocketConnection
{
  private final Masker masker;
  
  public WebSocketClientConnection(EndPoint endp, Executor executor, Scheduler scheduler, WebSocketPolicy websocketPolicy, ByteBufferPool bufferPool)
  {
    super(endp, executor, scheduler, websocketPolicy, bufferPool);
    masker = new RandomMasker();
  }
  

  public InetSocketAddress getLocalAddress()
  {
    return getEndPoint().getLocalAddress();
  }
  

  public InetSocketAddress getRemoteAddress()
  {
    return getEndPoint().getRemoteAddress();
  }
  




  public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
  {
    if ((frame instanceof WebSocketFrame))
    {
      masker.setMask((WebSocketFrame)frame);
    }
    super.outgoingFrame(frame, callback, batchMode);
  }
  

  public void setNextIncomingFrames(IncomingFrames incoming)
  {
    getParser().setIncomingFramesHandler(incoming);
  }
}
