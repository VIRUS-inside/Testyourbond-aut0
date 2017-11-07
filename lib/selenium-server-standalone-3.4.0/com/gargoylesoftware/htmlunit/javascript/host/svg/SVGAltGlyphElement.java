package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.svg.SvgAltGlyph;

@JsxClass(domClass=SvgAltGlyph.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(value=com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF, maxVersion=45)})
public class SVGAltGlyphElement
  extends SVGTextPositioningElement
{
  @JsxConstructor
  public SVGAltGlyphElement() {}
}
