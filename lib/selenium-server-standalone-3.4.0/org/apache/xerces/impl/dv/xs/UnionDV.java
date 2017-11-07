package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public class UnionDV
  extends TypeValidator
{
  public UnionDV() {}
  
  public short getAllowedFacets()
  {
    return 2056;
  }
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    return paramString;
  }
}
