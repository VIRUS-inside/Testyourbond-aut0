package org.apache.http.impl.cookie;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.Obsolete;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.protocol.HttpContext;






































@Obsolete
@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class NetscapeDraftSpecProvider
  implements CookieSpecProvider
{
  private final String[] datepatterns;
  private volatile CookieSpec cookieSpec;
  
  public NetscapeDraftSpecProvider(String[] datepatterns)
  {
    this.datepatterns = datepatterns;
  }
  
  public NetscapeDraftSpecProvider() {
    this(null);
  }
  
  public CookieSpec create(HttpContext context)
  {
    if (cookieSpec == null) {
      synchronized (this) {
        if (cookieSpec == null) {
          cookieSpec = new NetscapeDraftSpec(datepatterns);
        }
      }
    }
    return cookieSpec;
  }
}
