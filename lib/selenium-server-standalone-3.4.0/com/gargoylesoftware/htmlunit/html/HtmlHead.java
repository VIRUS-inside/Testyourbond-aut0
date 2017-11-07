package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;































public class HtmlHead
  extends HtmlElement
{
  public static final String TAG_NAME = "head";
  
  HtmlHead(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  






  public final String getProfileAttribute()
  {
    return getAttribute("profile");
  }
  



  public boolean mayBeDisplayed()
  {
    return false;
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    return HtmlElement.DisplayStyle.NONE;
  }
}
