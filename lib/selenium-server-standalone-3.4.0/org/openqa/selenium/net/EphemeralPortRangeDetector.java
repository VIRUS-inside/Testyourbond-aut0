package org.openqa.selenium.net;

public abstract interface EphemeralPortRangeDetector
{
  public abstract int getLowestEphemeralPort();
  
  public abstract int getHighestEphemeralPort();
}
