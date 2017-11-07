package org.apache.regexp;



































































public class RESyntaxException
  extends Exception
{
  public RESyntaxException(String paramString)
  {
    super("Syntax error: " + paramString);
  }
}
