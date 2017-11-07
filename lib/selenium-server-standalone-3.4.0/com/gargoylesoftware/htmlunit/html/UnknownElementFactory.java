package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
import org.xml.sax.Attributes;



























public final class UnknownElementFactory
  implements ElementFactory
{
  public static final UnknownElementFactory instance = new UnknownElementFactory();
  



  private UnknownElementFactory() {}
  


  public HtmlElement createElement(SgmlPage page, String tagName, Attributes attributes)
  {
    String namespace = null;
    if ((page != null) && (page.isHtmlPage()) && (tagName.indexOf(':') != -1)) {
      HtmlPage htmlPage = (HtmlPage)page;
      String prefix = tagName.substring(0, tagName.indexOf(':'));
      Map<String, String> namespaces = htmlPage.getNamespaces();
      if (namespaces.containsKey(prefix)) {
        namespace = (String)namespaces.get(prefix);
      }
    }
    return createElementNS(page, namespace, tagName, attributes);
  }
  




  public HtmlElement createElementNS(SgmlPage page, String namespaceURI, String qualifiedName, Attributes attributes)
  {
    return createElementNS(page, namespaceURI, qualifiedName, attributes, false);
  }
  




  public HtmlElement createElementNS(SgmlPage page, String namespaceURI, String qualifiedName, Attributes attributes, boolean checkBrowserCompatibility)
  {
    Map<String, DomAttr> attributeMap = DefaultElementFactory.setAttributes(page, attributes);
    return new HtmlUnknownElement(page, qualifiedName, attributeMap);
  }
}
