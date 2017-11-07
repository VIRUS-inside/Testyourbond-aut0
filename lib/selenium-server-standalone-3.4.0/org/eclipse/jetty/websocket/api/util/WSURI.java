package org.eclipse.jetty.websocket.api.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;






























public final class WSURI
{
  public WSURI() {}
  
  public static URI toHttp(URI inputUri)
    throws URISyntaxException
  {
    Objects.requireNonNull(inputUri, "Input URI must not be null");
    String wsScheme = inputUri.getScheme();
    String httpScheme = null;
    if (("http".equalsIgnoreCase(wsScheme)) || ("https".equalsIgnoreCase(wsScheme)))
    {

      httpScheme = wsScheme;
    }
    else if ("ws".equalsIgnoreCase(wsScheme))
    {

      httpScheme = "http";
    }
    else if ("wss".equalsIgnoreCase(wsScheme))
    {

      httpScheme = "https";
    }
    else
    {
      throw new URISyntaxException(inputUri.toString(), "Unrecognized WebSocket scheme");
    }
    
    return new URI(httpScheme, inputUri.getUserInfo(), inputUri.getHost(), inputUri.getPort(), inputUri.getPath(), inputUri.getQuery(), inputUri.getFragment());
  }
  










  public static URI toWebsocket(CharSequence inputUrl)
    throws URISyntaxException
  {
    return toWebsocket(new URI(inputUrl.toString()));
  }
  












  public static URI toWebsocket(CharSequence inputUrl, String query)
    throws URISyntaxException
  {
    if (query == null)
    {
      return toWebsocket(new URI(inputUrl.toString()));
    }
    return toWebsocket(new URI(inputUrl.toString() + '?' + query));
  }
  











  public static URI toWebsocket(URI inputUri)
    throws URISyntaxException
  {
    Objects.requireNonNull(inputUri, "Input URI must not be null");
    String httpScheme = inputUri.getScheme();
    String wsScheme = null;
    if (("ws".equalsIgnoreCase(httpScheme)) || ("wss".equalsIgnoreCase(httpScheme)))
    {

      wsScheme = httpScheme;
    }
    else if ("http".equalsIgnoreCase(httpScheme))
    {

      wsScheme = "ws";
    }
    else if ("https".equalsIgnoreCase(httpScheme))
    {

      wsScheme = "wss";
    }
    else
    {
      throw new URISyntaxException(inputUri.toString(), "Unrecognized HTTP scheme");
    }
    return new URI(wsScheme, inputUri.getUserInfo(), inputUri.getHost(), inputUri.getPort(), inputUri.getPath(), inputUri.getQuery(), inputUri.getFragment());
  }
}
