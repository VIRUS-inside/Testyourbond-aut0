package org.apache.xerces.dom3.as;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @deprecated
 */
public abstract interface ElementEditAS
  extends NodeEditAS
{
  public abstract NodeList getDefinedElementTypes();
  
  public abstract short contentType();
  
  public abstract boolean canSetAttribute(String paramString1, String paramString2);
  
  public abstract boolean canSetAttributeNode(Attr paramAttr);
  
  public abstract boolean canSetAttributeNS(String paramString1, String paramString2, String paramString3);
  
  public abstract boolean canRemoveAttribute(String paramString);
  
  public abstract boolean canRemoveAttributeNS(String paramString1, String paramString2);
  
  public abstract boolean canRemoveAttributeNode(Node paramNode);
  
  public abstract NodeList getChildElements();
  
  public abstract NodeList getParentElements();
  
  public abstract NodeList getAttributeList();
  
  public abstract boolean isElementDefined(String paramString);
  
  public abstract boolean isElementDefinedNS(String paramString1, String paramString2, String paramString3);
}
