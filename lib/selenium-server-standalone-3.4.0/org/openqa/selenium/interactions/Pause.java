package org.openqa.selenium.interactions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;























public class Pause
  extends Interaction
  implements Encodable
{
  private final Duration duration;
  
  public Pause(InputSource device, Duration duration)
  {
    super(device);
    
    if (duration.isNegative()) {
      throw new IllegalStateException("Duration must be set to 0 or more: " + duration);
    }
    this.duration = duration;
  }
  
  protected boolean isValidFor(SourceType sourceType)
  {
    return true;
  }
  
  public Map<String, Object> encode()
  {
    Map<String, Object> toReturn = new HashMap();
    
    toReturn.put("type", "pause");
    toReturn.put("duration", Long.valueOf(duration.toMillis()));
    
    return toReturn;
  }
}
