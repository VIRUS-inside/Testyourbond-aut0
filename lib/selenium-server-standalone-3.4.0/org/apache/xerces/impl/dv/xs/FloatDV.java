package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.xs.datatypes.XSFloat;

public class FloatDV
  extends TypeValidator
{
  public FloatDV() {}
  
  public short getAllowedFacets()
  {
    return 2552;
  }
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    try
    {
      return new XFloat(paramString);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { paramString, "float" });
    }
  }
  
  public int compare(Object paramObject1, Object paramObject2)
  {
    return ((XFloat)paramObject1).compareTo((XFloat)paramObject2);
  }
  
  public boolean isIdentical(Object paramObject1, Object paramObject2)
  {
    if ((paramObject2 instanceof XFloat)) {
      return ((XFloat)paramObject1).isIdentical((XFloat)paramObject2);
    }
    return false;
  }
  
  private static final class XFloat
    implements XSFloat
  {
    private final float value;
    private String canonical;
    
    public XFloat(String paramString)
      throws NumberFormatException
    {
      if (DoubleDV.isPossibleFP(paramString)) {
        value = Float.parseFloat(paramString);
      } else if (paramString.equals("INF")) {
        value = Float.POSITIVE_INFINITY;
      } else if (paramString.equals("-INF")) {
        value = Float.NEGATIVE_INFINITY;
      } else if (paramString.equals("NaN")) {
        value = NaN.0F;
      } else {
        throw new NumberFormatException(paramString);
      }
    }
    
    public boolean equals(Object paramObject)
    {
      if (paramObject == this) {
        return true;
      }
      if (!(paramObject instanceof XFloat)) {
        return false;
      }
      XFloat localXFloat = (XFloat)paramObject;
      if (value == value) {
        return true;
      }
      return (value != value) && (value != value);
    }
    
    public int hashCode()
    {
      return value == 0.0F ? 0 : Float.floatToIntBits(value);
    }
    
    public boolean isIdentical(XFloat paramXFloat)
    {
      if (paramXFloat == this) {
        return true;
      }
      if (value == value) {
        return (value != 0.0F) || (Float.floatToIntBits(value) == Float.floatToIntBits(value));
      }
      return (value != value) && (value != value);
    }
    
    private int compareTo(XFloat paramXFloat)
    {
      float f = value;
      if (value < f) {
        return -1;
      }
      if (value > f) {
        return 1;
      }
      if (value == f) {
        return 0;
      }
      if (value != value)
      {
        if (f != f) {
          return 0;
        }
        return 2;
      }
      return 2;
    }
    
    public synchronized String toString()
    {
      if (canonical == null) {
        if (value == Float.POSITIVE_INFINITY)
        {
          canonical = "INF";
        }
        else if (value == Float.NEGATIVE_INFINITY)
        {
          canonical = "-INF";
        }
        else if (value != value)
        {
          canonical = "NaN";
        }
        else if (value == 0.0F)
        {
          canonical = "0.0E1";
        }
        else
        {
          canonical = Float.toString(value);
          if (canonical.indexOf('E') == -1)
          {
            int i = canonical.length();
            char[] arrayOfChar = new char[i + 3];
            canonical.getChars(0, i, arrayOfChar, 0);
            int j = arrayOfChar[0] == '-' ? 2 : 1;
            int k;
            int m;
            int n;
            if ((value >= 1.0F) || (value <= -1.0F))
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
    
    public float getValue()
    {
      return value;
    }
  }
}
