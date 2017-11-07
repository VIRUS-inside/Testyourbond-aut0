package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import org.w3c.dom.NamedNodeMap;




























public class XMLSerializer
{
  private boolean preserveWhiteSpace_;
  
  public XMLSerializer(boolean preserveWhiteSpace)
  {
    preserveWhiteSpace_ = preserveWhiteSpace;
  }
  




  public String serializeToString(XMLDOMNode root)
  {
    if (root == null) {
      return "";
    }
    if ((root instanceof XMLDOMDocument)) {
      root = ((XMLDOMDocument)root).getDocumentElement();
    }
    else if ((root instanceof XMLDOMDocumentFragment)) {
      root = root.getFirstChild();
    }
    if ((root instanceof XMLDOMElement)) {
      StringBuilder builder = new StringBuilder();
      DomNode node = root.getDomNodeOrDie();
      
      toXml(1, node, builder);
      
      builder.append("\r\n");
      return builder.toString();
    }
    return root.getDomNodeOrDie().asXml();
  }
  
  private void toXml(int indent, DomNode node, StringBuilder builder)
  {
    String nodeName = node.getNodeName();
    builder.append('<').append(nodeName);
    
    String optionalPrefix = "";
    String namespaceURI = node.getNamespaceURI();
    String prefix = node.getPrefix();
    if ((namespaceURI != null) && (prefix != null)) {
      boolean sameNamespace = false;
      for (DomNode parentNode = node.getParentNode(); (parentNode instanceof DomElement); 
          parentNode = parentNode.getParentNode()) {
        if (namespaceURI.equals(parentNode.getNamespaceURI())) {
          sameNamespace = true;
        }
      }
      if ((node.getParentNode() == null) || (!sameNamespace)) {
        ((DomElement)node).setAttribute("xmlns:" + prefix, namespaceURI);
      }
    }
    
    NamedNodeMap attributesMap = node.getAttributes();
    for (int i = 0; i < attributesMap.getLength(); i++) {
      DomAttr attrib = (DomAttr)attributesMap.item(i);
      builder.append(' ').append(attrib.getQualifiedName()).append('=')
        .append('"').append(attrib.getValue()).append('"');
    }
    boolean startTagClosed = false;
    for (DomNode child : node.getChildren()) {
      if (!startTagClosed) {
        builder.append("").append('>');
        startTagClosed = true;
      }
      switch (child.getNodeType()) {
      case 1: 
        toXml(indent + 1, child, builder);
        break;
      
      case 3: 
        String value = child.getNodeValue();
        value = com.gargoylesoftware.htmlunit.util.StringUtils.escapeXmlChars(value);
        if (preserveWhiteSpace_) {
          builder.append(value.replace("\n", "\r\n"));
        }
        else if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
          builder.append("\r\n");
          DomNode sibling = child.getNextSibling();
          if ((sibling != null) && (sibling.getNodeType() == 1)) {
            for (int i = 0; i < indent; i++) {
              builder.append('\t');
            }
          }
        }
        else {
          builder.append(value.replace("\n", "\r\n"));
        }
        break;
      
      case 4: 
      case 8: 
        if ((!preserveWhiteSpace_) && (builder.charAt(builder.length() - 1) == '\n')) {
          for (int i = 0; i < indent; i++) {
            builder.append('\t');
          }
        }
        builder.append(child.asXml());
      }
      
    }
    


    if (!startTagClosed) {
      builder.append("").append("/>");
    }
    else {
      if ((!preserveWhiteSpace_) && (builder.charAt(builder.length() - 1) == '\n')) {
        for (int i = 0; i < indent - 1; i++) {
          builder.append('\t');
        }
      }
      builder.append('<').append('/').append(nodeName).append('>');
    }
  }
}
