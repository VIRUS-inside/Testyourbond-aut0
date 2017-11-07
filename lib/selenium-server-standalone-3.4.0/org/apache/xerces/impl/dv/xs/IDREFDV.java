package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.util.XMLChar;

public class IDREFDV
  extends TypeValidator
{
  public IDREFDV() {}
  
  public short getAllowedFacets()
  {
    return 2079;
  }
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    if (!XMLChar.isValidNCName(paramString)) {
      throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { paramString, "NCName" });
    }
    return paramString;
  }
  
  public void checkExtraRules(Object paramObject, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    paramValidationContext.addIdRef((String)paramObject);
  }
}
