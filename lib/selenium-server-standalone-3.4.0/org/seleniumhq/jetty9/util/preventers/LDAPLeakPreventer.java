package org.seleniumhq.jetty9.util.preventers;

import org.seleniumhq.jetty9.util.log.Logger;






























public class LDAPLeakPreventer
  extends AbstractLeakPreventer
{
  public LDAPLeakPreventer() {}
  
  public void prevent(ClassLoader loader)
  {
    try
    {
      Class.forName("com.sun.jndi.LdapPoolManager", true, loader);
    }
    catch (ClassNotFoundException e)
    {
      LOG.ignore(e);
    }
  }
}
