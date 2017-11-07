package org.openqa.selenium.support.ui;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;




























public class Duration
{
  private final long time;
  private final TimeUnit unit;
  
  public Duration(long time, TimeUnit unit)
  {
    Preconditions.checkArgument(time >= 0L, "Duration < 0: %d", time);
    Preconditions.checkNotNull(unit);
    this.time = time;
    this.unit = unit;
  }
  
  public boolean equals(Object o)
  {
    if ((o instanceof Duration)) {
      Duration other = (Duration)o;
      return (time == time) && (unit == unit);
    }
    
    return false;
  }
  
  public int hashCode()
  {
    return Objects.hashCode(new Object[] { Long.valueOf(time), unit });
  }
  
  public String toString()
  {
    return String.format("%d %s", new Object[] { Long.valueOf(time), unit });
  }
  





  public long in(TimeUnit unit)
  {
    return unit.convert(time, this.unit);
  }
}
