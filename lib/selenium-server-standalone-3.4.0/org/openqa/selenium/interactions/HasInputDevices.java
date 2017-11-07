package org.openqa.selenium.interactions;

public abstract interface HasInputDevices
{
  public abstract Keyboard getKeyboard();
  
  public abstract Mouse getMouse();
}
