package org.openqa.selenium.net;








public class OlderWindowsVersionEphemeralPortDetector
  implements EphemeralPortRangeDetector
{
  public OlderWindowsVersionEphemeralPortDetector() {}
  







  public int getLowestEphemeralPort()
  {
    return 1025;
  }
  
  public int getHighestEphemeralPort() {
    return 5000;
  }
}
