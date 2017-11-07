package org.w3c.dom.css;

import org.w3c.dom.DOMException;

public abstract interface CSSCharsetRule
  extends CSSRule
{
  public abstract String getEncoding();
  
  public abstract void setEncoding(String paramString)
    throws DOMException;
}
