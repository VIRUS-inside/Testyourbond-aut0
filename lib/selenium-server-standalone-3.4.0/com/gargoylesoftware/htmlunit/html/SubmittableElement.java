package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

public abstract interface SubmittableElement
{
  public abstract NameValuePair[] getSubmitNameValuePairs();
  
  public abstract void reset();
  
  public abstract void setDefaultValue(String paramString);
  
  public abstract String getDefaultValue();
  
  public abstract void setDefaultChecked(boolean paramBoolean);
  
  public abstract boolean isDefaultChecked();
}
