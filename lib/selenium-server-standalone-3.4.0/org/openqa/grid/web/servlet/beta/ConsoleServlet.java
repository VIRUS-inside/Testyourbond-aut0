package org.openqa.grid.web.servlet.beta;

import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.internal.utils.HtmlRenderer;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.web.servlet.RegistryBasedServlet;
import org.openqa.selenium.internal.BuildInfo;
import org.openqa.selenium.remote.DesiredCapabilities;




















public class ConsoleServlet
  extends RegistryBasedServlet
{
  private static final long serialVersionUID = 8484071790930378855L;
  private static final Logger log = Logger.getLogger(ConsoleServlet.class.getName());
  private static String coreVersion;
  public static final String CONSOLE_PATH_PARAMETER = "webdriver.server.consoleservlet.path";
  
  public ConsoleServlet()
  {
    this(null);
  }
  
  public ConsoleServlet(Registry registry) {
    super(registry);
    coreVersion = new BuildInfo().getReleaseLabel();
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
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
    int refresh = -1;
    
    if (request.getParameter("refresh") != null) {
      try {
        refresh = Integer.parseInt(request.getParameter("refresh"));
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
    


    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(200);
    
    StringBuilder builder = new StringBuilder();
    
    builder.append("<html>");
    builder.append("<head>");
    builder
      .append("<script src='/grid/resources/org/openqa/grid/images/jquery-3.1.1.min.js'></script>");
    
    builder.append("<script src='/grid/resources/org/openqa/grid/images/consoleservlet.js'></script>");
    
    builder
      .append("<link href='/grid/resources/org/openqa/grid/images/consoleservlet.css' rel='stylesheet' type='text/css' />");
    builder
      .append("<link href='/grid/resources/org/openqa/grid/images/favicon.ico' rel='icon' type='image/x-icon' />");
    

    if (refresh != -1) {
      builder.append(String.format("<meta http-equiv='refresh' content='%d' />", new Object[] { Integer.valueOf(refresh) }));
    }
    builder.append("<title>Grid Console</title>");
    
    builder.append("<style>");
    builder.append(".busy {");
    builder.append(" opacity : 0.4;");
    builder.append("filter: alpha(opacity=40);");
    builder.append("}");
    builder.append("</style>");
    builder.append("</head>");
    
    builder.append("<body>");
    
    builder.append("<div id='main-content'>");
    
    builder.append(getHeader());
    

    List<String> nodes = new ArrayList();
    for (RemoteProxy proxy : getRegistry().getAllProxies()) {
      HtmlRenderer beta = new WebProxyHtmlRendererBeta(proxy);
      nodes.add(beta.renderSummary());
    }
    
    int size = nodes.size();
    int rightColumnSize = size / 2;
    int leftColumnSize = size - rightColumnSize;
    


    builder.append("<div id='left-column'>");
    for (int i = 0; i < leftColumnSize; i++) {
      builder.append((String)nodes.get(i));
    }
    

    builder.append("</div>");
    
    builder.append("<div id='right-column'>");
    for (int i = leftColumnSize; i < nodes.size(); i++) {
      builder.append((String)nodes.get(i));
    }
    

    builder.append("</div>");
    
    builder.append("<div class='clearfix'></div>");
    
    builder.append(getRequestQueue());
    

    if (request.getParameter("config") != null) {
      builder.append(getConfigInfo(request.getParameter("configDebug") != null));
    } else {
      builder.append("<a href='?config=true&configDebug=true'>view config</a>");
    }
    

    builder.append("</div>");
    builder.append("</body>");
    builder.append("</html>");
    
    InputStream in = new ByteArrayInputStream(builder.toString().getBytes("UTF-8"));
    try {
      ByteStreams.copy(in, response.getOutputStream());
    } finally {
      in.close();
      response.getOutputStream().close();
    }
  }
  
  private Object getRequestQueue() {
    StringBuilder builder = new StringBuilder();
    builder.append("<div>");
    int numUnprocessedRequests = getRegistry().getNewSessionRequestCount();
    
    if (numUnprocessedRequests > 0) {
      builder.append(String.format("%d requests waiting for a slot to be free.", new Object[] {
        Integer.valueOf(numUnprocessedRequests) }));
    }
    
    builder.append("<ul>");
    for (DesiredCapabilities req : getRegistry().getDesiredCapabilities()) {
      builder.append("<li>").append(req).append("</li>");
    }
    builder.append("</ul>");
    builder.append("</div>");
    return builder.toString();
  }
  
  private Object getHeader() {
    StringBuilder builder = new StringBuilder();
    builder.append("<div id='header'>");
    builder.append("<h1><a href='/grid/console'>Selenium</a></h1>");
    builder.append("<h2>Grid Console v.");
    builder.append(coreVersion);
    builder.append("</h2>");
    builder.append("<div><a id='helplink' target='_blank' href='https://github.com/SeleniumHQ/selenium/wiki/Grid2'>Help</a></div>");
    builder.append("</div>");
    builder.append("");
    return builder.toString();
  }
  






  private String getConfigInfo(boolean verbose)
  {
    StringBuilder builder = new StringBuilder();
    
    GridHubConfiguration config = getRegistry().getConfiguration();
    builder.append("<div  id='hub-config'>");
    builder.append("<b>Config for the hub :</b><br/>");
    builder.append(prettyHtmlPrint(config));
    
    if (verbose)
    {
      GridHubConfiguration tmp = new GridHubConfiguration();
      
      builder.append("<b>Config details :</b><br/>");
      builder.append("<b>hub launched with :</b>");
      builder.append(config.toString());
      
      builder.append("<br/><b>the final configuration comes from :</b><br/>");
      builder.append("<b>the default :</b><br/>");
      builder.append(prettyHtmlPrint(tmp));
      
      builder.append("<br/><b>updated with params :</b></br>");
      tmp.merge(config);
      builder.append(prettyHtmlPrint(tmp));
    }
    builder.append("</div>");
    return builder.toString();
  }
  
  private String prettyHtmlPrint(GridHubConfiguration config) {
    return config.toString("<abbr title='%1$s'>%1$s : </abbr>%2$s</br>");
  }
}
