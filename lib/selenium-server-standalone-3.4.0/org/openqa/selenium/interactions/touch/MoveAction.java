package org.openqa.selenium.interactions.touch;

import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.internal.TouchAction;


















public class MoveAction
  extends TouchAction
  implements Action
{
  private final int x;
  private final int y;
  
  public MoveAction(TouchScreen touchScreen, int x, int y)
  {
    super(touchScreen, null);
    this.x = x;
    this.y = y;
  }
  
  public void perform() {
    touchScreen.move(x, y);
  }
}
