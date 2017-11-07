package com.gargoylesoftware.htmlunit.html.impl;

import com.gargoylesoftware.htmlunit.Page;
import org.w3c.dom.Node;

public abstract interface SelectableTextInput
  extends Node
{
  public abstract Page getPage();
  
  public abstract void focus();
  
  public abstract void select();
  
  public abstract String getText();
  
  public abstract void setText(String paramString);
  
  public abstract String getSelectedText();
  
  public abstract int getSelectionStart();
  
  public abstract void setSelectionStart(int paramInt);
  
  public abstract int getSelectionEnd();
  
  public abstract void setSelectionEnd(int paramInt);
}
