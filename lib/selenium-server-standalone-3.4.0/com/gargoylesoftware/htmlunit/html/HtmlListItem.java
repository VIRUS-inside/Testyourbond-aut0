package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlListItem
  extends HtmlElement
{
  public static final String TAG_NAME = "li";
  
  HtmlListItem(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  







  public final String getTypeAttribute()
  {
    return getAttribute("type");
  }
  







  public final String getValueAttribute()
  {
    return getAttribute("value");
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.LIST_ITEM;
  }
}
