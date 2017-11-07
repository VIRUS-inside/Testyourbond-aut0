package org.apache.xerces.jaxp;

import java.io.IOException;
import javax.xml.validation.TypeInfoProvider;
import javax.xml.validation.ValidatorHandler;
import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.impl.xs.opti.DefaultXMLDocumentHandler;
import org.apache.xerces.util.AttributesProxy;
import org.apache.xerces.util.AugmentationsImpl;
import org.apache.xerces.util.ErrorHandlerProxy;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.LocatorProxy;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLResourceIdentifierImpl;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLComponentManager;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

final class JAXPValidatorComponent
  extends TeeXMLDocumentFilterImpl
  implements XMLComponent
{
  private static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
  private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
  private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
  private final ValidatorHandler validator;
  private final XNI2SAX xni2sax = new XNI2SAX(null);
  private final SAX2XNI sax2xni = new SAX2XNI(null);
  private final TypeInfoProvider typeInfoProvider;
  private Augmentations fCurrentAug;
  private XMLAttributes fCurrentAttributes;
  private SymbolTable fSymbolTable;
  private XMLErrorReporter fErrorReporter;
  private XMLEntityResolver fEntityResolver;
  private static final TypeInfoProvider noInfoProvider = new TypeInfoProvider()
  {
    public TypeInfo getElementTypeInfo()
    {
      return null;
    }
    
    public TypeInfo getAttributeTypeInfo(int paramAnonymousInt)
    {
      return null;
    }
    
    public TypeInfo getAttributeTypeInfo(String paramAnonymousString)
    {
      return null;
    }
    
    public TypeInfo getAttributeTypeInfo(String paramAnonymousString1, String paramAnonymousString2)
    {
      return null;
    }
    
    public boolean isIdAttribute(int paramAnonymousInt)
    {
      return false;
    }
    
    public boolean isSpecified(int paramAnonymousInt)
    {
      return false;
    }
  };
  
  public JAXPValidatorComponent(ValidatorHandler paramValidatorHandler)
  {
    validator = paramValidatorHandler;
    TypeInfoProvider localTypeInfoProvider = paramValidatorHandler.getTypeInfoProvider();
    if (localTypeInfoProvider == null) {
      localTypeInfoProvider = noInfoProvider;
    }
    typeInfoProvider = localTypeInfoProvider;
    xni2sax.setContentHandler(validator);
    validator.setContentHandler(sax2xni);
    setSide(xni2sax);
    validator.setErrorHandler(new ErrorHandlerProxy()
    {
      protected XMLErrorHandler getErrorHandler()
      {
        XMLErrorHandler localXMLErrorHandler = fErrorReporter.getErrorHandler();
        if (localXMLErrorHandler != null) {
          return localXMLErrorHandler;
        }
        return new ErrorHandlerWrapper(JAXPValidatorComponent.DraconianErrorHandler.getInstance());
      }
    });
    validator.setResourceResolver(new LSResourceResolver()
    {
      public LSInput resolveResource(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, String paramAnonymousString5)
      {
        if (fEntityResolver == null) {
          return null;
        }
        try
        {
          XMLInputSource localXMLInputSource = fEntityResolver.resolveEntity(new XMLResourceIdentifierImpl(paramAnonymousString3, paramAnonymousString4, paramAnonymousString5, null));
          if (localXMLInputSource == null) {
            return null;
          }
          DOMInputImpl localDOMInputImpl = new DOMInputImpl();
          localDOMInputImpl.setBaseURI(localXMLInputSource.getBaseSystemId());
          localDOMInputImpl.setByteStream(localXMLInputSource.getByteStream());
          localDOMInputImpl.setCharacterStream(localXMLInputSource.getCharacterStream());
          localDOMInputImpl.setEncoding(localXMLInputSource.getEncoding());
          localDOMInputImpl.setPublicId(localXMLInputSource.getPublicId());
          localDOMInputImpl.setSystemId(localXMLInputSource.getSystemId());
          return localDOMInputImpl;
        }
        catch (IOException localIOException)
        {
          throw new XNIException(localIOException);
        }
      }
    });
  }
  
  public void startElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    fCurrentAttributes = paramXMLAttributes;
    fCurrentAug = paramAugmentations;
    xni2sax.startElement(paramQName, paramXMLAttributes, null);
    fCurrentAttributes = null;
  }
  
  public void endElement(QName paramQName, Augmentations paramAugmentations)
    throws XNIException
  {
    fCurrentAug = paramAugmentations;
    xni2sax.endElement(paramQName, null);
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
    fCurrentAug = paramAugmentations;
    xni2sax.characters(paramXMLString, null);
  }
  
  public void ignorableWhitespace(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    fCurrentAug = paramAugmentations;
    xni2sax.ignorableWhitespace(paramXMLString, null);
  }
  
  public void reset(XMLComponentManager paramXMLComponentManager)
    throws XMLConfigurationException
  {
    fSymbolTable = ((SymbolTable)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
    fErrorReporter = ((XMLErrorReporter)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
    try
    {
      fEntityResolver = ((XMLEntityResolver)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-manager"));
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      fEntityResolver = null;
    }
  }
  
  private void updateAttributes(Attributes paramAttributes)
  {
    int i = paramAttributes.getLength();
    for (int j = 0; j < i; j++)
    {
      String str1 = paramAttributes.getQName(j);
      int k = fCurrentAttributes.getIndex(str1);
      String str2 = paramAttributes.getValue(j);
      if (k == -1)
      {
        int m = str1.indexOf(':');
        String str3;
        if (m < 0) {
          str3 = null;
        } else {
          str3 = symbolize(str1.substring(0, m));
        }
        k = fCurrentAttributes.addAttribute(new QName(str3, symbolize(paramAttributes.getLocalName(j)), symbolize(str1), symbolize(paramAttributes.getURI(j))), paramAttributes.getType(j), str2);
      }
      else if (!str2.equals(fCurrentAttributes.getValue(k)))
      {
        fCurrentAttributes.setValue(k, str2);
      }
    }
  }
  
  private String symbolize(String paramString)
  {
    return fSymbolTable.addSymbol(paramString);
  }
  
  public String[] getRecognizedFeatures()
  {
    return null;
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XMLConfigurationException
  {}
  
  public String[] getRecognizedProperties()
  {
    return new String[] { "http://apache.org/xml/properties/internal/entity-manager", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/symbol-table" };
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XMLConfigurationException
  {}
  
  public Boolean getFeatureDefault(String paramString)
  {
    return null;
  }
  
  public Object getPropertyDefault(String paramString)
  {
    return null;
  }
  
  private static final class DraconianErrorHandler
    implements ErrorHandler
  {
    private static final DraconianErrorHandler ERROR_HANDLER_INSTANCE = new DraconianErrorHandler();
    
    private DraconianErrorHandler() {}
    
    public static DraconianErrorHandler getInstance()
    {
      return ERROR_HANDLER_INSTANCE;
    }
    
    public void warning(SAXParseException paramSAXParseException)
      throws SAXException
    {}
    
    public void error(SAXParseException paramSAXParseException)
      throws SAXException
    {
      throw paramSAXParseException;
    }
    
    public void fatalError(SAXParseException paramSAXParseException)
      throws SAXException
    {
      throw paramSAXParseException;
    }
  }
  
  private static final class XNI2SAX
    extends DefaultXMLDocumentHandler
  {
    private ContentHandler fContentHandler;
    private String fVersion;
    protected NamespaceContext fNamespaceContext;
    private final AttributesProxy fAttributesProxy = new AttributesProxy(null);
    
    private XNI2SAX() {}
    
    public void setContentHandler(ContentHandler paramContentHandler)
    {
      fContentHandler = paramContentHandler;
    }
    
    public ContentHandler getContentHandler()
    {
      return fContentHandler;
    }
    
    public void xmlDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
      throws XNIException
    {
      fVersion = paramString1;
    }
    
    public void startDocument(XMLLocator paramXMLLocator, String paramString, NamespaceContext paramNamespaceContext, Augmentations paramAugmentations)
      throws XNIException
    {
      fNamespaceContext = paramNamespaceContext;
      fContentHandler.setDocumentLocator(new LocatorProxy(paramXMLLocator));
      try
      {
        fContentHandler.startDocument();
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
      }
    }
    
    public void endDocument(Augmentations paramAugmentations)
      throws XNIException
    {
      try
      {
        fContentHandler.endDocument();
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
      }
    }
    
    public void processingInstruction(String paramString, XMLString paramXMLString, Augmentations paramAugmentations)
      throws XNIException
    {
      try
      {
        fContentHandler.processingInstruction(paramString, paramXMLString.toString());
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
      }
    }
    
    public void startElement(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
      throws XNIException
    {
      try
      {
        int i = fNamespaceContext.getDeclaredPrefixCount();
        if (i > 0)
        {
          str1 = null;
          str2 = null;
          for (int j = 0; j < i; j++)
          {
            str1 = fNamespaceContext.getDeclaredPrefixAt(j);
            str2 = fNamespaceContext.getURI(str1);
            fContentHandler.startPrefixMapping(str1, str2 == null ? "" : str2);
          }
        }
        String str1 = uri != null ? uri : "";
        String str2 = localpart;
        fAttributesProxy.setAttributes(paramXMLAttributes);
        fContentHandler.startElement(str1, str2, rawname, fAttributesProxy);
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
      }
    }
    
    public void endElement(QName paramQName, Augmentations paramAugmentations)
      throws XNIException
    {
      try
      {
        String str1 = uri != null ? uri : "";
        String str2 = localpart;
        fContentHandler.endElement(str1, str2, rawname);
        int i = fNamespaceContext.getDeclaredPrefixCount();
        if (i > 0) {
          for (int j = 0; j < i; j++) {
            fContentHandler.endPrefixMapping(fNamespaceContext.getDeclaredPrefixAt(j));
          }
        }
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
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
      try
      {
        fContentHandler.characters(ch, offset, length);
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
      }
    }
    
    public void ignorableWhitespace(XMLString paramXMLString, Augmentations paramAugmentations)
      throws XNIException
    {
      try
      {
        fContentHandler.ignorableWhitespace(ch, offset, length);
      }
      catch (SAXException localSAXException)
      {
        throw new XNIException(localSAXException);
      }
    }
    
    XNI2SAX(JAXPValidatorComponent.1 param1)
    {
      this();
    }
  }
  
  private final class SAX2XNI
    extends DefaultHandler
  {
    private final Augmentations fAugmentations = new AugmentationsImpl();
    private final QName fQName = new QName();
    
    private SAX2XNI() {}
    
    public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
      throws SAXException
    {
      try
      {
        handler().characters(new XMLString(paramArrayOfChar, paramInt1, paramInt2), aug());
      }
      catch (XNIException localXNIException)
      {
        throw toSAXException(localXNIException);
      }
    }
    
    public void ignorableWhitespace(char[] paramArrayOfChar, int paramInt1, int paramInt2)
      throws SAXException
    {
      try
      {
        handler().ignorableWhitespace(new XMLString(paramArrayOfChar, paramInt1, paramInt2), aug());
      }
      catch (XNIException localXNIException)
      {
        throw toSAXException(localXNIException);
      }
    }
    
    public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
      throws SAXException
    {
      try
      {
        JAXPValidatorComponent.this.updateAttributes(paramAttributes);
        handler().startElement(toQName(paramString1, paramString2, paramString3), fCurrentAttributes, elementAug());
      }
      catch (XNIException localXNIException)
      {
        throw toSAXException(localXNIException);
      }
    }
    
    public void endElement(String paramString1, String paramString2, String paramString3)
      throws SAXException
    {
      try
      {
        handler().endElement(toQName(paramString1, paramString2, paramString3), aug());
      }
      catch (XNIException localXNIException)
      {
        throw toSAXException(localXNIException);
      }
    }
    
    private Augmentations elementAug()
    {
      Augmentations localAugmentations = aug();
      return localAugmentations;
    }
    
    private Augmentations aug()
    {
      if (fCurrentAug != null)
      {
        Augmentations localAugmentations = fCurrentAug;
        fCurrentAug = null;
        return localAugmentations;
      }
      fAugmentations.removeAllItems();
      return fAugmentations;
    }
    
    private XMLDocumentHandler handler()
    {
      return getDocumentHandler();
    }
    
    private SAXException toSAXException(XNIException paramXNIException)
    {
      Object localObject = paramXNIException.getException();
      if (localObject == null) {
        localObject = paramXNIException;
      }
      if ((localObject instanceof SAXException)) {
        return (SAXException)localObject;
      }
      return new SAXException((Exception)localObject);
    }
    
    private QName toQName(String paramString1, String paramString2, String paramString3)
    {
      String str = null;
      int i = paramString3.indexOf(':');
      if (i > 0) {
        str = JAXPValidatorComponent.this.symbolize(paramString3.substring(0, i));
      }
      paramString2 = JAXPValidatorComponent.this.symbolize(paramString2);
      paramString3 = JAXPValidatorComponent.this.symbolize(paramString3);
      paramString1 = JAXPValidatorComponent.this.symbolize(paramString1);
      fQName.setValues(str, paramString2, paramString3, paramString1);
      return fQName;
    }
    
    SAX2XNI(JAXPValidatorComponent.1 param1)
    {
      this();
    }
  }
}
