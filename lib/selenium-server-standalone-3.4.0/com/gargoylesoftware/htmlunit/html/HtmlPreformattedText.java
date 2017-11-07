package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlPreformattedText
  extends HtmlElement
{
  public static final String TAG_NAME = "pre";
  
  HtmlPreformattedText(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  







  public final String getWidthAttribute()
  {
    return getAttribute("width");
  }
}
