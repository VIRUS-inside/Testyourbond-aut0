package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

class PrecisionDecimalDV
  extends TypeValidator
{
  PrecisionDecimalDV() {}
  
  public short getAllowedFacets()
  {
    return 4088;
  }
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    try
    {
      return new XPrecisionDecimal(paramString);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { paramString, "precisionDecimal" });
    }
  }
  
  public int compare(Object paramObject1, Object paramObject2)
  {
    return ((XPrecisionDecimal)paramObject1).compareTo((XPrecisionDecimal)paramObject2);
  }
  
  public int getFractionDigits(Object paramObject)
  {
    return fracDigits;
  }
  
  public int getTotalDigits(Object paramObject)
  {
    return totalDigits;
  }
  
  public boolean isIdentical(Object paramObject1, Object paramObject2)
  {
    if ((!(paramObject2 instanceof XPrecisionDecimal)) || (!(paramObject1 instanceof XPrecisionDecimal))) {
      return false;
    }
    return ((XPrecisionDecimal)paramObject1).isIdentical((XPrecisionDecimal)paramObject2);
  }
  
  static class XPrecisionDecimal
  {
    int sign = 1;
    int totalDigits = 0;
    int intDigits = 0;
    int fracDigits = 0;
    String ivalue = "";
    String fvalue = "";
    int pvalue = 0;
    private String canonical;
    
    XPrecisionDecimal(String paramString)
      throws NumberFormatException
    {
      if (paramString.equals("NaN"))
      {
        ivalue = paramString;
        sign = 0;
      }
      if ((paramString.equals("+INF")) || (paramString.equals("INF")) || (paramString.equals("-INF")))
      {
        ivalue = (paramString.charAt(0) == '+' ? paramString.substring(1) : paramString);
        return;
      }
      initD(paramString);
    }
    
    void initD(String paramString)
      throws NumberFormatException
    {
      int i = paramString.length();
      if (i == 0) {
        throw new NumberFormatException();
      }
      int j = 0;
      int k = 0;
      int m = 0;
      int n = 0;
      if (paramString.charAt(0) == '+')
      {
        j = 1;
      }
      else if (paramString.charAt(0) == '-')
      {
        j = 1;
        sign = -1;
      }
      for (int i1 = j; (i1 < i) && (paramString.charAt(i1) == '0'); i1++) {}
      for (k = i1; (k < i) && (TypeValidator.isDigit(paramString.charAt(k))); k++) {}
      if (k < i)
      {
        if ((paramString.charAt(k) != '.') && (paramString.charAt(k) != 'E') && (paramString.charAt(k) != 'e')) {
          throw new NumberFormatException();
        }
        if (paramString.charAt(k) == '.')
        {
          m = k + 1;
          n = m;
          do
          {
            n++;
            if (n >= i) {
              break;
            }
          } while (TypeValidator.isDigit(paramString.charAt(n)));
        }
        else
        {
          pvalue = Integer.parseInt(paramString.substring(k + 1, i));
        }
      }
      if ((j == k) && (m == n)) {
        throw new NumberFormatException();
      }
      for (int i2 = m; i2 < n; i2++) {
        if (!TypeValidator.isDigit(paramString.charAt(i2))) {
          throw new NumberFormatException();
        }
      }
      intDigits = (k - i1);
      fracDigits = (n - m);
      if (intDigits > 0) {
        ivalue = paramString.substring(i1, k);
      }
      if (fracDigits > 0)
      {
        fvalue = paramString.substring(m, n);
        if (n < i) {
          pvalue = Integer.parseInt(paramString.substring(n + 1, i));
        }
      }
      totalDigits = (intDigits + fracDigits);
    }
    
    public boolean equals(Object paramObject)
    {
      if (paramObject == this) {
        return true;
      }
      if (!(paramObject instanceof XPrecisionDecimal)) {
        return false;
      }
      XPrecisionDecimal localXPrecisionDecimal = (XPrecisionDecimal)paramObject;
      return compareTo(localXPrecisionDecimal) == 0;
    }
    
    private int compareFractionalPart(XPrecisionDecimal paramXPrecisionDecimal)
    {
      if (fvalue.equals(fvalue)) {
        return 0;
      }
      StringBuffer localStringBuffer1 = new StringBuffer(fvalue);
      StringBuffer localStringBuffer2 = new StringBuffer(fvalue);
      truncateTrailingZeros(localStringBuffer1, localStringBuffer2);
      return localStringBuffer1.toString().compareTo(localStringBuffer2.toString());
    }
    
    private void truncateTrailingZeros(StringBuffer paramStringBuffer1, StringBuffer paramStringBuffer2)
    {
      for (int i = paramStringBuffer1.length() - 1; i >= 0; i--)
      {
        if (paramStringBuffer1.charAt(i) != '0') {
          break;
        }
        paramStringBuffer1.deleteCharAt(i);
      }
      for (int j = paramStringBuffer2.length() - 1; j >= 0; j--)
      {
        if (paramStringBuffer2.charAt(j) != '0') {
          break;
        }
        paramStringBuffer2.deleteCharAt(j);
      }
    }
    
    public int compareTo(XPrecisionDecimal paramXPrecisionDecimal)
    {
      if (sign == 0) {
        return 2;
      }
      if ((ivalue.equals("INF")) || (ivalue.equals("INF")))
      {
        if (ivalue.equals(ivalue)) {
          return 0;
        }
        if (ivalue.equals("INF")) {
          return 1;
        }
        return -1;
      }
      if ((ivalue.equals("-INF")) || (ivalue.equals("-INF")))
      {
        if (ivalue.equals(ivalue)) {
          return 0;
        }
        if (ivalue.equals("-INF")) {
          return -1;
        }
        return 1;
      }
      if (sign != sign) {
        return sign > sign ? 1 : -1;
      }
      return sign * compare(paramXPrecisionDecimal);
    }
    
    private int compare(XPrecisionDecimal paramXPrecisionDecimal)
    {
      if ((pvalue != 0) || (pvalue != 0))
      {
        if (pvalue == pvalue) {
          return intComp(paramXPrecisionDecimal);
        }
        if (intDigits + pvalue != intDigits + pvalue) {
          return intDigits + pvalue > intDigits + pvalue ? 1 : -1;
        }
        if (pvalue > pvalue)
        {
          i = pvalue - pvalue;
          localStringBuffer1 = new StringBuffer(ivalue);
          localStringBuffer2 = new StringBuffer(fvalue);
          for (j = 0; j < i; j++) {
            if (j < fracDigits)
            {
              localStringBuffer1.append(fvalue.charAt(j));
              localStringBuffer2.deleteCharAt(j);
            }
            else
            {
              localStringBuffer1.append('0');
            }
          }
          return compareDecimal(localStringBuffer1.toString(), ivalue, localStringBuffer2.toString(), fvalue);
        }
        int i = pvalue - pvalue;
        StringBuffer localStringBuffer1 = new StringBuffer(ivalue);
        StringBuffer localStringBuffer2 = new StringBuffer(fvalue);
        for (int j = 0; j < i; j++) {
          if (j < fracDigits)
          {
            localStringBuffer1.append(fvalue.charAt(j));
            localStringBuffer2.deleteCharAt(j);
          }
          else
          {
            localStringBuffer1.append('0');
          }
        }
        return compareDecimal(ivalue, localStringBuffer1.toString(), fvalue, localStringBuffer2.toString());
      }
      return intComp(paramXPrecisionDecimal);
    }
    
    private int intComp(XPrecisionDecimal paramXPrecisionDecimal)
    {
      if (intDigits != intDigits) {
        return intDigits > intDigits ? 1 : -1;
      }
      return compareDecimal(ivalue, ivalue, fvalue, fvalue);
    }
    
    private int compareDecimal(String paramString1, String paramString2, String paramString3, String paramString4)
    {
      int i = paramString1.compareTo(paramString3);
      if (i != 0) {
        return i > 0 ? 1 : -1;
      }
      if (paramString2.equals(paramString4)) {
        return 0;
      }
      StringBuffer localStringBuffer1 = new StringBuffer(paramString2);
      StringBuffer localStringBuffer2 = new StringBuffer(paramString4);
      truncateTrailingZeros(localStringBuffer1, localStringBuffer2);
      i = localStringBuffer1.toString().compareTo(localStringBuffer2.toString());
      return i > 0 ? 1 : i == 0 ? 0 : -1;
    }
    
    public synchronized String toString()
    {
      if (canonical == null) {
        makeCanonical();
      }
      return canonical;
    }
    
    private void makeCanonical()
    {
      canonical = "TBD by Working Group";
    }
    
    public boolean isIdentical(XPrecisionDecimal paramXPrecisionDecimal)
    {
      if ((ivalue.equals(ivalue)) && ((ivalue.equals("INF")) || (ivalue.equals("-INF")) || (ivalue.equals("NaN")))) {
        return true;
      }
      return (sign == sign) && (intDigits == intDigits) && (fracDigits == fracDigits) && (pvalue == pvalue) && (ivalue.equals(ivalue)) && (fvalue.equals(fvalue));
    }
  }
}
