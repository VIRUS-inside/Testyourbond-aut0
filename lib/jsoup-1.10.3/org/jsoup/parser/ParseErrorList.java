package org.jsoup.parser;

import java.util.ArrayList;



public class ParseErrorList
  extends ArrayList<ParseError>
{
  private static final int INITIAL_CAPACITY = 16;
  private final int maxSize;
  
  ParseErrorList(int initialCapacity, int maxSize)
  {
    super(initialCapacity);
    this.maxSize = maxSize;
  }
  
  boolean canAddError() {
    return size() < maxSize;
  }
  
  int getMaxSize() {
    return maxSize;
  }
  
  public static ParseErrorList noTracking() {
    return new ParseErrorList(0, 0);
  }
  
  public static ParseErrorList tracking(int maxSize) {
    return new ParseErrorList(16, maxSize);
  }
}
