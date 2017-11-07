package org.w3c.dom.html;

public abstract interface HTMLFormElement
  extends HTMLElement
{
  public abstract HTMLCollection getElements();
  
  public abstract int getLength();
  
  public abstract String getName();
  
  public abstract void setName(String paramString);
  
  public abstract String getAcceptCharset();
  
  public abstract void setAcceptCharset(String paramString);
  
  public abstract String getAction();
  
  public abstract void setAction(String paramString);
  
  public abstract String getEnctype();
  
  public abstract void setEnctype(String paramString);
  
  public abstract String getMethod();
  
  public abstract void setMethod(String paramString);
  
  public abstract String getTarget();
  
  public abstract void setTarget(String paramString);
  
  public abstract void submit();
  
  public abstract void reset();
}
