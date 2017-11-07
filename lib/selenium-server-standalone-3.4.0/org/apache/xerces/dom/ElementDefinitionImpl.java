package org.apache.xerces.dom;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ElementDefinitionImpl
  extends ParentNode
{
  static final long serialVersionUID = -8373890672670022714L;
  protected String name;
  protected NamedNodeMapImpl attributes;
  
  public ElementDefinitionImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString)
  {
    super(paramCoreDocumentImpl);
    name = paramString;
    attributes = new NamedNodeMapImpl(paramCoreDocumentImpl);
  }
  
  public short getNodeType()
  {
    return 21;
  }
  
  public String getNodeName()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return name;
  }
  
  public Node cloneNode(boolean paramBoolean)
  {
    ElementDefinitionImpl localElementDefinitionImpl = (ElementDefinitionImpl)super.cloneNode(paramBoolean);
    attributes = attributes.cloneMap(localElementDefinitionImpl);
    return localElementDefinitionImpl;
  }
  
  public NamedNodeMap getAttributes()
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    return attributes;
  }
}
