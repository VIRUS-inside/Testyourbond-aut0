package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;







































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class BasicHeader
  implements Header, Cloneable, Serializable
{
  private static final long serialVersionUID = -5427236326487562174L;
  private final String name;
  private final String value;
  
  public BasicHeader(String name, String value)
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
    return BasicLineFormatter.INSTANCE.formatHeader(null, this).toString();
  }
  
  public HeaderElement[] getElements() throws ParseException
  {
    if (value != null)
    {
      return BasicHeaderValueParser.parseElements(value, null);
    }
    return new HeaderElement[0];
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
}
