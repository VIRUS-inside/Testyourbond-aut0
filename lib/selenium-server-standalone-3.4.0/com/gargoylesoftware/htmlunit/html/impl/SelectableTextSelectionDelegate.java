package com.gargoylesoftware.htmlunit.html.impl;

import org.w3c.dom.ranges.Range;
































public class SelectableTextSelectionDelegate
  implements SelectionDelegate
{
  private final SelectableTextInput element_;
  private final Range selection_;
  
  public SelectableTextSelectionDelegate(SelectableTextInput element)
  {
    element_ = element;
    selection_ = new SimpleRange(element, 0);
  }
  


  public void select()
  {
    element_.focus();
    setSelectionStart(0);
    setSelectionEnd(element_.getText().length());
  }
  



  public String getSelectedText()
  {
    return selection_.toString();
  }
  



  public int getSelectionStart()
  {
    return selection_.getStartOffset();
  }
  



  public void setSelectionStart(int selectionStart)
  {
    int length = element_.getText().length();
    selectionStart = Math.max(0, Math.min(selectionStart, length));
    selection_.setStart(element_, selectionStart);
    if (selection_.getEndOffset() < selectionStart) {
      selection_.setEnd(element_, selectionStart);
    }
  }
  



  public int getSelectionEnd()
  {
    return selection_.getEndOffset();
  }
  



  public void setSelectionEnd(int selectionEnd)
  {
    int length = element_.getText().length();
    selectionEnd = Math.min(length, Math.max(selectionEnd, 0));
    selection_.setEnd(element_, selectionEnd);
    if (selection_.getStartOffset() > selectionEnd) {
      selection_.setStart(element_, selectionEnd);
    }
  }
}
