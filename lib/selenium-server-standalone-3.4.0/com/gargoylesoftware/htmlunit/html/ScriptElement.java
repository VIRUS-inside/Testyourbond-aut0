package com.gargoylesoftware.htmlunit.html;

import java.nio.charset.Charset;

public abstract interface ScriptElement
{
  public abstract boolean isExecuted();
  
  public abstract void setExecuted(boolean paramBoolean);
  
  public abstract String getSrcAttribute();
  
  public abstract String getCharsetAttribute();
  
  public abstract Charset getCharset();
}
