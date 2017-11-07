package org.apache.xerces.dom;

import java.util.Hashtable;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public class DocumentTypeImpl
  extends ParentNode
  implements DocumentType
{
  static final long serialVersionUID = 7751299192316526485L;
  protected String name;
  protected NamedNodeMapImpl entities;
  protected NamedNodeMapImpl notations;
  protected NamedNodeMapImpl elements;
  protected String publicID;
  protected String systemID;
  protected String internalSubset;
  private int doctypeNumber = 0;
  private Hashtable userData = null;
  
  public DocumentTypeImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString)
  {
    super(paramCoreDocumentImpl);
    name = paramString;
    entities = new NamedNodeMapImpl(this);
    notations = new NamedNodeMapImpl(this);
    elements = new NamedNodeMapImpl(this);
  }
  
  public DocumentTypeImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString1, String paramString2, String paramString3)
  {
    this(paramCoreDocumentImpl, paramString1);
    publicID = paramString2;
    systemID = paramString3;
  }
  
  public String getPublicId()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return publicID;
  }
  
  public String getSystemId()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return systemID;
  }
  
  public void setInternalSubset(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    internalSubset = paramString;
  }
  
  public String getInternalSubset()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return internalSubset;
  }
  
  public short getNodeType()
  {
    return 10;
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
    DocumentTypeImpl localDocumentTypeImpl = (DocumentTypeImpl)super.cloneNode(paramBoolean);
    entities = entities.cloneMap(localDocumentTypeImpl);
    notations = notations.cloneMap(localDocumentTypeImpl);
    elements = elements.cloneMap(localDocumentTypeImpl);
    return localDocumentTypeImpl;
  }
  
  public String getTextContent()
    throws DOMException
  {
    return null;
  }
  
  public void setTextContent(String paramString)
    throws DOMException
  {}
  
  public boolean isEqualNode(Node paramNode)
  {
    if (!super.isEqualNode(paramNode)) {
      return false;
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    DocumentTypeImpl localDocumentTypeImpl = (DocumentTypeImpl)paramNode;
    if (((getPublicId() == null) && (localDocumentTypeImpl.getPublicId() != null)) || ((getPublicId() != null) && (localDocumentTypeImpl.getPublicId() == null)) || ((getSystemId() == null) && (localDocumentTypeImpl.getSystemId() != null)) || ((getSystemId() != null) && (localDocumentTypeImpl.getSystemId() == null)) || ((getInternalSubset() == null) && (localDocumentTypeImpl.getInternalSubset() != null)) || ((getInternalSubset() != null) && (localDocumentTypeImpl.getInternalSubset() == null))) {
      return false;
    }
    if ((getPublicId() != null) && (!getPublicId().equals(localDocumentTypeImpl.getPublicId()))) {
      return false;
    }
    if ((getSystemId() != null) && (!getSystemId().equals(localDocumentTypeImpl.getSystemId()))) {
      return false;
    }
    if ((getInternalSubset() != null) && (!getInternalSubset().equals(localDocumentTypeImpl.getInternalSubset()))) {
      return false;
    }
    NamedNodeMapImpl localNamedNodeMapImpl1 = entities;
    if (((entities == null) && (localNamedNodeMapImpl1 != null)) || ((entities != null) && (localNamedNodeMapImpl1 == null))) {
      return false;
    }
    Node localNode2;
    if ((entities != null) && (localNamedNodeMapImpl1 != null))
    {
      if (entities.getLength() != localNamedNodeMapImpl1.getLength()) {
        return false;
      }
      for (int i = 0; entities.item(i) != null; i++)
      {
        Node localNode1 = entities.item(i);
        localNode2 = localNamedNodeMapImpl1.getNamedItem(localNode1.getNodeName());
        if (!((NodeImpl)localNode1).isEqualNode(localNode2)) {
          return false;
        }
      }
    }
    NamedNodeMapImpl localNamedNodeMapImpl2 = notations;
    if (((notations == null) && (localNamedNodeMapImpl2 != null)) || ((notations != null) && (localNamedNodeMapImpl2 == null))) {
      return false;
    }
    if ((notations != null) && (localNamedNodeMapImpl2 != null))
    {
      if (notations.getLength() != localNamedNodeMapImpl2.getLength()) {
        return false;
      }
      for (int j = 0; notations.item(j) != null; j++)
      {
        localNode2 = notations.item(j);
        Node localNode3 = localNamedNodeMapImpl2.getNamedItem(localNode2.getNodeName());
        if (!((NodeImpl)localNode2).isEqualNode(localNode3)) {
          return false;
        }
      }
    }
    return true;
  }
  
  protected void setOwnerDocument(CoreDocumentImpl paramCoreDocumentImpl)
  {
    super.setOwnerDocument(paramCoreDocumentImpl);
    entities.setOwnerDocument(paramCoreDocumentImpl);
    notations.setOwnerDocument(paramCoreDocumentImpl);
    elements.setOwnerDocument(paramCoreDocumentImpl);
  }
  
  protected int getNodeNumber()
  {
    if (getOwnerDocument() != null) {
      return super.getNodeNumber();
    }
    if (doctypeNumber == 0)
    {
      CoreDOMImplementationImpl localCoreDOMImplementationImpl = (CoreDOMImplementationImpl)CoreDOMImplementationImpl.getDOMImplementation();
      doctypeNumber = localCoreDOMImplementationImpl.assignDocTypeNumber();
    }
    return doctypeNumber;
  }
  
  public String getName()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return name;
  }
  
  public NamedNodeMap getEntities()
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    return entities;
  }
  
  public NamedNodeMap getNotations()
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    return notations;
  }
  
  public void setReadOnly(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    super.setReadOnly(paramBoolean1, paramBoolean2);
    elements.setReadOnly(paramBoolean1, true);
    entities.setReadOnly(paramBoolean1, true);
    notations.setReadOnly(paramBoolean1, true);
  }
  
  public NamedNodeMap getElements()
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    return elements;
  }
  
  public Object setUserData(String paramString, Object paramObject, UserDataHandler paramUserDataHandler)
  {
    if (userData == null) {
      userData = new Hashtable();
    }
    ParentNode.UserDataRecord localUserDataRecord;
    if (paramObject == null)
    {
      if (userData != null)
      {
        localObject = userData.remove(paramString);
        if (localObject != null)
        {
          localUserDataRecord = (ParentNode.UserDataRecord)localObject;
          return fData;
        }
      }
      return null;
    }
    Object localObject = userData.put(paramString, new ParentNode.UserDataRecord(this, paramObject, paramUserDataHandler));
    if (localObject != null)
    {
      localUserDataRecord = (ParentNode.UserDataRecord)localObject;
      return fData;
    }
    return null;
  }
  
  public Object getUserData(String paramString)
  {
    if (userData == null) {
      return null;
    }
    Object localObject = userData.get(paramString);
    if (localObject != null)
    {
      ParentNode.UserDataRecord localUserDataRecord = (ParentNode.UserDataRecord)localObject;
      return fData;
    }
    return null;
  }
  
  protected Hashtable getUserDataRecord()
  {
    return userData;
  }
}
