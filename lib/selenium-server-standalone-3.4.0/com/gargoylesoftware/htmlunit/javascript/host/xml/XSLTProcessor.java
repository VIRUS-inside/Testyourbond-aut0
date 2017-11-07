package com.gargoylesoftware.htmlunit.javascript.host.xml;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentFragment;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlUtil;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class XSLTProcessor
  extends SimpleScriptable
{
  private com.gargoylesoftware.htmlunit.javascript.host.dom.Node style_;
  private Map<String, Object> parameters_ = new HashMap();
  






  @JsxConstructor
  public XSLTProcessor() {}
  





  @JsxFunction
  public void importStylesheet(com.gargoylesoftware.htmlunit.javascript.host.dom.Node style)
  {
    style_ = style;
  }
  






  @JsxFunction
  public XMLDocument transformToDocument(com.gargoylesoftware.htmlunit.javascript.host.dom.Node source)
  {
    XMLDocument doc = new XMLDocument();
    doc.setPrototype(getPrototype(doc.getClass()));
    doc.setParentScope(getParentScope());
    
    Object transformResult = transform(source);
    org.w3c.dom.Node node;
    org.w3c.dom.Node node; if ((transformResult instanceof org.w3c.dom.Node)) {
      org.w3c.dom.Node transformedDoc = (org.w3c.dom.Node)transformResult;
      node = transformedDoc.getFirstChild();
    }
    else {
      node = null;
    }
    XmlPage page = new XmlPage(node, getWindow().getWebWindow());
    doc.setDomNode(page);
    return doc;
  }
  

  private Object transform(com.gargoylesoftware.htmlunit.javascript.host.dom.Node source)
  {
    try
    {
      Source xmlSource = new DOMSource(source.getDomNodeOrDie());
      Source xsltSource = new DOMSource(style_.getDomNodeOrDie());
      
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      org.w3c.dom.Document containerDocument = 
        factory.newDocumentBuilder().newDocument();
      Element containerElement = containerDocument.createElement("container");
      containerDocument.appendChild(containerElement);
      
      DOMResult result = new DOMResult(containerElement);
      
      Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
      for (Map.Entry<String, Object> entry : parameters_.entrySet()) {
        transformer.setParameter((String)entry.getKey(), entry.getValue());
      }
      transformer.transform(xmlSource, result);
      
      org.w3c.dom.Node transformedNode = result.getNode();
      if (transformedNode.getFirstChild().getNodeType() == 1) {
        return transformedNode;
      }
      
      xmlSource = new DOMSource(source.getDomNodeOrDie());
      StringWriter writer = new StringWriter();
      Result streamResult = new StreamResult(writer);
      transformer.transform(xmlSource, streamResult);
      return writer.toString();
    }
    catch (Exception e) {
      throw Context.reportRuntimeError("Exception: " + e);
    }
  }
  






  @JsxFunction
  public DocumentFragment transformToFragment(com.gargoylesoftware.htmlunit.javascript.host.dom.Node source, Object output)
  {
    SgmlPage page = (SgmlPage)((com.gargoylesoftware.htmlunit.javascript.host.dom.Document)output).getDomNodeOrDie();
    
    DomDocumentFragment fragment = page.createDocumentFragment();
    DocumentFragment rv = new DocumentFragment();
    rv.setPrototype(getPrototype(rv.getClass()));
    rv.setParentScope(getParentScope());
    rv.setDomNode(fragment);
    
    transform(source, fragment);
    return rv;
  }
  
  private void transform(com.gargoylesoftware.htmlunit.javascript.host.dom.Node source, DomNode parent) {
    Object result = transform(source);
    if ((result instanceof org.w3c.dom.Node)) {
      SgmlPage parentPage = parent.getPage();
      NodeList children = ((org.w3c.dom.Node)result).getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        XmlUtil.appendChild(parentPage, parent, children.item(i), true);
      }
    }
    else {
      DomText text = new DomText(parent.getPage(), (String)result);
      parent.appendChild(text);
    }
  }
  






  @JsxFunction
  public void setParameter(String namespaceURI, String localName, Object value)
  {
    parameters_.put(getQualifiedName(namespaceURI, localName), value);
  }
  







  @JsxFunction
  public Object getParameter(String namespaceURI, String localName) { return parameters_.get(getQualifiedName(namespaceURI, localName)); }
  
  private static String getQualifiedName(String namespaceURI, String localName) {
    String qualifiedName;
    String qualifiedName;
    if ((namespaceURI != null) && (!namespaceURI.isEmpty()) && (!"null".equals(namespaceURI))) {
      qualifiedName = '{' + namespaceURI + '}' + localName;
    }
    else {
      qualifiedName = localName;
    }
    return qualifiedName;
  }
}
