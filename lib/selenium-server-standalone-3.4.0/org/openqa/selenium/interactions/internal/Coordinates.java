package org.openqa.selenium.interactions.internal;

import org.openqa.selenium.Point;

public abstract interface Coordinates
{
  public abstract Point onScreen();
  
  public abstract Point inViewPort();
  
  public abstract Point onPage();
  
  public abstract Object getAuxiliary();
}
