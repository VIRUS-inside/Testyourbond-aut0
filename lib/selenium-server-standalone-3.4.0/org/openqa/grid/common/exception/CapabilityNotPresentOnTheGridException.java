package org.openqa.grid.common.exception;

import java.util.Map;
import org.openqa.selenium.remote.DesiredCapabilities;

















public class CapabilityNotPresentOnTheGridException
  extends GridException
{
  private static final long serialVersionUID = -5382151149204616537L;
  
  public CapabilityNotPresentOnTheGridException(String msg)
  {
    super(msg);
  }
  
  public CapabilityNotPresentOnTheGridException(Map<String, Object> capabilities) {
    super("cannot find : " + new DesiredCapabilities(capabilities));
  }
}
