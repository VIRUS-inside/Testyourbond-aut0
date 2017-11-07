package org.apache.http.impl.cookie;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;





























public class BasicClientCookie
  implements SetCookie, ClientCookie, Cloneable, Serializable
{
  private static final long serialVersionUID = -3869795591041535538L;
  private final String name;
  private Map<String, String> attribs;
  private String value;
  private String cookieComment;
  private String cookieDomain;
  private Date cookieExpiryDate;
  private String cookiePath;
  private boolean isSecure;
  private int cookieVersion;
  private Date creationDate;
  
  public BasicClientCookie(String name, String value)
  {
    Args.notNull(name, "Name");
    this.name = name;
    attribs = new HashMap();
    this.value = value;
  }
  





  public String getName()
  {
    return name;
  }
  





  public String getValue()
  {
    return value;
  }
  





  public void setValue(String value)
  {
    this.value = value;
  }
  








  public String getComment()
  {
    return cookieComment;
  }
  








  public void setComment(String comment)
  {
    cookieComment = comment;
  }
  




  public String getCommentURL()
  {
    return null;
  }
  












  public Date getExpiryDate()
  {
    return cookieExpiryDate;
  }
  











  public void setExpiryDate(Date expiryDate)
  {
    cookieExpiryDate = expiryDate;
  }
  








  public boolean isPersistent()
  {
    return null != cookieExpiryDate;
  }
  








  public String getDomain()
  {
    return cookieDomain;
  }
  







  public void setDomain(String domain)
  {
    if (domain != null) {
      cookieDomain = domain.toLowerCase(Locale.ROOT);
    } else {
      cookieDomain = null;
    }
  }
  








  public String getPath()
  {
    return cookiePath;
  }
  








  public void setPath(String path)
  {
    cookiePath = path;
  }
  




  public boolean isSecure()
  {
    return isSecure;
  }
  












  public void setSecure(boolean secure)
  {
    isSecure = secure;
  }
  




  public int[] getPorts()
  {
    return null;
  }
  










  public int getVersion()
  {
    return cookieVersion;
  }
  








  public void setVersion(int version)
  {
    cookieVersion = version;
  }
  






  public boolean isExpired(Date date)
  {
    Args.notNull(date, "Date");
    return (cookieExpiryDate != null) && (cookieExpiryDate.getTime() <= date.getTime());
  }
  



  public Date getCreationDate()
  {
    return creationDate;
  }
  


  public void setCreationDate(Date creationDate)
  {
    this.creationDate = creationDate;
  }
  
  public void setAttribute(String name, String value) {
    attribs.put(name, value);
  }
  
  public String getAttribute(String name)
  {
    return (String)attribs.get(name);
  }
  
  public boolean containsAttribute(String name)
  {
    return attribs.containsKey(name);
  }
  


  public boolean removeAttribute(String name)
  {
    return attribs.remove(name) != null;
  }
  
  public Object clone() throws CloneNotSupportedException
  {
    BasicClientCookie clone = (BasicClientCookie)super.clone();
    attribs = new HashMap(attribs);
    return clone;
  }
  
  public String toString()
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append("[version: ");
    buffer.append(Integer.toString(cookieVersion));
    buffer.append("]");
    buffer.append("[name: ");
    buffer.append(name);
    buffer.append("]");
    buffer.append("[value: ");
    buffer.append(value);
    buffer.append("]");
    buffer.append("[domain: ");
    buffer.append(cookieDomain);
    buffer.append("]");
    buffer.append("[path: ");
    buffer.append(cookiePath);
    buffer.append("]");
    buffer.append("[expiry: ");
    buffer.append(cookieExpiryDate);
    buffer.append("]");
    return buffer.toString();
  }
}
