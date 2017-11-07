package org.openqa.selenium;








public class Point
{
  public int x;
  





  public int y;
  






  public Point(int x, int y)
  {
    this.x = x;
    this.y = y;
  }
  
  public int getX() {
    return x;
  }
  
  public int getY() {
    return y;
  }
  
  public Point moveBy(int xOffset, int yOffset) {
    return new Point(x + xOffset, y + yOffset);
  }
  
  public boolean equals(Object o)
  {
    if (!(o instanceof Point)) {
      return false;
    }
    
    Point other = (Point)o;
    return (x == x) && (y == y);
  }
  


  public int hashCode()
  {
    return x << 12 + y;
  }
  
  public void move(int newX, int newY) {
    x = newX;
    y = newY;
  }
  
  public String toString()
  {
    return String.format("(%d, %d)", new Object[] { Integer.valueOf(x), Integer.valueOf(y) });
  }
}
