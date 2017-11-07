package com.gargoylesoftware.htmlunit;

public abstract interface OnbeforeunloadHandler
{
  public abstract boolean handleEvent(Page paramPage, String paramString);
}
