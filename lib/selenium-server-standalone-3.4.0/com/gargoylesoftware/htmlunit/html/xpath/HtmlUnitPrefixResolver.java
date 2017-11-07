package com.gargoylesoftware.htmlunit.html.xpath;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.xml.utils.PrefixResolverDefault;
import org.w3c.dom.Node;























final class HtmlUnitPrefixResolver
  extends PrefixResolverDefault
{
  HtmlUnitPrefixResolver(Node xpathExpressionContext)
  {
    super(xpathExpressionContext);
  }
  



  public String getNamespaceForPrefix(String prefix, Node namespaceContext)
  {
    String namespace = super.getNamespaceForPrefix(prefix, namespaceContext);
    if (namespace == null) {
      if ((namespaceContext instanceof XmlPage)) {
        DomElement documentElement = ((XmlPage)namespaceContext).getDocumentElement();
        if (documentElement != null) {
          namespace = getNamespace(documentElement, prefix);
        }
      }
      else if ((namespaceContext instanceof DomElement)) {
        namespace = getNamespace((DomElement)namespaceContext, prefix);
      }
    }
    return namespace;
  }
  
  private String getNamespace(DomElement element, String prefix) {
    Map<String, DomAttr> attributes = element.getAttributesMap();
    String xmlns = "xmlns:";
    int xmlnsLength = "xmlns:".length();
    
    for (Map.Entry<String, DomAttr> entry : attributes.entrySet()) {
      String name = (String)entry.getKey();
      if ((name.startsWith("xmlns:")) && (name.regionMatches(xmlnsLength, prefix, 0, prefix.length()))) {
        return ((DomAttr)entry.getValue()).getValue();
      }
    }
    for (DomNode child : element.getChildren()) {
      if ((child instanceof DomElement)) {
        String namespace = getNamespace((DomElement)child, prefix);
        if (namespace != null) {
          return namespace;
        }
      }
    }
    return null;
  }
}
