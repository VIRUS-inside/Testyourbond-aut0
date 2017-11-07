package org.openqa.selenium.interactions.internal;

import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.internal.Locatable;



















public class TouchAction
  extends DisplayAction
{
  protected final TouchScreen touchScreen;
  
  public TouchAction(TouchScreen touchScreen, Locatable locationProvider)
  {
    super(locationProvider);
    this.touchScreen = touchScreen;
  }
}
