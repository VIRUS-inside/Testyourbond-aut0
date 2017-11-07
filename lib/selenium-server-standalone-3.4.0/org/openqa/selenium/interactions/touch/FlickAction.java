package org.openqa.selenium.interactions.touch;

import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.internal.TouchAction;
import org.openqa.selenium.internal.Locatable;



















public class FlickAction
  extends TouchAction
  implements Action
{
  private int xOffset;
  private int yOffset;
  private int speed;
  private int xSpeed;
  private int ySpeed;
  public static final int SPEED_NORMAL = 0;
  public static final int SPEED_FAST = 1;
  
  public FlickAction(TouchScreen touchScreen, Locatable locationProvider, int x, int y, int speed)
  {
    super(touchScreen, locationProvider);
    xOffset = x;
    yOffset = y;
    this.speed = speed;
  }
  
  public FlickAction(TouchScreen touchScreen, int xSpeed, int ySpeed) {
    super(touchScreen, null);
    this.xSpeed = xSpeed;
    this.ySpeed = ySpeed;
  }
  
  public void perform() {
    if (where != null) {
      touchScreen.flick(getActionLocation(), xOffset, yOffset, speed);
    } else {
      touchScreen.flick(xSpeed, ySpeed);
    }
  }
}
