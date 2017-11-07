package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlTableDataCell
  extends HtmlTableCell
{
  public static final String TAG_NAME = "td";
  
  HtmlTableDataCell(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  







  public final String getAbbrAttribute()
  {
    return getAttribute("abbr");
  }
  







  public final String getAxisAttribute()
  {
    return getAttribute("axis");
  }
  







  public final String getHeadersAttribute()
  {
    return getAttribute("headers");
  }
  







  public final String getScopeAttribute()
  {
    return getAttribute("scope");
  }
  







  public final String getRowSpanAttribute()
  {
    return getAttribute("rowspan");
  }
  







  public final String getColumnSpanAttribute()
  {
    return getAttribute("colspan");
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
  







  public final String getNoWrapAttribute()
  {
    return getAttribute("nowrap");
  }
  







  public final String getBgcolorAttribute()
  {
    return getAttribute("bgcolor");
  }
  







  public final String getWidthAttribute()
  {
    return getAttribute("width");
  }
  







  public final String getHeightAttribute()
  {
    return getAttribute("height");
  }
}
