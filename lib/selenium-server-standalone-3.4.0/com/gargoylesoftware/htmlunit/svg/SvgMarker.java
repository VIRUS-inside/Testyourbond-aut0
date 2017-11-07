package com.gargoylesoftware.htmlunit.svg;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import java.util.Map;





























public class SvgMarker
  extends SvgElement
{
  public static final String TAG_NAME = "marker";
  
  SvgMarker(String namespaceURI, String qualifiedName, SgmlPage page, Map<String, DomAttr> attributes)
  {
    super(namespaceURI, qualifiedName, page, attributes);
  }
}
