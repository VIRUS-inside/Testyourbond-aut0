package org.w3c.dom.css;

import org.w3c.dom.Element;
import org.w3c.dom.views.AbstractView;

public abstract interface ViewCSS
  extends AbstractView
{
  public abstract CSSStyleDeclaration getComputedStyle(Element paramElement, String paramString);
}
