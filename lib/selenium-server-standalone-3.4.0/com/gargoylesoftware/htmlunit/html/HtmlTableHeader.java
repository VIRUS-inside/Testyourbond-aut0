package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlTableHeader
  extends TableRowGroup
{
  public static final String TAG_NAME = "thead";
  
  HtmlTableHeader(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.TABLE_HEADER_GROUP;
  }
}
