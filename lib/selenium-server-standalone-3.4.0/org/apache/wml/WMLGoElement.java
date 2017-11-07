package org.apache.wml;

public abstract interface WMLGoElement
  extends WMLElement
{
  public abstract void setSendreferer(String paramString);
  
  public abstract String getSendreferer();
  
  public abstract void setAcceptCharset(String paramString);
  
  public abstract String getAcceptCharset();
  
  public abstract void setHref(String paramString);
  
  public abstract String getHref();
  
  public abstract void setMethod(String paramString);
  
  public abstract String getMethod();
}
