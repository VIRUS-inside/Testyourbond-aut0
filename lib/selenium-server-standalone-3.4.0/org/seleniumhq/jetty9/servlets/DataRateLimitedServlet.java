package org.seleniumhq.jetty9.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.server.HttpOutput;







































public class DataRateLimitedServlet
  extends HttpServlet
{
  private static final long serialVersionUID = -4771757707068097025L;
  private int buffersize = 8192;
  private long pauseNS = TimeUnit.MILLISECONDS.toNanos(100L);
  ScheduledThreadPoolExecutor scheduler;
  private final ConcurrentHashMap<String, ByteBuffer> cache = new ConcurrentHashMap();
  
  public DataRateLimitedServlet() {}
  
  public void init() throws ServletException
  {
    String tmp = getInitParameter("buffersize");
    if (tmp != null)
      buffersize = Integer.parseInt(tmp);
    tmp = getInitParameter("pause");
    if (tmp != null)
      pauseNS = TimeUnit.MILLISECONDS.toNanos(Integer.parseInt(tmp));
    tmp = getInitParameter("pool");
    int pool = tmp == null ? Runtime.getRuntime().availableProcessors() : Integer.parseInt(tmp);
    

    scheduler = new ScheduledThreadPoolExecutor(pool);
  }
  

  public void destroy()
  {
    scheduler.shutdown();
  }
  

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String info = request.getPathInfo();
    

    if (info.endsWith("/"))
    {
      response.sendError(503, "directories not supported");
      return;
    }
    

    String content_type = getServletContext().getMimeType(info);
    response.setContentType(content_type == null ? "application/x-data" : content_type);
    

    String path = request.getPathTranslated();
    

    ServletOutputStream out = response.getOutputStream();
    if ((path != null) && ((out instanceof HttpOutput)))
    {

      File file = new File(path);
      if ((file.exists()) && (file.canRead()))
      {

        response.setContentLengthLong(file.length());
        

        ByteBuffer mapped = (ByteBuffer)cache.get(path);
        

        if (mapped == null)
        {

          RandomAccessFile raf = new RandomAccessFile(file, "r");Throwable localThrowable3 = null;
          try {
            ByteBuffer buf = raf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, raf.length());
            mapped = (ByteBuffer)cache.putIfAbsent(path, buf);
            if (mapped == null) {
              mapped = buf;
            }
          }
          catch (Throwable localThrowable1)
          {
            localThrowable3 = localThrowable1;throw localThrowable1;

          }
          finally
          {

            if (raf != null) if (localThrowable3 != null) try { raf.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { raf.close();
              }
          }
        }
        AsyncContext async = request.startAsync();
        

        out.setWriteListener(new JettyDataStream(mapped, async, out, null));
        return;
      }
    }
    



    InputStream content = getServletContext().getResourceAsStream(info);
    if (content == null)
    {
      response.sendError(404);
      return;
    }
    

    out.setWriteListener(new StandardDataStream(content, request.startAsync(), out, null));
  }
  

  private final class StandardDataStream
    implements WriteListener, Runnable
  {
    private final InputStream content;
    
    private final AsyncContext async;
    private final ServletOutputStream out;
    
    private StandardDataStream(InputStream content, AsyncContext async, ServletOutputStream out)
    {
      this.content = content;
      this.async = async;
      this.out = out;
    }
    

    public void onWritePossible()
      throws IOException
    {
      if (out.isReady())
      {


        byte[] buffer = new byte[buffersize];
        

        int len = content.read(buffer);
        

        if (len < 0)
        {

          async.complete();
          return;
        }
        




        out.write(buffer, 0, len);
        


        scheduler.schedule(this, pauseNS, TimeUnit.NANOSECONDS);
      }
    }
    




    public void run()
    {
      try
      {
        onWritePossible();
      }
      catch (Exception e)
      {
        onError(e);
      }
    }
    

    public void onError(Throwable t)
    {
      getServletContext().log("Async Error", t);
      async.complete();
    }
  }
  


  private final class JettyDataStream
    implements WriteListener, Runnable
  {
    private final ByteBuffer content;
    
    private final int limit;
    
    private final AsyncContext async;
    
    private final HttpOutput out;
    

    private JettyDataStream(ByteBuffer content, AsyncContext async, ServletOutputStream out)
    {
      this.content = content.asReadOnlyBuffer();
      
      limit = this.content.limit();
      this.async = async;
      this.out = ((HttpOutput)out);
    }
    

    public void onWritePossible()
      throws IOException
    {
      if (out.isReady())
      {

        int l = content.position() + buffersize;
        
        if (l > limit)
          l = limit;
        content.limit(l);
        

        if (!content.hasRemaining())
        {

          async.complete();
          return;
        }
        




        out.write(content);
        


        scheduler.schedule(this, pauseNS, TimeUnit.NANOSECONDS);
      }
    }
    




    public void run()
    {
      try
      {
        onWritePossible();
      }
      catch (Exception e)
      {
        onError(e);
      }
    }
    

    public void onError(Throwable t)
    {
      getServletContext().log("Async Error", t);
      async.complete();
    }
  }
}
