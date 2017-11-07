package com.gargoylesoftware.htmlunit.util;

import java.io.Serializable;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.LangUtils;




























public class NameValuePair
  implements Serializable
{
  private final String name_;
  private final String value_;
  
  public NameValuePair(String name, String value)
  {
    name_ = name;
    value_ = value;
  }
  



  public String getName()
  {
    return name_;
  }
  



  public String getValue()
  {
    return value_;
  }
  



  public boolean equals(Object object)
  {
    if (!(object instanceof NameValuePair)) {
      return false;
    }
    NameValuePair other = (NameValuePair)object;
    return (LangUtils.equals(name_, name_)) && (LangUtils.equals(value_, value_));
  }
  



  public int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, name_);
    hash = LangUtils.hashCode(hash, value_);
    return hash;
  }
  



  public String toString()
  {
    return name_ + "=" + value_;
  }
  




  public static org.apache.http.NameValuePair[] toHttpClient(NameValuePair[] pairs)
  {
    org.apache.http.NameValuePair[] pairs2 = 
      new org.apache.http.NameValuePair[pairs.length];
    for (int i = 0; i < pairs.length; i++) {
      NameValuePair pair = pairs[i];
      pairs2[i] = new BasicNameValuePair(pair.getName(), pair.getValue());
    }
    return pairs2;
  }
  




  public static org.apache.http.NameValuePair[] toHttpClient(List<NameValuePair> pairs)
  {
    org.apache.http.NameValuePair[] pairs2 = new org.apache.http.NameValuePair[pairs.size()];
    for (int i = 0; i < pairs.size(); i++) {
      NameValuePair pair = (NameValuePair)pairs.get(i);
      pairs2[i] = new BasicNameValuePair(pair.getName(), pair.getValue());
    }
    return pairs2;
  }
}
