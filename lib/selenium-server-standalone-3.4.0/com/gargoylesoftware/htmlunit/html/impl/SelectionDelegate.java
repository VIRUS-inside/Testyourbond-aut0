package com.gargoylesoftware.htmlunit.html.impl;

import java.io.Serializable;

public abstract interface SelectionDelegate
  extends Serializable
{
  public abstract int getSelectionStart();
  
  public abstract void setSelectionStart(int paramInt);
  
  public abstract int getSelectionEnd();
  
  public abstract void setSelectionEnd(int paramInt);
}
