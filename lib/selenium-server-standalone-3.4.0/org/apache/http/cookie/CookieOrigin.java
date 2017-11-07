package org.apache.http.cookie;

import java.util.Locale;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;


































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public final class CookieOrigin
{
  private final String host;
  private final int port;
  private final String path;
  private final boolean secure;
  
  public CookieOrigin(String host, int port, String path, boolean secure)
  {
    Args.notBlank(host, "Host");
    Args.notNegative(port, "Port");
    Args.notNull(path, "Path");
    this.host = host.toLowerCase(Locale.ROOT);
    this.port = port;
    if (!TextUtils.isBlank(path)) {
      this.path = path;
    } else {
      this.path = "/";
    }
    this.secure = secure;
  }
  
  public String getHost() {
    return host;
  }
  
  public String getPath() {
    return path;
  }
  
  public int getPort() {
    return port;
  }
  
  public boolean isSecure() {
    return secure;
  }
  
  public String toString()
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append('[');
    if (secure) {
      buffer.append("(secure)");
    }
    buffer.append(host);
    buffer.append(':');
    buffer.append(Integer.toString(port));
    buffer.append(path);
    buffer.append(']');
    return buffer.toString();
  }
}
