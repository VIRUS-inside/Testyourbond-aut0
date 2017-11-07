package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;

































public class HtmlParameter
  extends HtmlElement
{
  public static final String TAG_NAME = "param";
  
  HtmlParameter(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  







  public final String getIdAttribute()
  {
    return getAttribute("id");
  }
  







  public final String getNameAttribute()
  {
    return getAttribute("name");
  }
  







  public final String getValueAttribute()
  {
    return getAttribute("value");
  }
  







  public final String getValueTypeAttribute()
  {
    return getAttribute("valuetype");
  }
  







  public final String getTypeAttribute()
  {
    return getAttribute("type");
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_DISPLAY_BLOCK2)) {
      return HtmlElement.DisplayStyle.NONE;
    }
    return HtmlElement.DisplayStyle.INLINE;
  }
}
