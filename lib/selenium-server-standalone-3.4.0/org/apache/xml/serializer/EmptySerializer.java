package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;













































public class EmptySerializer
  implements SerializationHandler
{
  protected static final String ERR = "EmptySerializer method not over-ridden";
  
  public EmptySerializer() {}
  
  protected void couldThrowIOException()
    throws IOException
  {}
  
  protected void couldThrowSAXException()
    throws SAXException
  {}
  
  protected void couldThrowSAXException(char[] chars, int off, int len)
    throws SAXException
  {}
  
  protected void couldThrowSAXException(String elemQName)
    throws SAXException
  {}
  
  protected void couldThrowException()
    throws Exception
  {}
  
  void aMethodIsCalled() {}
  
  public ContentHandler asContentHandler()
    throws IOException
  {
    couldThrowIOException();
    return null;
  }
  


  public void setContentHandler(ContentHandler ch)
  {
    aMethodIsCalled();
  }
  


  public void close()
  {
    aMethodIsCalled();
  }
  


  public Properties getOutputFormat()
  {
    aMethodIsCalled();
    return null;
  }
  


  public OutputStream getOutputStream()
  {
    aMethodIsCalled();
    return null;
  }
  


  public Writer getWriter()
  {
    aMethodIsCalled();
    return null;
  }
  


  public boolean reset()
  {
    aMethodIsCalled();
    return false;
  }
  

  public void serialize(Node node)
    throws IOException
  {
    couldThrowIOException();
  }
  


  public void setCdataSectionElements(Vector URI_and_localNames)
  {
    aMethodIsCalled();
  }
  

  public boolean setEscaping(boolean escape)
    throws SAXException
  {
    couldThrowSAXException();
    return false;
  }
  


  public void setIndent(boolean indent)
  {
    aMethodIsCalled();
  }
  


  public void setIndentAmount(int spaces)
  {
    aMethodIsCalled();
  }
  


  public void setOutputFormat(Properties format)
  {
    aMethodIsCalled();
  }
  


  public void setOutputStream(OutputStream output)
  {
    aMethodIsCalled();
  }
  


  public void setVersion(String version)
  {
    aMethodIsCalled();
  }
  


  public void setWriter(Writer writer)
  {
    aMethodIsCalled();
  }
  


  public void setTransformer(Transformer transformer)
  {
    aMethodIsCalled();
  }
  


  public Transformer getTransformer()
  {
    aMethodIsCalled();
    return null;
  }
  

  public void flushPending()
    throws SAXException
  {
    couldThrowSAXException();
  }
  








  public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void addAttributes(Attributes atts)
    throws SAXException
  {
    couldThrowSAXException();
  }
  


  public void addAttribute(String name, String value)
  {
    aMethodIsCalled();
  }
  


  public void characters(String chars)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void endElement(String elemName)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void startDocument()
    throws SAXException
  {
    couldThrowSAXException();
  }
  


  public void startElement(String uri, String localName, String qName)
    throws SAXException
  {
    couldThrowSAXException(qName);
  }
  

  public void startElement(String qName)
    throws SAXException
  {
    couldThrowSAXException(qName);
  }
  


  public void namespaceAfterStartElement(String uri, String prefix)
    throws SAXException
  {
    couldThrowSAXException();
  }
  





  public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush)
    throws SAXException
  {
    couldThrowSAXException();
    return false;
  }
  

  public void entityReference(String entityName)
    throws SAXException
  {
    couldThrowSAXException();
  }
  


  public NamespaceMappings getNamespaceMappings()
  {
    aMethodIsCalled();
    return null;
  }
  


  public String getPrefix(String uri)
  {
    aMethodIsCalled();
    return null;
  }
  


  public String getNamespaceURI(String name, boolean isElement)
  {
    aMethodIsCalled();
    return null;
  }
  


  public String getNamespaceURIFromPrefix(String prefix)
  {
    aMethodIsCalled();
    return null;
  }
  


  public void setDocumentLocator(Locator arg0)
  {
    aMethodIsCalled();
  }
  

  public void endDocument()
    throws SAXException
  {
    couldThrowSAXException();
  }
  


  public void startPrefixMapping(String arg0, String arg1)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void endPrefixMapping(String arg0)
    throws SAXException
  {
    couldThrowSAXException();
  }
  






  public void startElement(String arg0, String arg1, String arg2, Attributes arg3)
    throws SAXException
  {
    couldThrowSAXException();
  }
  


  public void endElement(String arg0, String arg1, String arg2)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void characters(char[] arg0, int arg1, int arg2)
    throws SAXException
  {
    couldThrowSAXException(arg0, arg1, arg2);
  }
  


  public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
    throws SAXException
  {
    couldThrowSAXException();
  }
  


  public void processingInstruction(String arg0, String arg1)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void skippedEntity(String arg0)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void comment(String comment)
    throws SAXException
  {
    couldThrowSAXException();
  }
  


  public void startDTD(String arg0, String arg1, String arg2)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void endDTD()
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void startEntity(String arg0)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void endEntity(String arg0)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void startCDATA()
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void endCDATA()
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void comment(char[] arg0, int arg1, int arg2)
    throws SAXException
  {
    couldThrowSAXException();
  }
  


  public String getDoctypePublic()
  {
    aMethodIsCalled();
    return null;
  }
  


  public String getDoctypeSystem()
  {
    aMethodIsCalled();
    return null;
  }
  


  public String getEncoding()
  {
    aMethodIsCalled();
    return null;
  }
  


  public boolean getIndent()
  {
    aMethodIsCalled();
    return false;
  }
  


  public int getIndentAmount()
  {
    aMethodIsCalled();
    return 0;
  }
  


  public String getMediaType()
  {
    aMethodIsCalled();
    return null;
  }
  


  public boolean getOmitXMLDeclaration()
  {
    aMethodIsCalled();
    return false;
  }
  


  public String getStandalone()
  {
    aMethodIsCalled();
    return null;
  }
  


  public String getVersion()
  {
    aMethodIsCalled();
    return null;
  }
  

  public void setCdataSectionElements(Hashtable h)
    throws Exception
  {
    couldThrowException();
  }
  


  public void setDoctype(String system, String pub)
  {
    aMethodIsCalled();
  }
  


  public void setDoctypePublic(String doctype)
  {
    aMethodIsCalled();
  }
  


  public void setDoctypeSystem(String doctype)
  {
    aMethodIsCalled();
  }
  


  public void setEncoding(String encoding)
  {
    aMethodIsCalled();
  }
  


  public void setMediaType(String mediatype)
  {
    aMethodIsCalled();
  }
  


  public void setOmitXMLDeclaration(boolean b)
  {
    aMethodIsCalled();
  }
  


  public void setStandalone(String standalone)
  {
    aMethodIsCalled();
  }
  

  public void elementDecl(String arg0, String arg1)
    throws SAXException
  {
    couldThrowSAXException();
  }
  







  public void attributeDecl(String arg0, String arg1, String arg2, String arg3, String arg4)
    throws SAXException
  {
    couldThrowSAXException();
  }
  


  public void internalEntityDecl(String arg0, String arg1)
    throws SAXException
  {
    couldThrowSAXException();
  }
  


  public void externalEntityDecl(String arg0, String arg1, String arg2)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void warning(SAXParseException arg0)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void error(SAXParseException arg0)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void fatalError(SAXParseException arg0)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public DOMSerializer asDOMSerializer()
    throws IOException
  {
    couldThrowIOException();
    return null;
  }
  


  public void setNamespaceMappings(NamespaceMappings mappings)
  {
    aMethodIsCalled();
  }
  



  public void setSourceLocator(SourceLocator locator)
  {
    aMethodIsCalled();
  }
  



  public void addUniqueAttribute(String name, String value, int flags)
    throws SAXException
  {
    couldThrowSAXException();
  }
  


  public void characters(Node node)
    throws SAXException
  {
    couldThrowSAXException();
  }
  



  public void addXSLAttribute(String qName, String value, String uri)
  {
    aMethodIsCalled();
  }
  


  public void addAttribute(String uri, String localName, String rawName, String type, String value)
    throws SAXException
  {
    couldThrowSAXException();
  }
  

  public void notationDecl(String arg0, String arg1, String arg2)
    throws SAXException
  {
    couldThrowSAXException();
  }
  






  public void unparsedEntityDecl(String arg0, String arg1, String arg2, String arg3)
    throws SAXException
  {
    couldThrowSAXException();
  }
  


  public void setDTDEntityExpansion(boolean expand)
  {
    aMethodIsCalled();
  }
  

  public String getOutputProperty(String name)
  {
    aMethodIsCalled();
    return null;
  }
  
  public String getOutputPropertyDefault(String name) {
    aMethodIsCalled();
    return null;
  }
  
  public void setOutputProperty(String name, String val) {
    aMethodIsCalled();
  }
  
  public void setOutputPropertyDefault(String name, String val)
  {
    aMethodIsCalled();
  }
  



  public Object asDOM3Serializer()
    throws IOException
  {
    couldThrowIOException();
    return null;
  }
}
