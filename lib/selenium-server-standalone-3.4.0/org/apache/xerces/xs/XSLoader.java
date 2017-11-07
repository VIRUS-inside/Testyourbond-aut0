package org.apache.xerces.xs;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.ls.LSInput;

public abstract interface XSLoader
{
  public abstract DOMConfiguration getConfig();
  
  public abstract XSModel loadURIList(StringList paramStringList);
  
  public abstract XSModel loadInputList(LSInputList paramLSInputList);
  
  public abstract XSModel loadURI(String paramString);
  
  public abstract XSModel load(LSInput paramLSInput);
}
