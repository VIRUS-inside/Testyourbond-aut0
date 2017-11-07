package org.eclipse.jetty.websocket.common.extensions;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.common.Generator;
import org.eclipse.jetty.websocket.common.WebSocketFrame;

















public class FrameCaptureExtension
  extends AbstractExtension
{
  private static final Logger LOG = Log.getLogger(FrameCaptureExtension.class);
  
  private static final int BUFSIZE = 32768;
  private Generator generator;
  private Path outputDir;
  private String prefix = "frame";
  
  private Path incomingFramesPath;
  private Path outgoingFramesPath;
  private AtomicInteger incomingCount = new AtomicInteger(0);
  private AtomicInteger outgoingCount = new AtomicInteger(0);
  private SeekableByteChannel incomingChannel;
  private SeekableByteChannel outgoingChannel;
  
  public FrameCaptureExtension() {}
  
  public String getName()
  {
    return "@frame-capture";
  }
  

  public void incomingFrame(Frame frame)
  {
    saveFrame(frame, false);
    try
    {
      nextIncomingFrame(frame);
    }
    catch (Throwable t)
    {
      IO.close(incomingChannel);
      incomingChannel = null;
      throw t;
    }
  }
  

  public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
  {
    saveFrame(frame, true);
    try
    {
      nextOutgoingFrame(frame, callback, batchMode);
    }
    catch (Throwable t)
    {
      IO.close(outgoingChannel);
      outgoingChannel = null;
      throw t;
    }
  }
  
  private void saveFrame(Frame frame, boolean outgoing)
  {
    if ((outputDir == null) || (generator == null))
    {
      return;
    }
    

    SeekableByteChannel channel = outgoing ? outgoingChannel : incomingChannel;
    
    if (channel == null)
    {
      return;
    }
    
    ByteBuffer buf = getBufferPool().acquire(32768, false);
    
    try
    {
      WebSocketFrame f = WebSocketFrame.copy(frame);
      f.setMasked(false);
      generator.generateHeaderBytes(f, buf);
      channel.write(buf);
      if (frame.hasPayload())
      {
        channel.write(frame.getPayload().slice());
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("Saved {} frame #{}", new Object[] { outgoing ? "outgoing" : "incoming", 
          Integer.valueOf(outgoing ? outgoingCount.incrementAndGet() : incomingCount.incrementAndGet()) });
      }
    }
    catch (IOException e) {
      LOG.warn("Unable to save frame: " + frame, e);
    }
    finally
    {
      getBufferPool().release(buf);
    }
  }
  

  public void setConfig(ExtensionConfig config)
  {
    super.setConfig(config);
    
    String cfgOutputDir = config.getParameter("output-dir", null);
    if (StringUtil.isNotBlank(cfgOutputDir))
    {
      Path path = new File(cfgOutputDir).toPath();
      if ((Files.isDirectory(path, new LinkOption[0])) && (Files.exists(path, new LinkOption[0])) && (Files.isWritable(path)))
      {
        outputDir = path;
      }
      else
      {
        LOG.warn("Unable to configure {}: not a valid output directory", new Object[] { path.toAbsolutePath().toString() });
      }
    }
    
    String cfgPrefix = config.getParameter("prefix", "frame");
    if (StringUtil.isNotBlank(cfgPrefix))
    {
      prefix = cfgPrefix;
    }
    
    if (outputDir != null)
    {
      try
      {
        Path dir = outputDir.toRealPath(new LinkOption[0]);
        

        String tstamp = String.format("%1$tY%1$tm%1$td-%1$tH%1$tM%1$tS", new Object[] { Calendar.getInstance() });
        incomingFramesPath = dir.resolve(String.format("%s-%s-incoming.dat", new Object[] { prefix, tstamp }));
        outgoingFramesPath = dir.resolve(String.format("%s-%s-outgoing.dat", new Object[] { prefix, tstamp }));
        
        incomingChannel = Files.newByteChannel(incomingFramesPath, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE });
        outgoingChannel = Files.newByteChannel(outgoingFramesPath, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE });
        
        generator = new Generator(WebSocketPolicy.newServerPolicy(), getBufferPool(), false, true);
      }
      catch (IOException e)
      {
        LOG.warn("Unable to create capture file(s)", e);
      }
    }
  }
}
