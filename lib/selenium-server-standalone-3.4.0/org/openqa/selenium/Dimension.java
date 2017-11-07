package org.openqa.selenium;








public class Dimension
{
  public final int width;
  





  public final int height;
  






  public Dimension(int width, int height)
  {
    this.width = width;
    this.height = height;
  }
  
  public int getWidth() {
    return width;
  }
  
  public int getHeight() {
    return height;
  }
  
  public boolean equals(Object o)
  {
    if (!(o instanceof Dimension)) {
      return false;
    }
    
    Dimension other = (Dimension)o;
    return (width == width) && (height == height);
  }
  


  public int hashCode()
  {
    return width << 12 + height;
  }
  
  public String toString()
  {
    return String.format("(%d, %d)", new Object[] { Integer.valueOf(width), Integer.valueOf(height) });
  }
}
