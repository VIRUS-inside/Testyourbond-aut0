package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.xs.datatypes.XSDouble;

public class DoubleDV
  extends TypeValidator
{
  public DoubleDV() {}
  
  public short getAllowedFacets()
  {
    return 2552;
  }
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    try
    {
      return new XDouble(paramString);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { paramString, "double" });
    }
  }
  
  public int compare(Object paramObject1, Object paramObject2)
  {
    return ((XDouble)paramObject1).compareTo((XDouble)paramObject2);
  }
  
  public boolean isIdentical(Object paramObject1, Object paramObject2)
  {
    if ((paramObject2 instanceof XDouble)) {
      return ((XDouble)paramObject1).isIdentical((XDouble)paramObject2);
    }
    return false;
  }
  
  static boolean isPossibleFP(String paramString)
  {
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      int k = paramString.charAt(j);
      if (((k < 48) || (k > 57)) && (k != 46) && (k != 45) && (k != 43) && (k != 69) && (k != 101)) {
        return false;
      }
    }
    return true;
  }
  
  private static final class XDouble
    implements XSDouble
  {
    private final double value;
    private String canonical;
    
    public XDouble(String paramString)
      throws NumberFormatException
    {
      if (DoubleDV.isPossibleFP(paramString)) {
        value = Double.parseDouble(paramString);
      } else if (paramString.equals("INF")) {
        value = Double.POSITIVE_INFINITY;
      } else if (paramString.equals("-INF")) {
        value = Double.NEGATIVE_INFINITY;
      } else if (paramString.equals("NaN")) {
        value = NaN.0D;
      } else {
        throw new NumberFormatException(paramString);
      }
    }
    
    public boolean equals(Object paramObject)
    {
      if (paramObject == this) {
        return true;
      }
      if (!(paramObject instanceof XDouble)) {
        return false;
      }
      XDouble localXDouble = (XDouble)paramObject;
      if (value == value) {
        return true;
      }
      return (value != value) && (value != value);
    }
    
    public int hashCode()
    {
      if (value == 0.0D) {
        return 0;
      }
      long l = Double.doubleToLongBits(value);
      return (int)(l ^ l >>> 32);
    }
    
    public boolean isIdentical(XDouble paramXDouble)
    {
      if (paramXDouble == this) {
        return true;
      }
      if (value == value) {
        return (value != 0.0D) || (Double.doubleToLongBits(value) == Double.doubleToLongBits(value));
      }
      return (value != value) && (value != value);
    }
    
    private int compareTo(XDouble paramXDouble)
    {
      double d = value;
      if (value < d) {
        return -1;
      }
      if (value > d) {
        return 1;
      }
      if (value == d) {
        return 0;
      }
      if (value != value)
      {
        if (d != d) {
          return 0;
        }
        return 2;
      }
      return 2;
    }
    
    public synchronized String toString()
    {
      if (canonical == null) {
        if (value == Double.POSITIVE_INFINITY)
        {
          canonical = "INF";
        }
        else if (value == Double.NEGATIVE_INFINITY)
        {
          canonical = "-INF";
        }
        else if (value != value)
        {
          canonical = "NaN";
        }
        else if (value == 0.0D)
        {
          canonical = "0.0E1";
        }
        else
        {
          canonical = Double.toString(value);
          if (canonical.indexOf('E') == -1)
          {
            int i = canonical.length();
            char[] arrayOfChar = new char[i + 3];
            canonical.getChars(0, i, arrayOfChar, 0);
            int j = arrayOfChar[0] == '-' ? 2 : 1;
            int k;
            int m;
            int n;
            if ((value >= 1.0D) || (value <= -1.0D))
            {
              k = canonical.indexOf('.');
              for (m = k; m > j; m--) {
                arrayOfChar[m] = arrayOfChar[(m - 1)];
              }
              arrayOfChar[j] = '.';
              while (arrayOfChar[(i - 1)] == '0') {
                i--;
              }
              if (arrayOfChar[(i - 1)] == '.') {
                i++;
              }
              arrayOfChar[(i++)] = 'E';
              n = k - j;
              arrayOfChar[(i++)] = ((char)(n + 48));
            }
            else
            {
              for (k = j + 1; arrayOfChar[k] == '0'; k++) {}
              arrayOfChar[(j - 1)] = arrayOfChar[k];
              arrayOfChar[j] = '.';
              m = k + 1;
              for (n = j + 1; m < i; n++)
              {
                arrayOfChar[n] = arrayOfChar[m];
                m++;
              }
              i -= k - j;
              if (i == j + 1) {
                arrayOfChar[(i++)] = '0';
              }
              arrayOfChar[(i++)] = 'E';
              arrayOfChar[(i++)] = '-';
              int i1 = k - j;
              arrayOfChar[(i++)] = ((char)(i1 + 48));
            }
            canonical = new String(arrayOfChar, 0, i);
          }
        }
      }
      return canonical;
    }
    
    public double getValue()
    {
      return value;
    }
  }
}
