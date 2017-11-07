package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.SgmlPage;
import org.xml.sax.Attributes;

public abstract interface ElementFactory
{
  public abstract DomElement createElement(SgmlPage paramSgmlPage, String paramString, Attributes paramAttributes);
  
  public abstract DomElement createElementNS(SgmlPage paramSgmlPage, String paramString1, String paramString2, Attributes paramAttributes);
  
  public abstract DomElement createElementNS(SgmlPage paramSgmlPage, String paramString1, String paramString2, Attributes paramAttributes, boolean paramBoolean);
}
