package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
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
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XSLProcessor
  extends MSXMLScriptable
{
  private XMLDOMNode style_;
  private XMLDOMNode input_;
  private Object output_;
  private Map<String, Object> parameters_ = new HashMap();
  

  public XSLProcessor() {}
  
  @JsxSetter
  public void setInput(XMLDOMNode input)
  {
    input_ = input;
  }
  



  @JsxGetter
  public XMLDOMNode getInput()
  {
    return input_;
  }
  



  @JsxSetter
  public void setOutput(Object output)
  {
    output_ = output;
  }
  



  @JsxGetter
  public Object getOutput()
  {
    return output_;
  }
  


  @JsxFunction
  public void addParameter(String baseName, Object parameter, Object namespaceURI)
  {
    String nsString;
    

    String nsString;
    

    if ((namespaceURI instanceof String)) {
      nsString = (String)namespaceURI;
    }
    else {
      nsString = null;
    }
    parameters_.put(getQualifiedName(nsString, baseName), parameter);
  }
  
  private static String getQualifiedName(String namespaceURI, String localName) { String qualifiedName;
    String qualifiedName;
    if ((namespaceURI != null) && (!namespaceURI.isEmpty()) && (!"null".equals(namespaceURI))) {
      qualifiedName = '{' + namespaceURI + '}' + localName;
    }
    else {
      qualifiedName = localName;
    }
    return qualifiedName;
  }
  


  @JsxFunction
  public void transform()
  {
    XMLDOMNode input = input_;
    SgmlPage page = input.getDomNodeOrDie().getPage();
    
    if ((output_ == null) || (!(output_ instanceof XMLDOMNode))) {
      DomDocumentFragment fragment = page.createDocumentFragment();
      XMLDOMDocumentFragment node = new XMLDOMDocumentFragment();
      node.setParentScope(getParentScope());
      node.setPrototype(getPrototype(node.getClass()));
      node.setDomNode(fragment);
      output_ = fragment.getScriptableObject();
    }
    
    transform(input_, ((XMLDOMNode)output_).getDomNodeOrDie());
    XMLSerializer serializer = new XMLSerializer(false);
    StringBuilder output = new StringBuilder();
    for (DomNode child : ((XMLDOMNode)output_).getDomNodeOrDie().getChildren()) {
      if ((child instanceof DomText))
      {


        if (StringUtils.isNotBlank(((DomText)child).getData())) {
          output.append(((DomText)child).getData());
        }
      }
      else
      {
        String serializedString = 
          serializer.serializeToString((XMLDOMNode)child.getScriptableObject());
        output.append(serializedString, 0, serializedString.length() - 2);
      }
    }
    output_ = output.toString();
  }
  

  private Object transform(XMLDOMNode source)
  {
    try
    {
      Source xmlSource = new DOMSource(source.getDomNodeOrDie());
      Source xsltSource = new DOMSource(style_.getDomNodeOrDie());
      
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      Document containerDocument = factory.newDocumentBuilder().newDocument();
      Element containerElement = containerDocument.createElement("container");
      containerDocument.appendChild(containerElement);
      
      DOMResult result = new DOMResult(containerElement);
      
      Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
      for (Map.Entry<String, Object> entry : parameters_.entrySet()) {
        transformer.setParameter((String)entry.getKey(), entry.getValue());
      }
      transformer.transform(xmlSource, result);
      
      Node transformedNode = result.getNode();
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
  
  private void transform(XMLDOMNode source, DomNode parent) {
    Object result = transform(source);
    if ((result instanceof Node)) {
      SgmlPage parentPage = parent.getPage();
      NodeList children = ((Node)result).getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        XmlUtil.appendChild(parentPage, parent, children.item(i), false);
      }
    }
    else {
      DomText text = new DomText(parent.getPage(), (String)result);
      parent.appendChild(text);
    }
  }
  







  public void importStylesheet(XMLDOMNode style)
  {
    style_ = style;
  }
}
