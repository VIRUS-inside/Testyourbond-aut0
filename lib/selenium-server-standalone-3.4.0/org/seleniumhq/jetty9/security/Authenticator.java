package org.seleniumhq.jetty9.security;

import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.seleniumhq.jetty9.server.Authentication;
import org.seleniumhq.jetty9.server.Authentication.User;
import org.seleniumhq.jetty9.server.Server;

public abstract interface Authenticator
{
  public abstract void setConfiguration(AuthConfiguration paramAuthConfiguration);
  
  public abstract String getAuthMethod();
  
  public abstract void prepareRequest(ServletRequest paramServletRequest);
  
  public abstract Authentication validateRequest(ServletRequest paramServletRequest, ServletResponse paramServletResponse, boolean paramBoolean)
    throws ServerAuthException;
  
  public abstract boolean secureResponse(ServletRequest paramServletRequest, ServletResponse paramServletResponse, boolean paramBoolean, Authentication.User paramUser)
    throws ServerAuthException;
  
  public static abstract interface Factory
  {
    public abstract Authenticator getAuthenticator(Server paramServer, ServletContext paramServletContext, Authenticator.AuthConfiguration paramAuthConfiguration, IdentityService paramIdentityService, LoginService paramLoginService);
  }
  
  public static abstract interface AuthConfiguration
  {
    public abstract String getAuthMethod();
    
    public abstract String getRealmName();
    
    public abstract String getInitParameter(String paramString);
    
    public abstract Set<String> getInitParameterNames();
    
    public abstract LoginService getLoginService();
    
    public abstract IdentityService getIdentityService();
    
    public abstract boolean isSessionRenewedOnAuthentication();
  }
}
