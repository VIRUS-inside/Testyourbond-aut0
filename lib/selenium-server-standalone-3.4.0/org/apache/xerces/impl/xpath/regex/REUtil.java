package org.apache.xerces.impl.xpath.regex;

import java.io.PrintStream;
import java.text.CharacterIterator;

public final class REUtil
{
  static final int CACHESIZE = 20;
  static final RegularExpression[] regexCache = new RegularExpression[20];
  
  private REUtil() {}
  
  static final int composeFromSurrogates(int paramInt1, int paramInt2)
  {
    return 65536 + (paramInt1 - 55296 << 10) + paramInt2 - 56320;
  }
  
  static final boolean isLowSurrogate(int paramInt)
  {
    return (paramInt & 0xFC00) == 56320;
  }
  
  static final boolean isHighSurrogate(int paramInt)
  {
    return (paramInt & 0xFC00) == 55296;
  }
  
  static final String decomposeToSurrogates(int paramInt)
  {
    char[] arrayOfChar = new char[2];
    paramInt -= 65536;
    arrayOfChar[0] = ((char)((paramInt >> 10) + 55296));
    arrayOfChar[1] = ((char)((paramInt & 0x3FF) + 56320));
    return new String(arrayOfChar);
  }
  
  static final String substring(CharacterIterator paramCharacterIterator, int paramInt1, int paramInt2)
  {
    char[] arrayOfChar = new char[paramInt2 - paramInt1];
    for (int i = 0; i < arrayOfChar.length; i++) {
      arrayOfChar[i] = paramCharacterIterator.setIndex(i + paramInt1);
    }
    return new String(arrayOfChar);
  }
  
  static final int getOptionValue(int paramInt)
  {
    int i = 0;
    switch (paramInt)
    {
    case 105: 
      i = 2;
      break;
    case 109: 
      i = 8;
      break;
    case 115: 
      i = 4;
      break;
    case 120: 
      i = 16;
      break;
    case 117: 
      i = 32;
      break;
    case 119: 
      i = 64;
      break;
    case 70: 
      i = 256;
      break;
    case 72: 
      i = 128;
      break;
    case 88: 
      i = 512;
      break;
    case 44: 
      i = 1024;
      break;
    }
    return i;
  }
  
  static final int parseOptions(String paramString)
    throws ParseException
  {
    if (paramString == null) {
      return 0;
    }
    int i = 0;
    for (int j = 0; j < paramString.length(); j++)
    {
      int k = getOptionValue(paramString.charAt(j));
      if (k == 0) {
        throw new ParseException("Unknown Option: " + paramString.substring(j), -1);
      }
      i |= k;
    }
    return i;
  }
  
  static final String createOptionString(int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer(9);
    if ((paramInt & 0x100) != 0) {
      localStringBuffer.append('F');
    }
    if ((paramInt & 0x80) != 0) {
      localStringBuffer.append('H');
    }
    if ((paramInt & 0x200) != 0) {
      localStringBuffer.append('X');
    }
    if ((paramInt & 0x2) != 0) {
      localStringBuffer.append('i');
    }
    if ((paramInt & 0x8) != 0) {
      localStringBuffer.append('m');
    }
    if ((paramInt & 0x4) != 0) {
      localStringBuffer.append('s');
    }
    if ((paramInt & 0x20) != 0) {
      localStringBuffer.append('u');
    }
    if ((paramInt & 0x40) != 0) {
      localStringBuffer.append('w');
    }
    if ((paramInt & 0x10) != 0) {
      localStringBuffer.append('x');
    }
    if ((paramInt & 0x400) != 0) {
      localStringBuffer.append(',');
    }
    return localStringBuffer.toString().intern();
  }
  
  static String stripExtendedComment(String paramString)
  {
    int i = paramString.length();
    StringBuffer localStringBuffer = new StringBuffer(i);
    int j = 0;
    int k = 0;
    while (j < i)
    {
      int m = paramString.charAt(j++);
      if ((m == 9) || (m == 10) || (m == 12) || (m == 13) || (m == 32))
      {
        if (k > 0) {
          localStringBuffer.append((char)m);
        }
      }
      else if (m == 35)
      {
        while (j < i)
        {
          m = paramString.charAt(j++);
          if ((m == 13) || (m == 10)) {
            break;
          }
        }
      }
      else
      {
        int n;
        if ((m == 92) && (j < i))
        {
          if (((n = paramString.charAt(j)) == '#') || (n == 9) || (n == 10) || (n == 12) || (n == 13) || (n == 32))
          {
            localStringBuffer.append((char)n);
            j++;
          }
          else
          {
            localStringBuffer.append('\\');
            localStringBuffer.append((char)n);
            j++;
          }
        }
        else if (m == 91)
        {
          k++;
          localStringBuffer.append((char)m);
          if (j < i)
          {
            n = paramString.charAt(j);
            if ((n == 91) || (n == 93))
            {
              localStringBuffer.append((char)n);
              j++;
            }
            else if ((n == 94) && (j + 1 < i))
            {
              n = paramString.charAt(j + 1);
              if ((n == 91) || (n == 93))
              {
                localStringBuffer.append('^');
                localStringBuffer.append((char)n);
                j += 2;
              }
            }
          }
        }
        else
        {
          if ((k > 0) && (m == 93)) {
            k--;
          }
          localStringBuffer.append((char)m);
        }
      }
    }
    return localStringBuffer.toString();
  }
  
