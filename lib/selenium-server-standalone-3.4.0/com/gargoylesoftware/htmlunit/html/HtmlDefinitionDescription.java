package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;

































public class HtmlDefinitionDescription
  extends HtmlElement
{
  public static final String TAG_NAME = "dd";
  
  HtmlDefinitionDescription(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  



  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
  



  public HtmlElement.DisplayStyle getDefaultStyleDisplay()
  {
    HtmlPage page = getHtmlPageOrNull();
    if ((page.isQuirksMode()) && (page.hasFeature(BrowserVersionFeatures.HTMLDEFINITION_INLINE_IN_QUIRKS))) {
      return HtmlElement.DisplayStyle.INLINE;
    }
    return super.getDefaultStyleDisplay();
  }
}
