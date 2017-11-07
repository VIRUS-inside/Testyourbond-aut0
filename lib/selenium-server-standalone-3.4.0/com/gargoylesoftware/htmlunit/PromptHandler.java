package com.gargoylesoftware.htmlunit;

public abstract interface PromptHandler
{
  public abstract String handlePrompt(Page paramPage, String paramString1, String paramString2);
}
