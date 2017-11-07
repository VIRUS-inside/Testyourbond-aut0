package org.apache.wml;

public abstract interface WMLTemplateElement
  extends WMLElement
{
  public abstract void setOnTimer(String paramString);
  
  public abstract String getOnTimer();
  
  public abstract void setOnEnterBackward(String paramString);
  
  public abstract String getOnEnterBackward();
  
  public abstract void setOnEnterForward(String paramString);
  
  public abstract String getOnEnterForward();
}
