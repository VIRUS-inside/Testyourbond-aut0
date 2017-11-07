package org.openqa.selenium.interactions;

import org.openqa.selenium.interactions.internal.Coordinates;

public abstract interface Mouse
{
  public abstract void click(Coordinates paramCoordinates);
  
  public abstract void doubleClick(Coordinates paramCoordinates);
  
  public abstract void mouseDown(Coordinates paramCoordinates);
  
  public abstract void mouseUp(Coordinates paramCoordinates);
  
  public abstract void mouseMove(Coordinates paramCoordinates);
  
  public abstract void mouseMove(Coordinates paramCoordinates, long paramLong1, long paramLong2);
  
  public abstract void contextClick(Coordinates paramCoordinates);
}
