package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;

































public class HtmlFont
  extends HtmlElement
{
  public static final String TAG_NAME = "font";
  
  HtmlFont(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  






  public final String getSizeAttribute()
  {
    return getAttribute("size");
  }
  






  public final String getColorAttribute()
  {
    return getAttribute("color");
  }
  






  public final String getFaceAttribute()
  {
    return getAttribute("face");
  }
  



  protected boolean isTrimmedText()
  {
    return false;
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.INLINE;
  }
}
