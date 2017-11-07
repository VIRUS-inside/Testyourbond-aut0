package org.seleniumhq.jetty9.security.authentication;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.seleniumhq.jetty9.security.Authenticator;
import org.seleniumhq.jetty9.security.Authenticator.AuthConfiguration;
import org.seleniumhq.jetty9.security.IdentityService;
import org.seleniumhq.jetty9.security.LoginService;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Response;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.server.session.Session;
import org.seleniumhq.jetty9.server.session.SessionHandler;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

















public abstract class LoginAuthenticator
  implements Authenticator
{
  private static final Logger LOG = Log.getLogger(LoginAuthenticator.class);
  

  protected LoginService _loginService;
  

  protected IdentityService _identityService;
  

  private boolean _renewSession;
  


  protected LoginAuthenticator() {}
  


  public void prepareRequest(ServletRequest request) {}
  


  public UserIdentity login(String username, Object password, ServletRequest request)
  {
    UserIdentity user = _loginService.login(username, password, request);
    if (user != null)
    {
      renewSession((HttpServletRequest)request, (request instanceof Request) ? ((Request)request).getResponse() : null);
      return user;
    }
    return null;
  }
  


  public void setConfiguration(Authenticator.AuthConfiguration configuration)
  {
    _loginService = configuration.getLoginService();
    if (_loginService == null)
      throw new IllegalStateException("No LoginService for " + this + " in " + configuration);
    _identityService = configuration.getIdentityService();
    if (_identityService == null)
      throw new IllegalStateException("No IdentityService for " + this + " in " + configuration);
    _renewSession = configuration.isSessionRenewedOnAuthentication();
  }
  


  public LoginService getLoginService()
  {
    return _loginService;
  }
  












  protected HttpSession renewSession(HttpServletRequest request, HttpServletResponse response)
  {
    HttpSession httpSession = request.getSession(false);
    
    if ((_renewSession) && (httpSession != null))
    {
      synchronized (httpSession)
      {


        if (httpSession.getAttribute("org.seleniumhq.jetty9.security.sessionCreatedSecure") != Boolean.TRUE)
        {
          if ((httpSession instanceof Session))
          {
            Session s = (Session)httpSession;
            String oldId = s.getId();
            s.renewId(request);
            s.setAttribute("org.seleniumhq.jetty9.security.sessionCreatedSecure", Boolean.TRUE);
            if ((s.isIdChanged()) && (response != null) && ((response instanceof Response)))
              ((Response)response).addCookie(s.getSessionHandler().getSessionCookie(s, request.getContextPath(), request.isSecure()));
            LOG.debug("renew {}->{}", new Object[] { oldId, s.getId() });
          }
          else {
            LOG.warn("Unable to renew session " + httpSession, new Object[0]);
          }
          return httpSession;
        }
      }
    }
    return httpSession;
  }
}
