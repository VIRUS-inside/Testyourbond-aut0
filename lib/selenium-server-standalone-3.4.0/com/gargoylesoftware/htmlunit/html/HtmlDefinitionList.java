package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlDefinitionList
  extends HtmlElement
{
  public static final String TAG_NAME = "dl";
  
  HtmlDefinitionList(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  






  public final String getCompactAttribute()
  {
    return getAttribute("compact");
  }
}
