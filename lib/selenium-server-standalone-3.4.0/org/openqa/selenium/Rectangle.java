package org.openqa.selenium;





public class Rectangle
{
  public int x;
  


  public int y;
  


  public int height;
  


  public int width;
  



  public Rectangle(int x, int y, int height, int width)
  {
    this.x = x;
    this.y = y;
    this.height = height;
    this.width = width;
  }
  
  public Rectangle(Point p, Dimension d) {
    x = x;
    y = y;
    height = height;
    width = width;
  }
  
  public int getX() {
    return x;
  }
  
  public int getY() {
    return y;
  }
  
  public void setX(int x) {
    this.x = x;
  }
  
  public void setY(int y) {
    this.y = y;
  }
  
  public int getHeight() {
    return height;
  }
  
  public void setHeight(int height) {
    this.height = height;
  }
  
  public int getWidth() {
    return width;
  }
  
  public void setWidth(int width) {
    this.width = width;
  }
  
  public Point getPoint() {
    return new Point(x, y);
  }
  
  public Dimension getDimension() {
    return new Dimension(width, height);
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    
    Rectangle rectangle = (Rectangle)o;
    
    if (!getPoint().equals(rectangle.getPoint())) {
      return false;
    }
    return getDimension().equals(rectangle.getDimension());
  }
  

  public int hashCode()
  {
    int result = getPoint().hashCode();
    result = 31 * result + getDimension().hashCode();
    return result;
  }
}
