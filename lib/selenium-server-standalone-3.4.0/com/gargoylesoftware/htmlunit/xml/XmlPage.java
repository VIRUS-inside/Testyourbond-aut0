package com.gargoylesoftware.htmlunit.xml;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomProcessingInstruction;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
























public class XmlPage
  extends SgmlPage
{
  private static final Log LOG = LogFactory.getLog(XmlPage.class);
  



  private Node node_;
  




  public XmlPage(WebResponse webResponse, WebWindow enclosingWindow)
    throws IOException
  {
    this(webResponse, enclosingWindow, true);
  }
  







  public XmlPage(Node node, WebWindow enclosingWindow)
  {
    super(null, enclosingWindow);
    node_ = node;
    if (node_ != null) {
      XmlUtil.appendChild(this, this, node_, true);
    }
  }
  









  public XmlPage(WebResponse webResponse, WebWindow enclosingWindow, boolean ignoreSAXException)
    throws IOException
  {
    this(webResponse, enclosingWindow, ignoreSAXException, true);
  }
  











  public XmlPage(WebResponse webResponse, WebWindow enclosingWindow, boolean ignoreSAXException, boolean handleXHTMLAsHTML)
    throws IOException
  {
    super(webResponse, enclosingWindow);
    try
    {
      try {
        Document document = XmlUtil.buildDocument(webResponse);
        node_ = document.getFirstChild();
      }
      catch (SAXException e) {
        LOG.warn("Failed parsing XML document " + webResponse.getWebRequest().getUrl() + 
          ": " + e.getMessage());
        if (ignoreSAXException) break label168; }
      throw new IOException(e.getMessage());

    }
    catch (ParserConfigurationException e)
    {
      if (webResponse == null) {
        LOG.warn("Failed parsing XML empty document: " + e.getMessage());
      }
      else {
        LOG.warn("Failed parsing XML empty document " + webResponse.getWebRequest().getUrl() + 
          ": " + e.getMessage());
      }
      label168:
      Map<Integer, List<String>> attributesOrderMap;
      Map<Integer, List<String>> attributesOrderMap;
      if ((node_ != null) && (getWebClient().getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML))) {
        attributesOrderMap = XmlUtil.getAttributesOrderMap(node_.getOwnerDocument());
      }
      else {
        attributesOrderMap = null;
      }
      for (Node node = node_; node != null; node = node.getNextSibling()) {
        XmlUtil.appendChild(this, this, node, handleXHTMLAsHTML, attributesOrderMap);
      }
    }
  }
  


  public boolean hasCaseSensitiveTagNames()
  {
    return true;
  }
  



  public Document getXmlDocument()
  {
    if (node_ != null) {
      return node_.getOwnerDocument();
    }
    return null;
  }
  




  public Node adoptNode(Node source)
  {
    throw new UnsupportedOperationException("XmlPage.adoptNode is not yet implemented.");
  }
  




  public Attr createAttributeNS(String namespaceURI, String qualifiedName)
  {
    throw new UnsupportedOperationException("XmlPage.createAttributeNS is not yet implemented.");
  }
  



  public DomElement createElement(String tagName)
  {
    return createElementNS(null, tagName);
  }
  



  public DomElement createElementNS(String namespaceURI, String qualifiedName)
  {
    return new DomElement(namespaceURI, qualifiedName, this, new HashMap());
  }
  




  public EntityReference createEntityReference(String name)
  {
    throw new UnsupportedOperationException("XmlPage.createEntityReference is not yet implemented.");
  }
  



  public DomProcessingInstruction createProcessingInstruction(String target, String data)
  {
    return new DomProcessingInstruction(this, target, data);
  }
  




  public String getDocumentURI()
  {
    throw new UnsupportedOperationException("XmlPage.getDocumentURI is not yet implemented.");
  }
  




  public DOMConfiguration getDomConfig()
  {
    throw new UnsupportedOperationException("XmlPage.getDomConfig is not yet implemented.");
  }
  




  public Element getElementById(String elementId)
  {
    throw new UnsupportedOperationException("XmlPage.getElementById is not yet implemented.");
  }
  




  public DOMImplementation getImplementation()
  {
    throw new UnsupportedOperationException("XmlPage.getImplementation is not yet implemented.");
  }
  




  public String getInputEncoding()
  {
    throw new UnsupportedOperationException("XmlPage.getInputEncoding is not yet implemented.");
  }
  




  public boolean getStrictErrorChecking()
  {
    throw new UnsupportedOperationException("XmlPage.getStrictErrorChecking is not yet implemented.");
  }
  




  public String getXmlEncoding()
  {
    throw new UnsupportedOperationException("XmlPage.getXmlEncoding is not yet implemented.");
  }
  




  public boolean getXmlStandalone()
  {
    throw new UnsupportedOperationException("XmlPage.getXmlStandalone is not yet implemented.");
  }
  




  public String getXmlVersion()
  {
    throw new UnsupportedOperationException("XmlPage.getXmlVersion is not yet implemented.");
  }
  




  public Node importNode(Node importedNode, boolean deep)
  {
    throw new UnsupportedOperationException("XmlPage.importNode is not yet implemented.");
  }
  




  public Node renameNode(Node n, String namespaceURI, String qualifiedName)
  {
    throw new UnsupportedOperationException("XmlPage.renameNode is not yet implemented.");
  }
  




  public void setDocumentURI(String documentURI)
  {
    throw new UnsupportedOperationException("XmlPage.setDocumentURI is not yet implemented.");
  }
  




  public void setStrictErrorChecking(boolean strictErrorChecking)
  {
    throw new UnsupportedOperationException("XmlPage.setStrictErrorChecking is not yet implemented.");
  }
  




  public void setXmlStandalone(boolean xmlStandalone)
  {
    throw new UnsupportedOperationException("XmlPage.setXmlStandalone is not yet implemented.");
  }
  




  public void setXmlVersion(String xmlVersion)
  {
    throw new UnsupportedOperationException("XmlPage.setXmlVersion is not yet implemented.");
  }
  




  public Charset getCharset()
  {
    throw new UnsupportedOperationException("XmlPage.getCharset is not yet implemented.");
  }
  



  protected void setDocumentType(DocumentType type)
  {
    super.setDocumentType(type);
  }
}
