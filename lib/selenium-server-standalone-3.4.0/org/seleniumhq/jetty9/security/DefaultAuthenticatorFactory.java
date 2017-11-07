package org.seleniumhq.jetty9.security;

import javax.servlet.ServletContext;
import org.seleniumhq.jetty9.security.authentication.BasicAuthenticator;
import org.seleniumhq.jetty9.security.authentication.ClientCertAuthenticator;
import org.seleniumhq.jetty9.security.authentication.DigestAuthenticator;
import org.seleniumhq.jetty9.security.authentication.FormAuthenticator;
import org.seleniumhq.jetty9.security.authentication.SpnegoAuthenticator;
import org.seleniumhq.jetty9.server.Server;








































public class DefaultAuthenticatorFactory
  implements Authenticator.Factory
{
  LoginService _loginService;
  
  public DefaultAuthenticatorFactory() {}
  
  public Authenticator getAuthenticator(Server server, ServletContext context, Authenticator.AuthConfiguration configuration, IdentityService identityService, LoginService loginService)
  {
    String auth = configuration.getAuthMethod();
    Authenticator authenticator = null;
    
    if ((auth == null) || ("BASIC".equalsIgnoreCase(auth))) {
      authenticator = new BasicAuthenticator();
    } else if ("DIGEST".equalsIgnoreCase(auth)) {
      authenticator = new DigestAuthenticator();
    } else if ("FORM".equalsIgnoreCase(auth)) {
      authenticator = new FormAuthenticator();
    } else if ("SPNEGO".equalsIgnoreCase(auth)) {
      authenticator = new SpnegoAuthenticator();
    } else if ("NEGOTIATE".equalsIgnoreCase(auth))
      authenticator = new SpnegoAuthenticator("NEGOTIATE");
    if (("CLIENT_CERT".equalsIgnoreCase(auth)) || ("CLIENT-CERT".equalsIgnoreCase(auth))) {
      authenticator = new ClientCertAuthenticator();
    }
    return authenticator;
  }
  




  public LoginService getLoginService()
  {
    return _loginService;
  }
  




  public void setLoginService(LoginService loginService)
  {
    _loginService = loginService;
  }
}
