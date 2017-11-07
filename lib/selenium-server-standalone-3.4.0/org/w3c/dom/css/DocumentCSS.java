package org.w3c.dom.css;

import org.w3c.dom.Element;
import org.w3c.dom.stylesheets.DocumentStyle;

public abstract interface DocumentCSS
  extends DocumentStyle
{
  public abstract CSSStyleDeclaration getOverrideStyle(Element paramElement, String paramString);
}
