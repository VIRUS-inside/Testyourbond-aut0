package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;

































public class HtmlTableColumn
  extends HtmlElement
{
  public static final String TAG_NAME = "col";
  
  HtmlTableColumn(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  







  public final String getSpanAttribute()
  {
    return getAttribute("span");
  }
  







  public final String getWidthAttribute()
  {
    return getAttribute("width");
  }
  







  public final String getAlignAttribute()
  {
    return getAttribute("align");
  }
  







  public final String getCharAttribute()
  {
    return getAttribute("char");
  }
  







  public final String getCharoffAttribute()
  {
    return getAttribute("charoff");
  }
  







  public final String getValignAttribute()
  {
    return getAttribute("valign");
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.TABLE_COLUMN;
  }
}
