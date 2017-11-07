package org.w3c.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.stylesheets.StyleSheet;

public abstract interface CSSStyleSheet
  extends StyleSheet
{
  public abstract CSSRule getOwnerRule();
  
  public abstract CSSRuleList getCssRules();
  
  public abstract int insertRule(String paramString, int paramInt)
    throws DOMException;
  
  public abstract void deleteRule(int paramInt)
    throws DOMException;
}
