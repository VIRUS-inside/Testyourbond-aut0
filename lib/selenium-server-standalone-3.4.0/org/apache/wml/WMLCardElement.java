package org.apache.wml;

public abstract interface WMLCardElement
  extends WMLElement
{
  public abstract void setOnEnterBackward(String paramString);
  
  public abstract String getOnEnterBackward();
  
  public abstract void setOnEnterForward(String paramString);
  
  public abstract String getOnEnterForward();
  
  public abstract void setOnTimer(String paramString);
  
  public abstract String getOnTimer();
  
  public abstract void setTitle(String paramString);
  
  public abstract String getTitle();
  
  public abstract void setNewContext(boolean paramBoolean);
  
  public abstract boolean getNewContext();
  
  public abstract void setOrdered(boolean paramBoolean);
  
  public abstract boolean getOrdered();
  
  public abstract void setXmlLang(String paramString);
  
  public abstract String getXmlLang();
}
