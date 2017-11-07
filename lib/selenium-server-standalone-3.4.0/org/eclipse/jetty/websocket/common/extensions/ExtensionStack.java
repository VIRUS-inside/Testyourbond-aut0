package org.eclipse.jetty.websocket.common.extensions;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import org.eclipse.jetty.util.IteratingCallback;
import org.eclipse.jetty.util.IteratingCallback.Action;
import org.eclipse.jetty.util.annotation.ManagedAttribute;
import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.util.component.ContainerLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.extensions.Extension;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
import org.eclipse.jetty.websocket.common.Generator;
import org.eclipse.jetty.websocket.common.Parser;




















@ManagedObject("Extension Stack")
public class ExtensionStack
  extends ContainerLifeCycle
  implements IncomingFrames, OutgoingFrames
{
  private static final Logger LOG = Log.getLogger(ExtensionStack.class);
  
  private final Queue<FrameEntry> entries = new ArrayDeque();
  private final IteratingCallback flusher = new Flusher(null);
  private final ExtensionFactory factory;
  private List<Extension> extensions;
  private IncomingFrames nextIncoming;
  private OutgoingFrames nextOutgoing;
  
  public ExtensionStack(ExtensionFactory factory)
  {
    this.factory = factory;
  }
  
  public void configure(Generator generator)
  {
    generator.configureFromExtensions(extensions);
  }
  
  public void configure(Parser parser)
  {
    parser.configureFromExtensions(extensions);
  }
  
  protected void doStart()
    throws Exception
  {
    super.doStart();
    

    if ((extensions != null) && (extensions.size() > 0))
    {
      ListIterator<Extension> exts = extensions.listIterator();
      

      while (exts.hasNext())
      {
        Extension ext = (Extension)exts.next();
        ext.setNextOutgoingFrames(nextOutgoing);
        nextOutgoing = ext;
        
        if ((ext instanceof LifeCycle))
        {
          addBean(ext, true);
        }
      }
      

      while (exts.hasPrevious())
      {
        Extension ext = (Extension)exts.previous();
        ext.setNextIncomingFrames(nextIncoming);
        nextIncoming = ext;
      }
    }
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    super.dump(out, indent);
    
    IncomingFrames websocket = getLastIncoming();
    OutgoingFrames network = getLastOutgoing();
    
    out.append(indent).append(" +- Stack").append(System.lineSeparator());
    out.append(indent).append("     +- Network  : ").append(network.toString()).append(System.lineSeparator());
    for (Extension ext : extensions)
    {
      out.append(indent).append("     +- Extension: ").append(ext.toString()).append(System.lineSeparator());
    }
    out.append(indent).append("     +- Websocket: ").append(websocket.toString()).append(System.lineSeparator());
  }
  
  @ManagedAttribute(name="Extension List", readonly=true)
  public List<Extension> getExtensions()
  {
    return extensions;
  }
  
  private IncomingFrames getLastIncoming()
  {
    IncomingFrames last = nextIncoming;
    boolean done = false;
    while (!done)
    {
      if ((last instanceof AbstractExtension))
      {
        last = ((AbstractExtension)last).getNextIncoming();
      }
      else
      {
        done = true;
      }
    }
    return last;
  }
  
  private OutgoingFrames getLastOutgoing()
  {
    OutgoingFrames last = nextOutgoing;
    boolean done = false;
    while (!done)
    {
      if ((last instanceof AbstractExtension))
      {
        last = ((AbstractExtension)last).getNextOutgoing();
      }
      else
      {
        done = true;
      }
    }
    return last;
  }
  





  public List<ExtensionConfig> getNegotiatedExtensions()
  {
    List<ExtensionConfig> ret = new ArrayList();
    if (extensions == null)
    {
      return ret;
    }
    
    for (Extension ext : extensions)
    {
      if (ext.getName().charAt(0) != '@')
      {



        ret.add(ext.getConfig()); }
    }
    return ret;
  }
  
  @ManagedAttribute(name="Next Incoming Frames Handler", readonly=true)
  public IncomingFrames getNextIncoming()
  {
    return nextIncoming;
  }
  
  @ManagedAttribute(name="Next Outgoing Frames Handler", readonly=true)
  public OutgoingFrames getNextOutgoing()
  {
    return nextOutgoing;
  }
  
  public boolean hasNegotiatedExtensions()
  {
    return (extensions != null) && (extensions.size() > 0);
  }
  

  public void incomingError(Throwable e)
  {
    nextIncoming.incomingError(e);
  }
  

  public void incomingFrame(Frame frame)
  {
    nextIncoming.incomingFrame(frame);
  }
  








  public void negotiate(List<ExtensionConfig> configs)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Extension Configs={}", new Object[] { configs });
    }
    extensions = new ArrayList();
    
    String[] rsvClaims = new String[3];
    
    for (ExtensionConfig config : configs)
    {
      Extension ext = factory.newInstance(config);
      if (ext != null)
      {





        if ((ext.isRsv1User()) && (rsvClaims[0] != null))
        {
          LOG.debug("Not adding extension {}. Extension {} already claimed RSV1", new Object[] { config, rsvClaims[0] });

        }
        else if ((ext.isRsv2User()) && (rsvClaims[1] != null))
        {
          LOG.debug("Not adding extension {}. Extension {} already claimed RSV2", new Object[] { config, rsvClaims[1] });

        }
        else if ((ext.isRsv3User()) && (rsvClaims[2] != null))
        {
          LOG.debug("Not adding extension {}. Extension {} already claimed RSV3", new Object[] { config, rsvClaims[2] });

        }
        else
        {
          extensions.add(ext);
          addBean(ext);
          
          if (LOG.isDebugEnabled()) {
            LOG.debug("Adding Extension: {}", new Object[] { config });
          }
          
          if (ext.isRsv1User())
          {
            rsvClaims[0] = ext.getName();
          }
          if (ext.isRsv2User())
          {
            rsvClaims[1] = ext.getName();
          }
          if (ext.isRsv3User())
          {
            rsvClaims[2] = ext.getName();
          }
        }
      }
    }
  }
  
  public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode) {
    FrameEntry entry = new FrameEntry(frame, callback, batchMode, null);
    if (LOG.isDebugEnabled())
      LOG.debug("Queuing {}", new Object[] { entry });
    offerEntry(entry);
    flusher.iterate();
  }
  
  public void setNextIncoming(IncomingFrames nextIncoming)
  {
    this.nextIncoming = nextIncoming;
  }
  
  public void setNextOutgoing(OutgoingFrames nextOutgoing)
  {
    this.nextOutgoing = nextOutgoing;
  }
  
  public void setPolicy(WebSocketPolicy policy)
  {
    for (Extension extension : extensions)
    {
      if ((extension instanceof AbstractExtension))
      {
        ((AbstractExtension)extension).setPolicy(policy);
      }
    }
  }
  
  private void offerEntry(FrameEntry entry)
  {
    synchronized (this)
    {
      entries.offer(entry);
    }
  }
  
  private FrameEntry pollEntry()
  {
    synchronized (this)
    {
      return (FrameEntry)entries.poll();
    }
  }
  
  private int getQueueSize()
  {
    synchronized (this)
    {
      return entries.size();
    }
  }
  

  public String toString()
  {
    StringBuilder s = new StringBuilder();
    s.append("ExtensionStack[");
    s.append("queueSize=").append(getQueueSize());
    s.append(",extensions=");
    if (extensions == null)
    {
      s.append("<null>");
    }
    else
    {
      s.append('[');
      boolean delim = false;
      for (Extension ext : extensions)
      {
        if (delim)
        {
          s.append(',');
        }
        if (ext == null)
        {
          s.append("<null>");
        }
        else
        {
          s.append(ext.getName());
        }
        delim = true;
      }
      s.append(']');
    }
    s.append(",incoming=").append(nextIncoming == null ? "<null>" : nextIncoming.getClass().getName());
    s.append(",outgoing=").append(nextOutgoing == null ? "<null>" : nextOutgoing.getClass().getName());
    s.append("]");
    return s.toString();
  }
  
  private static class FrameEntry
  {
    private final Frame frame;
    private final WriteCallback callback;
    private final BatchMode batchMode;
    
    private FrameEntry(Frame frame, WriteCallback callback, BatchMode batchMode)
    {
      this.frame = frame;
      this.callback = callback;
      this.batchMode = batchMode;
    }
    

    public String toString()
    {
      return frame.toString();
    }
  }
  
  private class Flusher extends IteratingCallback implements WriteCallback
  {
    private ExtensionStack.FrameEntry current;
    
    private Flusher() {}
    
    protected IteratingCallback.Action process() throws Exception {
      current = ExtensionStack.this.pollEntry();
      if (current == null)
      {
        if (ExtensionStack.LOG.isDebugEnabled())
          ExtensionStack.LOG.debug("Entering IDLE", new Object[0]);
        return IteratingCallback.Action.IDLE;
      }
      if (ExtensionStack.LOG.isDebugEnabled())
        ExtensionStack.LOG.debug("Processing {}", new Object[] { current });
      nextOutgoing.outgoingFrame(ExtensionStack.FrameEntry.access$400(current), this, ExtensionStack.FrameEntry.access$500(current));
      return IteratingCallback.Action.SCHEDULED;
    }
    





    protected void onCompleteSuccess() {}
    




    protected void onCompleteFailure(Throwable x) {}
    




    public void writeSuccess()
    {
      notifyCallbackSuccess(ExtensionStack.FrameEntry.access$700(current));
      succeeded();
    }
    






    public void writeFailed(Throwable x)
    {
      notifyCallbackFailure(ExtensionStack.FrameEntry.access$700(current), x);
      succeeded();
    }
    
    private void notifyCallbackSuccess(WriteCallback callback)
    {
      try
      {
        if (callback != null) {
          callback.writeSuccess();
        }
      }
      catch (Throwable x) {
        ExtensionStack.LOG.debug("Exception while notifying success of callback " + callback, x);
      }
    }
    
    private void notifyCallbackFailure(WriteCallback callback, Throwable failure)
    {
      try
      {
        if (callback != null) {
          callback.writeFailed(failure);
        }
      }
      catch (Throwable x) {
        ExtensionStack.LOG.debug("Exception while notifying failure of callback " + callback, x);
      }
    }
  }
}
