package com.gargoylesoftware.htmlunit.javascript.background;

import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;

















final class JavascriptXMLHttpRequestJob
  extends BasicJavaScriptJob
{
  private final ContextFactory contextFactory_;
  private final ContextAction action_;
  
  JavascriptXMLHttpRequestJob(ContextFactory contextFactory, ContextAction action)
  {
    contextFactory_ = contextFactory;
    action_ = action;
  }
  
  public void run()
  {
    contextFactory_.call(action_);
  }
  
  public String toString()
  {
    return "XMLHttpRequest Execution Job " + getId() + ": " + action_.toString();
  }
}
