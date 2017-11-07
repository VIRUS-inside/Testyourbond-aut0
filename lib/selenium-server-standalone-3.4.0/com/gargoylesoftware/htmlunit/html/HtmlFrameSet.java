package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlFrameSet
  extends HtmlElement
{
  public static final String TAG_NAME = "frameset";
  
  HtmlFrameSet(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
    

    getScriptableObject();
  }
  






  public final String getRowsAttribute()
  {
    return getAttribute("rows");
  }
  






  public final String getColsAttribute()
  {
    return getAttribute("cols");
  }
  






  public final String getOnLoadAttribute()
  {
    return getAttribute("onload");
  }
  






  public final String getOnUnloadAttribute()
  {
    return getAttribute("onunload");
  }
}
