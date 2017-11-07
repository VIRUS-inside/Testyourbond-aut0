package org.eclipse.jetty.websocket.common.extensions.compress;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.BadPayloadException;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.Frame.Type;
import org.eclipse.jetty.websocket.common.OpCode;




















public class PerMessageDeflateExtension
  extends CompressExtension
{
  private static final Logger LOG = Log.getLogger(PerMessageDeflateExtension.class);
  
  private ExtensionConfig configRequested;
  private ExtensionConfig configNegotiated;
  private boolean incomingContextTakeover = true;
  private boolean outgoingContextTakeover = true;
  private boolean incomingCompressed;
  
  public PerMessageDeflateExtension() {}
  
  public String getName() {
    return "permessage-deflate";
  }
  







  public void incomingFrame(Frame frame)
  {
    if (frame.getType().isData())
    {
      incomingCompressed = frame.isRsv1();
    }
    
    if ((OpCode.isControlFrame(frame.getOpCode())) || (!incomingCompressed))
    {
      nextIncomingFrame(frame);
      return;
    }
    
    ByteAccumulator accumulator = newByteAccumulator();
    
    try
    {
      ByteBuffer payload = frame.getPayload();
      decompress(accumulator, payload);
      if (frame.isFin())
      {
        decompress(accumulator, TAIL_BYTES_BUF.slice());
      }
      
      forwardIncoming(frame, accumulator);
    }
    catch (DataFormatException e)
    {
      throw new BadPayloadException(e);
    }
    
    if (frame.isFin()) {
      incomingCompressed = false;
    }
  }
  
  protected void nextIncomingFrame(Frame frame)
  {
    if ((frame.isFin()) && (!incomingContextTakeover))
    {
      LOG.debug("Incoming Context Reset", new Object[0]);
      decompressCount.set(0);
      getInflater().reset();
    }
    super.nextIncomingFrame(frame);
  }
  

  protected void nextOutgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
  {
    if ((frame.isFin()) && (!outgoingContextTakeover))
    {
      LOG.debug("Outgoing Context Reset", new Object[0]);
      getDeflater().reset();
    }
    super.nextOutgoingFrame(frame, callback, batchMode);
  }
  

  int getRsvUseMode()
  {
    return 1;
  }
  

  int getTailDropMode()
  {
    return 2;
  }
  

  public void setConfig(ExtensionConfig config)
  {
    configRequested = new ExtensionConfig(config);
    configNegotiated = new ExtensionConfig(config.getName());
    
    for (String key : config.getParameterKeys())
    {
      key = key.trim();
      switch (key)
      {
      case "client_max_window_bits": 
      case "server_max_window_bits": 
        break;
      




      case "client_no_context_takeover": 
        configNegotiated.setParameter("client_no_context_takeover");
        switch (1.$SwitchMap$org$eclipse$jetty$websocket$api$WebSocketBehavior[getPolicy().getBehavior().ordinal()])
        {
        case 1: 
          incomingContextTakeover = false;
          break;
        case 2: 
          outgoingContextTakeover = false;
        }
        
        break;
      

      case "server_no_context_takeover": 
        configNegotiated.setParameter("server_no_context_takeover");
        switch (1.$SwitchMap$org$eclipse$jetty$websocket$api$WebSocketBehavior[getPolicy().getBehavior().ordinal()])
        {
        case 1: 
          outgoingContextTakeover = false;
          break;
        case 2: 
          incomingContextTakeover = false;
        }
        
        break;
      

      default: 
        throw new IllegalArgumentException();
      }
      
    }
    
    LOG.debug("config: outgoingContextTakover={}, incomingContextTakeover={} : {}", new Object[] { Boolean.valueOf(outgoingContextTakeover), Boolean.valueOf(incomingContextTakeover), this });
    
    super.setConfig(configNegotiated);
  }
  

  public String toString()
  {
    return String.format("%s[requested=\"%s\", negotiated=\"%s\"]", new Object[] {
      getClass().getSimpleName(), configRequested
      .getParameterizedName(), configNegotiated
      .getParameterizedName() });
  }
}
