package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomDocumentType;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.NamedNodeMap;






































@JsxClass(domClass=DomDocumentType.class)
public class DocumentType
  extends Node
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public DocumentType() {}
  
  @JsxGetter
  public String getName()
  {
    return ((DomDocumentType)getDomNodeOrDie()).getName();
  }
  



  public String getNodeName()
  {
    return getName();
  }
  



  @JsxGetter
  public String getPublicId()
  {
    return ((DomDocumentType)getDomNodeOrDie()).getPublicId();
  }
  



  @JsxGetter
  public String getSystemId()
  {
    return ((DomDocumentType)getDomNodeOrDie()).getSystemId();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getInternalSubset()
  {
    String subset = ((DomDocumentType)getDomNodeOrDie()).getInternalSubset();
    if (StringUtils.isNotEmpty(subset)) {
      return subset;
    }
    
    return null;
  }
  



  @JsxGetter
  public Object getEntities()
  {
    NamedNodeMap entities = ((DomDocumentType)getDomNodeOrDie()).getEntities();
    if (entities != null) {
      return entities;
    }
    
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOCTYPE_ENTITIES_NULL)) {
      return null;
    }
    return Undefined.instance;
  }
  



  @JsxGetter
  public Object getNotations()
  {
    NamedNodeMap notations = ((DomDocumentType)getDomNodeOrDie()).getNotations();
    if (notations != null) {
      return notations;
    }
    
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_DOCTYPE_NOTATIONS_NULL)) {
      return null;
    }
    return Undefined.instance;
  }
  



  public Object getPrefix()
  {
    Object prefix = super.getPrefix();
    
    if ((prefix == null) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.DOCTYPE_PREFIX_UNDEFINED))) {
      return Undefined.instance;
    }
    return prefix;
  }
  



  public Object getLocalName()
  {
    Object localName = super.getLocalName();
    
    if ((localName == null) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.DOCTYPE_PREFIX_UNDEFINED))) {
      return Undefined.instance;
    }
    return localName;
  }
  



  public Object getNamespaceURI()
  {
    Object namespaceURI = super.getNamespaceURI();
    
    if ((namespaceURI == null) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.DOCTYPE_PREFIX_UNDEFINED))) {
      return Undefined.instance;
    }
    return namespaceURI;
  }
}
