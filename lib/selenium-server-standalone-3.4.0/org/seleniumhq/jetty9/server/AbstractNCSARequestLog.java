package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.security.Principal;
import java.util.Locale;
import javax.servlet.http.Cookie;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.MetaData.Response;
import org.seleniumhq.jetty9.http.pathmap.PathMappings;
import org.seleniumhq.jetty9.util.DateCache;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
























public abstract class AbstractNCSARequestLog
  extends AbstractLifeCycle
  implements RequestLog
{
  protected static final Logger LOG = Log.getLogger(AbstractNCSARequestLog.class);
  
  private static ThreadLocal<StringBuilder> _buffers = new ThreadLocal()
  {

    protected StringBuilder initialValue()
    {
      return new StringBuilder(256);
    }
  };
  
  private String[] _ignorePaths;
  
  private boolean _extended;
  private transient PathMappings<String> _ignorePathMap;
  private boolean _logLatency = false;
  private boolean _logCookies = false;
  private boolean _logServer = false;
  private boolean _preferProxiedForAddress;
  private transient DateCache _logDateCache;
  private String _logDateFormat = "dd/MMM/yyyy:HH:mm:ss Z";
  private Locale _logLocale = Locale.getDefault();
  private String _logTimeZone = "GMT";
  



  public AbstractNCSARequestLog() {}
  



  protected abstract boolean isEnabled();
  



  public abstract void write(String paramString)
    throws IOException;
  



  private void append(StringBuilder buf, String s)
  {
    if ((s == null) || (s.length() == 0)) {
      buf.append('-');
    } else {
      buf.append(s);
    }
  }
  





  public void log(Request request, Response response)
  {
    try
    {
      if ((_ignorePathMap != null) && (_ignorePathMap.getMatch(request.getRequestURI()) != null)) {
        return;
      }
      if (!isEnabled()) {
        return;
      }
      StringBuilder buf = (StringBuilder)_buffers.get();
      buf.setLength(0);
      
      if (_logServer)
      {
        append(buf, request.getServerName());
        buf.append(' ');
      }
      
      String addr = null;
      if (_preferProxiedForAddress)
      {
        addr = request.getHeader(HttpHeader.X_FORWARDED_FOR.toString());
      }
      
      if (addr == null) {
        addr = request.getRemoteAddr();
      }
      buf.append(addr);
      buf.append(" - ");
      Authentication authentication = request.getAuthentication();
      append(buf, (authentication instanceof Authentication.User) ? ((Authentication.User)authentication).getUserIdentity().getUserPrincipal().getName() : null);
      
      buf.append(" [");
      if (_logDateCache != null) {
        buf.append(_logDateCache.format(request.getTimeStamp()));
      } else {
        buf.append(request.getTimeStamp());
      }
      buf.append("] \"");
      append(buf, request.getMethod());
      buf.append(' ');
      append(buf, request.getOriginalURI());
      buf.append(' ');
      append(buf, request.getProtocol());
      buf.append("\" ");
      
      int status = response.getCommittedMetaData().getStatus();
      if (status >= 0)
      {
        buf.append((char)(48 + status / 100 % 10));
        buf.append((char)(48 + status / 10 % 10));
        buf.append((char)(48 + status % 10));
      }
      else {
        buf.append(status);
      }
      long written = response.getHttpChannel().getBytesWritten();
      if (written >= 0L)
      {
        buf.append(' ');
        if (written > 99999L) {
          buf.append(written);
        }
        else {
          if (written > 9999L)
            buf.append((char)(int)(48L + written / 10000L % 10L));
          if (written > 999L)
            buf.append((char)(int)(48L + written / 1000L % 10L));
          if (written > 99L)
            buf.append((char)(int)(48L + written / 100L % 10L));
          if (written > 9L)
            buf.append((char)(int)(48L + written / 10L % 10L));
          buf.append((char)(int)(48L + written % 10L));
        }
        buf.append(' ');
      }
      else {
        buf.append(" - ");
      }
      
      if (_extended) {
        logExtended(buf, request, response);
      }
      if (_logCookies)
      {
        Cookie[] cookies = request.getCookies();
        if ((cookies == null) || (cookies.length == 0)) {
          buf.append(" -");
        }
        else {
          buf.append(" \"");
          for (int i = 0; i < cookies.length; i++)
          {
            if (i != 0)
              buf.append(';');
            buf.append(cookies[i].getName());
            buf.append('=');
            buf.append(cookies[i].getValue());
          }
          buf.append('"');
        }
      }
      
      if (_logLatency)
      {
        long now = System.currentTimeMillis();
        
        if (_logLatency)
        {
          buf.append(' ');
          buf.append(now - request.getTimeStamp());
        }
      }
      
      String log = buf.toString();
      write(log);
    }
    catch (IOException e)
    {
      LOG.warn(e);
    }
  }
  







  protected void logExtended(StringBuilder b, Request request, Response response)
    throws IOException
  {
    String referer = request.getHeader(HttpHeader.REFERER.toString());
    if (referer == null) {
      b.append("\"-\" ");
    }
    else {
      b.append('"');
      b.append(referer);
      b.append("\" ");
    }
    
    String agent = request.getHeader(HttpHeader.USER_AGENT.toString());
    if (agent == null) {
      b.append("\"-\"");
    }
    else {
      b.append('"');
      b.append(agent);
      b.append('"');
    }
  }
  





  public void setIgnorePaths(String[] ignorePaths)
  {
    _ignorePaths = ignorePaths;
  }
  





  public String[] getIgnorePaths()
  {
    return _ignorePaths;
  }
  






  public void setLogCookies(boolean logCookies)
  {
    _logCookies = logCookies;
  }
  





  public boolean getLogCookies()
  {
    return _logCookies;
  }
  





  public void setLogServer(boolean logServer)
  {
    _logServer = logServer;
  }
  





  public boolean getLogServer()
  {
    return _logServer;
  }
  






  public void setLogLatency(boolean logLatency)
  {
    _logLatency = logLatency;
  }
  





  public boolean getLogLatency()
  {
    return _logLatency;
  }
  





  @Deprecated
  public void setLogDispatch(boolean value) {}
  





  @Deprecated
  public boolean isLogDispatch()
  {
    return false;
  }
  







  public void setPreferProxiedForAddress(boolean preferProxiedForAddress)
  {
    _preferProxiedForAddress = preferProxiedForAddress;
  }
  





  public boolean getPreferProxiedForAddress()
  {
    return _preferProxiedForAddress;
  }
  





  public void setExtended(boolean extended)
  {
    _extended = extended;
  }
  





  @ManagedAttribute("use extended NCSA format")
  public boolean isExtended()
  {
    return _extended;
  }
  





  protected synchronized void doStart()
    throws Exception
  {
    if (_logDateFormat != null)
    {
      _logDateCache = new DateCache(_logDateFormat, _logLocale, _logTimeZone);
    }
    
    if ((_ignorePaths != null) && (_ignorePaths.length > 0))
    {
      _ignorePathMap = new PathMappings();
      for (int i = 0; i < _ignorePaths.length; i++) {
        _ignorePathMap.put(_ignorePaths[i], _ignorePaths[i]);
      }
    } else {
      _ignorePathMap = null;
    }
    super.doStart();
  }
  
  protected void doStop()
    throws Exception
  {
    _logDateCache = null;
    super.doStop();
  }
  






  public void setLogDateFormat(String format)
  {
    _logDateFormat = format;
  }
  





  public String getLogDateFormat()
  {
    return _logDateFormat;
  }
  





  public void setLogLocale(Locale logLocale)
  {
    _logLocale = logLocale;
  }
  





  public Locale getLogLocale()
  {
    return _logLocale;
  }
  





  public void setLogTimeZone(String tz)
  {
    _logTimeZone = tz;
  }
  





  @ManagedAttribute("the timezone")
  public String getLogTimeZone()
  {
    return _logTimeZone;
  }
}
