package com.gargoylesoftware.htmlunit.javascript.host.worker;

import com.gargoylesoftware.htmlunit.javascript.background.BasicJavaScriptJob;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;

























































































































































































































class WorkerJob
  extends BasicJavaScriptJob
{
  private final ContextFactory contextFactory_;
  private final ContextAction action_;
  private final String description_;
  
  WorkerJob(ContextFactory contextFactory, ContextAction action, String description)
  {
    contextFactory_ = contextFactory;
    action_ = action;
    description_ = description;
  }
  
  public void run()
  {
    contextFactory_.call(action_);
  }
  
  public String toString()
  {
    return "WorkerJob(" + description_ + ")";
  }
}
