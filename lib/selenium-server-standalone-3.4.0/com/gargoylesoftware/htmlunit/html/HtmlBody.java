package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;





































public class HtmlBody
  extends HtmlElement
{
  public static final String TAG_NAME = "body";
  private final boolean temporary_;
  
  public HtmlBody(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes, boolean temporary)
  {
    super(qualifiedName, page, attributes);
    
    temporary_ = temporary;
    

    if ((getOwnerDocument() instanceof HtmlPage)) {
      getScriptableObject();
    }
  }
  






  public final String getOnLoadAttribute()
  {
    return getAttribute("onload");
  }
  






  public final String getOnUnloadAttribute()
  {
    return getAttribute("onunload");
  }
  






  public final String getBackgroundAttribute()
  {
    return getAttribute("background");
  }
  






  public final String getBgcolorAttribute()
  {
    return getAttribute("bgcolor");
  }
  






  public final String getTextAttribute()
  {
    return getAttribute("text");
  }
  






  public final String getLinkAttribute()
  {
    return getAttribute("link");
  }
  






  public final String getVlinkAttribute()
  {
    return getAttribute("vlink");
  }
  






  public final String getAlinkAttribute()
  {
    return getAttribute("alink");
  }
  






  public final boolean isTemporary()
  {
    return temporary_;
  }
}
