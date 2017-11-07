package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlOrderedList
  extends HtmlElement
{
  public static final String TAG_NAME = "ol";
  
  HtmlOrderedList(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  







  public final String getTypeAttribute()
  {
    return getAttribute("type");
  }
  







  public final String getCompactAttribute()
  {
    return getAttribute("compact");
  }
  







  public final String getStartAttribute()
  {
    return getAttribute("start");
  }
  



  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
}
