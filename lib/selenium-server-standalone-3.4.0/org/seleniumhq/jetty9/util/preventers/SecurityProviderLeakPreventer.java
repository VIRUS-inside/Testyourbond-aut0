package org.seleniumhq.jetty9.util.preventers;

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
