package org.openqa.selenium.interactions;

public abstract interface Keyboard
{
  public abstract void sendKeys(CharSequence... paramVarArgs);
  
  public abstract void pressKey(CharSequence paramCharSequence);
  
  public abstract void releaseKey(CharSequence paramCharSequence);
}
