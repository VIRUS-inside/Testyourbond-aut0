package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSModelGroupDefinition;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObjectList;

public class XSGroupDecl
  implements XSModelGroupDefinition
{
  public String fName = null;
  public String fTargetNamespace = null;
  public XSModelGroupImpl fModelGroup = null;
  public XSObjectList fAnnotations = null;
  private XSNamespaceItem fNamespaceItem = null;
  
  public XSGroupDecl() {}
  
  public short getType()
  {
    return 6;
  }
  
  public String getName()
  {
    return fName;
  }
  
  public String getNamespace()
  {
    return fTargetNamespace;
  }
  
  public XSModelGroup getModelGroup()
  {
    return fModelGroup;
  }
  
  public XSAnnotation getAnnotation()
  {
    return fAnnotations != null ? (XSAnnotation)fAnnotations.item(0) : null;
  }
  
  public XSObjectList getAnnotations()
  {
    return fAnnotations != null ? fAnnotations : XSObjectListImpl.EMPTY_LIST;
  }
  
  public XSNamespaceItem getNamespaceItem()
  {
    return fNamespaceItem;
  }
  
  void setNamespaceItem(XSNamespaceItem paramXSNamespaceItem)
  {
    fNamespaceItem = paramXSNamespaceItem;
  }
}
