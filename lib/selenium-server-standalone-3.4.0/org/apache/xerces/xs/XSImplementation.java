package org.apache.xerces.xs;

import org.w3c.dom.ls.LSInput;

public abstract interface XSImplementation
{
  public abstract StringList getRecognizedVersions();
  
  public abstract XSLoader createXSLoader(StringList paramStringList)
    throws XSException;
  
  public abstract StringList createStringList(String[] paramArrayOfString);
  
  public abstract LSInputList createLSInputList(LSInput[] paramArrayOfLSInput);
}
