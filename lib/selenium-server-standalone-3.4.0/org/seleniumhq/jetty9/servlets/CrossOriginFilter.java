package org.seleniumhq.jetty9.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;





































































































public class CrossOriginFilter
  implements Filter
{
  private static final Logger LOG = Log.getLogger(CrossOriginFilter.class);
  
  private static final String ORIGIN_HEADER = "Origin";
  
  public static final String ACCESS_CONTROL_REQUEST_METHOD_HEADER = "Access-Control-Request-Method";
  
  public static final String ACCESS_CONTROL_REQUEST_HEADERS_HEADER = "Access-Control-Request-Headers";
  
  public static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEADER = "Access-Control-Allow-Origin";
  public static final String ACCESS_CONTROL_ALLOW_METHODS_HEADER = "Access-Control-Allow-Methods";
  public static final String ACCESS_CONTROL_ALLOW_HEADERS_HEADER = "Access-Control-Allow-Headers";
  public static final String ACCESS_CONTROL_MAX_AGE_HEADER = "Access-Control-Max-Age";
  public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER = "Access-Control-Allow-Credentials";
  public static final String ACCESS_CONTROL_EXPOSE_HEADERS_HEADER = "Access-Control-Expose-Headers";
  public static final String TIMING_ALLOW_ORIGIN_HEADER = "Timing-Allow-Origin";
  public static final String ALLOWED_ORIGINS_PARAM = "allowedOrigins";
  public static final String ALLOWED_TIMING_ORIGINS_PARAM = "allowedTimingOrigins";
  public static final String ALLOWED_METHODS_PARAM = "allowedMethods";
  public static final String ALLOWED_HEADERS_PARAM = "allowedHeaders";
  public static final String PREFLIGHT_MAX_AGE_PARAM = "preflightMaxAge";
  public static final String ALLOW_CREDENTIALS_PARAM = "allowCredentials";
  public static final String EXPOSED_HEADERS_PARAM = "exposedHeaders";
  public static final String OLD_CHAIN_PREFLIGHT_PARAM = "forwardPreflight";
  public static final String CHAIN_PREFLIGHT_PARAM = "chainPreflight";
  private static final String ANY_ORIGIN = "*";
  private static final String DEFAULT_ALLOWED_ORIGINS = "*";
  private static final String DEFAULT_ALLOWED_TIMING_ORIGINS = "";
  private static final List<String> SIMPLE_HTTP_METHODS = Arrays.asList(new String[] { "GET", "POST", "HEAD" });
  private static final List<String> DEFAULT_ALLOWED_METHODS = Arrays.asList(new String[] { "GET", "POST", "HEAD" });
  private static final List<String> DEFAULT_ALLOWED_HEADERS = Arrays.asList(new String[] { "X-Requested-With", "Content-Type", "Accept", "Origin" });
  
  private boolean anyOriginAllowed;
  private boolean anyTimingOriginAllowed;
  private boolean anyHeadersAllowed;
  private List<String> allowedOrigins = new ArrayList();
  private List<String> allowedTimingOrigins = new ArrayList();
  private List<String> allowedMethods = new ArrayList();
  private List<String> allowedHeaders = new ArrayList();
  private List<String> exposedHeaders = new ArrayList();
  private int preflightMaxAge;
  private boolean allowCredentials;
  private boolean chainPreflight;
  
  public CrossOriginFilter() {}
  
  public void init(FilterConfig config) throws ServletException { String allowedOriginsConfig = config.getInitParameter("allowedOrigins");
    String allowedTimingOriginsConfig = config.getInitParameter("allowedTimingOrigins");
    
    anyOriginAllowed = generateAllowedOrigins(allowedOrigins, allowedOriginsConfig, "*");
    anyTimingOriginAllowed = generateAllowedOrigins(allowedTimingOrigins, allowedTimingOriginsConfig, "");
    
    String allowedMethodsConfig = config.getInitParameter("allowedMethods");
    if (allowedMethodsConfig == null) {
      allowedMethods.addAll(DEFAULT_ALLOWED_METHODS);
    } else {
      allowedMethods.addAll(Arrays.asList(StringUtil.csvSplit(allowedMethodsConfig)));
    }
    String allowedHeadersConfig = config.getInitParameter("allowedHeaders");
    if (allowedHeadersConfig == null) {
      allowedHeaders.addAll(DEFAULT_ALLOWED_HEADERS);
    } else if ("*".equals(allowedHeadersConfig)) {
      anyHeadersAllowed = true;
    } else {
      allowedHeaders.addAll(Arrays.asList(StringUtil.csvSplit(allowedHeadersConfig)));
    }
    String preflightMaxAgeConfig = config.getInitParameter("preflightMaxAge");
    if (preflightMaxAgeConfig == null) {
      preflightMaxAgeConfig = "1800";
    }
    try {
      preflightMaxAge = Integer.parseInt(preflightMaxAgeConfig);
    }
    catch (NumberFormatException x)
    {
      LOG.info("Cross-origin filter, could not parse '{}' parameter as integer: {}", new Object[] { "preflightMaxAge", preflightMaxAgeConfig });
    }
    
    String allowedCredentialsConfig = config.getInitParameter("allowCredentials");
    if (allowedCredentialsConfig == null)
      allowedCredentialsConfig = "true";
    allowCredentials = Boolean.parseBoolean(allowedCredentialsConfig);
    
    String exposedHeadersConfig = config.getInitParameter("exposedHeaders");
    if (exposedHeadersConfig == null)
      exposedHeadersConfig = "";
    exposedHeaders.addAll(Arrays.asList(StringUtil.csvSplit(exposedHeadersConfig)));
    
    String chainPreflightConfig = config.getInitParameter("forwardPreflight");
    if (chainPreflightConfig != null) {
      LOG.warn("DEPRECATED CONFIGURATION: Use chainPreflight instead of forwardPreflight", new Object[0]);
    } else
      chainPreflightConfig = config.getInitParameter("chainPreflight");
    if (chainPreflightConfig == null)
      chainPreflightConfig = "true";
    chainPreflight = Boolean.parseBoolean(chainPreflightConfig);
    
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Cross-origin filter configuration: allowedOrigins = " + allowedOriginsConfig + ", " + "allowedTimingOrigins" + " = " + allowedTimingOriginsConfig + ", " + "allowedMethods" + " = " + allowedMethodsConfig + ", " + "allowedHeaders" + " = " + allowedHeadersConfig + ", " + "preflightMaxAge" + " = " + preflightMaxAgeConfig + ", " + "allowCredentials" + " = " + allowedCredentialsConfig + "," + "exposedHeaders" + " = " + exposedHeadersConfig + "," + "chainPreflight" + " = " + chainPreflightConfig, new Object[0]);
    }
  }
  









  private boolean generateAllowedOrigins(List<String> allowedOriginStore, String allowedOriginsConfig, String defaultOrigin)
  {
    if (allowedOriginsConfig == null)
      allowedOriginsConfig = defaultOrigin;
    String[] allowedOrigins = StringUtil.csvSplit(allowedOriginsConfig);
    for (String allowedOrigin : allowedOrigins)
    {
      if (allowedOrigin.length() > 0)
      {
        if ("*".equals(allowedOrigin))
        {
          allowedOriginStore.clear();
          return true;
        }
        

        allowedOriginStore.add(allowedOrigin);
      }
    }
    
    return false;
  }
  
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
  {
    handle((HttpServletRequest)request, (HttpServletResponse)response, chain);
  }
  
  private void handle(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
  {
    String origin = request.getHeader("Origin");
    
    if ((origin != null) && (isEnabled(request)))
    {
      if ((anyOriginAllowed) || (originMatches(allowedOrigins, origin)))
      {
        if (isSimpleRequest(request))
        {
          LOG.debug("Cross-origin request to {} is a simple cross-origin request", new Object[] { request.getRequestURI() });
          handleSimpleResponse(request, response, origin);
        }
        else if (isPreflightRequest(request))
        {
          LOG.debug("Cross-origin request to {} is a preflight cross-origin request", new Object[] { request.getRequestURI() });
          handlePreflightResponse(request, response, origin);
          if (chainPreflight) {
            LOG.debug("Preflight cross-origin request to {} forwarded to application", new Object[] { request.getRequestURI() });
          }
          
        }
        else
        {
          LOG.debug("Cross-origin request to {} is a non-simple cross-origin request", new Object[] { request.getRequestURI() });
          handleSimpleResponse(request, response, origin);
        }
        
        if ((anyTimingOriginAllowed) || (originMatches(allowedTimingOrigins, origin)))
        {
          response.setHeader("Timing-Allow-Origin", origin);
        }
        else
        {
          LOG.debug("Cross-origin request to " + request.getRequestURI() + " with origin " + origin + " does not match allowed timing origins " + allowedTimingOrigins, new Object[0]);
        }
      }
      else
      {
        LOG.debug("Cross-origin request to " + request.getRequestURI() + " with origin " + origin + " does not match allowed origins " + allowedOrigins, new Object[0]);
      }
    }
    
    chain.doFilter(request, response);
  }
  


  protected boolean isEnabled(HttpServletRequest request)
  {
    for (Enumeration<String> connections = request.getHeaders("Connection"); connections.hasMoreElements();)
    {
      String connection = (String)connections.nextElement();
      if ("Upgrade".equalsIgnoreCase(connection))
      {
        for (upgrades = request.getHeaders("Upgrade"); upgrades.hasMoreElements();)
        {
          String upgrade = (String)upgrades.nextElement();
          if ("WebSocket".equalsIgnoreCase(upgrade))
            return false;
        } }
    }
    Enumeration<String> upgrades;
    return true;
  }
  
  private boolean originMatches(List<String> allowedOrigins, String originList)
  {
    if (originList.trim().length() == 0) {
      return false;
    }
    String[] origins = originList.split(" ");
    String origin; for (origin : origins)
    {
      if (origin.trim().length() != 0)
      {

        for (String allowedOrigin : allowedOrigins)
        {
          if (allowedOrigin.contains("*"))
          {
            Matcher matcher = createMatcher(origin, allowedOrigin);
            if (matcher.matches()) {
              return true;
            }
          } else if (allowedOrigin.equals(origin))
          {
            return true;
          } }
      }
    }
    return false;
  }
  
  private Matcher createMatcher(String origin, String allowedOrigin)
  {
    String regex = parseAllowedWildcardOriginToRegex(allowedOrigin);
    Pattern pattern = Pattern.compile(regex);
    return pattern.matcher(origin);
  }
  
  private String parseAllowedWildcardOriginToRegex(String allowedOrigin)
  {
    String regex = allowedOrigin.replace(".", "\\.");
    return regex.replace("*", ".*");
  }
  
  private boolean isSimpleRequest(HttpServletRequest request)
  {
    String method = request.getMethod();
    if (SIMPLE_HTTP_METHODS.contains(method))
    {




      return request.getHeader("Access-Control-Request-Method") == null;
    }
    return false;
  }
  
  private boolean isPreflightRequest(HttpServletRequest request)
  {
    String method = request.getMethod();
    if (!"OPTIONS".equalsIgnoreCase(method))
      return false;
    if (request.getHeader("Access-Control-Request-Method") == null)
      return false;
    return true;
  }
  
  private void handleSimpleResponse(HttpServletRequest request, HttpServletResponse response, String origin)
  {
    response.setHeader("Access-Control-Allow-Origin", origin);
    
    if (!anyOriginAllowed)
      response.addHeader("Vary", "Origin");
    if (allowCredentials)
      response.setHeader("Access-Control-Allow-Credentials", "true");
    if (!exposedHeaders.isEmpty()) {
      response.setHeader("Access-Control-Expose-Headers", commify(exposedHeaders));
    }
  }
  
  private void handlePreflightResponse(HttpServletRequest request, HttpServletResponse response, String origin) {
    boolean methodAllowed = isMethodAllowed(request);
    
    if (!methodAllowed)
      return;
    List<String> headersRequested = getAccessControlRequestHeaders(request);
    boolean headersAllowed = areHeadersAllowed(headersRequested);
    if (!headersAllowed)
      return;
    response.setHeader("Access-Control-Allow-Origin", origin);
    
    if (!anyOriginAllowed)
      response.addHeader("Vary", "Origin");
    if (allowCredentials)
      response.setHeader("Access-Control-Allow-Credentials", "true");
    if (preflightMaxAge > 0)
      response.setHeader("Access-Control-Max-Age", String.valueOf(preflightMaxAge));
    response.setHeader("Access-Control-Allow-Methods", commify(allowedMethods));
    if (anyHeadersAllowed) {
      response.setHeader("Access-Control-Allow-Headers", commify(headersRequested));
    } else {
      response.setHeader("Access-Control-Allow-Headers", commify(allowedHeaders));
    }
  }
  
  private boolean isMethodAllowed(HttpServletRequest request) {
    String accessControlRequestMethod = request.getHeader("Access-Control-Request-Method");
    LOG.debug("{} is {}", new Object[] { "Access-Control-Request-Method", accessControlRequestMethod });
    boolean result = false;
    if (accessControlRequestMethod != null)
      result = allowedMethods.contains(accessControlRequestMethod);
    LOG.debug("Method {} is" + (result ? "" : " not") + " among allowed methods {}", new Object[] { accessControlRequestMethod, allowedMethods });
    return result;
  }
  
  private List<String> getAccessControlRequestHeaders(HttpServletRequest request)
  {
    String accessControlRequestHeaders = request.getHeader("Access-Control-Request-Headers");
    LOG.debug("{} is {}", new Object[] { "Access-Control-Request-Headers", accessControlRequestHeaders });
    if (accessControlRequestHeaders == null) {
      return Collections.emptyList();
    }
    List<String> requestedHeaders = new ArrayList();
    String[] headers = StringUtil.csvSplit(accessControlRequestHeaders);
    for (String header : headers)
    {
      String h = header.trim();
      if (h.length() > 0)
        requestedHeaders.add(h);
    }
    return requestedHeaders;
  }
  
  private boolean areHeadersAllowed(List<String> requestedHeaders)
  {
    if (anyHeadersAllowed)
    {
      LOG.debug("Any header is allowed", new Object[0]);
      return true;
    }
    
    boolean result = true;
    for (String requestedHeader : requestedHeaders)
    {
      boolean headerAllowed = false;
      for (String allowedHeader : allowedHeaders)
      {
        if (requestedHeader.equalsIgnoreCase(allowedHeader.trim()))
        {
          headerAllowed = true;
          break;
        }
      }
      if (!headerAllowed)
      {
        result = false;
        break;
      }
    }
    LOG.debug("Headers [{}] are" + (result ? "" : " not") + " among allowed headers {}", new Object[] { requestedHeaders, allowedHeaders });
    return result;
  }
  
  private String commify(List<String> strings)
  {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < strings.size(); i++)
    {
      if (i > 0) builder.append(",");
      String string = (String)strings.get(i);
      builder.append(string);
    }
    return builder.toString();
  }
  
  public void destroy()
  {
    anyOriginAllowed = false;
    allowedOrigins.clear();
    allowedMethods.clear();
    allowedHeaders.clear();
    preflightMaxAge = 0;
    allowCredentials = false;
  }
}
