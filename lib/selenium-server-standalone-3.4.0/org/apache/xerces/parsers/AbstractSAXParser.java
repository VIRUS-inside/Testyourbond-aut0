package org.apache.xerces.parsers;

import java.io.CharConversionException;
import java.io.IOException;
import java.util.Locale;
import org.apache.xerces.util.EntityResolver2Wrapper;
import org.apache.xerces.util.EntityResolverWrapper;
import org.apache.xerces.util.ErrorHandlerWrapper;
import org.apache.xerces.util.SAXMessageFormatter;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLConfigurationException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.ElementPSVI;
import org.apache.xerces.xs.PSVIProvider;
import org.xml.sax.AttributeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.Attributes2;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.Locator2;
import org.xml.sax.ext.Locator2Impl;

public abstract class AbstractSAXParser
  extends AbstractXMLDocumentParser
  implements PSVIProvider, Parser, XMLReader
{
  protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
  protected static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
  protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
  private static final String[] RECOGNIZED_FEATURES = { "http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/string-interning" };
  protected static final String LEXICAL_HANDLER = "http://xml.org/sax/properties/lexical-handler";
  protected static final String DECLARATION_HANDLER = "http://xml.org/sax/properties/declaration-handler";
  protected static final String DOM_NODE = "http://xml.org/sax/properties/dom-node";
  private static final String[] RECOGNIZED_PROPERTIES = { "http://xml.org/sax/properties/lexical-handler", "http://xml.org/sax/properties/declaration-handler", "http://xml.org/sax/properties/dom-node" };
  protected boolean fNamespaces;
  protected boolean fNamespacePrefixes = false;
  protected boolean fLexicalHandlerParameterEntities = true;
  protected boolean fStandalone;
  protected boolean fResolveDTDURIs = true;
  protected boolean fUseEntityResolver2 = true;
  protected boolean fXMLNSURIs = false;
  protected ContentHandler fContentHandler;
  protected DocumentHandler fDocumentHandler;
  protected NamespaceContext fNamespaceContext;
  protected DTDHandler fDTDHandler;
  protected DeclHandler fDeclHandler;
  protected LexicalHandler fLexicalHandler;
  protected final QName fQName = new QName();
  protected boolean fParseInProgress = false;
  protected String fVersion;
  private final AttributesProxy fAttributesProxy = new AttributesProxy();
  private Augmentations fAugmentations = null;
  protected SymbolHash fDeclaredAttrs = null;
  
  protected AbstractSAXParser(XMLParserConfiguration paramXMLParserConfiguration)
  {
    super(paramXMLParserConfiguration);
    paramXMLParserConfiguration.addRecognizedFeatures(RECOGNIZED_FEATURES);
    paramXMLParserConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
    try
    {
      paramXMLParserConfiguration.setFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", false);
    }
    catch (XMLConfigurationException localXMLConfigurationException) {}
  }
  
  public void startDocument(XMLLocator paramXMLLocator, String paramString, NamespaceContext paramNamespaceContext, Augmentations paramAugmentations)
    throws XNIException
  {
    fNamespaceContext = paramNamespaceContext;
    try
    {
      if (fDocumentHandler != null)
      {
        if (paramXMLLocator != null) {
          fDocumentHandler.setDocumentLocator(new LocatorProxy(paramXMLLocator));
        }
        if (fDocumentHandler != null) {
          fDocumentHandler.startDocument();
        }
      }
      if (fContentHandler != null)
      {
        if (paramXMLLocator != null) {
          fContentHandler.setDocumentLocator(new LocatorProxy(paramXMLLocator));
        }
        if (fContentHandler != null) {
          fContentHandler.startDocument();
        }
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void xmlDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {
    fVersion = paramString1;
    fStandalone = "yes".equals(paramString3);
  }
  
  public void doctypeDecl(String paramString1, String paramString2, String paramString3, Augmentations paramAugmentations)
    throws XNIException
  {
    fInDTD = true;
    try
    {
      if (fLexicalHandler != null) {
        fLexicalHandler.startDTD(paramString1, paramString2, paramString3);
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
    if (fDeclHandler != null) {
      fDeclaredAttrs = new SymbolHash();
    }
  }
  
  public void startGeneralEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      if ((paramAugmentations != null) && (Boolean.TRUE.equals(paramAugmentations.getItem("ENTITY_SKIPPED"))))
      {
        if (fContentHandler != null) {
          fContentHandler.skippedEntity(paramString1);
        }
      }
      else if (fLexicalHandler != null) {
        fLexicalHandler.startEntity(paramString1);
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void endGeneralEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      if (((paramAugmentations == null) || (!Boolean.TRUE.equals(paramAugmentations.getItem("ENTITY_SKIPPED")))) && (fLexicalHandler != null)) {
        fLexicalHandler.endEntity(paramString);
      }
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
      if (fDocumentHandler != null)
      {
        fAttributesProxy.setAttributes(paramXMLAttributes);
        fDocumentHandler.startElement(rawname, fAttributesProxy);
      }
      if (fContentHandler != null)
      {
        if (fNamespaces)
        {
          startNamespaceMapping();
          int i = paramXMLAttributes.getLength();
          int j;
          if (!fNamespacePrefixes) {
            for (j = i - 1; j >= 0; j--)
            {
              paramXMLAttributes.getName(j, fQName);
              if ((fQName.prefix == XMLSymbols.PREFIX_XMLNS) || (fQName.rawname == XMLSymbols.PREFIX_XMLNS)) {
                paramXMLAttributes.removeAttributeAt(j);
              }
            }
          } else if (!fXMLNSURIs) {
            for (j = i - 1; j >= 0; j--)
            {
              paramXMLAttributes.getName(j, fQName);
              if ((fQName.prefix == XMLSymbols.PREFIX_XMLNS) || (fQName.rawname == XMLSymbols.PREFIX_XMLNS))
              {
                fQName.prefix = "";
                fQName.uri = "";
                fQName.localpart = "";
                paramXMLAttributes.setName(j, fQName);
              }
            }
          }
        }
        fAugmentations = paramAugmentations;
        String str1 = uri != null ? uri : "";
        String str2 = fNamespaces ? localpart : "";
        fAttributesProxy.setAttributes(paramXMLAttributes);
        fContentHandler.startElement(str1, str2, rawname, fAttributesProxy);
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void characters(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    if (length == 0) {
      return;
    }
    try
    {
      if (fDocumentHandler != null) {
        fDocumentHandler.characters(ch, offset, length);
      }
      if (fContentHandler != null) {
        fContentHandler.characters(ch, offset, length);
      }
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
      if (fDocumentHandler != null) {
        fDocumentHandler.ignorableWhitespace(ch, offset, length);
      }
      if (fContentHandler != null) {
        fContentHandler.ignorableWhitespace(ch, offset, length);
      }
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
      if (fDocumentHandler != null) {
        fDocumentHandler.endElement(rawname);
      }
      if (fContentHandler != null)
      {
        fAugmentations = paramAugmentations;
        String str1 = uri != null ? uri : "";
        String str2 = fNamespaces ? localpart : "";
        fContentHandler.endElement(str1, str2, rawname);
        if (fNamespaces) {
          endNamespaceMapping();
        }
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void startCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      if (fLexicalHandler != null) {
        fLexicalHandler.startCDATA();
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void endCDATA(Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      if (fLexicalHandler != null) {
        fLexicalHandler.endCDATA();
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void comment(XMLString paramXMLString, Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      if (fLexicalHandler != null) {
        fLexicalHandler.comment(ch, 0, length);
      }
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
      if (fDocumentHandler != null) {
        fDocumentHandler.processingInstruction(paramString, paramXMLString.toString());
      }
      if (fContentHandler != null) {
        fContentHandler.processingInstruction(paramString, paramXMLString.toString());
      }
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
      if (fDocumentHandler != null) {
        fDocumentHandler.endDocument();
      }
      if (fContentHandler != null) {
        fContentHandler.endDocument();
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void startExternalSubset(XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    startParameterEntity("[dtd]", null, null, paramAugmentations);
  }
  
  public void endExternalSubset(Augmentations paramAugmentations)
    throws XNIException
  {
    endParameterEntity("[dtd]", paramAugmentations);
  }
  
  public void startParameterEntity(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      if ((paramAugmentations != null) && (Boolean.TRUE.equals(paramAugmentations.getItem("ENTITY_SKIPPED"))))
      {
        if (fContentHandler != null) {
          fContentHandler.skippedEntity(paramString1);
        }
      }
      else if ((fLexicalHandler != null) && (fLexicalHandlerParameterEntities)) {
        fLexicalHandler.startEntity(paramString1);
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void endParameterEntity(String paramString, Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      if (((paramAugmentations == null) || (!Boolean.TRUE.equals(paramAugmentations.getItem("ENTITY_SKIPPED")))) && (fLexicalHandler != null) && (fLexicalHandlerParameterEntities)) {
        fLexicalHandler.endEntity(paramString);
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void elementDecl(String paramString1, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      if (fDeclHandler != null) {
        fDeclHandler.elementDecl(paramString1, paramString2);
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void attributeDecl(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString, String paramString4, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      if (fDeclHandler != null)
      {
        String str = paramString1 + '<' + paramString2;
        if (fDeclaredAttrs.get(str) != null) {
          return;
        }
        fDeclaredAttrs.put(str, Boolean.TRUE);
        if ((paramString3.equals("NOTATION")) || (paramString3.equals("ENUMERATION")))
        {
          localObject = new StringBuffer();
          if (paramString3.equals("NOTATION"))
          {
            ((StringBuffer)localObject).append(paramString3);
            ((StringBuffer)localObject).append(" (");
          }
          else
          {
            ((StringBuffer)localObject).append('(');
          }
          for (int i = 0; i < paramArrayOfString.length; i++)
          {
            ((StringBuffer)localObject).append(paramArrayOfString[i]);
            if (i < paramArrayOfString.length - 1) {
              ((StringBuffer)localObject).append('|');
            }
          }
          ((StringBuffer)localObject).append(')');
          paramString3 = ((StringBuffer)localObject).toString();
        }
        Object localObject = paramXMLString1 == null ? null : paramXMLString1.toString();
        fDeclHandler.attributeDecl(paramString1, paramString2, paramString3, paramString4, (String)localObject);
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void internalEntityDecl(String paramString, XMLString paramXMLString1, XMLString paramXMLString2, Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      if (fDeclHandler != null) {
        fDeclHandler.internalEntityDecl(paramString, paramXMLString1.toString());
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void externalEntityDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      if (fDeclHandler != null)
      {
        String str1 = paramXMLResourceIdentifier.getPublicId();
        String str2 = fResolveDTDURIs ? paramXMLResourceIdentifier.getExpandedSystemId() : paramXMLResourceIdentifier.getLiteralSystemId();
        fDeclHandler.externalEntityDecl(paramString, str1, str2);
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void unparsedEntityDecl(String paramString1, XMLResourceIdentifier paramXMLResourceIdentifier, String paramString2, Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      if (fDTDHandler != null)
      {
        String str1 = paramXMLResourceIdentifier.getPublicId();
        String str2 = fResolveDTDURIs ? paramXMLResourceIdentifier.getExpandedSystemId() : paramXMLResourceIdentifier.getLiteralSystemId();
        fDTDHandler.unparsedEntityDecl(paramString1, str1, str2, paramString2);
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void notationDecl(String paramString, XMLResourceIdentifier paramXMLResourceIdentifier, Augmentations paramAugmentations)
    throws XNIException
  {
    try
    {
      if (fDTDHandler != null)
      {
        String str1 = paramXMLResourceIdentifier.getPublicId();
        String str2 = fResolveDTDURIs ? paramXMLResourceIdentifier.getExpandedSystemId() : paramXMLResourceIdentifier.getLiteralSystemId();
        fDTDHandler.notationDecl(paramString, str1, str2);
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
  }
  
  public void endDTD(Augmentations paramAugmentations)
    throws XNIException
  {
    fInDTD = false;
    try
    {
      if (fLexicalHandler != null) {
        fLexicalHandler.endDTD();
      }
    }
    catch (SAXException localSAXException)
    {
      throw new XNIException(localSAXException);
    }
    if (fDeclaredAttrs != null) {
      fDeclaredAttrs.clear();
    }
  }
  
  public void parse(String paramString)
    throws SAXException, IOException
  {
    XMLInputSource localXMLInputSource = new XMLInputSource(null, paramString, null);
    try
    {
      parse(localXMLInputSource);
    }
    catch (XMLParseException localXMLParseException)
    {
      Exception localException = localXMLParseException.getException();
      if ((localException == null) || ((localException instanceof CharConversionException)))
      {
        localObject = new Locator2Impl();
        ((Locator2Impl)localObject).setXMLVersion(fVersion);
        ((Locator2Impl)localObject).setPublicId(localXMLParseException.getPublicId());
        ((Locator2Impl)localObject).setSystemId(localXMLParseException.getExpandedSystemId());
        ((Locator2Impl)localObject).setLineNumber(localXMLParseException.getLineNumber());
        ((Locator2Impl)localObject).setColumnNumber(localXMLParseException.getColumnNumber());
        throw (localException == null ? new SAXParseException(localXMLParseException.getMessage(), (Locator)localObject) : new SAXParseException(localXMLParseException.getMessage(), (Locator)localObject, localException));
      }
      if ((localException instanceof SAXException)) {
        throw ((SAXException)localException);
      }
      if ((localException instanceof IOException)) {
        throw ((IOException)localException);
      }
      throw new SAXException(localException);
    }
    catch (XNIException localXNIException)
    {
      Object localObject = localXNIException.getException();
      if (localObject == null) {
        throw new SAXException(localXNIException.getMessage());
      }
      if ((localObject instanceof SAXException)) {
        throw ((SAXException)localObject);
      }
      if ((localObject instanceof IOException)) {
        throw ((IOException)localObject);
      }
      throw new SAXException((Exception)localObject);
    }
  }
  
  public void parse(InputSource paramInputSource)
    throws SAXException, IOException
  {
    try
    {
      XMLInputSource localXMLInputSource = new XMLInputSource(paramInputSource.getPublicId(), paramInputSource.getSystemId(), null);
      localXMLInputSource.setByteStream(paramInputSource.getByteStream());
      localXMLInputSource.setCharacterStream(paramInputSource.getCharacterStream());
      localXMLInputSource.setEncoding(paramInputSource.getEncoding());
      parse(localXMLInputSource);
    }
    catch (XMLParseException localXMLParseException)
    {
      Exception localException = localXMLParseException.getException();
      if ((localException == null) || ((localException instanceof CharConversionException)))
      {
        localObject = new Locator2Impl();
        ((Locator2Impl)localObject).setXMLVersion(fVersion);
        ((Locator2Impl)localObject).setPublicId(localXMLParseException.getPublicId());
        ((Locator2Impl)localObject).setSystemId(localXMLParseException.getExpandedSystemId());
        ((Locator2Impl)localObject).setLineNumber(localXMLParseException.getLineNumber());
        ((Locator2Impl)localObject).setColumnNumber(localXMLParseException.getColumnNumber());
        throw (localException == null ? new SAXParseException(localXMLParseException.getMessage(), (Locator)localObject) : new SAXParseException(localXMLParseException.getMessage(), (Locator)localObject, localException));
      }
      if ((localException instanceof SAXException)) {
        throw ((SAXException)localException);
      }
      if ((localException instanceof IOException)) {
        throw ((IOException)localException);
      }
      throw new SAXException(localException);
    }
    catch (XNIException localXNIException)
    {
      Object localObject = localXNIException.getException();
      if (localObject == null) {
        throw new SAXException(localXNIException.getMessage());
      }
      if ((localObject instanceof SAXException)) {
        throw ((SAXException)localObject);
      }
      if ((localObject instanceof IOException)) {
        throw ((IOException)localObject);
      }
      throw new SAXException((Exception)localObject);
    }
  }
  
  public void setEntityResolver(EntityResolver paramEntityResolver)
  {
    try
    {
      XMLEntityResolver localXMLEntityResolver = (XMLEntityResolver)fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
      Object localObject;
      if ((fUseEntityResolver2) && ((paramEntityResolver instanceof EntityResolver2)))
      {
        if ((localXMLEntityResolver instanceof EntityResolver2Wrapper))
        {
          localObject = (EntityResolver2Wrapper)localXMLEntityResolver;
          ((EntityResolver2Wrapper)localObject).setEntityResolver((EntityResolver2)paramEntityResolver);
        }
        else
        {
          fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolver2Wrapper((EntityResolver2)paramEntityResolver));
        }
      }
      else if ((localXMLEntityResolver instanceof EntityResolverWrapper))
      {
        localObject = (EntityResolverWrapper)localXMLEntityResolver;
        ((EntityResolverWrapper)localObject).setEntityResolver(paramEntityResolver);
      }
      else
      {
        fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolverWrapper(paramEntityResolver));
      }
    }
    catch (XMLConfigurationException localXMLConfigurationException) {}
  }
  
  public EntityResolver getEntityResolver()
  {
    Object localObject = null;
    try
    {
      XMLEntityResolver localXMLEntityResolver = (XMLEntityResolver)fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
      if (localXMLEntityResolver != null) {
        if ((localXMLEntityResolver instanceof EntityResolverWrapper)) {
          localObject = ((EntityResolverWrapper)localXMLEntityResolver).getEntityResolver();
        } else if ((localXMLEntityResolver instanceof EntityResolver2Wrapper)) {
          localObject = ((EntityResolver2Wrapper)localXMLEntityResolver).getEntityResolver();
        }
      }
    }
    catch (XMLConfigurationException localXMLConfigurationException) {}
    return localObject;
  }
  
  public void setErrorHandler(ErrorHandler paramErrorHandler)
  {
    try
    {
      XMLErrorHandler localXMLErrorHandler = (XMLErrorHandler)fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
      if ((localXMLErrorHandler instanceof ErrorHandlerWrapper))
      {
        ErrorHandlerWrapper localErrorHandlerWrapper = (ErrorHandlerWrapper)localXMLErrorHandler;
        localErrorHandlerWrapper.setErrorHandler(paramErrorHandler);
      }
      else
      {
        fConfiguration.setProperty("http://apache.org/xml/properties/internal/error-handler", new ErrorHandlerWrapper(paramErrorHandler));
      }
    }
    catch (XMLConfigurationException localXMLConfigurationException) {}
  }
  
  public ErrorHandler getErrorHandler()
  {
    ErrorHandler localErrorHandler = null;
    try
    {
      XMLErrorHandler localXMLErrorHandler = (XMLErrorHandler)fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
      if ((localXMLErrorHandler != null) && ((localXMLErrorHandler instanceof ErrorHandlerWrapper))) {
        localErrorHandler = ((ErrorHandlerWrapper)localXMLErrorHandler).getErrorHandler();
      }
    }
    catch (XMLConfigurationException localXMLConfigurationException) {}
    return localErrorHandler;
  }
  
  public void setLocale(Locale paramLocale)
    throws SAXException
  {
    fConfiguration.setLocale(paramLocale);
  }
  
  public void setDTDHandler(DTDHandler paramDTDHandler)
  {
    fDTDHandler = paramDTDHandler;
  }
  
  public void setDocumentHandler(DocumentHandler paramDocumentHandler)
  {
    fDocumentHandler = paramDocumentHandler;
  }
  
  public void setContentHandler(ContentHandler paramContentHandler)
  {
    fContentHandler = paramContentHandler;
  }
  
  public ContentHandler getContentHandler()
  {
    return fContentHandler;
  }
  
  public DTDHandler getDTDHandler()
  {
    return fDTDHandler;
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    try
    {
      if (paramString.startsWith("http://xml.org/sax/features/"))
      {
        int i = paramString.length() - "http://xml.org/sax/features/".length();
        if ((i == "namespaces".length()) && (paramString.endsWith("namespaces")))
        {
          fConfiguration.setFeature(paramString, paramBoolean);
          fNamespaces = paramBoolean;
          return;
        }
        if ((i == "namespace-prefixes".length()) && (paramString.endsWith("namespace-prefixes")))
        {
          fNamespacePrefixes = paramBoolean;
          return;
        }
        if ((i == "string-interning".length()) && (paramString.endsWith("string-interning")))
        {
          if (!paramBoolean) {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "false-not-supported", new Object[] { paramString }));
          }
          return;
        }
        if ((i == "lexical-handler/parameter-entities".length()) && (paramString.endsWith("lexical-handler/parameter-entities")))
        {
          fLexicalHandlerParameterEntities = paramBoolean;
          return;
        }
        if ((i == "resolve-dtd-uris".length()) && (paramString.endsWith("resolve-dtd-uris")))
        {
          fResolveDTDURIs = paramBoolean;
          return;
        }
        if ((i == "unicode-normalization-checking".length()) && (paramString.endsWith("unicode-normalization-checking")))
        {
          if (paramBoolean) {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "true-not-supported", new Object[] { paramString }));
          }
          return;
        }
        if ((i == "xmlns-uris".length()) && (paramString.endsWith("xmlns-uris")))
        {
          fXMLNSURIs = paramBoolean;
          return;
        }
        if ((i == "use-entity-resolver2".length()) && (paramString.endsWith("use-entity-resolver2")))
        {
          if (paramBoolean != fUseEntityResolver2)
          {
            fUseEntityResolver2 = paramBoolean;
            setEntityResolver(getEntityResolver());
          }
          return;
        }
        if (((i == "is-standalone".length()) && (paramString.endsWith("is-standalone"))) || ((i == "use-attributes2".length()) && (paramString.endsWith("use-attributes2"))) || ((i == "use-locator2".length()) && (paramString.endsWith("use-locator2"))) || ((i == "xml-1.1".length()) && (paramString.endsWith("xml-1.1")))) {
          throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "feature-read-only", new Object[] { paramString }));
        }
      }
      fConfiguration.setFeature(paramString, paramBoolean);
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      String str = localXMLConfigurationException.getIdentifier();
      if (localXMLConfigurationException.getType() == 0) {
        throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "feature-not-recognized", new Object[] { str }));
      }
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "feature-not-supported", new Object[] { str }));
    }
  }
  
  public boolean getFeature(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    try
    {
      if (paramString.startsWith("http://xml.org/sax/features/"))
      {
        int i = paramString.length() - "http://xml.org/sax/features/".length();
        if ((i == "namespace-prefixes".length()) && (paramString.endsWith("namespace-prefixes"))) {
          return fNamespacePrefixes;
        }
        if ((i == "string-interning".length()) && (paramString.endsWith("string-interning"))) {
          return true;
        }
        if ((i == "is-standalone".length()) && (paramString.endsWith("is-standalone"))) {
          return fStandalone;
        }
        if ((i == "xml-1.1".length()) && (paramString.endsWith("xml-1.1"))) {
          return fConfiguration instanceof XML11Configurable;
        }
        if ((i == "lexical-handler/parameter-entities".length()) && (paramString.endsWith("lexical-handler/parameter-entities"))) {
          return fLexicalHandlerParameterEntities;
        }
        if ((i == "resolve-dtd-uris".length()) && (paramString.endsWith("resolve-dtd-uris"))) {
          return fResolveDTDURIs;
        }
        if ((i == "xmlns-uris".length()) && (paramString.endsWith("xmlns-uris"))) {
          return fXMLNSURIs;
        }
        if ((i == "unicode-normalization-checking".length()) && (paramString.endsWith("unicode-normalization-checking"))) {
          return false;
        }
        if ((i == "use-entity-resolver2".length()) && (paramString.endsWith("use-entity-resolver2"))) {
          return fUseEntityResolver2;
        }
        if (((i == "use-attributes2".length()) && (paramString.endsWith("use-attributes2"))) || ((i == "use-locator2".length()) && (paramString.endsWith("use-locator2")))) {
          return true;
        }
      }
      return fConfiguration.getFeature(paramString);
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      String str = localXMLConfigurationException.getIdentifier();
      if (localXMLConfigurationException.getType() == 0) {
        throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "feature-not-recognized", new Object[] { str }));
      }
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "feature-not-supported", new Object[] { str }));
    }
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    try
    {
      if (paramString.startsWith("http://xml.org/sax/properties/"))
      {
        int i = paramString.length() - "http://xml.org/sax/properties/".length();
        if ((i == "lexical-handler".length()) && (paramString.endsWith("lexical-handler")))
        {
          try
          {
            setLexicalHandler((LexicalHandler)paramObject);
          }
          catch (ClassCastException localClassCastException1)
          {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "incompatible-class", new Object[] { paramString, "org.xml.sax.ext.LexicalHandler" }));
          }
          return;
        }
        if ((i == "declaration-handler".length()) && (paramString.endsWith("declaration-handler")))
        {
          try
          {
            setDeclHandler((DeclHandler)paramObject);
          }
          catch (ClassCastException localClassCastException2)
          {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "incompatible-class", new Object[] { paramString, "org.xml.sax.ext.DeclHandler" }));
          }
          return;
        }
        if (((i == "dom-node".length()) && (paramString.endsWith("dom-node"))) || ((i == "document-xml-version".length()) && (paramString.endsWith("document-xml-version")))) {
          throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "property-read-only", new Object[] { paramString }));
        }
      }
      fConfiguration.setProperty(paramString, paramObject);
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      String str = localXMLConfigurationException.getIdentifier();
      if (localXMLConfigurationException.getType() == 0) {
        throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "property-not-recognized", new Object[] { str }));
      }
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "property-not-supported", new Object[] { str }));
    }
  }
  
  public Object getProperty(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    try
    {
      if (paramString.startsWith("http://xml.org/sax/properties/"))
      {
        int i = paramString.length() - "http://xml.org/sax/properties/".length();
        if ((i == "document-xml-version".length()) && (paramString.endsWith("document-xml-version"))) {
          return fVersion;
        }
        if ((i == "lexical-handler".length()) && (paramString.endsWith("lexical-handler"))) {
          return getLexicalHandler();
        }
        if ((i == "declaration-handler".length()) && (paramString.endsWith("declaration-handler"))) {
          return getDeclHandler();
        }
        if ((i == "dom-node".length()) && (paramString.endsWith("dom-node"))) {
          throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "dom-node-read-not-supported", null));
        }
      }
      return fConfiguration.getProperty(paramString);
    }
    catch (XMLConfigurationException localXMLConfigurationException)
    {
      String str = localXMLConfigurationException.getIdentifier();
      if (localXMLConfigurationException.getType() == 0) {
        throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "property-not-recognized", new Object[] { str }));
      }
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "property-not-supported", new Object[] { str }));
    }
  }
  
  protected void setDeclHandler(DeclHandler paramDeclHandler)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (fParseInProgress) {
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "property-not-parsing-supported", new Object[] { "http://xml.org/sax/properties/declaration-handler" }));
    }
    fDeclHandler = paramDeclHandler;
  }
  
  protected DeclHandler getDeclHandler()
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    return fDeclHandler;
  }
  
  protected void setLexicalHandler(LexicalHandler paramLexicalHandler)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (fParseInProgress) {
      throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(fConfiguration.getLocale(), "property-not-parsing-supported", new Object[] { "http://xml.org/sax/properties/lexical-handler" }));
    }
    fLexicalHandler = paramLexicalHandler;
  }
  
  protected LexicalHandler getLexicalHandler()
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    return fLexicalHandler;
  }
  
  protected final void startNamespaceMapping()
    throws SAXException
  {
    int i = fNamespaceContext.getDeclaredPrefixCount();
    if (i > 0)
    {
      String str1 = null;
      String str2 = null;
      for (int j = 0; j < i; j++)
      {
        str1 = fNamespaceContext.getDeclaredPrefixAt(j);
        str2 = fNamespaceContext.getURI(str1);
        fContentHandler.startPrefixMapping(str1, str2 == null ? "" : str2);
      }
    }
  }
  
  protected final void endNamespaceMapping()
    throws SAXException
  {
    int i = fNamespaceContext.getDeclaredPrefixCount();
    if (i > 0) {
      for (int j = 0; j < i; j++) {
        fContentHandler.endPrefixMapping(fNamespaceContext.getDeclaredPrefixAt(j));
      }
    }
  }
  
  public void reset()
    throws XNIException
  {
    super.reset();
    fInDTD = false;
    fVersion = "1.0";
    fStandalone = false;
    fNamespaces = fConfiguration.getFeature("http://xml.org/sax/features/namespaces");
    fAugmentations = null;
    fDeclaredAttrs = null;
  }
  
  public ElementPSVI getElementPSVI()
  {
    return fAugmentations != null ? (ElementPSVI)fAugmentations.getItem("ELEMENT_PSVI") : null;
  }
  
  public AttributePSVI getAttributePSVI(int paramInt)
  {
    return (AttributePSVI)fAttributesProxy.fAttributes.getAugmentations(paramInt).getItem("ATTRIBUTE_PSVI");
  }
  
  public AttributePSVI getAttributePSVIByName(String paramString1, String paramString2)
  {
    return (AttributePSVI)fAttributesProxy.fAttributes.getAugmentations(paramString1, paramString2).getItem("ATTRIBUTE_PSVI");
  }
  
  protected static final class AttributesProxy
    implements AttributeList, Attributes2
  {
    protected XMLAttributes fAttributes;
    
    protected AttributesProxy() {}
    
    public void setAttributes(XMLAttributes paramXMLAttributes)
    {
      fAttributes = paramXMLAttributes;
    }
    
    public int getLength()
    {
      return fAttributes.getLength();
    }
    
    public String getName(int paramInt)
    {
      return fAttributes.getQName(paramInt);
    }
    
    public String getQName(int paramInt)
    {
      return fAttributes.getQName(paramInt);
    }
    
    public String getURI(int paramInt)
    {
      String str = fAttributes.getURI(paramInt);
      return str != null ? str : "";
    }
    
    public String getLocalName(int paramInt)
    {
      return fAttributes.getLocalName(paramInt);
    }
    
    public String getType(int paramInt)
    {
      return fAttributes.getType(paramInt);
    }
    
    public String getType(String paramString)
    {
      return fAttributes.getType(paramString);
    }
    
    public String getType(String paramString1, String paramString2)
    {
      return paramString1.length() == 0 ? fAttributes.getType(null, paramString2) : fAttributes.getType(paramString1, paramString2);
    }
    
    public String getValue(int paramInt)
    {
      return fAttributes.getValue(paramInt);
    }
    
    public String getValue(String paramString)
    {
      return fAttributes.getValue(paramString);
    }
    
    public String getValue(String paramString1, String paramString2)
    {
      return paramString1.length() == 0 ? fAttributes.getValue(null, paramString2) : fAttributes.getValue(paramString1, paramString2);
    }
    
    public int getIndex(String paramString)
    {
      return fAttributes.getIndex(paramString);
    }
    
    public int getIndex(String paramString1, String paramString2)
    {
      return paramString1.length() == 0 ? fAttributes.getIndex(null, paramString2) : fAttributes.getIndex(paramString1, paramString2);
    }
    
    public boolean isDeclared(int paramInt)
    {
      if ((paramInt < 0) || (paramInt >= fAttributes.getLength())) {
        throw new ArrayIndexOutOfBoundsException(paramInt);
      }
      return Boolean.TRUE.equals(fAttributes.getAugmentations(paramInt).getItem("ATTRIBUTE_DECLARED"));
    }
    
    public boolean isDeclared(String paramString)
    {
      int i = getIndex(paramString);
      if (i == -1) {
        throw new IllegalArgumentException(paramString);
      }
      return Boolean.TRUE.equals(fAttributes.getAugmentations(i).getItem("ATTRIBUTE_DECLARED"));
    }
    
    public boolean isDeclared(String paramString1, String paramString2)
    {
      int i = getIndex(paramString1, paramString2);
      if (i == -1) {
        throw new IllegalArgumentException(paramString2);
      }
      return Boolean.TRUE.equals(fAttributes.getAugmentations(i).getItem("ATTRIBUTE_DECLARED"));
    }
    
    public boolean isSpecified(int paramInt)
    {
      if ((paramInt < 0) || (paramInt >= fAttributes.getLength())) {
        throw new ArrayIndexOutOfBoundsException(paramInt);
      }
      return fAttributes.isSpecified(paramInt);
    }
    
    public boolean isSpecified(String paramString)
    {
      int i = getIndex(paramString);
      if (i == -1) {
        throw new IllegalArgumentException(paramString);
      }
      return fAttributes.isSpecified(i);
    }
    
    public boolean isSpecified(String paramString1, String paramString2)
    {
      int i = getIndex(paramString1, paramString2);
      if (i == -1) {
        throw new IllegalArgumentException(paramString2);
      }
      return fAttributes.isSpecified(i);
    }
  }
  
  protected static final class LocatorProxy
    implements Locator2
  {
    protected XMLLocator fLocator;
    
    public LocatorProxy(XMLLocator paramXMLLocator)
    {
      fLocator = paramXMLLocator;
    }
    
    public String getPublicId()
    {
      return fLocator.getPublicId();
    }
    
    public String getSystemId()
    {
      return fLocator.getExpandedSystemId();
    }
    
    public int getLineNumber()
    {
      return fLocator.getLineNumber();
    }
    
    public int getColumnNumber()
    {
      return fLocator.getColumnNumber();
    }
    
    public String getXMLVersion()
    {
      return fLocator.getXMLVersion();
    }
    
    public String getEncoding()
    {
      return fLocator.getEncoding();
    }
  }
}
