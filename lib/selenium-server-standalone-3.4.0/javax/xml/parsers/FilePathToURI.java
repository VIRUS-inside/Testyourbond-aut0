package javax.xml.parsers;

import java.io.File;
import java.io.UnsupportedEncodingException;

class FilePathToURI
{
  private static boolean[] gNeedEscaping = new boolean[''];
  private static char[] gAfterEscaping1 = new char[''];
  private static char[] gAfterEscaping2 = new char[''];
  private static char[] gHexChs = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
  
  FilePathToURI() {}
  
  public static String filepath2URI(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    char c = File.separatorChar;
    paramString = paramString.replace(c, '/');
    int i = paramString.length();
    StringBuffer localStringBuffer = new StringBuffer(i * 3);
    localStringBuffer.append("file://");
    int j;
    if ((i >= 2) && (paramString.charAt(1) == ':'))
    {
      j = Character.toUpperCase(paramString.charAt(0));
      if ((j >= 65) && (j <= 90)) {
        localStringBuffer.append('/');
      }
    }
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
    return localStringBuffer.toString();
  }
  
  static
  {
    for (int i = 0; i <= 31; i++)
    {
      gNeedEscaping[i] = true;
      gAfterEscaping1[i] = gHexChs[(i >> 4)];
      gAfterEscaping2[i] = gHexChs[(i & 0xF)];
    }
    gNeedEscaping[127] = true;
    gAfterEscaping1[127] = '7';
    gAfterEscaping2[127] = 'F';
    char[] arrayOfChar = { ' ', '<', '>', '#', '%', '"', '{', '}', '|', '\\', '^', '~', '[', ']', '`' };
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
