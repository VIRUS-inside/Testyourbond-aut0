package org.apache.xerces.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.TypeInfo;

public class AttrImpl
  extends NodeImpl
  implements Attr, TypeInfo
{
  static final long serialVersionUID = 7277707688218972102L;
  static final String DTD_URI = "http://www.w3.org/TR/REC-xml";
  protected Object value = null;
  protected String name;
  transient Object type;
  
  protected AttrImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString)
  {
    super(paramCoreDocumentImpl);
    name = paramString;
    isSpecified(true);
    hasStringValue(true);
  }
  
  protected AttrImpl() {}
  
  void rename(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    name = paramString;
  }
  
  protected void makeChildNode()
  {
    if (hasStringValue())
    {
      if (value != null)
      {
        TextImpl localTextImpl = (TextImpl)ownerDocument().createTextNode((String)value);
        value = localTextImpl;
        localTextImpl.isFirstChild(true);
        previousSibling = localTextImpl;
        ownerNode = this;
        localTextImpl.isOwned(true);
      }
      hasStringValue(false);
    }
  }
  
  protected void setOwnerDocument(CoreDocumentImpl paramCoreDocumentImpl)
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    super.setOwnerDocument(paramCoreDocumentImpl);
    if (!hasStringValue()) {
      for (ChildNode localChildNode = (ChildNode)value; localChildNode != null; localChildNode = nextSibling) {
        localChildNode.setOwnerDocument(paramCoreDocumentImpl);
      }
    }
  }
  
  public void setIdAttribute(boolean paramBoolean)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    isIdAttribute(paramBoolean);
  }
  
  public boolean isId()
  {
    return isIdAttribute();
  }
  
  public Node cloneNode(boolean paramBoolean)
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    AttrImpl localAttrImpl = (AttrImpl)super.cloneNode(paramBoolean);
    if (!localAttrImpl.hasStringValue())
    {
      value = null;
      for (Node localNode = (Node)value; localNode != null; localNode = localNode.getNextSibling()) {
        localAttrImpl.appendChild(localNode.cloneNode(true));
      }
    }
    localAttrImpl.isSpecified(true);
    return localAttrImpl;
  }
  
  public short getNodeType()
  {
    return 2;
  }
  
  public String getNodeName()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return name;
  }
  
  public void setNodeValue(String paramString)
    throws DOMException
  {
    setValue(paramString);
  }
  
  public String getTypeName()
  {
    return (String)type;
  }
  
  public String getTypeNamespace()
  {
    if (type != null) {
      return "http://www.w3.org/TR/REC-xml";
    }
    return null;
  }
  
  public TypeInfo getSchemaTypeInfo()
  {
    return this;
  }
  
  public String getNodeValue()
  {
    return getValue();
  }
  
  public String getName()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return name;
  }
  
  public void setValue(String paramString)
  {
    CoreDocumentImpl localCoreDocumentImpl = ownerDocument();
    if ((errorChecking) && (isReadOnly()))
    {
      localObject = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
      throw new DOMException((short)7, (String)localObject);
    }
    Object localObject = getOwnerElement();
    String str = "";
    TextImpl localTextImpl = null;
    if (needsSyncData()) {
      synchronizeData();
    }
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    if (value != null)
    {
      if (localCoreDocumentImpl.getMutationEvents())
      {
        if (hasStringValue())
        {
          str = (String)value;
          localTextImpl = (TextImpl)localCoreDocumentImpl.createTextNode((String)value);
          value = localTextImpl;
          localTextImpl.isFirstChild(true);
          previousSibling = localTextImpl;
          ownerNode = this;
          localTextImpl.isOwned(true);
          hasStringValue(false);
          internalRemoveChild(localTextImpl, true);
        }
        else
        {
          str = getValue();
          while (value != null) {
            internalRemoveChild((Node)value, true);
          }
        }
      }
      else
      {
        if (hasStringValue())
        {
          str = (String)value;
        }
        else
        {
          str = getValue();
          ChildNode localChildNode = (ChildNode)value;
          previousSibling = null;
          localChildNode.isFirstChild(false);
          ownerNode = localCoreDocumentImpl;
        }
        value = null;
        needsSyncChildren(false);
      }
      if ((isIdAttribute()) && (localObject != null)) {
        localCoreDocumentImpl.removeIdentifier(str);
      }
    }
    isSpecified(true);
    if (localCoreDocumentImpl.getMutationEvents())
    {
      if (localTextImpl == null) {
        localTextImpl = (TextImpl)localCoreDocumentImpl.createTextNode(paramString);
      } else {
        data = paramString;
      }
      internalInsertBefore(localTextImpl, null, true);
      hasStringValue(false);
      localCoreDocumentImpl.modifiedAttrValue(this, str);
    }
    else
    {
      value = paramString;
      hasStringValue(true);
      changed();
    }
    if ((isIdAttribute()) && (localObject != null)) {
      localCoreDocumentImpl.putIdentifier(paramString, (Element)localObject);
    }
  }
  
  public String getValue()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    if (value == null) {
      return "";
    }
    if (hasStringValue()) {
      return (String)value;
    }
    ChildNode localChildNode1 = (ChildNode)value;
    String str = null;
    if (localChildNode1.getNodeType() == 5) {
      str = ((EntityReferenceImpl)localChildNode1).getEntityRefValue();
    } else {
      str = localChildNode1.getNodeValue();
    }
    ChildNode localChildNode2 = nextSibling;
    if ((localChildNode2 == null) || (str == null)) {
      return str == null ? "" : str;
    }
    StringBuffer localStringBuffer = new StringBuffer(str);
    while (localChildNode2 != null)
    {
      if (localChildNode2.getNodeType() == 5)
      {
        str = ((EntityReferenceImpl)localChildNode2).getEntityRefValue();
        if (str == null) {
          return "";
        }
        localStringBuffer.append(str);
      }
      else
      {
        localStringBuffer.append(localChildNode2.getNodeValue());
      }
      localChildNode2 = nextSibling;
    }
    return localStringBuffer.toString();
  }
  
  public boolean getSpecified()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return isSpecified();
  }
  
  /**
   * @deprecated
   */
  public Element getElement()
  {
    return (Element)(isOwned() ? ownerNode : null);
  }
  
  public Element getOwnerElement()
  {
    return (Element)(isOwned() ? ownerNode : null);
  }
  
  public void normalize()
  {
    if ((isNormalized()) || (hasStringValue())) {
      return;
    }
    ChildNode localChildNode = (ChildNode)value;
    Object localObject2;
    for (Object localObject1 = localChildNode; localObject1 != null; localObject1 = localObject2)
    {
      localObject2 = ((Node)localObject1).getNextSibling();
      if (((Node)localObject1).getNodeType() == 3) {
        if ((localObject2 != null) && (((Node)localObject2).getNodeType() == 3))
        {
          ((Text)localObject1).appendData(((Node)localObject2).getNodeValue());
          removeChild((Node)localObject2);
          localObject2 = localObject1;
        }
        else if ((((Node)localObject1).getNodeValue() == null) || (((Node)localObject1).getNodeValue().length() == 0))
        {
          removeChild((Node)localObject1);
        }
      }
    }
    isNormalized(true);
  }
  
  public void setSpecified(boolean paramBoolean)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    isSpecified(paramBoolean);
  }
  
  public void setType(Object paramObject)
  {
    type = paramObject;
  }
  
  public String toString()
  {
    return getName() + "=" + "\"" + getValue() + "\"";
  }
  
  public boolean hasChildNodes()
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    return value != null;
  }
  
  public NodeList getChildNodes()
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    return this;
  }
  
  public Node getFirstChild()
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    makeChildNode();
    return (Node)value;
  }
  
  public Node getLastChild()
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    return lastChild();
  }
  
  final ChildNode lastChild()
  {
    makeChildNode();
    return value != null ? value).previousSibling : null;
  }
  
  final void lastChild(ChildNode paramChildNode)
  {
    if (value != null) {
      value).previousSibling = paramChildNode;
    }
  }
  
  public Node insertBefore(Node paramNode1, Node paramNode2)
    throws DOMException
  {
    return internalInsertBefore(paramNode1, paramNode2, false);
  }
  
  Node internalInsertBefore(Node paramNode1, Node paramNode2, boolean paramBoolean)
    throws DOMException
  {
    CoreDocumentImpl localCoreDocumentImpl = ownerDocument();
    boolean bool = errorChecking;
    Object localObject1;
    if (paramNode1.getNodeType() == 11)
    {
      if (bool) {
        for (localObject1 = paramNode1.getFirstChild(); localObject1 != null; localObject1 = ((Node)localObject1).getNextSibling()) {
          if (!localCoreDocumentImpl.isKidOK(this, (Node)localObject1))
          {
            localObject2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null);
            throw new DOMException((short)3, (String)localObject2);
          }
        }
      }
      while (paramNode1.hasChildNodes()) {
        insertBefore(paramNode1.getFirstChild(), paramNode2);
      }
      return paramNode1;
    }
    if (paramNode1 == paramNode2)
    {
      paramNode2 = paramNode2.getNextSibling();
      removeChild(paramNode1);
      insertBefore(paramNode1, paramNode2);
      return paramNode1;
    }
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    if (bool)
    {
      if (isReadOnly())
      {
        localObject1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
        throw new DOMException((short)7, (String)localObject1);
      }
      if (paramNode1.getOwnerDocument() != localCoreDocumentImpl)
      {
        localObject1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
        throw new DOMException((short)4, (String)localObject1);
      }
      if (!localCoreDocumentImpl.isKidOK(this, paramNode1))
      {
        localObject1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null);
        throw new DOMException((short)3, (String)localObject1);
      }
      if ((paramNode2 != null) && (paramNode2.getParentNode() != this))
      {
        localObject1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
        throw new DOMException((short)8, (String)localObject1);
      }
      int i = 1;
      for (localObject2 = this; (i != 0) && (localObject2 != null); localObject2 = ((NodeImpl)localObject2).parentNode()) {
        i = paramNode1 != localObject2 ? 1 : 0;
      }
      if (i == 0)
      {
        localObject3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null);
        throw new DOMException((short)3, (String)localObject3);
      }
    }
    makeChildNode();
    localCoreDocumentImpl.insertingNode(this, paramBoolean);
    ChildNode localChildNode1 = (ChildNode)paramNode1;
    Object localObject2 = localChildNode1.parentNode();
    if (localObject2 != null) {
      ((Node)localObject2).removeChild(localChildNode1);
    }
    Object localObject3 = (ChildNode)paramNode2;
    ownerNode = this;
    localChildNode1.isOwned(true);
    ChildNode localChildNode2 = (ChildNode)value;
    if (localChildNode2 == null)
    {
      value = localChildNode1;
      localChildNode1.isFirstChild(true);
      previousSibling = localChildNode1;
    }
    else
    {
      ChildNode localChildNode3;
      if (localObject3 == null)
      {
        localChildNode3 = previousSibling;
        nextSibling = localChildNode1;
        previousSibling = localChildNode3;
        previousSibling = localChildNode1;
      }
      else if (paramNode2 == localChildNode2)
      {
        localChildNode2.isFirstChild(false);
        nextSibling = localChildNode2;
        previousSibling = previousSibling;
        previousSibling = localChildNode1;
        value = localChildNode1;
        localChildNode1.isFirstChild(true);
      }
      else
      {
        localChildNode3 = previousSibling;
        nextSibling = ((ChildNode)localObject3);
        nextSibling = localChildNode1;
        previousSibling = localChildNode1;
        previousSibling = localChildNode3;
      }
    }
    changed();
    localCoreDocumentImpl.insertedNode(this, localChildNode1, paramBoolean);
    checkNormalizationAfterInsert(localChildNode1);
    return paramNode1;
  }
  
  public Node removeChild(Node paramNode)
    throws DOMException
  {
    if (hasStringValue())
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
      throw new DOMException((short)8, str);
    }
    return internalRemoveChild(paramNode, false);
  }
  
  Node internalRemoveChild(Node paramNode, boolean paramBoolean)
    throws DOMException
  {
    CoreDocumentImpl localCoreDocumentImpl = ownerDocument();
    if (errorChecking)
    {
      if (isReadOnly())
      {
        localObject = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
        throw new DOMException((short)7, (String)localObject);
      }
      if ((paramNode != null) && (paramNode.getParentNode() != this))
      {
        localObject = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null);
        throw new DOMException((short)8, (String)localObject);
      }
    }
    Object localObject = (ChildNode)paramNode;
    localCoreDocumentImpl.removingNode(this, (NodeImpl)localObject, paramBoolean);
    if (localObject == value)
    {
      ((ChildNode)localObject).isFirstChild(false);
      value = nextSibling;
      localChildNode1 = (ChildNode)value;
      if (localChildNode1 != null)
      {
        localChildNode1.isFirstChild(true);
        previousSibling = previousSibling;
      }
    }
    else
    {
      localChildNode1 = previousSibling;
      ChildNode localChildNode2 = nextSibling;
      nextSibling = localChildNode2;
      if (localChildNode2 == null)
      {
        ChildNode localChildNode3 = (ChildNode)value;
        previousSibling = localChildNode1;
      }
      else
      {
        previousSibling = localChildNode1;
      }
    }
    ChildNode localChildNode1 = ((ChildNode)localObject).previousSibling();
    ownerNode = localCoreDocumentImpl;
    ((ChildNode)localObject).isOwned(false);
    nextSibling = null;
    previousSibling = null;
    changed();
    localCoreDocumentImpl.removedNode(this, paramBoolean);
    checkNormalizationAfterRemove(localChildNode1);
    return localObject;
  }
  
  public Node replaceChild(Node paramNode1, Node paramNode2)
    throws DOMException
  {
    makeChildNode();
    CoreDocumentImpl localCoreDocumentImpl = ownerDocument();
    localCoreDocumentImpl.replacingNode(this);
    internalInsertBefore(paramNode1, paramNode2, true);
    if (paramNode1 != paramNode2) {
      internalRemoveChild(paramNode2, true);
    }
    localCoreDocumentImpl.replacedNode(this);
    return paramNode2;
  }
  
  public int getLength()
  {
    if (hasStringValue()) {
      return 1;
    }
    ChildNode localChildNode = (ChildNode)value;
    int i = 0;
    while (localChildNode != null)
    {
      i++;
      localChildNode = nextSibling;
    }
    return i;
  }
  
  public Node item(int paramInt)
  {
    if (hasStringValue())
    {
      if ((paramInt != 0) || (value == null)) {
        return null;
      }
      makeChildNode();
      return (Node)value;
    }
    if (paramInt < 0) {
      return null;
    }
    ChildNode localChildNode = (ChildNode)value;
    for (int i = 0; (i < paramInt) && (localChildNode != null); i++) {
      localChildNode = nextSibling;
    }
    return localChildNode;
  }
  
  public boolean isEqualNode(Node paramNode)
  {
    return super.isEqualNode(paramNode);
  }
  
  public boolean isDerivedFrom(String paramString1, String paramString2, int paramInt)
  {
    return false;
  }
  
  public void setReadOnly(boolean paramBoolean1, boolean paramBoolean2)
  {
    super.setReadOnly(paramBoolean1, paramBoolean2);
    if (paramBoolean2)
    {
      if (needsSyncChildren()) {
        synchronizeChildren();
      }
      if (hasStringValue()) {
        return;
      }
      for (ChildNode localChildNode = (ChildNode)value; localChildNode != null; localChildNode = nextSibling) {
        if (localChildNode.getNodeType() != 5) {
          localChildNode.setReadOnly(paramBoolean1, true);
        }
      }
    }
  }
  
  protected void synchronizeChildren()
  {
    needsSyncChildren(false);
  }
  
  void checkNormalizationAfterInsert(ChildNode paramChildNode)
  {
    if (paramChildNode.getNodeType() == 3)
    {
      ChildNode localChildNode1 = paramChildNode.previousSibling();
      ChildNode localChildNode2 = nextSibling;
      if (((localChildNode1 != null) && (localChildNode1.getNodeType() == 3)) || ((localChildNode2 != null) && (localChildNode2.getNodeType() == 3))) {
        isNormalized(false);
      }
    }
    else if (!paramChildNode.isNormalized())
    {
      isNormalized(false);
    }
  }
  
  void checkNormalizationAfterRemove(ChildNode paramChildNode)
  {
    if ((paramChildNode != null) && (paramChildNode.getNodeType() == 3))
    {
      ChildNode localChildNode = nextSibling;
      if ((localChildNode != null) && (localChildNode.getNodeType() == 3)) {
        isNormalized(false);
      }
    }
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    paramObjectOutputStream.defaultWriteObject();
  }
  
  private void readObject(ObjectInputStream paramObjectInputStream)
    throws ClassNotFoundException, IOException
  {
    paramObjectInputStream.defaultReadObject();
    needsSyncChildren(false);
  }
}
