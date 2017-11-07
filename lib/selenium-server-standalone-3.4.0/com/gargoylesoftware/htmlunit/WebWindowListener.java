package com.gargoylesoftware.htmlunit;

public abstract interface WebWindowListener
{
  public abstract void webWindowOpened(WebWindowEvent paramWebWindowEvent);
  
  public abstract void webWindowContentChanged(WebWindowEvent paramWebWindowEvent);
  
  public abstract void webWindowClosed(WebWindowEvent paramWebWindowEvent);
}
