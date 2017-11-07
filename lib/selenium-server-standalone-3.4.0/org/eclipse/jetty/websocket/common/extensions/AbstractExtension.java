package org.eclipse.jetty.websocket.common.extensions;

import java.io.IOException;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.util.annotation.ManagedAttribute;
import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.ContainerLifeCycle;
import org.eclipse.jetty.util.component.Dumpable;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.extensions.Extension;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
import org.eclipse.jetty.websocket.common.LogicalConnection;
import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;


















@ManagedObject("Abstract Extension")
public abstract class AbstractExtension
  extends AbstractLifeCycle
  implements Dumpable, Extension
{
  private final Logger log;
  private WebSocketPolicy policy;
  private ByteBufferPool bufferPool;
  private ExtensionConfig config;
  private LogicalConnection connection;
  private OutgoingFrames nextOutgoing;
  private IncomingFrames nextIncoming;
  
  public AbstractExtension()
  {
    log = Log.getLogger(getClass());
  }
  

  public String dump()
  {
    return ContainerLifeCycle.dump(this);
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    dumpWithHeading(out, indent, "incoming", nextIncoming);
    dumpWithHeading(out, indent, "outgoing", nextOutgoing);
  }
  
  protected void dumpWithHeading(Appendable out, String indent, String heading, Object bean) throws IOException
  {
    out.append(indent).append(" +- ");
    out.append(heading).append(" : ");
    out.append(bean.toString());
  }
  
  @Deprecated
  public void init(WebSocketContainerScope container)
  {
    init(container.getPolicy(), container.getBufferPool());
  }
  
  public void init(WebSocketPolicy policy, ByteBufferPool bufferPool)
  {
    this.policy = policy;
    this.bufferPool = bufferPool;
  }
  
  public ByteBufferPool getBufferPool()
  {
    return bufferPool;
  }
  

  public ExtensionConfig getConfig()
  {
    return config;
  }
  
  public LogicalConnection getConnection()
  {
    return connection;
  }
  

  public String getName()
  {
    return config.getName();
  }
  
  @ManagedAttribute(name="Next Incoming Frame Handler", readonly=true)
  public IncomingFrames getNextIncoming()
  {
    return nextIncoming;
  }
  
  @ManagedAttribute(name="Next Outgoing Frame Handler", readonly=true)
  public OutgoingFrames getNextOutgoing()
  {
    return nextOutgoing;
  }
  
  public WebSocketPolicy getPolicy()
  {
    return policy;
  }
  

  public void incomingError(Throwable e)
  {
    nextIncomingError(e);
  }
  








  public boolean isRsv1User()
  {
    return false;
  }
  








  public boolean isRsv2User()
  {
    return false;
  }
  








  public boolean isRsv3User()
  {
    return false;
  }
  
  protected void nextIncomingError(Throwable e)
  {
    nextIncoming.incomingError(e);
  }
  
  protected void nextIncomingFrame(Frame frame)
  {
    log.debug("nextIncomingFrame({})", new Object[] { frame });
    nextIncoming.incomingFrame(frame);
  }
  
  protected void nextOutgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
  {
    log.debug("nextOutgoingFrame({})", new Object[] { frame });
    nextOutgoing.outgoingFrame(frame, callback, batchMode);
  }
  
  public void setBufferPool(ByteBufferPool bufferPool)
  {
    this.bufferPool = bufferPool;
  }
  
  public void setConfig(ExtensionConfig config)
  {
    this.config = config;
  }
  
  public void setConnection(LogicalConnection connection)
  {
    this.connection = connection;
  }
  

  public void setNextIncomingFrames(IncomingFrames nextIncoming)
  {
    this.nextIncoming = nextIncoming;
  }
  

  public void setNextOutgoingFrames(OutgoingFrames nextOutgoing)
  {
    this.nextOutgoing = nextOutgoing;
  }
  
  public void setPolicy(WebSocketPolicy policy)
  {
    this.policy = policy;
  }
  

  public String toString()
  {
    return String.format("%s[%s]", new Object[] { getClass().getSimpleName(), config.getParameterizedName() });
  }
}
