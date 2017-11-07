package org.openqa.selenium.support.events.internal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.support.events.WebDriverEventListener;



















public class EventFiringTouch
  implements TouchScreen
{
  private final WebDriver driver;
  private final WebDriverEventListener dispatcher;
  private final TouchScreen touchScreen;
  
  public EventFiringTouch(WebDriver driver, WebDriverEventListener dispatcher)
  {
    this.driver = driver;
    this.dispatcher = dispatcher;
    touchScreen = ((HasTouchScreen)this.driver).getTouch();
  }
  
  public void singleTap(Coordinates where) {
    touchScreen.singleTap(where);
  }
  
  public void down(int x, int y) {
    touchScreen.down(x, y);
  }
  
  public void up(int x, int y) {
    touchScreen.up(x, y);
  }
  
  public void move(int x, int y) {
    touchScreen.move(x, y);
  }
  
  public void scroll(Coordinates where, int xOffset, int yOffset) {
    touchScreen.scroll(where, xOffset, yOffset);
  }
  
  public void doubleTap(Coordinates where) {
    touchScreen.doubleTap(where);
  }
  
  public void longPress(Coordinates where) {
    touchScreen.longPress(where);
  }
  
  public void scroll(int xOffset, int yOffset) {
    touchScreen.scroll(xOffset, yOffset);
  }
  
  public void flick(int xSpeed, int ySpeed) {
    touchScreen.flick(xSpeed, ySpeed);
  }
  
  public void flick(Coordinates where, int xOffset, int yOffset, int speed) {
    touchScreen.flick(where, xOffset, yOffset, speed);
  }
}
