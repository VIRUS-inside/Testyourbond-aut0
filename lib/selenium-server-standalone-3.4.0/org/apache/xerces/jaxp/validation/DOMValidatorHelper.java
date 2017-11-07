package org.apache.xerces.jaxp.validation;

import java.io.IOException;
import java.util.Enumeration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.impl.xs.XMLSchemaValidator;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLParseException;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

final class DOMValidatorHelper
  implements ValidatorHelper, EntityState
{
  private static final int CHUNK_SIZE = 1024;
  private static final int CHUNK_MASK = 1023;
  private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  private static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
  private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
  private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
  private final XMLErrorReporter fErrorReporter;
  private final NamespaceSupport fNamespaceContext;
  private final DOMNamespaceContext fDOMNamespaceContext = new DOMNamespaceContext();
  private final XMLSchemaValidator fSchemaValidator;
  private final SymbolTable fSymbolTable;
  private final ValidationManager fValidationManager;
  private final XMLSchemaValidatorComponentManager fComponentManager;
  private final SimpleLocator fXMLLocator = new SimpleLocator(null, null, -1, -1, -1);
  private DOMDocumentHandler fDOMValidatorHandler;
  private final DOMResultAugmentor fDOMResultAugmentor = new DOMResultAugmentor(this);
  private final DOMResultBuilder fDOMResultBuilder = new DOMResultBuilder();
  private NamedNodeMap fEntities = null;
  private final char[] fCharBuffer = new char['Ѐ'];
  private Node fRoot;
  private Node fCurrentElement;
  final QName fElementQName = new QName();
  final QName fAttributeQName = new QName();
  final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
  final XMLString fTempString = new XMLString();
  
  public DOMValidatorHelper(XMLSchemaValidatorComponentManager paramXMLSchemaValidatorComponentManager)
  {
    fComponentManager = paramXMLSchemaValidatorComponentManager;
    fErrorReporter = ((XMLErrorReporter)fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
    fNamespaceContext = ((NamespaceSupport)fComponentManager.getProperty("http://apache.org/xml/properties/internal/namespace-context"));
    fSchemaValidator = ((XMLSchemaValidator)fComponentManager.getProperty("http://apache.org/xml/properties/internal/validator/schema"));
    fSymbolTable = ((SymbolTable)fComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
    fValidationManager = ((ValidationManager)fComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager"));
  }
  
  public void validate(Source paramSource, Result paramResult)
    throws SAXException, IOException
  {
    if (((paramResult instanceof DOMResult)) || (paramResult == null))
    {
      DOMSource localDOMSource = (DOMSource)paramSource;
      DOMResult localDOMResult = (DOMResult)paramResult;
      Node localNode = localDOMSource.getNode();
      fRoot = localNode;
      if (localNode != null)
      {
        fComponentManager.reset();
        fValidationManager.setEntityState(this);
        fDOMNamespaceContext.reset();
        String str = localDOMSource.getSystemId();
        fXMLLocator.setLiteralSystemId(str);
        fXMLLocator.setExpandedSystemId(str);
        fErrorReporter.setDocumentLocator(fXMLLocator);
        try
        {
          setupEntityMap(localNode.getNodeType() == 9 ? (Document)localNode : localNode.getOwnerDocument());
          setupDOMResultHandler(localDOMSource, localDOMResult);
          fSchemaValidator.startDocument(fXMLLocator, null, fDOMNamespaceContext, null);
          validate(localNode);
          fSchemaValidator.endDocument(null);
        }
        catch (XMLParseException localXMLParseException)
        {
          throw Util.toSAXParseException(localXMLParseException);
        }
        catch (XNIException localXNIException)
        {
          throw Util.toSAXException(localXNIException);
        }
        finally
        {
          fRoot = null;
          fCurrentElement = null;
          fEntities = null;
          if (fDOMValidatorHandler != null) {
            fDOMValidatorHandler.setDOMResult(null);
          }
        }
      }
      return;
    }
    throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(fComponentManager.getLocale(), "SourceResultMismatch", new Object[] { paramSource.getClass().getName(), paramResult.getClass().getName() }));
  }
  
  public boolean isEntityDeclared(String paramString)
  {
    return false;
  }
  
  public boolean isEntityUnparsed(String paramString)
  {
    if (fEntities != null)
    {
      Entity localEntity = (Entity)fEntities.getNamedItem(paramString);
      if (localEntity != null) {
        return localEntity.getNotationName() != null;
      }
    }
    return false;
  }
  
  private void validate(Node paramNode)
  {
    Node localNode1 = paramNode;
    boolean bool = useIsSameNode(localNode1);
    while (paramNode != null)
    {
      beginNode(paramNode);
      Node localNode2 = paramNode.getFirstChild();
      while (localNode2 == null)
      {
        finishNode(paramNode);
        if (localNode1 == paramNode) {
          break;
        }
        localNode2 = paramNode.getNextSibling();
        if (localNode2 == null)
        {
          paramNode = paramNode.getParentNode();
          if (paramNode != null)
          {
            if ((localNode1 == paramNode ? 1 : bool ? localNode1.isSameNode(paramNode) : 0) == 0) {}
          }
          else
          {
            if (paramNode != null) {
              finishNode(paramNode);
            }
            localNode2 = null;
            break;
          }
        }
      }
      paramNode = localNode2;
    }
  }
  
  private void beginNode(Node paramNode)
  {
    switch (paramNode.getNodeType())
    {
    case 1: 
      fCurrentElement = paramNode;
      fNamespaceContext.pushContext();
      fillQName(fElementQName, paramNode);
      processAttributes(paramNode.getAttributes());
      fSchemaValidator.startElement(fElementQName, fAttributes, null);
      break;
    case 3: 
      if (fDOMValidatorHandler != null)
      {
        fDOMValidatorHandler.setIgnoringCharacters(true);
        sendCharactersToValidator(paramNode.getNodeValue());
        fDOMValidatorHandler.setIgnoringCharacters(false);
        fDOMValidatorHandler.characters((Text)paramNode);
      }
      else
      {
        sendCharactersToValidator(paramNode.getNodeValue());
      }
      break;
    case 4: 
      if (fDOMValidatorHandler != null)
      {
        fDOMValidatorHandler.setIgnoringCharacters(true);
        fSchemaValidator.startCDATA(null);
        sendCharactersToValidator(paramNode.getNodeValue());
        fSchemaValidator.endCDATA(null);
        fDOMValidatorHandler.setIgnoringCharacters(false);
        fDOMValidatorHandler.cdata((CDATASection)paramNode);
      }
      else
      {
        fSchemaValidator.startCDATA(null);
        sendCharactersToValidator(paramNode.getNodeValue());
        fSchemaValidator.endCDATA(null);
      }
      break;
    case 7: 
      if (fDOMValidatorHandler != null) {
        fDOMValidatorHandler.processingInstruction((ProcessingInstruction)paramNode);
      }
      break;
    case 8: 
      if (fDOMValidatorHandler != null) {
        fDOMValidatorHandler.comment((Comment)paramNode);
      }
      break;
    case 10: 
      if (fDOMValidatorHandler != null) {
        fDOMValidatorHandler.doctypeDecl((DocumentType)paramNode);
      }
      break;
    }
  }
  
  private void finishNode(Node paramNode)
  {
    if (paramNode.getNodeType() == 1)
    {
      fCurrentElement = paramNode;
      fillQName(fElementQName, paramNode);
      fSchemaValidator.endElement(fElementQName, null);
      fNamespaceContext.popContext();
    }
  }
  
  private void setupEntityMap(Document paramDocument)
  {
    if (paramDocument != null)
    {
      DocumentType localDocumentType = paramDocument.getDoctype();
      if (localDocumentType != null)
      {
        fEntities = localDocumentType.getEntities();
        return;
      }
    }
    fEntities = null;
  }
  
  private void setupDOMResultHandler(DOMSource paramDOMSource, DOMResult paramDOMResult)
    throws SAXException
  {
    if (paramDOMResult == null)
    {
      fDOMValidatorHandler = null;
      fSchemaValidator.setDocumentHandler(null);
      return;
    }
    Node localNode = paramDOMResult.getNode();
    if (paramDOMSource.getNode() == localNode)
    {
      fDOMValidatorHandler = fDOMResultAugmentor;
      fDOMResultAugmentor.setDOMResult(paramDOMResult);
      fSchemaValidator.setDocumentHandler(fDOMResultAugmentor);
      return;
    }
    if (paramDOMResult.getNode() == null) {
      try
      {
        DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        localDocumentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
        paramDOMResult.setNode(localDocumentBuilder.newDocument());
      }
      catch (ParserConfigurationException localParserConfigurationException)
      {
        throw new SAXException(localParserConfigurationException);
      }
    }
    fDOMValidatorHandler = fDOMResultBuilder;
    fDOMResultBuilder.setDOMResult(paramDOMResult);
    fSchemaValidator.setDocumentHandler(fDOMResultBuilder);
  }
  
  private void fillQName(QName paramQName, Node paramNode)
  {
    String str1 = paramNode.getPrefix();
    String str2 = paramNode.getLocalName();
    String str3 = paramNode.getNodeName();
    String str4 = paramNode.getNamespaceURI();
    prefix = (str1 != null ? fSymbolTable.addSymbol(str1) : XMLSymbols.EMPTY_STRING);
    localpart = (str2 != null ? fSymbolTable.addSymbol(str2) : XMLSymbols.EMPTY_STRING);
    rawname = (str3 != null ? fSymbolTable.addSymbol(str3) : XMLSymbols.EMPTY_STRING);
    uri = ((str4 != null) && (str4.length() > 0) ? fSymbolTable.addSymbol(str4) : null);
  }
  
  private void processAttributes(NamedNodeMap paramNamedNodeMap)
  {
    int i = paramNamedNodeMap.getLength();
    fAttributes.removeAllAttributes();
    for (int j = 0; j < i; j++)
    {
      Attr localAttr = (Attr)paramNamedNodeMap.item(j);
      String str = localAttr.getValue();
      if (str == null) {
        str = XMLSymbols.EMPTY_STRING;
      }
      fillQName(fAttributeQName, localAttr);
      fAttributes.addAttributeNS(fAttributeQName, XMLSymbols.fCDATASymbol, str);
      fAttributes.setSpecified(j, localAttr.getSpecified());
      if (fAttributeQName.uri == NamespaceContext.XMLNS_URI) {
        if (fAttributeQName.prefix == XMLSymbols.PREFIX_XMLNS) {
          fNamespaceContext.declarePrefix(fAttributeQName.localpart, str.length() != 0 ? fSymbolTable.addSymbol(str) : null);
        } else {
          fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, str.length() != 0 ? fSymbolTable.addSymbol(str) : null);
        }
      }
    }
  }
  
  private void sendCharactersToValidator(String paramString)
  {
    if (paramString != null)
    {
      int i = paramString.length();
      int j = i & 0x3FF;
      if (j > 0)
      {
        paramString.getChars(0, j, fCharBuffer, 0);
        fTempString.setValues(fCharBuffer, 0, j);
        fSchemaValidator.characters(fTempString, null);
      }
      int k = j;
      while (k < i)
      {
        k += 1024;
        paramString.getChars(k, k, fCharBuffer, 0);
        fTempString.setValues(fCharBuffer, 0, 1024);
        fSchemaValidator.characters(fTempString, null);
      }
    }
  }
  
  private boolean useIsSameNode(Node paramNode)
  {
    if ((paramNode instanceof NodeImpl)) {
      return false;
    }
    Document localDocument = paramNode.getNodeType() == 9 ? (Document)paramNode : paramNode.getOwnerDocument();
    return (localDocument != null) && (localDocument.getImplementation().hasFeature("Core", "3.0"));
  }
  
  Node getCurrentElement()
  {
    return fCurrentElement;
  }
  
  final class DOMNamespaceContext
    implements NamespaceContext
  {
    protected String[] fNamespace = new String[32];
    protected int fNamespaceSize = 0;
    protected boolean fDOMContextBuilt = false;
    
    DOMNamespaceContext() {}
    
    public void pushContext()
    {
      fNamespaceContext.pushContext();
    }
    
    public void popContext()
    {
      fNamespaceContext.popContext();
    }
    
    public boolean declarePrefix(String paramString1, String paramString2)
    {
      return fNamespaceContext.declarePrefix(paramString1, paramString2);
    }
    
    public String getURI(String paramString)
    {
      String str = fNamespaceContext.getURI(paramString);
      if (str == null)
      {
        if (!fDOMContextBuilt)
        {
          fillNamespaceContext();
          fDOMContextBuilt = true;
        }
        if ((fNamespaceSize > 0) && (!fNamespaceContext.containsPrefix(paramString))) {
          str = getURI0(paramString);
        }
      }
      return str;
    }
    
    public String getPrefix(String paramString)
    {
      return fNamespaceContext.getPrefix(paramString);
    }
    
    public int getDeclaredPrefixCount()
    {
      return fNamespaceContext.getDeclaredPrefixCount();
    }
    
    public String getDeclaredPrefixAt(int paramInt)
    {
      return fNamespaceContext.getDeclaredPrefixAt(paramInt);
    }
    
    public Enumeration getAllPrefixes()
    {
      return fNamespaceContext.getAllPrefixes();
    }
    
    public void reset()
    {
      fDOMContextBuilt = false;
      fNamespaceSize = 0;
    }
    
    private void fillNamespaceContext()
    {
      if (fRoot != null) {
        for (Node localNode = fRoot.getParentNode(); localNode != null; localNode = localNode.getParentNode()) {
          if (1 == localNode.getNodeType())
          {
            NamedNodeMap localNamedNodeMap = localNode.getAttributes();
            int i = localNamedNodeMap.getLength();
            for (int j = 0; j < i; j++)
            {
              Attr localAttr = (Attr)localNamedNodeMap.item(j);
              String str = localAttr.getValue();
              if (str == null) {
                str = XMLSymbols.EMPTY_STRING;
              }
              DOMValidatorHelper.this.fillQName(fAttributeQName, localAttr);
              if (fAttributeQName.uri == NamespaceContext.XMLNS_URI) {
                if (fAttributeQName.prefix == XMLSymbols.PREFIX_XMLNS) {
                  declarePrefix0(fAttributeQName.localpart, str.length() != 0 ? fSymbolTable.addSymbol(str) : null);
                } else {
                  declarePrefix0(XMLSymbols.EMPTY_STRING, str.length() != 0 ? fSymbolTable.addSymbol(str) : null);
                }
              }
            }
          }
        }
      }
    }
    
    private void declarePrefix0(String paramString1, String paramString2)
    {
      if (fNamespaceSize == fNamespace.length)
      {
        String[] arrayOfString = new String[fNamespaceSize * 2];
        System.arraycopy(fNamespace, 0, arrayOfString, 0, fNamespaceSize);
        fNamespace = arrayOfString;
      }
      fNamespace[(fNamespaceSize++)] = paramString1;
      fNamespace[(fNamespaceSize++)] = paramString2;
    }
    
    private String getURI0(String paramString)
    {
      for (int i = 0; i < fNamespaceSize; i += 2) {
        if (fNamespace[i] == paramString) {
          return fNamespace[(i + 1)];
        }
      }
      return null;
    }
  }
}
