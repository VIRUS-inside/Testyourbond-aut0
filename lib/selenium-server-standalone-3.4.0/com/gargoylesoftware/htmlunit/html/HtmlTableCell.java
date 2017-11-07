package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;

































public abstract class HtmlTableCell
  extends HtmlElement
{
  protected HtmlTableCell(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public int getColumnSpan()
  {
    String spanString = getAttribute("colspan");
    if ((spanString == null) || (spanString.isEmpty())) {
      return 1;
    }
    return Integer.parseInt(spanString);
  }
  



  public int getRowSpan()
  {
    String spanString = getAttribute("rowspan");
    if ((spanString == null) || (spanString.isEmpty())) {
      return 1;
    }
    return Integer.parseInt(spanString);
  }
  



  public HtmlTableRow getEnclosingRow()
  {
    return (HtmlTableRow)getEnclosingElement("tr");
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.TABLE_CELL;
  }
}
