package org.apache.xerces.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

public class URI
  implements Serializable
{
  static final long serialVersionUID = 1601921774685357214L;
  private static final byte[] fgLookupTable = new byte[''];
  private static final int RESERVED_CHARACTERS = 1;
  private static final int MARK_CHARACTERS = 2;
  private static final int SCHEME_CHARACTERS = 4;
  private static final int USERINFO_CHARACTERS = 8;
  private static final int ASCII_ALPHA_CHARACTERS = 16;
  private static final int ASCII_DIGIT_CHARACTERS = 32;
  private static final int ASCII_HEX_CHARACTERS = 64;
  private static final int PATH_CHARACTERS = 128;
  private static final int MASK_ALPHA_NUMERIC = 48;
  private static final int MASK_UNRESERVED_MASK = 50;
  private static final int MASK_URI_CHARACTER = 51;
  private static final int MASK_SCHEME_CHARACTER = 52;
  private static final int MASK_USERINFO_CHARACTER = 58;
  private static final int MASK_PATH_CHARACTER = 178;
  private String m_scheme = null;
  private String m_userinfo = null;
  private String m_host = null;
  private int m_port = -1;
  private String m_regAuthority = null;
  private String m_path = null;
  private String m_queryString = null;
  private String m_fragment = null;
  private static boolean DEBUG = false;
  
  public URI() {}
  
  public URI(URI paramURI)
  {
    initialize(paramURI);
  }
  
  public URI(String paramString)
    throws URI.MalformedURIException
  {
    this((URI)null, paramString);
  }
  
  public URI(String paramString, boolean paramBoolean)
    throws URI.MalformedURIException
  {
    this((URI)null, paramString, paramBoolean);
  }
  
  public URI(URI paramURI, String paramString)
    throws URI.MalformedURIException
  {
    initialize(paramURI, paramString);
  }
  
  public URI(URI paramURI, String paramString, boolean paramBoolean)
    throws URI.MalformedURIException
  {
    initialize(paramURI, paramString, paramBoolean);
  }
  
  public URI(String paramString1, String paramString2)
    throws URI.MalformedURIException
  {
    if ((paramString1 == null) || (paramString1.trim().length() == 0)) {
      throw new MalformedURIException("Cannot construct URI with null/empty scheme!");
    }
    if ((paramString2 == null) || (paramString2.trim().length() == 0)) {
      throw new MalformedURIException("Cannot construct URI with null/empty scheme-specific part!");
    }
    setScheme(paramString1);
    setPath(paramString2);
  }
  
  public URI(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws URI.MalformedURIException
  {
    this(paramString1, null, paramString2, -1, paramString3, paramString4, paramString5);
  }
  
  public URI(String paramString1, String paramString2, String paramString3, int paramInt, String paramString4, String paramString5, String paramString6)
    throws URI.MalformedURIException
  {
    if ((paramString1 == null) || (paramString1.trim().length() == 0)) {
      throw new MalformedURIException("Scheme is required!");
    }
    if (paramString3 == null)
    {
      if (paramString2 != null) {
        throw new MalformedURIException("Userinfo may not be specified if host is not specified!");
      }
      if (paramInt != -1) {
        throw new MalformedURIException("Port may not be specified if host is not specified!");
      }
    }
    if (paramString4 != null)
    {
      if ((paramString4.indexOf('?') != -1) && (paramString5 != null)) {
        throw new MalformedURIException("Query string cannot be specified in path and query string!");
      }
      if ((paramString4.indexOf('#') != -1) && (paramString6 != null)) {
        throw new MalformedURIException("Fragment cannot be specified in both the path and fragment!");
      }
    }
    setScheme(paramString1);
    setHost(paramString3);
    setPort(paramInt);
    setUserinfo(paramString2);
    setPath(paramString4);
    setQueryString(paramString5);
    setFragment(paramString6);
  }
  
  private void initialize(URI paramURI)
  {
    m_scheme = paramURI.getScheme();
    m_userinfo = paramURI.getUserinfo();
    m_host = paramURI.getHost();
    m_port = paramURI.getPort();
    m_regAuthority = paramURI.getRegBasedAuthority();
    m_path = paramURI.getPath();
    m_queryString = paramURI.getQueryString();
    m_fragment = paramURI.getFragment();
  }
  
  private void initialize(URI paramURI, String paramString, boolean paramBoolean)
    throws URI.MalformedURIException
  {
    String str = paramString;
    int i = str != null ? str.length() : 0;
    if ((paramURI == null) && (i == 0))
    {
      if (paramBoolean)
      {
        m_path = "";
        return;
      }
      throw new MalformedURIException("Cannot initialize URI with empty parameters.");
    }
    if (i == 0)
    {
      initialize(paramURI);
      return;
    }
    int j = 0;
    int k = str.indexOf(':');
    int m;
    int n;
    if (k != -1)
    {
      m = k - 1;
      n = str.lastIndexOf('/', m);
      int i1 = str.lastIndexOf('?', m);
      int i2 = str.lastIndexOf('#', m);
      if ((k == 0) || (n != -1) || (i1 != -1) || (i2 != -1))
      {
        if ((k == 0) || ((paramURI == null) && (i2 != 0) && (!paramBoolean))) {
          throw new MalformedURIException("No scheme found in URI.");
        }
      }
      else
      {
        initializeScheme(str);
        j = m_scheme.length() + 1;
        if ((k == i - 1) || (str.charAt(k + 1) == '#')) {
          throw new MalformedURIException("Scheme specific part cannot be empty.");
        }
      }
    }
    else if ((paramURI == null) && (str.indexOf('#') != 0) && (!paramBoolean))
    {
      throw new MalformedURIException("No scheme found in URI.");
    }
    if ((j + 1 < i) && (str.charAt(j) == '/') && (str.charAt(j + 1) == '/'))
    {
      j += 2;
      m = j;
      n = 0;
      while (j < i)
      {
        n = str.charAt(j);
        if ((n == 47) || (n == 63) || (n == 35)) {
          break;
        }
        j++;
      }
      if (j > m)
      {
        if (!initializeAuthority(str.substring(m, j))) {
          j = m - 2;
        }
      }
      else {
        m_host = "";
      }
    }
    initializePath(str, j);
    if (paramURI != null) {
      absolutize(paramURI);
    }
  }
  
  private void initialize(URI paramURI, String paramString)
    throws URI.MalformedURIException
  {
    String str = paramString;
    int i = str != null ? str.length() : 0;
    if ((paramURI == null) && (i == 0)) {
      throw new MalformedURIException("Cannot initialize URI with empty parameters.");
    }
    if (i == 0)
    {
      initialize(paramURI);
      return;
    }
    int j = 0;
    int k = str.indexOf(':');
    int m;
    int n;
    if (k != -1)
    {
      m = k - 1;
      n = str.lastIndexOf('/', m);
      int i1 = str.lastIndexOf('?', m);
      int i2 = str.lastIndexOf('#', m);
      if ((k == 0) || (n != -1) || (i1 != -1) || (i2 != -1))
      {
        if ((k == 0) || ((paramURI == null) && (i2 != 0))) {
          throw new MalformedURIException("No scheme found in URI.");
        }
      }
      else
      {
        initializeScheme(str);
        j = m_scheme.length() + 1;
        if ((k == i - 1) || (str.charAt(k + 1) == '#')) {
          throw new MalformedURIException("Scheme specific part cannot be empty.");
        }
      }
    }
    else if ((paramURI == null) && (str.indexOf('#') != 0))
    {
      throw new MalformedURIException("No scheme found in URI.");
    }
    if ((j + 1 < i) && (str.charAt(j) == '/') && (str.charAt(j + 1) == '/'))
    {
      j += 2;
      m = j;
      n = 0;
      while (j < i)
      {
        n = str.charAt(j);
        if ((n == 47) || (n == 63) || (n == 35)) {
          break;
        }
        j++;
      }
      if (j > m)
      {
        if (!initializeAuthority(str.substring(m, j))) {
          j = m - 2;
        }
      }
      else {
        m_host = "";
      }
    }
    initializePath(str, j);
    if (paramURI != null) {
      absolutize(paramURI);
    }
  }
  
  public void absolutize(URI paramURI)
  {
    if ((m_path.length() == 0) && (m_scheme == null) && (m_host == null) && (m_regAuthority == null))
    {
      m_scheme = paramURI.getScheme();
      m_userinfo = paramURI.getUserinfo();
      m_host = paramURI.getHost();
      m_port = paramURI.getPort();
      m_regAuthority = paramURI.getRegBasedAuthority();
      m_path = paramURI.getPath();
      if (m_queryString == null)
      {
        m_queryString = paramURI.getQueryString();
        if (m_fragment == null) {
          m_fragment = paramURI.getFragment();
        }
      }
      return;
    }
    if (m_scheme == null) {
      m_scheme = paramURI.getScheme();
    } else {
      return;
    }
    if ((m_host == null) && (m_regAuthority == null))
    {
      m_userinfo = paramURI.getUserinfo();
      m_host = paramURI.getHost();
      m_port = paramURI.getPort();
      m_regAuthority = paramURI.getRegBasedAuthority();
    }
    else
    {
      return;
    }
    if ((m_path.length() > 0) && (m_path.startsWith("/"))) {
      return;
    }
    String str1 = "";
    String str2 = paramURI.getPath();
    if ((str2 != null) && (str2.length() > 0))
    {
      i = str2.lastIndexOf('/');
      if (i != -1) {
        str1 = str2.substring(0, i + 1);
      }
    }
    else if (m_path.length() > 0)
    {
      str1 = "/";
    }
    str1 = str1.concat(m_path);
    int i = -1;
    while ((i = str1.indexOf("/./")) != -1) {
      str1 = str1.substring(0, i + 1).concat(str1.substring(i + 3));
    }
    if (str1.endsWith("/.")) {
      str1 = str1.substring(0, str1.length() - 1);
    }
    i = 1;
    int j = -1;
    String str3 = null;
    while ((i = str1.indexOf("/../", i)) > 0)
    {
      str3 = str1.substring(0, str1.indexOf("/../"));
      j = str3.lastIndexOf('/');
      if (j != -1)
      {
        if (!str3.substring(j).equals(".."))
        {
          str1 = str1.substring(0, j + 1).concat(str1.substring(i + 4));
          i = j;
        }
        else
        {
          i += 4;
        }
      }
      else {
        i += 4;
      }
    }
    if (str1.endsWith("/.."))
    {
      str3 = str1.substring(0, str1.length() - 3);
      j = str3.lastIndexOf('/');
      if (j != -1) {
        str1 = str1.substring(0, j + 1);
      }
    }
    m_path = str1;
  }
  
  private void initializeScheme(String paramString)
    throws URI.MalformedURIException
  {
    int i = paramString.length();
    int j = 0;
    String str = null;
    int k = 0;
    while (j < i)
    {
      k = paramString.charAt(j);
      if ((k == 58) || (k == 47) || (k == 63) || (k == 35)) {
        break;
      }
      j++;
    }
    str = paramString.substring(0, j);
    if (str.length() == 0) {
      throw new MalformedURIException("No scheme found in URI.");
    }
    setScheme(str);
  }
  
  private boolean initializeAuthority(String paramString)
  {
    int i = 0;
    int j = 0;
    int k = paramString.length();
    int m = 0;
    String str1 = null;
    if (paramString.indexOf('@', j) != -1)
    {
      while (i < k)
      {
        m = paramString.charAt(i);
        if (m == 64) {
          break;
        }
        i++;
      }
      str1 = paramString.substring(j, i);
      i++;
    }
    String str2 = null;
    j = i;
    int n = 0;
    if (i < k) {
      if (paramString.charAt(j) == '[')
      {
        i1 = paramString.indexOf(']', j);
        i = i1 != -1 ? i1 : k;
        if ((i + 1 < k) && (paramString.charAt(i + 1) == ':'))
        {
          i++;
          n = 1;
        }
        else
        {
          i = k;
        }
      }
      else
      {
        i1 = paramString.lastIndexOf(':', k);
        i = i1 > j ? i1 : k;
        n = i != k ? 1 : 0;
      }
    }
    str2 = paramString.substring(j, i);
    int i1 = -1;
    if ((str2.length() > 0) && (n != 0))
    {
      i++;
      j = i;
      while (i < k) {
        i++;
      }
      String str3 = paramString.substring(j, i);
      if (str3.length() > 0) {
        try
        {
          i1 = Integer.parseInt(str3);
          if (i1 == -1) {
            i1--;
          }
        }
        catch (NumberFormatException localNumberFormatException)
        {
          i1 = -2;
        }
      }
    }
    if (isValidServerBasedAuthority(str2, i1, str1))
    {
      m_host = str2;
      m_port = i1;
      m_userinfo = str1;
      return true;
    }
    if (isValidRegistryBasedAuthority(paramString))
    {
      m_regAuthority = paramString;
      return true;
    }
    return false;
  }
  
  private boolean isValidServerBasedAuthority(String paramString1, int paramInt, String paramString2)
  {
    if (!isWellFormedAddress(paramString1)) {
      return false;
    }
    if ((paramInt < -1) || (paramInt > 65535)) {
      return false;
    }
    if (paramString2 != null)
    {
      int i = 0;
      int j = paramString2.length();
      char c = '\000';
      while (i < j)
      {
        c = paramString2.charAt(i);
        if (c == '%')
        {
          if ((i + 2 >= j) || (!isHex(paramString2.charAt(i + 1))) || (!isHex(paramString2.charAt(i + 2)))) {
            return false;
          }
          i += 2;
        }
        else if (!isUserinfoCharacter(c))
        {
          return false;
        }
        i++;
      }
    }
    return true;
  }
  
  private boolean isValidRegistryBasedAuthority(String paramString)
  {
    int i = 0;
    int j = paramString.length();
    while (i < j)
    {
      char c = paramString.charAt(i);
      if (c == '%')
      {
        if ((i + 2 >= j) || (!isHex(paramString.charAt(i + 1))) || (!isHex(paramString.charAt(i + 2)))) {
          return false;
        }
        i += 2;
      }
      else if (!isPathCharacter(c))
      {
        return false;
      }
      i++;
    }
    return true;
  }
  
  private void initializePath(String paramString, int paramInt)
    throws URI.MalformedURIException
  {
    if (paramString == null) {
      throw new MalformedURIException("Cannot initialize path from null string!");
    }
    int i = paramInt;
    int j = paramInt;
    int k = paramString.length();
    char c = '\000';
    if (j < k)
    {
      if (getScheme() != null)
      {
        if (paramString.charAt(j) != '/') {}
      }
      else
      {
        while (i < k)
        {
          c = paramString.charAt(i);
          if (c == '%')
          {
            if ((i + 2 >= k) || (!isHex(paramString.charAt(i + 1))) || (!isHex(paramString.charAt(i + 2)))) {
              throw new MalformedURIException("Path contains invalid escape sequence!");
            }
            i += 2;
          }
          else if (!isPathCharacter(c))
          {
            if ((c == '?') || (c == '#')) {
              break;
            }
            throw new MalformedURIException("Path contains invalid character: " + c);
          }
          i++;
        }
        break label311;
      }
      while (i < k)
      {
        c = paramString.charAt(i);
        if ((c == '?') || (c == '#')) {
          break;
        }
        if (c == '%')
        {
          if ((i + 2 >= k) || (!isHex(paramString.charAt(i + 1))) || (!isHex(paramString.charAt(i + 2)))) {
            throw new MalformedURIException("Opaque part contains invalid escape sequence!");
          }
          i += 2;
        }
        else if (!isURICharacter(c))
        {
          throw new MalformedURIException("Opaque part contains invalid character: " + c);
        }
        i++;
      }
    }
    label311:
    m_path = paramString.substring(j, i);
    if (c == '?')
    {
      i++;
      j = i;
      while (i < k)
      {
        c = paramString.charAt(i);
        if (c == '#') {
          break;
        }
        if (c == '%')
        {
          if ((i + 2 >= k) || (!isHex(paramString.charAt(i + 1))) || (!isHex(paramString.charAt(i + 2)))) {
            throw new MalformedURIException("Query string contains invalid escape sequence!");
          }
          i += 2;
        }
        else if (!isURICharacter(c))
        {
          throw new MalformedURIException("Query string contains invalid character: " + c);
        }
        i++;
      }
      m_queryString = paramString.substring(j, i);
    }
    if (c == '#')
    {
      i++;
      j = i;
      while (i < k)
      {
        c = paramString.charAt(i);
        if (c == '%')
        {
          if ((i + 2 >= k) || (!isHex(paramString.charAt(i + 1))) || (!isHex(paramString.charAt(i + 2)))) {
            throw new MalformedURIException("Fragment contains invalid escape sequence!");
          }
          i += 2;
        }
        else if (!isURICharacter(c))
        {
          throw new MalformedURIException("Fragment contains invalid character: " + c);
        }
        i++;
      }
      m_fragment = paramString.substring(j, i);
    }
  }
  
  public String getScheme()
  {
    return m_scheme;
  }
  
  public String getSchemeSpecificPart()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if ((m_host != null) || (m_regAuthority != null))
    {
      localStringBuffer.append("//");
      if (m_host != null)
      {
        if (m_userinfo != null)
        {
          localStringBuffer.append(m_userinfo);
          localStringBuffer.append('@');
        }
        localStringBuffer.append(m_host);
        if (m_port != -1)
        {
          localStringBuffer.append(':');
          localStringBuffer.append(m_port);
        }
      }
      else
      {
        localStringBuffer.append(m_regAuthority);
      }
    }
    if (m_path != null) {
      localStringBuffer.append(m_path);
    }
    if (m_queryString != null)
    {
      localStringBuffer.append('?');
      localStringBuffer.append(m_queryString);
    }
    if (m_fragment != null)
    {
      localStringBuffer.append('#');
      localStringBuffer.append(m_fragment);
    }
    return localStringBuffer.toString();
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
  
  public String getRegBasedAuthority()
  {
    return m_regAuthority;
  }
  
  public String getAuthority()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if ((m_host != null) || (m_regAuthority != null))
    {
      localStringBuffer.append("//");
      if (m_host != null)
      {
        if (m_userinfo != null)
        {
          localStringBuffer.append(m_userinfo);
          localStringBuffer.append('@');
        }
        localStringBuffer.append(m_host);
        if (m_port != -1)
        {
          localStringBuffer.append(':');
          localStringBuffer.append(m_port);
        }
      }
      else
      {
        localStringBuffer.append(m_regAuthority);
      }
    }
    return localStringBuffer.toString();
  }
  
  public String getPath(boolean paramBoolean1, boolean paramBoolean2)
  {
    StringBuffer localStringBuffer = new StringBuffer(m_path);
    if ((paramBoolean1) && (m_queryString != null))
    {
      localStringBuffer.append('?');
      localStringBuffer.append(m_queryString);
    }
    if ((paramBoolean2) && (m_fragment != null))
    {
      localStringBuffer.append('#');
      localStringBuffer.append(m_fragment);
    }
    return localStringBuffer.toString();
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
  
  public void setScheme(String paramString)
    throws URI.MalformedURIException
  {
    if (paramString == null) {
      throw new MalformedURIException("Cannot set scheme from null string!");
    }
    if (!isConformantSchemeName(paramString)) {
      throw new MalformedURIException("The scheme is not conformant.");
    }
    m_scheme = paramString.toLowerCase(Locale.ENGLISH);
  }
  
  public void setUserinfo(String paramString)
    throws URI.MalformedURIException
  {
    if (paramString == null)
    {
      m_userinfo = null;
      return;
    }
    if (m_host == null) {
      throw new MalformedURIException("Userinfo cannot be set when host is null!");
    }
    int i = 0;
    int j = paramString.length();
    char c = '\000';
    while (i < j)
    {
      c = paramString.charAt(i);
      if (c == '%')
      {
        if ((i + 2 >= j) || (!isHex(paramString.charAt(i + 1))) || (!isHex(paramString.charAt(i + 2)))) {
          throw new MalformedURIException("Userinfo contains invalid escape sequence!");
        }
      }
      else if (!isUserinfoCharacter(c)) {
        throw new MalformedURIException("Userinfo contains invalid character:" + c);
      }
      i++;
    }
    m_userinfo = paramString;
  }
  
  public void setHost(String paramString)
    throws URI.MalformedURIException
  {
    if ((paramString == null) || (paramString.length() == 0))
    {
      if (paramString != null) {
        m_regAuthority = null;
      }
      m_host = paramString;
      m_userinfo = null;
      m_port = -1;
      return;
    }
    if (!isWellFormedAddress(paramString)) {
      throw new MalformedURIException("Host is not a well formed address!");
    }
    m_host = paramString;
    m_regAuthority = null;
  }
  
  public void setPort(int paramInt)
    throws URI.MalformedURIException
  {
    if ((paramInt >= 0) && (paramInt <= 65535))
    {
      if (m_host == null) {
        throw new MalformedURIException("Port cannot be set when host is null!");
      }
    }
    else if (paramInt != -1) {
      throw new MalformedURIException("Invalid port number!");
    }
    m_port = paramInt;
  }
  
  public void setRegBasedAuthority(String paramString)
    throws URI.MalformedURIException
  {
    if (paramString == null)
    {
      m_regAuthority = null;
      return;
    }
    if ((paramString.length() < 1) || (!isValidRegistryBasedAuthority(paramString)) || (paramString.indexOf('/') != -1)) {
      throw new MalformedURIException("Registry based authority is not well formed.");
    }
    m_regAuthority = paramString;
    m_host = null;
    m_userinfo = null;
    m_port = -1;
  }
  
  public void setPath(String paramString)
    throws URI.MalformedURIException
  {
    if (paramString == null)
    {
      m_path = null;
      m_queryString = null;
      m_fragment = null;
    }
    else
    {
      initializePath(paramString, 0);
    }
  }
  
  public void appendPath(String paramString)
    throws URI.MalformedURIException
  {
    if ((paramString == null) || (paramString.trim().length() == 0)) {
      return;
    }
    if (!isURIString(paramString)) {
      throw new MalformedURIException("Path contains invalid character!");
    }
    if ((m_path == null) || (m_path.trim().length() == 0))
    {
      if (paramString.startsWith("/")) {
        m_path = paramString;
      } else {
        m_path = ("/" + paramString);
      }
    }
    else if (m_path.endsWith("/"))
    {
      if (paramString.startsWith("/")) {
        m_path = m_path.concat(paramString.substring(1));
      } else {
        m_path = m_path.concat(paramString);
      }
    }
    else if (paramString.startsWith("/")) {
      m_path = m_path.concat(paramString);
    } else {
      m_path = m_path.concat("/" + paramString);
    }
  }
  
  public void setQueryString(String paramString)
    throws URI.MalformedURIException
  {
    if (paramString == null)
    {
      m_queryString = null;
    }
    else
    {
      if (!isGenericURI()) {
        throw new MalformedURIException("Query string can only be set for a generic URI!");
      }
      if (getPath() == null) {
        throw new MalformedURIException("Query string cannot be set when path is null!");
      }
      if (!isURIString(paramString)) {
        throw new MalformedURIException("Query string contains invalid character!");
      }
      m_queryString = paramString;
    }
  }
  
  public void setFragment(String paramString)
    throws URI.MalformedURIException
  {
    if (paramString == null)
    {
      m_fragment = null;
    }
    else
    {
      if (!isGenericURI()) {
        throw new MalformedURIException("Fragment can only be set for a generic URI!");
      }
      if (getPath() == null) {
        throw new MalformedURIException("Fragment cannot be set when path is null!");
      }
      if (!isURIString(paramString)) {
        throw new MalformedURIException("Fragment contains invalid character!");
      }
      m_fragment = paramString;
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof URI))
    {
      URI localURI = (URI)paramObject;
      if (((m_scheme == null) && (m_scheme == null)) || ((m_scheme != null) && (m_scheme != null) && (m_scheme.equals(m_scheme)) && (((m_userinfo == null) && (m_userinfo == null)) || ((m_userinfo != null) && (m_userinfo != null) && (m_userinfo.equals(m_userinfo)) && (((m_host == null) && (m_host == null)) || ((m_host != null) && (m_host != null) && (m_host.equals(m_host)) && (m_port == m_port) && (((m_path == null) && (m_path == null)) || ((m_path != null) && (m_path != null) && (m_path.equals(m_path)) && (((m_queryString == null) && (m_queryString == null)) || ((m_queryString != null) && (m_queryString != null) && (m_queryString.equals(m_queryString)) && (((m_fragment == null) && (m_fragment == null)) || ((m_fragment != null) && (m_fragment != null) && (m_fragment.equals(m_fragment)))))))))))))) {
        return true;
      }
    }
    return false;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (m_scheme != null)
    {
      localStringBuffer.append(m_scheme);
      localStringBuffer.append(':');
    }
    localStringBuffer.append(getSchemeSpecificPart());
    return localStringBuffer.toString();
  }
  
  public boolean isGenericURI()
  {
    return m_host != null;
  }
  
  public boolean isAbsoluteURI()
  {
    return m_scheme != null;
  }
  
  public static boolean isConformantSchemeName(String paramString)
  {
    if ((paramString == null) || (paramString.trim().length() == 0)) {
      return false;
    }
    if (!isAlpha(paramString.charAt(0))) {
      return false;
    }
    int i = paramString.length();
    for (int j = 1; j < i; j++)
    {
      char c = paramString.charAt(j);
      if (!isSchemeCharacter(c)) {
        return false;
      }
    }
    return true;
  }
  
  public static boolean isWellFormedAddress(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    int i = paramString.length();
    if (i == 0) {
      return false;
    }
    if (paramString.startsWith("[")) {
      return isWellFormedIPv6Reference(paramString);
    }
    if ((paramString.startsWith(".")) || (paramString.startsWith("-")) || (paramString.endsWith("-"))) {
      return false;
    }
    int j = paramString.lastIndexOf('.');
    if (paramString.endsWith(".")) {
      j = paramString.substring(0, j).lastIndexOf('.');
    }
    if ((j + 1 < i) && (isDigit(paramString.charAt(j + 1)))) {
      return isWellFormedIPv4Address(paramString);
    }
    if (i > 255) {
      return false;
    }
    int k = 0;
    for (int m = 0; m < i; m++)
    {
      char c = paramString.charAt(m);
      if (c == '.')
      {
        if (!isAlphanum(paramString.charAt(m - 1))) {
          return false;
        }
        if ((m + 1 < i) && (!isAlphanum(paramString.charAt(m + 1)))) {
          return false;
        }
        k = 0;
      }
      else
      {
        if ((!isAlphanum(c)) && (c != '-')) {
          return false;
        }
        k++;
        if (k > 63) {
          return false;
        }
      }
    }
    return true;
  }
  
  public static boolean isWellFormedIPv4Address(String paramString)
  {
    int i = paramString.length();
    int j = 0;
    int k = 0;
    for (int m = 0; m < i; m++)
    {
      char c = paramString.charAt(m);
      if (c == '.')
      {
        if (((m > 0) && (!isDigit(paramString.charAt(m - 1)))) || ((m + 1 < i) && (!isDigit(paramString.charAt(m + 1))))) {
          return false;
        }
        k = 0;
        j++;
        if (j > 3) {
          return false;
        }
      }
      else
      {
        if (!isDigit(c)) {
          return false;
        }
        k++;
        if (k > 3) {
          return false;
        }
        if (k == 3)
        {
          int n = paramString.charAt(m - 2);
          int i1 = paramString.charAt(m - 1);
          if ((n >= 50) && ((n != 50) || ((i1 >= 53) && ((i1 != 53) || (c > '5'))))) {
            return false;
          }
        }
      }
    }
    return j == 3;
  }
  
  public static boolean isWellFormedIPv6Reference(String paramString)
  {
    int i = paramString.length();
    int j = 1;
    int k = i - 1;
    if ((i <= 2) || (paramString.charAt(0) != '[') || (paramString.charAt(k) != ']')) {
      return false;
    }
    int[] arrayOfInt = new int[1];
    j = scanHexSequence(paramString, j, k, arrayOfInt);
    if (j == -1) {
      return false;
    }
    if (j == k) {
      return arrayOfInt[0] == 8;
    }
    if ((j + 1 < k) && (paramString.charAt(j) == ':'))
    {
      if (paramString.charAt(j + 1) == ':')
      {
        if (arrayOfInt[0] += 1 > 8) {
          return false;
        }
        j += 2;
        if (j == k) {
          return true;
        }
      }
      else
      {
        return (arrayOfInt[0] == 6) && (isWellFormedIPv4Address(paramString.substring(j + 1, k)));
      }
    }
    else {
      return false;
    }
    int m = arrayOfInt[0];
    j = scanHexSequence(paramString, j, k, arrayOfInt);
    if (j != k) {
      if (j == -1) {
        break label221;
      }
    }
    label221:
    return isWellFormedIPv4Address(paramString.substring(arrayOfInt[0] > m ? j + 1 : j, k));
  }
  
  private static int scanHexSequence(String paramString, int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    int i = 0;
    int j = paramInt1;
    while (paramInt1 < paramInt2)
    {
      char c = paramString.charAt(paramInt1);
      if (c == ':')
      {
        if (i > 0) {
          if (paramArrayOfInt[0] += 1 > 8) {
            return -1;
          }
        }
        if ((i == 0) || ((paramInt1 + 1 < paramInt2) && (paramString.charAt(paramInt1 + 1) == ':'))) {
          return paramInt1;
        }
        i = 0;
      }
      else
      {
        if (!isHex(c))
        {
          if ((c == '.') && (i < 4) && (i > 0) && (paramArrayOfInt[0] <= 6))
          {
            int k = paramInt1 - i - 1;
            return k >= j ? k : k + 1;
          }
          return -1;
        }
        i++;
        if (i > 4) {
          return -1;
        }
      }
      paramInt1++;
    }
    if (i > 0) {}
    return tmp162_160[tmp162_161] += 1 <= 8 ? paramInt2 : -1;
  }
  
  private static boolean isDigit(char paramChar)
  {
    return (paramChar >= '0') && (paramChar <= '9');
  }
  
  private static boolean isHex(char paramChar)
  {
    return (paramChar <= 'f') && ((fgLookupTable[paramChar] & 0x40) != 0);
  }
  
  private static boolean isAlpha(char paramChar)
  {
    return ((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= 'A') && (paramChar <= 'Z'));
  }
  
  private static boolean isAlphanum(char paramChar)
  {
    return (paramChar <= 'z') && ((fgLookupTable[paramChar] & 0x30) != 0);
  }
  
  private static boolean isReservedCharacter(char paramChar)
  {
    return (paramChar <= ']') && ((fgLookupTable[paramChar] & 0x1) != 0);
  }
  
  private static boolean isUnreservedCharacter(char paramChar)
  {
    return (paramChar <= '~') && ((fgLookupTable[paramChar] & 0x32) != 0);
  }
  
  private static boolean isURICharacter(char paramChar)
  {
    return (paramChar <= '~') && ((fgLookupTable[paramChar] & 0x33) != 0);
  }
  
  private static boolean isSchemeCharacter(char paramChar)
  {
    return (paramChar <= 'z') && ((fgLookupTable[paramChar] & 0x34) != 0);
  }
  
  private static boolean isUserinfoCharacter(char paramChar)
  {
    return (paramChar <= 'z') && ((fgLookupTable[paramChar] & 0x3A) != 0);
  }
  
  private static boolean isPathCharacter(char paramChar)
  {
    return (paramChar <= '~') && ((fgLookupTable[paramChar] & 0xB2) != 0);
  }
  
  private static boolean isURIString(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    int i = paramString.length();
    char c = '\000';
    for (int j = 0; j < i; j++)
    {
      c = paramString.charAt(j);
      if (c == '%')
      {
        if ((j + 2 >= i) || (!isHex(paramString.charAt(j + 1))) || (!isHex(paramString.charAt(j + 2)))) {
          return false;
        }
        j += 2;
      }
      else if (!isURICharacter(c))
      {
        return false;
      }
    }
    return true;
  }
  
  static
  {
    for (int i = 48; i <= 57; i++)
    {
      int tmp18_17 = i;
      byte[] tmp18_14 = fgLookupTable;
      tmp18_14[tmp18_17] = ((byte)(tmp18_14[tmp18_17] | 0x60));
    }
    for (int j = 65; j <= 70; j++)
    {
      int tmp44_43 = j;
      byte[] tmp44_40 = fgLookupTable;
      tmp44_40[tmp44_43] = ((byte)(tmp44_40[tmp44_43] | 0x50));
      int tmp58_57 = (j + 32);
      byte[] tmp58_51 = fgLookupTable;
      tmp58_51[tmp58_57] = ((byte)(tmp58_51[tmp58_57] | 0x50));
    }
    for (int k = 71; k <= 90; k++)
    {
      int tmp84_83 = k;
      byte[] tmp84_80 = fgLookupTable;
      tmp84_80[tmp84_83] = ((byte)(tmp84_80[tmp84_83] | 0x10));
      int tmp98_97 = (k + 32);
      byte[] tmp98_91 = fgLookupTable;
      tmp98_91[tmp98_97] = ((byte)(tmp98_91[tmp98_97] | 0x10));
    }
    byte[] tmp119_114 = fgLookupTable;
    tmp119_114[59] = ((byte)(tmp119_114[59] | 0x1));
    byte[] tmp130_125 = fgLookupTable;
    tmp130_125[47] = ((byte)(tmp130_125[47] | 0x1));
    byte[] tmp141_136 = fgLookupTable;
    tmp141_136[63] = ((byte)(tmp141_136[63] | 0x1));
    byte[] tmp152_147 = fgLookupTable;
    tmp152_147[58] = ((byte)(tmp152_147[58] | 0x1));
    byte[] tmp163_158 = fgLookupTable;
    tmp163_158[64] = ((byte)(tmp163_158[64] | 0x1));
    byte[] tmp174_169 = fgLookupTable;
    tmp174_169[38] = ((byte)(tmp174_169[38] | 0x1));
    byte[] tmp185_180 = fgLookupTable;
    tmp185_180[61] = ((byte)(tmp185_180[61] | 0x1));
    byte[] tmp196_191 = fgLookupTable;
    tmp196_191[43] = ((byte)(tmp196_191[43] | 0x1));
    byte[] tmp207_202 = fgLookupTable;
    tmp207_202[36] = ((byte)(tmp207_202[36] | 0x1));
    byte[] tmp218_213 = fgLookupTable;
    tmp218_213[44] = ((byte)(tmp218_213[44] | 0x1));
    byte[] tmp229_224 = fgLookupTable;
    tmp229_224[91] = ((byte)(tmp229_224[91] | 0x1));
    byte[] tmp240_235 = fgLookupTable;
    tmp240_235[93] = ((byte)(tmp240_235[93] | 0x1));
    byte[] tmp251_246 = fgLookupTable;
    tmp251_246[45] = ((byte)(tmp251_246[45] | 0x2));
    byte[] tmp262_257 = fgLookupTable;
    tmp262_257[95] = ((byte)(tmp262_257[95] | 0x2));
    byte[] tmp273_268 = fgLookupTable;
    tmp273_268[46] = ((byte)(tmp273_268[46] | 0x2));
    byte[] tmp284_279 = fgLookupTable;
    tmp284_279[33] = ((byte)(tmp284_279[33] | 0x2));
    byte[] tmp295_290 = fgLookupTable;
    tmp295_290[126] = ((byte)(tmp295_290[126] | 0x2));
    byte[] tmp306_301 = fgLookupTable;
    tmp306_301[42] = ((byte)(tmp306_301[42] | 0x2));
    byte[] tmp317_312 = fgLookupTable;
    tmp317_312[39] = ((byte)(tmp317_312[39] | 0x2));
    byte[] tmp328_323 = fgLookupTable;
    tmp328_323[40] = ((byte)(tmp328_323[40] | 0x2));
    byte[] tmp339_334 = fgLookupTable;
    tmp339_334[41] = ((byte)(tmp339_334[41] | 0x2));
    byte[] tmp350_345 = fgLookupTable;
    tmp350_345[43] = ((byte)(tmp350_345[43] | 0x4));
    byte[] tmp361_356 = fgLookupTable;
    tmp361_356[45] = ((byte)(tmp361_356[45] | 0x4));
    byte[] tmp372_367 = fgLookupTable;
    tmp372_367[46] = ((byte)(tmp372_367[46] | 0x4));
    byte[] tmp383_378 = fgLookupTable;
    tmp383_378[59] = ((byte)(tmp383_378[59] | 0x8));
    byte[] tmp395_390 = fgLookupTable;
    tmp395_390[58] = ((byte)(tmp395_390[58] | 0x8));
    byte[] tmp407_402 = fgLookupTable;
    tmp407_402[38] = ((byte)(tmp407_402[38] | 0x8));
    byte[] tmp419_414 = fgLookupTable;
    tmp419_414[61] = ((byte)(tmp419_414[61] | 0x8));
    byte[] tmp431_426 = fgLookupTable;
    tmp431_426[43] = ((byte)(tmp431_426[43] | 0x8));
    byte[] tmp443_438 = fgLookupTable;
    tmp443_438[36] = ((byte)(tmp443_438[36] | 0x8));
    byte[] tmp455_450 = fgLookupTable;
    tmp455_450[44] = ((byte)(tmp455_450[44] | 0x8));
    byte[] tmp467_462 = fgLookupTable;
    tmp467_462[59] = ((byte)(tmp467_462[59] | 0x80));
    byte[] tmp480_475 = fgLookupTable;
    tmp480_475[47] = ((byte)(tmp480_475[47] | 0x80));
    byte[] tmp493_488 = fgLookupTable;
    tmp493_488[58] = ((byte)(tmp493_488[58] | 0x80));
    byte[] tmp506_501 = fgLookupTable;
    tmp506_501[64] = ((byte)(tmp506_501[64] | 0x80));
    byte[] tmp519_514 = fgLookupTable;
    tmp519_514[38] = ((byte)(tmp519_514[38] | 0x80));
    byte[] tmp532_527 = fgLookupTable;
    tmp532_527[61] = ((byte)(tmp532_527[61] | 0x80));
    byte[] tmp545_540 = fgLookupTable;
    tmp545_540[43] = ((byte)(tmp545_540[43] | 0x80));
    byte[] tmp558_553 = fgLookupTable;
    tmp558_553[36] = ((byte)(tmp558_553[36] | 0x80));
    byte[] tmp571_566 = fgLookupTable;
    tmp571_566[44] = ((byte)(tmp571_566[44] | 0x80));
  }
  
  public static class MalformedURIException
    extends IOException
  {
    static final long serialVersionUID = -6695054834342951930L;
    
    public MalformedURIException() {}
    
    public MalformedURIException(String paramString)
    {
      super();
    }
  }
}
