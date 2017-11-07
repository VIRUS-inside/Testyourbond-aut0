package com.gargoylesoftware.htmlunit.javascript.host.xml;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomCDataSection;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.dom.CDATASection;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentFragment;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.w3c.dom.NamedNodeMap;



























@JsxClass
public class XMLSerializer
  extends SimpleScriptable
{
  private static final Set<String> NON_EMPTY_TAGS = new HashSet(Arrays.asList(new String[] {
    "abbr", "acronym", 
    "a", "applet", "address", "audio", 
    "bgsound", 
    "bdo", "big", "blink", 
    "blockquote", "body", "b", 
    "button", "canvas", "caption", 
    "center", "cite", "code", 
    "dfn", "dd", 
    "del", "dir", 
    "div", 
    "dl", 
    "dt", "embed", 
    "em", "fieldset", 
    "font", "form", 
    "frame", "frameset", "h1", 
    "h2", "h3", 
    "h4", "h5", 
    "h6", "head", 
    "html", "iframe", 
    "ins", "isindex", 
    "i", "kbd", "label", 
    "legend", "listing", "li", 
    "map", "marquee", 
    "menu", "multicol", 
    "nobr", "noembed", "noframes", 
    "noscript", "object", "ol", 
    "optgroup", "option", "p", 
    "plaintext", "pre", 
    "q", "s", "samp", 
    "script", "select", "small", 
    "source", "span", 
    "strike", "strong", "style", 
    "sub", "sup", "title", 
    "table", "col", "colgroup", 
    "tbody", "td", "th", 
    "tr", "textarea", "tfoot", 
    "thead", "tt", "u", 
    "ul", "var", "video", 
    "wbr", "xmp" }));
  





  @JsxConstructor
  public XMLSerializer() {}
  





  @JsxFunction
  public String serializeToString(Node root)
  {
    if (root == null) {
      return "";
    }
    if ((root instanceof Document)) {
      root = ((Document)root).getDocumentElement();
    }
    else if ((root instanceof DocumentFragment)) {
      if (((root.getOwnerDocument() instanceof HTMLDocument)) && 
        (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_SERIALIZER_HTML_DOCUMENT_FRAGMENT_ALWAYS_EMPTY))) {
        return "";
      }
      root = root.getFirstChild();
    }
    if ((root instanceof Element)) {
      StringBuilder builder = new StringBuilder();
      DomNode node = root.getDomNodeOrDie();
      SgmlPage page = node.getPage();
      boolean isHtmlPage = (page != null) && (page.isHtmlPage());
      
      String forcedNamespace = null;
      if (isHtmlPage) {
        forcedNamespace = "http://www.w3.org/1999/xhtml";
      }
      toXml(1, node, builder, forcedNamespace);
      
      return builder.toString();
    }
    if (((root instanceof CDATASection)) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_SERIALIZER_ROOT_CDATA_AS_ESCAPED_TEXT))) {
      DomCDataSection domCData = (DomCDataSection)root.getDomNodeOrDie();
      String data = domCData.getData();
      if (org.apache.commons.lang3.StringUtils.isNotBlank(data)) {
        return com.gargoylesoftware.htmlunit.util.StringUtils.escapeXmlChars(data);
      }
    }
    return root.getDomNodeOrDie().asXml();
  }
  
  private void toXml(int indent, DomNode node, StringBuilder builder, String foredNamespace)
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
    else if (foredNamespace != null) {
      builder.append(" xmlns=\"").append(foredNamespace).append('"');
      optionalPrefix = " ";
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
        builder.append(optionalPrefix).append('>');
        startTagClosed = true;
      }
      switch (child.getNodeType()) {
      case 1: 
        toXml(indent + 1, child, builder, null);
        break;
      
      case 3: 
        String value = child.getNodeValue();
        value = com.gargoylesoftware.htmlunit.util.StringUtils.escapeXmlChars(value);
        builder.append(value);
        break;
      
      case 4: 
      case 8: 
        builder.append(child.asXml());
      }
      
    }
    


    if (!startTagClosed) {
      String tagName = nodeName.toLowerCase(Locale.ROOT);
      boolean nonEmptyTagsSupported = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_SERIALIZER_NON_EMPTY_TAGS);
      if ((nonEmptyTagsSupported) && (NON_EMPTY_TAGS.contains(tagName))) {
        builder.append('>');
        builder.append("</").append(nodeName).append('>');
      }
      else {
        builder.append(optionalPrefix);
        if ((builder.charAt(builder.length() - 1) != ' ') && 
          (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_SERIALIZER_BLANK_BEFORE_SELF_CLOSING))) {
          builder.append(" ");
        }
        builder.append("/>");
      }
    }
    else {
      builder.append('<').append('/').append(nodeName).append('>');
    }
  }
}
