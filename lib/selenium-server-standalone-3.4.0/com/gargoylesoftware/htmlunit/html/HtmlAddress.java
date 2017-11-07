package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import java.util.Map;
































public class HtmlAddress
  extends HtmlElement
{
  public static final String TAG_NAME = "address";
  
  HtmlAddress(String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, page, attributes);
  }
}
