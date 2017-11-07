package org.xml.sax.helpers;

import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

public class XMLFilterImpl
  implements XMLFilter, EntityResolver, DTDHandler, ContentHandler, ErrorHandler
{
  private XMLReader parent = null;
  private Locator locator = null;
  private EntityResolver entityResolver = null;
  private DTDHandler dtdHandler = null;
  private ContentHandler contentHandler = null;
  private ErrorHandler errorHandler = null;
  
  public XMLFilterImpl() {}
  
  public XMLFilterImpl(XMLReader paramXMLReader)
  {
    setParent(paramXMLReader);
  }
  
  public void setParent(XMLReader paramXMLReader)
  {
    parent = paramXMLReader;
  }
  
  public XMLReader getParent()
  {
    return parent;
  }
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (parent != null) {
      parent.setFeature(paramString, paramBoolean);
    } else {
      throw new SAXNotRecognizedException("Feature: " + paramString);
    }
  }
  
  public boolean getFeature(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (parent != null) {
      return parent.getFeature(paramString);
    }
    throw new SAXNotRecognizedException("Feature: " + paramString);
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (parent != null) {
      parent.setProperty(paramString, paramObject);
    } else {
      throw new SAXNotRecognizedException("Property: " + paramString);
    }
  }
  
  public Object getProperty(String paramString)
    throws SAXNotRecognizedException, SAXNotSupportedException
  {
    if (parent != null) {
      return parent.getProperty(paramString);
    }
    throw new SAXNotRecognizedException("Property: " + paramString);
  }
  
  public void setEntityResolver(EntityResolver paramEntityResolver)
  {
    entityResolver = paramEntityResolver;
  }
  
  public EntityResolver getEntityResolver()
  {
    return entityResolver;
  }
  
  public void setDTDHandler(DTDHandler paramDTDHandler)
  {
    dtdHandler = paramDTDHandler;
  }
  
  public DTDHandler getDTDHandler()
  {
    return dtdHandler;
  }
  
  public void setContentHandler(ContentHandler paramContentHandler)
  {
    contentHandler = paramContentHandler;
  }
  
  public ContentHandler getContentHandler()
  {
    return contentHandler;
  }
  
  public void setErrorHandler(ErrorHandler paramErrorHandler)
  {
    errorHandler = paramErrorHandler;
  }
  
  public ErrorHandler getErrorHandler()
  {
    return errorHandler;
  }
  
  public void parse(InputSource paramInputSource)
    throws SAXException, IOException
  {
    setupParse();
    parent.parse(paramInputSource);
  }
  
  public void parse(String paramString)
    throws SAXException, IOException
  {
    parse(new InputSource(paramString));
  }
  
  public InputSource resolveEntity(String paramString1, String paramString2)
    throws SAXException, IOException
  {
    if (entityResolver != null) {
      return entityResolver.resolveEntity(paramString1, paramString2);
    }
    return null;
  }
  
  public void notationDecl(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    if (dtdHandler != null) {
      dtdHandler.notationDecl(paramString1, paramString2, paramString3);
    }
  }
  
  public void unparsedEntityDecl(String paramString1, String paramString2, String paramString3, String paramString4)
    throws SAXException
  {
    if (dtdHandler != null) {
      dtdHandler.unparsedEntityDecl(paramString1, paramString2, paramString3, paramString4);
    }
  }
  
  public void setDocumentLocator(Locator paramLocator)
  {
    locator = paramLocator;
    if (contentHandler != null) {
      contentHandler.setDocumentLocator(paramLocator);
    }
  }
  
  public void startDocument()
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.startDocument();
    }
  }
  
  public void endDocument()
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.endDocument();
    }
  }
  
  public void startPrefixMapping(String paramString1, String paramString2)
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.startPrefixMapping(paramString1, paramString2);
    }
  }
  
  public void endPrefixMapping(String paramString)
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.endPrefixMapping(paramString);
    }
  }
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.startElement(paramString1, paramString2, paramString3, paramAttributes);
    }
  }
  
  public void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.endElement(paramString1, paramString2, paramString3);
    }
  }
  
  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.characters(paramArrayOfChar, paramInt1, paramInt2);
    }
  }
  
  public void ignorableWhitespace(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.ignorableWhitespace(paramArrayOfChar, paramInt1, paramInt2);
    }
  }
  
  public void processingInstruction(String paramString1, String paramString2)
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.processingInstruction(paramString1, paramString2);
    }
  }
  
  public void skippedEntity(String paramString)
    throws SAXException
  {
    if (contentHandler != null) {
      contentHandler.skippedEntity(paramString);
    }
  }
  
  public void warning(SAXParseException paramSAXParseException)
    throws SAXException
  {
    if (errorHandler != null) {
      errorHandler.warning(paramSAXParseException);
    }
  }
  
  public void error(SAXParseException paramSAXParseException)
    throws SAXException
  {
    if (errorHandler != null) {
      errorHandler.error(paramSAXParseException);
    }
  }
  
  public void fatalError(SAXParseException paramSAXParseException)
    throws SAXException
  {
    if (errorHandler != null) {
      errorHandler.fatalError(paramSAXParseException);
    }
  }
  
  private void setupParse()
  {
    if (parent == null) {
      throw new NullPointerException("No parent for filter");
    }
    parent.setEntityResolver(this);
    parent.setDTDHandler(this);
    parent.setContentHandler(this);
    parent.setErrorHandler(this);
  }
}
