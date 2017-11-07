package javax.xml.xpath;

import java.util.List;

public abstract interface XPathFunction
{
  public abstract Object evaluate(List paramList)
    throws XPathFunctionException;
}
