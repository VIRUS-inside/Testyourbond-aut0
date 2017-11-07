package org.seleniumhq.jetty9.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
































public abstract class EventSourceServlet
  extends HttpServlet
{
  private static final byte[] CRLF = { 13, 10 };
  private static final byte[] EVENT_FIELD = "event: ".getBytes(StandardCharsets.UTF_8);
  private static final byte[] DATA_FIELD = "data: ".getBytes(StandardCharsets.UTF_8);
  private static final byte[] COMMENT_FIELD = ": ".getBytes(StandardCharsets.UTF_8);
  
  private ScheduledExecutorService scheduler;
  private int heartBeatPeriod = 10;
  
  public EventSourceServlet() {}
  
  public void init() throws ServletException {
    String heartBeatPeriodParam = getServletConfig().getInitParameter("heartBeatPeriod");
    if (heartBeatPeriodParam != null)
      heartBeatPeriod = Integer.parseInt(heartBeatPeriodParam);
    scheduler = Executors.newSingleThreadScheduledExecutor();
  }
  

  public void destroy()
  {
    if (scheduler != null) {
      scheduler.shutdown();
    }
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    Enumeration<String> acceptValues = request.getHeaders("Accept");
    while (acceptValues.hasMoreElements())
    {
      String accept = (String)acceptValues.nextElement();
      if (accept.equals("text/event-stream"))
      {
        EventSource eventSource = newEventSource(request);
        if (eventSource == null)
        {
          response.sendError(503);
        }
        else
        {
          respond(request, response);
          AsyncContext async = request.startAsync();
          

          async.setTimeout(0L);
          EventSourceEmitter emitter = new EventSourceEmitter(eventSource, async);
          emitter.scheduleHeartBeat();
          open(eventSource, emitter);
        }
        return;
      }
    }
    super.doGet(request, response);
  }
  
  protected abstract EventSource newEventSource(HttpServletRequest paramHttpServletRequest);
  
  protected void respond(HttpServletRequest request, HttpServletResponse response) throws IOException
  {
    response.setStatus(200);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setContentType("text/event-stream");
    


    response.addHeader("Connection", "close");
    response.flushBuffer();
  }
  
  protected void open(EventSource eventSource, EventSource.Emitter emitter) throws IOException
  {
    eventSource.onOpen(emitter);
  }
  
  protected class EventSourceEmitter implements EventSource.Emitter, Runnable
  {
    private final EventSource eventSource;
    private final AsyncContext async;
    private final ServletOutputStream output;
    private Future<?> heartBeat;
    private boolean closed;
    
    public EventSourceEmitter(EventSource eventSource, AsyncContext async) throws IOException
    {
      this.eventSource = eventSource;
      this.async = async;
      output = async.getResponse().getOutputStream();
    }
    
    public void event(String name, String data)
      throws IOException
    {
      synchronized (this)
      {
        output.write(EventSourceServlet.EVENT_FIELD);
        output.write(name.getBytes(StandardCharsets.UTF_8));
        output.write(EventSourceServlet.CRLF);
        data(data);
      }
    }
    
    public void data(String data)
      throws IOException
    {
      synchronized (this)
      {
        BufferedReader reader = new BufferedReader(new StringReader(data));
        String line;
        while ((line = reader.readLine()) != null)
        {
          output.write(EventSourceServlet.DATA_FIELD);
          output.write(line.getBytes(StandardCharsets.UTF_8));
          output.write(EventSourceServlet.CRLF);
        }
        output.write(EventSourceServlet.CRLF);
        flush();
      }
    }
    
    public void comment(String comment)
      throws IOException
    {
      synchronized (this)
      {
        output.write(EventSourceServlet.COMMENT_FIELD);
        output.write(comment.getBytes(StandardCharsets.UTF_8));
        output.write(EventSourceServlet.CRLF);
        output.write(EventSourceServlet.CRLF);
        flush();
      }
    }
    




    public void run()
    {
      try
      {
        synchronized (this)
        {
          output.write(13);
          flush();
          output.write(10);
          flush();
        }
        
        scheduleHeartBeat();

      }
      catch (IOException x)
      {
        close();
        eventSource.onClose();
      }
    }
    
    protected void flush() throws IOException
    {
      async.getResponse().flushBuffer();
    }
    

    public void close()
    {
      synchronized (this)
      {
        closed = true;
        heartBeat.cancel(false);
      }
      async.complete();
    }
    
    private void scheduleHeartBeat()
    {
      synchronized (this)
      {
        if (!closed) {
          heartBeat = scheduler.schedule(this, heartBeatPeriod, TimeUnit.SECONDS);
        }
      }
    }
  }
}
