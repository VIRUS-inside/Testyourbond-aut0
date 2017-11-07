package org.apache.xerces.dom;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public abstract class NodeImpl
  implements Node, NodeList, EventTarget, Cloneable, Serializable
{
  public static final short TREE_POSITION_PRECEDING = 1;
  public static final short TREE_POSITION_FOLLOWING = 2;
  public static final short TREE_POSITION_ANCESTOR = 4;
  public static final short TREE_POSITION_DESCENDANT = 8;
  public static final short TREE_POSITION_EQUIVALENT = 16;
  public static final short TREE_POSITION_SAME_NODE = 32;
  public static final short TREE_POSITION_DISCONNECTED = 0;
  public static final short DOCUMENT_POSITION_DISCONNECTED = 1;
  public static final short DOCUMENT_POSITION_PRECEDING = 2;
  public static final short DOCUMENT_POSITION_FOLLOWING = 4;
  public static final short DOCUMENT_POSITION_CONTAINS = 8;
  public static final short DOCUMENT_POSITION_IS_CONTAINED = 16;
  public static final short DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;
  static final long serialVersionUID = -6316591992167219696L;
  public static final short ELEMENT_DEFINITION_NODE = 21;
  protected NodeImpl ownerNode;
  protected short flags;
  protected static final short READONLY = 1;
  protected static final short SYNCDATA = 2;
  protected static final short SYNCCHILDREN = 4;
  protected static final short OWNED = 8;
  protected static final short FIRSTCHILD = 16;
  protected static final short SPECIFIED = 32;
  protected static final short IGNORABLEWS = 64;
  protected static final short HASSTRING = 128;
  protected static final short NORMALIZED = 256;
  protected static final short ID = 512;
  
  protected NodeImpl(CoreDocumentImpl paramCoreDocumentImpl)
  {
    ownerNode = paramCoreDocumentImpl;
  }
  
  public NodeImpl() {}
  
  public abstract short getNodeType();
  
  public abstract String getNodeName();
  
  public String getNodeValue()
    throws DOMException
  {
    return null;
  }
  
  public void setNodeValue(String paramString)
    throws DOMException
  {}
  
  public Node appendChild(Node paramNode)
    throws DOMException
  {
    return insertBefore(paramNode, null);
  }
  
  public Node cloneNode(boolean paramBoolean)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    NodeImpl localNodeImpl;
    try
    {
      localNodeImpl = (NodeImpl)clone();
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
      throw new RuntimeException("**Internal Error**" + localCloneNotSupportedException);
    }
    ownerNode = ownerDocument();
    localNodeImpl.isOwned(false);
    localNodeImpl.isReadOnly(false);
    ownerDocument().callUserDataHandlers(this, localNodeImpl, (short)1);
    return localNodeImpl;
  }
  
  public Document getOwnerDocument()
  {
    if (isOwned()) {
      return ownerNode.ownerDocument();
    }
    return (Document)ownerNode;
  }
  
  CoreDocumentImpl ownerDocument()
  {
    if (isOwned()) {
      return ownerNode.ownerDocument();
    }
    return (CoreDocumentImpl)ownerNode;
  }
  
  protected void setOwnerDocument(CoreDocumentImpl paramCoreDocumentImpl)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (!isOwned()) {
      ownerNode = paramCoreDocumentImpl;
    }
  }
  
  protected int getNodeNumber()
  {
    CoreDocumentImpl localCoreDocumentImpl = (CoreDocumentImpl)getOwnerDocument();
    int i = localCoreDocumentImpl.getNodeNumber(this);
    return i;
  }
  
  public Node getParentNode()
  {
    return null;
  }
  
  NodeImpl parentNode()
  {
    return null;
  }
  
  public Node getNextSibling()
  {
    return null;
  }
  
  public Node getPreviousSibling()
  {
    return null;
  }
  
  ChildNode previousSibling()
  {
    return null;
  }
  
  public NamedNodeMap getAttributes()
  {
    return null;
  }
  
  public boolean hasAttributes()
  {
    return false;
  }
  
  public boolean hasChildNodes()
  {
    return false;
  }
  
  public NodeList getChildNodes()
  {
    return this;
  }
  
  public Node getFirstChild()
  {
    return null;
  }
  
  public Node getLastChild()
  {
    return null;
  }
  
  public Node insertBefore(Node paramNode1, Node paramNode2)
    throws DOMException
  {
    throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
  }
  
  public Node removeChild(Node paramNode)
    throws DOMException
  {
    throw new DOMException((short)8, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null));
  }
  
  public Node replaceChild(Node paramNode1, Node paramNode2)
    throws DOMException
  {
    throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
  }
  
  public int getLength()
  {
    return 0;
  }
  
  public Node item(int paramInt)
  {
    return null;
  }
  
  public void normalize() {}
  
  public boolean isSupported(String paramString1, String paramString2)
  {
    return ownerDocument().getImplementation().hasFeature(paramString1, paramString2);
  }
  
  public String getNamespaceURI()
  {
    return null;
  }
  
  public String getPrefix()
  {
    return null;
  }
  
  public void setPrefix(String paramString)
    throws DOMException
  {
    throw new DOMException((short)14, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null));
  }
  
  public String getLocalName()
  {
    return null;
  }
  
  public void addEventListener(String paramString, EventListener paramEventListener, boolean paramBoolean)
  {
    ownerDocument().addEventListener(this, paramString, paramEventListener, paramBoolean);
  }
  
  public void removeEventListener(String paramString, EventListener paramEventListener, boolean paramBoolean)
  {
    ownerDocument().removeEventListener(this, paramString, paramEventListener, paramBoolean);
  }
  
  public boolean dispatchEvent(Event paramEvent)
  {
    return ownerDocument().dispatchEvent(this, paramEvent);
  }
  
  public String getBaseURI()
  {
    return null;
  }
  
  /**
   * @deprecated
   */
  public short compareTreePosition(Node paramNode)
  {
    if (this == paramNode) {
      return 48;
    }
    int i = getNodeType();
    int j = paramNode.getNodeType();
    if ((i == 6) || (i == 12) || (j == 6) || (j == 12)) {
      return 0;
    }
    Object localObject2 = this;
    Object localObject3 = paramNode;
    int k = 0;
    int m = 0;
    for (Object localObject1 = this; localObject1 != null; localObject1 = ((Node)localObject1).getParentNode())
    {
      k++;
      if (localObject1 == paramNode) {
        return 5;
      }
      localObject2 = localObject1;
    }
    for (localObject1 = paramNode; localObject1 != null; localObject1 = ((Node)localObject1).getParentNode())
    {
      m++;
      if (localObject1 == this) {
        return 10;
      }
      localObject3 = localObject1;
    }
    Object localObject4 = this;
    Object localObject5 = paramNode;
    int n = ((Node)localObject2).getNodeType();
    int i1 = ((Node)localObject3).getNodeType();
    if (n == 2) {
      localObject4 = ((AttrImpl)localObject2).getOwnerElement();
    }
    if (i1 == 2) {
      localObject5 = ((AttrImpl)localObject3).getOwnerElement();
    }
    if ((n == 2) && (i1 == 2) && (localObject4 == localObject5)) {
      return 16;
    }
    if (n == 2)
    {
      k = 0;
      for (localObject1 = localObject4; localObject1 != null; localObject1 = ((Node)localObject1).getParentNode())
      {
        k++;
        if (localObject1 == localObject5) {
          return 1;
        }
        localObject2 = localObject1;
      }
    }
    if (i1 == 2)
    {
      m = 0;
      for (localObject1 = localObject5; localObject1 != null; localObject1 = ((Node)localObject1).getParentNode())
      {
        m++;
        if (localObject1 == localObject4) {
          return 2;
        }
        localObject3 = localObject1;
      }
    }
    if (localObject2 != localObject3) {
      return 0;
    }
    int i2;
    if (k > m)
    {
      for (i2 = 0; i2 < k - m; i2++) {
        localObject4 = ((Node)localObject4).getParentNode();
      }
      if (localObject4 == localObject5) {
        return 1;
      }
    }
    else
    {
      for (i2 = 0; i2 < m - k; i2++) {
        localObject5 = ((Node)localObject5).getParentNode();
      }
      if (localObject5 == localObject4) {
        return 2;
      }
    }
    Node localNode1 = ((Node)localObject4).getParentNode();
    for (Node localNode2 = ((Node)localObject5).getParentNode(); localNode1 != localNode2; localNode2 = localNode2.getParentNode())
    {
      localObject4 = localNode1;
      localObject5 = localNode2;
      localNode1 = localNode1.getParentNode();
    }
    for (Node localNode3 = localNode1.getFirstChild(); localNode3 != null; localNode3 = localNode3.getNextSibling())
    {
      if (localNode3 == localObject5) {
        return 1;
      }
      if (localNode3 == localObject4) {
        return 2;
      }
    }
    return 0;
  }
  
  public short compareDocumentPosition(Node paramNode)
    throws DOMException
  {
    if (this == paramNode) {
      return 0;
    }
    Object localObject1;
    if ((paramNode != null) && (!(paramNode instanceof NodeImpl)))
    {
      localObject1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
      throw new DOMException((short)9, (String)localObject1);
    }
    if (getNodeType() == 9) {
      localObject1 = (Document)this;
    } else {
      localObject1 = getOwnerDocument();
    }
    Document localDocument;
    if (paramNode.getNodeType() == 9) {
      localDocument = (Document)paramNode;
    } else {
      localDocument = paramNode.getOwnerDocument();
    }
    if ((localObject1 != localDocument) && (localObject1 != null) && (localDocument != null))
    {
      int i = ((CoreDocumentImpl)localDocument).getNodeNumber();
      int j = ((CoreDocumentImpl)localObject1).getNodeNumber();
      if (i > j) {
        return 37;
      }
      return 35;
    }
    Object localObject3 = this;
    Object localObject4 = paramNode;
    int k = 0;
    int m = 0;
    for (Object localObject2 = this; localObject2 != null; localObject2 = ((Node)localObject2).getParentNode())
    {
      k++;
      if (localObject2 == paramNode) {
        return 10;
      }
      localObject3 = localObject2;
    }
    for (localObject2 = paramNode; localObject2 != null; localObject2 = ((Node)localObject2).getParentNode())
    {
      m++;
      if (localObject2 == this) {
        return 20;
      }
      localObject4 = localObject2;
    }
    int n = ((Node)localObject3).getNodeType();
    int i1 = ((Node)localObject4).getNodeType();
    Object localObject5 = this;
    Object localObject6 = paramNode;
    DocumentType localDocumentType;
    switch (n)
    {
    case 6: 
    case 12: 
      localDocumentType = ((Document)localObject1).getDoctype();
      if (localDocumentType == localObject4) {
        return 10;
      }
      switch (i1)
      {
      case 6: 
      case 12: 
        if (n != i1) {
          return n > i1 ? 2 : 4;
        }
        if (n == 12)
        {
          if (((NamedNodeMapImpl)localDocumentType.getNotations()).precedes((Node)localObject4, (Node)localObject3)) {
            return 34;
          }
          return 36;
        }
        if (((NamedNodeMapImpl)localDocumentType.getEntities()).precedes((Node)localObject4, (Node)localObject3)) {
          return 34;
        }
        return 36;
      }
      localObject5 = localObject3 = localObject1;
      break;
    case 10: 
      if (localObject6 == localObject1) {
        return 10;
      }
      if ((localObject1 != null) && (localObject1 == localDocument)) {
        return 4;
      }
      break;
    case 2: 
      localObject5 = ((AttrImpl)localObject3).getOwnerElement();
      if (i1 == 2)
      {
        localObject6 = ((AttrImpl)localObject4).getOwnerElement();
        if (localObject6 == localObject5)
        {
          if (((NamedNodeMapImpl)((Node)localObject5).getAttributes()).precedes(paramNode, this)) {
            return 34;
          }
          return 36;
        }
      }
      k = 0;
      for (localObject2 = localObject5; localObject2 != null; localObject2 = ((Node)localObject2).getParentNode())
      {
        k++;
        if (localObject2 == localObject6) {
          return 10;
        }
        localObject3 = localObject2;
      }
    }
    switch (i1)
    {
    case 6: 
    case 12: 
      localDocumentType = ((Document)localObject1).getDoctype();
      if (localDocumentType == this) {
        return 20;
      }
      localObject6 = localObject4 = localObject1;
      break;
    case 10: 
      if (localObject5 == localDocument) {
        return 20;
      }
      if ((localDocument != null) && (localObject1 == localDocument)) {
        return 2;
      }
      break;
    case 2: 
      m = 0;
      localObject6 = ((AttrImpl)localObject4).getOwnerElement();
      for (localObject2 = localObject6; localObject2 != null; localObject2 = ((Node)localObject2).getParentNode())
      {
        m++;
        if (localObject2 == localObject5) {
          return 20;
        }
        localObject4 = localObject2;
      }
    }
    int i2;
    if (localObject3 != localObject4)
    {
      i2 = ((NodeImpl)localObject3).getNodeNumber();
      int i3 = ((NodeImpl)localObject4).getNodeNumber();
      if (i2 > i3) {
        return 37;
      }
      return 35;
    }
    if (k > m)
    {
      for (i2 = 0; i2 < k - m; i2++) {
        localObject5 = ((Node)localObject5).getParentNode();
      }
      if (localObject5 == localObject6) {
        return 2;
      }
    }
    else
    {
      for (i2 = 0; i2 < m - k; i2++) {
        localObject6 = ((Node)localObject6).getParentNode();
      }
      if (localObject6 == localObject5) {
        return 4;
      }
    }
    Node localNode1 = ((Node)localObject5).getParentNode();
    for (Node localNode2 = ((Node)localObject6).getParentNode(); localNode1 != localNode2; localNode2 = localNode2.getParentNode())
    {
      localObject5 = localNode1;
      localObject6 = localNode2;
      localNode1 = localNode1.getParentNode();
    }
    for (Node localNode3 = localNode1.getFirstChild(); localNode3 != null; localNode3 = localNode3.getNextSibling())
    {
      if (localNode3 == localObject6) {
        return 2;
      }
      if (localNode3 == localObject5) {
        return 4;
      }
    }
    return 0;
  }
  
  public String getTextContent()
    throws DOMException
  {
    return getNodeValue();
  }
  
  void getTextContent(StringBuffer paramStringBuffer)
    throws DOMException
  {
    String str = getNodeValue();
    if (str != null) {
      paramStringBuffer.append(str);
    }
  }
  
  public void setTextContent(String paramString)
    throws DOMException
  {
    setNodeValue(paramString);
  }
  
  public boolean isSameNode(Node paramNode)
  {
    return this == paramNode;
  }
  
  public boolean isDefaultNamespace(String paramString)
  {
    int i = getNodeType();
    switch (i)
    {
    case 1: 
      localObject1 = getNamespaceURI();
      String str1 = getPrefix();
      if ((str1 == null) || (str1.length() == 0))
      {
        if (paramString == null) {
          return localObject1 == paramString;
        }
        return paramString.equals(localObject1);
      }
      if (hasAttributes())
      {
        localObject2 = (ElementImpl)this;
        NodeImpl localNodeImpl = (NodeImpl)((ElementImpl)localObject2).getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns");
        if (localNodeImpl != null)
        {
          String str2 = localNodeImpl.getNodeValue();
          if (paramString == null) {
            return localObject1 == str2;
          }
          return paramString.equals(str2);
        }
      }
      Object localObject2 = (NodeImpl)getElementAncestor(this);
      if (localObject2 != null) {
        return ((NodeImpl)localObject2).isDefaultNamespace(paramString);
      }
      return false;
    case 9: 
      return ((NodeImpl)((Document)this).getDocumentElement()).isDefaultNamespace(paramString);
    case 6: 
    case 10: 
    case 11: 
    case 12: 
      return false;
    case 2: 
      if (ownerNode.getNodeType() == 1) {
        return ownerNode.isDefaultNamespace(paramString);
      }
      return false;
    }
    Object localObject1 = (NodeImpl)getElementAncestor(this);
    if (localObject1 != null) {
      return ((NodeImpl)localObject1).isDefaultNamespace(paramString);
    }
    return false;
  }
  
  public String lookupPrefix(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = getNodeType();
    switch (i)
    {
    case 1: 
      getNamespaceURI();
      return lookupNamespacePrefix(paramString, (ElementImpl)this);
    case 9: 
      return ((NodeImpl)((Document)this).getDocumentElement()).lookupPrefix(paramString);
    case 6: 
    case 10: 
    case 11: 
    case 12: 
      return null;
    case 2: 
      if (ownerNode.getNodeType() == 1) {
        return ownerNode.lookupPrefix(paramString);
      }
      return null;
    }
    NodeImpl localNodeImpl = (NodeImpl)getElementAncestor(this);
    if (localNodeImpl != null) {
      return localNodeImpl.lookupPrefix(paramString);
    }
    return null;
  }
  
  public String lookupNamespaceURI(String paramString)
  {
    int i = getNodeType();
    switch (i)
    {
    case 1: 
      localObject1 = getNamespaceURI();
      String str1 = getPrefix();
      if (localObject1 != null)
      {
        if ((paramString == null) && (str1 == paramString)) {
          return localObject1;
        }
        if ((str1 != null) && (str1.equals(paramString))) {
          return localObject1;
        }
      }
      if (hasAttributes())
      {
        localObject2 = getAttributes();
        int j = ((NamedNodeMap)localObject2).getLength();
        for (int k = 0; k < j; k++)
        {
          Node localNode = ((NamedNodeMap)localObject2).item(k);
          String str2 = localNode.getPrefix();
          String str3 = localNode.getNodeValue();
          localObject1 = localNode.getNamespaceURI();
          if ((localObject1 != null) && (((String)localObject1).equals("http://www.w3.org/2000/xmlns/")))
          {
            if ((paramString == null) && (localNode.getNodeName().equals("xmlns"))) {
              return str3.length() > 0 ? str3 : null;
            }
            if ((str2 != null) && (str2.equals("xmlns")) && (localNode.getLocalName().equals(paramString))) {
              return str3.length() > 0 ? str3 : null;
            }
          }
        }
      }
      Object localObject2 = (NodeImpl)getElementAncestor(this);
      if (localObject2 != null) {
        return ((NodeImpl)localObject2).lookupNamespaceURI(paramString);
      }
      return null;
    case 9: 
      return ((NodeImpl)((Document)this).getDocumentElement()).lookupNamespaceURI(paramString);
    case 6: 
    case 10: 
    case 11: 
    case 12: 
      return null;
    case 2: 
      if (ownerNode.getNodeType() == 1) {
        return ownerNode.lookupNamespaceURI(paramString);
      }
      return null;
    }
    Object localObject1 = (NodeImpl)getElementAncestor(this);
    if (localObject1 != null) {
      return ((NodeImpl)localObject1).lookupNamespaceURI(paramString);
    }
    return null;
  }
  
  Node getElementAncestor(Node paramNode)
  {
    for (Node localNode = paramNode.getParentNode(); localNode != null; localNode = localNode.getParentNode())
    {
      int i = localNode.getNodeType();
      if (i == 1) {
        return localNode;
      }
    }
    return null;
  }
  
  String lookupNamespacePrefix(String paramString, ElementImpl paramElementImpl)
  {
    String str1 = getNamespaceURI();
    String str2 = getPrefix();
    if ((str1 != null) && (str1.equals(paramString)) && (str2 != null))
    {
      localObject = paramElementImpl.lookupNamespaceURI(str2);
      if ((localObject != null) && (((String)localObject).equals(paramString))) {
        return str2;
      }
    }
    if (hasAttributes())
    {
      localObject = getAttributes();
      int i = ((NamedNodeMap)localObject).getLength();
      for (int j = 0; j < i; j++)
      {
        Node localNode = ((NamedNodeMap)localObject).item(j);
        String str3 = localNode.getPrefix();
        String str4 = localNode.getNodeValue();
        str1 = localNode.getNamespaceURI();
        if ((str1 != null) && (str1.equals("http://www.w3.org/2000/xmlns/")) && ((localNode.getNodeName().equals("xmlns")) || ((str3 != null) && (str3.equals("xmlns")) && (str4.equals(paramString)))))
        {
          String str5 = localNode.getLocalName();
          String str6 = paramElementImpl.lookupNamespaceURI(str5);
          if ((str6 != null) && (str6.equals(paramString))) {
            return str5;
          }
        }
      }
    }
    Object localObject = (NodeImpl)getElementAncestor(this);
    if (localObject != null) {
      return ((NodeImpl)localObject).lookupNamespacePrefix(paramString, paramElementImpl);
    }
    return null;
  }
  
  public boolean isEqualNode(Node paramNode)
  {
    if (paramNode == this) {
      return true;
    }
    if (paramNode.getNodeType() != getNodeType()) {
      return false;
    }
    if (getNodeName() == null)
    {
      if (paramNode.getNodeName() != null) {
        return false;
      }
    }
    else if (!getNodeName().equals(paramNode.getNodeName())) {
      return false;
    }
    if (getLocalName() == null)
    {
      if (paramNode.getLocalName() != null) {
        return false;
      }
    }
    else if (!getLocalName().equals(paramNode.getLocalName())) {
      return false;
    }
    if (getNamespaceURI() == null)
    {
      if (paramNode.getNamespaceURI() != null) {
        return false;
      }
    }
    else if (!getNamespaceURI().equals(paramNode.getNamespaceURI())) {
      return false;
    }
    if (getPrefix() == null)
    {
      if (paramNode.getPrefix() != null) {
        return false;
      }
    }
    else if (!getPrefix().equals(paramNode.getPrefix())) {
      return false;
    }
    if (getNodeValue() == null)
    {
      if (paramNode.getNodeValue() != null) {
        return false;
      }
    }
    else if (!getNodeValue().equals(paramNode.getNodeValue())) {
      return false;
    }
    return true;
  }
  
  public Object getFeature(String paramString1, String paramString2)
  {
    return isSupported(paramString1, paramString2) ? this : null;
  }
  
  public Object setUserData(String paramString, Object paramObject, UserDataHandler paramUserDataHandler)
  {
    return ownerDocument().setUserData(this, paramString, paramObject, paramUserDataHandler);
  }
  
  public Object getUserData(String paramString)
  {
    return ownerDocument().getUserData(this, paramString);
  }
  
  protected Hashtable getUserDataRecord()
  {
    return ownerDocument().getUserDataRecord(this);
  }
  
  public void setReadOnly(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    isReadOnly(paramBoolean1);
  }
  
  public boolean getReadOnly()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return isReadOnly();
  }
  
  public void setUserData(Object paramObject)
  {
    ownerDocument().setUserData(this, paramObject);
  }
  
  public Object getUserData()
  {
    return ownerDocument().getUserData(this);
  }
  
  protected void changed()
  {
    ownerDocument().changed();
  }
  
  protected int changes()
  {
    return ownerDocument().changes();
  }
  
  protected void synchronizeData()
  {
    needsSyncData(false);
  }
  
  protected Node getContainer()
  {
    return null;
  }
  
  final boolean isReadOnly()
  {
    return (flags & 0x1) != 0;
  }
  
  final void isReadOnly(boolean paramBoolean)
  {
    flags = (paramBoolean ? (short)(flags | 0x1) : (short)(flags & 0xFFFFFFFE));
  }
  
  final boolean needsSyncData()
  {
    return (flags & 0x2) != 0;
  }
  
  final void needsSyncData(boolean paramBoolean)
  {
    flags = (paramBoolean ? (short)(flags | 0x2) : (short)(flags & 0xFFFFFFFD));
  }
  
  final boolean needsSyncChildren()
  {
    return (flags & 0x4) != 0;
  }
  
  public final void needsSyncChildren(boolean paramBoolean)
  {
    flags = (paramBoolean ? (short)(flags | 0x4) : (short)(flags & 0xFFFFFFFB));
  }
  
  final boolean isOwned()
  {
    return (flags & 0x8) != 0;
  }
  
  final void isOwned(boolean paramBoolean)
  {
    flags = (paramBoolean ? (short)(flags | 0x8) : (short)(flags & 0xFFFFFFF7));
  }
  
  final boolean isFirstChild()
  {
    return (flags & 0x10) != 0;
  }
  
  final void isFirstChild(boolean paramBoolean)
  {
    flags = (paramBoolean ? (short)(flags | 0x10) : (short)(flags & 0xFFFFFFEF));
  }
  
  final boolean isSpecified()
  {
    return (flags & 0x20) != 0;
  }
  
  final void isSpecified(boolean paramBoolean)
  {
    flags = (paramBoolean ? (short)(flags | 0x20) : (short)(flags & 0xFFFFFFDF));
  }
  
  final boolean internalIsIgnorableWhitespace()
  {
    return (flags & 0x40) != 0;
  }
  
  final void isIgnorableWhitespace(boolean paramBoolean)
  {
    flags = (paramBoolean ? (short)(flags | 0x40) : (short)(flags & 0xFFFFFFBF));
  }
  
  final boolean hasStringValue()
  {
    return (flags & 0x80) != 0;
  }
  
  final void hasStringValue(boolean paramBoolean)
  {
    flags = (paramBoolean ? (short)(flags | 0x80) : (short)(flags & 0xFF7F));
  }
  
  final boolean isNormalized()
  {
    return (flags & 0x100) != 0;
  }
  
  final void isNormalized(boolean paramBoolean)
  {
    if ((!paramBoolean) && (isNormalized()) && (ownerNode != null)) {
      ownerNode.isNormalized(false);
    }
    flags = (paramBoolean ? (short)(flags | 0x100) : (short)(flags & 0xFEFF));
  }
  
  final boolean isIdAttribute()
  {
    return (flags & 0x200) != 0;
  }
  
  final void isIdAttribute(boolean paramBoolean)
  {
    flags = (paramBoolean ? (short)(flags | 0x200) : (short)(flags & 0xFDFF));
  }
  
  public String toString()
  {
    return "[" + getNodeName() + ": " + getNodeValue() + "]";
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    paramObjectOutputStream.defaultWriteObject();
  }
}
