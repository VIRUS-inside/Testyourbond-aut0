package org.apache.http.protocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;



































@Contract(threading=ThreadingBehavior.SAFE_CONDITIONAL)
public class BasicHttpContext
  implements HttpContext
{
  private final HttpContext parentContext;
  private final Map<String, Object> map;
  
  public BasicHttpContext()
  {
    this(null);
  }
  
  public BasicHttpContext(HttpContext parentContext)
  {
    map = new ConcurrentHashMap();
    this.parentContext = parentContext;
  }
  
  public Object getAttribute(String id)
  {
    Args.notNull(id, "Id");
    Object obj = map.get(id);
    if ((obj == null) && (parentContext != null)) {
      obj = parentContext.getAttribute(id);
    }
    return obj;
  }
  
  public void setAttribute(String id, Object obj)
  {
    Args.notNull(id, "Id");
    if (obj != null) {
      map.put(id, obj);
    } else {
      map.remove(id);
    }
  }
  
  public Object removeAttribute(String id)
  {
    Args.notNull(id, "Id");
    return map.remove(id);
  }
  


  public void clear()
  {
    map.clear();
  }
  
  public String toString()
  {
    return map.toString();
  }
}
