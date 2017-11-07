package com.gargoylesoftware.htmlunit.svg;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import java.util.Map;





























public class SvgTitle
  extends SvgElement
{
  public static final String TAG_NAME = "title";
  
  SvgTitle(String namespaceURI, String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(namespaceURI, qualifiedName, page, attributes);
  }
}
