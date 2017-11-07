package org.w3c.dom.html;

public abstract interface HTMLScriptElement
  extends HTMLElement
{
  public abstract String getText();
  
  public abstract void setText(String paramString);
  
  public abstract String getHtmlFor();
  
  public abstract void setHtmlFor(String paramString);
  
  public abstract String getEvent();
  
  public abstract void setEvent(String paramString);
  
  public abstract String getCharset();
  
  public abstract void setCharset(String paramString);
  
  public abstract boolean getDefer();
  
  public abstract void setDefer(boolean paramBoolean);
  
  public abstract String getSrc();
  
  public abstract void setSrc(String paramString);
  
  public abstract String getType();
  
  public abstract void setType(String paramString);
}
