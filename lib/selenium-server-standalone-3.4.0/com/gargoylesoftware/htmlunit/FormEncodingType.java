package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.util.Locale;





















public final class FormEncodingType
  implements Serializable
{
  public static final FormEncodingType URL_ENCODED = new FormEncodingType("application/x-www-form-urlencoded");
  

  public static final FormEncodingType MULTIPART = new FormEncodingType("multipart/form-data");
  private final String name_;
  
  private FormEncodingType(String name)
  {
    name_ = name;
  }
  




  public String getName()
  {
    return name_;
  }
  





  public static FormEncodingType getInstance(String name)
  {
    String lowerCaseName = name.toLowerCase(Locale.ROOT);
    
    if (MULTIPART.getName().equals(lowerCaseName)) {
      return MULTIPART;
    }
    
    return URL_ENCODED;
  }
  




  public String toString()
  {
    return "EncodingType[name=" + getName() + "]";
  }
}
