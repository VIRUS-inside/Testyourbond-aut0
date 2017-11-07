package org.eclipse.jetty.util.preventers;

import java.security.Security;






























public class SecurityProviderLeakPreventer
  extends AbstractLeakPreventer
{
  public SecurityProviderLeakPreventer() {}
  
  public void prevent(ClassLoader loader)
  {
    Security.getProviders();
  }
}
