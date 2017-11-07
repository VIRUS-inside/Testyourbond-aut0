package org.apache.xerces.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import org.apache.xerces.util.URI;
import org.apache.xerces.util.URI.MalformedURIException;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.xni.NamespaceContext;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class CoreDocumentImpl
  extends ParentNode
  implements Document
{
  static final long serialVersionUID = 0L;
  protected DocumentTypeImpl docType;
  protected ElementImpl docElement;
  transient NodeListCache fFreeNLCache;
  protected String encoding;
  protected String actualEncoding;
  protected String version;
  protected boolean standalone;
  protected String fDocumentURI;
  protected Map userData;
  protected Hashtable identifiers;
  transient DOMNormalizer domNormalizer = null;
  transient DOMConfigurationImpl fConfiguration = null;
  transient Object fXPathEvaluator = null;
  private static final int[] kidOK = new int[13];
  protected int changes = 0;
  protected boolean allowGrammarAccess;
  protected boolean errorChecking = true;
  protected boolean xmlVersionChanged = false;
  private int documentNumber = 0;
  private int nodeCounter = 0;
  private Map nodeTable;
  private boolean xml11Version = false;
  
  public CoreDocumentImpl()
  {
    this(false);
  }
  
  public CoreDocumentImpl(boolean paramBoolean)
  {
    super(null);
    ownerDocument = this;
    allowGrammarAccess = paramBoolean;
  }
  
  public CoreDocumentImpl(DocumentType paramDocumentType)
  {
    this(paramDocumentType, false);
  }
  
  public CoreDocumentImpl(DocumentType paramDocumentType, boolean paramBoolean)
  {
    this(paramBoolean);
    if (paramDocumentType != null)
    {
      DocumentTypeImpl localDocumentTypeImpl;
      try
      {
        localDocumentTypeImpl = (DocumentTypeImpl)paramDocumentType;
      }
      catch (ClassCastException localClassCastException)
      {
        String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
        throw new DOMException((short)4, str);
      }
      ownerDocument = this;
      appendChild(paramDocumentType);
    }
  }
  
  public final Document getOwnerDocument()
  {
    return null;
  }
  
  public short getNodeType()
  {
    return 9;
  }
  
  public String getNodeName()
  {
    return "#document";
  }
  
  public Node cloneNode(boolean paramBoolean)
  {
    CoreDocumentImpl localCoreDocumentImpl = new CoreDocumentImpl();
    callUserDataHandlers(this, localCoreDocumentImpl, (short)1);
    cloneNode(localCoreDocumentImpl, paramBoolean);
    return localCoreDocumentImpl;
  }
  
  protected void cloneNode(CoreDocumentImpl paramCoreDocumentImpl, boolean paramBoolean)
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    if (paramBoolean)
    {
      HashMap localHashMap = null;
      if (identifiers != null)
      {
        localHashMap = new HashMap();
        localObject1 = identifiers.entrySet().iterator();
        while (((Iterator)localObject1).hasNext())
        {
          Map.Entry localEntry = (Map.Entry)((Iterator)localObject1).next();
          Object localObject2 = localEntry.getKey();
          Object localObject3 = localEntry.getValue();
          localHashMap.put(localObject3, localObject2);
        }
      }
      for (Object localObject1 = firstChild; localObject1 != null; localObject1 = nextSibling) {
        paramCoreDocumentImpl.appendChild(paramCoreDocumentImpl.importNode((Node)localObject1, true, true, localHashMap));
      }
    }
    allowGrammarAccess = allowGrammarAccess;
    errorChecking = errorChecking;
  }
  
  public Node insertBefore(Node paramNode1, Node paramNode2)
    throws DOMException
  {
    int i = paramNode1.getNodeType();
    if (errorChecking)
    {
      if (needsSyncChildren()) {
        synchronizeChildren();
      }
      if (((i == 1) && (docElement != null)) || ((i == 10) && (docType != null)))
      {
        String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null);
        throw new DOMException((short)3, str);
      }
    }
    if ((paramNode1.getOwnerDocument() == null) && ((paramNode1 instanceof DocumentTypeImpl))) {
      ownerDocument = this;
    }
    super.insertBefore(paramNode1, paramNode2);
    if (i == 1) {
      docElement = ((ElementImpl)paramNode1);
    } else if (i == 10) {
      docType = ((DocumentTypeImpl)paramNode1);
    }
    return paramNode1;
  }
  
  public Node removeChild(Node paramNode)
    throws DOMException
  {
    super.removeChild(paramNode);
    int i = paramNode.getNodeType();
    if (i == 1) {
      docElement = null;
    } else if (i == 10) {
      docType = null;
    }
    return paramNode;
  }
  
  public Node replaceChild(Node paramNode1, Node paramNode2)
    throws DOMException
  {
    if ((paramNode1.getOwnerDocument() == null) && ((paramNode1 instanceof DocumentTypeImpl))) {
      ownerDocument = this;
    }
    if ((errorChecking) && (((docType != null) && (paramNode2.getNodeType() != 10) && (paramNode1.getNodeType() == 10)) || ((docElement != null) && (paramNode2.getNodeType() != 1) && (paramNode1.getNodeType() == 1)))) {
      throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", null));
    }
    super.replaceChild(paramNode1, paramNode2);
    int i = paramNode2.getNodeType();
    if (i == 1) {
      docElement = ((ElementImpl)paramNode1);
    } else if (i == 10) {
      docType = ((DocumentTypeImpl)paramNode1);
    }
    return paramNode2;
  }
  
  public String getTextContent()
    throws DOMException
  {
    return null;
  }
  
  public void setTextContent(String paramString)
    throws DOMException
  {}
  
  public Object getFeature(String paramString1, String paramString2)
  {
    int i = (paramString2 == null) || (paramString2.length() == 0) ? 1 : 0;
    if ((paramString1.equalsIgnoreCase("+XPath")) && ((i != 0) || (paramString2.equals("3.0"))))
    {
      if (fXPathEvaluator != null) {
        return fXPathEvaluator;
      }
      try
      {
        Class localClass = ObjectFactory.findProviderClass("org.apache.xpath.domapi.XPathEvaluatorImpl", ObjectFactory.findClassLoader(), true);
        Constructor localConstructor = localClass.getConstructor(new Class[] { Document.class });
        Class[] arrayOfClass = localClass.getInterfaces();
        for (int j = 0; j < arrayOfClass.length; j++) {
          if (arrayOfClass[j].getName().equals("org.w3c.dom.xpath.XPathEvaluator"))
          {
            fXPathEvaluator = localConstructor.newInstance(new Object[] { this });
            return fXPathEvaluator;
          }
        }
        return null;
      }
      catch (Exception localException)
      {
        return null;
      }
    }
    return super.getFeature(paramString1, paramString2);
  }
  
  public Attr createAttribute(String paramString)
    throws DOMException
  {
    if ((errorChecking) && (!isXMLName(paramString, xml11Version)))
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
      throw new DOMException((short)5, str);
    }
    return new AttrImpl(this, paramString);
  }
  
  public CDATASection createCDATASection(String paramString)
    throws DOMException
  {
    return new CDATASectionImpl(this, paramString);
  }
  
  public Comment createComment(String paramString)
  {
    return new CommentImpl(this, paramString);
  }
  
  public DocumentFragment createDocumentFragment()
  {
    return new DocumentFragmentImpl(this);
  }
  
  public Element createElement(String paramString)
    throws DOMException
  {
    if ((errorChecking) && (!isXMLName(paramString, xml11Version)))
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
      throw new DOMException((short)5, str);
    }
    return new ElementImpl(this, paramString);
  }
  
  public EntityReference createEntityReference(String paramString)
    throws DOMException
  {
    if ((errorChecking) && (!isXMLName(paramString, xml11Version)))
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
      throw new DOMException((short)5, str);
    }
    return new EntityReferenceImpl(this, paramString);
  }
  
  public ProcessingInstruction createProcessingInstruction(String paramString1, String paramString2)
    throws DOMException
  {
    if ((errorChecking) && (!isXMLName(paramString1, xml11Version)))
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
      throw new DOMException((short)5, str);
    }
    return new ProcessingInstructionImpl(this, paramString1, paramString2);
  }
  
  public Text createTextNode(String paramString)
  {
    return new TextImpl(this, paramString);
  }
  
  public DocumentType getDoctype()
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    return docType;
  }
  
  public Element getDocumentElement()
  {
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    return docElement;
  }
  
  public NodeList getElementsByTagName(String paramString)
  {
    return new DeepNodeListImpl(this, paramString);
  }
  
  public DOMImplementation getImplementation()
  {
    return CoreDOMImplementationImpl.getDOMImplementation();
  }
  
  public void setErrorChecking(boolean paramBoolean)
  {
    errorChecking = paramBoolean;
  }
  
  public void setStrictErrorChecking(boolean paramBoolean)
  {
    errorChecking = paramBoolean;
  }
  
  public boolean getErrorChecking()
  {
    return errorChecking;
  }
  
  public boolean getStrictErrorChecking()
  {
    return errorChecking;
  }
  
  public String getInputEncoding()
  {
    return actualEncoding;
  }
  
  public void setInputEncoding(String paramString)
  {
    actualEncoding = paramString;
  }
  
  public void setXmlEncoding(String paramString)
  {
    encoding = paramString;
  }
  
  /**
   * @deprecated
   */
  public void setEncoding(String paramString)
  {
    setXmlEncoding(paramString);
  }
  
  public String getXmlEncoding()
  {
    return encoding;
  }
  
  /**
   * @deprecated
   */
  public String getEncoding()
  {
    return getXmlEncoding();
  }
  
  public void setXmlVersion(String paramString)
  {
    if ((paramString.equals("1.0")) || (paramString.equals("1.1")))
    {
      if (!getXmlVersion().equals(paramString))
      {
        xmlVersionChanged = true;
        isNormalized(false);
        version = paramString;
      }
    }
    else
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
      throw new DOMException((short)9, str);
    }
    if (getXmlVersion().equals("1.1")) {
      xml11Version = true;
    } else {
      xml11Version = false;
    }
  }
  
  /**
   * @deprecated
   */
  public void setVersion(String paramString)
  {
    setXmlVersion(paramString);
  }
  
  public String getXmlVersion()
  {
    return version == null ? "1.0" : version;
  }
  
  /**
   * @deprecated
   */
  public String getVersion()
  {
    return getXmlVersion();
  }
  
  public void setXmlStandalone(boolean paramBoolean)
    throws DOMException
  {
    standalone = paramBoolean;
  }
  
  /**
   * @deprecated
   */
  public void setStandalone(boolean paramBoolean)
  {
    setXmlStandalone(paramBoolean);
  }
  
  public boolean getXmlStandalone()
  {
    return standalone;
  }
  
  /**
   * @deprecated
   */
  public boolean getStandalone()
  {
    return getXmlStandalone();
  }
  
  public String getDocumentURI()
  {
    return fDocumentURI;
  }
  
  protected boolean canRenameElements(String paramString1, String paramString2, ElementImpl paramElementImpl)
  {
    return true;
  }
  
  public Node renameNode(Node paramNode, String paramString1, String paramString2)
    throws DOMException
  {
    if ((errorChecking) && (paramNode.getOwnerDocument() != this) && (paramNode != this))
    {
      localObject = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
      throw new DOMException((short)4, (String)localObject);
    }
    switch (paramNode.getNodeType())
    {
    case 1: 
      localObject = (ElementImpl)paramNode;
      if ((localObject instanceof ElementNSImpl))
      {
        if (canRenameElements(paramString1, paramString2, (ElementImpl)localObject))
        {
          ((ElementNSImpl)localObject).rename(paramString1, paramString2);
          callUserDataHandlers((Node)localObject, null, (short)4);
        }
        else
        {
          localObject = replaceRenameElement((ElementImpl)localObject, paramString1, paramString2);
        }
      }
      else if ((paramString1 == null) && (canRenameElements(null, paramString2, (ElementImpl)localObject)))
      {
        ((ElementImpl)localObject).rename(paramString2);
        callUserDataHandlers((Node)localObject, null, (short)4);
      }
      else
      {
        localObject = replaceRenameElement((ElementImpl)localObject, paramString1, paramString2);
      }
      renamedElement((Element)paramNode, (Element)localObject);
      return localObject;
    case 2: 
      localObject = (AttrImpl)paramNode;
      Element localElement = ((AttrImpl)localObject).getOwnerElement();
      if (localElement != null) {
        localElement.removeAttributeNode((Attr)localObject);
      }
      if ((paramNode instanceof AttrNSImpl))
      {
        ((AttrNSImpl)localObject).rename(paramString1, paramString2);
        if (localElement != null) {
          localElement.setAttributeNodeNS((Attr)localObject);
        }
        callUserDataHandlers((Node)localObject, null, (short)4);
      }
      else if (paramString1 == null)
      {
        ((AttrImpl)localObject).rename(paramString2);
        if (localElement != null) {
          localElement.setAttributeNode((Attr)localObject);
        }
        callUserDataHandlers((Node)localObject, null, (short)4);
      }
      else
      {
        AttrNSImpl localAttrNSImpl = (AttrNSImpl)createAttributeNS(paramString1, paramString2);
        copyEventListeners((NodeImpl)localObject, localAttrNSImpl);
        Hashtable localHashtable = removeUserDataTable((Node)localObject);
        for (Node localNode = ((AttrImpl)localObject).getFirstChild(); localNode != null; localNode = ((AttrImpl)localObject).getFirstChild())
        {
          ((AttrImpl)localObject).removeChild(localNode);
          localAttrNSImpl.appendChild(localNode);
        }
        setUserDataTable(localAttrNSImpl, localHashtable);
        callUserDataHandlers((Node)localObject, localAttrNSImpl, (short)4);
        if (localElement != null) {
          localElement.setAttributeNode(localAttrNSImpl);
        }
        localObject = localAttrNSImpl;
      }
      renamedAttrNode((Attr)paramNode, (Attr)localObject);
      return localObject;
    }
    Object localObject = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
    throw new DOMException((short)9, (String)localObject);
  }
  
  private ElementImpl replaceRenameElement(ElementImpl paramElementImpl, String paramString1, String paramString2)
  {
    ElementNSImpl localElementNSImpl = (ElementNSImpl)createElementNS(paramString1, paramString2);
    copyEventListeners(paramElementImpl, localElementNSImpl);
    Hashtable localHashtable = removeUserDataTable(paramElementImpl);
    Node localNode1 = paramElementImpl.getParentNode();
    Node localNode2 = paramElementImpl.getNextSibling();
    if (localNode1 != null) {
      localNode1.removeChild(paramElementImpl);
    }
    for (Node localNode3 = paramElementImpl.getFirstChild(); localNode3 != null; localNode3 = paramElementImpl.getFirstChild())
    {
      paramElementImpl.removeChild(localNode3);
      localElementNSImpl.appendChild(localNode3);
    }
    localElementNSImpl.moveSpecifiedAttributes(paramElementImpl);
    setUserDataTable(localElementNSImpl, localHashtable);
    callUserDataHandlers(paramElementImpl, localElementNSImpl, (short)4);
    if (localNode1 != null) {
      localNode1.insertBefore(localElementNSImpl, localNode2);
    }
    return localElementNSImpl;
  }
  
  public void normalizeDocument()
  {
    if ((isNormalized()) && (!isNormalizeDocRequired())) {
      return;
    }
    if (needsSyncChildren()) {
      synchronizeChildren();
    }
    if (domNormalizer == null) {
      domNormalizer = new DOMNormalizer();
    }
    if (fConfiguration == null) {
      fConfiguration = new DOMConfigurationImpl();
    } else {
      fConfiguration.reset();
    }
    domNormalizer.normalizeDocument(this, fConfiguration);
    isNormalized(true);
    xmlVersionChanged = false;
  }
  
  public DOMConfiguration getDomConfig()
  {
    if (fConfiguration == null) {
      fConfiguration = new DOMConfigurationImpl();
    }
    return fConfiguration;
  }
  
  public String getBaseURI()
  {
    if ((fDocumentURI != null) && (fDocumentURI.length() != 0)) {
      try
      {
        return new URI(fDocumentURI).toString();
      }
      catch (URI.MalformedURIException localMalformedURIException)
      {
        return null;
      }
    }
    return fDocumentURI;
  }
  
  public void setDocumentURI(String paramString)
  {
    fDocumentURI = paramString;
  }
  
  public boolean getAsync()
  {
    return false;
  }
  
  public void setAsync(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
      throw new DOMException((short)9, str);
    }
  }
  
  public void abort() {}
  
  public boolean load(String paramString)
  {
    return false;
  }
  
  public boolean loadXML(String paramString)
  {
    return false;
  }
  
  public String saveXML(Node paramNode)
    throws DOMException
  {
    if ((errorChecking) && (paramNode != null) && (this != paramNode.getOwnerDocument()))
    {
      localObject = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", null);
      throw new DOMException((short)4, (String)localObject);
    }
    Object localObject = (DOMImplementationLS)DOMImplementationImpl.getDOMImplementation();
    LSSerializer localLSSerializer = ((DOMImplementationLS)localObject).createLSSerializer();
    if (paramNode == null) {
      paramNode = this;
    }
    return localLSSerializer.writeToString(paramNode);
  }
  
  void setMutationEvents(boolean paramBoolean) {}
  
  boolean getMutationEvents()
  {
    return false;
  }
  
  public DocumentType createDocumentType(String paramString1, String paramString2, String paramString3)
    throws DOMException
  {
    return new DocumentTypeImpl(this, paramString1, paramString2, paramString3);
  }
  
  public Entity createEntity(String paramString)
    throws DOMException
  {
    if ((errorChecking) && (!isXMLName(paramString, xml11Version)))
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
      throw new DOMException((short)5, str);
    }
    return new EntityImpl(this, paramString);
  }
  
  public Notation createNotation(String paramString)
    throws DOMException
  {
    if ((errorChecking) && (!isXMLName(paramString, xml11Version)))
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
      throw new DOMException((short)5, str);
    }
    return new NotationImpl(this, paramString);
  }
  
  public ElementDefinitionImpl createElementDefinition(String paramString)
    throws DOMException
  {
    if ((errorChecking) && (!isXMLName(paramString, xml11Version)))
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
      throw new DOMException((short)5, str);
    }
    return new ElementDefinitionImpl(this, paramString);
  }
  
  protected int getNodeNumber()
  {
    if (documentNumber == 0)
    {
      CoreDOMImplementationImpl localCoreDOMImplementationImpl = (CoreDOMImplementationImpl)CoreDOMImplementationImpl.getDOMImplementation();
      documentNumber = localCoreDOMImplementationImpl.assignDocumentNumber();
    }
    return documentNumber;
  }
  
  protected int getNodeNumber(Node paramNode)
  {
    int i;
    if (nodeTable == null)
    {
      nodeTable = new WeakHashMap();
      i = --nodeCounter;
      nodeTable.put(paramNode, new Integer(i));
    }
    else
    {
      Integer localInteger = (Integer)nodeTable.get(paramNode);
      if (localInteger == null)
      {
        i = --nodeCounter;
        nodeTable.put(paramNode, new Integer(i));
      }
      else
      {
        i = localInteger.intValue();
      }
    }
    return i;
  }
  
  public Node importNode(Node paramNode, boolean paramBoolean)
    throws DOMException
  {
    return importNode(paramNode, paramBoolean, false, null);
  }
  
  private Node importNode(Node paramNode, boolean paramBoolean1, boolean paramBoolean2, HashMap paramHashMap)
    throws DOMException
  {
    Object localObject1 = null;
    Hashtable localHashtable = null;
    if ((paramNode instanceof NodeImpl)) {
      localHashtable = ((NodeImpl)paramNode).getUserDataRecord();
    }
    int i = paramNode.getNodeType();
    Object localObject2;
    NamedNodeMap localNamedNodeMap;
    int k;
    Object localObject4;
    Object localObject3;
    switch (i)
    {
    case 1: 
      boolean bool = paramNode.getOwnerDocument().getImplementation().hasFeature("XML", "2.0");
      if ((!bool) || (paramNode.getLocalName() == null)) {
        localObject2 = createElement(paramNode.getNodeName());
      } else {
        localObject2 = createElementNS(paramNode.getNamespaceURI(), paramNode.getNodeName());
      }
      localNamedNodeMap = paramNode.getAttributes();
      if (localNamedNodeMap != null)
      {
        int j = localNamedNodeMap.getLength();
        for (k = 0; k < j; k++)
        {
          Attr localAttr1 = (Attr)localNamedNodeMap.item(k);
          if ((localAttr1.getSpecified()) || (paramBoolean2))
          {
            Attr localAttr2 = (Attr)importNode(localAttr1, true, paramBoolean2, paramHashMap);
            if ((!bool) || (localAttr1.getLocalName() == null)) {
              ((Element)localObject2).setAttributeNode(localAttr2);
            } else {
              ((Element)localObject2).setAttributeNodeNS(localAttr2);
            }
          }
        }
      }
      if (paramHashMap != null)
      {
        localObject4 = paramHashMap.get(paramNode);
        if (localObject4 != null)
        {
          if (identifiers == null) {
            identifiers = new Hashtable();
          }
          identifiers.put(localObject4, localObject2);
        }
      }
      localObject1 = localObject2;
      break;
    case 2: 
      if (paramNode.getOwnerDocument().getImplementation().hasFeature("XML", "2.0"))
      {
        if (paramNode.getLocalName() == null) {
          localObject1 = createAttribute(paramNode.getNodeName());
        } else {
          localObject1 = createAttributeNS(paramNode.getNamespaceURI(), paramNode.getNodeName());
        }
      }
      else {
        localObject1 = createAttribute(paramNode.getNodeName());
      }
      if ((paramNode instanceof AttrImpl))
      {
        localObject2 = (AttrImpl)paramNode;
        if (((AttrImpl)localObject2).hasStringValue())
        {
          localObject3 = (AttrImpl)localObject1;
          ((AttrImpl)localObject3).setValue(((AttrImpl)localObject2).getValue());
          paramBoolean1 = false;
        }
        else
        {
          paramBoolean1 = true;
        }
      }
      else if (paramNode.getFirstChild() == null)
      {
        ((Node)localObject1).setNodeValue(paramNode.getNodeValue());
        paramBoolean1 = false;
      }
      else
      {
        paramBoolean1 = true;
      }
      break;
    case 3: 
      localObject1 = createTextNode(paramNode.getNodeValue());
      break;
    case 4: 
      localObject1 = createCDATASection(paramNode.getNodeValue());
      break;
    case 5: 
      localObject1 = createEntityReference(paramNode.getNodeName());
      paramBoolean1 = false;
      break;
    case 6: 
      localObject2 = (Entity)paramNode;
      localObject3 = (EntityImpl)createEntity(paramNode.getNodeName());
      ((EntityImpl)localObject3).setPublicId(((Entity)localObject2).getPublicId());
      ((EntityImpl)localObject3).setSystemId(((Entity)localObject2).getSystemId());
      ((EntityImpl)localObject3).setNotationName(((Entity)localObject2).getNotationName());
      ((EntityImpl)localObject3).isReadOnly(false);
      localObject1 = localObject3;
      break;
    case 7: 
      localObject1 = createProcessingInstruction(paramNode.getNodeName(), paramNode.getNodeValue());
      break;
    case 8: 
      localObject1 = createComment(paramNode.getNodeValue());
      break;
    case 10: 
      if (!paramBoolean2)
      {
        localObject2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
        throw new DOMException((short)9, (String)localObject2);
      }
      localObject2 = (DocumentType)paramNode;
      localObject3 = (DocumentTypeImpl)createDocumentType(((DocumentType)localObject2).getNodeName(), ((DocumentType)localObject2).getPublicId(), ((DocumentType)localObject2).getSystemId());
      ((DocumentTypeImpl)localObject3).setInternalSubset(((DocumentType)localObject2).getInternalSubset());
      localNamedNodeMap = ((DocumentType)localObject2).getEntities();
      localObject4 = ((DocumentTypeImpl)localObject3).getEntities();
      if (localNamedNodeMap != null) {
        for (k = 0; k < localNamedNodeMap.getLength(); k++) {
          ((NamedNodeMap)localObject4).setNamedItem(importNode(localNamedNodeMap.item(k), true, true, paramHashMap));
        }
      }
      localNamedNodeMap = ((DocumentType)localObject2).getNotations();
      localObject4 = ((DocumentTypeImpl)localObject3).getNotations();
      if (localNamedNodeMap != null) {
        for (k = 0; k < localNamedNodeMap.getLength(); k++) {
          ((NamedNodeMap)localObject4).setNamedItem(importNode(localNamedNodeMap.item(k), true, true, paramHashMap));
        }
      }
      localObject1 = localObject3;
      break;
    case 11: 
      localObject1 = createDocumentFragment();
      break;
    case 12: 
      localObject2 = (Notation)paramNode;
      localObject3 = (NotationImpl)createNotation(paramNode.getNodeName());
      ((NotationImpl)localObject3).setPublicId(((Notation)localObject2).getPublicId());
      ((NotationImpl)localObject3).setSystemId(((Notation)localObject2).getSystemId());
      localObject1 = localObject3;
      break;
    case 9: 
    default: 
      localObject2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
      throw new DOMException((short)9, (String)localObject2);
    }
    if (localHashtable != null) {
      callUserDataHandlers(paramNode, (Node)localObject1, (short)2, localHashtable);
    }
    if (paramBoolean1) {
      for (localObject2 = paramNode.getFirstChild(); localObject2 != null; localObject2 = ((Node)localObject2).getNextSibling()) {
        ((Node)localObject1).appendChild(importNode((Node)localObject2, true, paramBoolean2, paramHashMap));
      }
    }
    if (((Node)localObject1).getNodeType() == 6) {
      ((NodeImpl)localObject1).setReadOnly(true, true);
    }
    return localObject1;
  }
  
  public Node adoptNode(Node paramNode)
  {
    Hashtable localHashtable = null;
    NodeImpl localNodeImpl;
    try
    {
      localNodeImpl = (NodeImpl)paramNode;
    }
    catch (ClassCastException localClassCastException)
    {
      return null;
    }
    if (paramNode == null) {
      return null;
    }
    Object localObject1;
    Object localObject2;
    if ((paramNode != null) && (paramNode.getOwnerDocument() != null))
    {
      localObject1 = getImplementation();
      localObject2 = paramNode.getOwnerDocument().getImplementation();
      if (localObject1 != localObject2)
      {
        if (((localObject1 instanceof DOMImplementationImpl)) && ((localObject2 instanceof DeferredDOMImplementationImpl))) {
          undeferChildren(localNodeImpl);
        } else if ((!(localObject1 instanceof DeferredDOMImplementationImpl)) || (!(localObject2 instanceof DOMImplementationImpl))) {
          return null;
        }
      }
      else if ((localObject2 instanceof DeferredDOMImplementationImpl)) {
        undeferChildren(localNodeImpl);
      }
    }
    switch (localNodeImpl.getNodeType())
    {
    case 2: 
      localObject1 = (AttrImpl)localNodeImpl;
      if (((AttrImpl)localObject1).getOwnerElement() != null) {
        ((AttrImpl)localObject1).getOwnerElement().removeAttributeNode((Attr)localObject1);
      }
      ((AttrImpl)localObject1).isSpecified(true);
      localHashtable = localNodeImpl.getUserDataRecord();
      ((AttrImpl)localObject1).setOwnerDocument(this);
      if (localHashtable != null) {
        setUserDataTable(localNodeImpl, localHashtable);
      }
      break;
    case 6: 
    case 12: 
      localObject1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
      throw new DOMException((short)7, (String)localObject1);
    case 9: 
    case 10: 
      localObject1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
      throw new DOMException((short)9, (String)localObject1);
    case 5: 
      localHashtable = localNodeImpl.getUserDataRecord();
      localObject1 = localNodeImpl.getParentNode();
      if (localObject1 != null) {
        ((Node)localObject1).removeChild(paramNode);
      }
      while ((localObject2 = localNodeImpl.getFirstChild()) != null) {
        localNodeImpl.removeChild((Node)localObject2);
      }
      localNodeImpl.setOwnerDocument(this);
      if (localHashtable != null) {
        setUserDataTable(localNodeImpl, localHashtable);
      }
      if (docType != null)
      {
        NamedNodeMap localNamedNodeMap = docType.getEntities();
        Node localNode1 = localNamedNodeMap.getNamedItem(localNodeImpl.getNodeName());
        if (localNode1 != null) {
          for (localObject2 = localNode1.getFirstChild(); localObject2 != null; localObject2 = ((Node)localObject2).getNextSibling())
          {
            Node localNode2 = ((Node)localObject2).cloneNode(true);
            localNodeImpl.appendChild(localNode2);
          }
        }
      }
      break;
    case 1: 
      localHashtable = localNodeImpl.getUserDataRecord();
      localObject1 = localNodeImpl.getParentNode();
      if (localObject1 != null) {
        ((Node)localObject1).removeChild(paramNode);
      }
      localNodeImpl.setOwnerDocument(this);
      if (localHashtable != null) {
        setUserDataTable(localNodeImpl, localHashtable);
      }
      ((ElementImpl)localNodeImpl).reconcileDefaultAttributes();
      break;
    case 3: 
    case 4: 
    case 7: 
    case 8: 
    case 11: 
    default: 
      localHashtable = localNodeImpl.getUserDataRecord();
      localObject1 = localNodeImpl.getParentNode();
      if (localObject1 != null) {
        ((Node)localObject1).removeChild(paramNode);
      }
      localNodeImpl.setOwnerDocument(this);
      if (localHashtable != null) {
        setUserDataTable(localNodeImpl, localHashtable);
      }
      break;
    }
    if (localHashtable != null) {
      callUserDataHandlers(paramNode, null, (short)5, localHashtable);
    }
    return localNodeImpl;
  }
  
  protected void undeferChildren(Node paramNode)
  {
    Node localNode1 = paramNode;
    while (null != paramNode)
    {
      if (((NodeImpl)paramNode).needsSyncData()) {
        ((NodeImpl)paramNode).synchronizeData();
      }
      NamedNodeMap localNamedNodeMap = paramNode.getAttributes();
      if (localNamedNodeMap != null)
      {
        int i = localNamedNodeMap.getLength();
        for (int j = 0; j < i; j++) {
          undeferChildren(localNamedNodeMap.item(j));
        }
      }
      Node localNode2 = null;
      localNode2 = paramNode.getFirstChild();
      while (null == localNode2)
      {
        if (localNode1.equals(paramNode)) {
          break;
        }
        localNode2 = paramNode.getNextSibling();
        if (null == localNode2)
        {
          paramNode = paramNode.getParentNode();
          if ((null == paramNode) || (localNode1.equals(paramNode)))
          {
            localNode2 = null;
            break;
          }
        }
      }
      paramNode = localNode2;
    }
  }
  
  public Element getElementById(String paramString)
  {
    return getIdentifier(paramString);
  }
  
  protected final void clearIdentifiers()
  {
    if (identifiers != null) {
      identifiers.clear();
    }
  }
  
  public void putIdentifier(String paramString, Element paramElement)
  {
    if (paramElement == null)
    {
      removeIdentifier(paramString);
      return;
    }
    if (needsSyncData()) {
      synchronizeData();
    }
    if (identifiers == null) {
      identifiers = new Hashtable();
    }
    identifiers.put(paramString, paramElement);
  }
  
  public Element getIdentifier(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (identifiers == null) {
      return null;
    }
    Element localElement = (Element)identifiers.get(paramString);
    if (localElement != null) {
      for (Node localNode = localElement.getParentNode(); localNode != null; localNode = localNode.getParentNode()) {
        if (localNode == this) {
          return localElement;
        }
      }
    }
    return null;
  }
  
  public void removeIdentifier(String paramString)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (identifiers == null) {
      return;
    }
    identifiers.remove(paramString);
  }
  
  public Enumeration getIdentifiers()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (identifiers == null) {
      identifiers = new Hashtable();
    }
    return identifiers.keys();
  }
  
  public Element createElementNS(String paramString1, String paramString2)
    throws DOMException
  {
    return new ElementNSImpl(this, paramString1, paramString2);
  }
  
  public Element createElementNS(String paramString1, String paramString2, String paramString3)
    throws DOMException
  {
    return new ElementNSImpl(this, paramString1, paramString2, paramString3);
  }
  
  public Attr createAttributeNS(String paramString1, String paramString2)
    throws DOMException
  {
    return new AttrNSImpl(this, paramString1, paramString2);
  }
  
  public Attr createAttributeNS(String paramString1, String paramString2, String paramString3)
    throws DOMException
  {
    return new AttrNSImpl(this, paramString1, paramString2, paramString3);
  }
  
  public NodeList getElementsByTagNameNS(String paramString1, String paramString2)
  {
    return new DeepNodeListImpl(this, paramString1, paramString2);
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    CoreDocumentImpl localCoreDocumentImpl = (CoreDocumentImpl)super.clone();
    docType = null;
    docElement = null;
    return localCoreDocumentImpl;
  }
  
  public static final boolean isXMLName(String paramString, boolean paramBoolean)
  {
    if (paramString == null) {
      return false;
    }
    if (!paramBoolean) {
      return XMLChar.isValidName(paramString);
    }
    return XML11Char.isXML11ValidName(paramString);
  }
  
  public static final boolean isValidQName(String paramString1, String paramString2, boolean paramBoolean)
  {
    if (paramString2 == null) {
      return false;
    }
    boolean bool = false;
    if (!paramBoolean) {
      bool = ((paramString1 == null) || (XMLChar.isValidNCName(paramString1))) && (XMLChar.isValidNCName(paramString2));
    } else {
      bool = ((paramString1 == null) || (XML11Char.isXML11ValidNCName(paramString1))) && (XML11Char.isXML11ValidNCName(paramString2));
    }
    return bool;
  }
  
  protected boolean isKidOK(Node paramNode1, Node paramNode2)
  {
    if ((allowGrammarAccess) && (paramNode1.getNodeType() == 10)) {
      return paramNode2.getNodeType() == 1;
    }
    return 0 != (kidOK[paramNode1.getNodeType()] & 1 << paramNode2.getNodeType());
  }
  
  protected void changed()
  {
    changes += 1;
  }
  
  protected int changes()
  {
    return changes;
  }
  
  NodeListCache getNodeListCache(ParentNode paramParentNode)
  {
    if (fFreeNLCache == null) {
      return new NodeListCache(paramParentNode);
    }
    NodeListCache localNodeListCache = fFreeNLCache;
    fFreeNLCache = fFreeNLCache.next;
    fChild = null;
    fChildIndex = -1;
    fLength = -1;
    if (fOwner != null) {
      fOwner.fNodeListCache = null;
    }
    fOwner = paramParentNode;
    return localNodeListCache;
  }
  
  void freeNodeListCache(NodeListCache paramNodeListCache)
  {
    next = fFreeNLCache;
    fFreeNLCache = paramNodeListCache;
  }
  
  public Object setUserData(Node paramNode, String paramString, Object paramObject, UserDataHandler paramUserDataHandler)
  {
    Hashtable localHashtable;
    ParentNode.UserDataRecord localUserDataRecord;
    if (paramObject == null)
    {
      if (userData != null)
      {
        localHashtable = (Hashtable)userData.get(paramNode);
        if (localHashtable != null)
        {
          localObject = localHashtable.remove(paramString);
          if (localObject != null)
          {
            localUserDataRecord = (ParentNode.UserDataRecord)localObject;
            return fData;
          }
        }
      }
      return null;
    }
    if (userData == null)
    {
      userData = new WeakHashMap();
      localHashtable = new Hashtable();
      userData.put(paramNode, localHashtable);
    }
    else
    {
      localHashtable = (Hashtable)userData.get(paramNode);
      if (localHashtable == null)
      {
        localHashtable = new Hashtable();
        userData.put(paramNode, localHashtable);
      }
    }
    Object localObject = localHashtable.put(paramString, new ParentNode.UserDataRecord(this, paramObject, paramUserDataHandler));
    if (localObject != null)
    {
      localUserDataRecord = (ParentNode.UserDataRecord)localObject;
      return fData;
    }
    return null;
  }
  
  public Object getUserData(Node paramNode, String paramString)
  {
    if (userData == null) {
      return null;
    }
    Hashtable localHashtable = (Hashtable)userData.get(paramNode);
    if (localHashtable == null) {
      return null;
    }
    Object localObject = localHashtable.get(paramString);
    if (localObject != null)
    {
      ParentNode.UserDataRecord localUserDataRecord = (ParentNode.UserDataRecord)localObject;
      return fData;
    }
    return null;
  }
  
  protected Hashtable getUserDataRecord(Node paramNode)
  {
    if (userData == null) {
      return null;
    }
    Hashtable localHashtable = (Hashtable)userData.get(paramNode);
    if (localHashtable == null) {
      return null;
    }
    return localHashtable;
  }
  
  Hashtable removeUserDataTable(Node paramNode)
  {
    if (userData == null) {
      return null;
    }
    return (Hashtable)userData.get(paramNode);
  }
  
  void setUserDataTable(Node paramNode, Hashtable paramHashtable)
  {
    if (userData == null) {
      userData = new WeakHashMap();
    }
    if (paramHashtable != null) {
      userData.put(paramNode, paramHashtable);
    }
  }
  
  protected void callUserDataHandlers(Node paramNode1, Node paramNode2, short paramShort)
  {
    if (userData == null) {
      return;
    }
    if ((paramNode1 instanceof NodeImpl))
    {
      Hashtable localHashtable = ((NodeImpl)paramNode1).getUserDataRecord();
      if ((localHashtable == null) || (localHashtable.isEmpty())) {
        return;
      }
      callUserDataHandlers(paramNode1, paramNode2, paramShort, localHashtable);
    }
  }
  
  void callUserDataHandlers(Node paramNode1, Node paramNode2, short paramShort, Hashtable paramHashtable)
  {
    if ((paramHashtable == null) || (paramHashtable.isEmpty())) {
      return;
    }
    Iterator localIterator = paramHashtable.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      String str = (String)localEntry.getKey();
      ParentNode.UserDataRecord localUserDataRecord = (ParentNode.UserDataRecord)localEntry.getValue();
      if (fHandler != null) {
        fHandler.handle(paramShort, str, fData, paramNode1, paramNode2);
      }
    }
  }
  
  protected final void checkNamespaceWF(String paramString, int paramInt1, int paramInt2)
  {
    if (!errorChecking) {
      return;
    }
    if ((paramInt1 == 0) || (paramInt1 == paramString.length() - 1) || (paramInt2 != paramInt1))
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
      throw new DOMException((short)14, str);
    }
  }
  
  protected final void checkDOMNSErr(String paramString1, String paramString2)
  {
    if (errorChecking)
    {
      String str;
      if (paramString2 == null)
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
        throw new DOMException((short)14, str);
      }
      if ((paramString1.equals("xml")) && (!paramString2.equals(NamespaceContext.XML_URI)))
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
        throw new DOMException((short)14, str);
      }
      if (((paramString1.equals("xmlns")) && (!paramString2.equals(NamespaceContext.XMLNS_URI))) || ((!paramString1.equals("xmlns")) && (paramString2.equals(NamespaceContext.XMLNS_URI))))
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
        throw new DOMException((short)14, str);
      }
    }
  }
  
  protected final void checkQName(String paramString1, String paramString2)
  {
    if (!errorChecking) {
      return;
    }
    int i = 0;
    if (!xml11Version) {
      i = ((paramString1 == null) || (XMLChar.isValidNCName(paramString1))) && (XMLChar.isValidNCName(paramString2)) ? 1 : 0;
    } else {
      i = ((paramString1 == null) || (XML11Char.isXML11ValidNCName(paramString1))) && (XML11Char.isXML11ValidNCName(paramString2)) ? 1 : 0;
    }
    if (i == 0)
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
      throw new DOMException((short)5, str);
    }
  }
  
  boolean isXML11Version()
  {
    return xml11Version;
  }
  
  boolean isNormalizeDocRequired()
  {
    return true;
  }
  
  boolean isXMLVersionChanged()
  {
    return xmlVersionChanged;
  }
  
  protected void setUserData(NodeImpl paramNodeImpl, Object paramObject)
  {
    setUserData(paramNodeImpl, "XERCES1DOMUSERDATA", paramObject, null);
  }
  
  protected Object getUserData(NodeImpl paramNodeImpl)
  {
    return getUserData(paramNodeImpl, "XERCES1DOMUSERDATA");
  }
  
  protected void addEventListener(NodeImpl paramNodeImpl, String paramString, EventListener paramEventListener, boolean paramBoolean) {}
  
  protected void removeEventListener(NodeImpl paramNodeImpl, String paramString, EventListener paramEventListener, boolean paramBoolean) {}
  
  protected void copyEventListeners(NodeImpl paramNodeImpl1, NodeImpl paramNodeImpl2) {}
  
  protected boolean dispatchEvent(NodeImpl paramNodeImpl, Event paramEvent)
  {
    return false;
  }
  
  void replacedText(CharacterDataImpl paramCharacterDataImpl) {}
  
  void deletedText(CharacterDataImpl paramCharacterDataImpl, int paramInt1, int paramInt2) {}
  
  void insertedText(CharacterDataImpl paramCharacterDataImpl, int paramInt1, int paramInt2) {}
  
  void modifyingCharacterData(NodeImpl paramNodeImpl, boolean paramBoolean) {}
  
  void modifiedCharacterData(NodeImpl paramNodeImpl, String paramString1, String paramString2, boolean paramBoolean) {}
  
  void insertingNode(NodeImpl paramNodeImpl, boolean paramBoolean) {}
  
  void insertedNode(NodeImpl paramNodeImpl1, NodeImpl paramNodeImpl2, boolean paramBoolean) {}
  
  void removingNode(NodeImpl paramNodeImpl1, NodeImpl paramNodeImpl2, boolean paramBoolean) {}
  
  void removedNode(NodeImpl paramNodeImpl, boolean paramBoolean) {}
  
  void replacingNode(NodeImpl paramNodeImpl) {}
  
  void replacedNode(NodeImpl paramNodeImpl) {}
  
  void replacingData(NodeImpl paramNodeImpl) {}
  
  void replacedCharacterData(NodeImpl paramNodeImpl, String paramString1, String paramString2) {}
  
  void modifiedAttrValue(AttrImpl paramAttrImpl, String paramString) {}
  
  void setAttrNode(AttrImpl paramAttrImpl1, AttrImpl paramAttrImpl2) {}
  
  void removedAttrNode(AttrImpl paramAttrImpl, NodeImpl paramNodeImpl, String paramString) {}
  
  void renamedAttrNode(Attr paramAttr1, Attr paramAttr2) {}
  
  void renamedElement(Element paramElement1, Element paramElement2) {}
  
  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    if (userData != null) {
      userData = new WeakHashMap(userData);
    }
    if (nodeTable != null) {
      nodeTable = new WeakHashMap(nodeTable);
    }
  }
  
  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    Map localMap1 = userData;
    Map localMap2 = nodeTable;
    try
    {
      if (localMap1 != null) {
        userData = new Hashtable(localMap1);
      }
      if (localMap2 != null) {
        nodeTable = new Hashtable(localMap2);
      }
      paramObjectOutputStream.defaultWriteObject();
    }
    finally
    {
      userData = localMap1;
      nodeTable = localMap2;
    }
  }
  
  static
  {
    kidOK[9] = 1410;
    char tmp41_40 = (kidOK[5] = kidOK[1] = 'ƺ');
    kidOK[6] = tmp41_40;
    kidOK[11] = tmp41_40;
    kidOK[2] = 40;
    int tmp88_87 = (kidOK[8] = kidOK[3] = kidOK[4] = kidOK[12] = 0);
    kidOK[7] = tmp88_87;
    kidOK[10] = tmp88_87;
  }
}
