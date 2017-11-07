package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;

































public class HtmlTitle
  extends HtmlElement
{
  public static final String TAG_NAME = "title";
  
  HtmlTitle(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  




  public void setNodeValue(String message)
  {
    DomNode child = getFirstChild();
    if (child == null) {
      DomNode textNode = new DomText(getPage(), message);
      appendChild(textNode);
    }
    else if ((child instanceof DomText)) {
      ((DomText)child).setData(message);
    }
    else {
      throw new IllegalStateException("For title tag, this should be a text node");
    }
  }
  





  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.NONE;
  }
}
