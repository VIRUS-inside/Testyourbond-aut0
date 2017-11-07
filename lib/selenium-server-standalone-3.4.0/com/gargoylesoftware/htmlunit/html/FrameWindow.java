package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowImpl;





















public class FrameWindow
  extends WebWindowImpl
{
  private final BaseFrameElement frame_;
  
  FrameWindow(BaseFrameElement frame)
  {
    super(frame.getPage().getWebClient());
    frame_ = frame;
    WebWindowImpl parent = (WebWindowImpl)getParentWindow();
    performRegistration();
    parent.addChildWindow(this);
  }
  




  public String getName()
  {
    return frame_.getNameAttribute();
  }
  




  public void setName(String name)
  {
    frame_.setNameAttribute(name);
  }
  



  public WebWindow getParentWindow()
  {
    return frame_.getPage().getEnclosingWindow();
  }
  



  public WebWindow getTopWindow()
  {
    return getParentWindow().getTopWindow();
  }
  



  protected boolean isJavaScriptInitializationNeeded()
  {
    return (getScriptableObject() == null) || 
      (!(getEnclosedPage().getWebResponse() instanceof StringWebResponse));
  }
  






  public HtmlPage getEnclosingPage()
  {
    return (HtmlPage)frame_.getPage();
  }
  



  public void setEnclosedPage(Page page)
  {
    super.setEnclosedPage(page);
    



    WebResponse webResponse = page.getWebResponse();
    if ((webResponse instanceof StringWebResponse)) {
      StringWebResponse response = (StringWebResponse)webResponse;
      if (response.isFromJavascript()) {
        BaseFrameElement frame = getFrameElement();
        frame.setContentLoaded();
      }
    }
  }
  



  public BaseFrameElement getFrameElement()
  {
    return frame_;
  }
  




  public String toString()
  {
    return "FrameWindow[name=\"" + getName() + "\"]";
  }
  


  public void close()
  {
    WebWindowImpl parent = (WebWindowImpl)getParentWindow();
    parent.removeChildWindow(this);
    getWebClient().deregisterWebWindow(this);
  }
}
