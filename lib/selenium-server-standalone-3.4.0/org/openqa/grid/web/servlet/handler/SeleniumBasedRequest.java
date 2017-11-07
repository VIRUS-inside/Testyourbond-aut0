package org.openqa.grid.web.servlet.handler;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.io.ByteStreams;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.openqa.grid.common.exception.GridException;
import org.openqa.grid.internal.ExternalSessionKey;
import org.openqa.grid.internal.Registry;




























public abstract class SeleniumBasedRequest
  extends HttpServletRequestWrapper
{
  private byte[] body;
  private final Registry registry;
  private final RequestType type;
  private final String encoding = "UTF-8";
  private final Map<String, Object> desiredCapability;
  private final long timestamp = System.currentTimeMillis();
  
  private static List<SeleniumBasedRequestFactory> requestFactories = new ImmutableList.Builder()
  
    .add(new WebDriverRequestFactory())
    .add(new LegacySeleniumRequestFactory())
    .build();
  
  public static SeleniumBasedRequest createFromRequest(HttpServletRequest request, Registry registry) {
    for (SeleniumBasedRequestFactory factory : requestFactories) {
      SeleniumBasedRequest sbr = factory.createFromRequest(request, registry);
      if (sbr != null) {
        return sbr;
      }
    }
    throw new GridException("Request path " + request.getServletPath() + " is not recognized");
  }
  
  @VisibleForTesting
  public SeleniumBasedRequest(HttpServletRequest request, Registry registry, RequestType type, Map<String, Object> desiredCapability)
  {
    super(request);
    this.registry = registry;
    this.type = type;
    this.desiredCapability = desiredCapability;
  }
  
  public SeleniumBasedRequest(HttpServletRequest httpServletRequest, Registry registry) {
    super(httpServletRequest);
    try {
      InputStream is = super.getInputStream();
      setBody(ByteStreams.toByteArray(is));
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    this.registry = registry;
    type = extractRequestType();
    
    if (type == RequestType.START_SESSION) {
      desiredCapability = extractDesiredCapability();
    } else {
      desiredCapability = null;
    }
  }
  
  public Registry getRegistry() {
    return registry;
  }
  




  public abstract RequestType extractRequestType();
  




  public abstract ExternalSessionKey extractSession();
  



  public abstract Map<String, Object> extractDesiredCapability();
  



  public RequestType getRequestType()
  {
    return type;
  }
  
  public ServletInputStream getInputStream() throws IOException
  {
    return new ServletInputStreamImpl(new ByteArrayInputStream(body));
  }
  
  public BufferedReader getReader() throws IOException
  {
    return new BufferedReader(new InputStreamReader(getInputStream(), "UTF-8"));
  }
  
  public int getContentLength()
  {
    if (body == null) {
      return 0;
    }
    return body.length;
  }
  
  public String getBody() {
    try {
      Charset charset = Charset.forName("UTF-8");
      CharsetDecoder decoder = charset.newDecoder();
      CharBuffer cbuf = decoder.decode(ByteBuffer.wrap(body));
      return cbuf.toString();
    } catch (CharacterCodingException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public void setBody(String content) {
    setBody(content.getBytes());
  }
  
  public void setBody(byte[] content) {
    body = content;
    setAttribute("Content-Length", Integer.valueOf(content.length));
  }
  
  public long getCreationTime() {
    return timestamp;
  }
  
  public String toString() {
    SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
    StringBuilder builder = new StringBuilder();
    builder.append("[" + format.format(new Date(timestamp)) + "] ");
    builder.append(getMethod().toUpperCase() + " " + getPathInfo() + "   ");
    if ((getBody() != null) && (!getBody().isEmpty())) {
      builder.append(getBody());
    }
    return builder.toString();
  }
  
  public Map<String, Object> getDesiredCapabilities() {
    return desiredCapability;
  }
  
  private class ServletInputStreamImpl extends ServletInputStream
  {
    private InputStream is;
    
    public ServletInputStreamImpl(InputStream is) {
      this.is = is;
    }
    
    public int read() throws IOException {
      return is.read();
    }
    
    public boolean markSupported() {
      return false;
    }
    
    public synchronized void mark(int i) {
      throw new RuntimeException("not implemented");
    }
    
    public synchronized void reset() throws IOException {
      throw new RuntimeException("not implemented");
    }
    
    public boolean isFinished() {
      return false;
    }
    
    public boolean isReady() {
      return true;
    }
    
    public void setReadListener(ReadListener readListener) {
      throw new RuntimeException("setReadListener");
    }
  }
}
