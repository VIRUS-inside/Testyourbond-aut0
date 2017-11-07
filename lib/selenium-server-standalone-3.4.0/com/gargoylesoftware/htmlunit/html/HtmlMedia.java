package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;




























public class HtmlMedia
  extends HtmlElement
{
  HtmlMedia(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
  




  public String canPlayType(String type)
  {
    return "";
  }
}
