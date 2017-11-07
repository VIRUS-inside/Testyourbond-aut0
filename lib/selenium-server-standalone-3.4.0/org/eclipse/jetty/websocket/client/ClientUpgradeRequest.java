package org.eclipse.jetty.websocket.client;

import java.net.CookieStore;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.util.B64Code;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.UrlEncoded;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.common.UpgradeRequestAdapter;


























public class ClientUpgradeRequest
  extends UpgradeRequestAdapter
{
  private static final Set<String> FORBIDDEN_HEADERS = new TreeSet(String.CASE_INSENSITIVE_ORDER);
  
  static { FORBIDDEN_HEADERS.add("cookie");
    
    FORBIDDEN_HEADERS.add("upgrade");
    FORBIDDEN_HEADERS.add("host");
    FORBIDDEN_HEADERS.add("connection");
    FORBIDDEN_HEADERS.add("sec-websocket-key");
    FORBIDDEN_HEADERS.add("sec-websocket-extensions");
    FORBIDDEN_HEADERS.add("sec-websocket-accept");
    FORBIDDEN_HEADERS.add("sec-websocket-protocol");
    FORBIDDEN_HEADERS.add("sec-websocket-version");
    FORBIDDEN_HEADERS.add("pragma");
    FORBIDDEN_HEADERS.add("cache-control");
  }
  

  private final String key;
  
  private Object localEndpoint;
  public ClientUpgradeRequest()
  {
    key = genRandomKey();
  }
  
  protected ClientUpgradeRequest(URI requestURI)
  {
    super(requestURI);
    key = genRandomKey();
  }
  
  public ClientUpgradeRequest(WebSocketUpgradeRequest wsRequest)
  {
    this(wsRequest.getURI());
    
    setCookies(wsRequest.getCookies());
    
    Map<String, List<String>> headers = new HashMap();
    HttpFields fields = wsRequest.getHeaders();
    for (HttpField field : fields)
    {
      String key = field.getName();
      List<String> values = (List)headers.get(key);
      if (values == null)
      {
        values = new ArrayList();
      }
      values.addAll(Arrays.asList(field.getValues()));
      headers.put(key, values);
      
      if (key.equalsIgnoreCase("Sec-WebSocket-Protocol"))
      {
        for (String subProtocol : field.getValue().split(","))
        {
          setSubProtocols(new String[] { subProtocol });
        }
      }
      
      if (key.equalsIgnoreCase("Sec-WebSocket-Extensions"))
      {
        for (??? = ExtensionConfig.parseList(field.getValues()).iterator(); ((Iterator)???).hasNext();) { ExtensionConfig ext = (ExtensionConfig)((Iterator)???).next();
          
          addExtensions(new ExtensionConfig[] { ext });
        }
      }
    }
    super.setHeaders(headers);
    
    setHttpVersion(wsRequest.getVersion().toString());
    setMethod(wsRequest.getMethod());
  }
  
  private final String genRandomKey()
  {
    byte[] bytes = new byte[16];
    ThreadLocalRandom.current().nextBytes(bytes);
    return new String(B64Code.encode(bytes));
  }
  
  public String getKey()
  {
    return key;
  }
  




  @Deprecated
  public void setCookiesFrom(CookieStore cookieStore)
  {
    throw new UnsupportedOperationException("Request specific CookieStore no longer supported");
  }
  

  public void setRequestURI(URI uri)
  {
    super.setRequestURI(uri);
    

    Map<String, List<String>> pmap = new HashMap();
    
    String query = uri.getQuery();
    
    if (StringUtil.isNotBlank(query))
    {
      MultiMap<String> params = new MultiMap();
      UrlEncoded.decodeTo(uri.getQuery(), params, StandardCharsets.UTF_8);
      
      for (String key : params.keySet())
      {
        List<String> values = params.getValues(key);
        if (values == null)
        {
          pmap.put(key, new ArrayList());

        }
        else
        {
          List<String> copy = new ArrayList();
          copy.addAll(values);
          pmap.put(key, copy);
        }
      }
      
      super.setParameterMap(pmap);
    }
  }
  
  public void setLocalEndpoint(Object websocket)
  {
    localEndpoint = websocket;
  }
  
  public Object getLocalEndpoint()
  {
    return localEndpoint;
  }
}
