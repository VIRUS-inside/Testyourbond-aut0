package org.seleniumhq.jetty9.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;





























public class URIUtil
  implements Cloneable
{
  private static final Logger LOG = Log.getLogger(URIUtil.class);
  public static final String SLASH = "/";
  public static final String HTTP = "http";
  public static final String HTTPS = "https";
  private static final Pattern __PATH_SPLIT = Pattern.compile("(?<=\\/)");
  

  public static final Charset __CHARSET = StandardCharsets.UTF_8;
  




  private URIUtil() {}
  




  public static String encodePath(String path)
  {
    if ((path == null) || (path.length() == 0)) {
      return path;
    }
    StringBuilder buf = encodePath(null, path, 0);
    return buf == null ? path : buf.toString();
  }
  






  public static StringBuilder encodePath(StringBuilder buf, String path)
  {
    return encodePath(buf, path, 0);
  }
  






  private static StringBuilder encodePath(StringBuilder buf, String path, int offset)
  {
    byte[] bytes = null;
    if (buf == null)
    {
      for (int i = offset; i < path.length(); i++)
      {
        char c = path.charAt(i);
        switch (c)
        {
        case ' ': 
        case '"': 
        case '#': 
        case '%': 
        case '\'': 
        case ';': 
        case '<': 
        case '>': 
        case '?': 
        case '[': 
        case '\\': 
        case ']': 
        case '^': 
        case '`': 
        case '{': 
        case '|': 
        case '}': 
          buf = new StringBuilder(path.length() * 2);
          break;
        default: 
          if (c > '')
          {
            bytes = path.getBytes(__CHARSET);
            buf = new StringBuilder(path.length() * 2);
            break;
          }
          break; }
      }
      if (buf == null) {
        return null;
      }
    }
    

    for (int i = offset; i < path.length(); i++)
    {
      char c = path.charAt(i);
      switch (c)
      {
      case '%': 
        buf.append("%25");
        break;
      case '?': 
        buf.append("%3F");
        break;
      case ';': 
        buf.append("%3B");
        break;
      case '#': 
        buf.append("%23");
        break;
      case '"': 
        buf.append("%22");
        break;
      case '\'': 
        buf.append("%27");
        break;
      case '<': 
        buf.append("%3C");
        break;
      case '>': 
        buf.append("%3E");
        break;
      case ' ': 
        buf.append("%20");
        break;
      case '[': 
        buf.append("%5B");
        break;
      case '\\': 
        buf.append("%5C");
        break;
      case ']': 
        buf.append("%5D");
        break;
      case '^': 
        buf.append("%5E");
        break;
      case '`': 
        buf.append("%60");
        break;
      case '{': 
        buf.append("%7B");
        break;
      case '|': 
        buf.append("%7C");
        break;
      case '}': 
        buf.append("%7D");
        break;
      
      default: 
        if (c > '')
        {
          bytes = path.getBytes(__CHARSET);
          break label605;
        }
        buf.append(c);
      }
    }
    label605:
    if (bytes != null)
      for (; 
          i < bytes.length; i++)
      {
        byte c = bytes[i];
        switch (c)
        {
        case 37: 
          buf.append("%25");
          break;
        case 63: 
          buf.append("%3F");
          break;
        case 59: 
          buf.append("%3B");
          break;
        case 35: 
          buf.append("%23");
          break;
        case 34: 
          buf.append("%22");
          break;
        case 39: 
          buf.append("%27");
          break;
        case 60: 
          buf.append("%3C");
          break;
        case 62: 
          buf.append("%3E");
          break;
        case 32: 
          buf.append("%20");
          break;
        case 91: 
          buf.append("%5B");
          break;
        case 92: 
          buf.append("%5C");
          break;
        case 93: 
          buf.append("%5D");
          break;
        case 94: 
          buf.append("%5E");
          break;
        case 96: 
          buf.append("%60");
          break;
        case 123: 
          buf.append("%7B");
          break;
        case 124: 
          buf.append("%7C");
          break;
        case 125: 
          buf.append("%7D");
          break;
        default: 
          if (c < 0)
          {
            buf.append('%');
            TypeUtil.toHex(c, buf);
          }
          else {
            buf.append((char)c);
          }
          break;
        }
      }
    return buf;
  }
  









  public static StringBuilder encodeString(StringBuilder buf, String path, String encode)
  {
    if (buf == null)
    {
      for (int i = 0; i < path.length(); i++)
      {
        char c = path.charAt(i);
        if ((c == '%') || (encode.indexOf(c) >= 0))
        {
          buf = new StringBuilder(path.length() << 1);
          break;
        }
      }
      if (buf == null) {
        return null;
      }
    }
    for (int i = 0; i < path.length(); i++)
    {
      char c = path.charAt(i);
      if ((c == '%') || (encode.indexOf(c) >= 0))
      {
        buf.append('%');
        StringUtil.append(buf, (byte)(0xFF & c), 16);
      }
      else {
        buf.append(c);
      }
    }
    return buf;
  }
  



  public static String decodePath(String path)
  {
    return decodePath(path, 0, path.length());
  }
  



  public static String decodePath(String path, int offset, int length)
  {
    try
    {
      Utf8StringBuilder builder = null;
      int end = offset + length;
      for (int i = offset; i < end; i++)
      {
        char c = path.charAt(i);
        switch (c)
        {
        case '%': 
          if (builder == null)
          {
            builder = new Utf8StringBuilder(path.length());
            builder.append(path, offset, i - offset);
          }
          if (i + 2 < end)
          {
            char u = path.charAt(i + 1);
            if (u == 'u')
            {

              builder.append((char)(0xFFFF & TypeUtil.parseInt(path, i + 2, 4, 16)));
              i += 5;
            }
            else
            {
              builder.append((byte)(0xFF & TypeUtil.convertHexDigit(u) * 16 + TypeUtil.convertHexDigit(path.charAt(i + 2))));
              i += 2;
            }
          }
          else
          {
            throw new IllegalArgumentException("Bad URI % encoding");
          }
          

          break;
        case ';': 
          if (builder == null)
          {
            builder = new Utf8StringBuilder(path.length());
            builder.append(path, offset, i - offset);
          }
          do {
            i++; if (i >= end)
              break;
          } while (path.charAt(i) != '/');
          
          builder.append('/');
          break;
        




        default: 
          if (builder != null) {
            builder.append(c);
          }
          break;
        }
      }
      if (builder != null)
        return builder.toString();
      if ((offset == 0) && (length == path.length()))
        return path;
      return path.substring(offset, end);
    }
    catch (Utf8Appendable.NotUtf8Exception e)
    {
      LOG.warn(path.substring(offset, offset + length) + " " + e, new Object[0]);
      LOG.debug(e); }
    return decodeISO88591Path(path, offset, length);
  }
  





  private static String decodeISO88591Path(String path, int offset, int length)
  {
    StringBuilder builder = null;
    int end = offset + length;
    for (int i = offset; i < end; i++)
    {
      char c = path.charAt(i);
      switch (c)
      {
      case '%': 
        if (builder == null)
        {
          builder = new StringBuilder(path.length());
          builder.append(path, offset, i - offset);
        }
        if (i + 2 < end)
        {
          char u = path.charAt(i + 1);
          if (u == 'u')
          {

            builder.append((char)(0xFFFF & TypeUtil.parseInt(path, i + 2, 4, 16)));
            i += 5;
          }
          else
          {
            builder.append((byte)(0xFF & TypeUtil.convertHexDigit(u) * 16 + TypeUtil.convertHexDigit(path.charAt(i + 2))));
            i += 2;
          }
        }
        else
        {
          throw new IllegalArgumentException();
        }
        

        break;
      case ';': 
        if (builder == null)
        {
          builder = new StringBuilder(path.length());
          builder.append(path, offset, i - offset);
        }
        do { i++; if (i >= end)
            break;
        } while (path.charAt(i) != '/');
        
        builder.append('/');
        break;
      




      default: 
        if (builder != null) {
          builder.append(c);
        }
        break;
      }
    }
    if (builder != null)
      return builder.toString();
    if ((offset == 0) && (length == path.length()))
      return path;
    return path.substring(offset, end);
  }
  









  public static String addPaths(String p1, String p2)
  {
    if ((p1 == null) || (p1.length() == 0))
    {
      if ((p1 != null) && (p2 == null))
        return p1;
      return p2;
    }
    if ((p2 == null) || (p2.length() == 0)) {
      return p1;
    }
    int split = p1.indexOf(';');
    if (split < 0)
      split = p1.indexOf('?');
    if (split == 0)
      return p2 + p1;
    if (split < 0) {
      split = p1.length();
    }
    StringBuilder buf = new StringBuilder(p1.length() + p2.length() + 2);
    buf.append(p1);
    
    if (buf.charAt(split - 1) == '/')
    {
      if (p2.startsWith("/"))
      {
        buf.deleteCharAt(split - 1);
        buf.insert(split - 1, p2);
      }
      else {
        buf.insert(split, p2);
      }
      
    }
    else if (p2.startsWith("/")) {
      buf.insert(split, p2);
    }
    else {
      buf.insert(split, '/');
      buf.insert(split + 1, p2);
    }
    

    return buf.toString();
  }
  






  public static String parentPath(String p)
  {
    if ((p == null) || ("/".equals(p)))
      return null;
    int slash = p.lastIndexOf('/', p.length() - 2);
    if (slash >= 0)
      return p.substring(0, slash + 1);
    return null;
  }
  







  public static String canonicalPath(String path)
  {
    if ((path == null) || (path.isEmpty()) || (!path.contains("."))) {
      return path;
    }
    if (path.startsWith("/..")) {
      return null;
    }
    List<String> directories = new ArrayList();
    Collections.addAll(directories, __PATH_SPLIT.split(path));
    
    for (ListIterator<String> iterator = directories.listIterator(); iterator.hasNext();)
    {
      switch ((String)iterator.next()) {
      case ".": 
      case "./": 
        if ((!iterator.hasNext()) || (!((String)directories.get(iterator.nextIndex())).equals("/")))
        {

          iterator.remove(); }
        break;
      case "../": 
      case "..": 
        if (iterator.previousIndex() == 0) {
          return null;
        }
        iterator.remove();
        if ((((String)iterator.previous()).equals("/")) && (iterator.nextIndex() == 0)) {
          return null;
        }
        iterator.remove();
      }
      
    }
    
    return String.join("", directories);
  }
  






  public static String compactPath(String path)
  {
    if ((path == null) || (path.length() == 0)) {
      return path;
    }
    int state = 0;
    int end = path.length();
    int i = 0;
    

    while (i < end)
    {
      char c = path.charAt(i);
      switch (c)
      {
      case '?': 
        return path;
      case '/': 
        state++;
        if (state != 2) break;
        break;
      
      default: 
        state = 0;
      }
      i++;
    }
    
    if (state < 2) {
      return path;
    }
    StringBuilder buf = new StringBuilder(path.length());
    buf.append(path, 0, i);
    

    while (i < end)
    {
      char c = path.charAt(i);
      switch (c)
      {
      case '?': 
        buf.append(path, i, end);
        break;
      case '/': 
        if (state++ == 0)
          buf.append(c);
        break;
      default: 
        state = 0;
        buf.append(c);
      }
      i++;
    }
    
    return buf.toString();
  }
  





  public static boolean hasScheme(String uri)
  {
    for (int i = 0; i < uri.length(); i++)
    {
      char c = uri.charAt(i);
      if (c == ':')
        return true;
      if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && ((i <= 0) || (((c < '0') || (c > '9')) && (c != '.') && (c != '+') && (c != '-')))) {
        break;
      }
    }
    




    return false;
  }
  










  public static String newURI(String scheme, String server, int port, String path, String query)
  {
    StringBuilder builder = newURIBuilder(scheme, server, port);
    builder.append(path);
    if ((query != null) && (query.length() > 0))
      builder.append('?').append(query);
    return builder.toString();
  }
  








  public static StringBuilder newURIBuilder(String scheme, String server, int port)
  {
    StringBuilder builder = new StringBuilder();
    appendSchemeHostPort(builder, scheme, server, port);
    return builder;
  }
  








  public static void appendSchemeHostPort(StringBuilder url, String scheme, String server, int port)
  {
    url.append(scheme).append("://").append(HostPort.normalizeHost(server));
    
    if (port > 0)
    {
      switch (scheme)
      {
      case "http": 
        if (port != 80) {
          url.append(':').append(port);
        }
        break;
      case "https": 
        if (port != 443) {
          url.append(':').append(port);
        }
        break;
      default: 
        url.append(':').append(port);
      }
      
    }
  }
  







  public static void appendSchemeHostPort(StringBuffer url, String scheme, String server, int port)
  {
    synchronized (url)
    {
      url.append(scheme).append("://").append(HostPort.normalizeHost(server));
      
      if (port > 0)
      {
        switch (scheme)
        {
        case "http": 
          if (port != 80) {
            url.append(':').append(port);
          }
          break;
        case "https": 
          if (port != 443) {
            url.append(':').append(port);
          }
          break;
        default: 
          url.append(':').append(port);
        }
      }
    }
  }
  
  public static boolean equalsIgnoreEncodings(String uriA, String uriB)
  {
    int lenA = uriA.length();
    int lenB = uriB.length();
    int a = 0;
    int b = 0;
    
    while ((a < lenA) && (b < lenB))
    {
      int oa = uriA.charAt(a++);
      int ca = oa;
      if (ca == 37) {
        ca = TypeUtil.convertHexDigit(uriA.charAt(a++)) * 16 + TypeUtil.convertHexDigit(uriA.charAt(a++));
      }
      int ob = uriB.charAt(b++);
      int cb = ob;
      if (cb == 37) {
        cb = TypeUtil.convertHexDigit(uriB.charAt(b++)) * 16 + TypeUtil.convertHexDigit(uriB.charAt(b++));
      }
      if ((ca == 47) && (oa != ob)) {
        return false;
      }
      if (ca != cb)
        return decodePath(uriA).equals(decodePath(uriB));
    }
    return (a == lenA) && (b == lenB);
  }
  
  public static boolean equalsIgnoreEncodings(URI uriA, URI uriB)
  {
    if (uriA.equals(uriB)) {
      return true;
    }
    if (uriA.getScheme() == null)
    {
      if (uriB.getScheme() != null) {
        return false;
      }
    } else if (!uriA.getScheme().equals(uriB.getScheme())) {
      return false;
    }
    if (uriA.getAuthority() == null)
    {
      if (uriB.getAuthority() != null) {
        return false;
      }
    } else if (!uriA.getAuthority().equals(uriB.getAuthority())) {
      return false;
    }
    return equalsIgnoreEncodings(uriA.getPath(), uriB.getPath());
  }
  
  public static URI addDecodedPath(URI uri, String path)
  {
    String base = uri.toASCIIString();
    StringBuilder buf = new StringBuilder(base.length() + path.length() * 3);
    buf.append(base);
    if (buf.charAt(base.length() - 1) != '/') {
      buf.append('/');
    }
    byte[] bytes = null;
    int offset = path.charAt(0) == '/' ? 1 : 0;
    encodePath(buf, path, offset);
    
    return URI.create(buf.toString());
  }
  
  public static URI getJarSource(URI uri)
  {
    try
    {
      if (!"jar".equals(uri.getScheme()))
        return uri;
      String s = uri.getSchemeSpecificPart();
      int bang_slash = s.indexOf("!/");
      if (bang_slash >= 0)
        s = s.substring(0, bang_slash);
      return new URI(s);
    }
    catch (URISyntaxException e)
    {
      throw new IllegalArgumentException(e);
    }
  }
  
  public static String getJarSource(String uri)
  {
    if (!uri.startsWith("jar:"))
      return uri;
    int bang_slash = uri.indexOf("!/");
    return bang_slash >= 0 ? uri.substring(4, bang_slash) : uri.substring(4);
  }
}
