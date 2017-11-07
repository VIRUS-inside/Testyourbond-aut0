package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

class AnyAtomicDV
  extends TypeValidator
{
  AnyAtomicDV() {}
  
  public short getAllowedFacets()
  {
    return 0;
  }
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    return paramString;
  }
}
