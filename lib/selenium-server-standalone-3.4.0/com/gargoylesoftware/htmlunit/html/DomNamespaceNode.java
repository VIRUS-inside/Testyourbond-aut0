package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.xpath.XPathUtils;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import java.util.Locale;





























public abstract class DomNamespaceNode
  extends DomNode
{
  private String namespaceURI_;
  private String qualifiedName_;
  private final String localName_;
  private final String localNameLC_;
  private String prefix_;
  
  protected DomNamespaceNode(String namespaceURI, String qualifiedName, SgmlPage page)
  {
    super(page);
    WebAssert.notNull("qualifiedName", qualifiedName);
    qualifiedName_ = qualifiedName;
    
    if (qualifiedName.indexOf(':') != -1) {
      namespaceURI_ = namespaceURI;
      int colonPosition = qualifiedName_.indexOf(':');
      localName_ = qualifiedName_.substring(colonPosition + 1);
      prefix_ = qualifiedName_.substring(0, colonPosition);
    }
    else {
      namespaceURI_ = namespaceURI;
      localName_ = qualifiedName_;
      prefix_ = null;
    }
    
    localNameLC_ = localName_.toLowerCase(Locale.ROOT);
  }
  



  public String getNamespaceURI()
  {
    if ((getPage().isHtmlPage()) && (!(getPage() instanceof XHtmlPage)) && 
      ("http://www.w3.org/1999/xhtml".equals(namespaceURI_)) && 
      (XPathUtils.isProcessingXPath()))
    {

      return null;
    }
    return namespaceURI_;
  }
  



  public String getLocalName()
  {
    boolean caseSensitive = getPage().hasCaseSensitiveTagNames();
    if ((!caseSensitive) && (XPathUtils.isProcessingXPath())) {
      return localNameLC_;
    }
    return localName_;
  }
  



  public String getPrefix()
  {
    return prefix_;
  }
  



  public void setPrefix(String prefix)
  {
    prefix_ = prefix;
    if ((prefix_ != null) && (localName_ != null)) {
      qualifiedName_ = (prefix_ + ":" + localName_);
    }
  }
  



  public String getQualifiedName()
  {
    return qualifiedName_;
  }
  



  public void processImportNode(Document doc)
  {
    super.processImportNode(doc);
    



    SgmlPage page = (SgmlPage)doc.getDomNodeOrDie();
    if ((page.isHtmlPage()) && (!(page instanceof XHtmlPage)) && ("http://www.w3.org/1999/xhtml".equals(namespaceURI_))) {
      namespaceURI_ = null;
    }
  }
}
