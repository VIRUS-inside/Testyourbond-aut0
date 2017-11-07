package org.apache.xerces.impl.xs;

public class XMLSchemaException
  extends Exception
{
  static final long serialVersionUID = -9096984648537046218L;
  String key;
  Object[] args;
  
  public XMLSchemaException(String paramString, Object[] paramArrayOfObject)
  {
    key = paramString;
    args = paramArrayOfObject;
  }
  
  public String getKey()
  {
    return key;
  }
  
  public Object[] getArgs()
  {
    return args;
  }
}
