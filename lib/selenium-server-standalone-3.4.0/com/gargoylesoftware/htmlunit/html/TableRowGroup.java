package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

































public abstract class TableRowGroup
  extends HtmlElement
{
  protected TableRowGroup(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  




  public final List<HtmlTableRow> getRows()
  {
    List<HtmlTableRow> resultList = new ArrayList();
    
    for (DomElement element : getChildElements()) {
      if ((element instanceof HtmlTableRow)) {
        resultList.add((HtmlTableRow)element);
      }
    }
    
    return resultList;
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
}
