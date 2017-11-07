package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.seleniumhq.jetty9.http.HttpField;
import org.seleniumhq.jetty9.http.HttpParser;
import org.seleniumhq.jetty9.http.HttpParser.ResponseHandler;
import org.seleniumhq.jetty9.http.HttpVersion;
import org.seleniumhq.jetty9.io.ByteArrayEndPoint;
import org.seleniumhq.jetty9.io.ByteBufferPool;
import org.seleniumhq.jetty9.io.Connection;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.ByteArrayOutputStream2;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.ssl.SslContextFactory;
import org.seleniumhq.jetty9.util.thread.Scheduler;



























public class LocalConnector
  extends AbstractConnector
{
  private final BlockingQueue<LocalEndPoint> _connects = new LinkedBlockingQueue();
  
  public LocalConnector(Server server, Executor executor, Scheduler scheduler, ByteBufferPool pool, int acceptors, ConnectionFactory... factories)
  {
    super(server, executor, scheduler, pool, acceptors, factories);
    setIdleTimeout(30000L);
  }
  
  public LocalConnector(Server server)
  {
    this(server, null, null, null, -1, new ConnectionFactory[] { new HttpConnectionFactory() });
  }
  
  public LocalConnector(Server server, SslContextFactory sslContextFactory)
  {
    this(server, null, null, null, -1, AbstractConnectionFactory.getFactories(sslContextFactory, new ConnectionFactory[] { new HttpConnectionFactory() }));
  }
  
  public LocalConnector(Server server, ConnectionFactory connectionFactory)
  {
    this(server, null, null, null, -1, new ConnectionFactory[] { connectionFactory });
  }
  
  public LocalConnector(Server server, ConnectionFactory connectionFactory, SslContextFactory sslContextFactory)
  {
    this(server, null, null, null, -1, AbstractConnectionFactory.getFactories(sslContextFactory, new ConnectionFactory[] { connectionFactory }));
  }
  

  public Object getTransport()
  {
    return this;
  }
  











  @Deprecated
  public String getResponses(String requests)
    throws Exception
  {
    return getResponses(requests, 5L, TimeUnit.SECONDS);
  }
  













  @Deprecated
  public String getResponses(String requests, long idleFor, TimeUnit units)
    throws Exception
  {
    ByteBuffer result = getResponses(BufferUtil.toBuffer(requests, StandardCharsets.UTF_8), idleFor, units);
    return result == null ? null : BufferUtil.toString(result, StandardCharsets.UTF_8);
  }
  











  @Deprecated
  public ByteBuffer getResponses(ByteBuffer requestsBuffer)
    throws Exception
  {
    return getResponses(requestsBuffer, 5L, TimeUnit.SECONDS);
  }
  












  @Deprecated
  public ByteBuffer getResponses(ByteBuffer requestsBuffer, long idleFor, TimeUnit units)
    throws Exception
  {
    if (LOG.isDebugEnabled())
      LOG.debug("requests {}", new Object[] { BufferUtil.toUTF8String(requestsBuffer) });
    LocalEndPoint endp = executeRequest(requestsBuffer);
    endp.waitUntilClosedOrIdleFor(idleFor, units);
    ByteBuffer responses = endp.takeOutput();
    if (endp.isOutputShutdown())
      endp.close();
    if (LOG.isDebugEnabled())
      LOG.debug("responses {}", new Object[] { BufferUtil.toUTF8String(responses) });
    return responses;
  }
  






  public LocalEndPoint executeRequest(String rawRequest)
  {
    return executeRequest(BufferUtil.toBuffer(rawRequest, StandardCharsets.UTF_8));
  }
  
  private LocalEndPoint executeRequest(ByteBuffer rawRequest)
  {
    if (!isStarted())
      throw new IllegalStateException("!STARTED");
    LocalEndPoint endp = new LocalEndPoint();
    endp.addInput(rawRequest);
    _connects.add(endp);
    return endp;
  }
  
  public LocalEndPoint connect()
  {
    LocalEndPoint endp = new LocalEndPoint();
    _connects.add(endp);
    return endp;
  }
  
  protected void accept(int acceptorID)
    throws IOException, InterruptedException
  {
    if (LOG.isDebugEnabled())
      LOG.debug("accepting {}", acceptorID);
    LocalEndPoint endPoint = (LocalEndPoint)_connects.take();
    endPoint.onOpen();
    onEndPointOpened(endPoint);
    
    Connection connection = getDefaultConnectionFactory().newConnection(this, endPoint);
    endPoint.setConnection(connection);
    
    connection.onOpen();
  }
  





  public ByteBuffer getResponse(ByteBuffer requestsBuffer)
    throws Exception
  {
    return getResponse(requestsBuffer, false, 10L, TimeUnit.SECONDS);
  }
  






  public ByteBuffer getResponse(ByteBuffer requestBuffer, long time, TimeUnit unit)
    throws Exception
  {
    boolean head = BufferUtil.toString(requestBuffer).toLowerCase().startsWith("head ");
    if (LOG.isDebugEnabled())
      LOG.debug("requests {}", new Object[] { BufferUtil.toUTF8String(requestBuffer) });
    LocalEndPoint endp = executeRequest(requestBuffer);
    return endp.waitForResponse(head, time, unit);
  }
  







  public ByteBuffer getResponse(ByteBuffer requestBuffer, boolean head, long time, TimeUnit unit)
    throws Exception
  {
    if (LOG.isDebugEnabled())
      LOG.debug("requests {}", new Object[] { BufferUtil.toUTF8String(requestBuffer) });
    LocalEndPoint endp = executeRequest(requestBuffer);
    return endp.waitForResponse(head, time, unit);
  }
  





  public String getResponse(String rawRequest)
    throws Exception
  {
    return getResponse(rawRequest, false, 30L, TimeUnit.SECONDS);
  }
  






  public String getResponse(String rawRequest, long time, TimeUnit unit)
    throws Exception
  {
    boolean head = rawRequest.toLowerCase().startsWith("head ");
    ByteBuffer requestsBuffer = BufferUtil.toBuffer(rawRequest, StandardCharsets.ISO_8859_1);
    if (LOG.isDebugEnabled())
      LOG.debug("request {}", new Object[] { BufferUtil.toUTF8String(requestsBuffer) });
    LocalEndPoint endp = executeRequest(requestsBuffer);
    
    return BufferUtil.toString(endp.waitForResponse(head, time, unit), StandardCharsets.ISO_8859_1);
  }
  








  public String getResponse(String rawRequest, boolean head, long time, TimeUnit unit)
    throws Exception
  {
    ByteBuffer requestsBuffer = BufferUtil.toBuffer(rawRequest, StandardCharsets.ISO_8859_1);
    if (LOG.isDebugEnabled())
      LOG.debug("request {}", new Object[] { BufferUtil.toUTF8String(requestsBuffer) });
    LocalEndPoint endp = executeRequest(requestsBuffer);
    
    return BufferUtil.toString(endp.waitForResponse(head, time, unit), StandardCharsets.ISO_8859_1);
  }
  

  public class LocalEndPoint
    extends ByteArrayEndPoint
  {
    private final CountDownLatch _closed = new CountDownLatch(1);
    private ByteBuffer _responseData;
    
    public LocalEndPoint()
    {
      super(getIdleTimeout());
      setGrowOutput(true);
    }
    
    protected void execute(Runnable task)
    {
      getExecutor().execute(task);
    }
    

    public void onClose()
    {
      getConnection().onClose();
      onEndPointClosed(this);
      super.onClose();
      _closed.countDown();
    }
    

    public void doShutdownOutput()
    {
      super.shutdownOutput();
      close();
    }
    
    public void waitUntilClosed()
    {
      while (isOpen())
      {
        try
        {
          if (!_closed.await(10L, TimeUnit.SECONDS)) {
            break;
          }
        }
        catch (Exception e) {
          LOG.warn(e);
        }
      }
    }
    
    public void waitUntilClosedOrIdleFor(long idleFor, TimeUnit units)
    {
      Thread.yield();
      int size = getOutput().remaining();
      while (isOpen())
      {
        try
        {
          if (!_closed.await(idleFor, units))
          {
            if (size == getOutput().remaining())
            {
              if (LOG.isDebugEnabled())
                LOG.debug("idle for {} {}", new Object[] { Long.valueOf(idleFor), units });
              return;
            }
            size = getOutput().remaining();
          }
        }
        catch (Exception e)
        {
          LOG.warn(e);
        }
      }
    }
    




    public String getResponse()
      throws Exception
    {
      return getResponse(false, 30L, TimeUnit.SECONDS);
    }
    







    public String getResponse(boolean head, long time, TimeUnit unit)
      throws Exception
    {
      ByteBuffer response = waitForResponse(head, time, unit);
      if (response != null)
        return BufferUtil.toString(response);
      return null;
    }
    







    public ByteBuffer waitForResponse(boolean head, long time, TimeUnit unit)
      throws Exception
    {
      HttpParser.ResponseHandler handler = new HttpParser.ResponseHandler()
      {
        public void parsedHeader(HttpField field) {}
        




        public boolean messageComplete()
        {
          return true;
        }
        

        public boolean headerComplete()
        {
          return false;
        }
        

        public int getHeaderCacheSize()
        {
          return 0;
        }
        


        public void earlyEOF() {}
        


        public boolean content(ByteBuffer item)
        {
          return false;
        }
        


        public void badMessage(int status, String reason) {}
        


        public boolean startResponse(HttpVersion version, int status, String reason)
        {
          return false;
        }
        
      };
      HttpParser parser = new HttpParser(handler);
      parser.setHeadResponse(head);
      ByteArrayOutputStream2 bout = new ByteArrayOutputStream2();Throwable localThrowable5 = null;
      try
      {
        ByteBuffer chunk;
        for (;;) {
          ByteBuffer chunk;
          if (BufferUtil.hasContent(_responseData)) {
            chunk = _responseData;
          }
          else {
            chunk = waitForOutput(time, unit);
            if ((BufferUtil.isEmpty(chunk)) && ((!isOpen()) || (isOutputShutdown())))
            {
              parser.atEOF();
              parser.parseNext(BufferUtil.EMPTY_BUFFER);
              break;
            }
          }
          

          while (BufferUtil.hasContent(chunk))
          {
            int pos = chunk.position();
            boolean complete = parser.parseNext(chunk);
            if (chunk.position() == pos)
            {

              if (BufferUtil.isEmpty(chunk))
                break;
              return null;
            }
            

            bout.write(chunk.array(), chunk.arrayOffset() + pos, chunk.position() - pos);
            

            if (complete)
            {
              if (!BufferUtil.hasContent(chunk)) break label246;
              _responseData = chunk;
              break label246;
            }
          }
        }
        label246:
        if ((bout.getCount() == 0) && (isOutputShutdown()))
          return null;
        return ByteBuffer.wrap(bout.getBuf(), 0, bout.getCount());
      }
      catch (Throwable localThrowable3)
      {
        localThrowable5 = localThrowable3;throw localThrowable3;






















      }
      finally
      {






















        if (bout != null) if (localThrowable5 != null) try { bout.close(); } catch (Throwable localThrowable4) { localThrowable5.addSuppressed(localThrowable4); } else bout.close();
      }
    }
  }
}
