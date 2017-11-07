package org.seleniumhq.jetty9.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.Cookie;
import org.seleniumhq.jetty9.http.QuotedCSV;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;






























public class CookieCutter
{
  private static final Logger LOG = Log.getLogger(CookieCutter.class);
  
  private Cookie[] _cookies;
  private Cookie[] _lastCookies;
  private final List<String> _fieldList = new ArrayList();
  
  int _fields;
  

  public CookieCutter() {}
  
  public Cookie[] getCookies()
  {
    if (_cookies != null) {
      return _cookies;
    }
    if ((_lastCookies != null) && (_fields == _fieldList.size())) {
      _cookies = _lastCookies;
    } else
      parseFields();
    _lastCookies = _cookies;
    return _cookies;
  }
  
  public void setCookies(Cookie[] cookies)
  {
    _cookies = cookies;
    _lastCookies = null;
    _fieldList.clear();
    _fields = 0;
  }
  
  public void reset()
  {
    _cookies = null;
    _fields = 0;
  }
  
  public void addCookieField(String f)
  {
    if (f == null)
      return;
    f = f.trim();
    if (f.length() == 0) {
      return;
    }
    if (_fieldList.size() > _fields)
    {
      if (f.equals(_fieldList.get(_fields)))
      {
        _fields += 1;
        return;
      }
      
      while (_fieldList.size() > _fields)
        _fieldList.remove(_fields);
    }
    _cookies = null;
    _lastCookies = null;
    _fieldList.add(_fields++, f);
  }
  

  protected void parseFields()
  {
    _lastCookies = null;
    _cookies = null;
    
    List<Cookie> cookies = new ArrayList();
    
    int version = 0;
    

    while (_fieldList.size() > _fields) {
      _fieldList.remove(_fields);
    }
    
    for (String hdr : _fieldList)
    {

      String name = null;
      String value = null;
      
      Cookie cookie = null;
      
      boolean invalue = false;
      boolean quoted = false;
      boolean escaped = false;
      int tokenstart = -1;
      int tokenend = -1;
      int i = 0;int length = hdr.length(); for (int last = length - 1; i < length; i++)
      {
        char c = hdr.charAt(i);
        

        if (quoted)
        {
          if (escaped)
          {
            escaped = false;
            continue;
          }
          
          switch (c)
          {
          case '"': 
            tokenend = i;
            quoted = false;
            

            if (i != last)
              break;
            if (invalue) {
              value = hdr.substring(tokenstart, tokenend + 1);
            }
            else {
              name = hdr.substring(tokenstart, tokenend + 1);
              value = "";
            }
            

            break;
          case '\\': 
            escaped = true;
            break;
          



          }
          
        }
        else if (invalue)
        {

          switch (c)
          {
          case '\t': 
          case ' ': 
            break;
          
          case '"': 
            if (tokenstart < 0)
            {
              quoted = true;
              tokenstart = i;
            }
            tokenend = i;
            if (i != last)
              continue;
            value = hdr.substring(tokenstart, tokenend + 1);
            break;
          


          case ';': 
            if (tokenstart >= 0) {
              value = hdr.substring(tokenstart, tokenend + 1);
            } else
              value = "";
            tokenstart = -1;
            invalue = false;
            break;
          
          default: 
            if (tokenstart < 0)
              tokenstart = i;
            tokenend = i;
            if (i != last)
              continue;
            value = hdr.substring(tokenstart, tokenend + 1);
            break;
          

          }
          
        }
        else
        {
          switch (c)
          {
          case '\t': 
          case ' ': 
            break;
          
          case '"': 
            if (tokenstart < 0)
            {
              quoted = true;
              tokenstart = i;
            }
            tokenend = i;
            if (i != last)
              continue;
            name = hdr.substring(tokenstart, tokenend + 1);
            value = "";
            break;
          


          case ';': 
            if (tokenstart >= 0)
            {
              name = hdr.substring(tokenstart, tokenend + 1);
              value = "";
            }
            tokenstart = -1;
            break;
          
          case '=': 
            if (tokenstart >= 0)
              name = hdr.substring(tokenstart, tokenend + 1);
            tokenstart = -1;
            invalue = true;
            break;
          
          default: 
            if (tokenstart < 0)
              tokenstart = i;
            tokenend = i;
            if (i != last)
              continue;
            name = hdr.substring(tokenstart, tokenend + 1);
            value = "";
          }
          
        }
        




        if ((value != null) && (name != null))
        {

          name = QuotedCSV.unquote(name);
          value = QuotedCSV.unquote(value);
          
          try
          {
            if (name.startsWith("$"))
            {
              String lowercaseName = name.toLowerCase(Locale.ENGLISH);
              if ("$path".equals(lowercaseName))
              {
                if (cookie != null) {
                  cookie.setPath(value);
                }
              } else if ("$domain".equals(lowercaseName))
              {
                if (cookie != null) {
                  cookie.setDomain(value);
                }
              } else if ("$port".equals(lowercaseName))
              {
                if (cookie != null) {
                  cookie.setComment("$port=" + value);
                }
              } else if ("$version".equals(lowercaseName))
              {
                version = Integer.parseInt(value);
              }
            }
            else
            {
              cookie = new Cookie(name, value);
              if (version > 0)
                cookie.setVersion(version);
              cookies.add(cookie);
            }
          }
          catch (Exception e)
          {
            LOG.debug(e);
          }
          
          name = null;
          value = null;
        }
      }
    }
    
    _cookies = ((Cookie[])cookies.toArray(new Cookie[cookies.size()]));
    _lastCookies = _cookies;
  }
}
