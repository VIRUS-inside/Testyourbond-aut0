package org.w3c.css.sac;











public class CSSParseException
  extends CSSException
{
  private String uri;
  








  private int lineNumber;
  








  private int columnNumber;
  









  public CSSParseException(String paramString, Locator paramLocator)
  {
    super(paramString);
    code = 2;
    uri = paramLocator.getURI();
    lineNumber = paramLocator.getLineNumber();
    columnNumber = paramLocator.getColumnNumber();
  }
  

















  public CSSParseException(String paramString, Locator paramLocator, Exception paramException)
  {
    super((short)2, paramString, paramException);
    uri = paramLocator.getURI();
    lineNumber = paramLocator.getLineNumber();
    columnNumber = paramLocator.getColumnNumber();
  }
  
















  public CSSParseException(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    super(paramString1);
    code = 2;
    uri = paramString2;
    lineNumber = paramInt1;
    columnNumber = paramInt2;
  }
  





















  public CSSParseException(String paramString1, String paramString2, int paramInt1, int paramInt2, Exception paramException)
  {
    super((short)2, paramString1, paramException);
    uri = paramString2;
    lineNumber = paramInt1;
    columnNumber = paramInt2;
  }
  








  public String getURI()
  {
    return uri;
  }
  







  public int getLineNumber()
  {
    return lineNumber;
  }
  









  public int getColumnNumber()
  {
    return columnNumber;
  }
}
