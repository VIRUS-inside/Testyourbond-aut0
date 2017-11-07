package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;











































public class BasicHeaderElement
  implements HeaderElement, Cloneable
{
  private final String name;
  private final String value;
  private final NameValuePair[] parameters;
  
  public BasicHeaderElement(String name, String value, NameValuePair[] parameters)
  {
    this.name = ((String)Args.notNull(name, "Name"));
    this.value = value;
    if (parameters != null) {
      this.parameters = parameters;
    } else {
      this.parameters = new NameValuePair[0];
    }
  }
  





  public BasicHeaderElement(String name, String value)
  {
    this(name, value, null);
  }
  
  public String getName()
  {
    return name;
  }
  
  public String getValue()
  {
    return value;
  }
  
  public NameValuePair[] getParameters()
  {
    return (NameValuePair[])parameters.clone();
  }
  
  public int getParameterCount()
  {
    return parameters.length;
  }
  

  public NameValuePair getParameter(int index)
  {
    return parameters[index];
  }
  
  public NameValuePair getParameterByName(String name)
  {
    Args.notNull(name, "Name");
    NameValuePair found = null;
    for (NameValuePair current : parameters) {
      if (current.getName().equalsIgnoreCase(name)) {
        found = current;
        break;
      }
    }
    return found;
  }
  
  public boolean equals(Object object)
  {
    if (this == object) {
      return true;
    }
    if ((object instanceof HeaderElement)) {
      BasicHeaderElement that = (BasicHeaderElement)object;
      return (name.equals(name)) && (LangUtils.equals(value, value)) && (LangUtils.equals(parameters, parameters));
    }
    

    return false;
  }
  

  public int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, name);
    hash = LangUtils.hashCode(hash, value);
    for (NameValuePair parameter : parameters) {
      hash = LangUtils.hashCode(hash, parameter);
    }
    return hash;
  }
  
  public String toString()
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append(name);
    if (value != null) {
      buffer.append("=");
      buffer.append(value);
    }
    for (NameValuePair parameter : parameters) {
      buffer.append("; ");
      buffer.append(parameter);
    }
    return buffer.toString();
  }
  

  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
}
