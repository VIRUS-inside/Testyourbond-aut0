package org.apache.xerces.dom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.RevalidationHandler;
import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.impl.dtd.XMLDTDValidator;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLDocumentSource;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public class DOMNormalizer
  implements XMLDocumentHandler
{
  protected static final boolean DEBUG_ND = false;
  protected static final boolean DEBUG = false;
  protected static final boolean DEBUG_EVENTS = false;
  protected static final String PREFIX = "NS";
  protected DOMConfigurationImpl fConfiguration = null;
  protected CoreDocumentImpl fDocument = null;
  protected final XMLAttributesProxy fAttrProxy = new XMLAttributesProxy();
  protected final QName fQName = new QName();
  protected RevalidationHandler fValidationHandler;
  protected SymbolTable fSymbolTable;
  protected DOMErrorHandler fErrorHandler;
  private final DOMErrorImpl fError = new DOMErrorImpl();
  protected boolean fNamespaceValidation = false;
  protected boolean fPSVI = false;
  protected final NamespaceContext fNamespaceContext = new NamespaceSupport();
  protected final NamespaceContext fLocalNSBinder = new NamespaceSupport();
  protected final ArrayList fAttributeList = new ArrayList(5);
  protected final DOMLocatorImpl fLocator = new DOMLocatorImpl();
  protected Node fCurrentNode = null;
  private final QName fAttrQName = new QName();
  final XMLString fNormalizedValue = new XMLString(new char[16], 0, 0);
  public static final RuntimeException abort = new RuntimeException();
  public static final XMLString EMPTY_STRING = new XMLString();
  private boolean fAllWhitespace = false;
  
  public DOMNormalizer() {}
  
  protected void normalizeDocument(CoreDocumentImpl paramCoreDocumentImpl, DOMConfigurationImpl paramDOMConfigurationImpl)
  {
    fDocument = paramCoreDocumentImpl;
    fConfiguration = paramDOMConfigurationImpl;
    fAllWhitespace = false;
    fNamespaceValidation = false;
    String str1 = fDocument.getXmlVersion();
    String str2 = null;
    String[] arrayOfString = null;
    fSymbolTable = ((SymbolTable)fConfiguration.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
    fNamespaceContext.reset();
    fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, null);
    Object localObject1;
    if ((fConfiguration.features & 0x40) != 0)
    {
      localObject1 = (String)fConfiguration.getProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage");
      if ((localObject1 != null) && (((String)localObject1).equals(Constants.NS_XMLSCHEMA)))
      {
        str2 = "http://www.w3.org/2001/XMLSchema";
        fValidationHandler = CoreDOMImplementationImpl.singleton.getValidator(str2, str1);
        fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", true);
        fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
        fNamespaceValidation = true;
        fPSVI = ((fConfiguration.features & 0x80) != 0);
      }
      else
      {
        str2 = "http://www.w3.org/TR/REC-xml";
        if (localObject1 != null) {
          arrayOfString = (String[])fConfiguration.getProperty("http://java.sun.com/xml/jaxp/properties/schemaSource");
        }
        fConfiguration.setDTDValidatorFactory(str1);
        fValidationHandler = CoreDOMImplementationImpl.singleton.getValidator(str2, str1);
        fPSVI = false;
      }
      fConfiguration.setFeature("http://xml.org/sax/features/validation", true);
      fDocument.clearIdentifiers();
      if (fValidationHandler != null) {
        ((XMLComponent)fValidationHandler).reset(fConfiguration);
      }
    }
    else
    {
      fValidationHandler = null;
    }
    fErrorHandler = ((DOMErrorHandler)fConfiguration.getParameter("error-handler"));
    if (fValidationHandler != null)
    {
      fValidationHandler.setDocumentHandler(this);
      fValidationHandler.startDocument(new SimpleLocator(fDocument.fDocumentURI, fDocument.fDocumentURI, -1, -1), fDocument.encoding, fNamespaceContext, null);
      fValidationHandler.xmlDecl(fDocument.getXmlVersion(), fDocument.getXmlEncoding(), fDocument.getXmlStandalone() ? "yes" : "no", null);
    }
    try
    {
      if (str2 == "http://www.w3.org/TR/REC-xml") {
        processDTD(str1, arrayOfString != null ? arrayOfString[0] : null);
      }
      Object localObject2;
      for (localObject1 = fDocument.getFirstChild(); localObject1 != null; localObject1 = localObject2)
      {
        localObject2 = ((Node)localObject1).getNextSibling();
        localObject1 = normalizeNode((Node)localObject1);
        if (localObject1 != null) {
          localObject2 = localObject1;
        }
      }
      if (fValidationHandler != null)
      {
        fValidationHandler.endDocument(null);
        fValidationHandler.setDocumentHandler(null);
        CoreDOMImplementationImpl.singleton.releaseValidator(str2, str1, fValidationHandler);
        fValidationHandler = null;
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      if (fValidationHandler != null)
      {
        fValidationHandler.setDocumentHandler(null);
        CoreDOMImplementationImpl.singleton.releaseValidator(str2, str1, fValidationHandler);
        fValidationHandler = null;
      }
      if (localRuntimeException == abort) {
        return;
      }
      throw localRuntimeException;
    }
  }
  
  protected Node normalizeNode(Node paramNode)
  {
    int i = paramNode.getNodeType();
    fLocator.fRelatedNode = paramNode;
    boolean bool;
    Object localObject1;
    Object localObject2;
    Object localObject4;
    Object localObject5;
    Object localObject3;
    int j;
    switch (i)
    {
    case 10: 
      break;
    case 1: 
      if ((fDocument.errorChecking) && ((fConfiguration.features & 0x100) != 0) && (fDocument.isXMLVersionChanged()))
      {
        if (fNamespaceValidation) {
          bool = CoreDocumentImpl.isValidQName(paramNode.getPrefix(), paramNode.getLocalName(), fDocument.isXML11Version());
        } else {
          bool = CoreDocumentImpl.isXMLName(paramNode.getNodeName(), fDocument.isXML11Version());
        }
        if (!bool)
        {
          localObject1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[] { "Element", paramNode.getNodeName() });
          reportDOMError(fErrorHandler, fError, fLocator, (String)localObject1, (short)2, "wf-invalid-character-in-node-name");
        }
      }
      fNamespaceContext.pushContext();
      fLocalNSBinder.reset();
      localObject1 = (ElementImpl)paramNode;
      if (((ElementImpl)localObject1).needsSyncChildren()) {
        ((ElementImpl)localObject1).synchronizeChildren();
      }
      localObject2 = ((ElementImpl)localObject1).hasAttributes() ? (AttributeMap)((ElementImpl)localObject1).getAttributes() : null;
      int k;
      if ((fConfiguration.features & 0x1) != 0)
      {
        namespaceFixUp((ElementImpl)localObject1, (AttributeMap)localObject2);
        if (((fConfiguration.features & 0x200) == 0) && (localObject2 != null)) {
          for (k = 0; k < ((AttributeMap)localObject2).getLength(); k++)
          {
            localObject4 = (Attr)((AttributeMap)localObject2).getItem(k);
            if ((XMLSymbols.PREFIX_XMLNS.equals(((Attr)localObject4).getPrefix())) || (XMLSymbols.PREFIX_XMLNS.equals(((Attr)localObject4).getName())))
            {
              ((ElementImpl)localObject1).removeAttributeNode((Attr)localObject4);
              k--;
            }
          }
        }
      }
      else if (localObject2 != null)
      {
        for (k = 0; k < ((AttributeMap)localObject2).getLength(); k++)
        {
          localObject4 = (Attr)((AttributeMap)localObject2).item(k);
          ((Attr)localObject4).normalize();
          if ((fDocument.errorChecking) && ((fConfiguration.features & 0x100) != 0))
          {
            isAttrValueWF(fErrorHandler, fError, fLocator, (NamedNodeMap)localObject2, (Attr)localObject4, ((Attr)localObject4).getValue(), fDocument.isXML11Version());
            if (fDocument.isXMLVersionChanged())
            {
              if (fNamespaceValidation) {
                bool = CoreDocumentImpl.isValidQName(paramNode.getPrefix(), paramNode.getLocalName(), fDocument.isXML11Version());
              } else {
                bool = CoreDocumentImpl.isXMLName(paramNode.getNodeName(), fDocument.isXML11Version());
              }
              if (!bool)
              {
                localObject5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[] { "Attr", paramNode.getNodeName() });
                reportDOMError(fErrorHandler, fError, fLocator, (String)localObject5, (short)2, "wf-invalid-character-in-node-name");
              }
            }
          }
        }
      }
      if (fValidationHandler != null)
      {
        fAttrProxy.setAttributes((AttributeMap)localObject2, fDocument, (ElementImpl)localObject1);
        updateQName((Node)localObject1, fQName);
        fConfiguration.fErrorHandlerWrapper.fCurrentNode = paramNode;
        fCurrentNode = paramNode;
        fValidationHandler.startElement(fQName, fAttrProxy, null);
      }
      for (localObject3 = ((ElementImpl)localObject1).getFirstChild(); localObject3 != null; localObject3 = localObject4)
      {
        localObject4 = ((Node)localObject3).getNextSibling();
        localObject3 = normalizeNode((Node)localObject3);
        if (localObject3 != null) {
          localObject4 = localObject3;
        }
      }
      if (fValidationHandler != null)
      {
        updateQName((Node)localObject1, fQName);
        fConfiguration.fErrorHandlerWrapper.fCurrentNode = paramNode;
        fCurrentNode = paramNode;
        fValidationHandler.endElement(fQName, null);
      }
      fNamespaceContext.popContext();
      break;
    case 8: 
      if ((fConfiguration.features & 0x20) == 0)
      {
        localObject1 = paramNode.getPreviousSibling();
        localObject2 = paramNode.getParentNode();
        ((Node)localObject2).removeChild(paramNode);
        if ((localObject1 != null) && (((Node)localObject1).getNodeType() == 3))
        {
          localObject3 = ((Node)localObject1).getNextSibling();
          if ((localObject3 != null) && (((Node)localObject3).getNodeType() == 3))
          {
            ((TextImpl)localObject3).insertData(0, ((Node)localObject1).getNodeValue());
            ((Node)localObject2).removeChild((Node)localObject1);
            return localObject3;
          }
        }
      }
      else
      {
        if ((fDocument.errorChecking) && ((fConfiguration.features & 0x100) != 0))
        {
          localObject1 = ((Comment)paramNode).getData();
          isCommentWF(fErrorHandler, fError, fLocator, (String)localObject1, fDocument.isXML11Version());
        }
        if (fValidationHandler != null) {
          fValidationHandler.comment(EMPTY_STRING, null);
        }
      }
      break;
    case 5: 
      if ((fConfiguration.features & 0x4) == 0)
      {
        localObject1 = paramNode.getPreviousSibling();
        localObject2 = paramNode.getParentNode();
        ((EntityReferenceImpl)paramNode).setReadOnly(false, true);
        expandEntityRef((Node)localObject2, paramNode);
        ((Node)localObject2).removeChild(paramNode);
        localObject3 = localObject1 != null ? ((Node)localObject1).getNextSibling() : ((Node)localObject2).getFirstChild();
        if ((localObject1 != null) && (localObject3 != null) && (((Node)localObject1).getNodeType() == 3) && (((Node)localObject3).getNodeType() == 3)) {
          return localObject1;
        }
        return localObject3;
      }
      if ((fDocument.errorChecking) && ((fConfiguration.features & 0x100) != 0) && (fDocument.isXMLVersionChanged())) {
        CoreDocumentImpl.isXMLName(paramNode.getNodeName(), fDocument.isXML11Version());
      }
      break;
    case 4: 
      if ((fConfiguration.features & 0x8) == 0)
      {
        localObject1 = paramNode.getPreviousSibling();
        if ((localObject1 != null) && (((Node)localObject1).getNodeType() == 3))
        {
          ((Text)localObject1).appendData(paramNode.getNodeValue());
          paramNode.getParentNode().removeChild(paramNode);
          return localObject1;
        }
        localObject2 = fDocument.createTextNode(paramNode.getNodeValue());
        localObject3 = paramNode.getParentNode();
        paramNode = ((Node)localObject3).replaceChild((Node)localObject2, paramNode);
        return localObject2;
      }
      if (fValidationHandler != null)
      {
        fConfiguration.fErrorHandlerWrapper.fCurrentNode = paramNode;
        fCurrentNode = paramNode;
        fValidationHandler.startCDATA(null);
        fValidationHandler.characterData(paramNode.getNodeValue(), null);
        fValidationHandler.endCDATA(null);
      }
      localObject1 = paramNode.getNodeValue();
      if ((fConfiguration.features & 0x10) != 0)
      {
        localObject3 = paramNode.getParentNode();
        if (fDocument.errorChecking) {
          isXMLCharWF(fErrorHandler, fError, fLocator, paramNode.getNodeValue(), fDocument.isXML11Version());
        }
        while ((j = ((String)localObject1).indexOf("]]>")) >= 0)
        {
          paramNode.setNodeValue(((String)localObject1).substring(0, localObject2 + 2));
          localObject1 = ((String)localObject1).substring(localObject2 + 2);
          localObject4 = paramNode;
          localObject5 = fDocument.createCDATASection((String)localObject1);
          ((Node)localObject3).insertBefore((Node)localObject5, paramNode.getNextSibling());
          paramNode = (Node)localObject5;
          fLocator.fRelatedNode = ((Node)localObject4);
          String str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "cdata-sections-splitted", null);
          reportDOMError(fErrorHandler, fError, fLocator, str2, (short)1, "cdata-sections-splitted");
        }
      }
      else if (fDocument.errorChecking)
      {
        isCDataWF(fErrorHandler, fError, fLocator, (String)localObject1, fDocument.isXML11Version());
      }
      break;
    case 3: 
      localObject1 = paramNode.getNextSibling();
      if ((localObject1 != null) && (((Node)localObject1).getNodeType() == 3))
      {
        ((Text)paramNode).appendData(((Node)localObject1).getNodeValue());
        paramNode.getParentNode().removeChild((Node)localObject1);
        return paramNode;
      }
      if (paramNode.getNodeValue().length() == 0)
      {
        paramNode.getParentNode().removeChild(paramNode);
      }
      else
      {
        j = localObject1 != null ? ((Node)localObject1).getNodeType() : -1;
        if ((j == -1) || ((((fConfiguration.features & 0x4) != 0) || (j != 6)) && (((fConfiguration.features & 0x20) != 0) || (j != 8)) && (((fConfiguration.features & 0x8) != 0) || (j != 4))))
        {
          if ((fDocument.errorChecking) && ((fConfiguration.features & 0x100) != 0)) {
            isXMLCharWF(fErrorHandler, fError, fLocator, paramNode.getNodeValue(), fDocument.isXML11Version());
          }
          if (fValidationHandler != null)
          {
            fConfiguration.fErrorHandlerWrapper.fCurrentNode = paramNode;
            fCurrentNode = paramNode;
            fValidationHandler.characterData(paramNode.getNodeValue(), null);
            if (!fNamespaceValidation) {
              if (fAllWhitespace)
              {
                fAllWhitespace = false;
                ((TextImpl)paramNode).setIgnorableWhitespace(true);
              }
              else
              {
                ((TextImpl)paramNode).setIgnorableWhitespace(false);
              }
            }
          }
        }
      }
      break;
    case 7: 
      if ((fDocument.errorChecking) && ((fConfiguration.features & 0x100) != 0))
      {
        localObject1 = (ProcessingInstruction)paramNode;
        String str1 = ((ProcessingInstruction)localObject1).getTarget();
        if (fDocument.isXML11Version()) {
          bool = XML11Char.isXML11ValidName(str1);
        } else {
          bool = XMLChar.isValidName(str1);
        }
        if (!bool)
        {
          localObject3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[] { "Element", paramNode.getNodeName() });
          reportDOMError(fErrorHandler, fError, fLocator, (String)localObject3, (short)2, "wf-invalid-character-in-node-name");
        }
        isXMLCharWF(fErrorHandler, fError, fLocator, ((ProcessingInstruction)localObject1).getData(), fDocument.isXML11Version());
      }
      if (fValidationHandler != null) {
        fValidationHandler.processingInstruction(((ProcessingInstruction)paramNode).getTarget(), EMPTY_STRING, null);
      }
      break;
    }
    return null;
  }
  
  private void processDTD(String paramString1, String paramString2)
  {
    String str1 = null;
    String str2 = null;
    String str3 = paramString2;
    String str4 = fDocument.getDocumentURI();
    String str5 = null;
    DocumentType localDocumentType = fDocument.getDoctype();
    if (localDocumentType != null)
    {
      str1 = localDocumentType.getName();
      str2 = localDocumentType.getPublicId();
      if ((str3 == null) || (str3.length() == 0)) {
        str3 = localDocumentType.getSystemId();
      }
      str5 = localDocumentType.getInternalSubset();
    }
    else
    {
      localObject1 = fDocument.getDocumentElement();
      if (localObject1 == null) {
        return;
      }
      str1 = ((Element)localObject1).getNodeName();
      if ((str3 == null) || (str3.length() == 0)) {
        return;
      }
    }
    Object localObject1 = null;
    try
    {
      fValidationHandler.doctypeDecl(str1, str2, str3, null);
      localObject1 = CoreDOMImplementationImpl.singleton.getDTDLoader(paramString1);
      ((XMLDTDLoader)localObject1).setFeature("http://xml.org/sax/features/validation", true);
      ((XMLDTDLoader)localObject1).setEntityResolver(fConfiguration.getEntityResolver());
      ((XMLDTDLoader)localObject1).setErrorHandler(fConfiguration.getErrorHandler());
      ((XMLDTDLoader)localObject1).loadGrammarWithContext((XMLDTDValidator)fValidationHandler, str1, str2, str3, str4, str5);
    }
    catch (IOException localIOException) {}finally
    {
      if (localObject1 != null) {
        CoreDOMImplementationImpl.singleton.releaseDTDLoader(paramString1, (XMLDTDLoader)localObject1);
      }
    }
  }
  
  protected final void expandEntityRef(Node paramNode1, Node paramNode2)
  {
    Node localNode;
    for (Object localObject = paramNode2.getFirstChild(); localObject != null; localObject = localNode)
    {
      localNode = ((Node)localObject).getNextSibling();
      paramNode1.insertBefore((Node)localObject, paramNode2);
    }
  }
  
  protected final void namespaceFixUp(ElementImpl paramElementImpl, AttributeMap paramAttributeMap)
  {
    Attr localAttr;
    String str1;
    if (paramAttributeMap != null) {
      for (int i = 0; i < paramAttributeMap.getLength(); i++)
      {
        localAttr = (Attr)paramAttributeMap.getItem(i);
        str2 = localAttr.getNamespaceURI();
        if ((str2 != null) && (str2.equals(NamespaceContext.XMLNS_URI)))
        {
          str1 = localAttr.getNodeValue();
          if (str1 == null) {
            str1 = XMLSymbols.EMPTY_STRING;
          }
          String str4;
          if ((fDocument.errorChecking) && (str1.equals(NamespaceContext.XMLNS_URI)))
          {
            fLocator.fRelatedNode = localAttr;
            str4 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CantBindXMLNS", null);
            reportDOMError(fErrorHandler, fError, fLocator, str4, (short)2, "CantBindXMLNS");
          }
          else
          {
            localObject = localAttr.getPrefix();
            localObject = (localObject == null) || (((String)localObject).length() == 0) ? XMLSymbols.EMPTY_STRING : fSymbolTable.addSymbol((String)localObject);
            str4 = fSymbolTable.addSymbol(localAttr.getLocalName());
            if (localObject == XMLSymbols.PREFIX_XMLNS)
            {
              str1 = fSymbolTable.addSymbol(str1);
              if (str1.length() != 0) {
                fNamespaceContext.declarePrefix(str4, str1);
              }
            }
            else
            {
              str1 = fSymbolTable.addSymbol(str1);
              fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, str1.length() != 0 ? str1 : null);
            }
          }
        }
      }
    }
    String str2 = paramElementImpl.getNamespaceURI();
    Object localObject = paramElementImpl.getPrefix();
    if (str2 != null)
    {
      str2 = fSymbolTable.addSymbol(str2);
      localObject = (localObject == null) || (((String)localObject).length() == 0) ? XMLSymbols.EMPTY_STRING : fSymbolTable.addSymbol((String)localObject);
      if (fNamespaceContext.getURI((String)localObject) != str2)
      {
        addNamespaceDecl((String)localObject, str2, paramElementImpl);
        fLocalNSBinder.declarePrefix((String)localObject, str2);
        fNamespaceContext.declarePrefix((String)localObject, str2);
      }
    }
    else if (paramElementImpl.getLocalName() == null)
    {
      String str3;
      if (fNamespaceValidation)
      {
        str3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalElementName", new Object[] { paramElementImpl.getNodeName() });
        reportDOMError(fErrorHandler, fError, fLocator, str3, (short)3, "NullLocalElementName");
      }
      else
      {
        str3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalElementName", new Object[] { paramElementImpl.getNodeName() });
        reportDOMError(fErrorHandler, fError, fLocator, str3, (short)2, "NullLocalElementName");
      }
    }
    else
    {
      str2 = fNamespaceContext.getURI(XMLSymbols.EMPTY_STRING);
      if ((str2 != null) && (str2.length() > 0))
      {
        addNamespaceDecl(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING, paramElementImpl);
        fLocalNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, null);
        fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, null);
      }
    }
    if (paramAttributeMap != null)
    {
      paramAttributeMap.cloneMap(fAttributeList);
      for (int j = 0; j < fAttributeList.size(); j++)
      {
        localAttr = (Attr)fAttributeList.get(j);
        fLocator.fRelatedNode = localAttr;
        localAttr.normalize();
        str1 = localAttr.getValue();
        str2 = localAttr.getNamespaceURI();
        if (str1 == null) {
          str1 = XMLSymbols.EMPTY_STRING;
        }
        String str6;
        if ((fDocument.errorChecking) && ((fConfiguration.features & 0x100) != 0))
        {
          isAttrValueWF(fErrorHandler, fError, fLocator, paramAttributeMap, localAttr, str1, fDocument.isXML11Version());
          if (fDocument.isXMLVersionChanged())
          {
            boolean bool;
            if (fNamespaceValidation) {
              bool = CoreDocumentImpl.isValidQName(localAttr.getPrefix(), localAttr.getLocalName(), fDocument.isXML11Version());
            } else {
              bool = CoreDocumentImpl.isXMLName(localAttr.getNodeName(), fDocument.isXML11Version());
            }
            if (!bool)
            {
              str6 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[] { "Attr", localAttr.getNodeName() });
              reportDOMError(fErrorHandler, fError, fLocator, str6, (short)2, "wf-invalid-character-in-node-name");
            }
          }
        }
        String str5;
        if (str2 != null)
        {
          localObject = localAttr.getPrefix();
          localObject = (localObject == null) || (((String)localObject).length() == 0) ? XMLSymbols.EMPTY_STRING : fSymbolTable.addSymbol((String)localObject);
          fSymbolTable.addSymbol(localAttr.getLocalName());
          if ((str2 == null) || (!str2.equals(NamespaceContext.XMLNS_URI)))
          {
            ((AttrImpl)localAttr).setIdAttribute(false);
            str2 = fSymbolTable.addSymbol(str2);
            str5 = fNamespaceContext.getURI((String)localObject);
            if ((localObject == XMLSymbols.EMPTY_STRING) || (str5 != str2))
            {
              str6 = fNamespaceContext.getPrefix(str2);
              if ((str6 != null) && (str6 != XMLSymbols.EMPTY_STRING))
              {
                localObject = str6;
              }
              else
              {
                if ((localObject == XMLSymbols.EMPTY_STRING) || (fLocalNSBinder.getURI((String)localObject) != null))
                {
                  int k = 1;
                  for (localObject = fSymbolTable.addSymbol("NS" + k++); fLocalNSBinder.getURI((String)localObject) != null; localObject = fSymbolTable.addSymbol("NS" + k++)) {}
                }
                addNamespaceDecl((String)localObject, str2, paramElementImpl);
                str1 = fSymbolTable.addSymbol(str1);
                fLocalNSBinder.declarePrefix((String)localObject, str1);
                fNamespaceContext.declarePrefix((String)localObject, str2);
              }
              localAttr.setPrefix((String)localObject);
            }
          }
        }
        else
        {
          ((AttrImpl)localAttr).setIdAttribute(false);
          if (localAttr.getLocalName() == null) {
            if (fNamespaceValidation)
            {
              str5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalAttrName", new Object[] { localAttr.getNodeName() });
              reportDOMError(fErrorHandler, fError, fLocator, str5, (short)3, "NullLocalAttrName");
            }
            else
            {
              str5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NullLocalAttrName", new Object[] { localAttr.getNodeName() });
              reportDOMError(fErrorHandler, fError, fLocator, str5, (short)2, "NullLocalAttrName");
            }
          }
        }
      }
    }
  }
  
  protected final void addNamespaceDecl(String paramString1, String paramString2, ElementImpl paramElementImpl)
  {
    if (paramString1 == XMLSymbols.EMPTY_STRING) {
      paramElementImpl.setAttributeNS(NamespaceContext.XMLNS_URI, XMLSymbols.PREFIX_XMLNS, paramString2);
    } else {
      paramElementImpl.setAttributeNS(NamespaceContext.XMLNS_URI, "xmlns:" + paramString1, paramString2);
    }
  }
  
  public static final void isCDataWF(DOMErrorHandler paramDOMErrorHandler, DOMErrorImpl paramDOMErrorImpl, DOMLocatorImpl paramDOMLocatorImpl, String paramString, boolean paramBoolean)
  {
    if ((paramString == null) || (paramString.length() == 0)) {
      return;
    }
    char[] arrayOfChar = paramString.toCharArray();
    String str1 = arrayOfChar.length;
    String str2;
    char c1;
    String str5;
    if (paramBoolean)
    {
      str2 = 0;
      while (str2 < str1)
      {
        c1 = arrayOfChar[(str2++)];
        String str3;
        if (XML11Char.isXML11Invalid(c1))
        {
          if ((XMLChar.isHighSurrogate(c1)) && (str2 < str1))
          {
            char c2 = arrayOfChar[(str2++)];
            if ((XMLChar.isLowSurrogate(c2)) && (XMLChar.isSupplemental(XMLChar.supplemental(c1, c2)))) {}
          }
          else
          {
            str3 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInCDSect", new Object[] { Integer.toString(c1, 16) });
            reportDOMError(paramDOMErrorHandler, paramDOMErrorImpl, paramDOMLocatorImpl, str3, (short)2, "wf-invalid-character");
          }
        }
        else if (c1 == ']')
        {
          str3 = str2;
          if ((str3 < str1) && (arrayOfChar[str3] == ']'))
          {
            do
            {
              str3++;
            } while ((str3 < str1) && (arrayOfChar[str3] == ']'));
            if ((str3 < str1) && (arrayOfChar[str3] == '>'))
            {
              str5 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CDEndInContent", null);
              reportDOMError(paramDOMErrorHandler, paramDOMErrorImpl, paramDOMLocatorImpl, str5, (short)2, "wf-invalid-character");
            }
          }
        }
      }
    }
    else
    {
      str2 = 0;
      while (str2 < str1)
      {
        c1 = arrayOfChar[(str2++)];
        String str4;
        if (XMLChar.isInvalid(c1))
        {
          if ((XMLChar.isHighSurrogate(c1)) && (str2 < str1))
          {
            char c3 = arrayOfChar[(str2++)];
            if ((XMLChar.isLowSurrogate(c3)) && (XMLChar.isSupplemental(XMLChar.supplemental(c1, c3)))) {}
          }
          else
          {
            str4 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInCDSect", new Object[] { Integer.toString(c1, 16) });
            reportDOMError(paramDOMErrorHandler, paramDOMErrorImpl, paramDOMLocatorImpl, str4, (short)2, "wf-invalid-character");
          }
        }
        else if (c1 == ']')
        {
          str4 = str2;
          if ((str4 < str1) && (arrayOfChar[str4] == ']'))
          {
            do
            {
              str4++;
            } while ((str4 < str1) && (arrayOfChar[str4] == ']'));
            if ((str4 < str1) && (arrayOfChar[str4] == '>'))
            {
              str5 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CDEndInContent", null);
              reportDOMError(paramDOMErrorHandler, paramDOMErrorImpl, paramDOMLocatorImpl, str5, (short)2, "wf-invalid-character");
            }
          }
        }
      }
    }
  }
  
  public static final void isXMLCharWF(DOMErrorHandler paramDOMErrorHandler, DOMErrorImpl paramDOMErrorImpl, DOMLocatorImpl paramDOMLocatorImpl, String paramString, boolean paramBoolean)
  {
    if ((paramString == null) || (paramString.length() == 0)) {
      return;
    }
    char[] arrayOfChar = paramString.toCharArray();
    int i = arrayOfChar.length;
    int j;
    char c1;
    if (paramBoolean)
    {
      j = 0;
      while (j < i) {
        if (XML11Char.isXML11Invalid(arrayOfChar[(j++)]))
        {
          c1 = arrayOfChar[(j - 1)];
          if ((XMLChar.isHighSurrogate(c1)) && (j < i))
          {
            char c2 = arrayOfChar[(j++)];
            if ((XMLChar.isLowSurrogate(c2)) && (XMLChar.isSupplemental(XMLChar.supplemental(c1, c2)))) {}
          }
          else
          {
            String str1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "InvalidXMLCharInDOM", new Object[] { Integer.toString(arrayOfChar[(j - 1)], 16) });
            reportDOMError(paramDOMErrorHandler, paramDOMErrorImpl, paramDOMLocatorImpl, str1, (short)2, "wf-invalid-character");
          }
        }
      }
    }
    else
    {
      j = 0;
      while (j < i) {
        if (XMLChar.isInvalid(arrayOfChar[(j++)]))
        {
          c1 = arrayOfChar[(j - 1)];
          if ((XMLChar.isHighSurrogate(c1)) && (j < i))
          {
            char c3 = arrayOfChar[(j++)];
            if ((XMLChar.isLowSurrogate(c3)) && (XMLChar.isSupplemental(XMLChar.supplemental(c1, c3)))) {}
          }
          else
          {
            String str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "InvalidXMLCharInDOM", new Object[] { Integer.toString(arrayOfChar[(j - 1)], 16) });
            reportDOMError(paramDOMErrorHandler, paramDOMErrorImpl, paramDOMLocatorImpl, str2, (short)2, "wf-invalid-character");
          }
        }
      }
    }
  }
  
  public static final void isCommentWF(DOMErrorHandler paramDOMErrorHandler, DOMErrorImpl paramDOMErrorImpl, DOMLocatorImpl paramDOMLocatorImpl, String paramString, boolean paramBoolean)
  {
    if ((paramString == null) || (paramString.length() == 0)) {
      return;
    }
    char[] arrayOfChar = paramString.toCharArray();
    int i = arrayOfChar.length;
    int j;
    char c1;
    if (paramBoolean)
    {
      j = 0;
      while (j < i)
      {
        c1 = arrayOfChar[(j++)];
        String str1;
        if (XML11Char.isXML11Invalid(c1))
        {
          if ((XMLChar.isHighSurrogate(c1)) && (j < i))
          {
            char c2 = arrayOfChar[(j++)];
            if ((XMLChar.isLowSurrogate(c2)) && (XMLChar.isSupplemental(XMLChar.supplemental(c1, c2)))) {}
          }
          else
          {
            str1 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInComment", new Object[] { Integer.toString(arrayOfChar[(j - 1)], 16) });
            reportDOMError(paramDOMErrorHandler, paramDOMErrorImpl, paramDOMLocatorImpl, str1, (short)2, "wf-invalid-character");
          }
        }
        else if ((c1 == '-') && (j < i) && (arrayOfChar[j] == '-'))
        {
          str1 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "DashDashInComment", null);
          reportDOMError(paramDOMErrorHandler, paramDOMErrorImpl, paramDOMLocatorImpl, str1, (short)2, "wf-invalid-character");
        }
      }
    }
    else
    {
      j = 0;
      while (j < i)
      {
        c1 = arrayOfChar[(j++)];
        String str2;
        if (XMLChar.isInvalid(c1))
        {
          if ((XMLChar.isHighSurrogate(c1)) && (j < i))
          {
            char c3 = arrayOfChar[(j++)];
            if ((XMLChar.isLowSurrogate(c3)) && (XMLChar.isSupplemental(XMLChar.supplemental(c1, c3)))) {}
          }
          else
          {
            str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInComment", new Object[] { Integer.toString(arrayOfChar[(j - 1)], 16) });
            reportDOMError(paramDOMErrorHandler, paramDOMErrorImpl, paramDOMLocatorImpl, str2, (short)2, "wf-invalid-character");
          }
        }
        else if ((c1 == '-') && (j < i) && (arrayOfChar[j] == '-'))
        {
          str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "DashDashInComment", null);
          reportDOMError(paramDOMErrorHandler, paramDOMErrorImpl, paramDOMLocatorImpl, str2, (short)2, "wf-invalid-character");
        }
      }
    }
  }
  
  public static final void isAttrValueWF(DOMErrorHandler paramDOMErrorHandler, DOMErrorImpl paramDOMErrorImpl, DOMLocatorImpl paramDOMLocatorImpl, NamedNodeMap paramNamedNodeMap, Attr paramAttr, String paramString, boolean paramBoolean)
  {
    if (((paramAttr instanceof AttrImpl)) && (((AttrImpl)paramAttr).hasStringValue()))
    {
      isXMLCharWF(paramDOMErrorHandler, paramDOMErrorImpl, paramDOMLocatorImpl, paramString, paramBoolean);
    }
    else
    {
      NodeList localNodeList = paramAttr.getChildNodes();
      for (int i = 0; i < localNodeList.getLength(); i++)
      {
        Node localNode = localNodeList.item(i);
        if (localNode.getNodeType() == 5)
        {
          Document localDocument = paramAttr.getOwnerDocument();
          Entity localEntity = null;
          Object localObject;
          if (localDocument != null)
          {
            localObject = localDocument.getDoctype();
            if (localObject != null)
            {
              NamedNodeMap localNamedNodeMap = ((DocumentType)localObject).getEntities();
              localEntity = (Entity)localNamedNodeMap.getNamedItemNS("*", localNode.getNodeName());
            }
          }
          if (localEntity == null)
          {
            localObject = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "UndeclaredEntRefInAttrValue", new Object[] { paramAttr.getNodeName() });
            reportDOMError(paramDOMErrorHandler, paramDOMErrorImpl, paramDOMLocatorImpl, (String)localObject, (short)2, "UndeclaredEntRefInAttrValue");
          }
        }
        else
        {
          isXMLCharWF(paramDOMErrorHandler, paramDOMErrorImpl, paramDOMLocatorImpl, localNode.getNodeValue(), paramBoolean);
        }
      }
    }
  }
  
  public static final void reportDOMError(DOMErrorHandler paramDOMErrorHandler, DOMErrorImpl paramDOMErrorImpl, DOMLocatorImpl paramDOMLocatorImpl, String paramString1, short paramShort, String paramString2)
  {
    if (paramDOMErrorHandler != null)
    {
      paramDOMErrorImpl.reset();
      fMessage = paramString1;
      fSeverity = paramShort;
      fLocator = paramDOMLocatorImpl;
      fType = paramString2;
      fRelatedData = fRelatedNode;
      if (!paramDOMErrorHandler.handleError(paramDOMErrorImpl)) {
        throw abort;
      }
    }
    if (paramShort == 3) {
      throw abort;
    }
  }
  
  protected final void updateQName(Node paramNode, QName paramQName)
  {
    String str1 = paramNode.getPrefix();
    String str2 = paramNode.getNamespaceURI();
    String str3 = paramNode.getLocalName();
    prefix = ((str1 != null) && (str1.length() != 0) ? fSymbolTable.addSymbol(str1) : null);
    localpart = (str3 != null ? fSymbolTable.addSymbol(str3) : null);
    rawname = fSymbolTable.addSymbol(paramNode.getNodeName());
    uri = (str2 != null ? fSymbolTable.addSymbol(str2) : null);
  }
  
  final String normalizeAttributeValue(String paramString, Attr paramAttr)
  {
    if (!paramAttr.getSpecified()) {
      return paramString;
    }
    int i = paramString.length();
    if (fNormalizedValue.ch.length < i) {
      fNormalizedValue.ch = new char[i];
    }
    fNormalizedValue.length = 0;
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      int m = paramString.charAt(k);
      if ((m == 9) || (m == 10))
      {
        fNormalizedValue.ch[(fNormalizedValue.length++)] = ' ';
        j = 1;
      }
      else if (m == 13)
      {
        j = 1;
        fNormalizedValue.ch[(fNormalizedValue.length++)] = ' ';
        int n = k + 1;
        if ((n < i) && (paramString.charAt(n) == '\n')) {
          k = n;
        }
      }
      else
      {
        fNormalizedValue.ch[(fNormalizedValue.length++)] = m;
      }
    }
    if (j != 0)
    {
      paramString = fNormalizedValue.toString();
      paramAttr.setValue(paramString);
    }
    return paramString;
  }
  
  public void startDocument(XMLLocator paramXMLLocator, String paramString, NamespaceContext paramNamespaceContext, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void xmlDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void doctypeDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void startElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    Element localElement = (Element)fCurrentNode;
    int i = paramXMLAttributes.getLength();
    for (int j = 0; j < i; j++)
    {
      paramXMLAttributes.getName(j, fAttrQName);
      Attr localAttr = null;
      localAttr = localElement.getAttributeNodeNS(fAttrQName.uri, fAttrQName.localpart);
      if (localAttr == null) {
        localAttr = localElement.getAttributeNode(fAttrQName.rawname);
      }
      AttributePSVI localAttributePSVI = (AttributePSVI)paramXMLAttributes.getAugmentations(j).getItem("ATTRIBUTE_PSVI");
      Object localObject;
      boolean bool1;
      if (localAttributePSVI != null)
      {
        localObject = localAttributePSVI.getMemberTypeDefinition();
        bool1 = false;
        if (localObject != null)
        {
          bool1 = ((XSSimpleType)localObject).isIDType();
        }
        else
        {
          localObject = localAttributePSVI.getTypeDefinition();
          if (localObject != null) {
            bool1 = ((XSSimpleType)localObject).isIDType();
          }
        }
        if (bool1) {
          ((ElementImpl)localElement).setIdAttributeNode(localAttr, true);
        }
        if (fPSVI) {
          ((PSVIAttrNSImpl)localAttr).setPSVI(localAttributePSVI);
        }
        ((AttrImpl)localAttr).setType(localObject);
        if ((fConfiguration.features & 0x2) != 0)
        {
          String str = localAttributePSVI.getSchemaNormalizedValue();
          if (str != null)
          {
            boolean bool2 = localAttr.getSpecified();
            localAttr.setValue(str);
            if (!bool2) {
              ((AttrImpl)localAttr).setSpecified(bool2);
            }
          }
        }
      }
      else
      {
        localObject = null;
        bool1 = Boolean.TRUE.equals(paramXMLAttributes.getAugmentations(j).getItem("ATTRIBUTE_DECLARED"));
        if (bool1)
        {
          localObject = paramXMLAttributes.getType(j);
          if ("ID".equals(localObject)) {
            ((ElementImpl)localElement).setIdAttributeNode(localAttr, true);
          }
        }
        ((AttrImpl)localAttr).setType(localObject);
      }
    }
  }
  
  public void emptyElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    startElement(paramQName, paramXMLAttributes, paramAugmentations);
    endElement(paramQName, paramAugmentations);
  }
  
  public void startGeneralEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void textDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void endGeneralEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void characters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void ignorableWhitespace(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    fAllWhitespace = true;
  }
  
  public void endElement(QName paramQName, Augmentations paramAugmentations)
    throws XNIException
  {
    if (paramAugmentations != null)
    {
      ElementPSVI localElementPSVI = (ElementPSVI)paramAugmentations.getItem("ELEMENT_PSVI");
      if (localElementPSVI != null)
      {
        ElementImpl localElementImpl = (ElementImpl)fCurrentNode;
        if (fPSVI) {
          ((PSVIElementNSImpl)fCurrentNode).setPSVI(localElementPSVI);
        }
        if ((localElementImpl instanceof ElementNSImpl))
        {
          localObject = localElementPSVI.getMemberTypeDefinition();
          if (localObject == null) {
            localObject = localElementPSVI.getTypeDefinition();
          }
          ((ElementNSImpl)localElementImpl).setType((XSTypeDefinition)localObject);
        }
        Object localObject = localElementPSVI.getSchemaNormalizedValue();
        if ((fConfiguration.features & 0x2) != 0)
        {
          if (localObject != null) {
            localElementImpl.setTextContent((String)localObject);
          }
        }
        else
        {
          String str = localElementImpl.getTextContent();
          if ((str.length() == 0) && (localObject != null)) {
            localElementImpl.setTextContent((String)localObject);
          }
        }
        return;
      }
    }
    if ((fCurrentNode instanceof ElementNSImpl)) {
      ((ElementNSImpl)fCurrentNode).setType(null);
    }
  }
  
  public void startCDATA(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void endCDATA(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void endDocument(Augmentations paramAugmentations)
    throws XNIException
  {}
  
  public void setDocumentSource(XMLDocumentSource paramXMLDocumentSource) {}
  
  public XMLDocumentSource getDocumentSource()
  {
    return null;
  }
  
  protected final class XMLAttributesProxy
    implements XMLAttributes
  {
    protected AttributeMap fAttributes;
    protected CoreDocumentImpl fDocument;
    protected ElementImpl fElement;
    protected final Vector fDTDTypes = new Vector(5);
    protected final Vector fAugmentations = new Vector(5);
    
    protected XMLAttributesProxy() {}
    
    public void setAttributes(AttributeMap paramAttributeMap, CoreDocumentImpl paramCoreDocumentImpl, ElementImpl paramElementImpl)
    {
      fDocument = paramCoreDocumentImpl;
      fAttributes = paramAttributeMap;
      fElement = paramElementImpl;
      if (paramAttributeMap != null)
      {
        int i = paramAttributeMap.getLength();
        fDTDTypes.setSize(i);
        fAugmentations.setSize(i);
        for (int j = 0; j < i; j++) {
          fAugmentations.setElementAt(new AugmentationsImpl(), j);
        }
      }
      else
      {
        fDTDTypes.setSize(0);
        fAugmentations.setSize(0);
      }
    }
    
    public int addAttribute(QName paramQName, String paramString1, String paramString2)
    {
      int i = fElement.getXercesAttribute(uri, localpart);
      if (i < 0)
      {
        AttrImpl localAttrImpl = (AttrImpl)((CoreDocumentImpl)fElement.getOwnerDocument()).createAttributeNS(uri, rawname, localpart);
        localAttrImpl.setNodeValue(paramString2);
        i = fElement.setXercesAttributeNode(localAttrImpl);
        fDTDTypes.insertElementAt(paramString1, i);
        fAugmentations.insertElementAt(new AugmentationsImpl(), i);
        localAttrImpl.setSpecified(false);
      }
      return i;
    }
    
    public void removeAllAttributes() {}
    
    public void removeAttributeAt(int paramInt) {}
    
    public int getLength()
    {
      return fAttributes != null ? fAttributes.getLength() : 0;
    }
    
    public int getIndex(String paramString)
    {
      return -1;
    }
    
    public int getIndex(String paramString1, String paramString2)
    {
      return -1;
    }
    
    public void setName(int paramInt, QName paramQName) {}
    
    public void getName(int paramInt, QName paramQName)
    {
      if (fAttributes != null) {
        updateQName((Node)fAttributes.getItem(paramInt), paramQName);
      }
    }
    
    public String getPrefix(int paramInt)
    {
      if (fAttributes != null)
      {
        Node localNode = (Node)fAttributes.getItem(paramInt);
        String str = localNode.getPrefix();
        str = (str != null) && (str.length() != 0) ? fSymbolTable.addSymbol(str) : null;
        return str;
      }
      return null;
    }
    
    public String getURI(int paramInt)
    {
      if (fAttributes != null)
      {
        Node localNode = (Node)fAttributes.getItem(paramInt);
        String str = localNode.getNamespaceURI();
        str = str != null ? fSymbolTable.addSymbol(str) : null;
        return str;
      }
      return null;
    }
    
    public String getLocalName(int paramInt)
    {
      if (fAttributes != null)
      {
        Node localNode = (Node)fAttributes.getItem(paramInt);
        String str = localNode.getLocalName();
        str = str != null ? fSymbolTable.addSymbol(str) : null;
        return str;
      }
      return null;
    }
    
    public String getQName(int paramInt)
    {
      if (fAttributes != null)
      {
        Node localNode = (Node)fAttributes.getItem(paramInt);
        String str = fSymbolTable.addSymbol(localNode.getNodeName());
        return str;
      }
      return null;
    }
    
    public void setType(int paramInt, String paramString)
    {
      fDTDTypes.setElementAt(paramString, paramInt);
    }
    
    public String getType(int paramInt)
    {
      String str = (String)fDTDTypes.elementAt(paramInt);
      return str != null ? getReportableType(str) : "CDATA";
    }
    
    public String getType(String paramString)
    {
      return "CDATA";
    }
    
    public String getType(String paramString1, String paramString2)
    {
      return "CDATA";
    }
    
    private String getReportableType(String paramString)
    {
      if (paramString.charAt(0) == '(') {
        return "NMTOKEN";
      }
      return paramString;
    }
    
    public void setValue(int paramInt, String paramString)
    {
      if (fAttributes != null)
      {
        AttrImpl localAttrImpl = (AttrImpl)fAttributes.getItem(paramInt);
        boolean bool = localAttrImpl.getSpecified();
        localAttrImpl.setValue(paramString);
        localAttrImpl.setSpecified(bool);
      }
    }
    
    public String getValue(int paramInt)
    {
      return fAttributes != null ? fAttributes.item(paramInt).getNodeValue() : "";
    }
    
    public String getValue(String paramString)
    {
      return null;
    }
    
    public String getValue(String paramString1, String paramString2)
    {
      if (fAttributes != null)
      {
        Node localNode = fAttributes.getNamedItemNS(paramString1, paramString2);
        return localNode != null ? localNode.getNodeValue() : null;
      }
      return null;
    }
    
    public void setNonNormalizedValue(int paramInt, String paramString) {}
    
    public String getNonNormalizedValue(int paramInt)
    {
      return null;
    }
    
    public void setSpecified(int paramInt, boolean paramBoolean)
    {
      AttrImpl localAttrImpl = (AttrImpl)fAttributes.getItem(paramInt);
      localAttrImpl.setSpecified(paramBoolean);
    }
    
    public boolean isSpecified(int paramInt)
    {
      return ((Attr)fAttributes.getItem(paramInt)).getSpecified();
    }
    
    public Augmentations getAugmentations(int paramInt)
    {
      return (Augmentations)fAugmentations.elementAt(paramInt);
    }
    
    public Augmentations getAugmentations(String paramString1, String paramString2)
    {
      return null;
    }
    
    public Augmentations getAugmentations(String paramString)
    {
      return null;
    }
    
    public void setAugmentations(int paramInt, Augmentations paramAugmentations)
    {
      fAugmentations.setElementAt(paramAugmentations, paramInt);
    }
  }
}
