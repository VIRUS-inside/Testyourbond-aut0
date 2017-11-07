package org.apache.http.impl.auth;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;




































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class SPNegoSchemeFactory
  implements AuthSchemeFactory, AuthSchemeProvider
{
  private final boolean stripPort;
  private final boolean useCanonicalHostname;
  
  public SPNegoSchemeFactory(boolean stripPort, boolean useCanonicalHostname)
  {
    this.stripPort = stripPort;
    this.useCanonicalHostname = useCanonicalHostname;
  }
  
  public SPNegoSchemeFactory(boolean stripPort)
  {
    this.stripPort = stripPort;
    useCanonicalHostname = true;
  }
  
  public SPNegoSchemeFactory() {
    this(true, true);
  }
  
  public boolean isStripPort() {
    return stripPort;
  }
  
  public boolean isUseCanonicalHostname() {
    return useCanonicalHostname;
  }
  
  public AuthScheme newInstance(HttpParams params)
  {
    return new SPNegoScheme(stripPort, useCanonicalHostname);
  }
  
  public AuthScheme create(HttpContext context)
  {
    return new SPNegoScheme(stripPort, useCanonicalHostname);
  }
}
