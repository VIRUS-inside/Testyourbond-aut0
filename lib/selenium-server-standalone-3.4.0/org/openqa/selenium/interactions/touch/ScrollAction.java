package org.openqa.selenium.interactions.touch;

import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.internal.TouchAction;
import org.openqa.selenium.internal.Locatable;


















public class ScrollAction
  extends TouchAction
  implements Action
{
  private final int xOffset;
  private final int yOffset;
  
  public ScrollAction(TouchScreen touchScreen, Locatable locationProvider, int x, int y)
  {
    super(touchScreen, locationProvider);
    xOffset = x;
    yOffset = y;
  }
  
  public ScrollAction(TouchScreen touchScreen, int xOffset, int yOffset) {
    super(touchScreen, null);
    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }
  
  public void perform() {
    touchScreen.scroll(getActionLocation(), xOffset, yOffset);
  }
}
