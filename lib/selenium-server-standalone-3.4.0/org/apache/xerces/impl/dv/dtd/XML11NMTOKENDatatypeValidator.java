package org.apache.xerces.impl.dv.dtd;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.util.XML11Char;

public class XML11NMTOKENDatatypeValidator
  extends NMTOKENDatatypeValidator
{
  public XML11NMTOKENDatatypeValidator() {}
  
  public void validate(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    if (!XML11Char.isXML11ValidNmtoken(paramString)) {
      throw new InvalidDatatypeValueException("NMTOKENInvalid", new Object[] { paramString });
    }
  }
}
