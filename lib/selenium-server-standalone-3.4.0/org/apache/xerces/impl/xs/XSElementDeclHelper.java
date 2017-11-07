package org.apache.xerces.impl.xs;

import org.apache.xerces.xni.QName;

public abstract interface XSElementDeclHelper
{
  public abstract XSElementDecl getGlobalElementDecl(QName paramQName);
}
