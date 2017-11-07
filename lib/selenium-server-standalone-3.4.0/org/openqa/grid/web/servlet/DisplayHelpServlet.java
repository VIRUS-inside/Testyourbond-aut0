package org.openqa.grid.web.servlet;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openqa.grid.common.GridRole;
import org.openqa.selenium.internal.BuildInfo;




























public class DisplayHelpServlet
  extends HttpServlet
{
  private static final long serialVersionUID = 8484071790930378855L;
  public static final String HELPER_TYPE_PARAMETER = "webdriver.server.displayhelpservlet.type";
  private static final String HELPER_SERVLET_TEMPLATE = "displayhelpservlet.html";
  private static final String HELPER_SERVLET_ASSET_PATH_PREFIX = "/assets/";
  private static final String HELPER_SERVLET_RESOURCE_PATH = "org/openqa/grid/images/";
  private static final String HELPER_SERVLET_TEMPLATE_CONFIG_JSON_VAR = "${servletConfigJson}";
  private final DisplayHelpServletConfig servletConfig = new DisplayHelpServletConfig(null);
  
  public DisplayHelpServlet() {}
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    process(request, response);
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    process(request, response);
  }
  
  protected void process(HttpServletRequest request, HttpServletResponse response)
    throws IOException
  {
    initServletConfig();
    
    String resource = request.getPathInfo();
    
    if ((resource.contains("/assets/")) && 
      (!resource.replace("/assets/", "").contains("/")) && 
      (!resource.replace("/assets/", "").equals("")))
    {
      resource = resource.replace("/assets/", "");
      InputStream in = getResourceInputStream(resource);
      if (in == null) {
        response.sendError(404);
      } else {
        response.setStatus(200);
        ByteStreams.copy(in, response.getOutputStream());
      }
    }
    else {
      InputStream in = getResourceInputStream("displayhelpservlet.html");
      if (in == null) {
        response.sendError(404);
      } else {
        String json = new GsonBuilder().serializeNulls().create().toJson(servletConfig);
        String jsonUtf8 = new String(json.getBytes(), "UTF-8");
        
        String htmlTemplate = (String)new BufferedReader(new InputStreamReader(in, "UTF-8")).lines().collect(Collectors.joining("\n"));
        
        String updatedTemplate = htmlTemplate.replace("${servletConfigJson}", jsonUtf8);
        
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        response.getOutputStream().print(updatedTemplate);
      }
    }
    
    response.flushBuffer();
  }
  
  private void initServletConfig() {
    if (servletConfig.version == null) {
      servletConfig.version = new BuildInfo().getReleaseLabel();
    }
    if (servletConfig.type == null) {
      servletConfig.type = getHelperType();
    }
    if (servletConfig.consoleLink == null)
    {

      servletConfig.consoleLink = getInitParameter("webdriver.server.consoleservlet.path", "");
    }
  }
  
  private String getHelperType() {
    GridRole role = GridRole.get(getInitParameter("webdriver.server.displayhelpservlet.type", "standalone"));
    String type = "Standalone";
    switch (1.$SwitchMap$org$openqa$grid$common$GridRole[role.ordinal()]) {
    case 1: 
      type = "Grid Hub";
      break;
    
    case 2: 
      type = "Grid Node";
      break;
    }
    
    


    return type;
  }
  
  public String getInitParameter(String param)
  {
    return getServletContext().getInitParameter(param);
  }
  
  private String getInitParameter(String param, String defaultValue) {
    String value = getInitParameter(param);
    if ((value == null) || (value.trim().isEmpty())) {
      return defaultValue;
    }
    return value;
  }
  
  private InputStream getResourceInputStream(String resource)
    throws IOException
  {
    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("org/openqa/grid/images/" + resource);
    if (in == null) {
      return null;
    }
    return in;
  }
  
  private final class DisplayHelpServletConfig
  {
    String version;
    String type;
    String consoleLink;
    
    private DisplayHelpServletConfig() {}
  }
}
