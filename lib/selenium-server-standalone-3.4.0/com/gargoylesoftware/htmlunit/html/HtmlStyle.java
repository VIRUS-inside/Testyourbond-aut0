package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlStyle
  extends HtmlElement
{
  public static final String TAG_NAME = "style";
  
  HtmlStyle(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  






  public final String getTypeAttribute()
  {
    return getAttribute("type");
  }
  




  public final void setTypeAttribute(String type)
  {
    setAttribute("type", type);
  }
  






  public final String getMediaAttribute()
  {
    return getAttribute("media");
  }
  






  public final String getTitleAttribute()
  {
    return getAttribute("title");
  }
  





  public String asText()
  {
    return "";
  }
  




  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.NONE;
  }
  



  public boolean mayBeDisplayed()
  {
    return false;
  }
}
