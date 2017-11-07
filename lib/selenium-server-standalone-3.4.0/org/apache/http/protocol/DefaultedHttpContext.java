package org.apache.http.protocol;

import org.apache.http.util.Args;





































@Deprecated
public final class DefaultedHttpContext
  implements HttpContext
{
  private final HttpContext local;
  private final HttpContext defaults;
  
  public DefaultedHttpContext(HttpContext local, HttpContext defaults)
  {
    this.local = ((HttpContext)Args.notNull(local, "HTTP context"));
    this.defaults = defaults;
  }
  
  public Object getAttribute(String id) {
    Object obj = local.getAttribute(id);
    if (obj == null) {
      return defaults.getAttribute(id);
    }
    return obj;
  }
  
  public Object removeAttribute(String id)
  {
    return local.removeAttribute(id);
  }
  
  public void setAttribute(String id, Object obj) {
    local.setAttribute(id, obj);
  }
  
  public HttpContext getDefaults() {
    return defaults;
  }
  
  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    buf.append("[local: ").append(local);
    buf.append("defaults: ").append(defaults);
    buf.append("]");
    return buf.toString();
  }
}
