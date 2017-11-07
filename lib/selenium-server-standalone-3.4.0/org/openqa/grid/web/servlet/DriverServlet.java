package org.openqa.grid.web.servlet;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openqa.grid.internal.ExternalSessionKey;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.web.servlet.handler.RequestHandler;
import org.openqa.grid.web.servlet.handler.SeleniumBasedRequest;
import org.openqa.grid.web.servlet.handler.WebDriverRequest;


























public class DriverServlet
  extends RegistryBasedServlet
{
  private static final long serialVersionUID = -1693540182205547227L;
  
  public DriverServlet()
  {
    this(null);
  }
  
  public DriverServlet(Registry registry) {
    super(registry);
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    process(request, response);
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    process(request, response);
  }
  
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    process(request, response);
  }
  
  protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException
  {
    RequestHandler req = null;
    SeleniumBasedRequest r = null;
    try {
      r = SeleniumBasedRequest.createFromRequest(request, getRegistry());
      req = new RequestHandler(r, response, getRegistry());
      req.process();
    }
    catch (Throwable e) {
      if (((r instanceof WebDriverRequest)) && (!response.isCommitted()))
      {
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(500);
        
        JsonObject resp = new JsonObject();
        
        ExternalSessionKey serverSession = req.getServerSession();
        resp.addProperty("sessionId", serverSession != null ? serverSession.getKey() : null);
        resp.addProperty("status", Integer.valueOf(13));
        JsonObject value = new JsonObject();
        value.addProperty("message", e.getMessage());
        value.addProperty("class", e.getClass().getCanonicalName());
        
        JsonArray stacktrace = new JsonArray();
        for (StackTraceElement ste : e.getStackTrace()) {
          JsonObject st = new JsonObject();
          st.addProperty("fileName", ste.getFileName());
          st.addProperty("className", ste.getClassName());
          st.addProperty("methodName", ste.getMethodName());
          st.addProperty("lineNumber", Integer.valueOf(ste.getLineNumber()));
          stacktrace.add(st);
        }
        value.add("stackTrace", stacktrace);
        resp.add("value", value);
        
        String json = new Gson().toJson(resp);
        
        byte[] bytes = json.getBytes("UTF-8");
        Object in = new ByteArrayInputStream(bytes);
        try {
          response.setHeader("Content-Length", Integer.toString(bytes.length));
          ByteStreams.copy((InputStream)in, response.getOutputStream());
        } finally {
          ((InputStream)in).close();
          response.flushBuffer();
        }
      } else {
        throw new IOException(e);
      }
    }
  }
}
