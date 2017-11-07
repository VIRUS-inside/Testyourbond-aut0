package org.openqa.selenium.interactions.touch;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.CompositeAction;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.internal.Locatable;



















public class TouchActions
  extends Actions
{
  protected TouchScreen touchScreen;
  
  public TouchActions(WebDriver driver)
  {
    this(((HasInputDevices)driver).getKeyboard(), ((HasTouchScreen)driver)
      .getTouch());
  }
  
  public TouchActions(Keyboard keyboard, TouchScreen touchScreen) {
    super(keyboard);
    this.touchScreen = touchScreen;
  }
  





  public TouchActions singleTap(WebElement onElement)
  {
    action.addAction(new SingleTapAction(touchScreen, (Locatable)onElement));
    return this;
  }
  







  public TouchActions down(int x, int y)
  {
    action.addAction(new DownAction(touchScreen, x, y));
    return this;
  }
  







  public TouchActions up(int x, int y)
  {
    action.addAction(new UpAction(touchScreen, x, y));
    return this;
  }
  






  public TouchActions move(int x, int y)
  {
    action.addAction(new MoveAction(touchScreen, x, y));
    return this;
  }
  







  public TouchActions scroll(WebElement onElement, int xOffset, int yOffset)
  {
    action.addAction(new ScrollAction(touchScreen, (Locatable)onElement, xOffset, yOffset));
    return this;
  }
  






  public TouchActions doubleTap(WebElement onElement)
  {
    action.addAction(new DoubleTapAction(touchScreen, (Locatable)onElement));
    return this;
  }
  






  public TouchActions longPress(WebElement onElement)
  {
    action.addAction(new LongPressAction(touchScreen, (Locatable)onElement));
    return this;
  }
  







  public TouchActions scroll(int xOffset, int yOffset)
  {
    action.addAction(new ScrollAction(touchScreen, xOffset, yOffset));
    return this;
  }
  







  public TouchActions flick(int xSpeed, int ySpeed)
  {
    action.addAction(new FlickAction(touchScreen, xSpeed, ySpeed));
    return this;
  }
  









  public TouchActions flick(WebElement onElement, int xOffset, int yOffset, int speed)
  {
    action.addAction(new FlickAction(touchScreen, (Locatable)onElement, xOffset, yOffset, speed));
    return this;
  }
}
