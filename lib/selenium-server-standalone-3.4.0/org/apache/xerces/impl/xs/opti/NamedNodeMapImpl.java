package org.apache.xerces.impl.xs.opti;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NamedNodeMapImpl
  implements NamedNodeMap
{
  Attr[] attrs;
  
  public NamedNodeMapImpl(Attr[] paramArrayOfAttr)
  {
    attrs = paramArrayOfAttr;
  }
  
  public Node getNamedItem(String paramString)
  {
    for (int i = 0; i < attrs.length; i++) {
      if (attrs[i].getName().equals(paramString)) {
        return attrs[i];
      }
    }
    return null;
  }
  
  public Node item(int paramInt)
  {
    if ((paramInt < 0) && (paramInt > getLength())) {
      return null;
    }
    return attrs[paramInt];
  }
  
  public int getLength()
  {
    return attrs.length;
  }
  
  public Node getNamedItemNS(String paramString1, String paramString2)
  {
    for (int i = 0; i < attrs.length; i++) {
      if ((attrs[i].getName().equals(paramString2)) && (attrs[i].getNamespaceURI().equals(paramString1))) {
        return attrs[i];
      }
    }
    return null;
  }
  
  public Node setNamedItemNS(Node paramNode)
    throws DOMException
  {
    throw new DOMException((short)9, "Method not supported");
  }
  
  public Node setNamedItem(Node paramNode)
    throws DOMException
  {
    throw new DOMException((short)9, "Method not supported");
  }
  
  public Node removeNamedItem(String paramString)
    throws DOMException
  {
    throw new DOMException((short)9, "Method not supported");
  }
  
  public Node removeNamedItemNS(String paramString1, String paramString2)
    throws DOMException
  {
    throw new DOMException((short)9, "Method not supported");
  }
}
