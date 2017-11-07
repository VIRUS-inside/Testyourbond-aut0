package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;








































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class BasicNameValuePair
  implements NameValuePair, Cloneable, Serializable
{
  private static final long serialVersionUID = -6437800749411518984L;
  private final String name;
  private final String value;
  
  public BasicNameValuePair(String name, String value)
  {
    this.name = ((String)Args.notNull(name, "Name"));
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
  


  public String toString()
  {
    if (value == null) {
      return name;
    }
    int len = name.length() + 1 + value.length();
    StringBuilder buffer = new StringBuilder(len);
    buffer.append(name);
    buffer.append("=");
    buffer.append(value);
    return buffer.toString();
  }
  
  public boolean equals(Object object)
  {
    if (this == object) {
      return true;
    }
    if ((object instanceof NameValuePair)) {
      BasicNameValuePair that = (BasicNameValuePair)object;
      return (name.equals(name)) && (LangUtils.equals(value, value));
    }
    
    return false;
  }
  
  public int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, name);
    hash = LangUtils.hashCode(hash, value);
    return hash;
  }
  
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
