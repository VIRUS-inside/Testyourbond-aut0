package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlKeygen;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRp;
import com.gargoylesoftware.htmlunit.html.HtmlRt;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Location;
import com.gargoylesoftware.htmlunit.javascript.host.NativeFunctionPrefixResolver;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.UIEvent;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAnchorElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeFunction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.utils.PrefixResolver;











































@JsxClass
public class Document
  extends EventNode
{
  private static final Log LOG = LogFactory.getLog(Document.class);
  private static final Pattern TAG_NAME_PATTERN = Pattern.compile("\\w+");
  

  private Window window_;
  

  private DOMImplementation implementation_;
  

  private String designMode_;
  

  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Document() {}
  

  public void setWindow(Window window)
  {
    window_ = window;
  }
  



  @JsxGetter
  public Location getLocation()
  {
    return window_.getLocation();
  }
  






  @JsxSetter
  public void setLocation(String location)
    throws IOException
  {
    Object event = getWindow().getEvent();
    boolean setLocation = true;
    if ((event instanceof UIEvent)) {
      Object target = ((UIEvent)event).getTarget();
      if ((target instanceof HTMLAnchorElement)) {
        String href = ((HTMLAnchorElement)target).getHref();
        if ((!href.isEmpty()) && 
          (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOCUMENT_SET_LOCATION_EXECUTED_IN_ANCHOR))) {
          setLocation = false;
        }
      }
    }
    if (setLocation) {
      window_.setLocation(location);
    }
  }
  



  @JsxGetter
  public String getReferrer()
  {
    String referrer = (String)getPage().getWebResponse().getWebRequest().getAdditionalHeaders().get("Referer");
    if (referrer == null) {
      return "";
    }
    return referrer;
  }
  



  @JsxGetter
  public com.gargoylesoftware.htmlunit.javascript.host.Element getDocumentElement()
  {
    Object documentElement = getPage().getDocumentElement();
    if (documentElement == null)
    {
      return null;
    }
    return (com.gargoylesoftware.htmlunit.javascript.host.Element)getScriptableFor(documentElement);
  }
  



  @JsxGetter
  public SimpleScriptable getDoctype()
  {
    Object documentType = getPage().getDoctype();
    if (documentType == null) {
      return null;
    }
    return getScriptableFor(documentType);
  }
  



  @JsxGetter
  public String getDesignMode()
  {
    if (designMode_ == null) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOCUMENT_DESIGN_MODE_INHERIT)) {
        designMode_ = "inherit";
      }
      else {
        designMode_ = "off";
      }
    }
    return designMode_;
  }
  



  @JsxSetter
  public void setDesignMode(String mode)
  {
    BrowserVersion browserVersion = getBrowserVersion();
    boolean inherit = browserVersion.hasFeature(BrowserVersionFeatures.JS_DOCUMENT_DESIGN_MODE_INHERIT);
    if (inherit) {
      if ((!"on".equalsIgnoreCase(mode)) && (!"off".equalsIgnoreCase(mode)) && (!"inherit".equalsIgnoreCase(mode))) {
        throw Context.reportRuntimeError("Invalid document.designMode value '" + mode + "'.");
      }
      
      if ("on".equalsIgnoreCase(mode)) {
        designMode_ = "on";
      }
      else if ("off".equalsIgnoreCase(mode)) {
        designMode_ = "off";
      }
      else if ("inherit".equalsIgnoreCase(mode)) {
        designMode_ = "inherit";
      }
      
    }
    else if ("on".equalsIgnoreCase(mode)) {
      designMode_ = "on";
      SgmlPage page = getPage();
      if ((page != null) && (page.isHtmlPage()) && 
        (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOCUMENT_SELECTION_RANGE_COUNT))) {
        HtmlPage htmlPage = (HtmlPage)page;
        DomNode child = htmlPage.getBody().getFirstChild();
        DomNode rangeNode = child == null ? htmlPage.getBody() : child;
        htmlPage.setSelectionRange(new SimpleRange(rangeNode, 0));
      }
    }
    else if ("off".equalsIgnoreCase(mode)) {
      designMode_ = "off";
    }
  }
  




  public SgmlPage getPage()
  {
    return (SgmlPage)getDomNodeOrDie();
  }
  



  @JsxGetter
  public Object getDefaultView()
  {
    return getWindow();
  }
  



  @JsxFunction
  public Object createDocumentFragment()
  {
    DomDocumentFragment fragment = getDomNodeOrDie().getPage().createDocumentFragment();
    DocumentFragment node = new DocumentFragment();
    node.setParentScope(getParentScope());
    node.setPrototype(getPrototype(node.getClass()));
    node.setDomNode(fragment);
    return getScriptableFor(fragment);
  }
  





  @JsxFunction
  public Attr createAttribute(String attributeName)
  {
    return (Attr)getPage().createAttribute(attributeName).getScriptableObject();
  }
  








  @JsxFunction
  public Object importNode(Node importedNode, boolean deep)
  {
    DomNode domNode = importedNode.getDomNodeOrDie();
    domNode = domNode.cloneNode(deep);
    domNode.processImportNode(this);
    for (DomNode childNode : domNode.getDescendants()) {
      childNode.processImportNode(this);
    }
    return domNode.getScriptableObject();
  }
  








  @JsxFunction
  public Object adoptNode(Node externalNode)
  {
    externalNode.remove();
    return importNode(externalNode, true);
  }
  



  @JsxGetter
  public DOMImplementation getImplementation()
  {
    if (implementation_ == null) {
      implementation_ = new DOMImplementation();
      implementation_.setParentScope(getWindow());
      implementation_.setPrototype(getPrototype(implementation_.getClass()));
    }
    return implementation_;
  }
  







  @JsxFunction
  public void captureEvents(String type) {}
  







  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public XPathNSResolver createNSResolver(Node nodeResolver)
  {
    XPathNSResolver resolver = new XPathNSResolver();
    resolver.setElement(nodeResolver);
    resolver.setParentScope(getWindow());
    resolver.setPrototype(getPrototype(resolver.getClass()));
    return resolver;
  }
  





  @JsxFunction
  public Object createTextNode(String newData)
  {
    Object result = NOT_FOUND;
    try {
      DomNode domNode = new DomText(getDomNodeOrDie().getPage(), newData);
      Object jsElement = getScriptableFor(domNode);
      
      if (jsElement == NOT_FOUND) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("createTextNode(" + newData + 
            ") cannot return a result as there isn't a JavaScript object for the DOM node " + 
            domNode.getClass().getName());
        }
      }
      else {
        result = jsElement;
      }
    }
    catch (ElementNotFoundException localElementNotFoundException) {}
    

    return result;
  }
  




  @JsxFunction
  public Object createComment(String comment)
  {
    DomNode domNode = new DomComment(getDomNodeOrDie().getPage(), comment);
    return getScriptableFor(domNode);
  }
  










  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public XPathResult evaluate(String expression, Node contextNode, Object resolver, int type, Object result)
  {
    XPathResult xPathResult = (XPathResult)result;
    if (xPathResult == null) {
      xPathResult = new XPathResult();
      xPathResult.setParentScope(getParentScope());
      xPathResult.setPrototype(getPrototype(xPathResult.getClass()));
    }
    
    PrefixResolver prefixResolver = null;
    if ((resolver instanceof NativeFunction)) {
      prefixResolver = new NativeFunctionPrefixResolver((NativeFunction)resolver, contextNode.getParentScope());
    }
    else if ((resolver instanceof PrefixResolver)) {
      prefixResolver = (PrefixResolver)resolver;
    }
    xPathResult.init(contextNode.getDomNodeOrDie().getByXPath(expression, prefixResolver), type);
    return xPathResult;
  }
  





  @JsxFunction
  public Object createElement(String tagName)
  {
    Object result = NOT_FOUND;
    try {
      BrowserVersion browserVersion = getBrowserVersion();
      
      if ((browserVersion.hasFeature(BrowserVersionFeatures.JS_DOCUMENT_CREATE_ELEMENT_STRICT)) && (
        (tagName.contains("<")) || (tagName.contains(">")))) {
        LOG.info("createElement: Provided string '" + 
          tagName + "' contains an invalid character; '<' and '>' are not allowed");
        throw Context.reportRuntimeError("String contains an invalid character");
      }
      if ((tagName.startsWith("<")) && (tagName.endsWith(">"))) {
        tagName = tagName.substring(1, tagName.length() - 1);
        
        Matcher matcher = TAG_NAME_PATTERN.matcher(tagName);
        if (!matcher.matches()) {
          LOG.info("createElement: Provided string '" + tagName + "' contains an invalid character");
          throw Context.reportRuntimeError("String contains an invalid character");
        }
      }
      
      SgmlPage page = getPage();
      org.w3c.dom.Node element = page.createElement(tagName);
      
      if ((element instanceof BaseFrameElement)) {
        ((BaseFrameElement)element).markAsCreatedByJavascript();
      }
      else if ((element instanceof HtmlInput)) {
        ((HtmlInput)element).markAsCreatedByJavascript();
      }
      else if ((element instanceof HtmlImage)) {
        ((HtmlImage)element).markAsCreatedByJavascript();
      }
      else if ((element instanceof HtmlKeygen)) {
        ((HtmlKeygen)element).markAsCreatedByJavascript();
      }
      else if ((element instanceof HtmlRp)) {
        ((HtmlRp)element).markAsCreatedByJavascript();
      }
      else if ((element instanceof HtmlRt)) {
        ((HtmlRt)element).markAsCreatedByJavascript();
      }
      else if ((element instanceof HtmlScript)) {
        ((HtmlScript)element).markAsCreatedByJavascript();
      }
      else if ((element instanceof HtmlUnknownElement)) {
        ((HtmlUnknownElement)element).markAsCreatedByJavascript();
      }
      Object jsElement = getScriptableFor(element);
      
      if (jsElement == NOT_FOUND) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("createElement(" + tagName + 
            ") cannot return a result as there isn't a JavaScript object for the element " + 
            element.getClass().getName());
        }
      }
      else {
        result = jsElement;
      }
    }
    catch (ElementNotFoundException localElementNotFoundException) {}
    

    return result;
  }
  







  @JsxFunction
  public Object createElementNS(String namespaceURI, String qualifiedName)
  {
    if ("http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul".equals(namespaceURI))
      throw Context.reportRuntimeError("XUL not available");
    org.w3c.dom.Element element;
    org.w3c.dom.Element element;
    if (("http://www.w3.org/1999/xhtml".equals(namespaceURI)) || 
      ("http://www.w3.org/2000/svg".equals(namespaceURI))) {
      element = getPage().createElementNS(namespaceURI, qualifiedName);
    }
    else {
      element = new DomElement(namespaceURI, qualifiedName, getPage(), null);
    }
    return getScriptableFor(element);
  }
  

  @JsxFunction
  public HTMLCollection getElementsByTagName(final String tagName)
  {
    HTMLCollection collection;
    
    HTMLCollection collection;
    
    if ("*".equals(tagName)) {
      collection = new HTMLCollection(getDomNodeOrDie(), false)
      {
        protected boolean isMatching(DomNode node) {
          return true;
        }
        
      };
    } else {
      collection = new HTMLCollection(getDomNodeOrDie(), false)
      {
        protected boolean isMatching(DomNode node) {
          return tagName.equalsIgnoreCase(node.getNodeName());
        }
      };
    }
    
    return collection;
  }
  






  @JsxFunction
  public Object getElementsByTagNameNS(Object namespaceURI, final String localName)
  {
    HTMLCollection collection = new HTMLCollection(getDomNodeOrDie(), false)
    {
      protected boolean isMatching(DomNode node) {
        return localName.equals(node.getLocalName());
      }
      
    };
    return collection;
  }
}
