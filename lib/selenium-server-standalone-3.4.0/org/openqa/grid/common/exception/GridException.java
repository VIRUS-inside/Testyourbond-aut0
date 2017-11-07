package org.openqa.grid.common.exception;









public class GridException
  extends RuntimeException
{
  private static final long serialVersionUID = -3994209521865743841L;
  







  public GridException(String msg)
  {
    super(msg);
  }
  
  public GridException(String msg, Throwable t) {
    super(msg, t);
  }
}
