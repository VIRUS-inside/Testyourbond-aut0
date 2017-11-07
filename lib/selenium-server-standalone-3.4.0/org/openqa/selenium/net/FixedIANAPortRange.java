package org.openqa.selenium.net;








public class FixedIANAPortRange
  implements EphemeralPortRangeDetector
{
  public FixedIANAPortRange() {}
  







  public int getLowestEphemeralPort()
  {
    return 49152;
  }
  
  public int getHighestEphemeralPort() {
    return 65535;
  }
}
