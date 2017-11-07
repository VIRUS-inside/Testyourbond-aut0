package javax.xml.xpath;

import javax.xml.namespace.QName;

public abstract interface XPathVariableResolver
{
  public abstract Object resolveVariable(QName paramQName);
}
