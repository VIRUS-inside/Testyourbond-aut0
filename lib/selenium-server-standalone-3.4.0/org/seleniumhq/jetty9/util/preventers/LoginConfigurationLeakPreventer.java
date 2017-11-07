package org.seleniumhq.jetty9.util.preventers;

import org.seleniumhq.jetty9.util.log.Logger;




























public class LoginConfigurationLeakPreventer
  extends AbstractLeakPreventer
{
  public LoginConfigurationLeakPreventer() {}
  
  public void prevent(ClassLoader loader)
  {
    try
    {
      Class.forName("javax.security.auth.login.Configuration", true, loader);
    }
    catch (ClassNotFoundException e)
    {
      LOG.warn(e);
    }
  }
}
