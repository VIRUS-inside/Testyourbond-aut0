package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Attr;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.dom.EventNode;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventHandler;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import org.w3c.css.sac.CSSException;










































@JsxClass(domClass=DomElement.class)
public class Element
  extends EventNode
{
  private NamedNodeMap attributes_;
  private Map<String, HTMLCollection> elementsByTagName_;
  private CSSStyleDeclaration style_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Element() {}
  
  public void setDomNode(DomNode domNode)
  {
    super.setDomNode(domNode);
    style_ = new CSSStyleDeclaration(this);
    
    setParentScope(getWindow().getDocument());
    




    DomElement htmlElt = (DomElement)domNode;
    for (DomAttr attr : htmlElt.getAttributesMap().values()) {
      String eventName = attr.getName();
      if ((eventName.length() > 2) && 
        (Character.toLowerCase(eventName.charAt(0)) == 'o') && 
        (Character.toLowerCase(eventName.charAt(1)) == 'n')) {
        createEventHandler(eventName, attr.getValue());
      }
    }
  }
  




  protected void createEventHandler(String eventName, String attrValue)
  {
    DomElement htmlElt = getDomNodeOrDie();
    
    BaseFunction eventHandler = new EventHandler(htmlElt, eventName, attrValue);
    setEventHandler(eventName, eventHandler);
  }
  



  @JsxGetter
  public String getTagName()
  {
    return getNodeName();
  }
  





  @JsxGetter
  public NamedNodeMap getAttributes()
  {
    if (attributes_ == null) {
      attributes_ = createAttributesObject();
    }
    return attributes_;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getBaseURI()
  {
    if (("Element".equals(getClass().getSimpleName())) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ELEMENT_BASE_URL_NULL))) {
      return null;
    }
    return getDomNodeOrDie().getPage().getUrl().toExternalForm();
  }
  



  protected NamedNodeMap createAttributesObject()
  {
    return new NamedNodeMap(getDomNodeOrDie());
  }
  







  @JsxFunction
  public String getAttribute(String attributeName, Integer flags)
  {
    String value = getDomNodeOrDie().getAttribute(attributeName);
    
    if (value == DomElement.ATTRIBUTE_NOT_DEFINED) {
      value = null;
    }
    
    return value;
  }
  





  @JsxFunction
  public void setAttribute(String name, String value)
  {
    getDomNodeOrDie().setAttribute(name, value);
  }
  




  @JsxFunction
  public HTMLCollection getElementsByTagName(String tagName)
  {
    final String tagNameLC = tagName.toLowerCase(Locale.ROOT);
    
    if (elementsByTagName_ == null) {
      elementsByTagName_ = new HashMap();
    }
    
    HTMLCollection collection = (HTMLCollection)elementsByTagName_.get(tagNameLC);
    if (collection != null) {
      return collection;
    }
    
    DomNode node = getDomNodeOrDie();
    if ("*".equals(tagName)) {
      collection = new HTMLCollection(node, false)
      {
        protected boolean isMatching(DomNode nodeToMatch) {
          return true;
        }
        
      };
    } else {
      collection = new HTMLCollection(node, false)
      {
        protected boolean isMatching(DomNode nodeToMatch) {
          return tagNameLC.equalsIgnoreCase(nodeToMatch.getNodeName());
        }
      };
    }
    
    elementsByTagName_.put(tagName, collection);
    
    return collection;
  }
  




  @JsxFunction
  public Object getAttributeNode(String name)
  {
    Map<String, DomAttr> attributes = getDomNodeOrDie().getAttributesMap();
    for (DomAttr attr : attributes.values()) {
      if (attr.getName().equals(name)) {
        return attr.getScriptableObject();
      }
    }
    return null;
  }
  






  @JsxFunction
  public Object getElementsByTagNameNS(final Object namespaceURI, final String localName)
  {
    HTMLCollection collection = new HTMLCollection(getDomNodeOrDie(), false)
    {
      protected boolean isMatching(DomNode node) {
        return (("*".equals(namespaceURI)) || (Objects.equals(namespaceURI, node.getNamespaceURI()))) && (
          ("*".equals(localName)) || (Objects.equals(localName, node.getLocalName())));
      }
      
    };
    return collection;
  }
  






  @JsxFunction
  public boolean hasAttribute(String name)
  {
    return getDomNodeOrDie().hasAttribute(name);
  }
  



  public DomElement getDomNodeOrDie()
  {
    return (DomElement)super.getDomNodeOrDie();
  }
  



  @JsxFunction
  public void removeAttribute(String name)
  {
    getDomNodeOrDie().removeAttribute(name);
  }
  




  @JsxFunction
  public ClientRect getBoundingClientRect()
  {
    if ((!getDomNodeOrDie().isAttachedToPage()) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_BOUNDINGCLIENTRECT_THROWS_IF_DISCONNECTED))) {
      throw Context.reportRuntimeError("Element is not attache to a page");
    }
    
    ClientRect textRectangle = new ClientRect(1, 1, 1, 1);
    textRectangle.setParentScope(getWindow());
    textRectangle.setPrototype(getPrototype(textRectangle.getClass()));
    return textRectangle;
  }
  



  @JsxGetter
  public int getChildElementCount()
  {
    return getDomNodeOrDie().getChildElementCount();
  }
  



  @JsxGetter
  public Element getFirstElementChild()
  {
    DomElement child = getDomNodeOrDie().getFirstElementChild();
    if (child != null) {
      return (Element)child.getScriptableObject();
    }
    return null;
  }
  



  @JsxGetter
  public Element getLastElementChild()
  {
    DomElement child = getDomNodeOrDie().getLastElementChild();
    if (child != null) {
      return (Element)child.getScriptableObject();
    }
    return null;
  }
  



  @JsxGetter
  public Element getNextElementSibling()
  {
    DomElement child = getDomNodeOrDie().getNextElementSibling();
    if (child != null) {
      return (Element)child.getScriptableObject();
    }
    return null;
  }
  



  @JsxGetter
  public Element getPreviousElementSibling()
  {
    DomElement child = getDomNodeOrDie().getPreviousElementSibling();
    if (child != null) {
      return (Element)child.getScriptableObject();
    }
    return null;
  }
  






  public Element getParentElement()
  {
    Node parent = getParent();
    while ((parent != null) && (!(parent instanceof Element))) {
      parent = parent.getParent();
    }
    return (Element)parent;
  }
  







  public void setDefaults(ComputedCSSStyleDeclaration style) {}
  






  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public HTMLCollection getChildren()
  {
    final DomElement node = getDomNodeOrDie();
    HTMLCollection collection = new HTMLCollection(node, false)
    {
      protected List<Object> computeElements() {
        List<Object> children = new LinkedList();
        for (DomNode domNode : node.getChildNodes()) {
          if ((domNode instanceof DomElement)) {
            children.add(domNode);
          }
        }
        return children;
      }
    };
    return collection;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public DOMTokenList getClassList()
  {
    return new DOMTokenList(this, "class");
  }
  





  @JsxFunction
  public String getAttributeNS(String namespaceURI, String localName)
  {
    String value = getDomNodeOrDie().getAttributeNS(namespaceURI, localName);
    if ((DomElement.ATTRIBUTE_NOT_DEFINED == value) && 
      (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ELEMENT_GET_ATTRIBUTE_RETURNS_EMPTY))) {
      return null;
    }
    return value;
  }
  








  @JsxFunction
  public boolean hasAttributeNS(String namespaceURI, String localName)
  {
    return getDomNodeOrDie().hasAttributeNS(namespaceURI, localName);
  }
  





  @JsxFunction
  public void setAttributeNS(String namespaceURI, String qualifiedName, String value)
  {
    getDomNodeOrDie().setAttributeNS(namespaceURI, qualifiedName, value);
  }
  




  @JsxFunction
  public void removeAttributeNS(String namespaceURI, String localName)
  {
    getDomNodeOrDie().removeAttributeNS(namespaceURI, localName);
  }
  



  @JsxGetter
  public CSSStyleDeclaration getStyle()
  {
    return style_;
  }
  



  @JsxSetter
  public void setStyle(String style)
  {
    if (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_ELEMENT_GET_ATTRIBUTE_RETURNS_EMPTY)) {
      getStyle().setCssText(style);
    }
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public CSSStyleDeclaration getRuntimeStyle()
  {
    return style_;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public ComputedCSSStyleDeclaration getCurrentStyle()
  {
    if (!getDomNodeOrDie().isAttachedToPage()) {
      return null;
    }
    return getWindow().getComputedStyle(this, null);
  }
  




  @JsxFunction
  public Attr setAttributeNode(Attr newAtt)
  {
    String name = newAtt.getName();
    
    NamedNodeMap nodes = getAttributes();
    Attr replacedAtt = (Attr)nodes.getNamedItemWithoutSytheticClassAttr(name);
    if (replacedAtt != null) {
      replacedAtt.detachFromParent();
    }
    
    DomAttr newDomAttr = newAtt.getDomNodeOrDie();
    getDomNodeOrDie().setAttributeNode(newDomAttr);
    return replacedAtt;
  }
  


  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public void blur()
  {
    DomNode domNode = getDomNodeOrDie();
    if ((domNode instanceof HtmlElement)) {
      ((HtmlElement)domNode).blur();
    }
  }
  



  public Object get(String name, Scriptable start)
  {
    Object response = super.get(name, start);
    


    if (((response instanceof FunctionObject)) && 
      (("querySelectorAll".equals(name)) || ("querySelector".equals(name))) && 
      (getBrowserVersion().hasFeature(BrowserVersionFeatures.QUERYSELECTORALL_NOT_IN_QUIRKS))) {
      Document doc = getWindow().getDocument();
      if (((doc instanceof HTMLDocument)) && (((HTMLDocument)doc).getDocumentMode() < 8)) {
        return NOT_FOUND;
      }
    }
    
    return response;
  }
  





  @JsxFunction
  public NodeList querySelectorAll(String selectors)
  {
    try
    {
      List<Object> nodes = new ArrayList();
      for (DomNode domNode : getDomNodeOrDie().querySelectorAll(selectors)) {
        nodes.add(domNode.getScriptableObject());
      }
      return NodeList.staticNodeList(this, nodes);
    }
    catch (CSSException e) {
      throw Context.reportRuntimeError("An invalid or illegal selector was specified (selector: '" + 
        selectors + "' error: " + e.getMessage() + ").");
    }
  }
  



  @JsxFunction
  public Node querySelector(String selectors)
  {
    try
    {
      DomNode node = getDomNodeOrDie().querySelector(selectors);
      if (node != null) {
        return (Node)node.getScriptableObject();
      }
      return null;
    }
    catch (CSSException e) {
      throw Context.reportRuntimeError("An invalid or illegal selector was specified (selector: '" + 
        selectors + "' error: " + e.getMessage() + ").");
    }
  }
}
