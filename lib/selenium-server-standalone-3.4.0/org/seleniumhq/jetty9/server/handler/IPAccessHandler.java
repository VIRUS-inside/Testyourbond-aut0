package org.seleniumhq.jetty9.server.handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.PathMap;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.HttpChannel;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.util.IPAddressMap;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;



















































































/**
 * @deprecated
 */
public class IPAccessHandler
  extends HandlerWrapper
{
  private static final Logger LOG = Log.getLogger(IPAccessHandler.class);
  
  PathMap<IPAddressMap<Boolean>> _white = new PathMap(true);
  PathMap<IPAddressMap<Boolean>> _black = new PathMap(true);
  boolean _whiteListByPath = false;
  








  public IPAccessHandler() {}
  








  public IPAccessHandler(String[] white, String[] black)
  {
    if ((white != null) && (white.length > 0))
      setWhite(white);
    if ((black != null) && (black.length > 0)) {
      setBlack(black);
    }
  }
  





  public void addWhite(String entry)
  {
    add(entry, _white);
  }
  






  public void addBlack(String entry)
  {
    add(entry, _black);
  }
  






  public void setWhite(String[] entries)
  {
    set(entries, _white);
  }
  






  public void setBlack(String[] entries)
  {
    set(entries, _black);
  }
  






  public void setWhiteListByPath(boolean whiteListByPath)
  {
    _whiteListByPath = whiteListByPath;
  }
  







  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    HttpChannel channel = baseRequest.getHttpChannel();
    if (channel != null)
    {
      EndPoint endp = channel.getEndPoint();
      if (endp != null)
      {
        InetSocketAddress address = endp.getRemoteAddress();
        if ((address != null) && (!isAddrUriAllowed(address.getHostString(), baseRequest.getPathInfo())))
        {
          response.sendError(403);
          baseRequest.setHandled(true);
          return;
        }
      }
    }
    
    getHandler().handle(target, baseRequest, request, response);
  }
  









  protected void add(String entry, PathMap<IPAddressMap<Boolean>> patternMap)
  {
    if ((entry != null) && (entry.length() > 0))
    {
      boolean deprecated = false;
      int idx;
      int idx; if (entry.indexOf('|') > 0)
      {
        idx = entry.indexOf('|');
      }
      else
      {
        idx = entry.indexOf('/');
        deprecated = idx >= 0;
      }
      
      String addr = idx > 0 ? entry.substring(0, idx) : entry;
      String path = idx > 0 ? entry.substring(idx) : "/*";
      
      if (addr.endsWith("."))
        deprecated = true;
      if ((path != null) && ((path.startsWith("|")) || (path.startsWith("/*.")))) {
        path = path.substring(1);
      }
      IPAddressMap<Boolean> addrMap = (IPAddressMap)patternMap.get(path);
      if (addrMap == null)
      {
        addrMap = new IPAddressMap();
        patternMap.put(path, addrMap);
      }
      if ((addr != null) && (!"".equals(addr)))
      {
        addrMap.put(addr, Boolean.valueOf(true));
      }
      if (deprecated) {
        LOG.debug(toString() + " - deprecated specification syntax: " + entry, new Object[0]);
      }
    }
  }
  







  protected void set(String[] entries, PathMap<IPAddressMap<Boolean>> patternMap)
  {
    patternMap.clear();
    
    if ((entries != null) && (entries.length > 0))
    {
      for (String addrPath : entries)
      {
        add(addrPath, patternMap);
      }
    }
  }
  




  protected boolean isAddrUriAllowed(String addr, String path)
  {
    boolean match;
    



    if (_white.size() > 0)
    {
      match = false;
      boolean matchedByPath = false;
      
      for (Map.Entry<String, IPAddressMap<Boolean>> entry : _white.getMatches(path))
      {
        matchedByPath = true;
        IPAddressMap<Boolean> addrMap = (IPAddressMap)entry.getValue();
        if ((addrMap != null) && ((addrMap.size() == 0) || (addrMap.match(addr) != null)))
        {
          match = true;
          break;
        }
      }
      
      if (_whiteListByPath)
      {
        if ((matchedByPath) && (!match)) {
          return false;
        }
        
      }
      else if (!match) {
        return false;
      }
    }
    
    if (_black.size() > 0)
    {
      for (Map.Entry<String, IPAddressMap<Boolean>> entry : _black.getMatches(path))
      {
        Object addrMap = (IPAddressMap)entry.getValue();
        if ((addrMap != null) && ((((IPAddressMap)addrMap).size() == 0) || (((IPAddressMap)addrMap).match(addr) != null))) {
          return false;
        }
      }
    }
    
    return true;
  }
  





  public String dump()
  {
    StringBuilder buf = new StringBuilder();
    
    buf.append(toString());
    buf.append(" WHITELIST:\n");
    dump(buf, _white);
    buf.append(toString());
    buf.append(" BLACKLIST:\n");
    dump(buf, _black);
    
    return buf.toString();
  }
  







  protected void dump(StringBuilder buf, PathMap<IPAddressMap<Boolean>> patternMap)
  {
    for (Iterator localIterator1 = patternMap.keySet().iterator(); localIterator1.hasNext();) { path = (String)localIterator1.next();
      
      for (String addr : ((IPAddressMap)patternMap.get(path)).keySet())
      {
        buf.append("# ");
        buf.append(addr);
        buf.append("|");
        buf.append(path);
        buf.append("\n");
      }
    }
    String path;
  }
}
