package org.eclipse.jetty.util.preventers;

import org.eclipse.jetty.util.log.Logger;




























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
