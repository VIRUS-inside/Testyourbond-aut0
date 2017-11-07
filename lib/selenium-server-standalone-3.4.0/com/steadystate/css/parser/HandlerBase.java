package com.steadystate.css.parser;

import com.steadystate.css.sac.DocumentHandlerExt;
import java.io.PrintStream;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Locator;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;


















public class HandlerBase
  implements DocumentHandlerExt, ErrorHandler
{
  public HandlerBase() {}
  
  public void startDocument(InputSource source)
    throws CSSException
  {}
  
  public void endDocument(InputSource source)
    throws CSSException
  {}
  
  public void comment(String text)
    throws CSSException
  {}
  
  public void ignorableAtRule(String atRule)
    throws CSSException
  {}
  
  public void ignorableAtRule(String atRule, Locator locator)
    throws CSSException
  {}
  
  public void namespaceDeclaration(String prefix, String uri)
    throws CSSException
  {}
  
  public void importStyle(String uri, SACMediaList media, String defaultNamespaceURI)
    throws CSSException
  {}
  
  public void importStyle(String uri, SACMediaList media, String defaultNamespaceURI, Locator locator)
    throws CSSException
  {}
  
  public void startMedia(SACMediaList media)
    throws CSSException
  {}
  
  public void startMedia(SACMediaList media, Locator locator)
    throws CSSException
  {}
  
  public void endMedia(SACMediaList media)
    throws CSSException
  {}
  
  public void startPage(String name, String pseudoPage)
    throws CSSException
  {}
  
  public void startPage(String name, String pseudoPage, Locator locator)
    throws CSSException
  {}
  
  public void endPage(String name, String pseudoPage)
    throws CSSException
  {}
  
  public void startFontFace()
    throws CSSException
  {}
  
  public void startFontFace(Locator locator)
    throws CSSException
  {}
  
  public void endFontFace()
    throws CSSException
  {}
  
  public void startSelector(SelectorList selectors)
    throws CSSException
  {}
  
  public void startSelector(SelectorList selectors, Locator locator)
    throws CSSException
  {}
  
  public void endSelector(SelectorList selectors)
    throws CSSException
  {}
  
  public void property(String name, LexicalUnit value, boolean important)
    throws CSSException
  {}
  
  public void property(String name, LexicalUnit value, boolean important, Locator locator) {}
  
  public void charset(String characterEncoding, Locator locator)
    throws CSSException
  {}
  
  public void warning(CSSParseException exception)
    throws CSSException
  {
    StringBuilder sb = new StringBuilder();
    sb.append(exception.getURI())
      .append(" [")
      .append(exception.getLineNumber())
      .append(":")
      .append(exception.getColumnNumber())
      .append("] ")
      .append(exception.getMessage());
    System.err.println(sb.toString());
  }
  
  public void error(CSSParseException exception) throws CSSException {
    StringBuilder sb = new StringBuilder();
    sb.append(exception.getURI())
      .append(" [")
      .append(exception.getLineNumber())
      .append(":")
      .append(exception.getColumnNumber())
      .append("] ")
      .append(exception.getMessage());
    System.err.println(sb.toString());
  }
  
  public void fatalError(CSSParseException exception) throws CSSException {
    StringBuilder sb = new StringBuilder();
    sb.append(exception.getURI())
      .append(" [")
      .append(exception.getLineNumber())
      .append(":")
      .append(exception.getColumnNumber())
      .append("] ")
      .append(exception.getMessage());
    System.err.println(sb.toString());
  }
}
