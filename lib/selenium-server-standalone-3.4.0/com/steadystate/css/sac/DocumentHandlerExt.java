package com.steadystate.css.sac;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Locator;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;

public abstract interface DocumentHandlerExt
  extends DocumentHandler
{
  public abstract void charset(String paramString, Locator paramLocator)
    throws CSSException;
  
  public abstract void importStyle(String paramString1, SACMediaList paramSACMediaList, String paramString2, Locator paramLocator)
    throws CSSException;
  
  public abstract void ignorableAtRule(String paramString, Locator paramLocator)
    throws CSSException;
  
  public abstract void startFontFace(Locator paramLocator)
    throws CSSException;
  
  public abstract void startPage(String paramString1, String paramString2, Locator paramLocator)
    throws CSSException;
  
  public abstract void startMedia(SACMediaList paramSACMediaList, Locator paramLocator)
    throws CSSException;
  
  public abstract void startSelector(SelectorList paramSelectorList, Locator paramLocator)
    throws CSSException;
  
  public abstract void property(String paramString, LexicalUnit paramLexicalUnit, boolean paramBoolean, Locator paramLocator);
}
