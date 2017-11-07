package org.openqa.selenium.interactions.touch;

import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.internal.TouchAction;
import org.openqa.selenium.internal.Locatable;


















public class LongPressAction
  extends TouchAction
  implements Action
{
  public LongPressAction(TouchScreen touchScreen, Locatable locationProvider)
  {
    super(touchScreen, locationProvider);
  }
  
  public void perform() {
    touchScreen.longPress(getActionLocation());
  }
}
