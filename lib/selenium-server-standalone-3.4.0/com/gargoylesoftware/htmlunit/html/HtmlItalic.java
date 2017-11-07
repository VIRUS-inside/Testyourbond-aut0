package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;






























public class HtmlItalic
  extends HtmlElement
{
  public static final String TAG_NAME = "i";
  
  HtmlItalic(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.INLINE;
  }
  




  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
}
