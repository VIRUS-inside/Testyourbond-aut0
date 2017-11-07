package org.jsoup.parser;


public class ParseError
{
  private int pos;
  private String errorMsg;
  
  ParseError(int pos, String errorMsg)
  {
    this.pos = pos;
    this.errorMsg = errorMsg;
  }
  
  ParseError(int pos, String errorFormat, Object... args) {
    errorMsg = String.format(errorFormat, args);
    this.pos = pos;
  }
  



  public String getErrorMessage()
  {
    return errorMsg;
  }
  



  public int getPosition()
  {
    return pos;
  }
  
  public String toString()
  {
    return pos + ": " + errorMsg;
  }
}
