package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlHeading1
  extends HtmlElement
{
  public static final String TAG_NAME = "h1";
  
  HtmlHeading1(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  






  public final String getAlignAttribute()
  {
    return getAttribute("align");
  }
}
