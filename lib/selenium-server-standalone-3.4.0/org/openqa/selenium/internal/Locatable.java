package org.openqa.selenium.internal;

import org.openqa.selenium.interactions.internal.Coordinates;

public abstract interface Locatable
{
  public abstract Coordinates getCoordinates();
}
