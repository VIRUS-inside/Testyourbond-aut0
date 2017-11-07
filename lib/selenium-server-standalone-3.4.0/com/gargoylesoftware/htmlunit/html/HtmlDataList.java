package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;





























public class HtmlDataList
  extends HtmlElement
{
  public static final String TAG_NAME = "datalist";
  
  HtmlDataList(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.NONE;
  }
}