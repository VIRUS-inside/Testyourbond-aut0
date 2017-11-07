package org.apache.xerces.impl.xpath.regex;

public class ParseException
  extends RuntimeException
{
  static final long serialVersionUID = -7012400318097691370L;
  final int location;
  
  public ParseException(String paramString, int paramInt)
  {
    super(paramString);
    location = paramInt;
  }
  
  public int getLocation()
  {
    return location;
  }
}
