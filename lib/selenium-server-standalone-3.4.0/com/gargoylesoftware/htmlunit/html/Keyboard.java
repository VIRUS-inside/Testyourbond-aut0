package com.gargoylesoftware.htmlunit.html;

import java.util.ArrayList;
import java.util.List;






















public class Keyboard
{
  private List<Object[]> keys_ = new ArrayList();
  
  private boolean startAtEnd_;
  

  public Keyboard()
  {
    this(false);
  }
  



  public Keyboard(boolean startAtEnd)
  {
    startAtEnd_ = startAtEnd;
  }
  



  public void type(char ch)
  {
    keys_.add(new Object[] { Character.valueOf(ch) });
  }
  







  public void press(int keyCode)
  {
    if ((keyCode >= 65) && (keyCode <= 90)) {
      throw new IllegalArgumentException("For key code " + keyCode + ", use type(char) instead");
    }
    keys_.add(new Object[] { Integer.valueOf(keyCode), Boolean.valueOf(true) });
  }
  







  public void release(int keyCode)
  {
    keys_.add(new Object[] { Integer.valueOf(keyCode), Boolean.valueOf(false) });
  }
  


  public void clear()
  {
    keys_.clear();
  }
  







  List<Object[]> getKeys()
  {
    return keys_;
  }
  



  public boolean isStartAtEnd()
  {
    return startAtEnd_;
  }
}
