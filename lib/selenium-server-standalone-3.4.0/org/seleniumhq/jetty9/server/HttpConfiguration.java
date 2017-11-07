package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.seleniumhq.jetty9.http.HttpMethod;
import org.seleniumhq.jetty9.http.HttpScheme;
import org.seleniumhq.jetty9.util.Jetty;
import org.seleniumhq.jetty9.util.TreeTrie;
import org.seleniumhq.jetty9.util.Trie;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
































@ManagedObject("HTTP Configuration")
public class HttpConfiguration
{
  public static final String SERVER_VERSION = "Jetty(" + Jetty.VERSION + ")";
  
  private final List<Customizer> _customizers = new CopyOnWriteArrayList();
  private final Trie<Boolean> _formEncodedMethods = new TreeTrie();
  private int _outputBufferSize = 32768;
  private int _outputAggregationSize = _outputBufferSize / 4;
  private int _requestHeaderSize = 8192;
  private int _responseHeaderSize = 8192;
  private int _headerCacheSize = 512;
  private int _securePort;
  private long _idleTimeout = -1L;
  private long _blockingTimeout = -1L;
  private String _secureScheme = HttpScheme.HTTPS.asString();
  private boolean _sendServerVersion = true;
  private boolean _sendXPoweredBy = false;
  private boolean _sendDateHeader = true;
  private boolean _delayDispatchUntilContent = true;
  private boolean _persistentConnectionsEnabled = true;
  private int _maxErrorDispatches = 10;
  












  private long _minRequestDataRate;
  













  public HttpConfiguration()
  {
    _formEncodedMethods.put(HttpMethod.POST.asString(), Boolean.TRUE);
    _formEncodedMethods.put(HttpMethod.PUT.asString(), Boolean.TRUE);
  }
  




  public HttpConfiguration(HttpConfiguration config)
  {
    _customizers.addAll(_customizers);
    for (String s : _formEncodedMethods.keySet())
      _formEncodedMethods.put(s, Boolean.TRUE);
    _outputBufferSize = _outputBufferSize;
    _outputAggregationSize = _outputAggregationSize;
    _requestHeaderSize = _requestHeaderSize;
    _responseHeaderSize = _responseHeaderSize;
    _headerCacheSize = _headerCacheSize;
    _secureScheme = _secureScheme;
    _securePort = _securePort;
    _idleTimeout = _idleTimeout;
    _blockingTimeout = _blockingTimeout;
    _sendDateHeader = _sendDateHeader;
    _sendServerVersion = _sendServerVersion;
    _sendXPoweredBy = _sendXPoweredBy;
    _delayDispatchUntilContent = _delayDispatchUntilContent;
    _persistentConnectionsEnabled = _persistentConnectionsEnabled;
    _maxErrorDispatches = _maxErrorDispatches;
    _minRequestDataRate = _minRequestDataRate;
  }
  








  public void addCustomizer(Customizer customizer)
  {
    _customizers.add(customizer);
  }
  

  public List<Customizer> getCustomizers()
  {
    return _customizers;
  }
  

  public <T> T getCustomizer(Class<T> type)
  {
    for (Customizer c : _customizers)
      if (type.isAssignableFrom(c.getClass()))
        return c;
    return null;
  }
  

  @ManagedAttribute("The size in bytes of the output buffer used to aggregate HTTP output")
  public int getOutputBufferSize()
  {
    return _outputBufferSize;
  }
  

  @ManagedAttribute("The maximum size in bytes for HTTP output to be aggregated")
  public int getOutputAggregationSize()
  {
    return _outputAggregationSize;
  }
  

  @ManagedAttribute("The maximum allowed size in bytes for a HTTP request header")
  public int getRequestHeaderSize()
  {
    return _requestHeaderSize;
  }
  

  @ManagedAttribute("The maximum allowed size in bytes for a HTTP response header")
  public int getResponseHeaderSize()
  {
    return _responseHeaderSize;
  }
  

  @ManagedAttribute("The maximum allowed size in bytes for a HTTP header field cache")
  public int getHeaderCacheSize()
  {
    return _headerCacheSize;
  }
  

  @ManagedAttribute("The port to which Integral or Confidential security constraints are redirected")
  public int getSecurePort()
  {
    return _securePort;
  }
  

  @ManagedAttribute("The scheme with which Integral or Confidential security constraints are redirected")
  public String getSecureScheme()
  {
    return _secureScheme;
  }
  

  @ManagedAttribute("Whether persistent connections are enabled")
  public boolean isPersistentConnectionsEnabled()
  {
    return _persistentConnectionsEnabled;
  }
  







  @ManagedAttribute("The idle timeout in ms for I/O operations during the handling of a HTTP request")
  public long getIdleTimeout()
  {
    return _idleTimeout;
  }
  







  public void setIdleTimeout(long timeoutMs)
  {
    _idleTimeout = timeoutMs;
  }
  








