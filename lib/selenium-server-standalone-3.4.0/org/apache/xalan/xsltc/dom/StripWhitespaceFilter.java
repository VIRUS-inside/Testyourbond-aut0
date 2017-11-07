package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;

public abstract interface StripWhitespaceFilter
{
  public abstract boolean stripSpace(DOM paramDOM, int paramInt1, int paramInt2);
}
