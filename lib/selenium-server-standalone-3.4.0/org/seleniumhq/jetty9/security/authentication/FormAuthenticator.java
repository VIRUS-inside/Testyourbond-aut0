package org.seleniumhq.jetty9.security.authentication;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpHeaderValue;
import org.seleniumhq.jetty9.http.HttpMethod;
import org.seleniumhq.jetty9.http.HttpVersion;
import org.seleniumhq.jetty9.http.MimeTypes.Type;
import org.seleniumhq.jetty9.security.Authenticator.AuthConfiguration;
import org.seleniumhq.jetty9.security.LoginService;
import org.seleniumhq.jetty9.security.ServerAuthException;
import org.seleniumhq.jetty9.security.UserAuthentication;
import org.seleniumhq.jetty9.server.Authentication;
import org.seleniumhq.jetty9.server.Authentication.ResponseSent;
import org.seleniumhq.jetty9.server.Authentication.User;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Response;
import org.seleniumhq.jetty9.server.UserIdentity;
import org.seleniumhq.jetty9.util.MultiMap;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.URIUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;































public class FormAuthenticator
  extends LoginAuthenticator
{
  private static final Logger LOG = Log.getLogger(FormAuthenticator.class);
  
  public static final String __FORM_LOGIN_PAGE = "org.seleniumhq.jetty9.security.form_login_page";
  
  public static final String __FORM_ERROR_PAGE = "org.seleniumhq.jetty9.security.form_error_page";
  
  public static final String __FORM_DISPATCH = "org.seleniumhq.jetty9.security.dispatch";
  
  public static final String __J_URI = "org.seleniumhq.jetty9.security.form_URI";
  
  public static final String __J_POST = "org.seleniumhq.jetty9.security.form_POST";
  public static final String __J_METHOD = "org.seleniumhq.jetty9.security.form_METHOD";
  public static final String __J_SECURITY_CHECK = "/j_security_check";
  public static final String __J_USERNAME = "j_username";
  public static final String __J_PASSWORD = "j_password";
  private String _formErrorPage;
  private String _formErrorPath;
  private String _formLoginPage;
  private String _formLoginPath;
  private boolean _dispatch;
  private boolean _alwaysSaveUri;
  
  public FormAuthenticator() {}
  
  public FormAuthenticator(String login, String error, boolean dispatch)
  {
    this();
    if (login != null)
      setLoginPage(login);
    if (error != null)
      setErrorPage(error);
    _dispatch = dispatch;
  }
  








  public void setAlwaysSaveUri(boolean alwaysSave)
  {
    _alwaysSaveUri = alwaysSave;
  }
  


  public boolean getAlwaysSaveUri()
  {
    return _alwaysSaveUri;
  }
  





  public void setConfiguration(Authenticator.AuthConfiguration configuration)
  {
    super.setConfiguration(configuration);
    String login = configuration.getInitParameter("org.seleniumhq.jetty9.security.form_login_page");
    if (login != null)
      setLoginPage(login);
    String error = configuration.getInitParameter("org.seleniumhq.jetty9.security.form_error_page");
    if (error != null)
      setErrorPage(error);
    String dispatch = configuration.getInitParameter("org.seleniumhq.jetty9.security.dispatch");
    _dispatch = (dispatch == null ? _dispatch : Boolean.valueOf(dispatch).booleanValue());
  }
  


  public String getAuthMethod()
  {
    return "FORM";
  }
  

  private void setLoginPage(String path)
  {
    if (!path.startsWith("/"))
    {
      LOG.warn("form-login-page must start with /", new Object[0]);
      path = "/" + path;
    }
    _formLoginPage = path;
    _formLoginPath = path;
    if (_formLoginPath.indexOf('?') > 0) {
      _formLoginPath = _formLoginPath.substring(0, _formLoginPath.indexOf('?'));
    }
  }
  
  private void setErrorPage(String path)
  {
    if ((path == null) || (path.trim().length() == 0))
    {
      _formErrorPath = null;
      _formErrorPage = null;
    }
    else
    {
      if (!path.startsWith("/"))
      {
        LOG.warn("form-error-page must start with /", new Object[0]);
        path = "/" + path;
      }
      _formErrorPage = path;
      _formErrorPath = path;
      
      if (_formErrorPath.indexOf('?') > 0) {
        _formErrorPath = _formErrorPath.substring(0, _formErrorPath.indexOf('?'));
      }
    }
  }
  



  public UserIdentity login(String username, Object password, ServletRequest request)
  {
    UserIdentity user = super.login(username, password, request);
    if (user != null)
    {
      HttpSession session = ((HttpServletRequest)request).getSession(true);
      Authentication cached = new SessionAuthentication(getAuthMethod(), user, password);
      session.setAttribute("org.seleniumhq.jetty9.security.UserIdentity", cached);
    }
    return user;
  }
  










  public void prepareRequest(ServletRequest request)
  {
    HttpServletRequest httpRequest = (HttpServletRequest)request;
    HttpSession session = httpRequest.getSession(false);
    if ((session == null) || (session.getAttribute("org.seleniumhq.jetty9.security.UserIdentity") == null)) {
      return;
    }
    String juri = (String)session.getAttribute("org.seleniumhq.jetty9.security.form_URI");
    if ((juri == null) || (juri.length() == 0)) {
      return;
    }
    String method = (String)session.getAttribute("org.seleniumhq.jetty9.security.form_METHOD");
    if ((method == null) || (method.length() == 0)) {
      return;
    }
    StringBuffer buf = httpRequest.getRequestURL();
    if (httpRequest.getQueryString() != null) {
      buf.append("?").append(httpRequest.getQueryString());
    }
    if (!juri.equals(buf.toString())) {
      return;
    }
    
    if (LOG.isDebugEnabled()) LOG.debug("Restoring original method {} for {} with method {}", new Object[] { method, juri, httpRequest.getMethod() });
    Request base_request = Request.getBaseRequest(request);
    base_request.setMethod(method);
  }
  

  public Authentication validateRequest(ServletRequest req, ServletResponse res, boolean mandatory)
    throws ServerAuthException
  {
    HttpServletRequest request = (HttpServletRequest)req;
    HttpServletResponse response = (HttpServletResponse)res;
    Request base_request = Request.getBaseRequest(request);
    Response base_response = base_request.getResponse();
    
    String uri = request.getRequestURI();
    if (uri == null) {
      uri = "/";
    }
    mandatory |= isJSecurityCheck(uri);
    if (!mandatory) {
      return new DeferredAuthentication(this);
    }
    if ((isLoginOrErrorPage(URIUtil.addPaths(request.getServletPath(), request.getPathInfo()))) && (!DeferredAuthentication.isDeferred(response))) {
      return new DeferredAuthentication(this);
    }
    HttpSession session = null;
    try
    {
      session = request.getSession(true);
    }
    catch (Exception e)
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug(e);
      }
    }
    

    if (session == null) {
      return Authentication.UNAUTHENTICATED;
    }
    
    try
    {
      if (isJSecurityCheck(uri))
      {
        String username = request.getParameter("j_username");
        String password = request.getParameter("j_password");
        
        UserIdentity user = login(username, password, request);
        LOG.debug("jsecuritycheck {} {}", new Object[] { username, user });
        session = request.getSession(true);
        if (user != null)
        {
          FormAuthentication form_auth;
          

          synchronized (session)
          {
            String nuri = (String)session.getAttribute("org.seleniumhq.jetty9.security.form_URI");
            
            if ((nuri == null) || (nuri.length() == 0))
            {
              nuri = request.getContextPath();
              if (nuri.length() == 0)
                nuri = "/";
            }
            form_auth = new FormAuthentication(getAuthMethod(), user); }
          FormAuthentication form_auth;
          String nuri; LOG.debug("authenticated {}->{}", new Object[] { form_auth, nuri });
          
          response.setContentLength(0);
          int redirectCode = base_request.getHttpVersion().getVersion() < HttpVersion.HTTP_1_1.getVersion() ? 302 : 303;
          base_response.sendRedirect(redirectCode, response.encodeRedirectURL(nuri));
          return form_auth;
        }
        

        if (LOG.isDebugEnabled())
          LOG.debug("Form authentication FAILED for " + StringUtil.printable(username), new Object[0]);
        if (_formErrorPage == null)
        {
          LOG.debug("auth failed {}->403", new Object[] { username });
          if (response != null) {
            response.sendError(403);
          }
        } else if (_dispatch)
        {
          LOG.debug("auth failed {}=={}", new Object[] { username, _formErrorPage });
          RequestDispatcher dispatcher = request.getRequestDispatcher(_formErrorPage);
          response.setHeader(HttpHeader.CACHE_CONTROL.asString(), HttpHeaderValue.NO_CACHE.asString());
          response.setDateHeader(HttpHeader.EXPIRES.asString(), 1L);
          dispatcher.forward(new FormRequest(request), new FormResponse(response));
        }
        else
        {
          LOG.debug("auth failed {}->{}", new Object[] { username, _formErrorPage });
          int redirectCode = base_request.getHttpVersion().getVersion() < HttpVersion.HTTP_1_1.getVersion() ? 302 : 303;
          base_response.sendRedirect(redirectCode, response.encodeRedirectURL(URIUtil.addPaths(request.getContextPath(), _formErrorPage)));
        }
        
        return Authentication.SEND_FAILURE;
      }
      

      Authentication authentication = (Authentication)session.getAttribute("org.seleniumhq.jetty9.security.UserIdentity");
      if (authentication != null)
      {

        if (((authentication instanceof Authentication.User)) && (_loginService != null))
        {
          if (!_loginService.validate(((Authentication.User)authentication).getUserIdentity()))
          {
            LOG.debug("auth revoked {}", new Object[] { authentication });
            session.removeAttribute("org.seleniumhq.jetty9.security.UserIdentity");
            break label991;
          }
        }
        synchronized (session)
        {
          String j_uri = (String)session.getAttribute("org.seleniumhq.jetty9.security.form_URI");
          if (j_uri != null)
          {


            LOG.debug("auth retry {}->{}", new Object[] { authentication, j_uri });
            StringBuffer buf = request.getRequestURL();
            if (request.getQueryString() != null) {
              buf.append("?").append(request.getQueryString());
            }
            if (j_uri.equals(buf.toString()))
            {
              MultiMap<String> j_post = (MultiMap)session.getAttribute("org.seleniumhq.jetty9.security.form_POST");
              if (j_post != null)
              {
                LOG.debug("auth rePOST {}->{}", new Object[] { authentication, j_uri });
                base_request.setContentParameters(j_post);
              }
              session.removeAttribute("org.seleniumhq.jetty9.security.form_URI");
              session.removeAttribute("org.seleniumhq.jetty9.security.form_METHOD");
              session.removeAttribute("org.seleniumhq.jetty9.security.form_POST");
            }
          }
        }
        LOG.debug("auth {}", new Object[] { authentication });
        return authentication;
      }
      
      label991:
      
      if (DeferredAuthentication.isDeferred(response))
      {
        LOG.debug("auth deferred {}", new Object[] { session.getId() });
        return Authentication.UNAUTHENTICATED;
      }
      

      synchronized (session)
      {

        if ((session.getAttribute("org.seleniumhq.jetty9.security.form_URI") == null) || (_alwaysSaveUri))
        {
          StringBuffer buf = request.getRequestURL();
          if (request.getQueryString() != null)
            buf.append("?").append(request.getQueryString());
          session.setAttribute("org.seleniumhq.jetty9.security.form_URI", buf.toString());
          session.setAttribute("org.seleniumhq.jetty9.security.form_METHOD", request.getMethod());
          
          if ((MimeTypes.Type.FORM_ENCODED.is(req.getContentType())) && (HttpMethod.POST.is(request.getMethod())))
          {
            MultiMap<String> formParameters = new MultiMap();
            base_request.extractFormParameters(formParameters);
            session.setAttribute("org.seleniumhq.jetty9.security.form_POST", formParameters);
          }
        }
      }
      

      if (_dispatch)
      {
        LOG.debug("challenge {}=={}", new Object[] { session.getId(), _formLoginPage });
        RequestDispatcher dispatcher = request.getRequestDispatcher(_formLoginPage);
        response.setHeader(HttpHeader.CACHE_CONTROL.asString(), HttpHeaderValue.NO_CACHE.asString());
        response.setDateHeader(HttpHeader.EXPIRES.asString(), 1L);
        dispatcher.forward(new FormRequest(request), new FormResponse(response));
      }
      else
      {
        LOG.debug("challenge {}->{}", new Object[] { session.getId(), _formLoginPage });
        int redirectCode = base_request.getHttpVersion().getVersion() < HttpVersion.HTTP_1_1.getVersion() ? 302 : 303;
        base_response.sendRedirect(redirectCode, response.encodeRedirectURL(URIUtil.addPaths(request.getContextPath(), _formLoginPage)));
      }
      return Authentication.SEND_CONTINUE;
    }
    catch (IOException|ServletException e)
    {
      throw new ServerAuthException(e);
    }
  }
  

  public boolean isJSecurityCheck(String uri)
  {
    int jsc = uri.indexOf("/j_security_check");
    
    if (jsc < 0)
      return false;
    int e = jsc + "/j_security_check".length();
    if (e == uri.length())
      return true;
    char c = uri.charAt(e);
    return (c == ';') || (c == '#') || (c == '/') || (c == '?');
  }
  

  public boolean isLoginOrErrorPage(String pathInContext)
  {
    return (pathInContext != null) && ((pathInContext.equals(_formErrorPath)) || (pathInContext.equals(_formLoginPath)));
  }
  

  public boolean secureResponse(ServletRequest req, ServletResponse res, boolean mandatory, Authentication.User validatedUser)
    throws ServerAuthException
  {
    return true;
  }
  

  protected static class FormRequest
    extends HttpServletRequestWrapper
  {
    public FormRequest(HttpServletRequest request)
    {
      super();
    }
    

    public long getDateHeader(String name)
    {
      if (name.toLowerCase(Locale.ENGLISH).startsWith("if-"))
        return -1L;
      return super.getDateHeader(name);
    }
    

    public String getHeader(String name)
    {
      if (name.toLowerCase(Locale.ENGLISH).startsWith("if-"))
        return null;
      return super.getHeader(name);
    }
    

    public Enumeration<String> getHeaderNames()
    {
      return Collections.enumeration(Collections.list(super.getHeaderNames()));
    }
    

    public Enumeration<String> getHeaders(String name)
    {
      if (name.toLowerCase(Locale.ENGLISH).startsWith("if-"))
        return Collections.enumeration(Collections.emptyList());
      return super.getHeaders(name);
    }
  }
  

  protected static class FormResponse
    extends HttpServletResponseWrapper
  {
    public FormResponse(HttpServletResponse response)
    {
      super();
    }
    

    public void addDateHeader(String name, long date)
    {
      if (notIgnored(name)) {
        super.addDateHeader(name, date);
      }
    }
    
    public void addHeader(String name, String value)
    {
      if (notIgnored(name)) {
        super.addHeader(name, value);
      }
    }
    
    public void setDateHeader(String name, long date)
    {
      if (notIgnored(name)) {
        super.setDateHeader(name, date);
      }
    }
    
    public void setHeader(String name, String value)
    {
      if (notIgnored(name)) {
        super.setHeader(name, value);
      }
    }
    
    private boolean notIgnored(String name) {
      if ((HttpHeader.CACHE_CONTROL.is(name)) || 
        (HttpHeader.PRAGMA.is(name)) || 
        (HttpHeader.ETAG.is(name)) || 
        (HttpHeader.EXPIRES.is(name)) || 
        (HttpHeader.LAST_MODIFIED.is(name)) || 
        (HttpHeader.AGE.is(name)))
        return false;
      return true;
    }
  }
  



  public static class FormAuthentication
    extends UserAuthentication
    implements Authentication.ResponseSent
  {
    public FormAuthentication(String method, UserIdentity userIdentity)
    {
      super(userIdentity);
    }
    

    public String toString()
    {
      return "Form" + super.toString();
    }
  }
}
