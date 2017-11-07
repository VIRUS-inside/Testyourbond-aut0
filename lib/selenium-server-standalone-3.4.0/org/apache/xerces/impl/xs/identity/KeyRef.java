package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.xs.XSIDCDefinition;

public class KeyRef
  extends IdentityConstraint
{
  protected final UniqueOrKey fKey;
  
  public KeyRef(String paramString1, String paramString2, String paramString3, UniqueOrKey paramUniqueOrKey)
  {
    super(paramString1, paramString2, paramString3);
    fKey = paramUniqueOrKey;
    type = 2;
  }
  
  public UniqueOrKey getKey()
  {
    return fKey;
  }
  
  public XSIDCDefinition getRefKey()
  {
    return fKey;
  }
}
