package org.openqa.selenium.interactions.internal;

import org.openqa.selenium.internal.Locatable;



















public abstract class DisplayAction
  extends BaseAction
{
  protected DisplayAction(Locatable locationProvider)
  {
    super(locationProvider);
  }
  
  protected Coordinates getActionLocation() {
    return where == null ? null : where.getCoordinates();
  }
}
