package org.seleniumhq.jetty9.security;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.security.authentication.DeferredAuthentication;
import org.seleniumhq.jetty9.server.Authentication;
import org.seleniumhq.jetty9.server.Authentication.Deferred;
import org.seleniumhq.jetty9.server.Authentication.ResponseSent;
import org.seleniumhq.jetty9.server.Authentication.User;
import org.seleniumhq.jetty9.server.Authentication.Wrapped;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Response;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ContextHandler.Context;
import org.seleniumhq.jetty9.server.handler.HandlerWrapper;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;



























public abstract class SecurityHandler
  extends HandlerWrapper
  implements Authenticator.AuthConfiguration
{
  private static final Logger LOG = Log.getLogger(SecurityHandler.class);
  

  private boolean _checkWelcomeFiles = false;
  private Authenticator _authenticator;
  private Authenticator.Factory _authenticatorFactory = new DefaultAuthenticatorFactory();
  private String _realmName;
  private String _authMethod;
  private final Map<String, String> _initParameters = new HashMap();
  private LoginService _loginService;
  private IdentityService _identityService;
  private boolean _renewSession = true;
  

  protected SecurityHandler()
  {
    addBean(_authenticatorFactory);
  }
  





  public IdentityService getIdentityService()
  {
    return _identityService;
  }
  




  public void setIdentityService(IdentityService identityService)
  {
    if (isStarted())
      throw new IllegalStateException("Started");
    updateBean(_identityService, identityService);
    _identityService = identityService;
  }
  





  public LoginService getLoginService()
  {
    return _loginService;
  }
  




  public void setLoginService(LoginService loginService)
  {
    if (isStarted())
      throw new IllegalStateException("Started");
    updateBean(_loginService, loginService);
    _loginService = loginService;
  }
  


  public Authenticator getAuthenticator()
  {
    return _authenticator;
  }
  






  public void setAuthenticator(Authenticator authenticator)
  {
    if (isStarted())
      throw new IllegalStateException("Started");
    updateBean(_authenticator, authenticator);
    _authenticator = authenticator;
    if (_authenticator != null) {
      _authMethod = _authenticator.getAuthMethod();
    }
  }
  



  public Authenticator.Factory getAuthenticatorFactory()
  {
    return _authenticatorFactory;
  }
  





  public void setAuthenticatorFactory(Authenticator.Factory authenticatorFactory)
  {
    if (isRunning())
      throw new IllegalStateException("running");
    updateBean(_authenticatorFactory, authenticatorFactory);
    _authenticatorFactory = authenticatorFactory;
  }
  





  public String getRealmName()
  {
    return _realmName;
  }
  





  public void setRealmName(String realmName)
  {
    if (isRunning())
      throw new IllegalStateException("running");
    _realmName = realmName;
  }
  





  public String getAuthMethod()
  {
    return _authMethod;
  }
  





  public void setAuthMethod(String authMethod)
  {
    if (isRunning())
      throw new IllegalStateException("running");
    _authMethod = authMethod;
  }
  




  public boolean isCheckWelcomeFiles()
  {
    return _checkWelcomeFiles;
  }
  






  public void setCheckWelcomeFiles(boolean authenticateWelcomeFiles)
  {
    if (isRunning())
      throw new IllegalStateException("running");
    _checkWelcomeFiles = authenticateWelcomeFiles;
  }
  


  public String getInitParameter(String key)
  {
    return (String)_initParameters.get(key);
  }
  


  public Set<String> getInitParameterNames()
  {
    return _initParameters.keySet();
  }
  







  public String setInitParameter(String key, String value)
  {
    if (isRunning())
      throw new IllegalStateException("running");
    return (String)_initParameters.put(key, value);
  }
  
  protected LoginService findLoginService()
    throws Exception
  {
    Collection<LoginService> list = getServer().getBeans(LoginService.class);
    LoginService service = null;
    String realm = getRealmName();
    if (realm != null)
    {
      for (LoginService s : list) {
        if ((s.getName() != null) && (s.getName().equals(realm)))
        {
          service = s;
          break;
        }
      }
    } else if (list.size() == 1) {
      service = (LoginService)list.iterator().next();
    }
    return service;
  }
  

  protected IdentityService findIdentityService()
  {
    return (IdentityService)getServer().getBean(IdentityService.class);
  }
  





  protected void doStart()
    throws Exception
  {
    ContextHandler.Context context = ContextHandler.getCurrentContext();
    if (context != null)
    {
      Enumeration<String> names = context.getInitParameterNames();
      while ((names != null) && (names.hasMoreElements()))
      {
        String name = (String)names.nextElement();
        if ((name.startsWith("org.seleniumhq.jetty9.security.")) && 
          (getInitParameter(name) == null)) {
          setInitParameter(name, context.getInitParameter(name));
        }
      }
    }
    


    if (_loginService == null)
    {
      setLoginService(findLoginService());
      if (_loginService != null) {
        unmanage(_loginService);
      }
    }
    if (_identityService == null)
    {
      if (_loginService != null) {
        setIdentityService(_loginService.getIdentityService());
      }
      if (_identityService == null) {
        setIdentityService(findIdentityService());
      }
      if (_identityService == null)
      {
        if (_realmName != null)
        {
          setIdentityService(new DefaultIdentityService());
          manage(_identityService);
        }
      }
      else {
        unmanage(_identityService);
      }
    }
    if (_loginService != null)
    {
      if (_loginService.getIdentityService() == null) {
        _loginService.setIdentityService(_identityService);
      } else if (_loginService.getIdentityService() != _identityService) {
        throw new IllegalStateException("LoginService has different IdentityService to " + this);
      }
    }
    Authenticator.Factory authenticatorFactory = getAuthenticatorFactory();
    if ((_authenticator == null) && (authenticatorFactory != null) && (_identityService != null)) {
      setAuthenticator(authenticatorFactory.getAuthenticator(getServer(), ContextHandler.getCurrentContext(), this, _identityService, _loginService));
    }
    if (_authenticator != null) {
      _authenticator.setConfiguration(this);
    } else if (_realmName != null)
    {
      LOG.warn("No Authenticator for " + this, new Object[0]);
      throw new IllegalStateException("No Authenticator");
    }
    
    super.doStart();
  }
  


  protected void doStop()
    throws Exception
  {
    if (!isManaged(_identityService))
    {
      removeBean(_identityService);
      _identityService = null;
    }
    
    if (!isManaged(_loginService))
    {
      removeBean(_loginService);
      _loginService = null;
    }
    
    super.doStop();
  }
  

  protected boolean checkSecurity(Request request)
  {
    switch (3.$SwitchMap$javax$servlet$DispatcherType[request.getDispatcherType().ordinal()])
    {
    case 1: 
    case 2: 
      return true;
    case 3: 
      if ((isCheckWelcomeFiles()) && (request.getAttribute("org.seleniumhq.jetty9.server.welcome") != null))
      {
        request.removeAttribute("org.seleniumhq.jetty9.server.welcome");
        return true;
      }
      return false;
    }
    return false;
  }
  






  public boolean isSessionRenewedOnAuthentication()
  {
    return _renewSession;
  }
  







  public void setSessionRenewedOnAuthentication(boolean renew)
  {
    _renewSession = renew;
  }
  






  public void handle(String pathInContext, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    Response base_response = baseRequest.getResponse();
    Handler handler = getHandler();
    
    if (handler == null) {
      return;
    }
    Authenticator authenticator = _authenticator;
    
    if (checkSecurity(baseRequest))
    {

      if (authenticator != null) {
        authenticator.prepareRequest(baseRequest);
      }
      RoleInfo roleInfo = prepareConstraintInfo(pathInContext, baseRequest);
      

      if (!checkUserDataPermissions(pathInContext, baseRequest, base_response, roleInfo))
      {
        if (!baseRequest.isHandled())
        {
          response.sendError(403);
          baseRequest.setHandled(true);
        }
        return;
      }
      


      boolean isAuthMandatory = isAuthMandatory(baseRequest, base_response, roleInfo);
      
      if ((isAuthMandatory) && (authenticator == null))
      {
        LOG.warn("No authenticator for: " + roleInfo, new Object[0]);
        if (!baseRequest.isHandled())
        {
          response.sendError(403);
          baseRequest.setHandled(true);
        }
        return;
      }
      

      Object previousIdentity = null;
      try
      {
        Authentication authentication = baseRequest.getAuthentication();
        if ((authentication == null) || (authentication == Authentication.NOT_CHECKED)) {
          authentication = authenticator == null ? Authentication.UNAUTHENTICATED : authenticator.validateRequest(request, response, isAuthMandatory);
        }
        if ((authentication instanceof Authentication.Wrapped))
        {
          request = ((Authentication.Wrapped)authentication).getHttpServletRequest();
          response = ((Authentication.Wrapped)authentication).getHttpServletResponse();
        }
        
        if ((authentication instanceof Authentication.ResponseSent))
        {
          baseRequest.setHandled(true);
        }
        else if ((authentication instanceof Authentication.User))
        {
          Authentication.User userAuth = (Authentication.User)authentication;
          baseRequest.setAuthentication(authentication);
          if (_identityService != null) {
            previousIdentity = _identityService.associate(userAuth.getUserIdentity());
          }
          if (isAuthMandatory)
          {
            boolean authorized = checkWebResourcePermissions(pathInContext, baseRequest, base_response, roleInfo, userAuth.getUserIdentity());
            if (!authorized)
            {
              response.sendError(403, "!role");
              baseRequest.setHandled(true);
              return;
            }
          }
          
          handler.handle(pathInContext, baseRequest, request, response);
          if (authenticator != null) {
            authenticator.secureResponse(request, response, isAuthMandatory, userAuth);
          }
        } else if ((authentication instanceof Authentication.Deferred))
        {
          DeferredAuthentication deferred = (DeferredAuthentication)authentication;
          baseRequest.setAuthentication(authentication);
          
          try
          {
            handler.handle(pathInContext, baseRequest, request, response);
          }
          finally
          {
            previousIdentity = deferred.getPreviousAssociation();
          }
          
          if (authenticator != null)
          {
            Authentication auth = baseRequest.getAuthentication();
            if ((auth instanceof Authentication.User))
            {
              Authentication.User userAuth = (Authentication.User)auth;
              authenticator.secureResponse(request, response, isAuthMandatory, userAuth);
            }
            else {
              authenticator.secureResponse(request, response, isAuthMandatory, null);
            }
          }
        }
        else {
          baseRequest.setAuthentication(authentication);
          if (_identityService != null)
            previousIdentity = _identityService.associate(null);
          handler.handle(pathInContext, baseRequest, request, response);
          if (authenticator != null) {
            authenticator.secureResponse(request, response, isAuthMandatory, null);
          }
          
        }
      }
      catch (ServerAuthException e)
      {
        response.sendError(500, e.getMessage());
      }
      finally
      {
        if (_identityService != null) {
          _identityService.disassociate(previousIdentity);
        }
      }
    } else {
      handler.handle(pathInContext, baseRequest, request, response);
    }
  }
  

  public static SecurityHandler getCurrentSecurityHandler()
  {
    ContextHandler.Context context = ContextHandler.getCurrentContext();
    if (context == null) {
      return null;
    }
    return (SecurityHandler)context.getContextHandler().getChildHandlerByClass(SecurityHandler.class);
  }
  

  public void logout(Authentication.User user)
  {
    LOG.debug("logout {}", new Object[] { user });
    LoginService login_service = getLoginService();
    if (login_service != null)
    {
      login_service.logout(user.getUserIdentity());
    }
    
    IdentityService identity_service = getIdentityService();
    if (identity_service != null)
    {

      Object previous = null;
      identity_service.disassociate(previous);
    }
  }
  

  protected abstract RoleInfo prepareConstraintInfo(String paramString, Request paramRequest);
  

  protected abstract boolean checkUserDataPermissions(String paramString, Request paramRequest, Response paramResponse, RoleInfo paramRoleInfo)
    throws IOException;
  

  protected abstract boolean isAuthMandatory(Request paramRequest, Response paramResponse, Object paramObject);
  

  protected abstract boolean checkWebResourcePermissions(String paramString, Request paramRequest, Response paramResponse, Object paramObject, UserIdentity paramUserIdentity)
    throws IOException;
  
  public class NotChecked
    implements Principal
  {
    public NotChecked() {}
    
    public String getName()
    {
      return null;
    }
    

    public String toString()
    {
      return "NOT CHECKED";
    }
    
    public SecurityHandler getSecurityHandler()
    {
      return SecurityHandler.this;
    }
  }
  



  public static final Principal __NO_USER = new Principal()
  {

    public String getName()
    {
      return null;
    }
    

    public String toString()
    {
      return "No User";
    }
  };
  










  public static final Principal __NOBODY = new Principal()
  {

    public String getName()
    {
      return "Nobody";
    }
    

    public String toString()
    {
      return getName();
    }
  };
}
