package org.seleniumhq.jetty9.security.authentication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.Principal;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import org.seleniumhq.jetty9.security.AbstractUserAuthentication;
import org.seleniumhq.jetty9.security.LoginService;
import org.seleniumhq.jetty9.security.SecurityHandler;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;




















public class SessionAuthentication
  extends AbstractUserAuthentication
  implements Serializable, HttpSessionActivationListener, HttpSessionBindingListener
{
  private static final Logger LOG = Log.getLogger(SessionAuthentication.class);
  
  private static final long serialVersionUID = -4643200685888258706L;
  
  public static final String __J_AUTHENTICATED = "org.seleniumhq.jetty9.security.UserIdentity";
  
  private final String _name;
  
  private final Object _credentials;
  
  private transient HttpSession _session;
  
  public SessionAuthentication(String method, UserIdentity userIdentity, Object credentials)
  {
    super(method, userIdentity);
    _name = userIdentity.getUserPrincipal().getName();
    _credentials = credentials;
  }
  

  private void readObject(ObjectInputStream stream)
    throws IOException, ClassNotFoundException
  {
    stream.defaultReadObject();
    
    SecurityHandler security = SecurityHandler.getCurrentSecurityHandler();
    if (security == null)
      throw new IllegalStateException("!SecurityHandler");
    LoginService login_service = security.getLoginService();
    if (login_service == null) {
      throw new IllegalStateException("!LoginService");
    }
    _userIdentity = login_service.login(_name, _credentials, null);
    LOG.debug("Deserialized and relogged in {}", new Object[] { this });
  }
  
  public void logout()
  {
    if ((_session != null) && (_session.getAttribute("org.seleniumhq.jetty9.security.UserIdentity") != null)) {
      _session.removeAttribute("org.seleniumhq.jetty9.security.UserIdentity");
    }
    doLogout();
  }
  
  private void doLogout()
  {
    SecurityHandler security = SecurityHandler.getCurrentSecurityHandler();
    if (security != null)
      security.logout(this);
    if (_session != null) {
      _session.removeAttribute("org.seleniumhq.jetty9.security.sessionCreatedSecure");
    }
  }
  
  public String toString()
  {
    return String.format("%s@%x{%s,%s}", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()), _session == null ? "-" : _session.getId(), _userIdentity });
  }
  



  public void sessionWillPassivate(HttpSessionEvent se) {}
  


  public void sessionDidActivate(HttpSessionEvent se)
  {
    if (_session == null)
    {
      _session = se.getSession();
    }
  }
  

  public void valueBound(HttpSessionBindingEvent event)
  {
    if (_session == null)
    {
      _session = event.getSession();
    }
  }
  

  public void valueUnbound(HttpSessionBindingEvent event)
  {
    doLogout();
  }
}
