package org.apache.xml.utils;

import java.io.IOException;
import java.io.Serializable;
import org.apache.xml.res.XMLMessages;



































































public class URI
  implements Serializable
{
  static final long serialVersionUID = 7096266377907081897L;
  private static final String RESERVED_CHARACTERS = ";/?:@&=+$,";
  private static final String MARK_CHARACTERS = "-_.!~*'() ";
  private static final String SCHEME_CHARACTERS = "+-.";
  private static final String USERINFO_CHARACTERS = ";:&=+$,";
  public URI() {}
  
  public static class MalformedURIException
    extends IOException
  {
    public MalformedURIException() {}
    
    public MalformedURIException(String p_msg)
    {
      super();
    }
  }
  




















  private String m_scheme = null;
  


  private String m_userinfo = null;
  


  private String m_host = null;
  


  private int m_port = -1;
  


  private String m_path = null;
  





  private String m_queryString = null;
  


  private String m_fragment = null;
  

  private static boolean DEBUG = false;
  











  public URI(URI p_other)
  {
    initialize(p_other);
  }
  














  public URI(String p_uriSpec)
    throws URI.MalformedURIException
  {
    this((URI)null, p_uriSpec);
  }
  











  public URI(URI p_base, String p_uriSpec)
    throws URI.MalformedURIException
  {
    initialize(p_base, p_uriSpec);
  }
  













  public URI(String p_scheme, String p_schemeSpecificPart)
    throws URI.MalformedURIException
  {
    if ((p_scheme == null) || (p_scheme.trim().length() == 0))
    {
      throw new MalformedURIException("Cannot construct URI with null/empty scheme!");
    }
    

    if ((p_schemeSpecificPart == null) || (p_schemeSpecificPart.trim().length() == 0))
    {

      throw new MalformedURIException("Cannot construct URI with null/empty scheme-specific part!");
    }
    

    setScheme(p_scheme);
    setPath(p_schemeSpecificPart);
  }
  





















  public URI(String p_scheme, String p_host, String p_path, String p_queryString, String p_fragment)
    throws URI.MalformedURIException
  {
    this(p_scheme, null, p_host, -1, p_path, p_queryString, p_fragment);
  }
  


























  public URI(String p_scheme, String p_userinfo, String p_host, int p_port, String p_path, String p_queryString, String p_fragment)
    throws URI.MalformedURIException
  {
    if ((p_scheme == null) || (p_scheme.trim().length() == 0))
    {
      throw new MalformedURIException(XMLMessages.createXMLMessage("ER_SCHEME_REQUIRED", null));
    }
    
    if (p_host == null)
    {
      if (p_userinfo != null)
      {
        throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_USERINFO_IF_NO_HOST", null));
      }
      

      if (p_port != -1)
      {
        throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_PORT_IF_NO_HOST", null));
      }
    }
    

    if (p_path != null)
    {
      if ((p_path.indexOf('?') != -1) && (p_queryString != null))
      {
        throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_QUERY_STRING_IN_PATH", null));
      }
      

      if ((p_path.indexOf('#') != -1) && (p_fragment != null))
      {
        throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_FRAGMENT_STRING_IN_PATH", null));
      }
    }
    

    setScheme(p_scheme);
    setHost(p_host);
    setPort(p_port);
    setUserinfo(p_userinfo);
    setPath(p_path);
    setQueryString(p_queryString);
    setFragment(p_fragment);
  }
  






  private void initialize(URI p_other)
  {
    m_scheme = p_other.getScheme();
    m_userinfo = p_other.getUserinfo();
    m_host = p_other.getHost();
    m_port = p_other.getPort();
    m_path = p_other.getPath();
    m_queryString = p_other.getQueryString();
    m_fragment = p_other.getFragment();
  }
  

















  private void initialize(URI p_base, String p_uriSpec)
    throws URI.MalformedURIException
  {
    if ((p_base == null) && ((p_uriSpec == null) || (p_uriSpec.trim().length() == 0)))
    {

      throw new MalformedURIException(XMLMessages.createXMLMessage("ER_CANNOT_INIT_URI_EMPTY_PARMS", null));
    }
    


    if ((p_uriSpec == null) || (p_uriSpec.trim().length() == 0))
    {
      initialize(p_base);
      
      return;
    }
    
    String uriSpec = p_uriSpec.trim();
    int uriSpecLen = uriSpec.length();
    int index = 0;
    

    int colonIndex = uriSpec.indexOf(':');
    if (colonIndex < 0)
    {
      if (p_base == null)
      {
        throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_SCHEME_IN_URI", new Object[] { uriSpec }));
      }
    }
    else
    {
      initializeScheme(uriSpec);
      uriSpec = uriSpec.substring(colonIndex + 1);
      
      if ((m_scheme != null) && (p_base != null))
      {














        if ((uriSpec.startsWith("/")) || (!m_scheme.equals(m_scheme)) || (!p_base.getSchemeSpecificPart().startsWith("/")))
        {
          p_base = null;
        }
      }
      
      uriSpecLen = uriSpec.length();
    }
    

    if (uriSpec.startsWith("//"))
    {
      index += 2;
      
      int startPos = index;
      

      char testChar = '\000';
      
      while (index < uriSpecLen)
      {
        testChar = uriSpec.charAt(index);
        
        if ((testChar == '/') || (testChar == '?') || (testChar == '#')) {
          break;
        }
        

        index++;
      }
      


      if (index > startPos)
      {
        initializeAuthority(uriSpec.substring(startPos, index));
      }
      else
      {
        m_host = "";
      }
    }
    
    initializePath(uriSpec.substring(index));
    





    if (p_base != null)
    {








      if ((m_path.length() == 0) && (m_scheme == null) && (m_host == null))
      {
        m_scheme = p_base.getScheme();
        m_userinfo = p_base.getUserinfo();
        m_host = p_base.getHost();
        m_port = p_base.getPort();
        m_path = p_base.getPath();
        
        if (m_queryString == null)
        {
          m_queryString = p_base.getQueryString();
        }
        
        return;
      }
      


      if (m_scheme == null)
      {
        m_scheme = p_base.getScheme();
      }
      


      if (m_host == null)
      {
        m_userinfo = p_base.getUserinfo();
        m_host = p_base.getHost();
        m_port = p_base.getPort();
      }
      else
      {
        return;
      }
      

      if ((m_path.length() > 0) && (m_path.startsWith("/")))
      {
        return;
      }
      


      String path = new String();
      String basePath = p_base.getPath();
      

      if (basePath != null)
      {
        int lastSlash = basePath.lastIndexOf('/');
        
        if (lastSlash != -1)
        {
          path = basePath.substring(0, lastSlash + 1);
        }
      }
      

      path = path.concat(m_path);
      

      index = -1;
      
      while ((index = path.indexOf("/./")) != -1)
      {
        path = path.substring(0, index + 1).concat(path.substring(index + 3));
      }
      

      if (path.endsWith("/."))
      {
        path = path.substring(0, path.length() - 1);
      }
      


      index = -1;
      
      int segIndex = -1;
      String tempString = null;
      
      while ((index = path.indexOf("/../")) > 0)
      {
        tempString = path.substring(0, path.indexOf("/../"));
        segIndex = tempString.lastIndexOf('/');
        
        if ((segIndex != -1) && 
        
          (!tempString.substring(segIndex++).equals("..")))
        {
          path = path.substring(0, segIndex).concat(path.substring(index + 4));
        }
      }
      




      if (path.endsWith("/.."))
      {
        tempString = path.substring(0, path.length() - 3);
        segIndex = tempString.lastIndexOf('/');
        
        if (segIndex != -1)
        {
          path = path.substring(0, segIndex + 1);
        }
      }
      
      m_path = path;
    }
  }
  








  private void initializeScheme(String p_uriSpec)
    throws URI.MalformedURIException
  {
    int uriSpecLen = p_uriSpec.length();
    int index = 0;
    String scheme = null;
    char testChar = '\000';
    
    while (index < uriSpecLen)
    {
      testChar = p_uriSpec.charAt(index);
      
      if ((testChar == ':') || (testChar == '/') || (testChar == '?') || (testChar == '#')) {
        break;
      }
      


      index++;
    }
    
    scheme = p_uriSpec.substring(0, index);
    
    if (scheme.length() == 0)
    {
      throw new MalformedURIException(XMLMessages.createXMLMessage("ER_NO_SCHEME_INURI", null));
    }
    

    setScheme(scheme);
  }
  










  private void initializeAuthority(String p_uriSpec)
    throws URI.MalformedURIException
  {
    int index = 0;
    int start = 0;
    int end = p_uriSpec.length();
    char testChar = '\000';
    String userinfo = null;
    

    if (p_uriSpec.indexOf('@', start) != -1)
    {
      while (index < end)
      {
        testChar = p_uriSpec.charAt(index);
        
        if (testChar == '@') {
          break;
        }
        

        index++;
      }
      
      userinfo = p_uriSpec.substring(start, index);
      
      index++;
    }
    

    String host = null;
    
    start = index;
    
    while (index < end)
    {
      testChar = p_uriSpec.charAt(index);
      
      if (testChar == ':') {
        break;
      }
      

      index++;
    }
    
    host = p_uriSpec.substring(start, index);
    
    int port = -1;
    
    if (host.length() > 0)
    {


      if (testChar == ':')
      {
        index++;
        
        start = index;
        
        while (index < end)
        {
          index++;
        }
        
        String portStr = p_uriSpec.substring(start, index);
        
        if (portStr.length() > 0)
        {
          for (int i = 0; i < portStr.length(); i++)
          {
            if (!isDigit(portStr.charAt(i)))
            {
              throw new MalformedURIException(portStr + " is invalid. Port should only contain digits!");
            }
          }
          

          try
          {
            port = Integer.parseInt(portStr);
          }
          catch (NumberFormatException nfe) {}
        }
      }
    }
    




    setHost(host);
    setPort(port);
    setUserinfo(userinfo);
  }
  







  private void initializePath(String p_uriSpec)
    throws URI.MalformedURIException
  {
    if (p_uriSpec == null)
    {
      throw new MalformedURIException("Cannot initialize path from null string!");
    }
    

    int index = 0;
    int start = 0;
    int end = p_uriSpec.length();
    char testChar = '\000';
    

    while (index < end)
    {
      testChar = p_uriSpec.charAt(index);
      
      if ((testChar == '?') || (testChar == '#')) {
        break;
      }
      


      if (testChar == '%')
      {
        if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
        {

          throw new MalformedURIException(XMLMessages.createXMLMessage("ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", null));
        }
        
      }
      else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
      {

        if ('\\' != testChar) {
          throw new MalformedURIException(XMLMessages.createXMLMessage("ER_PATH_INVALID_CHAR", new Object[] { String.valueOf(testChar) }));
        }
      }
      
      index++;
    }
    
    m_path = p_uriSpec.substring(start, index);
    

    if (testChar == '?')
    {
      index++;
      
      start = index;
      
      while (index < end)
      {
        testChar = p_uriSpec.charAt(index);
        
        if (testChar == '#') {
          break;
        }
        

        if (testChar == '%')
        {
          if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
          {

            throw new MalformedURIException("Query string contains invalid escape sequence!");
          }
          
        }
        else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
        {

          throw new MalformedURIException("Query string contains invalid character:" + testChar);
        }
        

        index++;
      }
      
      m_queryString = p_uriSpec.substring(start, index);
    }
    

    if (testChar == '#')
    {
      index++;
      
      start = index;
      
      while (index < end)
      {
        testChar = p_uriSpec.charAt(index);
        
        if (testChar == '%')
        {
          if ((index + 2 >= end) || (!isHex(p_uriSpec.charAt(index + 1))) || (!isHex(p_uriSpec.charAt(index + 2))))
          {

            throw new MalformedURIException("Fragment contains invalid escape sequence!");
          }
          
        }
        else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
        {

          throw new MalformedURIException("Fragment contains invalid character:" + testChar);
        }
        

        index++;
      }
      
      m_fragment = p_uriSpec.substring(start, index);
    }
  }
  





  public String getScheme()
  {
    return m_scheme;
  }
  







  public String getSchemeSpecificPart()
  {
    StringBuffer schemespec = new StringBuffer();
    
    if ((m_userinfo != null) || (m_host != null) || (m_port != -1))
    {
      schemespec.append("//");
    }
    
    if (m_userinfo != null)
    {
      schemespec.append(m_userinfo);
      schemespec.append('@');
    }
    
    if (m_host != null)
    {
      schemespec.append(m_host);
    }
    
    if (m_port != -1)
    {
      schemespec.append(':');
      schemespec.append(m_port);
    }
    
    if (m_path != null)
    {
      schemespec.append(m_path);
    }
    
    if (m_queryString != null)
    {
      schemespec.append('?');
      schemespec.append(m_queryString);
    }
    
    if (m_fragment != null)
    {
      schemespec.append('#');
      schemespec.append(m_fragment);
    }
    
    return schemespec.toString();
  }
  





  public String getUserinfo()
  {
    return m_userinfo;
  }
  





  public String getHost()
  {
    return m_host;
  }
  





  public int getPort()
  {
    return m_port;
  }
  
















  public String getPath(boolean p_includeQueryString, boolean p_includeFragment)
  {
    StringBuffer pathString = new StringBuffer(m_path);
    
    if ((p_includeQueryString) && (m_queryString != null))
    {
      pathString.append('?');
      pathString.append(m_queryString);
    }
    
    if ((p_includeFragment) && (m_fragment != null))
    {
      pathString.append('#');
      pathString.append(m_fragment);
    }
    
    return pathString.toString();
  }
  






  public String getPath()
  {
    return m_path;
  }
  







  public String getQueryString()
  {
    return m_queryString;
  }
  







  public String getFragment()
  {
    return m_fragment;
  }
  









  public void setScheme(String p_scheme)
    throws URI.MalformedURIException
  {
    if (p_scheme == null)
    {
      throw new MalformedURIException(XMLMessages.createXMLMessage("ER_SCHEME_FROM_NULL_STRING", null));
    }
    
    if (!isConformantSchemeName(p_scheme))
    {
      throw new MalformedURIException(XMLMessages.createXMLMessage("ER_SCHEME_NOT_CONFORMANT", null));
    }
    
    m_scheme = p_scheme.toLowerCase();
  }
  









  public void setUserinfo(String p_userinfo)
    throws URI.MalformedURIException
  {
    if (p_userinfo == null)
    {
      m_userinfo = null;
    }
    else
    {
      if (m_host == null)
      {
        throw new MalformedURIException("Userinfo cannot be set when host is null!");
      }
      



      int index = 0;
      int end = p_userinfo.length();
      char testChar = '\000';
      
      while (index < end)
      {
        testChar = p_userinfo.charAt(index);
        
        if (testChar == '%')
        {
          if ((index + 2 >= end) || (!isHex(p_userinfo.charAt(index + 1))) || (!isHex(p_userinfo.charAt(index + 2))))
          {

            throw new MalformedURIException("Userinfo contains invalid escape sequence!");
          }
          
        }
        else if ((!isUnreservedCharacter(testChar)) && (";:&=+$,".indexOf(testChar) == -1))
        {

          throw new MalformedURIException("Userinfo contains invalid character:" + testChar);
        }
        

        index++;
      }
    }
    
    m_userinfo = p_userinfo;
  }
  









  public void setHost(String p_host)
    throws URI.MalformedURIException
  {
    if ((p_host == null) || (p_host.trim().length() == 0))
    {
      m_host = p_host;
      m_userinfo = null;
      m_port = -1;
    }
    else if (!isWellFormedAddress(p_host))
    {
      throw new MalformedURIException(XMLMessages.createXMLMessage("ER_HOST_ADDRESS_NOT_WELLFORMED", null));
    }
    
    m_host = p_host;
  }
  











  public void setPort(int p_port)
    throws URI.MalformedURIException
  {
    if ((p_port >= 0) && (p_port <= 65535))
    {
      if (m_host == null)
      {
        throw new MalformedURIException(XMLMessages.createXMLMessage("ER_PORT_WHEN_HOST_NULL", null));
      }
      
    }
    else if (p_port != -1)
    {
      throw new MalformedURIException(XMLMessages.createXMLMessage("ER_INVALID_PORT", null));
    }
    
    m_port = p_port;
  }
  














  public void setPath(String p_path)
    throws URI.MalformedURIException
  {
    if (p_path == null)
    {
      m_path = null;
      m_queryString = null;
      m_fragment = null;
    }
    else
    {
      initializePath(p_path);
    }
  }
  













  public void appendPath(String p_addToPath)
    throws URI.MalformedURIException
  {
    if ((p_addToPath == null) || (p_addToPath.trim().length() == 0))
    {
      return;
    }
    
    if (!isURIString(p_addToPath))
    {
      throw new MalformedURIException(XMLMessages.createXMLMessage("ER_PATH_INVALID_CHAR", new Object[] { p_addToPath }));
    }
    
    if ((m_path == null) || (m_path.trim().length() == 0))
    {
      if (p_addToPath.startsWith("/"))
      {
        m_path = p_addToPath;
      }
      else
      {
        m_path = ("/" + p_addToPath);
      }
    }
    else if (m_path.endsWith("/"))
    {
      if (p_addToPath.startsWith("/"))
      {
        m_path = m_path.concat(p_addToPath.substring(1));
      }
      else
      {
        m_path = m_path.concat(p_addToPath);
      }
      

    }
    else if (p_addToPath.startsWith("/"))
    {
      m_path = m_path.concat(p_addToPath);
    }
    else
    {
      m_path = m_path.concat("/" + p_addToPath);
    }
  }
  













  public void setQueryString(String p_queryString)
    throws URI.MalformedURIException
  {
    if (p_queryString == null)
    {
      m_queryString = null;
    } else {
      if (!isGenericURI())
      {
        throw new MalformedURIException("Query string can only be set for a generic URI!");
      }
      
      if (getPath() == null)
      {
        throw new MalformedURIException("Query string cannot be set when path is null!");
      }
      
      if (!isURIString(p_queryString))
      {
        throw new MalformedURIException("Query string contains invalid character!");
      }
      


      m_queryString = p_queryString;
    }
  }
  











  public void setFragment(String p_fragment)
    throws URI.MalformedURIException
  {
    if (p_fragment == null)
    {
      m_fragment = null;
    } else {
      if (!isGenericURI())
      {
        throw new MalformedURIException(XMLMessages.createXMLMessage("ER_FRAG_FOR_GENERIC_URI", null));
      }
      
      if (getPath() == null)
      {
        throw new MalformedURIException(XMLMessages.createXMLMessage("ER_FRAG_WHEN_PATH_NULL", null));
      }
      
      if (!isURIString(p_fragment))
      {
        throw new MalformedURIException(XMLMessages.createXMLMessage("ER_FRAG_INVALID_CHAR", null));
      }
      

      m_fragment = p_fragment;
    }
  }
  









  public boolean equals(Object p_test)
  {
    if ((p_test instanceof URI))
    {
      URI testURI = (URI)p_test;
      
      if (((m_scheme == null) && (m_scheme == null)) || ((m_scheme != null) && (m_scheme != null) && (m_scheme.equals(m_scheme)) && (((m_userinfo == null) && (m_userinfo == null)) || ((m_userinfo != null) && (m_userinfo != null) && (m_userinfo.equals(m_userinfo)) && (((m_host == null) && (m_host == null)) || ((m_host != null) && (m_host != null) && (m_host.equals(m_host)) && (m_port == m_port) && (((m_path == null) && (m_path == null)) || ((m_path != null) && (m_path != null) && (m_path.equals(m_path)) && (((m_queryString == null) && (m_queryString == null)) || ((m_queryString != null) && (m_queryString != null) && (m_queryString.equals(m_queryString)) && (((m_fragment == null) && (m_fragment == null)) || ((m_fragment != null) && (m_fragment != null) && (m_fragment.equals(m_fragment))))))))))))))
      {






        return true;
      }
    }
    
    return false;
  }
  






  public String toString()
  {
    StringBuffer uriSpecString = new StringBuffer();
    
    if (m_scheme != null)
    {
      uriSpecString.append(m_scheme);
      uriSpecString.append(':');
    }
    
    uriSpecString.append(getSchemeSpecificPart());
    
    return uriSpecString.toString();
  }
  










  public boolean isGenericURI()
  {
    return m_host != null;
  }
  










  public static boolean isConformantSchemeName(String p_scheme)
  {
    if ((p_scheme == null) || (p_scheme.trim().length() == 0))
    {
      return false;
    }
    
    if (!isAlpha(p_scheme.charAt(0)))
    {
      return false;
    }
    


    for (int i = 1; i < p_scheme.length(); i++)
    {
      char testChar = p_scheme.charAt(i);
      
      if ((!isAlphanum(testChar)) && ("+-.".indexOf(testChar) == -1))
      {
        return false;
      }
    }
    
    return true;
  }
  














  public static boolean isWellFormedAddress(String p_address)
  {
    if (p_address == null)
    {
      return false;
    }
    
    String address = p_address.trim();
    int addrLength = address.length();
    
    if ((addrLength == 0) || (addrLength > 255))
    {
      return false;
    }
    
    if ((address.startsWith(".")) || (address.startsWith("-")))
    {
      return false;
    }
    



    int index = address.lastIndexOf('.');
    
    if (address.endsWith("."))
    {
      index = address.substring(0, index).lastIndexOf('.');
    }
    
    if ((index + 1 < addrLength) && (isDigit(p_address.charAt(index + 1))))
    {

      int numDots = 0;
      



      for (int i = 0; i < addrLength; i++)
      {
        char testChar = address.charAt(i);
        
        if (testChar == '.')
        {
          if ((!isDigit(address.charAt(i - 1))) || ((i + 1 < addrLength) && (!isDigit(address.charAt(i + 1)))))
          {

            return false;
          }
          
          numDots++;
        }
        else if (!isDigit(testChar))
        {
          return false;
        }
      }
      
      if (numDots != 3)
      {
        return false;

      }
      


    }
    else
    {

      for (int i = 0; i < addrLength; i++)
      {
        char testChar = address.charAt(i);
        
        if (testChar == '.')
        {
          if (!isAlphanum(address.charAt(i - 1)))
          {
            return false;
          }
          
          if ((i + 1 < addrLength) && (!isAlphanum(address.charAt(i + 1))))
          {
            return false;
          }
        }
        else if ((!isAlphanum(testChar)) && (testChar != '-'))
        {
          return false;
        }
      }
    }
    
    return true;
  }
  







  private static boolean isDigit(char p_char)
  {
    return (p_char >= '0') && (p_char <= '9');
  }
  








  private static boolean isHex(char p_char)
  {
    return (isDigit(p_char)) || ((p_char >= 'a') && (p_char <= 'f')) || ((p_char >= 'A') && (p_char <= 'F'));
  }
  








  private static boolean isAlpha(char p_char)
  {
    return ((p_char >= 'a') && (p_char <= 'z')) || ((p_char >= 'A') && (p_char <= 'Z'));
  }
  








  private static boolean isAlphanum(char p_char)
  {
    return (isAlpha(p_char)) || (isDigit(p_char));
  }
  








  private static boolean isReservedCharacter(char p_char)
  {
    return ";/?:@&=+$,".indexOf(p_char) != -1;
  }
  







  private static boolean isUnreservedCharacter(char p_char)
  {
    return (isAlphanum(p_char)) || ("-_.!~*'() ".indexOf(p_char) != -1);
  }
  










  private static boolean isURIString(String p_uric)
  {
    if (p_uric == null)
    {
      return false;
    }
    
    int end = p_uric.length();
    char testChar = '\000';
    
    for (int i = 0; i < end; i++)
    {
      testChar = p_uric.charAt(i);
      
      if (testChar == '%')
      {
        if ((i + 2 >= end) || (!isHex(p_uric.charAt(i + 1))) || (!isHex(p_uric.charAt(i + 2))))
        {

          return false;
        }
        

        i += 2;




      }
      else if ((!isReservedCharacter(testChar)) && (!isUnreservedCharacter(testChar)))
      {




        return false;
      }
    }
    
    return true;
  }
}
