package org.apache.xerces.impl.dtd.models;

import org.apache.xerces.xni.QName;

public abstract interface ContentModelValidator
{
  public abstract int validate(QName[] paramArrayOfQName, int paramInt1, int paramInt2);
}
