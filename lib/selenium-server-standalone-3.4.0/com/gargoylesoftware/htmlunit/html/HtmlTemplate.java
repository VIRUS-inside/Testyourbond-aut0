package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;






























public class HtmlTemplate
  extends HtmlElement
{
  public static final String TAG_NAME = "template";
  private DomDocumentFragment domDocumentFragment_;
  
  HtmlTemplate(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
    
    domDocumentFragment_ = new DomDocumentFragment(page);
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.NONE;
  }
  


  public DomDocumentFragment getContent()
  {
    return domDocumentFragment_;
  }
  



  protected void onAllChildrenAddedToPage(boolean postponed)
  {
    while (getFirstChild() != null) {
      DomNode child = getFirstChild();
      child.basicRemove();
      domDocumentFragment_.appendChild(child);
    }
  }
}
