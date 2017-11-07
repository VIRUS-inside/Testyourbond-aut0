package org.apache.xerces.impl.dv.xs;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.xs.datatypes.XSDecimal;

public class DecimalDV
  extends TypeValidator
{
  public DecimalDV() {}
  
  public final short getAllowedFacets()
  {
    return 4088;
  }
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    try
    {
      return new XDecimal(paramString);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { paramString, "decimal" });
    }
  }
  
  public final int compare(Object paramObject1, Object paramObject2)
  {
    return ((XDecimal)paramObject1).compareTo((XDecimal)paramObject2);
  }
  
  public final int getTotalDigits(Object paramObject)
  {
    return totalDigits;
  }
  
  public final int getFractionDigits(Object paramObject)
  {
    return fracDigits;
  }
  
  static class XDecimal
    implements XSDecimal
  {
    int sign = 1;
    int totalDigits = 0;
    int intDigits = 0;
    int fracDigits = 0;
    String ivalue = "";
    String fvalue = "";
    boolean integer = false;
    private String canonical;
    
    XDecimal(String paramString)
      throws NumberFormatException
    {
      initD(paramString);
    }
    
    XDecimal(String paramString, boolean paramBoolean)
      throws NumberFormatException
    {
      if (paramBoolean) {
        initI(paramString);
      } else {
        initD(paramString);
      }
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
        if (paramString.charAt(k) != '.') {
          throw new NumberFormatException();
        }
        m = k + 1;
        n = i;
      }
      if ((j == k) && (m == n)) {
        throw new NumberFormatException();
      }
      while ((n > m) && (paramString.charAt(n - 1) == '0')) {
        n--;
      }
      for (int i2 = m; i2 < n; i2++) {
        if (!TypeValidator.isDigit(paramString.charAt(i2))) {
          throw new NumberFormatException();
        }
      }
      intDigits = (k - i1);
      fracDigits = (n - m);
      totalDigits = (intDigits + fracDigits);
      if (intDigits > 0)
      {
        ivalue = paramString.substring(i1, k);
        if (fracDigits > 0) {
          fvalue = paramString.substring(m, n);
        }
      }
      else if (fracDigits > 0)
      {
        fvalue = paramString.substring(m, n);
      }
      else
      {
        sign = 0;
      }
    }
    
    void initI(String paramString)
      throws NumberFormatException
    {
      int i = paramString.length();
      if (i == 0) {
        throw new NumberFormatException();
      }
      int j = 0;
      int k = 0;
      if (paramString.charAt(0) == '+')
      {
        j = 1;
      }
      else if (paramString.charAt(0) == '-')
      {
        j = 1;
        sign = -1;
      }
      for (int m = j; (m < i) && (paramString.charAt(m) == '0'); m++) {}
      for (k = m; (k < i) && (TypeValidator.isDigit(paramString.charAt(k))); k++) {}
      if (k < i) {
        throw new NumberFormatException();
      }
      if (j == k) {
        throw new NumberFormatException();
      }
      intDigits = (k - m);
      fracDigits = 0;
      totalDigits = intDigits;
      if (intDigits > 0) {
        ivalue = paramString.substring(m, k);
      } else {
        sign = 0;
      }
      integer = true;
    }
    
    public boolean equals(Object paramObject)
    {
      if (paramObject == this) {
        return true;
      }
      if (!(paramObject instanceof XDecimal)) {
        return false;
      }
      XDecimal localXDecimal = (XDecimal)paramObject;
      if (sign != sign) {
        return false;
      }
      if (sign == 0) {
        return true;
      }
      return (intDigits == intDigits) && (fracDigits == fracDigits) && (ivalue.equals(ivalue)) && (fvalue.equals(fvalue));
    }
    
    public int compareTo(XDecimal paramXDecimal)
    {
      if (sign != sign) {
        return sign > sign ? 1 : -1;
      }
      if (sign == 0) {
        return 0;
      }
      return sign * intComp(paramXDecimal);
    }
    
    private int intComp(XDecimal paramXDecimal)
    {
      if (intDigits != intDigits) {
        return intDigits > intDigits ? 1 : -1;
      }
      int i = ivalue.compareTo(ivalue);
      if (i != 0) {
        return i > 0 ? 1 : -1;
      }
      i = fvalue.compareTo(fvalue);
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
      if (sign == 0)
      {
        if (integer) {
          canonical = "0";
        } else {
          canonical = "0.0";
        }
        return;
      }
      if ((integer) && (sign > 0))
      {
        canonical = ivalue;
        return;
      }
      StringBuffer localStringBuffer = new StringBuffer(totalDigits + 3);
      if (sign == -1) {
        localStringBuffer.append('-');
      }
      if (intDigits != 0) {
        localStringBuffer.append(ivalue);
      } else {
        localStringBuffer.append('0');
      }
      if (!integer)
      {
        localStringBuffer.append('.');
        if (fracDigits != 0) {
          localStringBuffer.append(fvalue);
        } else {
          localStringBuffer.append('0');
        }
      }
      canonical = localStringBuffer.toString();
    }
    
    public BigDecimal getBigDecimal()
    {
      if (sign == 0) {
        return new BigDecimal(BigInteger.ZERO);
      }
      return new BigDecimal(toString());
    }
    
    public BigInteger getBigInteger()
      throws NumberFormatException
    {
      if (fracDigits != 0) {
        throw new NumberFormatException();
      }
      if (sign == 0) {
        return BigInteger.ZERO;
      }
      if (sign == 1) {
        return new BigInteger(ivalue);
      }
      return new BigInteger("-" + ivalue);
    }
    
    public long getLong()
      throws NumberFormatException
    {
      if (fracDigits != 0) {
        throw new NumberFormatException();
      }
      if (sign == 0) {
        return 0L;
      }
      if (sign == 1) {
        return Long.parseLong(ivalue);
      }
      return Long.parseLong("-" + ivalue);
    }
    
    public int getInt()
      throws NumberFormatException
    {
      if (fracDigits != 0) {
        throw new NumberFormatException();
      }
      if (sign == 0) {
        return 0;
      }
      if (sign == 1) {
        return Integer.parseInt(ivalue);
      }
      return Integer.parseInt("-" + ivalue);
    }
    
    public short getShort()
      throws NumberFormatException
    {
      if (fracDigits != 0) {
        throw new NumberFormatException();
      }
      if (sign == 0) {
        return 0;
      }
      if (sign == 1) {
        return Short.parseShort(ivalue);
      }
      return Short.parseShort("-" + ivalue);
    }
    
    public byte getByte()
      throws NumberFormatException
    {
      if (fracDigits != 0) {
        throw new NumberFormatException();
      }
      if (sign == 0) {
        return 0;
      }
      if (sign == 1) {
        return Byte.parseByte(ivalue);
      }
      return Byte.parseByte("-" + ivalue);
    }
  }
}
