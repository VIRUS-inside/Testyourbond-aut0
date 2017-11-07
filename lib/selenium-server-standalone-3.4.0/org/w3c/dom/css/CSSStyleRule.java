package org.w3c.dom.css;

import org.w3c.dom.DOMException;

public abstract interface CSSStyleRule
  extends CSSRule
{
  public abstract String getSelectorText();
  
  public abstract void setSelectorText(String paramString)
    throws DOMException;
  
  public abstract CSSStyleDeclaration getStyle();
}
