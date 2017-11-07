package org.apache.xerces.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public abstract class ParentNode
  extends ChildNode
{
  static final long serialVersionUID = 2815829867152120872L;
  protected CoreDocumentImpl ownerDocument;
  protected ChildNode firstChild = null;
  protected transient NodeListCache fNodeListCache = null;
  
  protected ParentNode(CoreDocumentImpl paramCoreDocumentImpl)
  {
    super(paramCoreDocumentImpl);
    ownerDocument = paramCoreDocumentImpl;
  }
  
  public ParentNode() {}
  
  public Node cloneNode(boolean paramBoolean)
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    ParentNode localParentNode = (ParentNode)super.cloneNode(paramBoolean);
    ownerDocument = ownerDocument;
    firstChild = null;
    fNodeListCache = null;
    if (paramBoolean) {
      for (ChildNode localChildNode = firstChild; localChildNode != null; localChildNode = nextSibling) {
        localParentNode.appendChild(localChildNode.cloneNode(true));
      }
    }
    return localParentNode;
  }
  
  public Document getOwnerDocument()
  {
    return ownerDocument;
  }
  
  CoreDocumentImpl ownerDocument()
  {
    return ownerDocument;
  }
  
  protected void setOwnerDocument(CoreDocumentImpl paramCoreDocumentImpl)
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    super.setOwnerDocument(paramCoreDocumentImpl);
    ownerDocument = paramCoreDocumentImpl;
    for (ChildNode localChildNode = firstChild; localChildNode != null; localChildNode = nextSibling) {
      localChildNode.setOwnerDocument(paramCoreDocumentImpl);
    }
  }
  
  public boolean hasChildNodes()
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    return firstChild != null;
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
    return firstChild;
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
    return firstChild != null ? firstChild.previousSibling : null;
  }
  
  final void lastChild(ChildNode paramChildNode)
  {
    if (firstChild != null) {
      firstChild.previousSibling = paramChildNode;
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
    boolean bool = ownerDocument.errorChecking;
    if (paramNode1.getNodeType() == 11)
    {
      if (bool) {
        for (Node localNode = paramNode1.getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
          if (!ownerDocument.isKidOK(this, localNode)) {
            throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
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
      if (isReadOnly()) {
        throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
      }
      if ((paramNode1.getOwnerDocument() != ownerDocument) && (paramNode1 != ownerDocument)) {
        throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null));
      }
      if (!ownerDocument.isKidOK(this, paramNode1)) {
        throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
      }
      if ((paramNode2 != null) && (paramNode2.getParentNode() != this)) {
        throw new DOMException((short)8, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null));
      }
      int i = 1;
      for (localObject = this; (i != 0) && (localObject != null); localObject = ((NodeImpl)localObject).parentNode()) {
        i = paramNode1 != localObject ? 1 : 0;
      }
      if (i == 0) {
        throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
      }
    }
    ownerDocument.insertingNode(this, paramBoolean);
    ChildNode localChildNode1 = (ChildNode)paramNode1;
    Object localObject = localChildNode1.parentNode();
    if (localObject != null) {
      ((Node)localObject).removeChild(localChildNode1);
    }
    ChildNode localChildNode2 = (ChildNode)paramNode2;
    ownerNode = this;
    localChildNode1.isOwned(true);
    if (firstChild == null)
    {
      firstChild = localChildNode1;
      localChildNode1.isFirstChild(true);
      previousSibling = localChildNode1;
    }
    else
    {
      ChildNode localChildNode3;
      if (localChildNode2 == null)
      {
        localChildNode3 = firstChild.previousSibling;
        nextSibling = localChildNode1;
        previousSibling = localChildNode3;
        firstChild.previousSibling = localChildNode1;
      }
      else if (paramNode2 == firstChild)
      {
        firstChild.isFirstChild(false);
        nextSibling = firstChild;
        previousSibling = firstChild.previousSibling;
        firstChild.previousSibling = localChildNode1;
        firstChild = localChildNode1;
        localChildNode1.isFirstChild(true);
      }
      else
      {
        localChildNode3 = previousSibling;
        nextSibling = localChildNode2;
        nextSibling = localChildNode1;
        previousSibling = localChildNode1;
        previousSibling = localChildNode3;
      }
    }
    changed();
    if (fNodeListCache != null)
    {
      if (fNodeListCache.fLength != -1) {
        fNodeListCache.fLength += 1;
      }
      if (fNodeListCache.fChildIndex != -1) {
        if (fNodeListCache.fChild == localChildNode2) {
          fNodeListCache.fChild = localChildNode1;
        } else {
          fNodeListCache.fChildIndex = -1;
        }
      }
    }
    ownerDocument.insertedNode(this, localChildNode1, paramBoolean);
    checkNormalizationAfterInsert(localChildNode1);
    return paramNode1;
  }
  
  public Node removeChild(Node paramNode)
    throws DOMException
  {
    return internalRemoveChild(paramNode, false);
  }
  
  Node internalRemoveChild(Node paramNode, boolean paramBoolean)
    throws DOMException
  {
    CoreDocumentImpl localCoreDocumentImpl = ownerDocument();
    if (errorChecking)
    {
      if (isReadOnly()) {
        throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null));
      }
      if ((paramNode != null) && (paramNode.getParentNode() != this)) {
        throw new DOMException((short)8, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", null));
      }
    }
    ChildNode localChildNode1 = (ChildNode)paramNode;
    localCoreDocumentImpl.removingNode(this, localChildNode1, paramBoolean);
    if (fNodeListCache != null)
    {
      if (fNodeListCache.fLength != -1) {
        fNodeListCache.fLength -= 1;
      }
      if (fNodeListCache.fChildIndex != -1) {
        if (fNodeListCache.fChild == localChildNode1)
        {
          fNodeListCache.fChildIndex -= 1;
          fNodeListCache.fChild = localChildNode1.previousSibling();
        }
        else
        {
          fNodeListCache.fChildIndex = -1;
        }
      }
    }
    if (localChildNode1 == firstChild)
    {
      localChildNode1.isFirstChild(false);
      firstChild = nextSibling;
      if (firstChild != null)
      {
        firstChild.isFirstChild(true);
        firstChild.previousSibling = previousSibling;
      }
    }
    else
    {
      localChildNode2 = previousSibling;
      ChildNode localChildNode3 = nextSibling;
      nextSibling = localChildNode3;
      if (localChildNode3 == null) {
        firstChild.previousSibling = localChildNode2;
      } else {
        previousSibling = localChildNode2;
      }
    }
    ChildNode localChildNode2 = localChildNode1.previousSibling();
    ownerNode = localCoreDocumentImpl;
    localChildNode1.isOwned(false);
    nextSibling = null;
    previousSibling = null;
    changed();
    localCoreDocumentImpl.removedNode(this, paramBoolean);
    checkNormalizationAfterRemove(localChildNode2);
    return localChildNode1;
  }
  
  public Node replaceChild(Node paramNode1, Node paramNode2)
    throws DOMException
  {
    ownerDocument.replacingNode(this);
    internalInsertBefore(paramNode1, paramNode2, true);
    if (paramNode1 != paramNode2) {
      internalRemoveChild(paramNode2, true);
    }
    ownerDocument.replacedNode(this);
    return paramNode2;
  }
  
  public String getTextContent()
    throws DOMException
  {
    Node localNode1 = getFirstChild();
    if (localNode1 != null)
    {
      Node localNode2 = localNode1.getNextSibling();
      if (localNode2 == null) {
        return hasTextContent(localNode1) ? ((NodeImpl)localNode1).getTextContent() : "";
      }
      StringBuffer localStringBuffer = new StringBuffer();
      getTextContent(localStringBuffer);
      return localStringBuffer.toString();
    }
    return "";
  }
  
  void getTextContent(StringBuffer paramStringBuffer)
    throws DOMException
  {
    for (Node localNode = getFirstChild(); localNode != null; localNode = localNode.getNextSibling()) {
      if (hasTextContent(localNode)) {
        ((NodeImpl)localNode).getTextContent(paramStringBuffer);
      }
    }
  }
  
  final boolean hasTextContent(Node paramNode)
  {
    return (paramNode.getNodeType() != 8) && (paramNode.getNodeType() != 7) && ((paramNode.getNodeType() != 3) || (!((TextImpl)paramNode).isIgnorableWhitespace()));
  }
  
  public void setTextContent(String paramString)
    throws DOMException
  {
    Node localNode;
    while ((localNode = getFirstChild()) != null) {
      removeChild(localNode);
    }
    if ((paramString != null) && (paramString.length() != 0)) {
      appendChild(ownerDocument().createTextNode(paramString));
    }
  }
  
  private int nodeListGetLength()
  {
    if (fNodeListCache == null)
    {
      if (needsSyncChildren()) {
        synchronizeChildren();
      }
      if (firstChild == null) {
        return 0;
      }
      if (firstChild == lastChild()) {
        return 1;
      }
      fNodeListCache = ownerDocument.getNodeListCache(this);
    }
    if (fNodeListCache.fLength == -1)
    {
      int i;
      ChildNode localChildNode;
      if ((fNodeListCache.fChildIndex != -1) && (fNodeListCache.fChild != null))
      {
        i = fNodeListCache.fChildIndex;
        localChildNode = fNodeListCache.fChild;
      }
      else
      {
        localChildNode = firstChild;
        i = 0;
      }
      while (localChildNode != null)
      {
        i++;
        localChildNode = nextSibling;
      }
      fNodeListCache.fLength = i;
    }
    return fNodeListCache.fLength;
  }
  
  public int getLength()
  {
    return nodeListGetLength();
  }
  
  private Node nodeListItem(int paramInt)
  {
    if (fNodeListCache == null)
    {
      if (needsSyncChildren()) {
        synchronizeChildren();
      }
      if (firstChild == lastChild()) {
        return paramInt == 0 ? firstChild : null;
      }
      fNodeListCache = ownerDocument.getNodeListCache(this);
    }
    int i = fNodeListCache.fChildIndex;
    ChildNode localChildNode = fNodeListCache.fChild;
    int j = 1;
    if ((i != -1) && (localChildNode != null))
    {
      j = 0;
      if (i < paramInt) {
        do
        {
          i++;
          localChildNode = nextSibling;
          if (i >= paramInt) {
            break;
          }
        } while (localChildNode != null);
      } else if (i > paramInt) {
        do
        {
          i--;
          localChildNode = localChildNode.previousSibling();
          if (i <= paramInt) {
            break;
          }
        } while (localChildNode != null);
      }
    }
    else
    {
      if (paramInt < 0) {
        return null;
      }
      localChildNode = firstChild;
      for (i = 0; (i < paramInt) && (localChildNode != null); i++) {
        localChildNode = nextSibling;
      }
    }
    if ((j == 0) && ((localChildNode == firstChild) || (localChildNode == lastChild())))
    {
      fNodeListCache.fChildIndex = -1;
      fNodeListCache.fChild = null;
      ownerDocument.freeNodeListCache(fNodeListCache);
    }
    else
    {
      fNodeListCache.fChildIndex = i;
      fNodeListCache.fChild = localChildNode;
    }
    return localChildNode;
  }
  
  public Node item(int paramInt)
  {
    return nodeListItem(paramInt);
  }
  
  protected final NodeList getChildNodesUnoptimized()
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    new NodeList()
    {
      public int getLength()
      {
        return ParentNode.this.nodeListGetLength();
      }
      
      public Node item(int paramAnonymousInt)
      {
        return ParentNode.this.nodeListItem(paramAnonymousInt);
      }
    };
  }
  
  public void normalize()
  {
    if (isNormalized()) {
      return;
    }
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    for (ChildNode localChildNode = firstChild; localChildNode != null; localChildNode = nextSibling) {
      localChildNode.normalize();
    }
    isNormalized(true);
  }
  
  public boolean isEqualNode(Node paramNode)
  {
    if (!super.isEqualNode(paramNode)) {
      return false;
    }
    Node localNode1 = getFirstChild();
    for (Node localNode2 = paramNode.getFirstChild(); (localNode1 != null) && (localNode2 != null); localNode2 = localNode2.getNextSibling())
    {
      if (!localNode1.isEqualNode(localNode2)) {
        return false;
      }
      localNode1 = localNode1.getNextSibling();
    }
    return localNode1 == localNode2;
  }
  
  public void setReadOnly(boolean paramBoolean1, boolean paramBoolean2)
  {
    super.setReadOnly(paramBoolean1, paramBoolean2);
    if (paramBoolean2)
    {
      if (needsSyncChildren()) {
        synchronizeChildren();
      }
      for (ChildNode localChildNode = firstChild; localChildNode != null; localChildNode = nextSibling) {
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
  
  class UserDataRecord
    implements Serializable
  {
    private static final long serialVersionUID = 3258126977134310455L;
    Object fData;
    UserDataHandler fHandler;
    
    UserDataRecord(Object paramObject, UserDataHandler paramUserDataHandler)
    {
      fData = paramObject;
      fHandler = paramUserDataHandler;
    }
  }
}
