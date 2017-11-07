package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlHeading3
  extends HtmlElement
{
  public static final String TAG_NAME = "h3";
  
  HtmlHeading3(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  






  public final String getAlignAttribute()
  {
    return getAttribute("align");
  }
}