  public static void main(String[] paramArrayOfString)
  {
    String str1 = null;
    try
    {
      String str2 = "";
      str3 = null;
      if (paramArrayOfString.length == 0)
      {
        System.out.println("Error:Usage: java REUtil -i|-m|-s|-u|-w|-X regularExpression String");
        System.exit(0);
      }
      for (i = 0; i < paramArrayOfString.length; i++) {
        if ((paramArrayOfString[i].length() == 0) || (paramArrayOfString[i].charAt(0) != '-'))
        {
          if (str1 == null) {
            str1 = paramArrayOfString[i];
          } else if (str3 == null) {
            str3 = paramArrayOfString[i];
          } else {
            System.err.println("Unnecessary: " + paramArrayOfString[i]);
          }
        }
        else if (paramArrayOfString[i].equals("-i")) {
          str2 = str2 + "i";
        } else if (paramArrayOfString[i].equals("-m")) {
          str2 = str2 + "m";
        } else if (paramArrayOfString[i].equals("-s")) {
          str2 = str2 + "s";
        } else if (paramArrayOfString[i].equals("-u")) {
          str2 = str2 + "u";
        } else if (paramArrayOfString[i].equals("-w")) {
          str2 = str2 + "w";
        } else if (paramArrayOfString[i].equals("-X")) {
          str2 = str2 + "X";
        } else {
          System.err.println("Unknown option: " + paramArrayOfString[i]);
        }
      }
      RegularExpression localRegularExpression = new RegularExpression(str1, str2);
      System.out.println("RegularExpression: " + localRegularExpression);
      Match localMatch = new Match();
      localRegularExpression.matches(str3, localMatch);
      for (int k = 0; k < localMatch.getNumberOfGroups(); k++)
      {
        if (k == 0) {
          System.out.print("Matched range for the whole pattern: ");
        } else {
          System.out.print("[" + k + "]: ");
        }
        if (localMatch.getBeginning(k) < 0)
        {
          System.out.println("-1");
        }
        else
        {
          System.out.print(localMatch.getBeginning(k) + ", " + localMatch.getEnd(k) + ", ");
          System.out.println("\"" + localMatch.getCapturedText(k) + "\"");
        }
      }
    }
    catch (ParseException localParseException)
    {
      String str3;
      int i;
      if (str1 == null)
      {
        localParseException.printStackTrace();
      }
      else
      {
        System.err.println("org.apache.xerces.utils.regex.ParseException: " + localParseException.getMessage());
        str3 = "        ";
        System.err.println(str3 + str1);
        i = localParseException.getLocation();
        if (i >= 0)
        {
          System.err.print(str3);
          for (int j = 0; j < i; j++) {
            System.err.print("-");
          }
          System.err.println("^");
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public static RegularExpression createRegex(String paramString1, String paramString2)
    throws ParseException
  {
    Object localObject1 = null;
    int i = parseOptions(paramString2);
    synchronized (regexCache)
    {
      for (int j = 0; j < 20; j++)
      {
        RegularExpression localRegularExpression = regexCache[j];
        if (localRegularExpression == null)
        {
          j = -1;
          break;
        }
        if (localRegularExpression.equals(paramString1, i))
        {
          localObject1 = localRegularExpression;
          break;
        }
      }
      if (localObject1 != null)
      {
        if (j != 0)
        {
          System.arraycopy(regexCache, 0, regexCache, 1, j);
          regexCache[0] = localObject1;
        }
      }
      else
      {
        localObject1 = new RegularExpression(paramString1, paramString2);
        System.arraycopy(regexCache, 0, regexCache, 1, 19);
        regexCache[0] = localObject1;
      }
    }
    return localObject1;
  }
  
  public static boolean matches(String paramString1, String paramString2)
    throws ParseException
  {
    return createRegex(paramString1, null).matches(paramString2);
  }
  
  public static boolean matches(String paramString1, String paramString2, String paramString3)
    throws ParseException
  {
    return createRegex(paramString1, paramString2).matches(paramString3);
  }
  
  public static String quoteMeta(String paramString)
  {
    int i = paramString.length();
    StringBuffer localStringBuffer = null;
    for (int j = 0; j < i; j++)
    {
      int k = paramString.charAt(j);
      if (".*+?{[()|\\^$".indexOf(k) >= 0)
      {
        if (localStringBuffer == null)
        {
          localStringBuffer = new StringBuffer(j + (i - j) * 2);
          if (j > 0) {
            localStringBuffer.append(paramString.substring(0, j));
          }
        }
        localStringBuffer.append('\\');
        localStringBuffer.append((char)k);
      }
      else if (localStringBuffer != null)
      {
        localStringBuffer.append((char)k);
      }
    }
    return localStringBuffer != null ? localStringBuffer.toString() : paramString;
  }
  
  static void dumpString(String paramString)
  {
    for (int i = 0; i < paramString.length(); i++)
    {
      System.out.print(Integer.toHexString(paramString.charAt(i)));
      System.out.print(" ");
    }
    System.out.println();
  }
}
