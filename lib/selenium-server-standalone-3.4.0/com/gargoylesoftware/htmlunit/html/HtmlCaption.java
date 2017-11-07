package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlCaption
  extends HtmlElement
{
  public static final String TAG_NAME = "caption";
  
  HtmlCaption(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  






  public final String getAlignAttribute()
  {
    return getAttribute("align");
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.TABLE_CAPTION;
  }
}
