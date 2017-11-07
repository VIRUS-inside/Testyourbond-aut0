package org.seleniumhq.jetty9.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.HttpMethod;
import org.seleniumhq.jetty9.util.IO;
import org.seleniumhq.jetty9.util.MultiMap;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.UrlEncoded;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;











































public class CGI
  extends HttpServlet
{
  private static final long serialVersionUID = -6182088932884791074L;
  private static final Logger LOG = Log.getLogger(CGI.class);
  private boolean _ok;
  private File _docRoot;
  private boolean _cgiBinProvided;
  private String _path;
  private String _cmdPrefix;
  private boolean _useFullPath;
  private EnvList _env;
  private boolean _ignoreExitState;
  private boolean _relative;
  
  public CGI() {}
  
  public void init() throws ServletException
  {
    _env = new EnvList();
    _cmdPrefix = getInitParameter("commandPrefix");
    _useFullPath = Boolean.parseBoolean(getInitParameter("useFullPath"));
    _relative = Boolean.parseBoolean(getInitParameter("cgibinResourceBaseIsRelative"));
    
    String tmp = getInitParameter("cgibinResourceBase");
    if (tmp != null) {
      _cgiBinProvided = true;
    }
    else {
      tmp = getInitParameter("resourceBase");
      if (tmp != null) {
        _cgiBinProvided = true;
      } else {
        tmp = getServletContext().getRealPath("/");
      }
    }
    if ((_relative) && (_cgiBinProvided))
    {
      tmp = getServletContext().getRealPath(tmp);
    }
    
    if (tmp == null)
    {
      LOG.warn("CGI: no CGI bin !", new Object[0]);
      return;
    }
    
    File dir = new File(tmp);
    if (!dir.exists())
    {
      LOG.warn("CGI: CGI bin does not exist - " + dir, new Object[0]);
      return;
    }
    
    if (!dir.canRead())
    {
      LOG.warn("CGI: CGI bin is not readable - " + dir, new Object[0]);
      return;
    }
    
    if (!dir.isDirectory())
    {
      LOG.warn("CGI: CGI bin is not a directory - " + dir, new Object[0]);
      return;
    }
    
    try
    {
      _docRoot = dir.getCanonicalFile();
    }
    catch (IOException e)
    {
      LOG.warn("CGI: CGI bin failed - " + dir, e);
      return;
    }
    
    _path = getInitParameter("Path");
    if (_path != null) {
      _env.set("PATH", _path);
    }
    _ignoreExitState = "true".equalsIgnoreCase(getInitParameter("ignoreExitState"));
    Enumeration<String> e = getInitParameterNames();
    while (e.hasMoreElements())
    {
      String n = (String)e.nextElement();
      if ((n != null) && (n.startsWith("ENV_")))
        _env.set(n.substring(4), getInitParameter(n));
    }
    if (!_env.envMap.containsKey("SystemRoot"))
    {
      String os = System.getProperty("os.name");
      if ((os != null) && (os.toLowerCase(Locale.ENGLISH).contains("windows")))
      {
        _env.set("SystemRoot", "C:\\WINDOWS");
      }
    }
    
    _ok = true;
  }
  
  public void service(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
  {
    if (!_ok)
    {
      res.sendError(503);
      return;
    }
    
    if (LOG.isDebugEnabled())
    {
      LOG.debug("CGI: ContextPath : " + req.getContextPath(), new Object[0]);
      LOG.debug("CGI: ServletPath : " + req.getServletPath(), new Object[0]);
      LOG.debug("CGI: PathInfo    : " + req.getPathInfo(), new Object[0]);
      LOG.debug("CGI: _docRoot    : " + _docRoot, new Object[0]);
      LOG.debug("CGI: _path       : " + _path, new Object[0]);
      LOG.debug("CGI: _ignoreExitState: " + _ignoreExitState, new Object[0]);
    }
    



    String pathInContext = (_relative ? "" : StringUtil.nonNull(req.getServletPath())) + StringUtil.nonNull(req.getPathInfo());
    File execCmd = new File(_docRoot, pathInContext);
    String pathInfo = pathInContext;
    
    if (!_useFullPath)
    {
      String path = pathInContext;
      String info = "";
      

      while (((path.endsWith("/")) || (!execCmd.exists())) && (path.length() >= 0))
      {
        int index = path.lastIndexOf('/');
        path = path.substring(0, index);
        info = pathInContext.substring(index, pathInContext.length());
        execCmd = new File(_docRoot, path);
      }
      
      if ((path.length() == 0) || (!execCmd.exists()) || (execCmd.isDirectory()) || (!execCmd.getCanonicalPath().equals(execCmd.getAbsolutePath())))
      {
        res.sendError(404);
      }
      
      pathInfo = info;
    }
    exec(execCmd, pathInfo, req, res);
  }
  










  private void exec(File command, String pathInfo, HttpServletRequest req, HttpServletResponse res)
    throws IOException
  {
    assert (req != null);
    assert (res != null);
    assert (pathInfo != null);
    assert (command != null);
    
    if (LOG.isDebugEnabled())
    {
      LOG.debug("CGI: script is " + command, new Object[0]);
      LOG.debug("CGI: pathInfo is " + pathInfo, new Object[0]);
    }
    
    String bodyFormEncoded = null;
    if (((HttpMethod.POST.is(req.getMethod())) || (HttpMethod.PUT.is(req.getMethod()))) && ("application/x-www-form-urlencoded".equals(req.getContentType())))
    {
      MultiMap<String> parameterMap = new MultiMap();
      Enumeration<String> names = req.getParameterNames();
      while (names.hasMoreElements())
      {
        String parameterName = (String)names.nextElement();
        parameterMap.addValues(parameterName, req.getParameterValues(parameterName));
      }
      bodyFormEncoded = UrlEncoded.encode(parameterMap, Charset.forName(req.getCharacterEncoding()), true);
    }
    
    EnvList env = new EnvList(_env);
    


    env.set("AUTH_TYPE", req.getAuthType());
    
    int contentLen = req.getContentLength();
    if (contentLen < 0)
      contentLen = 0;
    if (bodyFormEncoded != null)
    {
      env.set("CONTENT_LENGTH", Integer.toString(bodyFormEncoded.length()));
    }
    else
    {
      env.set("CONTENT_LENGTH", Integer.toString(contentLen));
    }
    env.set("CONTENT_TYPE", req.getContentType());
    env.set("GATEWAY_INTERFACE", "CGI/1.1");
    if (pathInfo.length() > 0)
    {
      env.set("PATH_INFO", pathInfo);
    }
    
    String pathTranslated = req.getPathTranslated();
    if ((pathTranslated == null) || (pathTranslated.length() == 0))
      pathTranslated = pathInfo;
    env.set("PATH_TRANSLATED", pathTranslated);
    env.set("QUERY_STRING", req.getQueryString());
    env.set("REMOTE_ADDR", req.getRemoteAddr());
    env.set("REMOTE_HOST", req.getRemoteHost());
    





    env.set("REMOTE_USER", req.getRemoteUser());
    env.set("REQUEST_METHOD", req.getMethod());
    
    String scriptName;
    String scriptName;
    String scriptPath;
    if (_cgiBinProvided)
    {
      String scriptPath = command.getAbsolutePath();
      scriptName = scriptPath.substring(_docRoot.getAbsolutePath().length());
    }
    else
    {
      String requestURI = req.getRequestURI();
      scriptName = requestURI.substring(0, requestURI.length() - pathInfo.length());
      scriptPath = getServletContext().getRealPath(scriptName);
    }
    env.set("SCRIPT_FILENAME", scriptPath);
    env.set("SCRIPT_NAME", scriptName);
    
    env.set("SERVER_NAME", req.getServerName());
    env.set("SERVER_PORT", Integer.toString(req.getServerPort()));
    env.set("SERVER_PROTOCOL", req.getProtocol());
    env.set("SERVER_SOFTWARE", getServletContext().getServerInfo());
    
    Enumeration<String> enm = req.getHeaderNames();
    while (enm.hasMoreElements())
    {
      String name = (String)enm.nextElement();
      if (!name.equalsIgnoreCase("Proxy"))
      {
        String value = req.getHeader(name);
        env.set("HTTP_" + name.toUpperCase(Locale.ENGLISH).replace('-', '_'), value);
      }
    }
    
    env.set("HTTPS", req.isSecure() ? "ON" : "OFF");
    






    String absolutePath = command.getAbsolutePath();
    String execCmd = absolutePath;
    

    if ((execCmd.length() > 0) && (execCmd.charAt(0) != '"') && (execCmd.contains(" "))) {
      execCmd = "\"" + execCmd + "\"";
    }
    if (_cmdPrefix != null) {
      execCmd = _cmdPrefix + " " + execCmd;
    }
    LOG.debug("Environment: " + env.getExportString(), new Object[0]);
    LOG.debug("Command: " + execCmd, new Object[0]);
    
    final Process p = Runtime.getRuntime().exec(execCmd, env.getEnvArray(), _docRoot);
    

    if (bodyFormEncoded != null) {
      writeProcessInput(p, bodyFormEncoded);
    } else if (contentLen > 0) {
      writeProcessInput(p, req.getInputStream(), contentLen);
    }
    

    OutputStream os = null;
    AsyncContext async = req.startAsync();
    try
    {
      async.start(new Runnable()
      {

        public void run()
        {
          try
          {
            IO.copy(p.getErrorStream(), System.err);
          }
          catch (IOException e)
          {
            CGI.LOG.warn(e);
          }
          
        }
        

      });
      String line = null;
      InputStream inFromCgi = p.getInputStream();
      


      while ((line = getTextLineFromStream(inFromCgi)).length() > 0)
      {
        if (!line.startsWith("HTTP"))
        {
          int k = line.indexOf(':');
          if (k > 0)
          {
            String key = line.substring(0, k).trim();
            String value = line.substring(k + 1).trim();
            if ("Location".equals(key))
            {
              res.sendRedirect(res.encodeRedirectURL(value));
            }
            else if ("Status".equals(key))
            {
              String[] token = value.split(" ");
              int status = Integer.parseInt(token[0]);
              res.setStatus(status);

            }
            else
            {
              res.addHeader(key, value);
            }
          }
        }
      }
      
      os = res.getOutputStream();
      IO.copy(inFromCgi, os);
      p.waitFor();
      
      if (!_ignoreExitState)
      {
        int exitValue = p.exitValue();
        if (0 != exitValue)
        {
          LOG.warn("Non-zero exit status (" + exitValue + ") from CGI program: " + absolutePath, new Object[0]);
          if (!res.isCommitted()) {
            res.sendError(500, "Failed to exec CGI");
          }
          
        }
      }
    }
    catch (IOException e)
    {
      LOG.debug("CGI: Client closed connection!", e);
    }
    catch (InterruptedException ie)
    {
      LOG.debug("CGI: interrupted!", new Object[0]);
    }
    finally
    {
      if (os != null)
      {
        try
        {
          os.close();
        }
        catch (Exception e)
        {
          LOG.debug(e);
        }
      }
      p.destroy();
      
      async.complete();
    }
  }
  

















  private static void writeProcessInput(Process p, final String input)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Writer outToCgi = new OutputStreamWriter(val$p.getOutputStream());Throwable localThrowable3 = null;
          try {
            outToCgi.write(input);
          }
          catch (Throwable localThrowable1)
          {
            localThrowable3 = localThrowable1;throw localThrowable1;
          }
          finally {
            if (outToCgi != null) if (localThrowable3 != null) try { outToCgi.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else outToCgi.close();
          }
        }
        catch (IOException e) {
          CGI.LOG.debug(e);
        }
      }
    })
    















      .start();
  }
  
  private static void writeProcessInput(Process p, final InputStream input, final int len)
  {
    if (len <= 0) { return;
    }
    















    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          OutputStream outToCgi = val$p.getOutputStream();
          IO.copy(input, outToCgi, len);
          outToCgi.close();
        }
        catch (IOException e)
        {
          CGI.LOG.debug(e);
        }
      }
    })
    














      .start();
  }
  






  private static String getTextLineFromStream(InputStream is)
    throws IOException
  {
    StringBuilder buffer = new StringBuilder();
    
    int b;
    while (((b = is.read()) != -1) && (b != 10))
    {
      buffer.append((char)b);
    }
    return buffer.toString().trim();
  }
  


  private static class EnvList
  {
    private Map<String, String> envMap;
    

    EnvList()
    {
      envMap = new HashMap();
    }
    
    EnvList(EnvList l)
    {
      envMap = new HashMap(envMap);
    }
    






    public void set(String name, String value)
    {
      envMap.put(name, name + "=" + StringUtil.nonNull(value));
    }
    





    public String[] getEnvArray()
    {
      return (String[])envMap.values().toArray(new String[envMap.size()]);
    }
    
    public String getExportString()
    {
      StringBuilder sb = new StringBuilder();
      for (String variable : getEnvArray())
      {
        sb.append("export \"");
        sb.append(variable);
        sb.append("\"; ");
      }
      return sb.toString();
    }
    

    public String toString()
    {
      return envMap.toString();
    }
  }
}
