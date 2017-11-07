package org.w3c.dom.html;

public abstract interface HTMLLinkElement
  extends HTMLElement
{
  public abstract boolean getDisabled();
  
  public abstract void setDisabled(boolean paramBoolean);
  
  public abstract String getCharset();
  
  public abstract void setCharset(String paramString);
  
  public abstract String getHref();
  
  public abstract void setHref(String paramString);
  
  public abstract String getHreflang();
  
  public abstract void setHreflang(String paramString);
  
  public abstract String getMedia();
  
  public abstract void setMedia(String paramString);
  
  public abstract String getRel();
  
  public abstract void setRel(String paramString);
  
  public abstract String getRev();
  
  public abstract void setRev(String paramString);
  
  public abstract String getTarget();
  
  public abstract void setTarget(String paramString);
  
  public abstract String getType();
  
  public abstract void setType(String paramString);
}
