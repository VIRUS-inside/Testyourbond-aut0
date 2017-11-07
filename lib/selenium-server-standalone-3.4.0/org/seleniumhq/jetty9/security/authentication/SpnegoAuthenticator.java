package org.seleniumhq.jetty9.security.authentication;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.security.ServerAuthException;
import org.seleniumhq.jetty9.security.UserAuthentication;
import org.seleniumhq.jetty9.server.Authentication;
import org.seleniumhq.jetty9.server.Authentication.User;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;





















public class SpnegoAuthenticator
  extends LoginAuthenticator
{
  private static final Logger LOG = Log.getLogger(SpnegoAuthenticator.class);
  private String _authMethod = "SPNEGO";
  



  public SpnegoAuthenticator() {}
  



  public SpnegoAuthenticator(String authMethod)
  {
    _authMethod = authMethod;
  }
  

  public String getAuthMethod()
  {
    return _authMethod;
  }
  
  public Authentication validateRequest(ServletRequest request, ServletResponse response, boolean mandatory)
    throws ServerAuthException
  {
    HttpServletRequest req = (HttpServletRequest)request;
    HttpServletResponse res = (HttpServletResponse)response;
    
    String header = req.getHeader(HttpHeader.AUTHORIZATION.asString());
    
    if (!mandatory)
    {
      return new DeferredAuthentication(this);
    }
    

    if (header == null)
    {
      try
      {
        if (DeferredAuthentication.isDeferred(res))
        {
          return Authentication.UNAUTHENTICATED;
        }
        
        LOG.debug("SpengoAuthenticator: sending challenge", new Object[0]);
        res.setHeader(HttpHeader.WWW_AUTHENTICATE.asString(), HttpHeader.NEGOTIATE.asString());
        res.sendError(401);
        return Authentication.SEND_CONTINUE;
      }
      catch (IOException ioe)
      {
        throw new ServerAuthException(ioe);
      }
    }
    if ((header != null) && (header.startsWith(HttpHeader.NEGOTIATE.asString())))
    {
      String spnegoToken = header.substring(10);
      
      UserIdentity user = login(null, spnegoToken, request);
      
      if (user != null)
      {
        return new UserAuthentication(getAuthMethod(), user);
      }
    }
    
    return Authentication.UNAUTHENTICATED;
  }
  
  public boolean secureResponse(ServletRequest request, ServletResponse response, boolean mandatory, Authentication.User validatedUser)
    throws ServerAuthException
  {
    return true;
  }
}
