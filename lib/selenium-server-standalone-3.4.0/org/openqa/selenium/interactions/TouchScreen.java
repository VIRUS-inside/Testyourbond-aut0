package org.openqa.selenium.interactions;

import org.openqa.selenium.interactions.internal.Coordinates;

public abstract interface TouchScreen
{
  public abstract void singleTap(Coordinates paramCoordinates);
  
  public abstract void down(int paramInt1, int paramInt2);
  
  public abstract void up(int paramInt1, int paramInt2);
  
  public abstract void move(int paramInt1, int paramInt2);
  
  public abstract void scroll(Coordinates paramCoordinates, int paramInt1, int paramInt2);
  
  public abstract void doubleTap(Coordinates paramCoordinates);
  
  public abstract void longPress(Coordinates paramCoordinates);
  
  public abstract void scroll(int paramInt1, int paramInt2);
  
  public abstract void flick(int paramInt1, int paramInt2);
  
  public abstract void flick(Coordinates paramCoordinates, int paramInt1, int paramInt2, int paramInt3);
}
