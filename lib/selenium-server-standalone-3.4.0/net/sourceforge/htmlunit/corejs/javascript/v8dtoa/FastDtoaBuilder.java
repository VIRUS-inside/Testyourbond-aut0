package net.sourceforge.htmlunit.corejs.javascript.v8dtoa;

import java.util.Arrays;






public class FastDtoaBuilder
{
  public FastDtoaBuilder() {}
  
  final char[] chars = new char[25];
  int end = 0;
  int point;
  boolean formatted = false;
  
  void append(char c) {
    chars[(end++)] = c; }
  
  void decreaseLast()
  {
    int tmp10_9 = (end - 1); char[] tmp10_1 = chars;tmp10_1[tmp10_9] = ((char)(tmp10_1[tmp10_9] - '\001'));
  }
  
  public void reset() {
    end = 0;
    formatted = false;
  }
  
  public String toString()
  {
    return "[chars:" + new String(chars, 0, end) + ", point:" + point + "]";
  }
  
  public String format() {
    if (!formatted)
    {
      int firstDigit = chars[0] == '-' ? 1 : 0;
      int decPoint = point - firstDigit;
      if ((decPoint < -5) || (decPoint > 21)) {
        toExponentialFormat(firstDigit, decPoint);
      } else {
        toFixedFormat(firstDigit, decPoint);
      }
      formatted = true;
    }
    return new String(chars, 0, end);
  }
  
  private void toFixedFormat(int firstDigit, int decPoint)
  {
    if (point < end)
    {
      if (decPoint > 0)
      {
        System.arraycopy(chars, point, chars, point + 1, end - point);
        chars[point] = '.';
        end += 1;
      }
      else {
        int target = firstDigit + 2 - decPoint;
        System.arraycopy(chars, firstDigit, chars, target, end - firstDigit);
        
        chars[firstDigit] = '0';
        chars[(firstDigit + 1)] = '.';
        if (decPoint < 0) {
          Arrays.fill(chars, firstDigit + 2, target, '0');
        }
        end += 2 - decPoint;
      }
    } else if (point > end)
    {
      Arrays.fill(chars, end, point, '0');
      end += point - end;
    }
  }
  
  private void toExponentialFormat(int firstDigit, int decPoint) {
    if (end - firstDigit > 1)
    {
      int dot = firstDigit + 1;
      System.arraycopy(chars, dot, chars, dot + 1, end - dot);
      chars[dot] = '.';
      end += 1;
    }
    chars[(end++)] = 'e';
    char sign = '+';
    int exp = decPoint - 1;
    if (exp < 0) {
      sign = '-';
      exp = -exp;
    }
    chars[(end++)] = sign;
    
    int charPos = exp > 9 ? end + 1 : exp > 99 ? end + 2 : end;
    end = (charPos + 1);
    
    for (;;)
    {
      int r = exp % 10;
      chars[(charPos--)] = digits[r];
      exp /= 10;
      if (exp == 0)
        break;
    }
  }
  
  static final char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
}
