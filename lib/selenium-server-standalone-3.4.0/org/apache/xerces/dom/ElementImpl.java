package org.apache.xerces.dom;

import org.apache.xerces.util.URI;
import org.apache.xerces.util.URI.MalformedURIException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ElementTraversal;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;

public class ElementImpl
  extends ParentNode
  implements Element, ElementTraversal, TypeInfo
{
  static final long serialVersionUID = 3717253516652722278L;
  protected String name;
  protected AttributeMap attributes;
  
  public ElementImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString)
  {
    super(paramCoreDocumentImpl);
    name = paramString;
    needsSyncData(true);
  }
  
  protected ElementImpl() {}
  
  void rename(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (ownerDocument.errorChecking)
    {
      int i = paramString.indexOf(':');
      String str;
      if (i != -1)
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
        throw new DOMException((short)14, str);
      }
      if (!CoreDocumentImpl.isXMLName(paramString, ownerDocument.isXML11Version()))
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
        throw new DOMException((short)5, str);
      }
    }
    name = paramString;
    reconcileDefaultAttributes();
  }
  
  public short getNodeType()
  {
    return 1;
  }
  
  public String getNodeName()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return name;
  }
  
  public NamedNodeMap getAttributes()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (attributes == null) {
      attributes = new AttributeMap(this, null);
    }
    return attributes;
  }
  
  public Node cloneNode(boolean paramBoolean)
  {
    ElementImpl localElementImpl = (ElementImpl)super.cloneNode(paramBoolean);
    if (attributes != null) {
      attributes = ((AttributeMap)attributes.cloneMap(localElementImpl));
    }
    return localElementImpl;
  }
  
  public String getBaseURI()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (attributes != null)
    {
      Attr localAttr = getXMLBaseAttribute();
      if (localAttr != null)
      {
        String str1 = localAttr.getNodeValue();
        if (str1.length() != 0) {
          try
          {
            URI localURI1 = new URI(str1, true);
            if (localURI1.isAbsoluteURI()) {
              return localURI1.toString();
            }
            String str2 = ownerNode != null ? ownerNode.getBaseURI() : null;
            if (str2 != null) {
              try
              {
                URI localURI2 = new URI(str2);
                localURI1.absolutize(localURI2);
                return localURI1.toString();
              }
              catch (URI.MalformedURIException localMalformedURIException2)
              {
                return null;
              }
            }
            return null;
          }
          catch (URI.MalformedURIException localMalformedURIException1)
          {
            return null;
          }
        }
      }
    }
    return ownerNode != null ? ownerNode.getBaseURI() : null;
  }
  
  protected Attr getXMLBaseAttribute()
  {
    return (Attr)attributes.getNamedItem("xml:base");
  }
  
  protected void setOwnerDocument(CoreDocumentImpl paramCoreDocumentImpl)
  {
    super.setOwnerDocument(paramCoreDocumentImpl);
    if (attributes != null) {
      attributes.setOwnerDocument(paramCoreDocumentImpl);
    }
  }
  
  public String getAttribute(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (attributes == null) {
      return "";
    }
    Attr localAttr = (Attr)attributes.getNamedItem(paramString);
    return localAttr == null ? "" : localAttr.getValue();
  }
  
  public Attr getAttributeNode(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (attributes == null) {
      return null;
    }
    return (Attr)attributes.getNamedItem(paramString);
  }
  
  public NodeList getElementsByTagName(String paramString)
  {
    return new DeepNodeListImpl(this, paramString);
  }
  
  public String getTagName()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return name;
  }
  
  public void normalize()
  {
    if (isNormalized()) {
      return;
    }
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    Object localObject2;
    for (Object localObject1 = firstChild; localObject1 != null; localObject1 = localObject2)
    {
      localObject2 = nextSibling;
      if (((ChildNode)localObject1).getNodeType() == 3)
      {
        if ((localObject2 != null) && (((ChildNode)localObject2).getNodeType() == 3))
        {
          ((Text)localObject1).appendData(((ChildNode)localObject2).getNodeValue());
          removeChild((Node)localObject2);
          localObject2 = localObject1;
        }
        else if ((((ChildNode)localObject1).getNodeValue() == null) || (((ChildNode)localObject1).getNodeValue().length() == 0))
        {
          removeChild((Node)localObject1);
        }
      }
      else if (((ChildNode)localObject1).getNodeType() == 1) {
        ((ChildNode)localObject1).normalize();
      }
    }
    if (attributes != null) {
      for (int i = 0; i < attributes.getLength(); i++)
      {
        Node localNode = attributes.item(i);
        localNode.normalize();
      }
    }
    isNormalized(true);
  }
  
  public void removeAttribute(String paramString)
  {
    if ((ownerDocument.errorChecking) && (isReadOnly()))
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
      throw new DOMException((short)7, str);
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    if (attributes == null) {
      return;
    }
    attributes.safeRemoveNamedItem(paramString);
  }
  
  public Attr removeAttributeNode(Attr paramAttr)
    throws DOMException
  {
    String str;
    if ((ownerDocument.errorChecking) && (isReadOnly()))
    {
      str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
      throw new DOMException((short)7, str);
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    if (attributes == null)
    {
      str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
      throw new DOMException((short)8, str);
    }
    return (Attr)attributes.removeItem(paramAttr, true);
  }
  
  public void setAttribute(String paramString1, String paramString2)
  {
    if ((ownerDocument.errorChecking) && (isReadOnly()))
    {
      localObject = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
      throw new DOMException((short)7, (String)localObject);
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    Object localObject = getAttributeNode(paramString1);
    if (localObject == null)
    {
      localObject = getOwnerDocument().createAttribute(paramString1);
      if (attributes == null) {
        attributes = new AttributeMap(this, null);
      }
      ((Attr)localObject).setNodeValue(paramString2);
      attributes.setNamedItem((Node)localObject);
    }
    else
    {
      ((Attr)localObject).setNodeValue(paramString2);
    }
  }
  
  public Attr setAttributeNode(Attr paramAttr)
    throws DOMException
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (ownerDocument.errorChecking)
    {
      String str;
      if (isReadOnly())
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
        throw new DOMException((short)7, str);
      }
      if (paramAttr.getOwnerDocument() != ownerDocument)
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
        throw new DOMException((short)4, str);
      }
    }
    if (attributes == null) {
      attributes = new AttributeMap(this, null);
    }
    return (Attr)attributes.setNamedItem(paramAttr);
  }
  
  public String getAttributeNS(String paramString1, String paramString2)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (attributes == null) {
      return "";
    }
    Attr localAttr = (Attr)attributes.getNamedItemNS(paramString1, paramString2);
    return localAttr == null ? "" : localAttr.getValue();
  }
  
  public void setAttributeNS(String paramString1, String paramString2, String paramString3)
  {
    if ((ownerDocument.errorChecking) && (isReadOnly()))
    {
      String str1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
      throw new DOMException((short)7, str1);
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    int i = paramString2.indexOf(':');
    String str2;
    String str3;
    if (i < 0)
    {
      str2 = null;
      str3 = paramString2;
    }
    else
    {
      str2 = paramString2.substring(0, i);
      str3 = paramString2.substring(i + 1);
    }
    Attr localAttr = getAttributeNodeNS(paramString1, str3);
    if (localAttr == null)
    {
      localAttr = getOwnerDocument().createAttributeNS(paramString1, paramString2);
      if (attributes == null) {
        attributes = new AttributeMap(this, null);
      }
      localAttr.setNodeValue(paramString3);
      attributes.setNamedItemNS(localAttr);
    }
    else
    {
      if ((localAttr instanceof AttrNSImpl))
      {
        name = (str2 != null ? str2 + ":" + str3 : str3);
      }
      else
      {
        localAttr = ((CoreDocumentImpl)getOwnerDocument()).createAttributeNS(paramString1, paramString2, str3);
        attributes.setNamedItemNS(localAttr);
      }
      localAttr.setNodeValue(paramString3);
    }
  }
  
  public void removeAttributeNS(String paramString1, String paramString2)
  {
    if ((ownerDocument.errorChecking) && (isReadOnly()))
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
      throw new DOMException((short)7, str);
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    if (attributes == null) {
      return;
    }
    attributes.safeRemoveNamedItemNS(paramString1, paramString2);
  }
  
  public Attr getAttributeNodeNS(String paramString1, String paramString2)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (attributes == null) {
      return null;
    }
    return (Attr)attributes.getNamedItemNS(paramString1, paramString2);
  }
  
  public Attr setAttributeNodeNS(Attr paramAttr)
    throws DOMException
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (ownerDocument.errorChecking)
    {
      String str;
      if (isReadOnly())
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
        throw new DOMException((short)7, str);
      }
      if (paramAttr.getOwnerDocument() != ownerDocument)
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
        throw new DOMException((short)4, str);
      }
    }
    if (attributes == null) {
      attributes = new AttributeMap(this, null);
    }
    return (Attr)attributes.setNamedItemNS(paramAttr);
  }
  
  protected int setXercesAttributeNode(Attr paramAttr)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (attributes == null) {
      attributes = new AttributeMap(this, null);
    }
    return attributes.addItem(paramAttr);
  }
  
  protected int getXercesAttribute(String paramString1, String paramString2)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (attributes == null) {
      return -1;
    }
    return attributes.getNamedItemIndex(paramString1, paramString2);
  }
  
  public boolean hasAttributes()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return (attributes != null) && (attributes.getLength() != 0);
  }
  
  public boolean hasAttribute(String paramString)
  {
    return getAttributeNode(paramString) != null;
  }
  
  public boolean hasAttributeNS(String paramString1, String paramString2)
  {
    return getAttributeNodeNS(paramString1, paramString2) != null;
  }
  
  public NodeList getElementsByTagNameNS(String paramString1, String paramString2)
  {
    return new DeepNodeListImpl(this, paramString1, paramString2);
  }
  
  public boolean isEqualNode(Node paramNode)
  {
    if (!super.isEqualNode(paramNode)) {
      return false;
    }
    boolean bool = hasAttributes();
    if (bool != ((Element)paramNode).hasAttributes()) {
      return false;
    }
    if (bool)
    {
      NamedNodeMap localNamedNodeMap1 = getAttributes();
      NamedNodeMap localNamedNodeMap2 = ((Element)paramNode).getAttributes();
      int i = localNamedNodeMap1.getLength();
      if (i != localNamedNodeMap2.getLength()) {
        return false;
      }
      for (int j = 0; j < i; j++)
      {
        Node localNode1 = localNamedNodeMap1.item(j);
        Node localNode2;
        if (localNode1.getLocalName() == null)
        {
          localNode2 = localNamedNodeMap2.getNamedItem(localNode1.getNodeName());
          if ((localNode2 == null) || (!((NodeImpl)localNode1).isEqualNode(localNode2))) {
            return false;
          }
        }
        else
        {
          localNode2 = localNamedNodeMap2.getNamedItemNS(localNode1.getNamespaceURI(), localNode1.getLocalName());
          if ((localNode2 == null) || (!((NodeImpl)localNode1).isEqualNode(localNode2))) {
            return false;
          }
        }
      }
    }
    return true;
  }
  
  public void setIdAttributeNode(Attr paramAttr, boolean paramBoolean)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (ownerDocument.errorChecking)
    {
      String str;
      if (isReadOnly())
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
        throw new DOMException((short)7, str);
      }
      if (paramAttr.getOwnerElement() != this)
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
        throw new DOMException((short)8, str);
      }
    }
    ((AttrImpl)paramAttr).isIdAttribute(paramBoolean);
    if (!paramBoolean) {
      ownerDocument.removeIdentifier(paramAttr.getValue());
    } else {
      ownerDocument.putIdentifier(paramAttr.getValue(), this);
    }
  }
  
  public void setIdAttribute(String paramString, boolean paramBoolean)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    Attr localAttr = getAttributeNode(paramString);
    String str;
    if (localAttr == null)
    {
      str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
      throw new DOMException((short)8, str);
    }
    if (ownerDocument.errorChecking)
    {
      if (isReadOnly())
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
        throw new DOMException((short)7, str);
      }
      if (localAttr.getOwnerElement() != this)
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
        throw new DOMException((short)8, str);
      }
    }
    ((AttrImpl)localAttr).isIdAttribute(paramBoolean);
    if (!paramBoolean) {
      ownerDocument.removeIdentifier(localAttr.getValue());
    } else {
      ownerDocument.putIdentifier(localAttr.getValue(), this);
    }
  }
  
  public void setIdAttributeNS(String paramString1, String paramString2, boolean paramBoolean)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    Attr localAttr = getAttributeNodeNS(paramString1, paramString2);
    String str;
    if (localAttr == null)
    {
      str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
      throw new DOMException((short)8, str);
    }
    if (ownerDocument.errorChecking)
    {
      if (isReadOnly())
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
        throw new DOMException((short)7, str);
      }
      if (localAttr.getOwnerElement() != this)
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
        throw new DOMException((short)8, str);
      }
    }
    ((AttrImpl)localAttr).isIdAttribute(paramBoolean);
    if (!paramBoolean) {
      ownerDocument.removeIdentifier(localAttr.getValue());
    } else {
      ownerDocument.putIdentifier(localAttr.getValue(), this);
    }
  }
  
  public String getTypeName()
  {
    return null;
  }
  
  public String getTypeNamespace()
  {
    return null;
  }
  
  public boolean isDerivedFrom(String paramString1, String paramString2, int paramInt)
  {
    return false;
  }
  
  public TypeInfo getSchemaTypeInfo()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return this;
  }
  
  public void setReadOnly(boolean paramBoolean1, boolean paramBoolean2)
  {
    super.setReadOnly(paramBoolean1, paramBoolean2);
    if (attributes != null) {
      attributes.setReadOnly(paramBoolean1, true);
    }
  }
  
  protected void synchronizeData()
  {
    needsSyncData(false);
    boolean bool = ownerDocument.getMutationEvents();
    ownerDocument.setMutationEvents(false);
    setupDefaultAttributes();
    ownerDocument.setMutationEvents(bool);
  }
  
  void moveSpecifiedAttributes(ElementImpl paramElementImpl)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (paramElementImpl.hasAttributes())
    {
      if (attributes == null) {
        attributes = new AttributeMap(this, null);
      }
      attributes.moveSpecifiedAttributes(attributes);
    }
  }
  
  protected void setupDefaultAttributes()
  {
    NamedNodeMapImpl localNamedNodeMapImpl = getDefaultAttributes();
    if (localNamedNodeMapImpl != null) {
      attributes = new AttributeMap(this, localNamedNodeMapImpl);
    }
  }
  
  protected void reconcileDefaultAttributes()
  {
    if (attributes != null)
    {
      NamedNodeMapImpl localNamedNodeMapImpl = getDefaultAttributes();
      attributes.reconcileDefaults(localNamedNodeMapImpl);
    }
  }
  
  protected NamedNodeMapImpl getDefaultAttributes()
  {
    DocumentTypeImpl localDocumentTypeImpl = (DocumentTypeImpl)ownerDocument.getDoctype();
    if (localDocumentTypeImpl == null) {
      return null;
    }
    ElementDefinitionImpl localElementDefinitionImpl = (ElementDefinitionImpl)localDocumentTypeImpl.getElements().getNamedItem(getNodeName());
    if (localElementDefinitionImpl == null) {
      return null;
    }
    return (NamedNodeMapImpl)localElementDefinitionImpl.getAttributes();
  }
  
  public final int getChildElementCount()
  {
    int i = 0;
    for (Element localElement = getFirstElementChild(); localElement != null; localElement = ((ElementImpl)localElement).getNextElementSibling()) {
      i++;
    }
    return i;
  }
  
  public final Element getFirstElementChild()
  {
    for (Node localNode = getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      switch (localNode.getNodeType())
      {
      case 1: 
        return (Element)localNode;
      case 5: 
        Element localElement = getFirstElementChild(localNode);
        if (localElement != null) {
          return localElement;
        }
        break;
      }
    }
    return null;
  }
  
  public final Element getLastElementChild()
  {
    for (Node localNode = getLastChild(); localNode != null; localNode = localNode.getPreviousSibling()) {
      switch (localNode.getNodeType())
      {
      case 1: 
        return (Element)localNode;
      case 5: 
        Element localElement = getLastElementChild(localNode);
        if (localElement != null) {
          return localElement;
        }
        break;
      }
    }
    return null;
  }
  
  public final Element getNextElementSibling()
  {
    for (Node localNode = getNextLogicalSibling(this); localNode != null; localNode = getNextLogicalSibling(localNode)) {
      switch (localNode.getNodeType())
      {
      case 1: 
        return (Element)localNode;
      case 5: 
        Element localElement = getFirstElementChild(localNode);
        if (localElement != null) {
          return localElement;
        }
        break;
      }
    }
    return null;
  }
  
  public final Element getPreviousElementSibling()
  {
    for (Node localNode = getPreviousLogicalSibling(this); localNode != null; localNode = getPreviousLogicalSibling(localNode)) {
      switch (localNode.getNodeType())
      {
      case 1: 
        return (Element)localNode;
      case 5: 
        Element localElement = getLastElementChild(localNode);
        if (localElement != null) {
          return localElement;
        }
        break;
      }
    }
    return null;
  }
  
  private Element getFirstElementChild(Node paramNode)
  {
    Node localNode1 = paramNode;
    while (paramNode != null)
    {
      if (paramNode.getNodeType() == 1) {
        return (Element)paramNode;
      }
      Node localNode2 = paramNode.getFirstChild();
      while (localNode2 == null)
      {
        if (localNode1 == paramNode) {
          break;
        }
        localNode2 = paramNode.getNextSibling();
        if (localNode2 == null)
        {
          paramNode = paramNode.getParentNode();
          if ((paramNode == null) || (localNode1 == paramNode)) {
            return null;
          }
        }
      }
      paramNode = localNode2;
    }
    return null;
  }
  
  private Element getLastElementChild(Node paramNode)
  {
    Node localNode1 = paramNode;
    while (paramNode != null)
    {
      if (paramNode.getNodeType() == 1) {
        return (Element)paramNode;
      }
      Node localNode2 = paramNode.getLastChild();
      while (localNode2 == null)
      {
        if (localNode1 == paramNode) {
          break;
        }
        localNode2 = paramNode.getPreviousSibling();
        if (localNode2 == null)
        {
          paramNode = paramNode.getParentNode();
          if ((paramNode == null) || (localNode1 == paramNode)) {
            return null;
          }
        }
      }
      paramNode = localNode2;
    }
    return null;
  }
  
  private Node getNextLogicalSibling(Node paramNode)
  {
    Node localNode1 = paramNode.getNextSibling();
    if (localNode1 == null) {
      for (Node localNode2 = paramNode.getParentNode(); (localNode2 != null) && (localNode2.getNodeType() == 5); localNode2 = localNode2.getParentNode())
      {
        localNode1 = localNode2.getNextSibling();
        if (localNode1 != null) {
          break;
        }
      }
    }
    return localNode1;
  }
  
  private Node getPreviousLogicalSibling(Node paramNode)
  {
    Node localNode1 = paramNode.getPreviousSibling();
    if (localNode1 == null) {
      for (Node localNode2 = paramNode.getParentNode(); (localNode2 != null) && (localNode2.getNodeType() == 5); localNode2 = localNode2.getParentNode())
      {
        localNode1 = localNode2.getPreviousSibling();
        if (localNode1 != null) {
          break;
        }
      }
    }
    return localNode1;
  }
}
