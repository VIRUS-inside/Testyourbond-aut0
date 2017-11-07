package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;





























public class HtmlDialog
  extends HtmlElement
{
  public static final String TAG_NAME = "dialog";
  
  HtmlDialog(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    if (hasFeature(BrowserVersionFeatures.CSS_DIALOG_NONE)) {
      return HtmlElement.DisplayStyle.NONE;
    }
    return HtmlElement.DisplayStyle.INLINE;
  }
}
