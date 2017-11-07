package org.apache.xerces.impl.xs.identity;

public class UniqueOrKey
  extends IdentityConstraint
{
  public UniqueOrKey(String paramString1, String paramString2, String paramString3, short paramShort)
  {
    super(paramString1, paramString2, paramString3);
    type = paramShort;
  }
}
