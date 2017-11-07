package org.apache.http.entity.mime;












public class MinimalField
{
  private final String name;
  










  private final String value;
  










  public MinimalField(String name, String value)
  {
    this.name = name;
    this.value = value;
  }
  
  public String getName() {
    return name;
  }
  
  public String getBody() {
    return value;
  }
  
  public String toString()
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append(name);
    buffer.append(": ");
    buffer.append(value);
    return buffer.toString();
  }
}
