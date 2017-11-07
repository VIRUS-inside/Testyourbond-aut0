package org.apache.http.message;










public class ParserCursor
{
  private final int lowerBound;
  








  private final int upperBound;
  








  private int pos;
  









  public ParserCursor(int lowerBound, int upperBound)
  {
    if (lowerBound < 0) {
      throw new IndexOutOfBoundsException("Lower bound cannot be negative");
    }
    if (lowerBound > upperBound) {
      throw new IndexOutOfBoundsException("Lower bound cannot be greater then upper bound");
    }
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
    pos = lowerBound;
  }
  
  public int getLowerBound() {
    return lowerBound;
  }
  
  public int getUpperBound() {
    return upperBound;
  }
  
  public int getPos() {
    return pos;
  }
  
  public void updatePos(int pos) {
    if (pos < lowerBound) {
      throw new IndexOutOfBoundsException("pos: " + pos + " < lowerBound: " + lowerBound);
    }
    if (pos > upperBound) {
      throw new IndexOutOfBoundsException("pos: " + pos + " > upperBound: " + upperBound);
    }
    this.pos = pos;
  }
  
  public boolean atEnd() {
    return pos >= upperBound;
  }
  
  public String toString()
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append('[');
    buffer.append(Integer.toString(lowerBound));
    buffer.append('>');
    buffer.append(Integer.toString(pos));
    buffer.append('>');
    buffer.append(Integer.toString(upperBound));
    buffer.append(']');
    return buffer.toString();
  }
}
