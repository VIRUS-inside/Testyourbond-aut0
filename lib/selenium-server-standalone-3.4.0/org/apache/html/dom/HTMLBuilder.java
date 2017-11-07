package org.apache.html.dom;

import java.util.Vector;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.ProcessingInstructionImpl;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class HTMLBuilder
  implements DocumentHandler
{
  protected HTMLDocumentImpl _document;
  protected ElementImpl _current;
  private boolean _ignoreWhitespace = true;
  private boolean _done = true;
  protected Vector _preRootNodes;
  
  public HTMLBuilder() {}
  
  public void startDocument()
    throws SAXException
  {
    if (!_done) {
      throw new SAXException("HTM001 State error: startDocument fired twice on one builder.");
    }
    _document = null;
    _done = false;
  }
  
  public void endDocument()
    throws SAXException
  {
    if (_document == null) {
      throw new SAXException("HTM002 State error: document never started or missing document element.");
    }
    if (_current != null) {
      throw new SAXException("HTM003 State error: document ended before end of document element.");
    }
    _current = null;
    _done = true;
  }
  
  public synchronized void startElement(String paramString, AttributeList paramAttributeList)
    throws SAXException
  {
    if (paramString == null) {
      throw new SAXException("HTM004 Argument 'tagName' is null.");
    }
    ElementImpl localElementImpl;
    int i;
    if (_document == null)
    {
      _document = new HTMLDocumentImpl();
      localElementImpl = (ElementImpl)_document.getDocumentElement();
      _current = localElementImpl;
      if (_current == null) {
        throw new SAXException("HTM005 State error: Document.getDocumentElement returns null.");
      }
      if (_preRootNodes != null)
      {
        i = _preRootNodes.size();
        while (i-- > 0) {
          _document.insertBefore((Node)_preRootNodes.elementAt(i), localElementImpl);
        }
        _preRootNodes = null;
      }
    }
    else
    {
      if (_current == null) {
        throw new SAXException("HTM006 State error: startElement called after end of document element.");
      }
      localElementImpl = (ElementImpl)_document.createElement(paramString);
      _current.appendChild(localElementImpl);
      _current = localElementImpl;
    }
    if (paramAttributeList != null) {
      for (i = 0; i < paramAttributeList.getLength(); i++) {
        localElementImpl.setAttribute(paramAttributeList.getName(i), paramAttributeList.getValue(i));
      }
    }
  }
  
  public void endElement(String paramString)
    throws SAXException
  {
    if (_current == null) {
      throw new SAXException("HTM007 State error: endElement called with no current node.");
    }
    if (!_current.getNodeName().equalsIgnoreCase(paramString)) {
      throw new SAXException("HTM008 State error: mismatch in closing tag name " + paramString + "\n" + paramString);
    }
    if (_current.getParentNode() == _current.getOwnerDocument()) {
      _current = null;
    } else {
      _current = ((ElementImpl)_current.getParentNode());
    }
  }
  
  public void characters(String paramString)
    throws SAXException
  {
    if (_current == null) {
      throw new SAXException("HTM009 State error: character data found outside of root element.");
    }
    _current.appendChild(_document.createTextNode(paramString));
  }
  
  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    if (_current == null) {
      throw new SAXException("HTM010 State error: character data found outside of root element.");
    }
    _current.appendChild(_document.createTextNode(new String(paramArrayOfChar, paramInt1, paramInt2)));
  }
  
  public void ignorableWhitespace(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    if (!_ignoreWhitespace) {
      _current.appendChild(_document.createTextNode(new String(paramArrayOfChar, paramInt1, paramInt2)));
    }
  }
  
  public void processingInstruction(String paramString1, String paramString2)
    throws SAXException
  {
    if ((_current == null) && (_document == null))
    {
      if (_preRootNodes == null) {
        _preRootNodes = new Vector();
      }
      _preRootNodes.addElement(new ProcessingInstructionImpl(null, paramString1, paramString2));
    }
    else if ((_current == null) && (_document != null))
    {
      _document.appendChild(_document.createProcessingInstruction(paramString1, paramString2));
    }
    else
    {
      _current.appendChild(_document.createProcessingInstruction(paramString1, paramString2));
    }
  }
  
  public HTMLDocument getHTMLDocument()
  {
    return _document;
  }
  
  public void setDocumentLocator(Locator paramLocator) {}
}
