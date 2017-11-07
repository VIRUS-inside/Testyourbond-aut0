package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;


































public class HtmlDivision
  extends HtmlElement
{
  public static final String TAG_NAME = "div";
  
  public HtmlDivision(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  






  public final String getAlignAttribute()
  {
    return getAttribute("align");
  }
  




  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
}
