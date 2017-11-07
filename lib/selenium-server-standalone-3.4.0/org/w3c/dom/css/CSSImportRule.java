package org.w3c.dom.css;

import org.w3c.dom.stylesheets.MediaList;

public abstract interface CSSImportRule
  extends CSSRule
{
  public abstract String getHref();
  
  public abstract MediaList getMedia();
  
  public abstract CSSStyleSheet getStyleSheet();
}
