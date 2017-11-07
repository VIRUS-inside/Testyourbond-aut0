package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public class BooleanDV
  extends TypeValidator
{
  public BooleanDV() {}
  
  public short getAllowedFacets()
  {
    return 24;
  }
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    if (("false".equals(paramString)) || ("0".equals(paramString))) {
      return Boolean.FALSE;
    }
    if (("true".equals(paramString)) || ("1".equals(paramString))) {
      return Boolean.TRUE;
    }
    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { paramString, "boolean" });
  }
}
