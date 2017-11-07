package org.xml.sax.helpers;

import java.io.IOException;
import java.util.Locale;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class XMLReaderAdapter
  implements Parser, ContentHandler
{
  XMLReader xmlReader;
  DocumentHandler documentHandler;
  AttributesAdapter qAtts;
  
  public XMLReaderAdapter()
    throws SAXException
  {
    setup(XMLReaderFactory.createXMLReader());
  }
  
  public XMLReaderAdapter(XMLReader paramXMLReader)
  {
    setup(paramXMLReader);
  }
  
  private void setup(XMLReader paramXMLReader)
  {
    if (paramXMLReader == null) {
      throw new NullPointerException("XMLReader must not be null");
    }
    xmlReader = paramXMLReader;
    qAtts = new AttributesAdapter();
  }
  
  public void setLocale(Locale paramLocale)
    throws SAXException
  {
    throw new SAXNotSupportedException("setLocale not supported");
  }
  
  public void setEntityResolver(EntityResolver paramEntityResolver)
  {
    xmlReader.setEntityResolver(paramEntityResolver);
  }
  
  public void setDTDHandler(DTDHandler paramDTDHandler)
  {
    xmlReader.setDTDHandler(paramDTDHandler);
  }
  
  public void setDocumentHandler(DocumentHandler paramDocumentHandler)
  {
    documentHandler = paramDocumentHandler;
  }
  
  public void setErrorHandler(ErrorHandler paramErrorHandler)
  {
    xmlReader.setErrorHandler(paramErrorHandler);
  }
  
  public void parse(String paramString)
    throws IOException, SAXException
  {
    parse(new InputSource(paramString));
  }
  
  public void parse(InputSource paramInputSource)
    throws IOException, SAXException
  {
    setupXMLReader();
    xmlReader.parse(paramInputSource);
  }
  
  private void setupXMLReader()
    throws SAXException
  {
    xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
    try
    {
      xmlReader.setFeature("http://xml.org/sax/features/namespaces", false);
    }
    catch (SAXException localSAXException) {}
    xmlReader.setContentHandler(this);
  }
  
  public void setDocumentLocator(Locator paramLocator)
  {
    if (documentHandler != null) {
      documentHandler.setDocumentLocator(paramLocator);
    }
  }
  
  public void startDocument()
    throws SAXException
  {
    if (documentHandler != null) {
      documentHandler.startDocument();
    }
  }
  
  public void endDocument()
    throws SAXException
  {
    if (documentHandler != null) {
      documentHandler.endDocument();
    }
  }
  
  public void startPrefixMapping(String paramString1, String paramString2) {}
  
  public void endPrefixMapping(String paramString) {}
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    if (documentHandler != null)
    {
      qAtts.setAttributes(paramAttributes);
      documentHandler.startElement(paramString3, qAtts);
    }
  }
  
  public void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    if (documentHandler != null) {
      documentHandler.endElement(paramString3);
    }
  }
  
  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    if (documentHandler != null) {
      documentHandler.characters(paramArrayOfChar, paramInt1, paramInt2);
    }
  }
  
  public void ignorableWhitespace(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    if (documentHandler != null) {
      documentHandler.ignorableWhitespace(paramArrayOfChar, paramInt1, paramInt2);
    }
  }
  
  public void processingInstruction(String paramString1, String paramString2)
    throws SAXException
  {
    if (documentHandler != null) {
      documentHandler.processingInstruction(paramString1, paramString2);
    }
  }
  
  public void skippedEntity(String paramString)
    throws SAXException
  {}
  
  final class AttributesAdapter
    implements AttributeList
  {
    private Attributes attributes;
    
    AttributesAdapter() {}
    
    void setAttributes(Attributes paramAttributes)
    {
      attributes = paramAttributes;
    }
    
    public int getLength()
    {
      return attributes.getLength();
    }
    
    public String getName(int paramInt)
    {
      return attributes.getQName(paramInt);
    }
    
    public String getType(int paramInt)
    {
      return attributes.getType(paramInt);
    }
    
    public String getValue(int paramInt)
    {
      return attributes.getValue(paramInt);
    }
    
    public String getType(String paramString)
    {
      return attributes.getType(paramString);
    }
    
    public String getValue(String paramString)
    {
      return attributes.getValue(paramString);
    }
  }
}
