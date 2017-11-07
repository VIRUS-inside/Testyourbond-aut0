package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;

































public class HtmlDeletedText
  extends HtmlElement
{
  public static final String TAG_NAME = "del";
  
  HtmlDeletedText(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  






  public final String getCiteAttribute()
  {
    return getAttribute("cite");
  }
  






  public final String getDateTimeAttribute()
  {
    return getAttribute("datetime");
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.INLINE;
  }
}
