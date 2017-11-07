package com.gargoylesoftware.htmlunit.html.impl;






public class SimpleSelectionDelegate
  implements SelectionDelegate
{
  private int selectionStart_;
  




  private int selectionEnd_;
  





  public SimpleSelectionDelegate() {}
  




  public int getSelectionStart()
  {
    return selectionStart_;
  }
  



  public void setSelectionStart(int selectionStart)
  {
    selectionStart_ = selectionStart;
  }
  



  public int getSelectionEnd()
  {
    return selectionEnd_;
  }
  



  public void setSelectionEnd(int selectionEnd)
  {
    selectionEnd_ = selectionEnd;
  }
}
