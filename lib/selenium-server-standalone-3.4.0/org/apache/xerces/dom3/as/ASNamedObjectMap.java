package org.apache.xerces.dom3.as;

import org.w3c.dom.DOMException;

/**
 * @deprecated
 */
public abstract interface ASNamedObjectMap
{
  public abstract int getLength();
  
  public abstract ASObject getNamedItem(String paramString);
  
  public abstract ASObject getNamedItemNS(String paramString1, String paramString2);
  
  public abstract ASObject item(int paramInt);
  
  public abstract ASObject removeNamedItem(String paramString)
    throws DOMException;
  
  public abstract ASObject removeNamedItemNS(String paramString1, String paramString2)
    throws DOMException;
  
  public abstract ASObject setNamedItem(ASObject paramASObject)
    throws DOMException;
  
  public abstract ASObject setNamedItemNS(ASObject paramASObject)
    throws DOMException;
}