  @ManagedAttribute("Total timeout in ms for blocking I/O operations.")
  public long getBlockingTimeout()
  {
    return _blockingTimeout;
  }
  








  public void setBlockingTimeout(long blockingTimeout)
  {
    _blockingTimeout = blockingTimeout;
  }
  

  public void setPersistentConnectionsEnabled(boolean persistentConnectionsEnabled)
  {
    _persistentConnectionsEnabled = persistentConnectionsEnabled;
  }
  

  public void setSendServerVersion(boolean sendServerVersion)
  {
    _sendServerVersion = sendServerVersion;
  }
  

  @ManagedAttribute("Whether to send the Server header in responses")
  public boolean getSendServerVersion()
  {
    return _sendServerVersion;
  }
  
  public void writePoweredBy(Appendable out, String preamble, String postamble)
    throws IOException
  {
    if (getSendServerVersion())
    {
      if (preamble != null)
        out.append(preamble);
      out.append(Jetty.POWERED_BY);
      if (postamble != null) {
        out.append(postamble);
      }
    }
  }
  
  public void setSendXPoweredBy(boolean sendXPoweredBy)
  {
    _sendXPoweredBy = sendXPoweredBy;
  }
  

  @ManagedAttribute("Whether to send the X-Powered-By header in responses")
  public boolean getSendXPoweredBy()
  {
    return _sendXPoweredBy;
  }
  

  public void setSendDateHeader(boolean sendDateHeader)
  {
    _sendDateHeader = sendDateHeader;
  }
  

  @ManagedAttribute("Whether to send the Date header in responses")
  public boolean getSendDateHeader()
  {
    return _sendDateHeader;
  }
  




  public void setDelayDispatchUntilContent(boolean delay)
  {
    _delayDispatchUntilContent = delay;
  }
  

  @ManagedAttribute("Whether to delay the application dispatch until content is available")
  public boolean isDelayDispatchUntilContent()
  {
    return _delayDispatchUntilContent;
  }
  








  public void setCustomizers(List<Customizer> customizers)
  {
    _customizers.clear();
    _customizers.addAll(customizers);
  }
  








  public void setOutputBufferSize(int outputBufferSize)
  {
    _outputBufferSize = outputBufferSize;
    setOutputAggregationSize(outputBufferSize / 4);
  }
  








  public void setOutputAggregationSize(int outputAggregationSize)
  {
    _outputAggregationSize = outputAggregationSize;
  }
  







  public void setRequestHeaderSize(int requestHeaderSize)
  {
    _requestHeaderSize = requestHeaderSize;
  }
  







  public void setResponseHeaderSize(int responseHeaderSize)
  {
    _responseHeaderSize = responseHeaderSize;
  }
  




  public void setHeaderCacheSize(int headerCacheSize)
  {
    _headerCacheSize = headerCacheSize;
  }
  




  public void setSecurePort(int securePort)
  {
    _securePort = securePort;
  }
  




  public void setSecureScheme(String secureScheme)
  {
    _secureScheme = secureScheme;
  }
  


  public String toString()
  {
    return String.format("%s@%x{%d/%d,%d/%d,%s://:%d,%s}", new Object[] {
      getClass().getSimpleName(), 
      Integer.valueOf(hashCode()), 
      Integer.valueOf(_outputBufferSize), Integer.valueOf(_outputAggregationSize), 
      Integer.valueOf(_requestHeaderSize), Integer.valueOf(_responseHeaderSize), _secureScheme, 
      Integer.valueOf(_securePort), _customizers });
  }
  







  public void setFormEncodedMethods(String... methods)
  {
    _formEncodedMethods.clear();
    for (String method : methods) {
      addFormEncodedMethod(method);
    }
  }
  





  public Set<String> getFormEncodedMethods()
  {
    return _formEncodedMethods.keySet();
  }
  






  public void addFormEncodedMethod(String method)
  {
    _formEncodedMethods.put(method, Boolean.TRUE);
  }
  









  public boolean isFormEncodedMethod(String method)
  {
    return Boolean.TRUE.equals(_formEncodedMethods.get(method));
  }
  




  @ManagedAttribute("The maximum ERROR dispatches for a request for loop prevention (default 10)")
  public int getMaxErrorDispatches()
  {
    return _maxErrorDispatches;
  }
  




  public void setMaxErrorDispatches(int max)
  {
    _maxErrorDispatches = max;
  }
  




  @ManagedAttribute("The minimum request content data rate in bytes per second")
  public long getMinRequestDataRate()
  {
    return _minRequestDataRate;
  }
  




  public void setMinRequestDataRate(long bytesPerSecond)
  {
    _minRequestDataRate = bytesPerSecond;
  }
  
  public static abstract interface ConnectionFactory
  {
    public abstract HttpConfiguration getHttpConfiguration();
  }
  
  public static abstract interface Customizer
  {
    public abstract void customize(Connector paramConnector, HttpConfiguration paramHttpConfiguration, Request paramRequest);
  }
}
