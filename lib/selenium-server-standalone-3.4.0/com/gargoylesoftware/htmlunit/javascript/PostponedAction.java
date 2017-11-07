package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;
import java.lang.ref.WeakReference;
























public abstract class PostponedAction
{
  private final WeakReference<Page> owningPageRef_;
  private final String description_;
  
  public PostponedAction(Page owningPage)
  {
    this(owningPage, null);
  }
  




  public PostponedAction(Page owningPage, String description)
  {
    owningPageRef_ = new WeakReference(owningPage);
    description_ = description;
  }
  



  protected Page getOwningPage()
  {
    return (Page)owningPageRef_.get();
  }
  



  public abstract void execute()
    throws Exception;
  



  public boolean isStillAlive()
  {
    Page owningPage = getOwningPage();
    return (owningPage != null) && (owningPage == owningPage.getEnclosingWindow().getEnclosedPage());
  }
  
  public String toString()
  {
    if (description_ == null) {
      return super.toString();
    }
    
    return "PostponedAction(" + description_ + ")";
  }
}
