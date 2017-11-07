package org.w3c.dom.html;

import org.w3c.dom.DOMImplementation;

public abstract interface HTMLDOMImplementation
  extends DOMImplementation
{
  public abstract HTMLDocument createHTMLDocument(String paramString);
}
