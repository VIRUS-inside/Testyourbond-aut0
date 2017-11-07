package org.openqa.grid.internal;









public class DefaultTimeSource
  implements TimeSource
{
  public DefaultTimeSource() {}
  







  public long currentTimeInMillis()
  {
    return System.currentTimeMillis();
  }
}
