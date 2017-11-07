package org.apache.xerces.parsers;

import java.util.Locale;
import java.util.Stack;
import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DeferredDocumentImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.DocumentTypeImpl;
import org.apache.xerces.dom.ElementDefinitionImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.ElementNSImpl;
import org.apache.xerces.dom.EntityImpl;
import org.apache.xerces.dom.EntityReferenceImpl;
import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.dom.NotationImpl;
import org.apache.xerces.dom.PSVIAttrNSImpl;
import org.apache.xerces.dom.PSVIDocumentImpl;
import org.apache.xerces.dom.PSVIElementNSImpl;
import org.apache.xerces.dom.TextImpl;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.util.DOMErrorHandlerWrapper;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.ls.LSParserFilter;

public class AbstractDOMParser
  extends AbstractXMLDocumentParser
{
  protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
  protected static final String CREATE_ENTITY_REF_NODES = "http://apache.org/xml/features/dom/create-entity-ref-nodes";
  protected static final String INCLUDE_COMMENTS_FEATURE = "http://apache.org/xml/features/include-comments";
  protected static final String CREATE_CDATA_NODES_FEATURE = "http://apache.org/xml/features/create-cdata-nodes";
  protected static final String INCLUDE_IGNORABLE_WHITESPACE = "http://apache.org/xml/features/dom/include-ignorable-whitespace";
  protected static final String DEFER_NODE_EXPANSION = "http://apache.org/xml/features/dom/defer-node-expansion";
  private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/namespaces", "http://apache.org/xml/features/dom/create-entity-ref-nodes", "http://apache.org/xml/features/include-comments", "http://apache.org/xml/features/create-cdata-nodes", "http://apache.org/xml/features/dom/include-ignorable-whitespace", "http://apache.org/xml/features/dom/defer-node-expansion" };
  protected static final String DOCUMENT_CLASS_NAME = "http://apache.org/xml/properties/dom/document-class-name";
  protected static final String CURRENT_ELEMENT_NODE = "http://apache.org/xml/properties/dom/current-element-node";
  private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/dom/document-class-name", "http://apache.org/xml/properties/dom/current-element-node" };
  protected static final String DEFAULT_DOCUMENT_CLASS_NAME = "org.apache.xerces.dom.DocumentImpl";
  protected static final String CORE_DOCUMENT_CLASS_NAME = "org.apache.xerces.dom.CoreDocumentImpl";
  protected static final String PSVI_DOCUMENT_CLASS_NAME = "org.apache.xerces.dom.PSVIDocumentImpl";
  private static final boolean DEBUG_EVENTS = false;
  private static final boolean DEBUG_BASEURI = false;
  protected DOMErrorHandlerWrapper fErrorHandler = null;
  protected boolean fInDTD;
  protected boolean fCreateEntityRefNodes;
  protected boolean fIncludeIgnorableWhitespace;
  protected boolean fIncludeComments;
  protected boolean fCreateCDATANodes;
  protected Document fDocument;
  protected CoreDocumentImpl fDocumentImpl;
  protected boolean fStorePSVI;
  protected String fDocumentClassName;
  protected DocumentType fDocumentType;
  protected Node fCurrentNode;
  protected CDATASection fCurrentCDATASection;
  protected EntityImpl fCurrentEntityDecl;
  protected int fDeferredEntityDecl;
  protected final StringBuffer fStringBuffer = new StringBuffer(50);
  protected StringBuffer fInternalSubset;
  protected boolean fDeferNodeExpansion;
  protected boolean fNamespaceAware;
  protected DeferredDocumentImpl fDeferredDocumentImpl;
  protected int fDocumentIndex;
  protected int fDocumentTypeIndex;
  protected int fCurrentNodeIndex;
  protected int fCurrentCDATASectionIndex;
  protected boolean fInDTDExternalSubset;
  protected Node fRoot;
  protected boolean fInCDATASection;
  protected boolean fFirstChunk = false;
  protected boolean fFilterReject = false;
  protected final Stack fBaseURIStack = new Stack();
  protected int fRejectedElementDepth = 0;
  protected Stack fSkippedElemStack = null;
  protected boolean fInEntityRef = false;
  private final QName fAttrQName = new QName();
  private XMLLocator fLocator;
  protected LSParserFilter fDOMFilter = null;
  
  protected AbstractDOMParser(XMLParserConfiguration paramXMLParserConfiguration)
  {
    super(paramXMLParserConfiguration);
    fConfiguration.addRecognizedFeatures(RECOGNIZED_FEATURES);
    fConfiguration.setFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes", true);
    fConfiguration.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", true);
    fConfiguration.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", true);
    fConfiguration.setFeature("http://apache.org/xml/features/include-comments", true);
    fConfiguration.setFeature("http://apache.org/xml/features/create-cdata-nodes", true);
    fConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
    fConfiguration.setProperty("http://apache.org/xml/properties/dom/document-class-name", "org.apache.xerces.dom.DocumentImpl");
  }
  
  protected String getDocumentClassName()
  {
    return fDocumentClassName;
  }
  
  protected void setDocumentClassName(String paramString)
  {
    if (paramString == null) {
      paramString = "org.apache.xerces.dom.DocumentImpl";
    }
    if ((!paramString.equals("org.apache.xerces.dom.DocumentImpl")) && (!paramString.equals("org.apache.xerces.dom.PSVIDocumentImpl"))) {
      try
      {
        Class localClass = ObjectFactory.findProviderClass(paramString, ObjectFactory.findClassLoader(), true);
        if (!Document.class.isAssignableFrom(localClass)) {
          throw new IllegalArgumentException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "InvalidDocumentClassName", new Object[] { paramString }));
        }
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        throw new IllegalArgumentException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "MissingDocumentClassName", new Object[] { paramString }));
      }
    }
    fDocumentClassName = paramString;
    if (!paramString.equals("org.apache.xerces.dom.DocumentImpl")) {
      fDeferNodeExpansion = false;
    }
  }
  
  public Document getDocument()
  {
    return fDocument;
  }
  
  public final void dropDocumentReferences()
  {
    fDocument = null;
    fDocumentImpl = null;
    fDeferredDocumentImpl = null;
    fDocumentType = null;
    fCurrentNode = null;
    fCurrentCDATASection = null;
    fCurrentEntityDecl = null;
    fRoot = null;
  }
  
  public void reset()
    throws XNIException
  {
    super.reset();
    fCreateEntityRefNodes = fConfiguration.getFeature("http://apache.org/xml/features/dom/create-entity-ref-nodes");
    fIncludeIgnorableWhitespace = fConfiguration.getFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace");
    fDeferNodeExpansion = fConfiguration.getFeature("http://apache.org/xml/features/dom/defer-node-expansion");
    fNamespaceAware = fConfiguration.getFeature("http://xml.org/sax/features/namespaces");
    fIncludeComments = fConfiguration.getFeature("http://apache.org/xml/features/include-comments");
    fCreateCDATANodes = fConfiguration.getFeature("http://apache.org/xml/features/create-cdata-nodes");
    setDocumentClassName((String)fConfiguration.getProperty("http://apache.org/xml/properties/dom/document-class-name"));
    fDocument = null;
    fDocumentImpl = null;
    fStorePSVI = false;
    fDocumentType = null;
    fDocumentTypeIndex = -1;
    fDeferredDocumentImpl = null;
    fCurrentNode = null;
    fStringBuffer.setLength(0);
    fRoot = null;
    fInDTD = false;
    fInDTDExternalSubset = false;
    fInCDATASection = false;
    fFirstChunk = false;
    fCurrentCDATASection = null;
    fCurrentCDATASectionIndex = -1;
    fBaseURIStack.removeAllElements();
  }
  
  public void setLocale(Locale paramLocale)
  {
    fConfiguration.setLocale(paramLocale);
  }
  
  public void startGeneralEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (!fDeferNodeExpansion)
    {
      if (fFilterReject) {
        return;
      }
      setCharacterData(true);
      EntityReference localEntityReference = fDocument.createEntityReference(paramString1);
      if (fDocumentImpl != null)
      {
        EntityReferenceImpl localEntityReferenceImpl = (EntityReferenceImpl)localEntityReference;
        localEntityReferenceImpl.setBaseURI(paramXMLResourceIdentifier.getExpandedSystemId());
        if (fDocumentType != null)
        {
          NamedNodeMap localNamedNodeMap = fDocumentType.getEntities();
          fCurrentEntityDecl = ((EntityImpl)localNamedNodeMap.getNamedItem(paramString1));
          if (fCurrentEntityDecl != null) {
            fCurrentEntityDecl.setInputEncoding(paramString2);
          }
        }
        localEntityReferenceImpl.needsSyncChildren(false);
      }
      fInEntityRef = true;
      fCurrentNode.appendChild(localEntityReference);
      fCurrentNode = localEntityReference;
    }
    else
    {
      int i = fDeferredDocumentImpl.createDeferredEntityReference(paramString1, paramXMLResourceIdentifier.getExpandedSystemId());
      if (fDocumentTypeIndex != -1) {
        for (int j = fDeferredDocumentImpl.getLastChild(fDocumentTypeIndex, false); j != -1; j = fDeferredDocumentImpl.getRealPrevSibling(j, false))
        {
          int k = fDeferredDocumentImpl.getNodeType(j, false);
          if (k == 6)
          {
            String str = fDeferredDocumentImpl.getNodeName(j, false);
            if (str.equals(paramString1))
            {
              fDeferredEntityDecl = j;
              fDeferredDocumentImpl.setInputEncoding(j, paramString2);
              break;
            }
          }
        }
      }
      fDeferredDocumentImpl.appendChild(fCurrentNodeIndex, i);
      fCurrentNodeIndex = i;
    }
  }
  
  public void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fInDTD) {
      return;
    }
    if (!fDeferNodeExpansion)
    {
      if ((fCurrentEntityDecl != null) && (!fFilterReject))
      {
        fCurrentEntityDecl.setXmlEncoding(paramString2);
        if (paramString1 != null) {
          fCurrentEntityDecl.setXmlVersion(paramString1);
        }
      }
    }
    else if (fDeferredEntityDecl != -1) {
      fDeferredDocumentImpl.setEntityInfo(fDeferredEntityDecl, paramString1, paramString2);
    }
  }
  
  public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fInDTD)
    {
      if ((fInternalSubset != null) && (!fInDTDExternalSubset))
      {
        fInternalSubset.append("<!--");
        if (length > 0) {
          fInternalSubset.append(ch, offset, length);
        }
        fInternalSubset.append("-->");
      }
      return;
    }
    if ((!fIncludeComments) || (fFilterReject)) {
      return;
    }
    if (!fDeferNodeExpansion)
    {
      Comment localComment = fDocument.createComment(paramXMLString.toString());
      setCharacterData(false);
      fCurrentNode.appendChild(localComment);
      if ((fDOMFilter != null) && (!fInEntityRef) && ((fDOMFilter.getWhatToShow() & 0x80) != 0))
      {
        int j = fDOMFilter.acceptNode(localComment);
        switch (j)
        {
        case 4: 
          throw Abort.INSTANCE;
        case 2: 
        case 3: 
          fCurrentNode.removeChild(localComment);
          fFirstChunk = true;
          return;
        }
      }
    }
    else
    {
      int i = fDeferredDocumentImpl.createDeferredComment(paramXMLString.toString());
      fDeferredDocumentImpl.appendChild(fCurrentNodeIndex, i);
    }
  }
  
  public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (fInDTD)
    {
      if ((fInternalSubset != null) && (!fInDTDExternalSubset))
      {
        fInternalSubset.append("<?");
        fInternalSubset.append(paramString);
        if (length > 0) {
          fInternalSubset.append(' ').append(ch, offset, length);
        }
        fInternalSubset.append("?>");
      }
      return;
    }
    if (!fDeferNodeExpansion)
    {
      if (fFilterReject) {
        return;
      }
      ProcessingInstruction localProcessingInstruction = fDocument.createProcessingInstruction(paramString, paramXMLString.toString());
      setCharacterData(false);
      fCurrentNode.appendChild(localProcessingInstruction);
      if ((fDOMFilter != null) && (!fInEntityRef) && ((fDOMFilter.getWhatToShow() & 0x40) != 0))
      {
        int j = fDOMFilter.acceptNode(localProcessingInstruction);
        switch (j)
        {
        case 4: 
          throw Abort.INSTANCE;
        case 2: 
        case 3: 
          fCurrentNode.removeChild(localProcessingInstruction);
          fFirstChunk = true;
          return;
        }
      }
    }
    else
    {
      int i = fDeferredDocumentImpl.createDeferredProcessingInstruction(paramString, paramXMLString.toString());
      fDeferredDocumentImpl.appendChild(fCurrentNodeIndex, i);
    }
  }
  
  public void startDocument(XMLLocator paramXMLLocator, String paramString, NamespaceContext paramNamespaceContext, Augmentations paramAugmentations)
    throws XNIException
  {
    fLocator = paramXMLLocator;
    if (!fDeferNodeExpansion)
    {
      if (fDocumentClassName.equals("org.apache.xerces.dom.DocumentImpl"))
      {
        fDocument = new DocumentImpl();
        fDocumentImpl = ((CoreDocumentImpl)fDocument);
        fDocumentImpl.setStrictErrorChecking(false);
        fDocumentImpl.setInputEncoding(paramString);
        fDocumentImpl.setDocumentURI(paramXMLLocator.getExpandedSystemId());
      }
      else if (fDocumentClassName.equals("org.apache.xerces.dom.PSVIDocumentImpl"))
      {
        fDocument = new PSVIDocumentImpl();
        fDocumentImpl = ((CoreDocumentImpl)fDocument);
        fStorePSVI = true;
        fDocumentImpl.setStrictErrorChecking(false);
        fDocumentImpl.setInputEncoding(paramString);
        fDocumentImpl.setDocumentURI(paramXMLLocator.getExpandedSystemId());
      }
      else
      {
        try
        {
          ClassLoader localClassLoader = ObjectFactory.findClassLoader();
          Class localClass1 = ObjectFactory.findProviderClass(fDocumentClassName, localClassLoader, true);
          fDocument = ((Document)localClass1.newInstance());
          Class localClass2 = ObjectFactory.findProviderClass("org.apache.xerces.dom.CoreDocumentImpl", localClassLoader, true);
          if (localClass2.isAssignableFrom(localClass1))
          {
            fDocumentImpl = ((CoreDocumentImpl)fDocument);
            Class localClass3 = ObjectFactory.findProviderClass("org.apache.xerces.dom.PSVIDocumentImpl", localClassLoader, true);
            if (localClass3.isAssignableFrom(localClass1)) {
              fStorePSVI = true;
            }
            fDocumentImpl.setStrictErrorChecking(false);
            fDocumentImpl.setInputEncoding(paramString);
            if (paramXMLLocator != null) {
              fDocumentImpl.setDocumentURI(paramXMLLocator.getExpandedSystemId());
            }
          }
        }
        catch (ClassNotFoundException localClassNotFoundException) {}catch (Exception localException)
        {
          throw new RuntimeException(DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "CannotCreateDocumentClass", new Object[] { fDocumentClassName }));
        }
      }
      fCurrentNode = fDocument;
    }
    else
    {
      fDeferredDocumentImpl = new DeferredDocumentImpl(fNamespaceAware);
      fDocument = fDeferredDocumentImpl;
      fDocumentIndex = fDeferredDocumentImpl.createDeferredDocument();
      fDeferredDocumentImpl.setInputEncoding(paramString);
      fDeferredDocumentImpl.setDocumentURI(paramXMLLocator.getExpandedSystemId());
      fCurrentNodeIndex = fDocumentIndex;
    }
  }
  
  public void xmlDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {
    if (!fDeferNodeExpansion)
    {
      if (fDocumentImpl != null)
      {
        if (paramString1 != null) {
          fDocumentImpl.setXmlVersion(paramString1);
        }
        fDocumentImpl.setXmlEncoding(paramString2);
        fDocumentImpl.setXmlStandalone("yes".equals(paramString3));
      }
    }
    else
    {
      if (paramString1 != null) {
        fDeferredDocumentImpl.setXmlVersion(paramString1);
      }
      fDeferredDocumentImpl.setXmlEncoding(paramString2);
      fDeferredDocumentImpl.setXmlStandalone("yes".equals(paramString3));
    }
  }
  
  public void doctypeDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {
    if (!fDeferNodeExpansion)
    {
      if (fDocumentImpl != null)
      {
        fDocumentType = fDocumentImpl.createDocumentType(paramString1, paramString2, paramString3);
        fCurrentNode.appendChild(fDocumentType);
      }
    }
    else
    {
      fDocumentTypeIndex = fDeferredDocumentImpl.createDeferredDocumentType(paramString1, paramString2, paramString3);
      fDeferredDocumentImpl.appendChild(fCurrentNodeIndex, fDocumentTypeIndex);
    }
  }
  
  public void startElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    int k;
    int m;
    if (!fDeferNodeExpansion)
    {
      if (fFilterReject)
      {
        fRejectedElementDepth += 1;
        return;
      }
      Element localElement = createElementNode(paramQName);
      int j = paramXMLAttributes.getLength();
      k = 0;
      Object localObject2;
      Object localObject3;
      for (m = 0; m < j; m++)
      {
        paramXMLAttributes.getName(m, fAttrQName);
        localObject2 = createAttrNode(fAttrQName);
        localObject3 = paramXMLAttributes.getValue(m);
        AttributePSVI localAttributePSVI2 = (AttributePSVI)paramXMLAttributes.getAugmentations(m).getItem("ATTRIBUTE_PSVI");
        if ((fStorePSVI) && (localAttributePSVI2 != null)) {
          ((PSVIAttrNSImpl)localObject2).setPSVI(localAttributePSVI2);
        }
        ((Attr)localObject2).setValue((String)localObject3);
        boolean bool3 = paramXMLAttributes.isSpecified(m);
        if ((!bool3) && ((k != 0) || ((fAttrQName.uri != null) && (fAttrQName.uri != NamespaceContext.XMLNS_URI) && (fAttrQName.prefix == null))))
        {
          localElement.setAttributeNodeNS((Attr)localObject2);
          k = 1;
        }
        else
        {
          localElement.setAttributeNode((Attr)localObject2);
        }
        if (fDocumentImpl != null)
        {
          AttrImpl localAttrImpl = (AttrImpl)localObject2;
          Object localObject4 = null;
          boolean bool4 = false;
          if ((localAttributePSVI2 != null) && (fNamespaceAware))
          {
            localObject4 = localAttributePSVI2.getMemberTypeDefinition();
            if (localObject4 == null)
            {
              localObject4 = localAttributePSVI2.getTypeDefinition();
              if (localObject4 != null)
              {
                bool4 = ((XSSimpleType)localObject4).isIDType();
                localAttrImpl.setType(localObject4);
              }
            }
            else
            {
              bool4 = ((XSSimpleType)localObject4).isIDType();
              localAttrImpl.setType(localObject4);
            }
          }
          else
          {
            boolean bool5 = Boolean.TRUE.equals(paramXMLAttributes.getAugmentations(m).getItem("ATTRIBUTE_DECLARED"));
            if (bool5)
            {
              localObject4 = paramXMLAttributes.getType(m);
              bool4 = "ID".equals(localObject4);
            }
            localAttrImpl.setType(localObject4);
          }
          if (bool4) {
            ((ElementImpl)localElement).setIdAttributeNode((Attr)localObject2, true);
          }
          localAttrImpl.setSpecified(bool3);
        }
      }
      setCharacterData(false);
      if (paramAugmentations != null)
      {
        localObject2 = (ElementPSVI)paramAugmentations.getItem("ELEMENT_PSVI");
        if ((localObject2 != null) && (fNamespaceAware))
        {
          localObject3 = ((ElementPSVI)localObject2).getMemberTypeDefinition();
          if (localObject3 == null) {
            localObject3 = ((ElementPSVI)localObject2).getTypeDefinition();
          }
          ((ElementNSImpl)localElement).setType((XSTypeDefinition)localObject3);
        }
      }
      if ((fDOMFilter != null) && (!fInEntityRef)) {
        if (fRoot == null)
        {
          fRoot = localElement;
        }
        else
        {
          int n = fDOMFilter.startElement(localElement);
          switch (n)
          {
          case 4: 
            throw Abort.INSTANCE;
          case 2: 
            fFilterReject = true;
            fRejectedElementDepth = 0;
            return;
          case 3: 
            fFirstChunk = true;
            fSkippedElemStack.push(Boolean.TRUE);
            return;
          }
          if (!fSkippedElemStack.isEmpty()) {
            fSkippedElemStack.push(Boolean.FALSE);
          }
        }
      }
      fCurrentNode.appendChild(localElement);
      fCurrentNode = localElement;
    }
    else
    {
      int i = fDeferredDocumentImpl.createDeferredElement(fNamespaceAware ? uri : null, rawname);
      Object localObject1 = null;
      k = paramXMLAttributes.getLength();
      for (m = k - 1; m >= 0; m--)
      {
        AttributePSVI localAttributePSVI1 = (AttributePSVI)paramXMLAttributes.getAugmentations(m).getItem("ATTRIBUTE_PSVI");
        boolean bool1 = false;
        if ((localAttributePSVI1 != null) && (fNamespaceAware))
        {
          localObject1 = localAttributePSVI1.getMemberTypeDefinition();
          if (localObject1 == null)
          {
            localObject1 = localAttributePSVI1.getTypeDefinition();
            if (localObject1 != null) {
              bool1 = ((XSSimpleType)localObject1).isIDType();
            }
          }
          else
          {
            bool1 = ((XSSimpleType)localObject1).isIDType();
          }
        }
        else
        {
          boolean bool2 = Boolean.TRUE.equals(paramXMLAttributes.getAugmentations(m).getItem("ATTRIBUTE_DECLARED"));
          if (bool2)
          {
            localObject1 = paramXMLAttributes.getType(m);
            bool1 = "ID".equals(localObject1);
          }
        }
        fDeferredDocumentImpl.setDeferredAttribute(i, paramXMLAttributes.getQName(m), paramXMLAttributes.getURI(m), paramXMLAttributes.getValue(m), paramXMLAttributes.isSpecified(m), bool1, localObject1);
      }
      fDeferredDocumentImpl.appendChild(fCurrentNodeIndex, i);
      fCurrentNodeIndex = i;
    }
  }
  
  public void emptyElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    startElement(paramQName, paramXMLAttributes, paramAugmentations);
    endElement(paramQName, paramAugmentations);
  }
  
  public void characters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (!fDeferNodeExpansion)
    {
      if (fFilterReject) {
        return;
      }
      if ((fInCDATASection) && (fCreateCDATANodes))
      {
        if (fCurrentCDATASection == null)
        {
          fCurrentCDATASection = fDocument.createCDATASection(paramXMLString.toString());
          fCurrentNode.appendChild(fCurrentCDATASection);
          fCurrentNode = fCurrentCDATASection;
        }
        else
        {
          fCurrentCDATASection.appendData(paramXMLString.toString());
        }
      }
      else if (!fInDTD)
      {
        if (length == 0) {
          return;
        }
        Node localNode = fCurrentNode.getLastChild();
        if ((localNode != null) && (localNode.getNodeType() == 3))
        {
          if (fFirstChunk)
          {
            if (fDocumentImpl != null)
            {
              fStringBuffer.append(((TextImpl)localNode).removeData());
            }
            else
            {
              fStringBuffer.append(((Text)localNode).getData());
              ((Text)localNode).setNodeValue(null);
            }
            fFirstChunk = false;
          }
          if (length > 0) {
            fStringBuffer.append(ch, offset, length);
          }
        }
        else
        {
          fFirstChunk = true;
          Text localText = fDocument.createTextNode(paramXMLString.toString());
          fCurrentNode.appendChild(localText);
        }
      }
    }
    else if ((fInCDATASection) && (fCreateCDATANodes))
    {
      int i;
      if (fCurrentCDATASectionIndex == -1)
      {
        i = fDeferredDocumentImpl.createDeferredCDATASection(paramXMLString.toString());
        fDeferredDocumentImpl.appendChild(fCurrentNodeIndex, i);
        fCurrentCDATASectionIndex = i;
        fCurrentNodeIndex = i;
      }
      else
      {
        i = fDeferredDocumentImpl.createDeferredTextNode(paramXMLString.toString(), false);
        fDeferredDocumentImpl.appendChild(fCurrentNodeIndex, i);
      }
    }
    else if (!fInDTD)
    {
      if (length == 0) {
        return;
      }
      String str = paramXMLString.toString();
      int j = fDeferredDocumentImpl.createDeferredTextNode(str, false);
      fDeferredDocumentImpl.appendChild(fCurrentNodeIndex, j);
    }
  }
  
  public void ignorableWhitespace(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((!fIncludeIgnorableWhitespace) || (fFilterReject)) {
      return;
    }
    if (!fDeferNodeExpansion)
    {
      Node localNode = fCurrentNode.getLastChild();
      Text localText;
      if ((localNode != null) && (localNode.getNodeType() == 3))
      {
        localText = (Text)localNode;
        localText.appendData(paramXMLString.toString());
      }
      else
      {
        localText = fDocument.createTextNode(paramXMLString.toString());
        if (fDocumentImpl != null)
        {
          TextImpl localTextImpl = (TextImpl)localText;
          localTextImpl.setIgnorableWhitespace(true);
        }
        fCurrentNode.appendChild(localText);
      }
    }
    else
    {
      int i = fDeferredDocumentImpl.createDeferredTextNode(paramXMLString.toString(), true);
      fDeferredDocumentImpl.appendChild(fCurrentNodeIndex, i);
    }
  }
  
  public void endElement(QName paramQName, Augmentations paramAugmentations)
    throws XNIException
  {
    Object localObject;
    if (!fDeferNodeExpansion)
    {
      if ((paramAugmentations != null) && (fDocumentImpl != null) && ((fNamespaceAware) || (fStorePSVI)))
      {
        ElementPSVI localElementPSVI1 = (ElementPSVI)paramAugmentations.getItem("ELEMENT_PSVI");
        if (localElementPSVI1 != null)
        {
          if (fNamespaceAware)
          {
            localObject = localElementPSVI1.getMemberTypeDefinition();
            if (localObject == null) {
              localObject = localElementPSVI1.getTypeDefinition();
            }
            ((ElementNSImpl)fCurrentNode).setType((XSTypeDefinition)localObject);
          }
          if (fStorePSVI) {
            ((PSVIElementNSImpl)fCurrentNode).setPSVI(localElementPSVI1);
          }
        }
      }
      if (fDOMFilter != null)
      {
        if (fFilterReject)
        {
          if (fRejectedElementDepth-- == 0) {
            fFilterReject = false;
          }
          return;
        }
        if ((!fSkippedElemStack.isEmpty()) && (fSkippedElemStack.pop() == Boolean.TRUE)) {
          return;
        }
        setCharacterData(false);
        if ((fCurrentNode != fRoot) && (!fInEntityRef) && ((fDOMFilter.getWhatToShow() & 0x1) != 0))
        {
          int i = fDOMFilter.acceptNode(fCurrentNode);
          switch (i)
          {
          case 4: 
            throw Abort.INSTANCE;
          case 2: 
            localObject = fCurrentNode.getParentNode();
            ((Node)localObject).removeChild(fCurrentNode);
            fCurrentNode = ((Node)localObject);
            return;
          case 3: 
            fFirstChunk = true;
            localObject = fCurrentNode.getParentNode();
            NodeList localNodeList = fCurrentNode.getChildNodes();
            int j = localNodeList.getLength();
            for (int k = 0; k < j; k++) {
              ((Node)localObject).appendChild(localNodeList.item(0));
            }
            ((Node)localObject).removeChild(fCurrentNode);
            fCurrentNode = ((Node)localObject);
            return;
          }
        }
        fCurrentNode = fCurrentNode.getParentNode();
      }
      else
      {
        setCharacterData(false);
        fCurrentNode = fCurrentNode.getParentNode();
      }
    }
    else
    {
      if (paramAugmentations != null)
      {
        ElementPSVI localElementPSVI2 = (ElementPSVI)paramAugmentations.getItem("ELEMENT_PSVI");
        if (localElementPSVI2 != null)
        {
          localObject = localElementPSVI2.getMemberTypeDefinition();
          if (localObject == null) {
            localObject = localElementPSVI2.getTypeDefinition();
          }
          fDeferredDocumentImpl.setTypeInfo(fCurrentNodeIndex, localObject);
        }
      }
      fCurrentNodeIndex = fDeferredDocumentImpl.getParentNode(fCurrentNodeIndex, false);
    }
  }
  
  public void startCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    fInCDATASection = true;
    if (!fDeferNodeExpansion)
    {
      if (fFilterReject) {
        return;
      }
      if (fCreateCDATANodes) {
        setCharacterData(false);
      }
    }
  }
  
  public void endCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    fInCDATASection = false;
    if (!fDeferNodeExpansion)
    {
      if (fFilterReject) {
        return;
      }
      if (fCurrentCDATASection != null)
      {
        if ((fDOMFilter != null) && (!fInEntityRef) && ((fDOMFilter.getWhatToShow() & 0x8) != 0))
        {
          int i = fDOMFilter.acceptNode(fCurrentCDATASection);
          switch (i)
          {
          case 4: 
            throw Abort.INSTANCE;
          case 2: 
          case 3: 
            Node localNode = fCurrentNode.getParentNode();
            localNode.removeChild(fCurrentCDATASection);
            fCurrentNode = localNode;
            return;
          }
        }
        fCurrentNode = fCurrentNode.getParentNode();
        fCurrentCDATASection = null;
      }
    }
    else if (fCurrentCDATASectionIndex != -1)
    {
      fCurrentNodeIndex = fDeferredDocumentImpl.getParentNode(fCurrentNodeIndex, false);
      fCurrentCDATASectionIndex = -1;
    }
  }
  
  public void endDocument(Augmentations paramAugmentations)
    throws XNIException
  {
    if (!fDeferNodeExpansion)
    {
      if (fDocumentImpl != null)
      {
        if (fLocator != null) {
          fDocumentImpl.setInputEncoding(fLocator.getEncoding());
        }
        fDocumentImpl.setStrictErrorChecking(true);
      }
      fCurrentNode = null;
    }
    else
    {
      if (fLocator != null) {
        fDeferredDocumentImpl.setInputEncoding(fLocator.getEncoding());
      }
      fCurrentNodeIndex = -1;
    }
  }
  
  public void endGeneralEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    Object localObject;
    int i;
    int n;
    if (!fDeferNodeExpansion)
    {
      if (fFilterReject) {
        return;
      }
      setCharacterData(true);
      if (fDocumentType != null)
      {
        NamedNodeMap localNamedNodeMap = fDocumentType.getEntities();
        fCurrentEntityDecl = ((EntityImpl)localNamedNodeMap.getNamedItem(paramString));
        if (fCurrentEntityDecl != null)
        {
          if ((fCurrentEntityDecl != null) && (fCurrentEntityDecl.getFirstChild() == null))
          {
            fCurrentEntityDecl.setReadOnly(false, true);
            for (Node localNode1 = fCurrentNode.getFirstChild(); localNode1 != null; localNode1 = localNode1.getNextSibling())
            {
              localObject = localNode1.cloneNode(true);
              fCurrentEntityDecl.appendChild((Node)localObject);
            }
            fCurrentEntityDecl.setReadOnly(true, true);
          }
          fCurrentEntityDecl = null;
        }
      }
      fInEntityRef = false;
      i = 0;
      if (fCreateEntityRefNodes)
      {
        if (fDocumentImpl != null) {
          ((NodeImpl)fCurrentNode).setReadOnly(true, true);
        }
        int j;
        if ((fDOMFilter != null) && ((fDOMFilter.getWhatToShow() & 0x10) != 0)) {
          j = fDOMFilter.acceptNode(fCurrentNode);
        }
        switch (j)
        {
        case 4: 
          throw Abort.INSTANCE;
        case 2: 
          localObject = fCurrentNode.getParentNode();
          ((Node)localObject).removeChild(fCurrentNode);
          fCurrentNode = ((Node)localObject);
          return;
        case 3: 
          fFirstChunk = true;
          i = 1;
          break;
        default: 
          fCurrentNode = fCurrentNode.getParentNode();
          break;
          fCurrentNode = fCurrentNode.getParentNode();
        }
      }
      if ((!fCreateEntityRefNodes) || (i != 0))
      {
        NodeList localNodeList = fCurrentNode.getChildNodes();
        localObject = fCurrentNode.getParentNode();
        n = localNodeList.getLength();
        if (n > 0)
        {
          Node localNode2 = fCurrentNode.getPreviousSibling();
          Node localNode3 = localNodeList.item(0);
          if ((localNode2 != null) && (localNode2.getNodeType() == 3) && (localNode3.getNodeType() == 3))
          {
            ((Text)localNode2).appendData(localNode3.getNodeValue());
            fCurrentNode.removeChild(localNode3);
          }
          else
          {
            localNode2 = ((Node)localObject).insertBefore(localNode3, fCurrentNode);
            handleBaseURI(localNode2);
          }
          for (int i2 = 1; i2 < n; i2++)
          {
            localNode2 = ((Node)localObject).insertBefore(localNodeList.item(0), fCurrentNode);
            handleBaseURI(localNode2);
          }
        }
        ((Node)localObject).removeChild(fCurrentNode);
        fCurrentNode = ((Node)localObject);
      }
    }
    else
    {
      int k;
      if (fDocumentTypeIndex != -1) {
        for (i = fDeferredDocumentImpl.getLastChild(fDocumentTypeIndex, false); i != -1; i = fDeferredDocumentImpl.getRealPrevSibling(i, false))
        {
          k = fDeferredDocumentImpl.getNodeType(i, false);
          if (k == 6)
          {
            localObject = fDeferredDocumentImpl.getNodeName(i, false);
            if (((String)localObject).equals(paramString))
            {
              fDeferredEntityDecl = i;
              break;
            }
          }
        }
      }
      int m;
      if ((fDeferredEntityDecl != -1) && (fDeferredDocumentImpl.getLastChild(fDeferredEntityDecl, false) == -1))
      {
        i = -1;
        for (k = fDeferredDocumentImpl.getLastChild(fCurrentNodeIndex, false); k != -1; k = fDeferredDocumentImpl.getRealPrevSibling(k, false))
        {
          m = fDeferredDocumentImpl.cloneNode(k, true);
          fDeferredDocumentImpl.insertBefore(fDeferredEntityDecl, m, i);
          i = m;
        }
      }
      if (fCreateEntityRefNodes)
      {
        fCurrentNodeIndex = fDeferredDocumentImpl.getParentNode(fCurrentNodeIndex, false);
      }
      else
      {
        i = fDeferredDocumentImpl.getLastChild(fCurrentNodeIndex, false);
        k = fDeferredDocumentImpl.getParentNode(fCurrentNodeIndex, false);
        m = fCurrentNodeIndex;
        n = i;
        int i1 = -1;
        while (i != -1)
        {
          handleBaseURI(i);
          i1 = fDeferredDocumentImpl.getRealPrevSibling(i, false);
          fDeferredDocumentImpl.insertBefore(k, i, m);
          m = i;
          i = i1;
        }
        if (n != -1)
        {
          fDeferredDocumentImpl.setAsLastChild(k, n);
        }
        else
        {
          i1 = fDeferredDocumentImpl.getRealPrevSibling(m, false);
          fDeferredDocumentImpl.setAsLastChild(k, i1);
        }
        fCurrentNodeIndex = k;
      }
      fDeferredEntityDecl = -1;
    }
  }
  
  protected final void handleBaseURI(Node paramNode)
  {
    if (fDocumentImpl != null)
    {
      String str = null;
      int i = paramNode.getNodeType();
      if (i == 1)
      {
        if (fNamespaceAware)
        {
          if (((Element)paramNode).getAttributeNodeNS("http://www.w3.org/XML/1998/namespace", "base") == null) {}
        }
        else if (((Element)paramNode).getAttributeNode("xml:base") != null) {
          return;
        }
        str = ((EntityReferenceImpl)fCurrentNode).getBaseURI();
        if ((str != null) && (!str.equals(fDocumentImpl.getDocumentURI()))) {
          if (fNamespaceAware) {
            ((Element)paramNode).setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:base", str);
          } else {
            ((Element)paramNode).setAttribute("xml:base", str);
          }
        }
      }
      else if (i == 7)
      {
        str = ((EntityReferenceImpl)fCurrentNode).getBaseURI();
        if ((str != null) && (fErrorHandler != null))
        {
          DOMErrorImpl localDOMErrorImpl = new DOMErrorImpl();
          fType = "pi-base-uri-not-preserved";
          fRelatedData = str;
          fSeverity = 1;
          fErrorHandler.getErrorHandler().handleError(localDOMErrorImpl);
        }
      }
    }
  }
  
  protected final void handleBaseURI(int paramInt)
  {
    int i = fDeferredDocumentImpl.getNodeType(paramInt, false);
    String str;
    if (i == 1)
    {
      str = fDeferredDocumentImpl.getNodeValueString(fCurrentNodeIndex, false);
      if (str == null) {
        str = fDeferredDocumentImpl.getDeferredEntityBaseURI(fDeferredEntityDecl);
      }
      if ((str != null) && (!str.equals(fDeferredDocumentImpl.getDocumentURI()))) {
        fDeferredDocumentImpl.setDeferredAttribute(paramInt, "xml:base", "http://www.w3.org/XML/1998/namespace", str, true);
      }
    }
    else if (i == 7)
    {
      str = fDeferredDocumentImpl.getNodeValueString(fCurrentNodeIndex, false);
      if (str == null) {
        str = fDeferredDocumentImpl.getDeferredEntityBaseURI(fDeferredEntityDecl);
      }
      if ((str != null) && (fErrorHandler != null))
      {
        DOMErrorImpl localDOMErrorImpl = new DOMErrorImpl();
        fType = "pi-base-uri-not-preserved";
        fRelatedData = str;
        fSeverity = 1;
        fErrorHandler.getErrorHandler().handleError(localDOMErrorImpl);
      }
    }
  }
  
  public void startDTD(XMLLocator paramXMLLocator, Augmentations paramAugmentations)
    throws XNIException
  {
    fInDTD = true;
    if (paramXMLLocator != null) {
      fBaseURIStack.push(paramXMLLocator.getBaseSystemId());
    }
    if ((fDeferNodeExpansion) || (fDocumentImpl != null)) {
      fInternalSubset = new StringBuffer(1024);
    }
  }
  
  public void endDTD(Augmentations paramAugmentations)
    throws XNIException
  {
    fInDTD = false;
    if (!fBaseURIStack.isEmpty()) {
      fBaseURIStack.pop();
    }
    String str = (fInternalSubset != null) && (fInternalSubset.length() > 0) ? fInternalSubset.toString() : null;
    if (fDeferNodeExpansion)
    {
      if (str != null) {
        fDeferredDocumentImpl.setInternalSubset(fDocumentTypeIndex, str);
      }
    }
    else if ((fDocumentImpl != null) && (str != null)) {
      ((DocumentTypeImpl)fDocumentType).setInternalSubset(str);
    }
  }
  
  public void startConditional(short paramShort, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void endConditional(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void startExternalSubset(XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    fBaseURIStack.push(paramXMLResourceIdentifier.getBaseSystemId());
    fInDTDExternalSubset = true;
  }
  
  public void endExternalSubset(Augmentations paramAugmentations)
    throws XNIException
  {
    fInDTDExternalSubset = false;
    fBaseURIStack.pop();
  }
  
  public void internalEntityDecl(String paramString, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
    throws XNIException
  {
    Object localObject;
    if ((fInternalSubset != null) && (!fInDTDExternalSubset))
    {
      fInternalSubset.append("<!ENTITY ");
      if (paramString.startsWith("%"))
      {
        fInternalSubset.append("% ");
        fInternalSubset.append(paramString.substring(1));
      }
      else
      {
        fInternalSubset.append(paramString);
      }
      fInternalSubset.append(' ');
      localObject = paramXMLString2.toString();
      int j = ((String)localObject).indexOf('\'') == -1 ? 1 : 0;
      fInternalSubset.append(j != 0 ? '\'' : '"');
      fInternalSubset.append((String)localObject);
      fInternalSubset.append(j != 0 ? '\'' : '"');
      fInternalSubset.append(">\n");
    }
    if (paramString.startsWith("%")) {
      return;
    }
    if (fDocumentType != null)
    {
      localObject = fDocumentType.getEntities();
      EntityImpl localEntityImpl = (EntityImpl)((NamedNodeMap)localObject).getNamedItem(paramString);
      if (localEntityImpl == null)
      {
        localEntityImpl = (EntityImpl)fDocumentImpl.createEntity(paramString);
        localEntityImpl.setBaseURI((String)fBaseURIStack.peek());
        ((NamedNodeMap)localObject).setNamedItem(localEntityImpl);
      }
    }
    if (fDocumentTypeIndex != -1)
    {
      int i = 0;
      int m;
      for (int k = fDeferredDocumentImpl.getLastChild(fDocumentTypeIndex, false); k != -1; k = fDeferredDocumentImpl.getRealPrevSibling(k, false))
      {
        m = fDeferredDocumentImpl.getNodeType(k, false);
        if (m == 6)
        {
          String str = fDeferredDocumentImpl.getNodeName(k, false);
          if (str.equals(paramString))
          {
            i = 1;
            break;
          }
        }
      }
      if (i == 0)
      {
        m = fDeferredDocumentImpl.createDeferredEntity(paramString, null, null, null, (String)fBaseURIStack.peek());
        fDeferredDocumentImpl.appendChild(fDocumentTypeIndex, m);
      }
    }
  }
  
  public void externalEntityDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    String str1 = paramXMLResourceIdentifier.getPublicId();
    String str2 = paramXMLResourceIdentifier.getLiteralSystemId();
    if ((fInternalSubset != null) && (!fInDTDExternalSubset))
    {
      fInternalSubset.append("<!ENTITY ");
      if (paramString.startsWith("%"))
      {
        fInternalSubset.append("% ");
        fInternalSubset.append(paramString.substring(1));
      }
      else
      {
        fInternalSubset.append(paramString);
      }
      fInternalSubset.append(' ');
      if (str1 != null)
      {
        fInternalSubset.append("PUBLIC '");
        fInternalSubset.append(str1);
        fInternalSubset.append("' '");
      }
      else
      {
        fInternalSubset.append("SYSTEM '");
      }
      fInternalSubset.append(str2);
      fInternalSubset.append("'>\n");
    }
    if (paramString.startsWith("%")) {
      return;
    }
    if (fDocumentType != null)
    {
      NamedNodeMap localNamedNodeMap = fDocumentType.getEntities();
      EntityImpl localEntityImpl = (EntityImpl)localNamedNodeMap.getNamedItem(paramString);
      if (localEntityImpl == null)
      {
        localEntityImpl = (EntityImpl)fDocumentImpl.createEntity(paramString);
        localEntityImpl.setPublicId(str1);
        localEntityImpl.setSystemId(str2);
        localEntityImpl.setBaseURI(paramXMLResourceIdentifier.getBaseSystemId());
        localNamedNodeMap.setNamedItem(localEntityImpl);
      }
    }
    if (fDocumentTypeIndex != -1)
    {
      int i = 0;
      int k;
      for (int j = fDeferredDocumentImpl.getLastChild(fDocumentTypeIndex, false); j != -1; j = fDeferredDocumentImpl.getRealPrevSibling(j, false))
      {
        k = fDeferredDocumentImpl.getNodeType(j, false);
        if (k == 6)
        {
          String str3 = fDeferredDocumentImpl.getNodeName(j, false);
          if (str3.equals(paramString))
          {
            i = 1;
            break;
          }
        }
      }
      if (i == 0)
      {
        k = fDeferredDocumentImpl.createDeferredEntity(paramString, str1, str2, null, paramXMLResourceIdentifier.getBaseSystemId());
        fDeferredDocumentImpl.appendChild(fDocumentTypeIndex, k);
      }
    }
  }
  
  public void startParameterEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((paramAugmentations != null) && (fInternalSubset != null) && (!fInDTDExternalSubset) && (Boolean.TRUE.equals(paramAugmentations.getItem("ENTITY_SKIPPED")))) {
      fInternalSubset.append(paramString1).append(";\n");
    }
    fBaseURIStack.push(paramXMLResourceIdentifier.getExpandedSystemId());
  }
  
  public void endParameterEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    fBaseURIStack.pop();
  }
  
  public void unparsedEntityDecl(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    String str1 = paramXMLResourceIdentifier.getPublicId();
    String str2 = paramXMLResourceIdentifier.getLiteralSystemId();
    if ((fInternalSubset != null) && (!fInDTDExternalSubset))
    {
      fInternalSubset.append("<!ENTITY ");
      fInternalSubset.append(paramString1);
      fInternalSubset.append(' ');
      if (str1 != null)
      {
        fInternalSubset.append("PUBLIC '");
        fInternalSubset.append(str1);
        if (str2 != null)
        {
          fInternalSubset.append("' '");
          fInternalSubset.append(str2);
        }
      }
      else
      {
        fInternalSubset.append("SYSTEM '");
        fInternalSubset.append(str2);
      }
      fInternalSubset.append("' NDATA ");
      fInternalSubset.append(paramString2);
      fInternalSubset.append(">\n");
    }
    if (fDocumentType != null)
    {
      NamedNodeMap localNamedNodeMap = fDocumentType.getEntities();
      EntityImpl localEntityImpl = (EntityImpl)localNamedNodeMap.getNamedItem(paramString1);
      if (localEntityImpl == null)
      {
        localEntityImpl = (EntityImpl)fDocumentImpl.createEntity(paramString1);
        localEntityImpl.setPublicId(str1);
        localEntityImpl.setSystemId(str2);
        localEntityImpl.setNotationName(paramString2);
        localEntityImpl.setBaseURI(paramXMLResourceIdentifier.getBaseSystemId());
        localNamedNodeMap.setNamedItem(localEntityImpl);
      }
    }
    if (fDocumentTypeIndex != -1)
    {
      int i = 0;
      int k;
      for (int j = fDeferredDocumentImpl.getLastChild(fDocumentTypeIndex, false); j != -1; j = fDeferredDocumentImpl.getRealPrevSibling(j, false))
      {
        k = fDeferredDocumentImpl.getNodeType(j, false);
        if (k == 6)
        {
          String str3 = fDeferredDocumentImpl.getNodeName(j, false);
          if (str3.equals(paramString1))
          {
            i = 1;
            break;
          }
        }
      }
      if (i == 0)
      {
        k = fDeferredDocumentImpl.createDeferredEntity(paramString1, str1, str2, paramString2, paramXMLResourceIdentifier.getBaseSystemId());
        fDeferredDocumentImpl.appendChild(fDocumentTypeIndex, k);
      }
    }
  }
  
  public void notationDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    String str1 = paramXMLResourceIdentifier.getPublicId();
    String str2 = paramXMLResourceIdentifier.getLiteralSystemId();
    if ((fInternalSubset != null) && (!fInDTDExternalSubset))
    {
      fInternalSubset.append("<!NOTATION ");
      fInternalSubset.append(paramString);
      if (str1 != null)
      {
        fInternalSubset.append(" PUBLIC '");
        fInternalSubset.append(str1);
        if (str2 != null)
        {
          fInternalSubset.append("' '");
          fInternalSubset.append(str2);
        }
      }
      else
      {
        fInternalSubset.append(" SYSTEM '");
        fInternalSubset.append(str2);
      }
      fInternalSubset.append("'>\n");
    }
    if ((fDocumentImpl != null) && (fDocumentType != null))
    {
      NamedNodeMap localNamedNodeMap = fDocumentType.getNotations();
      if (localNamedNodeMap.getNamedItem(paramString) == null)
      {
        NotationImpl localNotationImpl = (NotationImpl)fDocumentImpl.createNotation(paramString);
        localNotationImpl.setPublicId(str1);
        localNotationImpl.setSystemId(str2);
        localNotationImpl.setBaseURI(paramXMLResourceIdentifier.getBaseSystemId());
        localNamedNodeMap.setNamedItem(localNotationImpl);
      }
    }
    if (fDocumentTypeIndex != -1)
    {
      int i = 0;
      int k;
      for (int j = fDeferredDocumentImpl.getLastChild(fDocumentTypeIndex, false); j != -1; j = fDeferredDocumentImpl.getPrevSibling(j, false))
      {
        k = fDeferredDocumentImpl.getNodeType(j, false);
        if (k == 12)
        {
          String str3 = fDeferredDocumentImpl.getNodeName(j, false);
          if (str3.equals(paramString))
          {
            i = 1;
            break;
          }
        }
      }
      if (i == 0)
      {
        k = fDeferredDocumentImpl.createDeferredNotation(paramString, str1, str2, paramXMLResourceIdentifier.getBaseSystemId());
        fDeferredDocumentImpl.appendChild(fDocumentTypeIndex, k);
      }
    }
  }
  
  public void ignoredCharacters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void elementDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    if ((fInternalSubset != null) && (!fInDTDExternalSubset))
    {
      fInternalSubset.append("<!ELEMENT ");
      fInternalSubset.append(paramString1);
      fInternalSubset.append(' ');
      fInternalSubset.append(paramString2);
      fInternalSubset.append(">\n");
    }
  }
  
  public void attributeDecl(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString, String paramString4, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
    throws XNIException
  {
    int i;
    if ((fInternalSubset != null) && (!fInDTDExternalSubset))
    {
      fInternalSubset.append("<!ATTLIST ");
      fInternalSubset.append(paramString1);
      fInternalSubset.append(' ');
      fInternalSubset.append(paramString2);
      fInternalSubset.append(' ');
      if (paramString3.equals("ENUMERATION"))
      {
        fInternalSubset.append('(');
        for (i = 0; i < paramArrayOfString.length; i++)
        {
          if (i > 0) {
            fInternalSubset.append('|');
          }
          fInternalSubset.append(paramArrayOfString[i]);
        }
        fInternalSubset.append(')');
      }
      else
      {
        fInternalSubset.append(paramString3);
      }
      if (paramString4 != null)
      {
        fInternalSubset.append(' ');
        fInternalSubset.append(paramString4);
      }
      if (paramXMLString1 != null)
      {
        fInternalSubset.append(" '");
        for (i = 0; i < length; i++)
        {
          char c = ch[(offset + i)];
          if (c == '\'') {
            fInternalSubset.append("&apos;");
          } else {
            fInternalSubset.append(c);
          }
        }
        fInternalSubset.append('\'');
      }
      fInternalSubset.append(">\n");
    }
    if (fDeferredDocumentImpl != null)
    {
      if (paramXMLString1 != null)
      {
        i = fDeferredDocumentImpl.lookupElementDefinition(paramString1);
        if (i == -1)
        {
          i = fDeferredDocumentImpl.createDeferredElementDefinition(paramString1);
          fDeferredDocumentImpl.appendChild(fDocumentTypeIndex, i);
        }
        boolean bool1 = fNamespaceAware;
        String str1 = null;
        if (bool1) {
          if ((paramString2.startsWith("xmlns:")) || (paramString2.equals("xmlns"))) {
            str1 = NamespaceContext.XMLNS_URI;
          } else if (paramString2.startsWith("xml:")) {
            str1 = NamespaceContext.XML_URI;
          }
        }
        int j = fDeferredDocumentImpl.createDeferredAttribute(paramString2, str1, paramXMLString1.toString(), false);
        if ("ID".equals(paramString3)) {
          fDeferredDocumentImpl.setIdAttribute(j);
        }
        fDeferredDocumentImpl.appendChild(i, j);
      }
    }
    else if ((fDocumentImpl != null) && (paramXMLString1 != null))
    {
      NamedNodeMap localNamedNodeMap = ((DocumentTypeImpl)fDocumentType).getElements();
      ElementDefinitionImpl localElementDefinitionImpl = (ElementDefinitionImpl)localNamedNodeMap.getNamedItem(paramString1);
      if (localElementDefinitionImpl == null)
      {
        localElementDefinitionImpl = fDocumentImpl.createElementDefinition(paramString1);
        ((DocumentTypeImpl)fDocumentType).getElements().setNamedItem(localElementDefinitionImpl);
      }
      boolean bool2 = fNamespaceAware;
      AttrImpl localAttrImpl;
      if (bool2)
      {
        String str2 = null;
        if ((paramString2.startsWith("xmlns:")) || (paramString2.equals("xmlns"))) {
          str2 = NamespaceContext.XMLNS_URI;
        } else if (paramString2.startsWith("xml:")) {
          str2 = NamespaceContext.XML_URI;
        }
        localAttrImpl = (AttrImpl)fDocumentImpl.createAttributeNS(str2, paramString2);
      }
      else
      {
        localAttrImpl = (AttrImpl)fDocumentImpl.createAttribute(paramString2);
      }
      localAttrImpl.setValue(paramXMLString1.toString());
      localAttrImpl.setSpecified(false);
      localAttrImpl.setIdAttribute("ID".equals(paramString3));
      if (bool2) {
        localElementDefinitionImpl.getAttributes().setNamedItemNS(localAttrImpl);
      } else {
        localElementDefinitionImpl.getAttributes().setNamedItem(localAttrImpl);
      }
    }
  }
  
  public void startAttlist(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void endAttlist(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  protected Element createElementNode(QName paramQName)
  {
    Element localElement = null;
    if (fNamespaceAware)
    {
      if (fDocumentImpl != null) {
        localElement = fDocumentImpl.createElementNS(uri, rawname, localpart);
      } else {
        localElement = fDocument.createElementNS(uri, rawname);
      }
    }
    else {
      localElement = fDocument.createElement(rawname);
    }
    return localElement;
  }
  
  protected Attr createAttrNode(QName paramQName)
  {
    Attr localAttr = null;
    if (fNamespaceAware)
    {
      if (fDocumentImpl != null) {
        localAttr = fDocumentImpl.createAttributeNS(uri, rawname, localpart);
      } else {
        localAttr = fDocument.createAttributeNS(uri, rawname);
      }
    }
    else {
      localAttr = fDocument.createAttribute(rawname);
    }
    return localAttr;
  }
  
  protected void setCharacterData(boolean paramBoolean)
  {
    fFirstChunk = paramBoolean;
    Node localNode = fCurrentNode.getLastChild();
    if (localNode != null)
    {
      if (fStringBuffer.length() > 0)
      {
        if (localNode.getNodeType() == 3) {
          if (fDocumentImpl != null) {
            ((TextImpl)localNode).replaceData(fStringBuffer.toString());
          } else {
            ((Text)localNode).setData(fStringBuffer.toString());
          }
        }
        fStringBuffer.setLength(0);
      }
      if ((fDOMFilter != null) && (!fInEntityRef) && (localNode.getNodeType() == 3) && ((fDOMFilter.getWhatToShow() & 0x4) != 0))
      {
        int i = fDOMFilter.acceptNode(localNode);
        switch (i)
        {
        case 4: 
          throw Abort.INSTANCE;
        case 2: 
        case 3: 
          fCurrentNode.removeChild(localNode);
          return;
        }
      }
    }
  }
  
  public void abort()
  {
    throw Abort.INSTANCE;
  }
  
  static final class Abort
    extends RuntimeException
  {
    private static final long serialVersionUID = 1687848994976808490L;
    static final Abort INSTANCE = new Abort();
    
    private Abort() {}
    
    public Throwable fillInStackTrace()
    {
      return this;
    }
  }
}
