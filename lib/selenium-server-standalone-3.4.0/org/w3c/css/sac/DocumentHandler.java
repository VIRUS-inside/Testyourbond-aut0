package org.w3c.css.sac;

public abstract interface DocumentHandler
{
  public abstract void startDocument(InputSource paramInputSource)
    throws CSSException;
  
  public abstract void endDocument(InputSource paramInputSource)
    throws CSSException;
  
  public abstract void comment(String paramString)
    throws CSSException;
  
  public abstract void ignorableAtRule(String paramString)
    throws CSSException;
  
  public abstract void namespaceDeclaration(String paramString1, String paramString2)
    throws CSSException;
  
  public abstract void importStyle(String paramString1, SACMediaList paramSACMediaList, String paramString2)
    throws CSSException;
  
  public abstract void startMedia(SACMediaList paramSACMediaList)
    throws CSSException;
  
  public abstract void endMedia(SACMediaList paramSACMediaList)
    throws CSSException;
  
  public abstract void startPage(String paramString1, String paramString2)
    throws CSSException;
  
  public abstract void endPage(String paramString1, String paramString2)
    throws CSSException;
  
  public abstract void startFontFace()
    throws CSSException;
  
  public abstract void endFontFace()
    throws CSSException;
  
  public abstract void startSelector(SelectorList paramSelectorList)
    throws CSSException;
  
  public abstract void endSelector(SelectorList paramSelectorList)
    throws CSSException;
  
  public abstract void property(String paramString, LexicalUnit paramLexicalUnit, boolean paramBoolean)
    throws CSSException;
}
