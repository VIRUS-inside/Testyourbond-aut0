package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlHorizontalRule
  extends HtmlElement
{
  public static final String TAG_NAME = "hr";
  
  HtmlHorizontalRule(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  






  public final String getAlignAttribute()
  {
    return getAttribute("align");
  }
  






  public final String getNoShadeAttribute()
  {
    return getAttribute("noshade");
  }
  






  public final String getSizeAttribute()
  {
    return getAttribute("size");
  }
  






  public final String getWidthAttribute()
  {
    return getAttribute("width");
  }
}
