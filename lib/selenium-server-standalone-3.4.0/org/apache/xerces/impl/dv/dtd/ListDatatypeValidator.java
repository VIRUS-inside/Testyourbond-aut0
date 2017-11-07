package org.apache.xerces.impl.dv.dtd;

import java.util.StringTokenizer;
import org.apache.xerces.impl.dv.DatatypeValidator;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public class ListDatatypeValidator
  implements DatatypeValidator
{
  final DatatypeValidator fItemValidator;
  
  public ListDatatypeValidator(DatatypeValidator paramDatatypeValidator)
  {
    fItemValidator = paramDatatypeValidator;
  }
  
  public void validate(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " ");
    int i = localStringTokenizer.countTokens();
    if (i == 0) {
      throw new InvalidDatatypeValueException("EmptyList", null);
    }
    while (localStringTokenizer.hasMoreTokens()) {
      fItemValidator.validate(localStringTokenizer.nextToken(), paramValidationContext);
    }
  }
}
