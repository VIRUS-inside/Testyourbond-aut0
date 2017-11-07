package org.apache.xerces.impl.dv.xs;

import java.io.UnsupportedEncodingException;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.util.URI;
import org.apache.xerces.util.URI.MalformedURIException;

public class AnyURIDV
  extends TypeValidator
{
  private static final URI BASE_URI;
  private static boolean[] gNeedEscaping;
  private static char[] gAfterEscaping1;
  private static char[] gAfterEscaping2;
  private static char[] gHexChs;
  
  public AnyURIDV() {}
  
  public short getAllowedFacets()
  {
    return 2079;
  }
  
  public Object getActualValue(String paramString, ValidationContext paramValidationContext)
    throws InvalidDatatypeValueException
  {
    try
    {
      if (paramString.length() != 0)
      {
        String str = encode(paramString);
        new URI(BASE_URI, str);
      }
    }
    catch (URI.MalformedURIException localMalformedURIException)
    {
      throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[] { paramString, "anyURI" });
    }
    return paramString;
  }
  
  private static String encode(String paramString)
  {
    int i = paramString.length();
    StringBuffer localStringBuffer = new StringBuffer(i * 3);
    int j;
    for (int k = 0; k < i; k++)
    {
      j = paramString.charAt(k);
      if (j >= 128) {
        break;
      }
      if (gNeedEscaping[j] != 0)
      {
        localStringBuffer.append('%');
        localStringBuffer.append(gAfterEscaping1[j]);
        localStringBuffer.append(gAfterEscaping2[j]);
      }
      else
      {
        localStringBuffer.append((char)j);
      }
    }
    if (k < i)
    {
      byte[] arrayOfByte = null;
      try
      {
        arrayOfByte = paramString.substring(k).getBytes("UTF-8");
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        return paramString;
      }
      i = arrayOfByte.length;
      for (k = 0; k < i; k++)
      {
        int m = arrayOfByte[k];
        if (m < 0)
        {
          j = m + 256;
          localStringBuffer.append('%');
          localStringBuffer.append(gHexChs[(j >> 4)]);
          localStringBuffer.append(gHexChs[(j & 0xF)]);
        }
        else if (gNeedEscaping[m] != 0)
        {
          localStringBuffer.append('%');
          localStringBuffer.append(gAfterEscaping1[m]);
          localStringBuffer.append(gAfterEscaping2[m]);
        }
        else
        {
          localStringBuffer.append((char)m);
        }
      }
    }
    if (localStringBuffer.length() != i) {
      return localStringBuffer.toString();
    }
    return paramString;
  }
  
  static
  {
    URI localURI = null;
    try
    {
      localURI = new URI("abc://def.ghi.jkl");
    }
    catch (URI.MalformedURIException localMalformedURIException) {}
    BASE_URI = localURI;
    gNeedEscaping = new boolean[''];
    gAfterEscaping1 = new char[''];
    gAfterEscaping2 = new char[''];
    gHexChs = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    for (int i = 0; i <= 31; i++)
    {
      gNeedEscaping[i] = true;
      gAfterEscaping1[i] = gHexChs[(i >> 4)];
      gAfterEscaping2[i] = gHexChs[(i & 0xF)];
    }
    gNeedEscaping[127] = true;
    gAfterEscaping1[127] = '7';
    gAfterEscaping2[127] = 'F';
    char[] arrayOfChar = { ' ', '<', '>', '"', '{', '}', '|', '\\', '^', '~', '`' };
    int j = arrayOfChar.length;
    for (int m = 0; m < j; m++)
    {
      int k = arrayOfChar[m];
      gNeedEscaping[k] = true;
      gAfterEscaping1[k] = gHexChs[(k >> 4)];
      gAfterEscaping2[k] = gHexChs[(k & 0xF)];
    }
  }
}
