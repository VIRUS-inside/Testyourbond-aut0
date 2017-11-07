package com.gargoylesoftware.htmlunit.javascript.host.xml;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Attr;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMException;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.svg.SvgElement;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.IOException;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.CDATASection;
import org.w3c.dom.ProcessingInstruction;






























@JsxClass
public class XMLDocument
  extends Document
{
  private static final Log LOG = LogFactory.getLog(XMLDocument.class);
  
  private boolean async_ = true;
  


  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public XMLDocument()
  {
    this(null);
  }
  



  public XMLDocument(WebWindow enclosingWindow)
  {
    if (enclosingWindow != null) {
      try {
        XmlPage page = new XmlPage(null, enclosingWindow);
        setDomNode(page);
      }
      catch (IOException e) {
        throw Context.reportRuntimeError("IOException: " + e);
      }
    }
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void setAsync(boolean async)
  {
    async_ = async;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public boolean getAsync()
  {
    return async_;
  }
  





  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public boolean load(String xmlSource)
  {
    if ((async_) && 
      (LOG.isDebugEnabled())) {
      LOG.debug("XMLDocument.load(): 'async' is true, currently treated as false.");
    }
    try
    {
      HtmlPage htmlPage = (HtmlPage)getWindow().getWebWindow().getEnclosedPage();
      WebRequest request = new WebRequest(htmlPage.getFullyQualifiedUrl(xmlSource));
      WebResponse webResponse = getWindow().getWebWindow().getWebClient().loadWebResponse(request);
      XmlPage page = new XmlPage(webResponse, getWindow().getWebWindow(), false);
      setDomNode(page);
      return true;
    }
    catch (IOException e) {
      if (LOG.isDebugEnabled())
        LOG.debug("Error parsing XML from '" + xmlSource + "'", e);
    }
    return false;
  }
  







  public boolean loadXML(String strXML)
  {
    WebWindow webWindow = getWindow().getWebWindow();
    try {
      if ((StringUtils.isEmpty(strXML)) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMPARSER_EMPTY_STRING_IS_ERROR))) {
        throw new IOException("Error parsing XML '" + strXML + "'");
      }
      
      WebResponse webResponse = new StringWebResponse(strXML, webWindow.getEnclosedPage().getUrl());
      
      XmlPage page = new XmlPage(webResponse, webWindow, false);
      setDomNode(page);
      return true;
    }
    catch (IOException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Error parsing XML\n" + strXML, e);
      }
      
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMPARSER_EXCEPTION_ON_ERROR)) {
        throw asJavaScriptException(
          new DOMException("Syntax Error", 
          (short)12));
      }
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOMPARSER_PARSERERROR_ON_ERROR)) {
        try {
          XmlPage page = createParserErrorXmlPage("Syntax Error", webWindow);
          setDomNode(page);
        }
        catch (IOException eI) {
          LOG.error("Could not handle ParserError", e);
        }
      }
    }
    return false;
  }
  
  private static XmlPage createParserErrorXmlPage(String message, WebWindow webWindow)
    throws IOException
  {
    String xml = "<parsererror xmlns=\"http://www.mozilla.org/newlayout/xml/parsererror.xml\">\n" + 
      message + "\n" + 
      "<sourcetext></sourcetext>\n" + 
      "</parsererror>";
    
    WebResponse webResponse = new StringWebResponse(xml, webWindow.getEnclosedPage().getUrl());
    
    return new XmlPage(webResponse, webWindow, false);
  }
  



  public SimpleScriptable makeScriptableFor(DomNode domNode)
  {
    SimpleScriptable scriptable;
    

    if (((domNode instanceof DomElement)) && (!(domNode instanceof HtmlElement))) {
      if ((domNode instanceof SvgElement)) {
        Class<? extends HtmlUnitScriptable> javaScriptClass = getWindow().getWebWindow().getWebClient()
          .getJavaScriptEngine().getJavaScriptClass(domNode.getClass());
        try {
          scriptable = (SimpleScriptable)javaScriptClass.newInstance();
        } catch (Exception e) {
          SimpleScriptable scriptable;
          throw Context.throwAsScriptRuntimeEx(e);
        }
      }
      else {
        scriptable = new Element();
      }
    } else { SimpleScriptable scriptable;
      if ((domNode instanceof DomAttr)) {
        scriptable = new Attr();
      }
      else
        return super.makeScriptableFor(domNode);
    }
    SimpleScriptable scriptable;
    scriptable.setPrototype(getPrototype(scriptable.getClass()));
    scriptable.setParentScope(getParentScope());
    scriptable.setDomNode(domNode);
    return scriptable;
  }
  



  protected void initParentScope(DomNode domNode, SimpleScriptable scriptable)
  {
    scriptable.setParentScope(getParentScope());
  }
  



  @JsxFunction
  public HTMLCollection getElementsByTagName(final String tagName)
  {
    DomNode firstChild = getDomNodeOrDie().getFirstChild();
    if (firstChild == null) {
      return HTMLCollection.emptyCollection(getWindow().getDomNodeOrDie());
    }
    
    HTMLCollection collection = new HTMLCollection(getDomNodeOrDie(), false) {
      protected boolean isMatching(DomNode node) {
        String nodeName;
        String nodeName;
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_GET_ELEMENTS_BY_TAG_NAME_LOCAL)) {
          nodeName = node.getLocalName();
        }
        else {
          nodeName = node.getNodeName();
        }
        
        return nodeName.equals(tagName);
      }
      
    };
    return collection;
  }
  




  @JsxFunction
  public Object getElementById(String id)
  {
    DomNode domNode = getDomNodeOrDie();
    Object domElement = domNode.getFirstByXPath("//*[@id = \"" + id + "\"]");
    if (domElement != null) {
      if ((!(domNode instanceof XmlPage)) || ((domElement instanceof HtmlElement)) || 
        (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_GET_ELEMENT_BY_ID__ANY_ELEMENT))) {
        return ((DomElement)domElement).getScriptableObject();
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("getElementById(" + id + "): no HTML DOM node found with this ID");
      }
    }
    return null;
  }
  





  @JsxFunction
  public Object createProcessingInstruction(String target, String data)
  {
    ProcessingInstruction node = getPage().createProcessingInstruction(target, data);
    return getScriptableFor(node);
  }
  




  @JsxFunction
  public Object createCDATASection(String data)
  {
    CDATASection node = getPage().createCDATASection(data);
    return getScriptableFor(node);
  }
}
