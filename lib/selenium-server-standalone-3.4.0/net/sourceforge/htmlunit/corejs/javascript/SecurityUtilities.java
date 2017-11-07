package net.sourceforge.htmlunit.corejs.javascript;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;














public class SecurityUtilities
{
  public SecurityUtilities() {}
  
  public static String getSystemProperty(String name)
  {
    (String)AccessController.doPrivileged(new PrivilegedAction() {
      public String run() {
        return System.getProperty(val$name);
      }
    });
  }
  
  public static ProtectionDomain getProtectionDomain(Class<?> clazz) {
    
      (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction() {
        public ProtectionDomain run() {
          return val$clazz.getProtectionDomain();
        }
      });
  }
  








  public static ProtectionDomain getScriptProtectionDomain()
  {
    SecurityManager securityManager = System.getSecurityManager();
    if ((securityManager instanceof RhinoSecurityManager)) {
      
        (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction()
        {
          public ProtectionDomain run() {
            Class<?> c = ((RhinoSecurityManager)val$securityManager).getCurrentScriptClass();
            return c == null ? null : c.getProtectionDomain();
          }
        });
    }
    return null;
  }
}
