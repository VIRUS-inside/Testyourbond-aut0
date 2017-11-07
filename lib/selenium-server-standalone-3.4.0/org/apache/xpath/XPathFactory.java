package org.apache.xpath;

import javax.xml.transform.SourceLocator;
import org.apache.xml.utils.PrefixResolver;

public abstract interface XPathFactory
{
  public abstract XPath create(String paramString, SourceLocator paramSourceLocator, PrefixResolver paramPrefixResolver, int paramInt);
}
