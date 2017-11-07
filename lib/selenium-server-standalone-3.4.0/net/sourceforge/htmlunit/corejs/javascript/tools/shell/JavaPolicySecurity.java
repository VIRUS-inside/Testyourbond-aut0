package net.sourceforge.htmlunit.corejs.javascript.tools.shell;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import net.sourceforge.htmlunit.corejs.javascript.Callable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.GeneratedClassLoader;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

public class JavaPolicySecurity extends SecurityProxy
{
  public Class<?> getStaticSecurityDomainClassInternal()
  {
    return ProtectionDomain.class;
  }
  
  private static class Loader extends ClassLoader implements GeneratedClassLoader
  {
    private ProtectionDomain domain;
    
    Loader(ClassLoader parent, ProtectionDomain domain) {
      super();
      this.domain = domain;
    }
    
    public Class<?> defineClass(String name, byte[] data) {
      return super.defineClass(name, data, 0, data.length, domain);
    }
    
    public void linkClass(Class<?> cl) {
      resolveClass(cl);
    }
  }
  
  private static class ContextPermissions extends PermissionCollection
  {
    static final long serialVersionUID = -1721494496320750721L;
    AccessControlContext _context;
    PermissionCollection _statisPermissions;
    
    ContextPermissions(ProtectionDomain staticDomain)
    {
      _context = AccessController.getContext();
      if (staticDomain != null) {
        _statisPermissions = staticDomain.getPermissions();
      }
      setReadOnly();
    }
    
    public void add(Permission permission)
    {
      throw new RuntimeException("NOT IMPLEMENTED");
    }
    
    public boolean implies(Permission permission)
    {
      if ((_statisPermissions != null) && 
        (!_statisPermissions.implies(permission))) {
        return false;
      }
      try
      {
        _context.checkPermission(permission);
        return true;
      } catch (java.security.AccessControlException ex) {}
      return false;
    }
    

    public java.util.Enumeration<Permission> elements()
    {
      new java.util.Enumeration() {
        public boolean hasMoreElements() {
          return false;
        }
        
        public Permission nextElement() {
          return null;
        }
      };
    }
    
    public String toString()
    {
      StringBuilder sb = new StringBuilder();
      sb.append(getClass().getName());
      sb.append('@');
      sb.append(Integer.toHexString(System.identityHashCode(this)));
      sb.append(" (context=");
      sb.append(_context);
      sb.append(", static_permitions=");
      sb.append(_statisPermissions);
      sb.append(')');
      return sb.toString();
    }
  }
  



  public JavaPolicySecurity()
  {
    new CodeSource(null, (java.security.cert.Certificate[])null);
  }
  

  protected void callProcessFileSecure(final Context cx, final Scriptable scope, final String filename)
  {
    AccessController.doPrivileged(new PrivilegedAction() {
      public Object run() {
        URL url = JavaPolicySecurity.this.getUrlObj(filename);
        ProtectionDomain staticDomain = JavaPolicySecurity.this.getUrlDomain(url);
        try {
          Main.processFileSecure(cx, scope, url.toExternalForm(), staticDomain);
        }
        catch (java.io.IOException ioex) {
          throw new RuntimeException(ioex);
        }
        return null;
      }
    });
  }
  
  private URL getUrlObj(String url)
  {
    try {
      urlObj = new URL(url);
    }
    catch (MalformedURLException ex) {
      URL urlObj;
      String curDir = System.getProperty("user.dir");
      curDir = curDir.replace('\\', '/');
      if (!curDir.endsWith("/")) {
        curDir = curDir + '/';
      }
      try {
        URL curDirURL = new URL("file:" + curDir);
        urlObj = new URL(curDirURL, url);
      } catch (MalformedURLException ex2) {
        URL urlObj;
        throw new RuntimeException("Can not construct file URL for '" + url + "':" + ex2.getMessage());
      } }
    URL urlObj;
    return urlObj;
  }
  
  private ProtectionDomain getUrlDomain(URL url)
  {
    CodeSource cs = new CodeSource(url, (java.security.cert.Certificate[])null);
    PermissionCollection pc = java.security.Policy.getPolicy().getPermissions(cs);
    return new ProtectionDomain(cs, pc);
  }
  

  public GeneratedClassLoader createClassLoader(final ClassLoader parentLoader, Object securityDomain)
  {
    final ProtectionDomain domain = (ProtectionDomain)securityDomain;
    (GeneratedClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
      public JavaPolicySecurity.Loader run() {
        return new JavaPolicySecurity.Loader(parentLoader, domain);
      }
    });
  }
  
  public Object getDynamicSecurityDomain(Object securityDomain)
  {
    ProtectionDomain staticDomain = (ProtectionDomain)securityDomain;
    return getDynamicDomain(staticDomain);
  }
  
  private ProtectionDomain getDynamicDomain(ProtectionDomain staticDomain) {
    ContextPermissions p = new ContextPermissions(staticDomain);
    ProtectionDomain contextDomain = new ProtectionDomain(null, p);
    return contextDomain;
  }
  


  public Object callWithDomain(Object securityDomain, final Context cx, final Callable callable, final Scriptable scope, final Scriptable thisObj, final Object[] args)
  {
    ProtectionDomain staticDomain = (ProtectionDomain)securityDomain;
    














    ProtectionDomain dynamicDomain = getDynamicDomain(staticDomain);
    ProtectionDomain[] tmp = { dynamicDomain };
    AccessControlContext restricted = new AccessControlContext(tmp);
    
    PrivilegedAction<Object> action = new PrivilegedAction() {
      public Object run() {
        return callable.call(cx, scope, thisObj, args);
      }
      
    };
    return AccessController.doPrivileged(action, restricted);
  }
}
