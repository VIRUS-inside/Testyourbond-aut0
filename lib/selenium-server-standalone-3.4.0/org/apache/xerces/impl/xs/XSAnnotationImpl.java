package org.apache.xerces.impl.xs;

import java.io.IOException;
import java.io.StringReader;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSNamespaceItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XSAnnotationImpl
  implements XSAnnotation
{
  private String fData = null;
  private SchemaGrammar fGrammar = null;
  
  public XSAnnotationImpl(String paramString, SchemaGrammar paramSchemaGrammar)
  {
    fData = paramString;
    fGrammar = paramSchemaGrammar;
  }
  
  public boolean writeAnnotation(Object paramObject, short paramShort)
  {
    if ((paramShort == 1) || (paramShort == 3))
    {
      writeToDOM((Node)paramObject, paramShort);
      return true;
    }
    if (paramShort == 2)
    {
      writeToSAX((ContentHandler)paramObject);
      return true;
    }
    return false;
  }
  
  public String getAnnotationString()
  {
    return fData;
  }
  
  public short getType()
  {
    return 12;
  }
  
  public String getName()
  {
    return null;
  }
  
  public String getNamespace()
  {
    return null;
  }
  
  public XSNamespaceItem getNamespaceItem()
  {
    return null;
  }
  
  private synchronized void writeToSAX(ContentHandler paramContentHandler)
  {
    SAXParser localSAXParser = fGrammar.getSAXParser();
    StringReader localStringReader = new StringReader(fData);
    InputSource localInputSource = new InputSource(localStringReader);
    localSAXParser.setContentHandler(paramContentHandler);
    try
    {
      localSAXParser.parse(localInputSource);
    }
    catch (SAXException localSAXException) {}catch (IOException localIOException) {}
    localSAXParser.setContentHandler(null);
  }
  
  private synchronized void writeToDOM(Node paramNode, short paramShort)
  {
    Document localDocument1 = paramShort == 1 ? paramNode.getOwnerDocument() : (Document)paramNode;
    DOMParser localDOMParser = fGrammar.getDOMParser();
    StringReader localStringReader = new StringReader(fData);
    InputSource localInputSource = new InputSource(localStringReader);
    try
    {
      localDOMParser.parse(localInputSource);
    }
    catch (SAXException localSAXException) {}catch (IOException localIOException) {}
    Document localDocument2 = localDOMParser.getDocument();
    localDOMParser.dropDocumentReferences();
    Element localElement = localDocument2.getDocumentElement();
    Node localNode = null;
    if ((localDocument1 instanceof CoreDocumentImpl))
    {
      localNode = localDocument1.adoptNode(localElement);
      if (localNode == null) {
        localNode = localDocument1.importNode(localElement, true);
      }
    }
    else
    {
      localNode = localDocument1.importNode(localElement, true);
    }
    paramNode.insertBefore(localNode, paramNode.getFirstChild());
  }
}
