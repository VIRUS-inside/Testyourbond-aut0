package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xml.dtm.DTMAxisIterator;

public abstract interface CurrentNodeListFilter
{
  public abstract boolean test(int paramInt1, int paramInt2, int paramInt3, int paramInt4, AbstractTranslet paramAbstractTranslet, DTMAxisIterator paramDTMAxisIterator);
}
